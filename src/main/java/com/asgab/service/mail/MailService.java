package com.asgab.service.mail;

import com.asgab.core.mail.MailUtil;
import com.asgab.entity.*;
import com.asgab.entity.Process;
import com.asgab.repository.CustMasterMapper;
import com.asgab.repository.MailMapper;
import com.asgab.repository.ProcessMapper;
import com.asgab.service.exchangeRate.ExchangeRateService;
import com.asgab.service.paymentPurpose.PaymentPurposeService;
import com.asgab.service.paytran.BlockTradeService;
import com.asgab.service.paytran.PayTranHeaderService;
import com.asgab.service.process.ProcessService;
import com.asgab.util.CommonUtil;
import com.asgab.web.AutoEmailNotification;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Transactional
public class MailService {

  @Resource
  private ProcessMapper processMapper;

  @Autowired
  private PayTranHeaderService payTranHeaderService;

  @Resource
  private CustMasterMapper custMasterMapper;

  @Autowired
  private ExchangeRateService exchangeRateService;

  @Autowired
  private PaymentPurposeService paymentPurposeService;

  @Resource
  private MailMapper mailMapper;

  @Autowired
  private ProcessService processService;

  @Resource
  private BlockTradeService blockTradeService;

  protected Log logger = LogFactory.getLog(getClass());

  public void save(Mail mail) {
    // 保存信息
    mailMapper.save(mail);
    // 保存mailReceiver
    for (String mailAddress : mail.getReceivers()) {
      MailReceiver mailReceiver = new MailReceiver();
      mailReceiver.setMailId(mail.getId());
      mailReceiver.setMailAddress(mailAddress);
      mailMapper.saveMailReceiver(mailReceiver);
    }
    // 发送邮件改到提交事务后
  }

  public List<Mail> getNotSentMail() {
    List<Mail> mails = mailMapper.getNotSendMails();
    if (mails != null && mails.size() > 0) {
      for (Mail mail : mails) {
        List<MailReceiver> mailReceivers = mailMapper.getMailReceiversByMailId(mail.getId());
        if (mailReceivers != null && mailReceivers.size() > 0) {
          for (MailReceiver mailReceiver : mailReceivers) {
            mail.getReceivers().add(mailReceiver.getMailAddress());
          }
        }
      }
    }
    return mails;
  }

  public boolean send(Mail mail) {
    boolean sendResult = false;

    if (mail.getReceivers().size() == 0) {

      List<MailReceiver> mailReceivers = mailMapper.getMailReceiversByMailId(mail.getId());
      if (mailReceivers != null && mailReceivers.size() > 0) {
        for (MailReceiver mailReceiver : mailReceivers) {
          mail.getReceivers().add(mailReceiver.getMailAddress());
        }
      }
    }

    if (mail.getReceivers().size() > 0) {
      // 立即发送邮件
      try {
        String template = mail.getTemplate();
        // 当前的流程
        Process process = processMapper.getById(mail.getProcessId());
        if (AutoEmailNotification.EMAIL_TEMPLATE_NAME_1.equalsIgnoreCase(template)) {
          // 发送给财务check
          step1_send_to_fin(process.getProcessId(), mail);
        } else if (AutoEmailNotification.EMAIL_TEMPLATE_NAME_2.equalsIgnoreCase(template)) {
          // 发送给财务confirm
          step2_send_to_fin(process.getProcessId(), mail);
        } else if (AutoEmailNotification.EMAIL_TEMPLATE_NAME_3.equalsIgnoreCase(template)) {
          // 发送给AM confirm
          step3_send_to_ops(process.getProcessId(), mail);
        } else if (AutoEmailNotification.EMAIL_TEMPLATE_NAME_4.equalsIgnoreCase(template)) {
          // 通知成功
          step4_send_to_AM_and_sales(process.getProcessId(), mail);
        } else if (AutoEmailNotification.EMAIL_TEMPLATE_NAME_5.equalsIgnoreCase(template)) {
          // 通知页面填写的邮箱
          step5_send_to_paytranEmail(mail);
        } else if (AutoEmailNotification.EMAIL_TEMPLATE_NAME_6.equalsIgnoreCase(template)){
          // 发送给AM，上传客户消费证明
          step6_send_to_amsuport(process.getProcessId(), mail);
        }
        else {
          send_reject(mail);
        }
        sendResult = true;
      } catch (Exception e) {
        // 异常
        logger.error("", e);
        MailUtil.notifyAdmin("CSA 邮件发送异常,paytrannum = " + mail.getPaytranNum() + "; mailid = " + mail.getId());
      }
    } else {
      logger.error("没有收件人:paytranNum=" + mail.getPaytranNum() + ";mailId=" + mail.getId());
    }
    return sendResult;
  }

  public void updateMailResult(Mail mail, boolean sendResult) {
    Character status = '2';
    if (sendResult) {
      status = '3';
    }
    mail.setStatus(status);
    mail.setSendDate(new Date());
    mailMapper.update(mail);
  }

  /**
   * 重发失败邮件 返回-1 表示重发失败, 否则返回重发邮件个数
   */
  public int resendNotSentMails() {
    int resendCount = 0;
    List<Mail> mails = getNotSentMail();
    int errorCount = 0;
    if (mails != null && mails.size() > 0) {
      for (Mail mail : mails) {
        boolean sendResult = send(mail);
        updateMailResult(mail, sendResult);
        if (!sendResult) {
          errorCount++;
        } else {
          resendCount++;
        }
        // 如果发送失败3, 则取消这次定时任务
        if (errorCount >= 3) {
          resendCount = -1;
          break;
        }
      }
    }
    return resendCount;
  }

  public void step1_send_to_fin(long processId, Mail mail) throws Exception {
    Process process = processMapper.getById(processId);
    PayTranHeader payTranHeader = payTranHeaderService.get(process.getPayTranNum());
    payTranHeader.setProcessAttachements(processService.getProcessAttachments(payTranHeader.getTranNum()));
    String bdUserNames = payTranHeader.getBdUserNamesSplit();
    mail.setSubject("客户入账通知("+bdUserNames+")-----财务查账用");
    resetPaytranHeader(payTranHeader, true);
    AutoEmailNotification.send1(getCustMasterMap(payTranHeader), payTranHeader, mail);
  }

  public void step2_send_to_fin(long processId, Mail mail) throws Exception {
    Process process = (Process) processMapper.getById(processId);
    PayTranHeader payTranHeader = payTranHeaderService.get(process.getPayTranNum());
    payTranHeader.setProcessAttachements(processService.getProcessAttachments(payTranHeader.getTranNum()));
    String bdUserNames = payTranHeader.getBdUserNamesSplit();
    mail.setSubject("客户入账通知("+bdUserNames+")-----财务确认打款用");
    // 获取上一个流程
    Process lastProcess = processMapper.getLastProcess(processId);
    mail.setFinConfirmAuditDate(lastProcess == null ? "" : CommonUtil.formatDate(lastProcess.getOperateDate()));
    mail.setFinAuditBy(lastProcess == null ? "" : lastProcess.getOperateByName());
    resetPaytranHeader(payTranHeader, true);
    Process financeCheckProcess = processService.getFinanceCheckProcess(payTranHeader.getTranNum());
    BlockTrade blockTrade = lastProcess.getStatus() == CommonUtil.STATUS_AM_UPLOAD ? blockTradeService.getBlockTrade(lastProcess.getProcessId()) : null;
    AutoEmailNotification.send2(getCustMasterMap(payTranHeader), payTranHeader, financeCheckProcess, mail,blockTrade);
  }

  public void step6_send_to_amsuport(long processId, Mail mail) throws Exception {
    Process process = (Process) processMapper.getById(processId);
    PayTranHeader payTranHeader = payTranHeaderService.get(process.getPayTranNum());
    payTranHeader.setProcessAttachements(processService.getProcessAttachments(payTranHeader.getTranNum()));
    String bdUserNames = payTranHeader.getBdUserNamesSplit();
    mail.setSubject("客户入账通知("+bdUserNames+")-----客服申请大额提前加款");
    // 获取上一个流程
    Process lastProcess = processMapper.getLastProcess(processId);
    mail.setFinConfirmAuditDate(lastProcess == null ? "" : CommonUtil.formatDate(lastProcess.getOperateDate()));
    mail.setFinAuditBy(lastProcess == null ? "" : lastProcess.getOperateByName());
    resetPaytranHeader(payTranHeader, true);
    Process financeCheckProcess = processService.getFinanceCheckProcess(payTranHeader.getTranNum());
    AutoEmailNotification.send6(getCustMasterMap(payTranHeader), payTranHeader, financeCheckProcess, mail);
  }



  public void step3_send_to_ops(long processId, Mail mail) throws Exception {
    Process process = (Process) processMapper.getById(processId);
    PayTranHeader payTranHeader = payTranHeaderService.get(process.getPayTranNum());
    payTranHeader.setProcessAttachements(processService.getProcessAttachments(payTranHeader.getTranNum()));
    String bdUserNames = payTranHeader.getBdUserNamesSplit();
    mail.setSubject("客户入账通知("+bdUserNames+")");
    // 获取上一个流程
    Process lastProcess = processMapper.getLastProcess(processId);
    Process last_lastProcess = null;
    if (lastProcess != null) {
      last_lastProcess = processMapper.getLastProcess(lastProcess.getProcessId());
    }
    mail.setFinConfirmReceivableDate(last_lastProcess == null ? "" : CommonUtil.formatDate(last_lastProcess.getOperateDate()));
    mail.setFinConfirmReceivableBy(last_lastProcess == null ? "" : last_lastProcess.getOperateByName());
    mail.setFinPayTranDate(lastProcess == null ? "" : CommonUtil.formatDate(lastProcess.getOperateDate()));
    mail.setFinPayTranBy(lastProcess == null ? "" : lastProcess.getOperateByName());
    resetPaytranHeader(payTranHeader, true);
    Process targetProgress = processService.getFinanceConfirmProcess(payTranHeader.getTranNum());
    Process finProgress = processService.getFinanceCheckProcess(payTranHeader.getTranNum());
    if (targetProgress == null || (targetProgress != null && StringUtils.isBlank(targetProgress.getDescription()))) {
      targetProgress = finProgress;
    }
    AutoEmailNotification.send3(getCustMasterMap(payTranHeader), payTranHeader, targetProgress,finProgress, mail);
  }

  public void step4_send_to_AM_and_sales(long processId, Mail mail) throws Exception {
    Process process = (Process) processMapper.getById(processId);
    PayTranHeader payTranHeader = payTranHeaderService.get(process.getPayTranNum());
    payTranHeader.setProcessAttachements(processService.getProcessAttachments(payTranHeader.getTranNum()));
    String bdUserNames = payTranHeader.getBdUserNamesSplit();
    mail.setSubject("客户入账通知("+bdUserNames+")");
    // 获取上一个流程
    Process lastProcess = processMapper.getLastProcess(processId);
    Process last_lastProcess = null;
    if (lastProcess != null) {
      // 获取上2个
      last_lastProcess = processMapper.getLastProcess(lastProcess.getProcessId());
    }
    // 获取上3个流程
    Process last3Process = null;
    if (last_lastProcess != null) {
      last3Process = processMapper.getLastProcess(last_lastProcess.getProcessId());
    }
    mail.setFinConfirmReceivableDate(last3Process == null ? "" : CommonUtil.formatDate(last3Process.getOperateDate()));
    mail.setFinConfirmReceivableBy(last3Process == null ? "" : last3Process.getOperateByName());
    mail.setFinPayTranDate(last_lastProcess == null ? "" : CommonUtil.formatDate(last_lastProcess.getOperateDate()));
    mail.setFinPayTranBy(last_lastProcess == null ? "" : last_lastProcess.getOperateByName());
    mail.setOpsPayTranDate(lastProcess == null ? "" : CommonUtil.formatDate(lastProcess.getOperateDate()));
    mail.setOpsPayTranBy(lastProcess == null ? "" : lastProcess.getOperateByName());
    resetPaytranHeader(payTranHeader, true);
    Process targetProgress = processService.getFinanceConfirmProcess(payTranHeader.getTranNum());
    Process finProgress = processService.getFinanceCheckProcess(payTranHeader.getTranNum());
    if (targetProgress == null || (targetProgress != null && StringUtils.isBlank(targetProgress.getDescription()))) {
      targetProgress = finProgress;
    }
    AutoEmailNotification.send4(getCustMasterMap(payTranHeader), payTranHeader, targetProgress,finProgress, mail);
  }

  public void step5_send_to_paytranEmail(Mail mail) throws Exception {
    PayTranHeader payTranHeader = payTranHeaderService.get(mail.getPaytranNum());
    payTranHeader.setProcessAttachements(processService.getProcessAttachments(payTranHeader.getTranNum()));
    resetPaytranHeader(payTranHeader, true);
    Process targetProgress = processService.getFinanceConfirmProcess(payTranHeader.getTranNum());
    Process finProgress = processService.getFinanceCheckProcess(payTranHeader.getTranNum());
    if (targetProgress == null || (targetProgress != null && StringUtils.isBlank(targetProgress.getDescription()))) {
      targetProgress = finProgress;
    }
    AutoEmailNotification.send5(payTranHeader, mail, targetProgress,finProgress);
  }

  public void send_reject(Mail mail) throws Exception {
    PayTranHeader payTranHeader = payTranHeaderService.get(mail.getPaytranNum());
    mail.setDescription(payTranHeader.getDescription());
    AutoEmailNotification.send_reject(mail, getCustMasterMap(payTranHeader), payTranHeader);
  }

  private Map<String, CustMaster> getCustMasterMap(PayTranHeader payTranHeader) {
    Map<String, CustMaster> custMasterMap = new HashMap<String, CustMaster>();
    for (int i = 0; i < payTranHeader.getPayTranDetails().size(); i++) {
      String baiduUserName = payTranHeader.getPayTranDetails().get(i).getBdUserName();
      CustMaster tempCustMaster = custMasterMapper.findByCustUsername(baiduUserName);
      if (tempCustMaster != null) {
        custMasterMap.put(baiduUserName, tempCustMaster);
      }
    }
    return custMasterMap;
  }

  private void resetPaytranHeader(PayTranHeader payTranHeader, boolean isZH) {
    payTranHeader.setStatuses(isZH ? CommonUtil.STATUSES_ZH : CommonUtil.STATUSES_EN);

    List<ExchangeRate> exchangeRates = exchangeRateService.getAllAvailExchangeRates();
    payTranHeader.getExchangeRates().clear();
    payTranHeader.getExchangeRates().addAll(exchangeRates);

    payTranHeader.getCurrencys().clear();
    payTranHeader.getCurrencys().putAll(isZH ? CommonUtil.CURRENCY_ZH : CommonUtil.CURRENCY_EN);

    List<PaymentPurpose> paymentPurposes = paymentPurposeService.getAllPaymentPurpose();
    payTranHeader.getPayCodes().clear();
    // 交易方式
    for (PaymentPurpose paymentPurpose : paymentPurposes) {
      payTranHeader.getPayCodes().put(paymentPurpose.getId(), isZH ? paymentPurpose.getPay_purpose() : paymentPurpose.getPay_purposeEN());
    }
    // detail 的交易方式
    for (PayTranDetail payTranDetail : payTranHeader.getPayTranDetails()) {
      payTranDetail.getPayCodes().clear();
      payTranDetail.getPayCodes().putAll(payTranHeader.getPayCodes());
    }
  }


}
