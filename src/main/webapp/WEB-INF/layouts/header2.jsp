<%@page import="org.apache.shiro.SecurityUtils"%>
<%@page import="com.asgab.service.account.ShiroDbRealm.ShiroUser"%>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
 <!-- Navigation -->
        <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
               
                <a class="navbar-brand" style="padding-top: 10px;padding-left: 15px;padding-bottom: 10px;" href="#">
                	<img alt="Brand" height="30px;" width="180px;" src="${ctx}/static/images/CSA_logo_final.png">
                </a>
            </div>
            <!-- /.navbar-header -->

            <ul class="nav navbar-top-links navbar-right">
            	  <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                        <spring:message code="index.welcome" />, <shiro:principal property="name"/>
                    </a>
                    <!-- /.dropdown-user -->
                </li>
                <!-- /.dropdown -->
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                        <i class="fa fa-user fa-fw"></i>  <i class="fa fa-caret-down"></i>
                    </a>
                    <ul class="dropdown-menu dropdown-user">
                    	<%
                    	ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
                    	%>
                       <%--  <li><a href="${ctx}/profile"><i class="fa fa-user fa-fw"></i><spring:message code="header.userprofile" /></a>
                        </li> --%>
                        <!-- 
                        <li><a href="${ctx}/profile"><i class="fa fa-key fa-fw"></i></i><spring:message code="header.changepassword" /></a>
                        </li>
                         -->
                        <!-- <li class="divider"></li> -->
                        <li><a href="${ctx}/logout"><i class="fa fa-sign-out fa-fw"></i><spring:message code="header.logout" /></a>
                        </li>
                    </ul>
                    <!-- /.dropdown-user -->
                </li>
                <!-- /.dropdown -->
            </ul>
            <!-- /.navbar-top-links -->
             <div class="navbar-default sidebar" role="navigation">
                <div class="sidebar-nav navbar-collapse">
                    <ul class="nav" id="side-menu">
                    	
                        
                        <li>
                            <a href="${ctx}/paytran"><i class="fa fa-tasks fa-fw"></i><spring:message code="menu.paytran" /></a>
                        </li>
                         <shiro:hasAnyRoles name="admin,finance,am,ops">
                        <li>
                            <a href="#"><i class="fa fa-sitemap fa-fw"></i><spring:message code="menu.maintenance" /><span class="fa arrow"></span></a>
                            <ul class="nav nav-second-level">
                                   <shiro:hasAnyRoles name="admin,ops">
                                  	<li>
			                        	 <a href="${ctx}/custMaster"><i class="fa fa-child fa-fw"></i><spring:message code="menu.client" /></a>
			                        </li>
			                        </shiro:hasAnyRoles>
			                        <shiro:hasAnyRoles name="admin,finance">
			                        <li>
			                            <a href="${ctx}/exchangeRate"><i class="fa fa-money fa-fw"></i><spring:message code="menu.currency" /></a>
			                        </li>
			                        <li>
			                            <a href="${ctx}/paymentPurpose"><span class="glyphicon glyphicon-grain"></span><spring:message code="menu.payment.purpose" /></a>
			                        </li>
			                      	</shiro:hasAnyRoles>
                            </ul>
                            <!-- /.nav-second-level -->
                        </li>
                        </shiro:hasAnyRoles>

                        <li>
                            <a href="#"><i class="fa fa-gear fa-fw"></i><spring:message code="menu.setting" /><span class="fa arrow"></span></a>
                            <ul class="nav nav-second-level">
                                 <shiro:hasRole name="admin">
		                        <li>
		                            <a href="${ctx}/user"><i class="fa fa-user fa-fw"></i><spring:message code="menu.user" /></a>
		                        </li>
		                        </shiro:hasRole>
		                        <%--<shiro:hasRole name="admin">--%>
		                        <li>
		                            <a href="${ctx}/group"><i class="fa fa-group fa-fw"></i><spring:message code="menu.group" /></a>
		                        </li>
		                        <%--</shiro:hasRole>--%>
		                        <shiro:hasRole name="admin">
		                        	<li>
		                            	<a href="${ctx}/mail"><i class="fa fa-envelope fa-fw"></i><spring:message code="menu.mail" /></a>
		                        	</li>
		                        </shiro:hasRole>
                            </ul>
                            <!-- /.nav-second-level -->
                        </li>

                      
                        <li>
                            <a href="#"><i class="fa fa-list-ul fa-fw"></i><spring:message code="menu.report" /><span class="fa arrow"></span></a>
                            <ul class="nav nav-second-level">
                            	<li>
		                            <a href="${ctx}/reports/custBaseInfoList"><spring:message code="menu.report.cust.base.info" /></a>
		                        </li>
		                        <li>
		                            <a href="${ctx}/reports/custPaymentInfoList"><spring:message code="menu.report.cust.payment.info" /></a>
		                        </li>
                            </ul>
                        </li>
                        
                        <shiro:hasAnyRoles name="admin">
                        <li>
                            <a href="${ctx}/log"><i class="fa fa-list-ol fa-fw"></i><spring:message code="menu.log" /></a>
                        </li>
                        </shiro:hasAnyRoles>
                    </ul>
                </div>
                <!-- /.sidebar-collapse -->
            </div>
            <!-- /.navbar-static-side -->
        </nav>