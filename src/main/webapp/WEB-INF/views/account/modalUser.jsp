<%@page import="com.asgab.entity.Group"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<style>
	.form-group{margin-bottom:0}
</style>
<form role="form" id="userGroupsForm" action="${ctx}/group/update/user" method="post">
	<input type="hidden" value="${userId}" name="userId" />
	<input type="hidden" value="<%=request.getHeader("Referer")%>" name="searchUrl" />
	<div class="row">
	<%
		List<Group> groups = (List<Group>)request.getAttribute("groups");
		int half =  (int)Math.ceil((double)groups.size()/2);
		%>
		<div class="col-md-6">
		<%
		for(int i = 0 ; i < half ; i++){
			%>
			<div class="form-group" >
				<input type="checkbox" class="userGroups" name="userGroups"  value="<%=groups.get(i).getId() %>"
				
				<% if(("checked").equals(groups.get(i).getChecked())){
					
				%>
					checked
				<% 
				} 
				%>
				>
				<%=groups.get(i).getGroup_name() %>
			</div>
			<%
		}
		%>
		</div>
		<div class="col-md-6">
		<%
		for(int i = half; i<groups.size();i++){
			%>
			<div class="form-group" >
				<input type="checkbox" class="userGroups" name="userGroups"  value="<%=groups.get(i).getId() %>" 
				
				<% if(("checked").equals(groups.get(i).getChecked())){
					
				%>
					checked
				<% 
				} 
				%>
				
				>
				<%=groups.get(i).getGroup_name() %>
			</div>
			<%
		}
	%>
		</div>
	</div>
</form>

<script>
	function updateUserGroups(){
		if($("input:checked").length>0){
			$("#userGroupsForm").submit();
		}else{
			
			swal({   
				title: "<spring:message code='public.is.continue' />?",   
				text: "<spring:message code="user.del.last"/>?",   
				type: "warning",   
				showCancelButton: true,   
				confirmButtonColor: "#DD6B55",   
				confirmButtonText: "<spring:message code="public.continue"/>",   
				cancelButtonText: "<spring:message code="public.cancel"/>",
				closeOnConfirm: false 
				}, 
				function(){ 
					$("#userGroupsForm").submit();
				}
			);
			
		}
	};
</script>
