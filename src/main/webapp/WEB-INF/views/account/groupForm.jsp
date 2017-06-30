<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<title><spring:message code="group.title" /></title>
</head>

<body>
	<div class="row">
		<div class="col-lg-12">
			<h1 class="page-header">
				<spring:message code="group.title" />
			</h1>
		</div>
		<!-- /.col-lg-12 -->
	</div>
	<form role="form" id="inputForm" action="${ctx}/group/${action}"
		method="post">
		<!-- /.row -->
		<div class="row">
			<div class="col-lg-12">
				<div class="panel panel-default">
					<div class="panel-body">
						<div class="row">
							<div class="col-lg-6">
								<input type="hidden" name="id" id="id" value="${group.id}" />
								<c:if test="${not empty message}">
									<div id="message" class="alert alert-success">
										<button data-dismiss="alert" class="close">×</button>${message}</div>
								</c:if>
								<div class="form-group">
									<label><spring:message code="group.group_name" />:</label> <input
										class="form-control" type="text" id="group_name" name="group_name"
										value="${group.group_name}">
								</div>
								
								<!-- 
								<div class="form-group" >
									<label><spring:message code="group.function_code" />:</label>
									<input class="form-control" type="text" id="function_code" name="function_code" value="${group.function_code}">
								</div>
								 -->
								 
								<div class="form-group">
									<label><spring:message code="group.description" />:</label> 
									<textarea rows="3" class="form-control" id="description" name="description">${group.description}</textarea>
								</div>
								
								<button id="submit_btn" type="submit" class="btn btn-success">
									<spring:message code="public.commit" />
								</button>

							</div>
							<div class="col-lg-6">
								
								<div class="form-group">
									<label><spring:message code="group.group_type" />:</label>
										<tags:selectbox name="group_type" map="${group.groupTypes}" add0="false" value="${group.group_type}"></tags:selectbox>
								</div>
								
								<div class="form-group">
									<label><spring:message code="group.status" />:</label>
									<tags:selectbox name="status" map="${group.statuses}" add0="false" value="${group.status}"></tags:selectbox>
								</div>
								
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</form>
	<script>
		$(document).ready(function() {
			$("#inputForm").validate({
				rules : {
					group_type : "required",
					group_name:{required:true,
						remote:{ 
							url : "${ctx}/group/checkGroupName",
							type : "post",
							dataType: 'json',
							data : {
								group_name : function(){ return $("#group_name").val(); },
								id : function(){ return $("#id").val(); }
							},
							dataFilter : function(data, type) {
								// 如果找到了就不允许
								if (data == "true")
									return false;
								else
									return true;
							}
						}	
					}
				},
				 messages:{
					 group_name:{
			    		remote:'<spring:message code="group.name.exist" />'
			    	}
			    }
			});
		});
	</script>
</body>
</html>
