<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<title><spring:message code="user.list" /></title>
</head>
<body>
	<link href="${ctx}/static/sweetalert/sweetalert.css" rel="stylesheet"></link>
	
	<script src="${ctx}/static/typeahead/bootstrap-typeahead.min.js"></script>
	<script src="${ctx}/static/sweetalert/sweetalert.min.js"></script>

	<br />
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success">
			<button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<form id="searchForm" action="${ctx}/user" method="get">
		<div class="row">
			<div class="col-lg-12">
				<div class="panel panel-info">
					<div class="panel-heading">
						<spring:message code="user.list" />
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


									<label for="name"><spring:message code="user.name" /></label>
									<input type="text" class="form-control" id="name" name="name"
										value="<c:out value="${pages.searchMap['name']}"/>"
										placeholder="<spring:message code="user.name" />">
								</div>
							</div>
							<div class="col-lg-4">
								<div class="form-group">
									<label for="loginName"><spring:message
											code="index.loginname" /></label> <input type="text"
										class="form-control" id="loginName" name="loginName"
										value="<c:out value="${pages.searchMap['loginName']}"/>"
										placeholder="<spring:message
											code="index.loginname" />">
								</div>
							</div>
							<div class="col-lg-4">
								<div class="form-group">
									<label for="status"><spring:message code="user.status" /></label> 
											<tags:selectbox name="status" map="${statuses}" value="${pages.searchMap['status']}"></tags:selectbox>
								</div>
							</div>
							
						</div>
					</div>
					<div class="panel-footer panel-footer-search">
						<div class="btn-group" role="group" aria-label="...">
							<button type="submit" class="btn  btn-info">
								<i class="fa fa-search"></i>
								<spring:message code="public.search" />
							</button>
							<button id="resetButtom" type="button" class="btn  btn-warning">
								<i class="fa fa-repeat"></i>
								<spring:message code="public.reset" />
							</button>
						</div>

						<%-- <a class="btn  btn-primary pull-right" href="${ctx}/user/create"><i
							class="fa fa-edit"></i> <spring:message code="public.create" /></a> --%>
							<%--
							<button type="button" class="btn btn-default pull-right" data-toggle="modal" data-target="#myModal">
						 	 <spring:message code="public.upload" />
							</button>
							--%>
						<button style="display: none;" type="button" class="btn btn-default pull-right" onclick="resendFailureMails();">
					 	 	Resend Failure Mails
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
						<table class="table table-striped  table-hover dataTable" style="margin-bottom:0px;">
							<thead>
								<tr>
									<th <tags:sort column="name" page="${pages}"/>><spring:message code="user.name" /></th>
									<th <tags:sort column="loginName" page="${pages}"/>><spring:message code="index.loginname" /></th>
									<th ><spring:message code="user.email" /></th>
									<th <tags:sort column="status" page="${pages}"/>><spring:message code="user.status" /></th>
									<th style="text-align: right;"><spring:message code="public.oper" /></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${pages.content}" var="user" varStatus="index">
									<tr class="${index.count%2==0?'odd':'even'}">
										<td>${user.name}</td>
										<td>${user.loginName}</td>
										<td>${user.email}</td>
										<td>${user.status}</td>
										<td style="text-align: right;"><a href="javascript:void(0);" onclick="showGroupModal(${user.id});"><i class="fa fa-w fa-edit"></i></a></td>
										
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
	
	<!-- modal  -->
	<div class="modal fade" id="groupInfoModal">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title"><spring:message code="user.group.list"/></h4>
	      </div>
	      <div class="modal-body">
	        	
	      </div>
	      <div class="modal-footer"><!-- data-dismiss="modal" -->
	        <button type="button" class="btn btn-success"  onclick="updateUserGroups();"><spring:message code="public.commit"/></button>
	      </div>
	    </div><!-- /.modal-content -->
	  </div><!-- /.modal-dialog -->
	</div><!-- /.modal -->
	
	<div id="shield" style="position: fixed; left: 0px; top: 0px; display: none; z-index: 9998; opacity: 0.8; background: #7D7159; width: 100%; height: 100%;"></div>

	
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
				$("#name").val("");
				$("#loginName").val("");
				$("#status").val("0");
			});
			
		});
		
		function resendFailureMails(){
			$.post("${ctx}/user/resend/mails",{},function(data){
				if(data==-1){
					alert("resend error. please check server.");
				}else{
					alert(data+" mails had been resent.");
				}
			},"text");
		};

		function showGroupModal(id){
			$.post("${ctx}/group/find/user/"+id,{},function(html){
				$(".modal-body").html(html);
			},"html");
			$("#groupInfoModal").modal('show');
		};
		
	</script>
</body>
</html>
