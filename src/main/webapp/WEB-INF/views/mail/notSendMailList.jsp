<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<title><spring:message code="mail.list" /></title>
</head>
<body>
	<br/>
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	
	<c:if test="${fn:length(mails) gt 0}">
	<div class="row">
		<div class="col-lg-12">
			<a style="margin-bottom: 15px;" class="btn btn-info pull-right" href="javascript:void(0);" onclick="send();"><spring:message code="mail.resend" /></a>
		</div>
	</div>
	</c:if>

	<div class="row">
		<div class="col-lg-12">
			<div class="panel panel-default">
				<!-- /.panel-heading -->
				<div class="panel-body">
					<div class="table-responsive">
						<table class="table table-striped dataTable table-hover" style="margin-bottom:0px;">
							<thead>
								<tr>
									<th><spring:message code="mail.id" /></th>
									<th><spring:message code="mail.receiver" /></th>
									<th><spring:message code="mail.subject" /></th>
									<th><spring:message code="mail.paytranNum" /></th>
									<th><spring:message code="mail.createDate" /></th>
									<th><spring:message code="mail.status" /></th>
								</tr>
							</thead>
							<tbody>
								<c:if test="${fn:length(mails) eq 0}">
									<tr>
										<td colspan="6" align="center">
										<spring:message code="mail.none"></spring:message>
										</td>
									</tr>
								</c:if>
								<c:forEach items="${mails}" var="mail" varStatus="index">
									<tr class="${index.count%2==0?'odd':'even'}">
										<td>${mail.id}</td>
										<td>${mail.receiver}</td>
										<td>${mail.subject}</td>
										<td>${mail.paytranNum}</td>
										<td><fmt:formatDate value="${mail.createDate}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td>
										<td><spring:message code='mail.toSend' /></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					<!-- /.table-responsive -->
				</div>
				<!-- /.panel-body -->
			</div>
			<!-- /.panel -->
		</div>
	</div>
	<div id="shield" style="position: fixed; left: 0px; top: 0px; display: none; z-index: 9998; opacity: 0.8; background: #7D7159; width: 100%; height: 100%;">
		<img src="${ctx}/static/images/loading_s.gif" style="position: absolute; top: 300px; left: 48%;" /></div>

	
	<script>
		$(document).ready(function() {
			
			
		});
		
		function send(){
			$("#shield").show();
			$.post("${ctx}/mail/resend",{},function(result){
				$("#shield").hide();
				if(result=="-1"){
					alert("<spring:message code='mail.send.error' />");
				}else{
					alert("<spring:message code='mail.send.success1' />! "+result+" <spring:message code='mail.send.success2' />");
				}
				window.location.href="${ctx}/mail";
			},"text");
		};
	
	</script>
</body>
</html>
