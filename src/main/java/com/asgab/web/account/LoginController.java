package com.asgab.web.account;



import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asgab.service.account.AccountService;
import com.asgab.service.account.ShiroDbRealm.ShiroUser;

/**
 * LoginController负责打开登录页面(GET请求)和登录出错页面(POST请求)，
 * 
 * 真正登录的POST请求由Filter完成,
 * 
 * @author calvin
 */
@Controller
@RequestMapping(value = "/login")
public class LoginController {


  @RequestMapping(method = RequestMethod.GET)
  public String login(HttpServletRequest request) {
    Subject currentUser = SecurityUtils.getSubject();
    if (null != currentUser) {
      if (null != (ShiroUser) currentUser.getPrincipal()) {// 如果已经登录，则退出or 跳转到主页
        // currentUser.logout(); //安全起见，用这种，退出再登录
        return "redirect:/paytran"; // "/" 会根据spring-mvc.xml 配置通过 pageController 重新分配跳转到主页
      }
    }
    return "account/login";
  }

  @RequestMapping(method = RequestMethod.POST)
  public String fail(@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String userName, Model model, ServletRequest request) {

    // 已经登录的情况下，点击后退会有问题，解决此问题
    if (SecurityUtils.getSubject() != null && SecurityUtils.getSubject().isAuthenticated()) {
      SavedRequest savedRequest = WebUtils.getSavedRequest(request);
      String url = savedRequest != null ? savedRequest.getRequestUrl() : "";
      if (StringUtils.isNotBlank(url)) {
        url = url.replaceFirst(request.getServletContext().getContextPath(), "");
        if (url.startsWith("/process") || url.startsWith("//process")) {
          return "redirect:/" + url;
        }
      }

    }
    model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, userName);
    return "account/login";
  }



}
