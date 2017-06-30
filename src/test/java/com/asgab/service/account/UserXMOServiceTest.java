package com.asgab.service.account;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.asgab.entity.Group;
import com.asgab.entity.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:/applicationContext.xml"})
@ActiveProfiles("production")
public class UserXMOServiceTest {

  @Autowired
  private UserXMOService userXMOService;

  @Test
  public void findUserGroupByLoginName() {
    List<Group> groups = userXMOService.findUserGroupByLoginName("tf.chen");
    System.out.println("############################################");
    for (int i = 0; i < groups.size(); i++) {
      System.out.println(groups.get(i).getGroup_name());
    }
    System.out.println("############################################");
  }

  @Test
  public void findUsersByGroupName() {
    List<User> users = userXMOService.findUsersByGroupName("Automation-CSA Intl Sales & Ams");
    System.out.println("############################################");
    for (int i = 0; i < users.size(); i++) {
      System.out.println(users.get(i).getName());
    }
    System.out.println("############################################");
  }

  @Test
  public void findUsersByGroupId() {
    List<User> users = userXMOService.findUsersByGroupId(390L);
    System.out.println("############################################");
    for (int i = 0; i < users.size(); i++) {
      System.out.println(users.get(i).getName());
    }
    System.out.println("############################################");
  }

}
