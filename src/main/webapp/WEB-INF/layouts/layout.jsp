<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>
    	 <sitemesh:title/>
    </title>

	<style type="text/css">
		table th, td{word-break: keep-all;white-space:nowrap;}
	</style>
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
	
	<!-- datetimepicker -->
    <link href="${ctx}/static/bootstrap/3.3.5/css/bootstrap-datetimepicker.min.css" rel="stylesheet">
	

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

   <!-- ajaxupload -->
   <!-- 
   <script src="${ctx}/static/ajaxupload/ajaxupload.3.6.js"></script>
    -->

    <!-- Custom Theme JavaScript -->
    <script src="${ctx}/static/startbootstrap-sb-admin-2-1.0.7/dist/js/sb-admin-2.js"></script>
    
    <!-- 
    <script src="${ctx}/static/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
     -->
	<script src="${ctx}/static/jquery-validation/1.14.0/dist/jquery.validate.js" type="text/javascript"></script>
	<script src="${ctx}/static/jquery-validation/1.14.0/dist/jquey.validate.override.js" type="text/javascript"></script>
	
	<!-- datepickerjs -->
	<script src="${ctx}/static/bootstrap/3.3.5/js/bootstrap-datetimepicker.min.js" type="text/javascript"></script>
	<!-- tooltip -->
	<script src="${ctx}/static/startbootstrap-sb-admin-2-1.0.7/bower_components/bootstrap/js/tooltip.js" type="text/javascript"></script>
	<script src="${ctx}/static/startbootstrap-sb-admin-2-1.0.7/bower_components/bootstrap/js/popover.js" type="text/javascript"></script>
	<%
	String lang = request.getLocale().getLanguage();
	if("zh".equals(lang)){
		%>
		<script src="${ctx}/static/jquery-validation/1.14.0/dist/localization/messages_zh.js" type="text/javascript"></script>
		<%
	}
	%>

</head>

<body>
    <div id="wrapper">
		<%@ include file="/WEB-INF/layouts/header2.jsp"%>
        <div id="page-wrapper">
          <sitemesh:body/>
        </div>
    </div>
    <script type="text/javascript">
    <!-- 3秒后自动关闭成功提示框，错误的不自动关闭 -->
    //window.setTimeout(function() { $(".alert-success").alert('close'); }, 3000);
    </script>
</body>

</html>
