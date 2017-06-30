<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title><spring:message code="user.title"/></title>
</head>

<body>
	<div class="row">
	    <div class="col-lg-12">
	        <h3 class="page-header"><spring:message code="header.userprofile"/></h3>
	    </div>
	    <!-- /.col-lg-12 -->
	</div>
	<!-- /.row -->
	<div class="row">
	    <div class="col-lg-12">
	        <div class="panel panel-default">
	            <div class="panel-body">
	                <div class="row">
	                    <div class="col-lg-6">
	                        <form role="form" id="inputForm" action="${ctx}/profile/update" method="post">
	                        	<input type="hidden" name="id" value="${user.id}"/>
	                        	<c:if test="${not empty success }">
	                        		<div class="alert alert-success alert-dismissable">
	                                	<button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
	                                	${ success }
	                            	</div>
	                            </c:if>
	                            <div class="form-group">
	                                <label><spring:message code="user.name" />:</label>
                       				<input class="form-control" type="text" id="name" name="name" value="${user.name}">
	                            </div>
	                            <div class="form-group">
	                                <label><spring:message code="index.password" />:</label>
                       				<input class="form-control" type="password" id="plainPassword" name="plainPassword" />
	                            </div>
	                            <div class="form-group">
	                            	<label><spring:message code="user.confirmpassword" />:</label>
                       				<input class="form-control" type="password" id="confirmPassword" name="confirmPassword" />
	                            </div>
	                            <button id="submit_btn" type="submit" class="btn btn-success"><spring:message code="public.commit" /></button>
                            </form>
                        </div>
                   </div>
               </div>
           </div>
       </div>
   </div>
	<script>
		$(document).ready(function() {
			$("#inputForm").validate({
				rules: {
					name:"required",
					plainPassword:{
						required: true,
						minlength: 6
					},
					confirmPassword:{
						required: true,
						minlength: 6,
						equalTo: "#plainPassword"
					}
			    }
			});
		});
	</script>
</body>
</html>
