<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title><spring:message code="exchangerate.manage"/></title>
</head>

<body>
	<div class="row">
	    <div class="col-lg-12">
	        <h1 class="page-header"><spring:message code="exchangerate.manage"/></h1>
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
	                        <form role="form" id="inputForm" action="${ctx}/exchangeRate/${action}" method="post">
	                        	<input type="hidden" name="id" value="${exchangeRate.id}"/>
	                        	<c:if test="${not empty message}">
									<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
								</c:if>
								<div class="form-group">
	                                <label><spring:message code="exchangerate.base.currency" />:</label>
                       				<tags:selectbox add0="true" name="base_currency" map="${currencys}" value="${exchangeRate.base_currency}"></tags:selectbox>
	                            </div>
	                            <div class="form-group">
	                                <label><spring:message code="exchangerate.currency" />:</label>
                       				<tags:selectbox add0="true" name="currency" map="${currencys}" value="${exchangeRate.currency}"></tags:selectbox>
	                            </div>
	                            <div class="form-group">
	                                <label><spring:message code="exchangerate.rate" />:</label>
                       				<input class="form-control" type="text" id="rate" name="rate" value="${exchangeRate.rate}">
	                            </div>
	                            <div class="form-group">
	                                <label><spring:message code="exchangerate.start.date" />:</label>
                       				<div class="input-group date form_date" data-date-format="dd MM yyyy" id="datetimepicker" data-link-format="yyyy-mm-dd">
	                            		<input class="form-control" type="text" value='<fmt:formatDate value="${exchangeRate.start_date}" pattern="yyyy-MM-dd"/>' id="start_date" name="start_date"  placeholder="<spring:message code="exchangerate.start.date"/>" >
										<span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span>
	                            	</div>
                       				
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
			$(".form_date").datetimepicker({
				format: 'yyyy-mm-dd',
				weekStart: 1,
		        todayBtn:  1,
				autoclose: 1,
				todayHighlight: 1,
				startView: 2,
				minView: 2,
				forceParse: 0
			});
			
			$("#inputForm").validate({
				rules: {
					base_currency:{
						required: true,
					},
					currency:{
						required: true,
					},
					rate:{
						required: true,
						number:true
					},
					start_date:{
						required: true,
						date:true,
						dateISO:true
					}
			    }
			});
		});
	</script>
</body>
</html>
