package com.asgab.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.asgab.service.mail.MailService;

public class EmailRetryTask {

  Log log = LogFactory.getLog(EmailRetryTask.class);

  @Autowired
  private MailService mailService;

  public void run() {
    log.info("email send retry task start----------------");
    mailService.resendNotSentMails();
    log.info("email send retry task end----------------");
  }
}
