<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<title><spring:message code="report.CustBaseInfo.title" /></title>
</head>
<body>
	<br/>
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<form id="searchForm" action="${ctx}/reports/custBaseInfoList" method="get">
		<div class="row">
			<div class="col-lg-12">
				<div class="panel panel-info">
					<div class="panel-heading">
						<spring:message code="report.CustBaseInfo.title" />
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
									<label for="custUsername"><spring:message code="custom.custUsername" /></label> <input type="text"
										class="form-control" id="custUsername" name="custUsername"
										value="<c:out value="${pages.searchMap['custUsername']}"/>"
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
									<th <tags:sort column="custName" page="${pages}"/>><spring:message code="report.CustBaseInfo.custName" /></th>
									<th <tags:sort column="advertiser" page="${pages}"/>><spring:message code="report.CustBaseInfo.advertiser" /></th>
									<th <tags:sort column="custUsername" page="${pages}"/>><spring:message code="report.CustBaseInfo.custUsername" /></th>
									<th><spring:message code="report.CustBaseInfo.contractNo" /></th>
									<th><spring:message code="report.CustBaseInfo.recordPayerName" /></th>
									<th><spring:message code="report.CustBaseInfo.custPort" /></th>
									<th><spring:message code="report.CustBaseInfo.am_contact" /></th>
									<th><spring:message code="report.CustBaseInfo.rewardsPercent" /></th>
									<th><spring:message code="report.CustBaseInfo.mgtFeePercent" /></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${pages.content}" var="custMaster" varStatus="index">
									<tr class="${index.count%2==0?'odd':'even'}">
										<td>${custMaster.custName}</td>
										<td>${custMaster.advertiser}</td>
										<td>${custMaster.custUsername}</td>
										<td>N / A</td>
										<td>N / A</td>
										<td>${custMaster.custPort}</td>
										<td>${custMaster.am_contact}</td>
										<td class="text-right">
												<c:set var="isNum" value="2" />
												<c:if test="${fn:startsWith(custMaster.rewardsPercent,'0') || fn:startsWith(custMaster.rewardsPercent,'1')}">
													<fmt:formatNumber value="${custMaster.rewardsPercent}" type="percent"></fmt:formatNumber>
													<c:set var="isNum" value="1" />
												</c:if>
												<c:if test="${ isNum == 2 }">
													${custMaster.rewardsPercent}
												</c:if>
												
										</td>
										<td class="text-right">
												<c:set var="isNum" value="2" />
												<c:if test="${fn:startsWith(custMaster.mgtFeePercent,'0') || fn:startsWith(custMaster.mgtFeePercent,'1')}">
													<fmt:formatNumber value="${custMaster.mgtFeePercent}" type="percent"></fmt:formatNumber>
													<c:set var="isNum" value="1" />
												</c:if>
												<c:if test="${ isNum == 2 }">
													${custMaster.mgtFeePercent}
												</c:if>
										</td>
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
		});
		
		function downloadReport(){
			var act = $("#searchForm").attr("action");
			$("#searchForm").attr("action","${ctx}/reports/downloadCustBaseInfo");
			$('#searchForm').submit();
			$("#searchForm").attr("action",act);
		};
	</script>
</body>
</html>