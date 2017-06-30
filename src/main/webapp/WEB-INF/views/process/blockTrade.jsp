<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="org.apache.shiro.authc.ExcessiveAttemptsException"%>
<%@ page import="org.apache.shiro.authc.IncorrectCredentialsException"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="en">
<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title></title>

    <!-- Bootstrap Core CSS -->
    <link href="${ctx}/static/startbootstrap-sb-admin-2-1.0.7/bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- MetisMenu CSS -->
    <link href="${ctx}/static/startbootstrap-sb-admin-2-1.0.7/bower_components/metisMenu/dist/metisMenu.min.css" rel="stylesheet">

    <!-- Timeline CSS -->
    <link href="${ctx}/static/startbootstrap-sb-admin-2-1.0.7/dist/css/timeline.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="${ctx}/static/startbootstrap-sb-admin-2-1.0.7/dist/css/sb-admin-2.css" rel="stylesheet">

    <!-- Morris Charts CSS -->
    <link href="${ctx}/static/startbootstrap-sb-admin-2-1.0.7/bower_components/morrisjs/morris.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="${ctx}/static/startbootstrap-sb-admin-2-1.0.7/bower_components/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

	<link href="${ctx}/static/jquery-validation/1.11.1/validate.css" type="text/css" rel="stylesheet" />

	

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
      <!-- jQuery -->
    <script src="${ctx}/static/startbootstrap-sb-admin-2-1.0.7/bower_components/jquery/dist/jquery.min.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="${ctx}/static/startbootstrap-sb-admin-2-1.0.7/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>

    <!-- Metis Menu Plugin JavaScript -->
    <script src="${ctx}/static/startbootstrap-sb-admin-2-1.0.7/bower_components/metisMenu/dist/metisMenu.min.js"></script>

   

    <!-- Custom Theme JavaScript -->
    <script src="${ctx}/static/startbootstrap-sb-admin-2-1.0.7/dist/js/sb-admin-2.js"></script>
    <!-- 
    <script src="${ctx}/static/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
     -->
	<script src="${ctx}/static/jquery-validation/1.14.0/dist/jquery.validate.js" type="text/javascript"></script>
	<script src="${ctx}/static/jquery-validation/1.14.0/dist/jquey.validate.override.js" type="text/javascript"></script>
	<%
	String lang = request.getLocale().getLanguage();
	if(lang.equals("zh")){
		%>
		<script src="${ctx}/static/jquery-validation/1.14.0/dist/localization/messages_zh.js" type="text/javascript"></script>
		<%
	}
	%>

</head>

<body>

    <div class="container">
        <div class="row">
            <div class="col-lg-12">
                <div class="panel panel-default">
                    <div class="panel-body">
                    <div class="row">
                    
                   	<c:if test="${error == null }">
                     <form role="form" id="inputForm" action="${ctx}/process/blockTradeCheck" method="post">
                     	<input type="hidden" name="encodedId" value="${process.encodedProcessId}">
                     	<input type="hidden" name="randomKey" value="${process.randomKey}">
                     	<input type="hidden" name="randomIdentification" value="${process.randomIdentification}">
	                    <div class="col-lg-8 col-lg-offset-2">
	                    	<label><spring:message code="am.blockTrade.title" /></label>
	                    	<div class="form-group">
		                    	<table class="table table-bordered table-striped">
		                    		<thead>
		                    			<tr>
		                    				<th><spring:message code="am.blockTrade.thirtyday" /></th>
		                    				<th><spring:message code="am.blockTrade.fifteenday" /></th>
		                    			</tr>
		                    		</thead>
		                    		<tbody>
		                    			<tr>
		                    				<td>
		                    					<input id="amount1" name="amount30" class="amount" type="text" style="width: 100%;text-align: right;" onkeyup="check('amount1');" placeholder="请填写">
		                    					<span id="erroramount1" class="label label-danger" style="display: none;"></span>
		                    				</td>
		                    				<td>
		                    					<input id="amount2" name="amount15" class="amount" type="text" style="width: 100%;text-align: right;" onkeyup="check('amount2');" placeholder="请填写">
		                    					<span id="erroramount2" class="label label-danger" style="display: none;"></span>
		                    				</td>
		                    			</tr>
		                    		</tbody>
		                    	</table>
	                    	</div>
	                    	
	                    	<div class="form-group">
	                    		<label><spring:message code="am.blockTrade.upload"/>:</label>
	                    		<span id="attachment-error" style="display: none">
							    	<spring:message code="public.fileempty"/>
							    </span>
	                    		<div id="filesDIV">
	                    			<button type="button" class="btn btn-info pull-left" data-toggle="modal" data-target="#myModal">
								 		 <spring:message code="public.upload"/>
									</button><br/><br/>
									<ul id="filesLabel">
									</ul>
	                    		</div>
	                    	</div>
	                    	
	                    	<div class="form-group" id="dumpImageDiv">
                            </div>
                            
                            <jsp:include page="upload.jsp"></jsp:include>
	                    	
	                    	<div class="form-group">
	                    		<button type="button" onclick="btnSubmit();" class="btn btn-success pull-right"><spring:message code='public.commit' /></button>
	                    	</div>
	                    </div>
	                  </form>
                   	</c:if>
	                  </div>
                    
                    </div>
                </div>
            </div>
        </div>
    </div>

   <div id="shield" style="position: fixed; left: 0px; top: 0px; display: none; z-index: 9998; opacity: 0.8; background: #7D7159; width: 100%; height: 100%;">
		<img src="${ctx}/static/images/loading_s.gif" style="position: absolute; top: 300px; left: 48%;" /></div>
	
</body>
<script>
		
		function btnSubmit(){
		  $("#erroramount1").removeAttr("style");
		  $("#erroramount2").removeAttr("style");
		  check("amount1");
		  check("amount2");
		  if($("#erroramount1").css('display') != 'none' || $("#erroramount1").css('display') != 'none'){
	        return "";
	      }
	      var amount1 = $("#amount1").val().replace(/(^s*)|(s*$)/g, "");
	      var amount2 = $("#amount2").val().replace(/(^s*)|(s*$)/g, "");
	      if(amount1.length > 0 && amount2.length > 0){
	      	$("#erroramount1").html('<spring:message code="am.blockTrade.twoNum"/>');
	      	$("#erroramount1").removeAttr("style");
	        return;
	      }
	      if(amount1.length == 0 && amount2.length == 0){
	      	$("#erroramount1").html('<spring:message code="am.blockTrade.oneNum"/>');
	      	$("#erroramount1").removeAttr("style");
	        return;
	      }
	      if(isNaN(amount1)){
	      	$("#erroramount1").html('<spring:message code="public.validateNum"/>');
	      	$("#erroramount1").removeAttr("style");
	        return;
	      }
	      if(isNaN(amount2)){
	      	$("#erroramount2").html('<spring:message code="public.validateNum"/>');
	      	$("#erroramount2").removeAttr("style");
	        return;
	      }
	      //判断文件上传
	      $("#attachment-error").hide();
	      var attachments = $("input[name^='attachment']").length;
	      if(attachments <= 0){
	    	  $("#attachment-error").addClass('label label-danger').show();
	    	  return false;
	      }
	      $("#inputForm").submit();
		};
		
		function delAttach(attachmentId,name){
			if(confirm('<spring:message code="public.confirmdel"/>?')){
				$.post('${ctx}/ajax/delAttach/'+attachmentId,{},function(data){
					var html = "<div  class=\"alert alert-success\"><button data-dismiss=\"alert\" class=\"close\">×</button>"+data+"</div>";
					$("#filesDIV").append(html);
				},'text');
				$(name).parent().remove();
				$("#attachment"+attachmentId).remove();
				$("#filename"+attachmentId).remove();
				$("#uploadImg"+attachmentId).remove();
				syncAttach();
			}
		};
		
		// 同步 form 和 上传页面的数据一致
		function syncAttach(){
			var html = "";
			$("#filesLabel li").each(function(){
				html = html +"<li><code style='color:#5CB85C'>"+$(this).text()+"</code></li>"
			});
			alert(html);
			$("#messange").html(html);
		};

    function check(inputId) {
      var i = $("#"+inputId).val();
      if ( isNaN(i) ) {
        $("#error"+inputId).html("<spring:message code='public.validateNum'/>");
        $("#error"+inputId).removeAttr("style");
      }
      else{
        $("#error"+inputId).css('display','none');
      }
    }
	</script>
</html>

