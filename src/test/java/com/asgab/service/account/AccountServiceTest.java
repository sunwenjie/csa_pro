package com.asgab.service.account;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:/applicationContext.xml"})
@ActiveProfiles("production")
public class AccountServiceTest {
  @Autowired
  private AccountService accountService;

  @Test
  public void testGetAllUser() {
    System.out.println(accountService.getAllUser().size());
  }

}
