<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<input type="hidden" value="${groupId}" name="groupId" id="groupId" />
<div class="row">
	<c:forEach items="${users}" var="user">
		<div class="col-md-6 col-user">
			<c:if test="${ 'Active' ne user.status}">
				<span style="color: red;">${user.name}</span>
			</c:if>
			<c:if test="${ 'Active' eq user.status}">
				${user.name}
			</c:if>
			&nbsp;
			<a href="javascript:void(0);" onclick="delUser(${groupId},${user.id},'${user.name}')">
			<i class="fa fa-w fa-times" ></i>
			</a>
		</div>
	</c:forEach>
</div>


<script>
	function delUser(groupId,userId,username){
			
		swal({   
			title: "<spring:message code='public.confirmdel' />?",   
			text: "",   
			type: "warning",   
			showCancelButton: true,   
			confirmButtonColor: "#DD6B55",   
			confirmButtonText: "<spring:message code="public.confirm"/>",   
			cancelButtonText: "<spring:message code="public.cancel"/>",
			closeOnConfirm: false 
			}, 
			function(){ 
				confirmDelUser(groupId,userId,username);
				swal("<spring:message code='public.deleted.success' />!", username+" <spring:message code='user.deleted.success'/>.", "success");
			}
		);
	
	};
	
	 function confirmDelUser(groupId,userId,username){
		 
		$.post("${ctx}/group/del/"+groupId+"/"+userId,{},function(data){
			if(data=='success'){
				//var html = "<div id='message' class='alert alert-success'>";
				//html += "<button data-dismiss='alert' class='close'>×</button>"+username+"&nbsp;<spring:message code='user.deleted.success'/></div>";
				//$(".modal-title").after(html);
			}
			loadGroupUser(groupId);	
		},"text");
			
	 };
</script>