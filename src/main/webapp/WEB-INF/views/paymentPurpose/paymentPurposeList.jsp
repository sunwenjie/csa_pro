<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<title><spring:message code="pay.purpose.list" /></title>
</head>
<body>
	<br/>
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<c:if test="${not empty warning}">
		<div id="warning" class="alert alert-warning"><button data-dismiss="alert" class="close">×</button>${warning}</div>
	</c:if>
	<form id="searchForm" action="${ctx}/paymentPurpose" method="get">
		<div class="row">
			<div class="col-lg-12">
				<div class="panel panel-info">
					<div class="panel-heading">
						<spring:message code="pay.purpose.list" />
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
									<label for="pay_purpose"><spring:message code="pay.purpose.name.zh" /></label> 
									<input type="text" class="form-control" id="pay_purpose" name="pay_purpose"
										value="<c:out value="${pages.searchMap['pay_purpose']}"/>"
										placeholder="<spring:message code="pay.purpose.name.zh" />">
								</div>
							</div>
							
							<div class="col-lg-4">
								<div class="form-group">
									<label for="pay_purposeEN"><spring:message code="pay.purpose.name.en" /></label> 
									<input type="text" class="form-control" id="pay_purposeEN" name="pay_purposeEN"
										value="<c:out value="${pages.searchMap['pay_purposeEN']}"/>"
										placeholder="<spring:message code="pay.purpose.name.en" />">
								</div>
							</div>
						</div>
					</div>
					<div class="panel-footer panel-footer-search">
						<div class="btn-group" role="group" aria-label="...">
							<button type="submit" class="btn btn-info"><i class="fa fa-search"></i> <spring:message code="public.search" /></button>
							<button id="resetButtom" type="button" class="btn  btn-warning"><i class="fa fa-repeat"></i> <spring:message code="public.reset" /></button>
						</div>
						
						<a class="btn  btn-primary pull-right" href="${ctx}/paymentPurpose/create"><i class="fa fa-edit"></i> <spring:message code="public.create" /></a>
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
									<th <tags:sort column="pay_purpose" page="${pages}"/>><spring:message code="pay.purpose.name.zh" /></th>
									<th <tags:sort column="pay_purposeEN" page="${pages}"/>><spring:message code="pay.purpose.name.en" /></th>
									<th><spring:message code="public.oper" /></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${pages.content}" var="paymentPurpose" varStatus="index">
									<tr class="${index.count%2==0?'odd':'even'}">
										<td>${paymentPurpose.pay_purpose}</td>
										<td>${paymentPurpose.pay_purposeEN}</td>
										<td>
											<a href="${ctx}/paymentPurpose/update/${paymentPurpose.id}"><i class="fa fa-pencil fa-fw"></i></a> 
											<a href="javascript:if(confirm('delete?'))window.location.href='${ctx}/paymentPurpose/delete/${paymentPurpose.id}'"><i class="fa fa-times fa-fw"></i></a>
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
	
	</script>
</body>
</html>