<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title><spring:message code="curr.manage"/></title>
</head>

<body>
	<div class="row">
	    <div class="col-lg-12">
	        <h1 class="page-header"><spring:message code="curr.manage"/></h1>
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
	                        <form role="form" id="inputForm" action="${ctx}/currMaster/${action}" method="post">
	                        	<input type="hidden" name="id" value="${currMaster.id}"/>
	                        	<c:if test="${not empty message}">
									<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
								</c:if>
								<div class="form-group">
	                                <label><spring:message code="curr.code" />:</label>
                       				<input class="form-control" type="text" id="curr_code" name="curr_code" value="${currMaster.curr_code}">
	                            </div>
	                            <div class="form-group">
	                                <label><spring:message code="curr.name" />:</label>
                       				<input class="form-control" type="text" id="curr_name" name="curr_name" value="${currMaster.curr_name}">
	                            </div>
	                            <div class="form-group">
	                                <label><spring:message code="curr.rate" />:</label>
                       				<input class="form-control" type="text" id="curr_rate" name="curr_rate" value="${currMaster.curr_rate}">
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
					curr_code:"required",
					curr_name:"required",
					curr_rate:{
						required: true,
						number:true
					}
			    }
			});
		});
	</script>
</body>
</html>
