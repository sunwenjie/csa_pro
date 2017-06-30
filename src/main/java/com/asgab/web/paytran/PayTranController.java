package com.asgab.web.paytran;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.asgab.core.JCaptcha;
import com.asgab.core.pagination.Page;
import com.asgab.entity.ExchangeRate;
import com.asgab.entity.Mail;
import com.asgab.entity.PayTranAttachement;
import com.asgab.entity.PayTranDetail;
import com.asgab.entity.PayTranHeader;
import com.asgab.entity.PaymentPurpose;
import com.asgab.entity.Process;
import com.asgab.repository.CustMasterMapper;
import com.asgab.repository.PayTranAttachmentMapper;
import com.asgab.repository.ProcessMapper;
import com.asgab.service.account.ShiroDbRealm.ShiroUser;
import com.asgab.service.exchangeRate.ExchangeRateService;
import com.asgab.service.mail.MailService;
import com.asgab.service.paymentPurpose.PaymentPurposeService;
import com.asgab.service.paytran.PayTranHeaderService;
import com.asgab.service.process.ProcessService;
import com.asgab.util.CommonUtil;
import com.asgab.util.JsonMapper;
import com.asgab.util.Servlets;
import com.asgab.web.AutoEmailNotification;

@Controller
@RequestMapping(value = "/paytran")
public class PayTranController {

  private static final String PAGE_SIZE = "10";

  @Autowired
  private PayTranHeaderService payTranHeaderService;

  @Resource
  private PayTranAttachmentMapper payTranAttachmentMapper;

  @Resource
  private CustMasterMapper custMasterMapper;

  @Autowired
  private ExchangeRateService exchangeRateService;

  @Autowired
  private PaymentPurposeService paymentPurposeService;

  @Resource
  private ProcessMapper processMapper;

  @Autowired
  private ProcessService processService;

  @Autowired
  private MailService mailService;

  @RequestMapping(method = RequestMethod.GET)
  public String List(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
      @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
      @RequestParam(value = "sort", defaultValue = "trannum desc") String sort, ServletRequest request, Model model) {

    Map<String, Object> params = new HashMap<String, Object>();
    if (StringUtils.isNotBlank(request.getParameter("email"))) {
      params.put("email", request.getParameter("email"));
    }
    if (StringUtils.isNotBlank(request.getParameter("tranStartDate"))) {
      params.put("tranStartDate", request.getParameter("tranStartDate"));
    }
    if (StringUtils.isNotBlank(request.getParameter("tranEndDate"))) {
      params.put("tranEndDate", request.getParameter("tranEndDate"));
    }
    if (StringUtils.isNotBlank(request.getParameter("tranNum"))) {
      params.put("tranNum", request.getParameter("tranNum"));
    }
    if (StringUtils.isNotBlank(request.getParameter("status"))) {
      params.put("status", request.getParameter("status"));
    }
    if (StringUtils.isNotBlank(request.getParameter("bdusername"))) {
      params.put("bdusername", request.getParameter("bdusername"));
    }
    model.addAttribute("search", Servlets.encodeParameterString(params));
    params.put("sort", sort);
    Page<PayTranHeader> page = new Page<PayTranHeader>(pageNumber, pageSize, sort, params);
    Page<PayTranHeader> pages = payTranHeaderService.getAllPayTranHeaders(page);

    List<PaymentPurpose> paymentPurposes = paymentPurposeService.getAllPaymentPurpose();
    for (PayTranHeader p : pages.getContent()) {
      resetPaytranHeader(p, request.getLocale().getLanguage().equalsIgnoreCase("zh"), paymentPurposes);
      // 如果是新建, 则添加checkurl
      if (p.getStatus() == CommonUtil.STATUS_NEW) {
        p.setFinanceCheckUrl(processService.getCheckUrl(p.getTranNum()));
      }

       int status = p.getStatus();
      if (status == CommonUtil.STATUS_CHECK || status == CommonUtil.STATUS_AM_UPLOAD) {
        if (p.getAmSupport() && status != CommonUtil.STATUS_AM_UPLOAD) {//如果是查账且需要am支持
          p.setAmSupportUrl(processService.getAmSuportUrl(p.getTranNum()));
        }else {
          p.setFinanceConfirmUrl(processService.getFinanceConfirmUrl(p.getTranNum()));
        }
      }
    }
    boolean isZH = request.getLocale().getLanguage().equalsIgnoreCase("zh");
    // 添加status搜索框
    model.addAttribute("statuses", isZH ? CommonUtil.STATUSES_ZH : CommonUtil.STATUSES_EN);
    model.addAttribute("pages", pages);
    return "paytran/paytranList";
  }

  @RequestMapping(value = "create", method = RequestMethod.GET)
  public String toCreate(Model model, HttpServletRequest request) {
    boolean isZH = "zh".equalsIgnoreCase(request.getLocale().getLanguage());
    PayTranHeader payTranHeader = new PayTranHeader();
    List<PaymentPurpose> paymentPurposes = paymentPurposeService.getAllPaymentPurpose();
    resetPaytranHeader(payTranHeader, isZH, paymentPurposes);
    // 设置默认的货币为CNY
    payTranHeader.setCurrency("CNY");
    model.addAttribute("payTranHeader", payTranHeader);
    model.addAttribute("action", "create");
    // 汇率
    List<ExchangeRate> exchangeRates = exchangeRateService.getAllAvailExchangeRates();
    model.addAttribute("currencyRates", getCurrencyMapByExchangeRates(exchangeRates));
    return "paytran/paytranForm";
  }

  private Map<String, Double> getCurrencyMapByExchangeRates(List<ExchangeRate> exchangeRates) {
    Map<String, Double> currencyMap = new HashMap<String, Double>();
    for (ExchangeRate exchangeRate : exchangeRates) {
      currencyMap.put(exchangeRate.getBase_currency() + "-" + exchangeRate.getCurrency(), exchangeRate.getRate());
    }
    return currencyMap;
  }

  @RequestMapping(value = "create", method = RequestMethod.POST)
  public String create(PayTranHeader payTranHeader, RedirectAttributes redirectAttributes, Model model, HttpServletRequest request,
      @RequestParam("files") MultipartFile[] files) {
    String[] attachments = request.getParameterValues("attachments");
    String[] fileNames = request.getParameterValues("fileNames");
    if (JCaptcha.validateResponse(request, request.getParameter("jcaptchaCode"))) {
      // 新建状态为new
      payTranHeader.setStatus(CommonUtil.STATUS_NEW);
      Mail mail = null;
      try {
        mail = payTranHeaderService.save(payTranHeader, request.getParameterValues("paytranDetailStr"), attachments);

      } catch (Exception e) {
        e.printStackTrace();
        redirectAttributes.addFlashAttribute("message", CommonUtil.i18nStr(request, "系统出错", "System Error"));
        redirectAttributes.addFlashAttribute("error", "error");
        return "redirect:/process/message";
      }
      
      redirectAttributes.addFlashAttribute("successPaytran", payTranHeaderService.get(payTranHeader.getTranNum()));

      if (mail != null) {
        boolean sendResult = mailService.send(mail);
        mailService.updateMailResult(mail, sendResult);
      }

      ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
      if (user != null) {
        return "redirect:/paytran";
      } else {
        return "redirect:/paytran/create";
      }
    } else {
      boolean isZH = "zh".equalsIgnoreCase(request.getLocale().getLanguage());
      resetPaytranForm(payTranHeader, attachments, fileNames, request.getParameterValues("paytranDetailStr"), isZH);
      model.addAttribute("payTranHeader", payTranHeader);
      model.addAttribute("error", CommonUtil.i18nStr(request, "验证码错误", "verification code error"));
      model.addAttribute("action", "create");
      // 汇率
      List<ExchangeRate> exchangeRates = exchangeRateService.getAllAvailExchangeRates();
      model.addAttribute("currencyRates", getCurrencyMapByExchangeRates(exchangeRates));
      return "paytran/paytranForm";
    }
  }

  /**
   * 验证码错误后,设置header参数
   * 
   * @param payTranHeader
   * @param attachments
   * @param parameterValues
   */
  private void resetPaytranForm(PayTranHeader payTranHeader, String[] attachments, String[] fileNames, String[] paytranDetailsStr, boolean isZH) {
    List<PaymentPurpose> paymentPurposes = paymentPurposeService.getAllPaymentPurpose();
    resetPaytranHeader(payTranHeader, isZH, paymentPurposes);
    if (attachments != null && attachments.length > 0) {
      for (int i = 0; i < attachments.length; i++) {
        PayTranAttachement attachement = payTranAttachmentMapper.getById(Long.parseLong(attachments[i]));
        payTranHeader.getPayTranAttachements().add(attachement);
      }
    }
    if (paytranDetailsStr != null && paytranDetailsStr.length > 0) {
      for (String paytranDetailstr : paytranDetailsStr) {
        PayTranDetail payTranDetail = new PayTranDetail();
        // bdUserName+"#"+payCode+"#"+amount
        String[] detailColums = paytranDetailstr.split("#");
        payTranDetail.setBdUserName(detailColums[0]);
        payTranDetail.setPayCode(Long.parseLong((detailColums[1])));
        payTranDetail.setAmount(StringUtils.isNoneBlank(detailColums[2]) ? Double.parseDouble(detailColums[2]) : 0d);
        payTranDetail.setAmountInRMB(StringUtils.isNoneBlank(detailColums[3]) ? Double.parseDouble(detailColums[3]) : 0d);
        if (detailColums.length > 4) {
          payTranDetail.setRechargeOnline("1".equals(detailColums[4]) ? true : false);
        }
        payTranHeader.getPayTranDetails().add(payTranDetail);
      }
    }
  }

  @RequestMapping(value = "update/{tranNum}", method = RequestMethod.GET)
  public String toUpdate(@PathVariable("tranNum") Long tranNum, Model model, HttpServletRequest request) {
    PayTranHeader payTranHeader = payTranHeaderService.get(tranNum);
    Map<String, Object> processMap = new HashMap<String, Object>();
    processMap.put("payTranNum", tranNum);
    processMap.put("isUpdated", '1');
    List<Process> processes = processMapper.get(processMap);
    List<PaymentPurpose> paymentPurposes = paymentPurposeService.getAllPaymentPurpose();
    resetPaytranHeader(payTranHeader, "zh".equalsIgnoreCase(request.getLocale().getLanguage()), paymentPurposes);
    model.addAttribute("payTranHeader", payTranHeader);
    List<PayTranAttachement> attachments = processService.getProcessAttachments(tranNum);
    for (int i = 0; i < processes.size(); i++) {
      if (processes.get(i).getStatus() == CommonUtil.STATUS_CHECK) {
        // check 信息
        model.addAttribute("processCheck", processes.get(i));
        List<PayTranAttachement> checkAttachs = new ArrayList<PayTranAttachement>();
        for (int j = 0; j < attachments.size(); j++) {
          if (attachments.get(j).getProcessId().longValue() == processes.get(i).getProcessId().longValue()) {
            checkAttachs.add(attachments.get(j));
          }
        }
        // check附件
        model.addAttribute("processCheckAttach", checkAttachs);
      }
      if (processes.get(i).getStatus() == CommonUtil.STATUS_FINANCE_CONFIRM) {
        // confirm 信息
        model.addAttribute("processConfirm", processes.get(i));
        List<PayTranAttachement> confirmAttachs = new ArrayList<PayTranAttachement>();
        for (int j = 0; j < attachments.size(); j++) {
          if (attachments.get(j).getProcessId().longValue() == processes.get(i).getProcessId().longValue()) {
            confirmAttachs.add(attachments.get(j));
          }
        }
        // confirm 附件
        model.addAttribute("processConfirmAttach", confirmAttachs);
      }
    }
    return "paytran/paytranView";
  }



  @RequestMapping(value = "delete/{tranNum}", method = RequestMethod.GET)
  public String delete(@PathVariable("tranNum") Long tranNum, RedirectAttributes redirectAttributes, HttpServletRequest request) {
    payTranHeaderService.delete(tranNum);
    redirectAttributes.addFlashAttribute("message", CommonUtil.i18nStr(request, "删除交易信息成功", "delete pay tran success"));
    return "redirect:/paytran";
  }

  @RequestMapping(value = "pass/{tranNum}/{status}", method = RequestMethod.GET)
  public String pass(@PathVariable("tranNum") Long tranNum, @PathVariable("status") Character status, HttpServletRequest request,
      RedirectAttributes redirectAttributes) {
    PayTranHeader payTranHeaderDB = payTranHeaderService.get(tranNum);
    if (payTranHeaderDB != null && payTranHeaderDB.getStatus() == status) {
      Mail mail = null;
      try {
        mail = payTranHeaderService.pass(tranNum, status);
        redirectAttributes.addFlashAttribute("message",
            "zh".equalsIgnoreCase(request.getLocale().getLanguage()) ? "感谢您的审批" : "thanks for your approving");
      } catch (Exception e) {
        e.printStackTrace();
      }

      if (mail != null) {
        boolean sendResult = mailService.send(mail);
        mailService.updateMailResult(mail, sendResult);
        if (status == CommonUtil.STATUS_FINANCE_CONFIRM) {
          PayTranHeader payTranHeader = payTranHeaderService.get(tranNum);
          // 页面录入的邮箱
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

        }
      }

    } else {
      // 这个页面已经邮件审批.
      redirectAttributes.addFlashAttribute("errorMessage", CommonUtil.i18nStr(request, "提交失败,页面信息已更新,请刷新后重新操作.",
          "Submit failed, page information has been updated, please refresh after reoperation"));
    }

    return "redirect:/paytran";
  }

  @RequestMapping(value = "reject", method = RequestMethod.POST)
  public String reject(PayTranHeader payTranHeader, HttpServletRequest request, RedirectAttributes redirectAttributes) {
    PayTranHeader payTranHeaderDB = payTranHeaderService.get(payTranHeader.getTranNum());
    if (payTranHeaderDB != null && payTranHeaderDB.getStatus() == payTranHeader.getStatus()) {
      Mail mail = null;
      try {
        mail = payTranHeaderService.reject(payTranHeader.getTranNum(), payTranHeader.getDescription());
        redirectAttributes.addFlashAttribute("paytranNum", payTranHeader.getTranNum());
        redirectAttributes.addFlashAttribute("message", CommonUtil.i18nStr(request, "交易被拒绝, 交易编号:", "Transaction Rejected, Transaction Number:"));
      } catch (Exception e) {
        e.printStackTrace();
      }

      if (mail != null) {
        boolean sendResult = mailService.send(mail);
        mailService.updateMailResult(mail, sendResult);
      }

    } else {
      // 这个页面已经邮件审批.
      redirectAttributes.addFlashAttribute("errorMessage", CommonUtil.i18nStr(request, "提交失败,页面信息已更新,请刷新后重新操作.",
          "Submit failed, page information has been updated, please refresh after reoperation"));
    }
    return "redirect:/paytran";
  }

  @RequestMapping(value = "downloadAttach/{attachmentId}", method = RequestMethod.GET)
  public void downloadAttach(@PathVariable("attachmentId") Long attachmentId, HttpServletResponse response) {
    try {
      PayTranAttachement payTranAttachement = payTranAttachmentMapper.getById(attachmentId);
      File file = new File(payTranAttachement.getPath() + "/" + payTranAttachement.getFileName());
      response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(payTranAttachement.getShowName(), "UTF-8"));
      FileInputStream in = new FileInputStream(file);
      OutputStream out = response.getOutputStream();
      byte buffer[] = new byte[1024];
      int len = 0;
      while ((len = in.read(buffer)) > 0) {
        // 输出缓冲区的内容到浏览器，实现文件下载
        out.write(buffer, 0, len);
      }
      in.close();
      out.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 查看paytran流程状态
   * 
   * @param paytranNum
   * @param request
   * @return
   */
  @RequestMapping(value = "show/process/{paytranNum}", method = RequestMethod.POST)
  @ResponseBody
  public String getProcess(@PathVariable("paytranNum") Long paytranNum, HttpServletRequest request) {
    boolean isZH = "zh".equalsIgnoreCase(request.getLocale().getLanguage());
    Map<String, String> map = new HashMap<String, String>();
    PayTranHeader payTranHeader = payTranHeaderService.get(paytranNum);
    List<Process> processes = processMapper.getProcessesByPaytranNums(paytranNum);
    map.put("tr1td0", CommonUtil.formatDate(payTranHeader.getCreateDate()));
    map.put("tr1td1", isZH ? "新建" : "New");
    map.put("tr1td2", payTranHeader.getEmail());
    if (processes != null && processes.size() > 0) {
      for (int i = 0; i < processes.size(); i++) {
        Process process = processes.get(i);
        if (process.getStatus() == '2') {
          map.put("tr2td0", CommonUtil.formatDate(process.getOperateDate()));
          map.put("tr2td1", isZH ? "财务查账拒绝" : "Finance Check Rejected");
          map.put("tr2td2", process.getOperateByName());
          map.put("tr2td3", "");
        } else if (process.getStatus() == '3') {
          map.put("tr3td0", CommonUtil.formatDate(process.getOperateDate()));
          map.put("tr3td1", isZH ? "财务查账确认" : "Finance Checked");
          map.put("tr3td2", process.getOperateByName());
          map.put("tr3td3", "");
        } else if (process.getStatus() == '7') {
          map.put("tr7td0", CommonUtil.formatDate(process.getOperateDate()));
          map.put("tr7td1", isZH ? "财务加款至端口拒绝" : "Finance Confirm Rejected");
          map.put("tr7td2", process.getOperateByName());
          map.put("tr7td3", "");
        } else if (process.getStatus() == '4') {
          map.put("tr4td0", CommonUtil.formatDate(process.getOperateDate()));
          map.put("tr4td1", isZH ? "财务加款至端口确认" : "Finance Confirm");
          map.put("tr4td2", process.getOperateByName());
          map.put("tr4td3", "");
        } else if (process.getStatus() == '5') {
          map.put("tr5td0", CommonUtil.formatDate(process.getOperateDate()));
          map.put("tr5td1", isZH ? "完成" : "Done");
          map.put("tr5td2", process.getOperateByName());
          map.put("tr5td3", "");
        } else if (process.getStatus() == '8') {
          map.put("tr8td0", CommonUtil.formatDate(process.getOperateDate()));
          map.put("tr8td1", isZH ? "客服大額加款申請提交" : "AM top up request (amount exceeds RMB 100K) submitted");
          map.put("tr8td2", process.getOperateByName());
          map.put("tr8td3", "");
        }
      }
    }
    // 如果不是拒绝.
    if (payTranHeader.getStatus() != '2' && payTranHeader.getStatus() != '7') {
      // 如果没有3
      if (!map.containsKey("tr3td0")) {
        map.put("tr3td0", " - ");
        map.put("tr3td1", isZH ? "财务查账" : "Finance Check");
        List<String> groupNames = processService.getReceiverGroup(paytranNum, "finance");
        map.put("tr3td2", groupNames.toString());
        map.put("tr3td3", "color-red");
      }
      // 如果没有4
      if (!map.containsKey("tr4td0")) {
        map.put("tr4td0", " - ");
        map.put("tr4td1", isZH ? "财务加款至端口" : "Finance Confirm");
        List<String> groupNames = processService.getReceiverGroup(paytranNum, "finance_confirm");
        map.put("tr4td2", groupNames.toString());
        map.put("tr4td3", "color-red");
      }
      // 如果没有5
      if (!map.containsKey("tr5td0")) {
        map.put("tr5td0", " - ");
        map.put("tr5td1", isZH ? "Ops确认" : "Ops Confirm");
        List<String> groupNames = processService.getReceiverGroup(paytranNum, "ops");
        map.put("tr5td2", groupNames.toString());
        map.put("tr5td3", "color-red");
      }
      // 如果是账期客户并且加款大于100K，并且没有8
      if ( payTranHeader.getAmSupport() && !map.containsKey("tr8td0")) {
        map.put("tr8td0", " - ");
        map.put("tr8td1", isZH ? "客服大額加款申請提交" : "AM top up request (amount exceeds RMB 100K) submitted");
        List<String> groupNames = processService.getReceiverGroup(paytranNum, "AM");
        map.put("tr8td2", groupNames.toString());
        map.put("tr8td3", "color-red");
      }
    }

    map.put("title", (isZH ? "交易进度" : "Payment Transaction process") + "(" + paytranNum + ")");

    return JsonMapper.nonDefaultMapper().toJson(map);
  }

  @ModelAttribute
  public void getPaytran(@RequestParam(value = "tranNum", defaultValue = "-1") Long tranNum, Model model) {
    if (tranNum != -1) {
      model.addAttribute("payTranHeader", payTranHeaderService.get(tranNum));
    }
  }

  private void resetPaytranHeader(PayTranHeader payTranHeader, boolean isZH, List<PaymentPurpose> paymentPurposes) {
    // 这里用状态集合
    payTranHeader.setStatuses(isZH ? CommonUtil.STATUSES_ZH : CommonUtil.STATUSES_EN);

    payTranHeader.getCurrencys().clear();
    payTranHeader.getCurrencys().putAll(isZH ? CommonUtil.CURRENCY_ZH : CommonUtil.CURRENCY_EN);

    payTranHeader.getPayCodes().clear();
    for (PaymentPurpose paymentPurpose : paymentPurposes) {
      payTranHeader.getPayCodes().put(paymentPurpose.getId(), isZH ? paymentPurpose.getPay_purpose() : paymentPurpose.getPay_purposeEN());
    }

    for (PayTranDetail payTranDetail : payTranHeader.getPayTranDetails()) {
      payTranDetail.getPayCodes().clear();
      payTranDetail.getPayCodes().putAll(payTranHeader.getPayCodes());
    }
  }
}
