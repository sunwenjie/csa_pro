<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<title><spring:message code="report.CustPaymentInfo.title" /></title>
</head>
<body>
	<br/>
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<form id="searchForm" action="${ctx}/reports/custPaymentInfoList" method="get">
		<div class="row">
			<div class="col-lg-12">
				<div class="panel panel-info">
					<div class="panel-heading">
						<spring:message code="report.CustPaymentInfo.title" />
						<small></small>
						<button type="button"
							class="btn-mini btn-link pull-right search-plus-minus">
							<i class="fa fa-search-minus"></i>
						</button>
					</div>
					<div class="panel-body panel-body-search">
						<div class="row">
							<div class="col-lg-4">
								<div class="form-group">
									<label for="advertiser"><spring:message code="custom.advertiser" /></label> <input type="text"
										class="form-control" id="advertiser" name="advertiser"
										value="<c:out value="${pages.searchMap['advertiser']}"/>"
										placeholder="<spring:message code="custom.advertiser" />">
								</div>
							</div>
							<div class="col-lg-4">
								<div class="form-group">
									<label for="paymentDateStart"><spring:message code="report.CustPaymentInfo.paymentDate.start"/></label> 
									<div class="input-group date form_date" data-date-format="dd MM yyyy" id="datetimepicker" data-link-format="yyyy-mm-dd">
	                            		<input class="form-control" type="text" value="${pages.searchMap['paymentDateStart']}" id="paymentDateStart" name="paymentDateStart"  
	                            		placeholder="<spring:message code="report.CustPaymentInfo.paymentDate.start"/>">
										<span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span>
	                            	</div>
								</div>
							</div>
							<div class="col-lg-4">
								<div class="form-group">
									<label for="paymentDateEnd"><spring:message code="report.CustPaymentInfo.paymentDate.end"/></label> 
									<div class="input-group date form_date" data-date-format="dd MM yyyy" id="datetimepicker" data-link-format="yyyy-mm-dd">
	                            		<input class="form-control" type="text" value="${pages.searchMap['paymentDateEnd']}" id="paymentDateEnd" name="paymentDateEnd"  
	                            		placeholder="<spring:message code="report.CustPaymentInfo.paymentDate.end"/>">
										<span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span>
	                            	</div>
								</div>
							</div>
							<div class="col-lg-4">
								<div class="form-group">
									<label for="bdUserName"><spring:message code="custom.custUsername" /></label> <input type="text"
										class="form-control" id="bdUserName" name="bdUserName"
										value="<c:out value="${pages.searchMap['bdUserName']}"/>"
										placeholder="<spring:message code="custom.custUsername" />">
								</div>
							</div>
						</div>
					</div>
					<div class="panel-footer panel-footer-search">
						<div class="btn-group" role="group" aria-label="...">
							<button type="submit" class="btn btn-info"><i class="fa fa-search"></i> <spring:message code="public.search" /></button>
							<button id="resetButtom" type="button" class="btn  btn-warning"><i class="fa fa-repeat"></i> <spring:message code="public.reset" /></button>
						</div>
						
						<button type="button" class="btn btn-outline btn-primary pull-right" onclick="downloadReport();">
						 	<i class="fa fa-download "></i> <spring:message code="public.download" />
						</button>
					</div>
				</div>
			</div>
		</div>
	</form>

	<div class="row">
		<div class="col-lg-12">
			<div class="panel panel-default">
				<!-- /.panel-heading -->
				<div class="panel-body">
					<div class="table-responsive">
						<table class="table table-striped dataTable table-hover" style="margin-bottom:0px;">
							<thead>
								<tr>
									<th <tags:sort column="paymentDate" page="${pages}"/>><spring:message code="report.CustPaymentInfo.paymentDate" /></th>
									<th><spring:message code="report.CustPaymentInfo.custName" /></th>
									<th <tags:sort column="advertiser" page="${pages}"/>><spring:message code="report.CustPaymentInfo.advertiser" /></th>
									<th><spring:message code="report.CustPaymentInfo.payerName" /></th>
									<th><spring:message code="report.CustPaymentInfo.custPort" /></th>
									<th><spring:message code="report.CustPaymentInfo.topupDate" /></th>
									<th><spring:message code="report.CustPaymentInfo.bdusername" /></th>
									<th><spring:message code="report.CustPaymentInfo.opsDate" /></th>
									<th><spring:message code="report.CustPaymentInfo.currency" /></th>
									<th><spring:message code="report.CustPaymentInfo.amount" /></th>
									<th><spring:message code="report.CustPaymentInfo.convertToRMB" /></th>
									<th><spring:message code="report.CustPaymentInfo.topupAmount" /></th>
									<th><spring:message code="report.CustPaymentInfo.realAmount" /></th>
									<th><spring:message code="report.CustPaymentInfo.description" /></th>
									<th><spring:message code="report.CustPaymentInfo.discountAmount" /></th>
									<th><spring:message code="report.CustPaymentInfo.acctCreateFeeAmount" /></th>
									<th><spring:message code="report.CustPaymentInfo.annualFeeAmount" /></th>
									<th><spring:message code="report.CustPaymentInfo.guaranteeFeeAmount" /></th>
									<th><spring:message code="report.CustPaymentInfo.mgtFeeAmount" /></th>
									<th><spring:message code="report.CustPaymentInfo.custType" /></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${pages.content}" var="custPaymentInfo" varStatus="index">
									<tr class="${index.count%2==0?'odd':'even'}">
										<td>
											<fmt:formatDate value="${custPaymentInfo.paymentDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
										</td>
										<td>${custPaymentInfo.custName}</td>
										<td>${custPaymentInfo.advertiser}</td>
										<td>N / A</td>
										<td>${custPaymentInfo.custPort}</td>
										<td>
											<fmt:formatDate value="${custPaymentInfo.topupDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
										</td>
										<td >${custPaymentInfo.bdUserName}</td>
										<td>
											<fmt:formatDate value="${custPaymentInfo.opsDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
										</td>
										<td >${custPaymentInfo.currency}</td>
										<td class="text-right">
											<fmt:formatNumber value="${custPaymentInfo.amount}" type="currency" pattern="#,##0.00" />
										</td>
										<td class="text-right">
											<fmt:formatNumber value="${custPaymentInfo.amountInRmb}" type="currency" pattern="#,##0.00" />
										</td>
										<td class="text-right">
											<fmt:formatNumber value="${custPaymentInfo.topupAmount}" type="currency" pattern="#,##0.00" />
										</td>
										<td class="text-right">
										<c:if test="${'1' eq custPaymentInfo.payCode}">
											<c:if test="${custPaymentInfo.realAddAmount gt 0.0}"><fmt:formatNumber value="${custPaymentInfo.realAddAmount}" type="currency" pattern="#,##0.00" /></c:if>
											<c:if test="${(custPaymentInfo.realAddAmount le 0.0) && (empty custPaymentInfo.description)}"><fmt:formatNumber value="${custPaymentInfo.topupAmount}" type="currency" pattern="#,##0.00" /></c:if>
										</c:if>
										</td>
										<td>
										<%-- <c:if test="${'1' eq custPaymentInfo.payCode}"> --%>
											<c:if test="${(empty custPaymentInfo.description) || fn:length(custPaymentInfo.description) <= 8}">
												${custPaymentInfo.description}
											</c:if>
											<c:if test="${!(empty custPaymentInfo.description) && fn:length(custPaymentInfo.description) > 8}">
												${fn:substring(custPaymentInfo.description,0, 8)}...
											</c:if>
										<%-- </c:if> --%>
										</td>
										<td class="text-right">
											<fmt:formatNumber value="${custPaymentInfo.rebateAmount}" type="currency" pattern="#,##0.00" />
										</td>
										<td class="text-right">
											<fmt:formatNumber value="${custPaymentInfo.acctCreateFeeAmount}" type="currency" pattern="#,##0.00" />
										</td>
										<td class="text-right">
											<fmt:formatNumber value="${custPaymentInfo.annualFeeAmount}" type="currency" pattern="#,##0.00" />
										</td>
										<td class="text-right">
											<fmt:formatNumber value="${custPaymentInfo.guaranteeFeeAmount}" type="currency" pattern="#,##0.00" />
										</td>
										<td class="text-right">
											<fmt:formatNumber value="${custPaymentInfo.mgtFeeAmount}" type="currency" pattern="#,##0.00" />
										</td>
										<td>${custPaymentInfo.custRemark}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					<!-- /.table-responsive -->
					
					<tags:pagination page="${pages}" paginationSize="5" />
				</div>
				<!-- /.panel-body -->
			</div>
			<!-- /.panel -->
		</div>
	</div>
	
	<script>
		$(document).ready(function() {
			$(".search-plus-minus").on("click", function() {
				var i = $(this).children(":first");
				if (i.attr("class").indexOf("fa-search-minus") > 0) {
					i.removeClass("fa-search-minus")
					i.addClass("fa-search-plus")
				} else {
					i.removeClass("fa-search-plus")
					i.addClass("fa-search-minus")
				}
				if ($(".panel-body-search").is(":visible") == false) {
					$(".panel-body-search").show();
				} else {
					$(".panel-body-search").hide();
				}
			});

			$("#resetButtom").on("click", function() {
				$('.form-control').val('');
			});
		 	
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
			
		});
		
		function downloadReport(){
			var act = $("#searchForm").attr("action");
			$("#searchForm").attr("action","${ctx}/reports/downloadCustPaymentInfo");
			$('#searchForm').submit();
			$("#searchForm").attr("action",act);
		};
	</script>
</body>
</html>