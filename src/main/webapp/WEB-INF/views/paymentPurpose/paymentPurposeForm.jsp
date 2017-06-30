<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title><spring:message code="pay.purpose.manage"/></title>
</head>

<body>
	<div class="row">
	    <div class="col-lg-12">
	        <h1 class="page-header"><spring:message code="pay.purpose.manage"/></h1>
	    </div>
	    <!-- /.col-lg-12 -->
	</div>
	<!-- /.row -->
	<div class="row">
	    <div class="col-lg-12">
	        <div class="panel panel-default">
	            <div class="panel-body">
	                <div class="row">
	                    <div class="col-lg-6">
	                        <form role="form" id="inputForm" action="${ctx}/paymentPurpose/${action}" method="post">
	                        	<input type="hidden" name="id" value="${paymentPurpose.id}"/>
	                        	<c:if test="${not empty message}">
									<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
								</c:if>
	                            <div class="form-group">
	                                <label><spring:message code="pay.purpose.name.zh" />:</label>
                       				<input class="form-control" type="text" id="pay_purpose" name="pay_purpose" value="${paymentPurpose.pay_purpose}">
	                            </div>
	                            <div class="form-group">
	                                <label><spring:message code="pay.purpose.name.en" />:</label>
                       				<input class="form-control" type="text" id="pay_purposeEN" name="pay_purposeEN" value="${paymentPurpose.pay_purposeEN}">
	                            </div>
	                           
	                            <button id="submit_btn" type="submit" class="btn btn-success"><spring:message code="public.commit" /></button>
                            </form>
                        </div>
                   </div>
               </div>
           </div>
       </div>
   </div>
	<script>
		$(document).ready(function() {
			$("#inputForm").validate({
				rules: {
					pay_purpose:"required",
					pay_purposeEN:"required"
			    }
			});
		});
	</script>
</body>
</html>
