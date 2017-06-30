<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="org.apache.shiro.authc.ExcessiveAttemptsException"%>
<%@ page import="org.apache.shiro.authc.IncorrectCredentialsException"%>
<%@ page import="org.apache.commons.lang3.StringUtils"%>
<%@ page import="org.apache.shiro.web.util.SavedRequest"%>
<%@ page import="org.apache.shiro.web.util.WebUtils"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="en">
<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>csa</title>

    <!-- Bootstrap Core CSS -->
    <link href="${ctx}/static/startbootstrap-sb-admin-2-1.0.7/bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- MetisMenu CSS -->
    <link href="${ctx}/static/startbootstrap-sb-admin-2-1.0.7/bower_components/metisMenu/dist/metisMenu.min.css" rel="stylesheet">

    <!-- Timeline CSS -->
    <link href="${ctx}/static/startbootstrap-sb-admin-2-1.0.7/dist/css/timeline.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="${ctx}/static/startbootstrap-sb-admin-2-1.0.7/dist/css/sb-admin-2.css" rel="stylesheet">

    <!-- Morris Charts CSS -->
    <link href="${ctx}/static/startbootstrap-sb-admin-2-1.0.7/bower_components/morrisjs/morris.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="${ctx}/static/startbootstrap-sb-admin-2-1.0.7/bower_components/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

	<link href="${ctx}/static/jquery-validation/1.11.1/validate.css" type="text/css" rel="stylesheet" />

	

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
      <!-- jQuery -->
    <script src="${ctx}/static/startbootstrap-sb-admin-2-1.0.7/bower_components/jquery/dist/jquery.min.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="${ctx}/static/startbootstrap-sb-admin-2-1.0.7/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>

    <!-- Metis Menu Plugin JavaScript -->
    <script src="${ctx}/static/startbootstrap-sb-admin-2-1.0.7/bower_components/metisMenu/dist/metisMenu.min.js"></script>

   

    <!-- Custom Theme JavaScript -->
    <script src="${ctx}/static/startbootstrap-sb-admin-2-1.0.7/dist/js/sb-admin-2.js"></script>
    <!-- 
    <script src="${ctx}/static/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
     -->
	<script src="${ctx}/static/jquery-validation/1.14.0/dist/jquery.validate.js" type="text/javascript"></script>
	<script src="${ctx}/static/jquery-validation/1.14.0/dist/jquey.validate.override.js" type="text/javascript"></script>
	<%
	String lang = request.getLocale().getLanguage();
	if(lang.equals("zh")){
		%>
		<script src="${ctx}/static/jquery-validation/1.14.0/dist/localization/messages_zh.js" type="text/javascript"></script>
		<%
	}
	%>
	

</head>

<body>

    <div class="container">
        <div class="row">
            <div class="col-md-4 col-md-offset-4">
                <div class="login-panel panel panel-default">
                    <div class="panel-heading">
                        <span style="font-size: 16px" ><spring:message code="index.welcome" /></span>
                        <a class="pull-right" href="${ctx}/paytran/create"><spring:message code="index.baidulogin" /></a>
                    </div>
                    <div class="panel-body">
                        <form role="form" id="loginForm" action="${ctx}/login" method="post">
                        	
                            <fieldset>
                            	<%
                            	String error = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
                            	SavedRequest savedRequest = WebUtils.getSavedRequest(request);
                        		String url = savedRequest!=null?savedRequest.getRequestUrl():"";
                        		if(StringUtils.isNotBlank(url)){
                        			url = url.replaceFirst(request.getServletContext().getContextPath(), "");
                        		}
                        		if(StringUtils.isBlank(error) && StringUtils.isNotBlank(url) && !url.startsWith("/login")&& !url.startsWith("/favicon.ico") && !url.equals("/")){
                        			error = "zh".equalsIgnoreCase(request.getLocale().getLanguage())? "请登录后进行操作。":"Please login before performing any operations.";
                        		}
								
								if(error != null){
									if(error.endsWith("UnknownAccountException") || error.endsWith("IncorrectCredentialsException")){
										error = "zh".equalsIgnoreCase(request.getLocale().getLanguage())? "邮箱或密码错误。":"Email or password error.";
									}
									if(error.endsWith("LockedAccountException")){
									    error = "zh".equalsIgnoreCase(request.getLocale().getLanguage())? "账号已过期(1个月内唯有任何登入记录).请<a target='_blank' href='https://xmo.optimix.asia/zh-cn/admin/forget_password'>按此</a>重新设定密码以激活账户.":
                                        "Account has expired(No login records within 1 months). Please <a target='_blank' href='https://xmo.optimix.asia/en/admin/forget_password'>click here</a> to reset password for activation.";
									}
								%>
									<div class="alert alert-danger alert-dismissable">
	                                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
	                                <%=error%>
	                            </div>
								<%
								}
								%>
                                <div class="form-group">
                                	<label for="username"><spring:message code="index.username"/>:</label>
                                    <input class="form-control" placeholder="<spring:message code="index.username"/>" name="username" type="text"  value="${username}" autofocus>
                                </div>
                                <div class="form-group">
                                	<label for="password"><spring:message code="index.password" />:</label>
                                    <input class="form-control" placeholder="<spring:message code="index.password" />" name="password" type="password" value="">
                                </div>
                                 <c:if test="${jcaptchaEbabled}">
                                 <div class="form-group input-group">
                                   	<label for="jcaptchaCode"><spring:message code="index.verificationcode" />:</label><br/>
									<input type="text" id="jcaptchaCode" name="jcaptchaCode" class="form-control col-sm-6" style="width: 50%">
									<img class="col-sm-6 jcaptcha-btn jcaptcha-img" style="height: 34px" id="jcaptchaCodeImg" src="${pageContext.request.contextPath}/jcaptcha.jpg" title="<spring:message code='index.verificationcode' />">
									
                                </div>
                                </c:if>
                               
                                <div class="checkbox">
                                    <label>
                                        <input name="rememberMe" type="checkbox" value="true"><spring:message code="index.rememberme" />
                                    </label>
                                </div>
                                <!-- Change this to a button or input when using this as a form -->
                                
                                <input id="submit_btn" class="btn btn-lg btn-success btn-block" type="submit" value="<spring:message code="public.login"/>"/>
                            </fieldset>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

   
	
</body>
<script>
		$(document).ready(function() {
			$("#loginForm").validate({
				errorPlacement: function(error, element) {
					if (element.attr("name") == "jcaptchaCode") {
						 error.insertAfter("#jcaptchaCodeImg");
					} 
					else if (element.parent('.input-group').length || element.prop('type') === 'checkbox' || element.prop('type') === 'radio') {
			            error.insertAfter(element.parent());
			        } else {
			            error.insertAfter(element);
			        }
			    },
			    rules: {
			    	username:"required",
			    	password:"required",
			    	jcaptchaCode:"required",
			    }
			   
			});
			
			 $(".jcaptcha-btn").click(function() {
		            $(".jcaptcha-img").attr("src", '${pageContext.request.contextPath}/jcaptcha.jpg?'+new Date().getTime());
		        });
		});
	</script>
</html>

