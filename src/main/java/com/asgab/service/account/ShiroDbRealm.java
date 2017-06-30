package com.asgab.service.account;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asgab.entity.Group;
import com.asgab.entity.User;
import com.google.common.base.Objects;

public class ShiroDbRealm extends AuthorizingRealm {

  protected AccountService accountService;

  private static final Logger logger = LoggerFactory.getLogger(ShiroDbRealm.class);

  Log log = LogFactory.getLog(ShiroDbRealm.class);

  /**
   * 认证回调函数,登录时调用.
   */
  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
    UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
    User user = accountService.findUserByLoginName(token.getUsername());

    if (user == null) {
      throw new UnknownAccountException();// 没找到帐号
    }

    // user.setStatus("Paused");
    if ("Paused".equals(user.getStatus()) || "Stopped".equals(user.getStatus())) {

      throw new LockedAccountException(); // 帐号锁定
    }

    token.setPassword((String.valueOf(token.getPassword()) + "wibble" + user.getSalt()).toCharArray());

    /* byte[] salt = Encodes.decodeHex(user.getSalt()); */

    return new SimpleAuthenticationInfo(new ShiroUser(user.getId(), user.getLoginName(), user.getName()), user.getPassword(), getName());

    /*
     * return new SimpleAuthenticationInfo( new ShiroUser(user.getId(), user.getLoginName(),
     * user.getName()), user.getPassword(), ByteSource.Util.bytes(salt), getName());
     */

  }

  /**
   * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
   */
  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

    ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
    // 修改最后登录时间
    try {

      int result = accountService.update_logged(shiroUser.id);
      if (result == 0) {
        logger.warn("update_logged execute fail. userid is {}  ", shiroUser.id);
      }
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("update_logged execute has errors. userid is {}  ", shiroUser.id);
    }

    List<Group> groups = accountService.findUserGroupByLoginName(shiroUser.loginName);
    SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
    List<String> roles = new ArrayList<>();

    for (int i = 0; i < groups.size(); i++) {
      roles.add(groups.get(i).getGroup_type().toLowerCase());
    }

    info.addRoles(roles);
    return info;
  }

  public boolean containsAny(List<Group> groups, List<String> groupsIds) {

    if (groupsIds != null && groups != null) {
      for (String id : groupsIds) {
        if (groups.contains(new Group(id))) {
          return true;
        }
      }
    }
    return false;
  }


  /**
   * 设定Password校验的Hash算法与迭代次数.
   */
  @PostConstruct
  public void initCredentialsMatcher() {
    HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(AccountService.HASH_ALGORITHM);
    // matcher.setHashIterations(AccountService.HASH_INTERATIONS);
    setCredentialsMatcher(matcher);
  }

  public void setAccountService(AccountService accountService) {
    this.accountService = accountService;
  }

  /**
   * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
   */
  public static class ShiroUser implements Serializable {
    private static final long serialVersionUID = -1373760761780840081L;
    public Long id;
    public String loginName;
    public String name;

    public ShiroUser(Long id, String loginName, String name) {
      this.id = id;
      this.loginName = loginName;
      this.name = name;
    }

    public String getName() {
      return name;
    }

    /**
     * 本函数输出将作为默认的<shiro:principal/>输出.
     */
    @Override
    public String toString() {
      return loginName;
    }

    /**
     * 重载hashCode,只计算loginName;
     */
    @Override
    public int hashCode() {
      return Objects.hashCode(loginName);
    }

    /**
     * 重载equals,只计算loginName;
     */
    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      ShiroUser other = (ShiroUser) obj;
      if (loginName == null) {
        if (other.loginName != null) {
          return false;
        }
      } else if (!loginName.equals(other.loginName)) {
        return false;
      }
      return true;
    }
  }
}
