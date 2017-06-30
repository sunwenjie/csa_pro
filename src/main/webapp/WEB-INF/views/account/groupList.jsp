<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
	<title><spring:message code="group.list" /></title>
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
	<c:if test="${not empty errorMessage}">
		<div id="message" class="alert alert-warning">
			<button data-dismiss="alert" class="close">×</button>${errorMessage}</div>
	</c:if>
	<form id="searchForm" action="${ctx}/group" method="get">
		<div class="row">
			<div class="col-lg-12">
				<div class="panel panel-info">
					<div class="panel-heading">
						<spring:message code="group.list" />
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
									<label for="group_name"><spring:message code="group.name" /></label>
									<input type="text" class="form-control" id="group_name" name="group_name"
										value="<c:out value="${pages.searchMap['group_name']}"/>"
										placeholder="<spring:message code="group.name" />">
								</div>
							</div>
								
							<div class="col-lg-4">
								<div class="form-group">
									<label for="status"><spring:message code="group.status" /></label>
									<tags:selectbox name="status" map="${statuses}" value="${pages.searchMap['status']}" add0="true"></tags:selectbox>
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
						<a class="btn  btn-primary pull-right" href="${ctx}/group/create"><i class="fa fa-edit"></i> <spring:message code="public.create" /></a>
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
									<th <tags:sort column="group_name" page="${pages}"/>><spring:message code="group.name" /></th>
									<th <tags:sort column="group_type" page="${pages}"/>><spring:message code="group.group_type" /></th>
									<th ><spring:message code="group.member.count" /></th>
									<th <tags:sort column="status" page="${pages}"/>><spring:message code="group.status" /></th>
									<th><spring:message code="group.description" /></th>
									<th style="text-align: right;"><spring:message code="public.oper" /></th>
								</tr>
								<tr>
							</thead>
							<tbody>
								<c:forEach items="${pages.content}" var="group" varStatus="index">
									<tr class="${index.count%2==0?'odd':'even'}">
										<td>${group.group_name}</td>
										<td>${group.group_type }</td>
										<td>${group.memberCount }</td>
										<td>${group.status }</td>
										<td>${group.description }</td>
										<td style="text-align: right;">
										<a href="javascript:void(0);" onclick="showGroupModal(${group.id},'${group.group_name}');"><i class="fa fa-w fa-search"></i></a>
											<shiro:hasRole name="admin">
										<a href="javascript:void(0);" onclick="editGroup(${group.id})"><i class="fa fa-w fa-edit"></i></a>
										<a href="javascript:void(0);" onclick="delGroup(${group.id})"><i class="fa fa-w fa-times-circle"></i></a></td>
										</shiro:hasRole>
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
	        <button type="button" class="close" onclick="$('#groupInfoModal').modal('hide');location.reload();"  aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title"><spring:message code="user.list"/><span id="groupNameSpan"></span></h4>
	      </div>
	      <div class="modal-body">
	      		
	      </div>
	      <div class="modal-footer">
	      	<form class="form-inline">
	        	<input type="text" class="userNameTypeahead form-control" placeholder="<spring:message code='user.input.name'/>">
	        	<input type="hidden" class="hiddenUserId">
	        	<input type="hidden" class="hiddenUserName">
	        	<input type="button" class="addUser btn btn-success" onclick="addUser();" value="<spring:message code="user.add"/>">
	        </form>
	      </div>
	    </div><!-- /.modal-content -->
	  </div><!-- /.modal-dialog -->
	</div><!-- /.modal -->
	
	<div id="shield" style="position: fixed; left: 0px; top: 0px; display: none; z-index: 9998; opacity: 0.8; background: #7D7159; width: 100%; height: 100%;"></div>

	<script>
	
		$(document).ready(function() {
			// [{id:,name:},{}]
			
			$.get('${ctx}/user/all',{},function(data){
				$(".userNameTypeahead").typeahead({ source: data,onSelect:function(item){
					$(".hiddenUserId").val(item.value);
					$(".hiddenUserName").val(item.text);
				}});
			},"json");
			
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
				$("#group_name").val("");
				$("#status").val("0");
			});
			
		});
		
		
		
		function showGroupModal(id,name){
			$("#groupNameSpan").html("&nbsp;("+name+")");
			loadGroupUser(id);
			$(".alert-success").alert('close');
			$("#groupInfoModal").modal("show");
		};
		
		function loadGroupUser(id){
			$.post("${ctx}/group/find/group/"+id,{},function(html){
				$(".modal-body").html(html);
			},"html");
		};
		
		function addUser(){
			var userId = $(".hiddenUserId").val();
			var username = $(".hiddenUserName").val();
			var groupId = $("#groupId").val();
			if(userId!='' && username!=''){
				$.post("${ctx}/group/add/"+groupId+"/"+userId,{},function(data){
					if(data=='success'){
						var html = "<div id='message' class='alert alert-success'>";
						html += "<button data-dismiss='alert' class='close'>×</button>"+username+"&nbsp;<spring:message code='user.add.success'/></div>";
						$(".modal-title").after(html);
					}
					loadGroupUser(groupId);
					$(".hiddenUserId").val('');
					$(".hiddenUserName").val('');
					$(".userNameTypeahead").val('');
				},"text");
			}
		};
		
		function editGroup(id){
			window.location.href="${ctx}/group/update/"+id;
		}
		
		function delGroup(groupId){
			if(confirm("<spring:message code='public.confirmdel' />?")){
				window.location.href="${ctx}/group/del/"+groupId;
			}
		}
	</script>
</body>
</html>
