package com.asgab.web.process;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.asgab.core.JCaptcha;
import com.asgab.entity.Mail;
import com.asgab.entity.PayTranAttachement;
import com.asgab.entity.PayTranHeader;
import com.asgab.entity.Process;
import com.asgab.repository.PayTranAttachmentMapper;
import com.asgab.service.mail.MailService;
import com.asgab.service.paytran.BlockTradeService;
import com.asgab.service.paytran.PayTranHeaderService;
import com.asgab.service.process.ProcessService;
import com.asgab.util.CommonUtil;
import com.asgab.util.Cryptogram;
import com.asgab.web.AutoEmailNotification;

@Controller
@RequestMapping(value = "/process")
public class ProcessController {
  // 1 找不到客户资料发邮件给AM
  // 2 找到客户资料发邮件给财务
  // 3 财务不同意发邮件给AM和客户重新修改资料
  // 4 财务同意发邮件给AM
  // 5 AM同意 发邮件给客户,入款完成

  @Autowired
  private ProcessService processService;

  @Autowired
  private MailService mailService;

  @Autowired
  private PayTranHeaderService payTranHeaderService;
  
  @Resource
  private BlockTradeService blockTradeService;

  @Autowired
  private PayTranAttachmentMapper payTranAttachmentMapper;

  // 去财务拒绝理由页面,
  @RequestMapping(value = "toFinanceOpin/{opin}/{encodedId}/{randomKey}/{randomIdentification}", method = RequestMethod.GET)
  public String toFinanceOpin(@PathVariable("opin") String opin, @PathVariable("encodedId") String encodedId,
      @PathVariable("randomKey") String randomKey, @PathVariable("randomIdentification") String randomIdentification, Model model,
      HttpServletRequest request, RedirectAttributes redirectAttributes) {
    Process process = checkAccessable(encodedId, randomKey, randomIdentification);
    if (process != null && checkPaytranStatus(process, CommonUtil.STATUS_NEW)) {
      model.addAttribute("process", process);
      model.addAttribute("showDescription", request.getParameter("showDescription"));
      if ("confirm".equalsIgnoreCase(opin)) {
        model.addAttribute("action", "check");
      } else {
        model.addAttribute("action", "reject");
      }
    } else {
      String message = getConfirmedMessage(request, false);
      redirectAttributes.addFlashAttribute("message", message);
      redirectAttributes.addFlashAttribute("error", "error");
      return "redirect:/process/message";
    }
    return "process/toFinanceCheck";
  }

  // 财务检查通过
  @RequestMapping(value = "check", method = RequestMethod.POST)
  public String check(Process process, RedirectAttributes redirectAttributes, HttpServletRequest request) {
    Process processDb = checkAccessable(process.getEncodedId() + "", process.getRandomKey(), process.getRandomIdentification());
    if (processDb != null && checkPaytranStatus(processDb, CommonUtil.STATUS_NEW)) {
      Mail mail = null;
      try {
        // 修改状态
        processDb.setIpAddr(getRemoteAddress(request));
        // 添加备注
        processDb.setDescription(process.getDescription());
        processDb.setRealAddAmount(process.getRealAddAmount());
        mail = processService.check(processDb, processDb.getPayTranNum());
        // 如果有附件,添加check附件
        String[] attachments = request.getParameterValues("attachments");
        if (attachments != null && attachments.length > 0) {
          for (String attachmentId : attachments) {
            PayTranAttachement tmpAtta = payTranAttachmentMapper.getById(Long.parseLong(attachmentId));
            tmpAtta.setProcessId(processDb.getProcessId());
            payTranAttachmentMapper.update(tmpAtta);
          }
        }
      } catch (Exception ex) {
        ex.printStackTrace();
        // 报错
        redirectAttributes.addFlashAttribute("message", getSysError(request));
        redirectAttributes.addFlashAttribute("error", "error");
        return "redirect:/process/message";
      }
      // check通过
      redirectAttributes.addFlashAttribute("message", getConfirmedMessage(request, true));

      if (mail != null) {
        boolean sendResult = mailService.send(mail);
        mailService.updateMailResult(mail, sendResult);
      }

    } else {
      // 链接过期
      redirectAttributes.addFlashAttribute("message", getConfirmedMessage(request, false));
      redirectAttributes.addFlashAttribute("error", "error");
    }

    return "redirect:/process/message";
  }

  // 3 财务检查不通过
  @RequestMapping(value = "reject", method = RequestMethod.POST)
  public String reject(Process process, RedirectAttributes redirectAttributes, HttpServletRequest request) {
    Process processDB = checkAccessable(process.getEncodedId() + "", process.getRandomKey(), process.getRandomIdentification());

    if (processDB != null && checkPaytranStatus(processDB, CommonUtil.STATUS_NEW)) {
      Mail mail = null;
      try {
        // reject,修改状态
        processDB.setIpAddr(getRemoteAddress(request));
        mail = processService.reject(processDB, processDB.getPayTranNum(), process.getDescription(), CommonUtil.STATUS_REJECT);
      } catch (Exception e) {
        e.printStackTrace();
        redirectAttributes.addFlashAttribute("message", getSysError(request));
        redirectAttributes.addFlashAttribute("error", "error");
        return "redirect:/process/message";
      }
      redirectAttributes.addFlashAttribute("paytranNum", processDB.getPayTranNum());
      redirectAttributes.addFlashAttribute("message", CommonUtil.i18nStr(request, "交易被拒绝, 交易编号:", "Transaction Rejected, Transaction Number:"));

      if (mail != null) {
        boolean sendResult = mailService.send(mail);
        mailService.updateMailResult(mail, sendResult);
      }

    } else {
      redirectAttributes.addFlashAttribute("message", getConfirmedMessage(request, false));
      redirectAttributes.addFlashAttribute("error", "error");
    }

    return "redirect:/process/message";
  }

  // 财务第二次操作 去拒绝页面
  @RequestMapping(value = "toFinanceReject/{encodedId}/{randomKey}/{randomIdentification}", method = RequestMethod.GET)
  public String toFinanceReject(@PathVariable("encodedId") String encodedId, @PathVariable("randomKey") String randomKey,
      @PathVariable("randomIdentification") String randomIdentification, Model model, RedirectAttributes redirectAttributes,
      HttpServletRequest request) {
    Process processDB = checkAccessable(encodedId, randomKey, randomIdentification);
    if (processDB != null && (checkPaytranStatus(processDB, CommonUtil.STATUS_CHECK) || checkPaytranStatus(processDB, CommonUtil.STATUS_AM_UPLOAD) )) {
      try {
        model.addAttribute("process", processDB);
        model.addAttribute("action", "financeReject");
      } catch (Exception e) {
        e.printStackTrace();
        redirectAttributes.addFlashAttribute("message", getSysError(request));
        redirectAttributes.addFlashAttribute("error", "error");
        return "redirect:/process/message";
      }
    } else {
      String message = getConfirmedMessage(request, false);
      redirectAttributes.addFlashAttribute("message", message);
      redirectAttributes.addFlashAttribute("error", "error");
      return "redirect:/process/message";
    }
    return "process/toFinanceCheck";
  }

  // 财务第二次 confirm 拒绝
  @RequestMapping(value = "financeReject", method = RequestMethod.POST)
  public String financeReject(Process process, RedirectAttributes redirectAttributes, HttpServletRequest request) {
    Process processDB = checkAccessable(process.getEncodedId() + "", process.getRandomKey(), process.getRandomIdentification());
    if (processDB != null && (checkPaytranStatus(processDB, CommonUtil.STATUS_CHECK) || checkPaytranStatus(processDB, CommonUtil.STATUS_AM_UPLOAD) )) {
      Mail mail = null;
      try {
        // reject,修改状态
        processDB.setIpAddr(getRemoteAddress(request));
        mail = processService.reject(processDB, processDB.getPayTranNum(), process.getDescription(), CommonUtil.STATUS_FINANCE_REJECT);
      } catch (Exception e) {
        e.printStackTrace();
        redirectAttributes.addFlashAttribute("message", getSysError(request));
        redirectAttributes.addFlashAttribute("error", "error");
        return "redirect:/process/message";
      }
      redirectAttributes.addFlashAttribute("paytranNum", processDB.getPayTranNum());
      redirectAttributes.addFlashAttribute("message", CommonUtil.i18nStr(request, "交易被拒绝, 交易编号:", "Transaction Rejected, Transaction Number:"));

      if (mail != null) {
        boolean sendResult = mailService.send(mail);
        mailService.updateMailResult(mail, sendResult);
      }


    } else {
      redirectAttributes.addFlashAttribute("message", getConfirmedMessage(request, false));
      redirectAttributes.addFlashAttribute("error", "error");
    }
    return "redirect:/process/message";
  }


  // 去第二次财务确认的页面, 因为之前不需要填写内容再提交,导致url被占用.requestMapping和方法名不一致
  @RequestMapping(value = "financeConfirm/{encodedId}/{randomKey}/{randomIdentification}", method = RequestMethod.GET)
  public String toFinanceConfirm(@PathVariable("encodedId") String encodedId, @PathVariable("randomKey") String randomKey,
      @PathVariable("randomIdentification") String randomIdentification, RedirectAttributes redirectAttributes, HttpServletRequest request,
      Model model) {
    Process process = checkAccessable(encodedId, randomKey, randomIdentification);
    if (process != null && (checkPaytranStatus(process, CommonUtil.STATUS_CHECK) || checkPaytranStatus(process, CommonUtil.STATUS_AM_UPLOAD)) ) {
      model.addAttribute("process", process);
      model.addAttribute("action", "financeConfirmSubmit");
    } else {
      String message = getConfirmedMessage(request, false);
      redirectAttributes.addFlashAttribute("message", message);
      redirectAttributes.addFlashAttribute("error", "error");
      return "redirect:/process/message";
    }
    return "process/toFinanceCheck";
  }


  // 第二次财务确认提交
  @RequestMapping(value = "financeConfirmSubmit", method = RequestMethod.POST)
  public String financeConfirm(Process process, RedirectAttributes redirectAttributes, HttpServletRequest request) {
    Process processDb = checkAccessable(process.getEncodedId() + "", process.getRandomKey(), process.getRandomIdentification());
    if (processDb != null && (checkPaytranStatus(processDb, CommonUtil.STATUS_CHECK) || checkPaytranStatus(processDb, CommonUtil.STATUS_AM_UPLOAD))) {
      Mail mail = null;
      try {
        // 修改状态
        processDb.setIpAddr(getRemoteAddress(request));
        // 添加备注
        processDb.setDescription(process.getDescription());
        mail = processService.financeConfirm(processDb, processDb.getPayTranNum());
        // 如果有附件,添加check附件
        String[] attachments = request.getParameterValues("attachments");
        if (attachments != null && attachments.length > 0) {
          for (String attachmentId : attachments) {
            PayTranAttachement tmpAtta = payTranAttachmentMapper.getById(Long.parseLong(attachmentId));
            tmpAtta.setProcessId(processDb.getProcessId());
            payTranAttachmentMapper.update(tmpAtta);
          }
        }
      } catch (Exception ex) {
        ex.printStackTrace();
        redirectAttributes.addFlashAttribute("message", getSysError(request));
        redirectAttributes.addFlashAttribute("error", "error");
        return "redirect:/process/message";
      }
      redirectAttributes.addFlashAttribute("message", getConfirmedMessage(request, true));

      if (mail != null) {
        boolean sendResult = mailService.send(mail);
        mailService.updateMailResult(mail, sendResult);
      }


    } else {
      redirectAttributes.addFlashAttribute("message", getConfirmedMessage(request, false));
      redirectAttributes.addFlashAttribute("error", "error");
    }
    return "redirect:/process/message";
  }

  // AM确认
  @RequestMapping(value = "opsConfirm/{encodedId}/{randomKey}/{randomIdentification}", method = RequestMethod.GET)
  public String opsConfirm(@PathVariable("encodedId") String encodedId, @PathVariable("randomKey") String randomKey,
      @PathVariable("randomIdentification") String randomIdentification, RedirectAttributes redirectAttributes, HttpServletRequest request) {
    Process processDB = checkAccessable(encodedId, randomKey, randomIdentification);
    if (processDB != null && checkPaytranStatus(processDB, CommonUtil.STATUS_FINANCE_CONFIRM)) {
      Mail mail = null;
      try {
        processDB.setIpAddr(getRemoteAddress(request));
        mail = processService.opsConfirm(processDB, processDB.getPayTranNum());
        // 页面录入的邮箱
        PayTranHeader payTranHeader = payTranHeaderService.get(processDB.getPayTranNum());
        Mail inputedMail = new Mail();
        inputedMail.setCreateDate(new Date());
        inputedMail.setReceiver(payTranHeader.getEmail());
        inputedMail.getReceivers().add(payTranHeader.getEmail());
        inputedMail.setSubject("流程完成");
        inputedMail.setTemplate(AutoEmailNotification.EMAIL_TEMPLATE_NAME_5);
        inputedMail.setPaytranNum(payTranHeader.getTranNum());
        mailService.save(inputedMail);
        boolean inputedMailResult = mailService.send(inputedMail);
        mailService.updateMailResult(inputedMail, inputedMailResult);
      } catch (Exception e) {
        e.printStackTrace();
        redirectAttributes.addFlashAttribute("message", getSysError(request));
        redirectAttributes.addFlashAttribute("error", "error");
        return "redirect:/process/message";
      }
      redirectAttributes.addFlashAttribute("message", getConfirmedMessage(request, true));

      if (mail != null) {
        boolean sendResult = mailService.send(mail);
        mailService.updateMailResult(mail, sendResult);
      }

    } else {
      redirectAttributes.addFlashAttribute("message", getConfirmedMessage(request, false));
      redirectAttributes.addFlashAttribute("error", "error");
    }

    return "redirect:/process/message";
  }

  //去AM上传客户消费证明页面
  @RequestMapping(value = "blockTrade/{encodedId}/{randomKey}/{randomIdentification}", method = RequestMethod.GET)
  public String blockTrade(@PathVariable("encodedId") String encodedId, @PathVariable("randomKey") String randomKey,
	      @PathVariable("randomIdentification") String randomIdentification,Model model, RedirectAttributes redirectAttributes, HttpServletRequest request){
	Process processDB = checkAccessable(encodedId, randomKey, randomIdentification);
    if (processDB != null && checkPaytranStatus(processDB, CommonUtil.STATUS_CHECK)) {//STATUS_CHECK
      model.addAttribute("process", processDB);
      return "process/blockTrade";
    } else {
      redirectAttributes.addFlashAttribute("message", getConfirmedMessage(request, false));
      redirectAttributes.addFlashAttribute("error", "error");
      return "redirect:/process/message";
    }
	//return "redirect:/process/message";
  }

  //AM上传客户消费证明提交页面
 @RequestMapping(value = "blockTradeCheck", method = RequestMethod.POST)
 public String blockTradeCheck(Process process, String amount30, String amount15, RedirectAttributes redirectAttributes, HttpServletRequest request) {
   Process processDb = checkAccessable(process.getEncodedId() + "", process.getRandomKey(), process.getRandomIdentification());
   if (processDb != null && checkPaytranStatus(processDb, CommonUtil.STATUS_CHECK)) {//STATUS_CHECK
     Mail mail = null;
     try {
       // 修改状态
       processDb.setIpAddr(getRemoteAddress(request));
       // 添加备注
       if ((amount30.length() > 0 && amount15.length() > 0) || (amount30.length() == 0 && amount15.length() == 0)){
	     redirectAttributes.addFlashAttribute("message", getConfirmedMessage(request, false));
	     redirectAttributes.addFlashAttribute("error", "error");
	     return "redirect:/process/message";
       }
       double amount30Double = string2Double(amount30);
       double amount15Double = string2Double(amount15);
       if ((amount30Double > 0 && amount15Double > 0) || (amount30Double <= 0 && amount15Double <= 0)) {
    	 redirectAttributes.addFlashAttribute("message", getConfirmedMessage(request, false));
	     redirectAttributes.addFlashAttribute("error", "error");
	     return "redirect:/process/message";
       }
         blockTradeService.insert(amount30Double, amount15Double, processDb.getPayTranNum(), processDb.getProcessId());
       mail = processService.amSupportDone(processDb, processDb.getPayTranNum());
       // 如果有附件,添加AM上传附件
       String[] attachments = request.getParameterValues("attachments");
       if (attachments != null && attachments.length > 0) {
         for (String attachmentId : attachments) {
           PayTranAttachement tmpAtta = payTranAttachmentMapper.getById(Long.parseLong(attachmentId));
           tmpAtta.setProcessId(processDb.getProcessId());
           payTranAttachmentMapper.update(tmpAtta);
         }
       }
     } catch (Exception ex) {
       ex.printStackTrace();
       // 报错
       redirectAttributes.addFlashAttribute("message", getSysError(request));
       redirectAttributes.addFlashAttribute("error", "error");
       return "redirect:/process/message";
     }
     // 客户消费证明填写完成
     redirectAttributes.addFlashAttribute("message", getConfirmedMessage(request, true));

     if (mail != null) {
       boolean sendResult = mailService.send(mail);
       mailService.updateMailResult(mail, sendResult);
     }

   } else {
     // 链接过期
     redirectAttributes.addFlashAttribute("message", getConfirmedMessage(request, false));
     redirectAttributes.addFlashAttribute("error", "error");
   }

   return "redirect:/process/message";
 }
 
 private double string2Double(String amount){
  double amountDouble;
  try{
	if (StringUtils.isBlank(amount)){
		return 0;
	}
	amount = amount.replaceAll("[^0-9.]","");
	String[] splitArray = amount.split("\\.");
	if(splitArray.length > 2){
		amount = splitArray[0]+"."+splitArray[1];
	}
	BigDecimal bd = new BigDecimal(amount);
	amountDouble = bd.doubleValue();
  }
  catch(Exception e){
	amountDouble = 0;
  }
  return amountDouble;
 }

  @RequestMapping(value = "message", method = RequestMethod.GET)
  public String message() {
    return "process/message";
  }

  /**
   * 验证是否合法
   * 
   * @param encodedId 这里是加密的id
   * @param randomKey
   * @return
   */
  private Process checkAccessable(String encodedId, String randomKey, String randomIdentification) {
    Process process = null;
    Long processId = Long.parseLong(Cryptogram.decodeId(encodedId));
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("processId", processId);
    map.put("randomKey", randomKey);
    map.put("randomIdentification", randomIdentification);
    List<Process> processes = processService.get(map);
    if (processes != null && processes.size() == 1) {
      process = processes.get(0);
    }
    return process;
  }

  private String getConfirmedMessage(HttpServletRequest request, boolean isSuccess) {
    String message = "";
    if (isSuccess) {
      message = "zh".equalsIgnoreCase(request.getLocale().getLanguage()) ? "感谢您的审批" : "thanks for your approving";
    } else {
      message = "zh".equalsIgnoreCase(request.getLocale().getLanguage()) ? "链接已过期或不是有效链接" : "The link has expired or illegal";
    }
    return message;
  }

  private String getSysError(HttpServletRequest request) {
    return "zh".equalsIgnoreCase(request.getLocale().getLanguage()) ? "系统出错" : "System Error";
  }

  /**
   * 检查当前的状态是否符合审批状态条件
   * 
   * @param process
   * @param statusNew
   * @return
   */
  private boolean checkPaytranStatus(Process process, Character status) {
    boolean flag = false;
    PayTranHeader payTranHeader = payTranHeaderService.get(process.getPayTranNum());
    if (payTranHeader != null && payTranHeader.getStatus() == status) {
      flag = true;
    }
    return flag;
  }

  public String getRemoteAddress(HttpServletRequest request) {
    String ip = request.getHeader("x-forwarded-for");
    if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
      ip = request.getRemoteAddr();
    }
    return ip;
  }

  @RequestMapping(value = "processBill/{paytranNum}", method = RequestMethod.POST)
  public String processBill(@PathVariable("paytranNum") Long paytranNum, HttpServletRequest request, Model model) {
    if (JCaptcha.validateResponse(request, request.getParameter("searchPaytranValidCode"))) {
      PayTranHeader payTranHeader = payTranHeaderService.get(paytranNum);
      if (payTranHeader != null) {
        model.addAttribute("payTranHeader", payTranHeader);
      } else {
        model.addAttribute("searchError", true);
        if ("zh".equalsIgnoreCase(request.getLocale().getLanguage())) {
        } else {
          model.addAttribute("searchErrorEN", true);
        }
      }
    } else {
      model.addAttribute("validError", true);
    }
    return "paytran/processBill";
  }

}
