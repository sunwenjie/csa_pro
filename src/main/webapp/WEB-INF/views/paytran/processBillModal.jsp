<%@page import="com.asgab.entity.Process"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<style>
	.label-danger{display: inline-block;margin-top: 1px;}
</style>

   <div class="modal fade" id="searchPaytranModal">
	  <div class="modal-dialog">
	    <div class="modal-content">
	    
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title"><spring:message code="process.bill.search"/></h4>
	      </div>
	      
	      <div class="modal-body">
	      <form id="searchProcessForm">
	      
	      <div class="row">
	      <div class="col-lg-6 col-md-6 col-sm-6">
	      	<div class="form-group">
	      		<input class="form-control"  id="searchPaytranId" name="searchPaytranId" placeholder="<spring:message code='paytran.search.process.trannum' />" style="width: 100%;font-size: 13px;" />
	      	</div>
	      	
	      	<div class="form-group">
		      	<input class="form-control col-sm-6" id="searchPaytranValidCode" name="searchPaytranValidCode" placeholder="<spring:message code='index.verificationcode' />" style="width: 50%;font-size: 13px;">
				<img class="col-sm-6 jcaptcha-btn jcaptcha-img popupJcaptcha" style="height: 34px" id="searchPaytranValidImage" src="${pageContext.request.contextPath}/jcaptcha.jpg" title="<spring:message code='index.verificationcode' />">
		    </div>
	      	
	      </div>
	       <div class="col-lg-6 col-md-6 col-sm-6">
	       	<div class="form-group">
	       		<input type="button" class="btn btn-info" id="btnSearchPaytran" value="<spring:message code='public.query' />">
	       	</div>
	       </div>
	       
	      </div>
	      
	       </form>
	      
	      <div class="row bill-body">
	      </div>
	      
	      </div>
	      
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="public.close"/></button>
	      </div>
	      
	    </div><!-- /.modal-content -->
	  </div><!-- /.modal-dialog -->
	</div><!-- /.modal -->
	
	
	<script>
	$(document).ready(function() {
		$("#searchPaytranValidCode").keyup(function(){
			$(".myValidCode-error").remove();
			$("#searchPaytranValidCode").removeClass("has-error");
			$("#searchPaytranValidCode").parent().removeClass("has-error");
		});
		
		var validatorSearch = $("#searchProcessForm").validate({
			rules: {
				searchPaytranId:{
					required: true,
					number:true,
					rangelength:[6,6]
				},
				searchPaytranValidCode:{
					required: true
					
				}
		    },messages:{
		    	searchPaytranId:{
		    		required:'<spring:message code="public.required" />',
		    		number:'<spring:message code="please.input.valid.trannum" />',
		    		rangelength:'<spring:message code="please.input.valid.trannum" />'
		    	},
		    	searchPaytranValidCode:{
		    		required:'<spring:message code="public.required" />'
		    	}
		    },
		    errorPlacement: function(error, element) {
				if (element.attr("name") == "searchPaytranValidCode") {
					error.insertAfter("#searchPaytranValidImage");
				} 
				else {
		            error.insertAfter(element);
		        }
		    }
		});
		
		$("#btnSearchPaytran").click(function(){
			var paytranId = $("#searchPaytranId").val();
			if(validatorSearch.form()){
				$.post('${ctx}/process/processBill/'+paytranId,{"searchPaytranValidCode":$("#searchPaytranValidCode").val()},function(html){
					$(".bill-body").html(html);
					$("#searchPaytranValidImage").attr("src", '${pageContext.request.contextPath}/jcaptcha.jpg?'+new Date().getTime());
				},"html");
			}
		});
	});
	
	function showSearchPaytranModal(){
		$("#searchPaytranModal").modal("show");
		$("#searchPaytranValidCode").val('');
		$("#searchPaytranId").val('');
		$(".bill-body").html('');
		
		$(".myValidCode-error").remove();
		$("#searchPaytranValidCode").removeClass("has-error");
		$("#searchPaytranValidCode").parent().removeClass("has-error");
		$("#searchPaytranId").parent().removeClass("has-error");
		
		$("#searchProcessForm").validate().resetForm();
	};
	</script>