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
	<form id="searchForm" action="${ctx}/log" method="get">
		<div class="row">
			<div class="col-lg-12">
				<div class="panel panel-info">
					<div class="panel-heading">
						<spring:message code="sys.log" />
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
									<label for="pKey"><spring:message code="sys.log.pkey" /></label> <input type="text"
										class="form-control" id="pKey" name="pKey"
										value="<c:out value="${pages.searchMap['pKey']}"/>"
										placeholder="<spring:message code="sys.log.pkey" />">
								</div>
							</div>
							<div class="col-lg-4">
								<div class="form-group">
									<label for="roles"><spring:message code="sys.log.module" /></label>
									<tags:selectbox add0="false" name="module" map="${modules}" value="${pages.searchMap['module']}"></tags:selectbox>
								</div>
							</div>
						</div>
					</div>
					<div class="panel-footer panel-footer-search">
						<div class="btn-group" role="group" aria-label="...">
							<button type="submit" class="btn btn-info"><i class="fa fa-search"></i> <spring:message code="public.search" /></button>
							<button id="resetButtom" type="button" class="btn  btn-warning"><i class="fa fa-repeat"></i> <spring:message code="public.reset" /></button>
						</div>
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
									<th><spring:message code="sys.log.module" /></th>
									<th><spring:message code="sys.log.pkey" /></th>
									<th><spring:message code="sys.log.operate.by" /></th>
									<th><spring:message code="sys.log.operate.time" /></th>
									<th><spring:message code="sys.log.operate.type" /></th>
									<th><spring:message code="sys.log.content" /></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${pages.content}" var="log" varStatus="index">
									<tr class="${index.count%2==0?'odd':'even'}">
										<td><tags:decode items="${modules}" value="${log.module}"></tags:decode></td>
										<td>${log.pKey}</td>
										<td>${log.operateBy}</td>
										<td><fmt:formatDate value="${log.operateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
										<td><tags:decode items="${operateTypes}" value="${log.operateType}"></tags:decode></td>
										<td>
											<c:if test="${not empty log.filePath }">
												<a href="${ctx}/file/download/${log.id}">${log.remark}</a>
												
											</c:if>
											<c:if test="${empty log.filePath }">
												<c:if test="${pages.searchMap['lang'] == 'zh' }">
														${log.remark1}
												</c:if>	
												<c:if test="${pages.searchMap['lang'] != 'zh' }">
														${log.remark2}
												</c:if>	
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
				$("#searchForm input").val('');
				$("#searchForm select option:first").attr("selected","selected");
			});
			
		});
	
	</script>
</body>
</html>
