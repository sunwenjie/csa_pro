package com.asgab.service.account;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.asgab.core.pagination.Page;
import com.asgab.entity.Group;
import com.asgab.entity.User;
import com.asgab.repository.UserMapper;
import com.asgab.repository.xmo.UserXMOMapper;
import com.asgab.service.ServiceException;
import com.asgab.service.account.ShiroDbRealm.ShiroUser;
import com.asgab.util.Clock;
import com.asgab.util.Digests;
import com.asgab.util.Encodes;

/**
 * 用户管理类.
 * 
 * @author calvin
 */
// Spring Service Bean的标识.
@Component
@Transactional
public class AccountService {

  public static final String HASH_ALGORITHM = "SHA-1";
  public static final int HASH_INTERATIONS = 1024;
  private static final int SALT_SIZE = 8;

  private static Logger logger = LoggerFactory.getLogger(AccountService.class);

  @Resource
  private UserMapper userMapper;

  @Resource
  private UserXMOMapper userXMOMapper;

  @Autowired
  private UserXMOService userXMOService;

  private Clock clock = Clock.DEFAULT;

  public List<User> getAllUser() {
    return (List<User>) userXMOMapper.search(null);
  }

  public Page<User> getAllUser(Page<User> page) {
    List<User> list = userXMOMapper.search(page.getSearchMap(), page.getRowBounds());
    int count = userXMOMapper.count(page.getSearchMap());
    page.setContent(list);
    page.setTotal(count);
    return page;
  }

  public User getUser(Long id) {
    return userMapper.get(id);
  }

  public User findUserByLoginName(String loginName) {
    List<User> users = userXMOMapper.findUserByLoginName(loginName);
    if (users != null && users.size() > 0) {
      if (users.size() == 1) {
        return users.get(0);
      }
    }
    return null;
  }

  public void registerUser(User user) {
    entryptPassword(user);
    // user.setRoles("admin");
    user.setRegisterDate(clock.getCurrentDate());

    userMapper.save(user);
  }

  public void updateUser(User user) {
    if (StringUtils.isNotBlank(user.getPlainPassword())) {
      entryptPassword(user);
    }
    userMapper.update(user);
  }

  public void deleteUser(Long id) {
    if (isSupervisor(id)) {
      logger.warn("操作员{}尝试删除超级管理员用户", getCurrentUserName());
      throw new ServiceException("不能删除超级管理员用户");
    }
    userMapper.delete(id);

  }

  public int update_logged(Long id) {
    return userXMOMapper.update_logged(id);
  }

  /**
   * 判断是否超级管理员.
   */
  private boolean isSupervisor(Long id) {
    return id == 1;
  }

  /**
   * 取出Shiro中的当前用户LoginName.
   */
  private String getCurrentUserName() {
    ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
    return user.loginName;
  }

  /**
   * 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
   */
  private void entryptPassword(User user) {
    byte[] salt = Digests.generateSalt(SALT_SIZE);
    user.setSalt(Encodes.encodeHex(salt));
    byte[] hashPassword = Digests.sha1(user.getPlainPassword().getBytes(), salt, HASH_INTERATIONS);
    user.setPassword(Encodes.encodeHex(hashPassword));
  }

  public void setClock(Clock clock) {
    this.clock = clock;
  }

  public List<Group> findUserGroupByLoginName(String loginName) {
    return userXMOService.findUserGroupByLoginName(loginName);
  }

  public static void main(String[] args) {
    byte[] hashPassword = Digests.sha1("0123456789".getBytes());

    System.out.println(Encodes.encodeHex(hashPassword));
  }

}
