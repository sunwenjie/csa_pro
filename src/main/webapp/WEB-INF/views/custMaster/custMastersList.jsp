<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<title><spring:message code="custom.list" /></title>
</head>
<body>
	<br/>
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<form id="searchForm" action="${ctx}/custMaster" method="get">
		<div class="row">
			<div class="col-lg-12">
				<div class="panel panel-info">
					<div class="panel-heading">
						<spring:message code="custom.list" />
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
									<label for="custPort"><spring:message code="custom.custPort" /></label> <input type="text"
										class="form-control" id="custPort" name="custPort"
										value="<c:out value="${pages.searchMap['custPort']}"/>"
										placeholder="<spring:message code="custom.custPort" />">
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
							<div class="col-lg-4">
								<div class="form-group">
									<label for="custName"><spring:message code="custom.custName" /></label> <input type="text"
										class="form-control" id="custName" name="custName"
										value="<c:out value="${pages.searchMap['custName']}"/>"
										placeholder="<spring:message code="custom.custName" />">
								</div>
							</div>
							
						</div>
					</div>
					<div class="panel-footer panel-footer-search">
						<div class="btn-group" role="group" aria-label="...">
							<button type="submit" class="btn btn-info"><i class="fa fa-search"></i> <spring:message code="public.search" /></button>
							<button id="resetButtom" type="button" class="btn  btn-warning"><i class="fa fa-repeat"></i> <spring:message code="public.reset" /></button>
						</div>
						<button style="margin-left: 10px;" type="button" class="btn btn-outline btn-primary pull-right" onclick="window.location.href='${ctx}/custMaster/download'">
							 	<i class="fa fa-download "></i> <spring:message code="public.download.template" />
						</button>
						 <shiro:hasAnyRoles name="admin,ops">
							<button type="button" class="btn btn-outline btn-primary pull-right" data-toggle="modal" data-target="#myModal">
							 	<i class="fa fa-upload "></i> <spring:message code="public.upload" />
							</button>
						</shiro:hasAnyRoles>
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
									<th><spring:message code="custom.custPort" /></th>
									<th <tags:sort column="custUsername" page="${pages}"/>><spring:message code="custom.custUsername" /></th>
									<th <tags:sort column="fin_email" page="${pages}"/>><spring:message code="custom.fin_email" /></th>
									<th <tags:sort column="sales_contact" page="${pages}"/>><spring:message code="custom.sales_contact" /></th>
									<th <tags:sort column="am_contact" page="${pages}"/>><spring:message code="custom.am_contact" /></th>
									<th <tags:sort column="ops_contact" page="${pages}"/>><spring:message code="custom.ops_contact" /></th>
									<th><spring:message code="custom.sales_email" /></th>
									<th><spring:message code="custom.am_email" /></th> 
									<th><spring:message code="custom.ops_email" /></th>
									<th><spring:message code="custom.custName" /></th>
									<th><spring:message code="custom.webName" /></th>
									<th><spring:message code="custom.advertiser" /></th>
									<th <tags:sort column="acctCreateDate" page="${pages}"/>><spring:message code="custom.acctCreateDate" /></th>
									<th><spring:message code="custom.annualSvcFeeDate" /></th>
									<th><spring:message code="custom.annualSvcFee" /></th>
									<th><spring:message code="custom.rewardsPercent" /></th>
									<th><spring:message code="custom.mgtFeePercent" /></th>
									<th><spring:message code="custom.remark1" /></th>
									<%-- <th><spring:message code="public.oper" /></th> --%>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${pages.content}" var="custMaster" varStatus="index">
									<tr class="${index.count%2==0?'odd':'even'}">
										<td style="cursor: pointer;" ondblclick="javascript:window.location.href='${ctx}/custMaster/update/${custMaster.id}';">${custMaster.custPort}</td>
										<td>${custMaster.custUsername}</td>
										<td>${custMaster.fin_email}</td>
										<td>${custMaster.sales_contact}</td>
										<td>${custMaster.am_contact}</td>
										<td>${custMaster.ops_contact}</td>
										<td>${custMaster.sales_email}</td>
										<td>${custMaster.am_email}</td>
										<td>${custMaster.ops_email}</td>
										<td>${custMaster.custName}</td>
										<td>${custMaster.webName}</td>
										<td>${custMaster.advertiser}</td>
										<td><fmt:formatDate value="${custMaster.acctCreateDate}" pattern="yyyy-MM-dd"/></td>
										<td>
											<c:if test="${not empty custMaster.annualSvcFeeDate}">
												<fmt:formatDate value="${custMaster.annualSvcFeeDate}" pattern="yyyy年MM月"/> 
											</c:if>
											<c:if test="${empty custMaster.annualSvcFeeDate}">
												-
											</c:if>
										</td>
										<td class="text-right">${custMaster.annualSvcFee}</td>
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
										<td>${custMaster.remark1}</td>
										 <%-- <td class="text-center">
											<a href="${ctx}/custMaster/update/${custMaster.id}"><i class="fa fa-pencil fa-fw"></i></a> 
										</td>  --%>
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
	
	<!-- modal -->
	<jsp:include page="/WEB-INF/views/custMaster/upload.jsp" flush="true"></jsp:include>

	
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
			
		 	$("#myModal").on("hidden.bs.modal", function() {
		 		 window.location.reload();
			}); 
			
		});
	
	</script>
</body>
</html>
