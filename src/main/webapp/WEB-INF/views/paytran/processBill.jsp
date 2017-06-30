<%@page import="com.asgab.entity.PayTranHeader"%>
<%@page import="com.asgab.entity.Process"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<style type="text/css">
	ul{list-style: none;padding: 0;}
	.li-date{background-color: #f6f6f6;padding: 5px;border-left: 5px solid #F00;}
</style>

<%
	if(request.getAttribute("payTranHeader")==null){
		if(request.getAttribute("validError")!=null){
			%>
			
	<script type="text/javascript">
		$(".myValidCode-error").remove();
		$("#searchPaytranValidCode").removeClass("has-error");
		$("#searchPaytranValidCode").parent().removeClass("has-error");
		$(".popupJcaptcha").after("<span class='label label-danger myValidCode-error' ><spring:message code="public.verificationcode.error"/></span>");
		$("#searchPaytranValidCode").addClass("has-error");
		$("#searchPaytranValidCode").parent().addClass("has-error");
	</script>
			
			<%
		}
		if(request.getAttribute("searchError")!=null){
			%>
			<label style="color: red;padding:0 15px 0 15px;"><spring:message code="process.search.error"/></label>
			<%
		}
		if(request.getAttribute("searchErrorEN")!=null){
			%>
			<label style="color: red;padding:0 15px 0 15px;"><spring:message code="process.search.error2"/></label>
			<%
		}
	}else{
		%>
		<div class="col-lg-6 col-md-6 col-sm-6">
		<ul>
			<li class="li-date"><spring:message code="paytran.status" /></li>
			<ul style="margin: 5px 0 0 20px;">
				<li>
					<%
						PayTranHeader payTranHeader = (PayTranHeader)request.getAttribute("payTranHeader");
						boolean isZH = "zh".equalsIgnoreCase(request.getLocale().getLanguage());
						String[] statusZH = {"","查账中","查账拒绝","加款中","加款中","完成","完成","加款拒绝"};  
						String[] statusEN = {"","Checking","Payment rejected","Topup in progress",
						    "Topup in progress","Completed","Completed","Topup rejected"};
						int index = (int)payTranHeader.getStatus()-48;
						%>
						<%=isZH?statusZH[index]:statusEN[index] %>
						<%
					%>
				</li>
			</ul>
		</ul>
		</div>
		<%
	}

%>

