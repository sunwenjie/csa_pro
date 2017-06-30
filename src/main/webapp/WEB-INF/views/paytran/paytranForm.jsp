<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	
	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title><spring:message code="paytran.title"/></title>

	<style type="text/css">
		table th, td{white-space:nowrap;word-break: keep-all;}
		#header-title{font-size: 36px;margin-top: 20px;margin-bottom: 10px;font-family: inherit;font-weight: 500;line-height: 1.1;color: inherit;}
		#header-title-query{font-size: 14px;}
		.label-danger{display: inline-block;margin-top: 1px;}
		#header-title-query a{margin-top: -12px;}
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


    <!-- Custom Theme JavaScript -->
    <script src="${ctx}/static/startbootstrap-sb-admin-2-1.0.7/dist/js/sb-admin-2.js"></script>
    
    <!-- 
    <script src="${ctx}/static/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
     -->
	<script src="${ctx}/static/jquery-validation/1.14.0/dist/jquery.validate.js" type="text/javascript"></script>
	<script src="${ctx}/static/jquery-validation/1.14.0/dist/jquey.validate.override.js" type="text/javascript"></script>
	
	<!-- datepickerjs -->
	<script src="${ctx}/static/bootstrap/3.3.5/js/bootstrap-datetimepicker.min.js" type="text/javascript"></script>
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
	<div class="container">
	<div class="row">
	    <div class="col-lg-8">
	        <label id="header-title"><spring:message code="paytran.edit"/></label>
	        <span id="header-title-query">
	        	<a href="#" class="btn btn-outline btn-info"  onclick="showSearchPaytranModal();"><spring:message code="process.bill.search" /></a>
	        </span>
	    </div>
	     <div class="col-lg-4" >
	        <img alt="logo" class="pull-right"   src="${ctx}/static/images/CSA_logo_final2.png">
	    </div>
	    <!-- /.col-lg-12 -->
	</div>
	<!-- /.row -->
	<div class="row">
	  <form role="form" id="inputForm" action="${ctx}/paytran/${action}" method="post" enctype="multipart/form-data">
	    <div class="col-lg-12">
	    	<c:if test="${not empty error}">
        		<div class="alert alert-danger alert-dismissable"><button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>${error}</div>
           	</c:if>
	    	<c:if test="${not empty successPaytran}">
        		<jsp:include page="successMessage.jsp"></jsp:include>
           	</c:if>
    
	        <div class="panel panel-default">
	            <div class="panel-body">
	                <div class="row">
	                
	                    <input type="hidden" name="tranNum" value="${payTranHeader.tranNum}"/>
	                    
                        <div class="col-lg-6">
                        		<div class="form-group div-currencys">
	                                <label for="currency"><spring:message code="paytran.currency" />
	                                :</label>
	                                <label><a target="_blank" href="${ctx}/ajax/latestExchangeRate"><spring:message code="exchangeRate.view" /></a></label>
                       				<tags:selectbox name="currency" map="${payTranHeader.currencys}" value="${payTranHeader.currency}"></tags:selectbox>
	                            </div>
                        </div>
                        <div class="col-lg-6">
                        		<div class="form-group">
	                                <label for="email"><spring:message code="paytran.email" />
	                               (<spring:message code="public.oneemail"/>)
	                                :</label>
                       				<input class="form-control" type="text" id="email" name="email" value="${payTranHeader.email}">
	                            </div>
                        </div>
                        <div class="col-lg-6">
                        		<div class="form-group">
	                                <label for="remarks"><spring:message code="paytran.remarks" />:</label>
                       				<textarea rows="3" class="form-control" id="remarks" name="remarks">${payTranHeader.remarks}</textarea>
	                            </div>
                        </div>
                        
                        <div class="col-lg-12">
                        	<div class="form-group">
							    <label for="inputFile"><spring:message code="paytran.attachment"/></label>
							    <span id="attachment-error" style="display: none">
							    	<spring:message code="public.fileempty"/>
							    </span>
							    <div id="filesDIV">
							    	<button type="button" class="btn btn-info pull-left" data-toggle="modal" data-target="#myModal">
								 		 <spring:message code="public.upload"/>
									</button>
										
									<br/><br/>
									<ul id="filesLabel">
										<c:if test="${not empty payTranHeader.payTranAttachements}">
											<c:forEach items="${payTranHeader.payTranAttachements}" var="payTranAttachement" varStatus="status">
												<label>&nbsp;&nbsp;&nbsp;&nbsp;${status.index+1}.&nbsp;${payTranAttachement.showName}<a href="javascript:void(0)" onclick="delAttach(${payTranAttachement.attachmentId},this)"><i class='fa fa-minus-circle'></i></a></label><br/>
											</c:forEach>
										</c:if>
									</ul>
									<c:if test="${not empty payTranHeader.payTranAttachements}">
										<c:forEach items="${payTranHeader.payTranAttachements}" var="payTranAttachement">
											<input type='hidden' name='attachments' id='attachment${payTranAttachement.attachmentId}' value='${payTranAttachement.attachmentId}'>
										</c:forEach>
									</c:if>
							    </div>
						  	</div>
                        </div>
                        
                        <div class="col-lg-12">
                        		<div class="form-group" id="dumpImageDiv">
                        			<c:if test="${not empty payTranHeader.payTranAttachements}">
                        				<c:forEach items="${payTranHeader.payTranAttachements }" var="attach">
                        					<c:if test="${attach.booleanPDF eq true }">
	                        					<img id="uploadImg${attach.attachmentId}" src="${ctx}/static/images/pdf.png" style="height:120px;width:100px;margin-right:10px;" />
                        					</c:if>
                        					<c:if test="${attach.booleanPDF ne true }">
    	                    					<img id="uploadImg${attach.attachmentId}" src="${ctx}/file/dumpImage?path=${attach.path}/${attach.fileName}" style="height:120px;width:100px;margin-right:10px;" />
                        					</c:if>
                        				</c:forEach>
                        			</c:if>
	                            </div>
                        </div>
                        
                        <div class="col-lg-12">
			             	<h3 class="" style="margin-top: 10px;"><spring:message code="paytran.detail.title" /></h3>
			             	<h5 class="" style="color: red"><spring:message code="paytran.detail.title.info" /></h5>
			             </div>
			             
                        <!-- pay details -->
                        <div class="col-lg-12">
						<div class="table-responsive">
							<table class="table table-striped ">
	                     		<thead>
								<tr>
									<th><spring:message code="paytran.bdusername" /></th>
									<th><spring:message code="paytran.paycode" /></th>
									<th><spring:message code="paytran.amount" /></th>
									<th><spring:message code="paytran.amountinrmb" /></th>
									<th><spring:message code="paytran.autoOnline" /></th>
									<th><spring:message code="public.oper" /></th>
								</tr>
								</thead>
								<tbody>
									<c:if test="${not empty payTranHeader.payTranDetails}">
										<c:forEach items="${payTranHeader.payTranDetails}" var="payTranDetail" varStatus="status">
											<tr class="detailTR">
									   			<td><input class="form-control bdUserName" type="text" id="bdUserName" name="bdUserName" value="${payTranDetail.bdUserName}" ></td>
									   			<td class="td-payCodes"><tags:selectbox name="payCode" map="${payTranHeader.payCodes}" value="${payTranDetail.payCode}" onchange="payCodeChange(this)"></tags:selectbox></td>
									   			<td ><input style="text-align: right;" class="form-control amount" type="text" id="amount" name="amount" value="${payTranDetail.amount}"></td>
									   			<td><input  style="text-align: right;" class="form-control amountInRMB" type="text" id="amountInRMB" name="amountInRMB" value="${payTranDetail.amountInRMB}" readonly></td>
									   			<td>
									   				<c:if test="${payTranDetail.isRechargeOnline}">
									   					<span<c:if test="${payTranDetail.payCode ne '1'}"> style="display: none;"</c:if>>
									   					<input type="radio" class="isRechargeOnline group_radio_button" name="isRechargeOnline${status.index}" value="1" checked="checked">&nbsp;&nbsp;<spring:message code="public.yes" />
									   					<input type="radio" class="isRechargeOnline group_radio_button" name="isRechargeOnline${status.index}" value="0" style="margin-left: 10px;">&nbsp;&nbsp;<spring:message code="public.no" />
									   					</span>
									   				</c:if>
									   				<c:if test="${!payTranDetail.isRechargeOnline}">
									   					<span<c:if test="${payTranDetail.payCode ne '1'}"> style="display: none;"</c:if>>
									   					<input type="radio" class="isRechargeOnline group_radio_button" name="isRechargeOnline${status.index}" value="1">&nbsp;&nbsp;<spring:message code="public.yes" />
									   					<input type="radio" class="isRechargeOnline group_radio_button" name="isRechargeOnline${status.index}" value="0" checked="checked" style="margin-left: 10px;">&nbsp;&nbsp;<spring:message code="public.no" />
									   					</span>
									   				</c:if>
									   			</td>
					                          <td>
					                            <c:if test="${status.index != 0}">
					                              <a href="javascript:void(0)" onclick="delDetailTR(this);">
					                              <i class="fa fa-minus-circle" style="margin-top: 10px;"></i>
					                              </a>
					                            </c:if>
					                            <input type="hidden" class="paytranDetailStr" name="paytranDetailStr">
					                          </td>
								   			</tr>
										</c:forEach>
									</c:if>
									<c:if test="${empty payTranHeader.payTranDetails}">
									<tr class="detailTR">
							   			<td><input class="form-control bdUserName" type="text" id="bdUserName" name="bdUserName" value=""></td>
							   			<td class="td-payCodes"><tags:selectbox name="payCode" map="${payTranHeader.payCodes}" value="" onchange="payCodeChange(this)"></tags:selectbox></td>
							   			<td><input style="text-align: right;" class="form-control amount" type="text" id="amount" name="amount" value=""></td>
							   			<td><input style="text-align: right;" class="form-control amountInRMB" type="text" id="amountInRMB" name="amountInRMB" value="" readonly></td>
					                    <td>
					                    	<span>
						   					<input type="radio" class="isRechargeOnline group_radio_button" name="isRechargeOnline0" value="1" checked="checked">&nbsp;&nbsp;<spring:message code="public.yes" />
						   					<input type="radio" class="isRechargeOnline group_radio_button" name="isRechargeOnline0" value="0" style="margin-left: 10px;">&nbsp;&nbsp;<spring:message code="public.no" />
					                    	</span>
					                    </td>
							   			<td>
							   				<input type="hidden" class="paytranDetailStr" name="paytranDetailStr">
							   			</td>
						   			</tr>
									</c:if>
									<tr>
										<td><a href="javascript:void(0)" onclick="addDetailTR(this);"><i class="fa fa-plus-circle"></i></a></td>
										<td><label><spring:message code="paytran.totalamount" />:</label></td>
										<td style="text-align: right;"><label id="totalAmount"></label></td>
										<td style="text-align: right;"><label id="totalAmountInRMB"></label></td>
										<td></td>
										<td></td>
									</tr>
								</tbody>
							</table>
						</div>
						</div>
	                   <!-- paydetails  end -->
	                   <div class="col-lg-4">
                            <div class="form-group input-group">
                             <label for="jcaptchaCode"><spring:message code="index.verificationcode" />:</label><br/>
							 <input type="text" id="jcaptchaCode" name="jcaptchaCode" class="form-control col-sm-6" style="width: 50%">
							 <img class="col-sm-6 jcaptcha-btn jcaptcha-img" style="height: 34px" id="jcaptchaCodeImg" src="${pageContext.request.contextPath}/jcaptcha.jpg" title="<spring:message code='index.verificationcode' />">
                           	</div>
	                   </div>
	                   
	                   <div class="col-lg-12">
		                       <button id="submit_btn" type="button" onclick="submitForm();" class="btn btn-success"><spring:message code="public.commit" /></button>
		               </div>
                   </div>
               </div>
           </div>
       </div>
     </form>
   </div>
   </div>
   <!-- modal -->
	<jsp:include page="upload.jsp"></jsp:include>
	
	<!-- modal  -->
	<div class="modal fade" id="submitModal">
	  <div class="modal-dialog">
	    <div class="modal-content" style="float: left;">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title"><spring:message code="public.notice"/></h4>
	      </div>
	      <div class="modal-body">
	        <p>
	        	<span><spring:message code="public.check.paytran1"/></span>
	        	<span style="color: red;font-size:16px;font-weight: bold;"><spring:message code="public.check.paytran2"/></span>
	        	<span><spring:message code="public.check.paytran3"/></span>
	        	<span style="color: red;font-size:16px;font-weight: bold;"><spring:message code="public.check.paytran4"/></span>
	        	<span><spring:message code="public.check.paytran5"/></span>
	        </p>
	        <spring:message code="paytran.trans.info"/>:
	        <ul>
	        	<li><spring:message code="paytran.info.li1"/>:&nbsp;<span id="infoli1"></span></li>
	        	<li><spring:message code="paytran.info.li2"/>:&nbsp;<span id="infoli2"></span></li>
	        	<li><spring:message code="paytran.info.li3"/>:&nbsp;<span id="infoli3"></span></li>
	        	<li><span id="infoli4"></span></li>
	        </ul>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-success" onclick="$('#submitModal').modal('hide');$('#shield').show();$('#inputForm').submit();"><spring:message code="public.confirm.submit"/></button>
	        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="public.cancel"/></button>
	      </div>
	    </div><!-- /.modal-content -->
	  </div><!-- /.modal-dialog -->
	</div><!-- /.modal -->
	
	<div id="shield" style="position: fixed; left: 0px; top: 0px; display: none; z-index: 9998; opacity: 0.8; background: #7D7159; width: 100%; height: 100%;">
		<img src="${ctx}/static/images/loading_s.gif" style="position: absolute; top: 300px; left: 48%;" /></div>
   
   <jsp:include page="processBillModal.jsp"></jsp:include>
   
	<script>
		//用于统计自动生成的tr
		var i = 1;    
		
		var keyArr = [];
		var valueArr=[];
		<c:forEach items="${currencyRates}" var="mymap" varStatus="status">
			keyArr['${status.index}'] = '${mymap.key}';
			valueArr['${status.index}'] = '${mymap.value}';
		</c:forEach>
	
		$(document).ready(function() {
			
			//判断是不是验证码错误跳转
			if($("#email").val()!=''){
				calTotalAmount();
			}
			
			// paycode change时事件
			$("tbody").on("change",".payCode",function(){
				var paycode = $(this).val();
				// 如果是加款, 则每次加款不能少于1000RMB
				if(paycode=="1"){
					var tmp1000 = get1000RMB($(".currency").val());
					$(this).parent().next().find(".amount").rules('add', {min:tmp1000});
				}else{
					$(this).parent().next().find(".amount").rules('remove', "min");
				}
			});
			
			// 货币改变
			$(".currency").change(function(){
				$(".detailTR").each(function(){
					var $tr = $(this);
					var $amount = $(this).find("td input.amount");
					$tr.find("td input.amountInRMB").val(moneyChange($(".currency").val(),$amount.val()));
				});
				calTotalAmount();
				// 货币改变,修改验证
				changeValid();
			});
			
			// 汇率转换
			$("tbody").on('keyup','.amount',function(){
				$(this).parent().next().find(".amountInRMB").val(moneyChange($(".currency").val(),$(this).val()));
				calTotalAmount();
			});
			
			
			
			$(".jcaptcha-btn").click(function() {
	            $(".jcaptcha-img").attr("src", '${pageContext.request.contextPath}/jcaptcha.jpg?'+new Date().getTime());
	        });
			
			// 添加上传按钮
			$("#filesDIV").on('click','.addInputFile',function(){
				var html = "<input type=\"file\"  name=\"files\">";
				html += "<a href=\"javascript:void(0)\" class=\"addInputFile\"><i class=\"fa fa-plus-circle\"></i></a>&nbsp;";
				html += "<a href=\"javascript:void(0)\" class=\"delInputFile\"><i class=\"fa fa-minus-circle\"></i></a>";
				$("#filesDIV").append(html);
			});
			
			$("#filesDIV").on('click','.delInputFile',function(){
				$(this).prev().prev().remove();
				$(this).prev().remove();
				$(this).remove();
			});
			
			$("#inputForm").validate({
				rules: {
					email:{
						required: true,
						email: true
					},
					remarks:{
						maxlength:500
					},
					jcaptchaCode:"required",
					totalAmount:"number",
					bdUserName:{required:true,
								remote:{ 
									url : "${ctx}/ajax/checkBaiduUsername",
									type : "post",
									dataType: 'json',
									data : {
										bdUserName : function() {
											return $("#bdUserName").val();
										}
									},
									dataFilter : function(data, type) {
										if (data == "true")
											return true;
										else
											return false;
									}
								}	
					},
					amount:{
						required:true,
						number:true,
						min:1000
					}
			    },
			    messages:{
			    	bdUserName:{
			    		remote:'<spring:message code="user.bdusername.notexist" />'
			    	},
			    	amount:{
			    		min:'<spring:message code="paytran.amountinrmb.min" />'
			    	}
			    },
			    errorPlacement: function(error, element) {
					if (element.attr("name") == "jcaptchaCode") {
						 error.insertAfter("#jcaptchaCodeImg");
					} 
					else if (element.parent('.input-group').length || element.prop('type') === 'checkbox' || element.prop('type') === 'radio') {
			            error.insertAfter(element.parent());
			        } else {
			            error.insertAfter(element);
			        }
			    }
			});
			
		});
		
		function submitFormHandler(){
			//var hasError = false;
			$(".detailTR").each(function(i){
				var dis = $(this).find(".label-danger").css("display");
				/* if(dis != 'none' && dis != undefined && dis != null)
					hasError = true; */
				var bdUserName = $(this).find(".bdUserName").val();
				var payCode = $(this).find(".payCode").val();
				var amount = $(this).find(".amount").val();
				var amountInRMB = $(this).find(".amountInRMB").val();
		        var radioName = $(this).find('input[type="radio"]').attr("name");
		        var isRechargeOnline = $(this).find('input[type="radio"][name="'+radioName+'"]:checked').val() == '1' ? 1 : 0;
				$(this).find(".paytranDetailStr").val(bdUserName+"#"+payCode+"#"+amount+"#"+amountInRMB+"#"+isRechargeOnline);
			});
			/* if(hasError) return; */
			var attachments = $("input[name^='attachment']").length;
			
			var flag = 0;
			// 这里先移除, 如果不移除下面的valid会清空掉文字
			$("#attachment-error").removeClass('label label-danger').hide();
			if(!$("#inputForm").valid()){
				flag = flag+1;
			}
			if(attachments==0){
				flag = flag+1;
				$("#attachment-error").addClass('label label-danger').show();
			}
			if(flag == 0){
				// 显示modal页面信息
				syncSubmitInfo();
				$("#submitModal").modal('show');
			}else{
				return false;
			}
			
		};
		
		var detailsLength = ${payTranHeader.payTranDetails.size()};
		
		function addDetailTR(name){
			detailsLength++;
			var selHtml = $(".td-payCodes select").html();
			var html="<tr class=\"detailTR\">";
			html+="<td><input class=\"form-control bdUserName\" type=\"text\" id=\"bdUserName"+i+"\" name=\"bdUserName"+i+"\" value=\"\"></td>";
			html+="<td><select class=\"form-control payCode\" id=\"payCode"+i+"\" name=\"payCode"+i+"\"  onchange='payCodeChange(this)'>"+selHtml+"</td>";
			html+="<td><input style=\"text-align: right;\" class=\"form-control amount\" type=\"text\" id=\"amount"+i+"\" name=\"amount"+i+"\" value=\"\"></td>";
			html+="<td><input style=\"text-align: right;\" class=\"form-control amountInRMB\" type=\"text\" id=\"amountInRMB"+i+"\" name=\"amountInRMB"+i+"\" value=\"\" readonly></td>";
      		html+='<td><span><input type="radio" class="isRechargeOnline group_radio_button" name="isRechargeOnline'+detailsLength+'" checked="checked" value="1">&nbsp;&nbsp;<spring:message code="public.yes" /><input type="radio" class="isRechargeOnline group_radio_button" name="isRechargeOnline'+detailsLength+'" value="0" style="margin-left: 13px;">&nbsp;&nbsp;<spring:message code="public.no" /></span></td>';
			html+="<td><a href=\"javascript:void(0)\" onclick=\"delDetailTR(this);\"><i class=\"fa fa-minus-circle\" style=\"margin-top: 10px;\"></i></a><input type=\"hidden\" class=\"paytranDetailStr\" name=\"paytranDetailStr\"></td></tr>";
			$(name).parent().parent().before(html);
			// 用于动态表单验证
			$("#bdUserName"+i).rules('add', 
					{required: true,
					remote:{
						url : "${ctx}/ajax/checkBaiduUsername",
						type : "post",
						dataType: 'json',
						data : {
							bdUserName : function() {
								return $("#bdUserName"+(i-1)).val();
							}
						},
						dataFilter : function(data, type) {
							if (data == "true")
								return true;
							else
								return false;
						}
					},
					'messages':{
						remote:'<spring:message code="user.bdusername.notexist" />'
					}
					});
			var tmp1000 = get1000RMB($(".currency").val());
			$("#amount"+i).rules('add', {required: true,number:true,min:tmp1000,messages:{min:'<spring:message code="paytran.amountinrmb.min" />'}});
			
			i++;
		};
		
		Array.prototype.unique = function(){
		 var res = [];
		 var json = {};
		 for(var i = 0; i < this.length; i++){
		  if(!json[this[i]]){
		   res.push(this[i]);
		   json[this[i]] = 1;
		  }
		 }
		 return res;
		}
		
		function submitForm(){
		  var nameArray = [];
	      $(".detailTR").each(function(){
			var bdUserName = $(this).find(".bdUserName").val();
			var payCode = $(this).find(".payCode").val();
			//if (payCode == '1' || payCode == 1){
			  nameArray.push(bdUserName);
			//}
	      });
	      nameArray = nameArray.unique();
	      if(nameArray.length <= 1){
    	  	$(".detailTR").each(function(){
	   			/* if($(this).find(".payCode").val() != 1)
	   			  return; */
	   			if($(this).find("td:nth-child(1) .label-danger").length > 0){
	   			  $(this).find("td:nth-child(1) .label-danger").eq(0).css("display",'none');
	   			}
	   	      });
    	  	submitFormHandler();
	        return;
	      }
          $.ajax({
            url: "${ctx}/ajax/checkBaiduUsertype",
            type: "post",
            datatype: "json",
            data: "names="+nameArray.join(","),
            success: function(result){
            	if(result == "false"){
            	  $(".detailTR").each(function(){
           			/* if($(this).find(".payCode").val() != 1)
           			  return; */
            		if($(this).find("td:nth-child(1) .label-danger").length > 0){
           			  $(this).find("td:nth-child(1) .label-danger").eq(0).html('<spring:message code="am.blockTrade.correctNum"/>').removeAttr("style");
           			}
           			else{
           			  $(this).find("td:nth-child(1)").append('<span id="nameerror" class="label label-danger"><spring:message code="am.blockTrade.correctNum"/></span>');
           			}
           	      });
            	}
            	else{
          		  $(".detailTR").each(function(){
           			/* if($(this).find(".payCode").val() != 1)
           			  return; */
           			if($(this).find("td:nth-child(1) .label-danger").length > 0){
           			  $(this).find("td:nth-child(1) .label-danger").eq(0).css("display",'none');
           			}
           			submitFormHandler();
           	      });
            	}
            }
          });
		}

    function payCodeChange(codeEle) {
      if ($(codeEle).val() == '1') {
        $(codeEle).parent().parent().find(".isRechargeOnline").parent().removeAttr("style");
        //checkBaiduUser();
      }
      else{
        $(codeEle).parent().parent().find(".isRechargeOnline").parent().css('display','none');
      }
    }
		
		function delDetailTR(name){
			$(name).parent().parent().remove();
			calTotalAmount();
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
			$("#messange").html(html);
			
			var attachments = $("input[name^='attachment']").length;
			if(attachments!=0){
				$("#attachment-error").removeClass('label label-danger').hide();
			}else{
				$("#attachment-error").addClass('label label-danger').show();
			}
		};
		
		function get1000RMB(key){
			if(key=='CNY'){
				return 1000;
			}
			else if(key=='HKD'){
				key='CNY-HKD';
			}else{
				key=key+'-CNY'
			}
			
			for(var i = 0 ; i < keyArr.length;i++){
				if(keyArr[i] == key){
					if(key=='CNY-HKD'){
						return Math.round(1000*valueArr[i]*100)/100;
					}
					else{
						return Math.round(1000/valueArr[i]*100)/100;
					}
				}
			}
		};
		
		function moneyChange(key,money){
			if(key=='CNY'){
				return money;
			}
			else if(key=='HKD'){
				key='CNY-HKD';
			}else{
				key=key+'-CNY'
			}
			
			for(var i = 0 ; i < keyArr.length;i++){
				if(keyArr[i] == key){
					if(key=='CNY-HKD'){
						return Math.round(money/valueArr[i]*100)/100;
					}
					else{
						return Math.round(money*valueArr[i]*100)/100;
					}
				}
			}
		};
		
		// 计算total amount
		function calTotalAmount(){
			var totalAmount = 0;
			var totalAmountInRMB = 0;
			$("tbody .detailTR").each(function(){
				var amount = $(this).find("td input.amount").val();
				var amountInRMB = $(this).find("td input.amountInRMB").val();
				totalAmount = parseFloat(totalAmount) + parseFloat(amount);
				totalAmountInRMB = parseFloat(totalAmountInRMB) + parseFloat(amountInRMB);
			});
			$("#totalAmount").text(Math.round(totalAmount*100)/100);
			$("#totalAmountInRMB").text(Math.round(totalAmountInRMB*100)/100);
		};
		
		// 修改验证条件
		function changeValid(){
			var tmp1000 = get1000RMB($(".currency").val());
			$(".amount").each(function(){
				$(this).rules('remove', "min");
				// 如果是加款,
				if($(this).parent().prev().find(".payCode").val()=="1"){
					$(this).rules('add', {min:tmp1000});
				}
			});
		};
		
		// 同步提交信息
		function syncSubmitInfo(){
			$("#infoli1").html($("#remarks").val());
			$("#infoli2").html($("#currency option:selected").text()+" "+ milliFormat($("#totalAmount").html()));
			$("#infoli3").html("<spring:message code='paytran.cny'/>"+" "+ milliFormat($("#totalAmountInRMB").html()));
			var detailTRHTML = "<table style='border:0px;margin-top: -20px;'><thead><tr><th style='font-size: 14px;font-weight: normal;'><spring:message code='paytran.info.li4'/>:</th><th></th><th></th><th></th><th style='font-size: 14px;font-weight: normal;'><spring:message code='paytran.autoOnline'/>:</th></tr></thead>";
			$(".detailTR").each(function(){
				detailTRHTML += "<tr>";
				detailTRHTML += "<td>";
				detailTRHTML += $(this).find("td:nth-child(1)").find("input:nth-child(1)").val();
				detailTRHTML += "&nbsp;&nbsp;&nbsp;&nbsp;</td>";
				detailTRHTML += "<td>";
				detailTRHTML += $(this).find("td:nth-child(2)").find(".payCode option:selected").text();
				detailTRHTML += "&nbsp;&nbsp;&nbsp;&nbsp;</td>";
				detailTRHTML += "<td>";
				detailTRHTML += "<spring:message code='paytran.cny'/>";
				detailTRHTML += "&nbsp;&nbsp;&nbsp;&nbsp;</td>";
				detailTRHTML += "<td style='text-align: right;'>";
				detailTRHTML += milliFormat($(this).find("td:nth-child(4)").find("input:nth-child(1)").val());
				detailTRHTML += "</td>";
		        if ($(this).find(".payCode").val() == 1) {
		          var radioName = $(this).find('input[type="radio"]').attr("name");
		          var autoOnline = $(this).find('input[type="radio"][name="'+radioName+'"]:checked').val() == '1' ? "<spring:message code='public.yes'/>" : "<spring:message code='public.no'/>";
		          detailTRHTML += "<td style='text-align: center;'>" + autoOnline + "</td>";
		        }
				detailTRHTML += "</tr>";
			});
			detailTRHTML+="</table>";
			$("#infoli4").html(detailTRHTML);
		};
		
		//添加千位符
		function milliFormat(s){
		    if(/[^0-9\.]/.test(s)) return s;  
		    s=s.replace(/^(\d*)$/,"$1.");  
		    s=(s+"00").replace(/(\d*\.\d\d)\d*/,"$1");  
		    s=s.replace(".",",");  
		    var re=/(\d)(\d{3},)/;  
		    while(re.test(s)){  
		        s=s.replace(re,"$1,$2");  
		    }  
		    s=s.replace(/,(\d\d)$/,".$1");  
		    return s.replace(/^\./,"0.")  
		};
		
	</script>

</body>
</html>
