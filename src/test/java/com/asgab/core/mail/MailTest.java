package com.asgab.core.mail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.asgab.entity.Mail;
import com.asgab.repository.MailMapper;
import com.asgab.service.mail.MailService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:/applicationContext.xml"})
@ActiveProfiles("production")
public class MailTest {
  @Autowired
  private MailService mailService;

  @Autowired
  private MailMapper mailMapper;

  @Test
  public void testMailTemplate() {
    Mail mail = mailMapper.get(3370L);
    mailService.send(mail);
  }

}
