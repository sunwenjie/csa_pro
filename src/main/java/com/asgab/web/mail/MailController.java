package com.asgab.web.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asgab.service.mail.MailService;

@Controller
@RequestMapping(value = "/mail")
public class MailController {

  @Autowired
  MailService mailService;

  @RequestMapping(method = RequestMethod.GET)
  public String list(Model model) {
    model.addAttribute("mails", mailService.getNotSentMail());
    return "mail/notSendMailList";
  }

  @ResponseBody
  @RequestMapping(value = "resend", method = RequestMethod.POST)
  public String resend() {
    return mailService.resendNotSentMails() + "";
  }



}
