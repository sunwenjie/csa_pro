<%@page import="org.apache.shiro.SecurityUtils"%>
<%@page import="com.asgab.service.account.ShiroDbRealm.ShiroUser"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%
	ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
%>

<html>
<head>

<title><spring:message code="paytran.title" /></title>
</head>
<body>
	<style type="text/css">
	.color-red{color: red;}
	.process-check{color:green;}
	.popover{max-width:none;}
	.moreIcon{margin-top: 5px;}
	</style>

	<br/>
	<c:if test="${not empty successPaytran}">
                <jsp:include page="successMessage.jsp"></jsp:include>
    </c:if>
	
	<c:if test="${not empty errorMessage}">
		<div id="message" class="alert alert-warning"><button data-dismiss="alert" class="close">×</button>
			${errorMessage}
		</div>
	</c:if>
	
	<form id="searchForm" action="${ctx}/paytran" method="get">
		<div class="row">
			<div class="col-lg-12">
				<div class="panel panel-info">
					<div class="panel-heading">
						<spring:message code="paytran.title" />
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
									<label for="tranNum"><spring:message code="paytran.trannum"/></label> <input type="text"
										class="form-control" id="tranNum" name="tranNum"
										value="<c:out value="${pages.searchMap['tranNum']}"/>"
										placeholder="<spring:message code="paytran.trannum"/>">
								</div>
							</div>
							<div class="col-lg-4">
								<div class="form-group">
									<label for="email"><spring:message code="paytran.email"/></label> <input type="text"
										class="form-control" id="email" name="email"
										value="<c:out value="${pages.searchMap['email']}"/>"
										placeholder="<spring:message code="paytran.email"/>">
								</div>
							</div>
							<div class="col-lg-4">
								<div class="form-group">
									<label for="bdusername"><spring:message code="paytran.bdusername"/></label> <input type="text"
										class="form-control" id="bdusername" name="bdusername"
										value="<c:out value="${pages.searchMap['bdusername']}"/>"
										placeholder="<spring:message code="paytran.bdusername"/>">
								</div>
							</div>
							<div class="col-lg-4">
								<div class="form-group">
									<label for="status"><spring:message code="paytran.status"/></label>
									<tags:selectbox name="status" add0="true" map="${statuses}" value="${pages.searchMap['status']}"></tags:selectbox>
								</div>
							</div>
							<div class="col-lg-4">
								<div class="form-group">
									<label for="tranStartDate"><spring:message code="paytran.transtartdate"/></label> 
									<div class="input-group date form_date" data-date-format="dd MM yyyy" id="datetimepicker" data-link-format="yyyy-mm-dd">
	                            		<input class="form-control" type="text" value="${pages.searchMap['tranStartDate']}" id="tranStartDate" name="tranStartDate"  placeholder="<spring:message code="paytran.transtartdate"/>">
										<span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span>
	                            	</div>
								</div>
							</div>
							<div class="col-lg-4">
								<div class="form-group">
									<label for="tranEndDate"><spring:message code="paytran.tranenddate"/></label> 
									<div class="input-group date form_date" data-date-format="dd MM yyyy" id="datetimepicker" data-link-format="yyyy-mm-dd">
	                            		<input class="form-control" type="text" value="${pages.searchMap['tranEndDate']}" id="tranEndDate" name="tranEndDate"  placeholder="<spring:message code="paytran.tranenddate"/>">
										<span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span>
	                            	</div>
								</div>
							</div>
							
						</div>
					</div>
					<div class="panel-footer panel-footer-search">
						<div class="btn-group" role="group" aria-label="...">
							<button type="submit" class="btn  btn-info"><i class="fa fa-search"></i> <spring:message code="public.search" /></button>
							<button id="resetButtom" type="button" class="btn  btn-warning"><i class="fa fa-repeat"></i> <spring:message code="public.reset" /></button>
						</div>
						<a class="btn  btn-primary pull-right" href="${ctx}/paytran/create"><i class="fa fa-edit"></i> <spring:message code="public.create" /></a>
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
						<table class="table table-striped dataTable table-hover" style="margin-bottom:0px;">
							<thead>
								<tr>
									<th <tags:sort column="trannum" page="${pages}"/>><spring:message code="paytran.trannum" /></th>
									<th><spring:message code="paytran.bdusername" /></th>
									<th><spring:message code="paytran.bdport" /></th>
									<th><spring:message code="paytran.bdtype" /></th>
									<th <tags:sort column="trandate" page="${pages}"/>><spring:message code="paytran.trandate" /></th>
									<th <tags:sort column="email" page="${pages}"/>><spring:message code="paytran.email" /></th>
									<th <tags:sort column="currency" page="${pages}"/>><spring:message code="paytran.currency" /></th>
									<th <tags:sort column="totalamount" page="${pages}"/>><spring:message code="paytran.totalamount" /></th>
									<th><spring:message code="paytran.status" /></th>
									<th><spring:message code="paytran.remarks" /></th>
									<th><spring:message code="public.oper" /></th>
								</tr>
							</thead>
							
							<tbody>
								<c:forEach items="${pages.content}" var="paytran" varStatus="index">
									<tr class="${index.count%2==0?'odd':'even'}">
										<td>
											${paytran.tranNum}
											<a onmouseover="showProcess(this,${paytran.tranNum});" onmouseout="hideProcess(this);" data-html="true"  data-original-title="" data-content=""><i class="fa fa-list-alt"></i></a>
										</td>
										<td>${paytran.bdUserNames}</td>
										<td>${paytran.bdPort}</td>
										<td>${paytran.bdUserType}</td>
										<td><fmt:formatDate value="${paytran.tranDate}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td>
										<td>${paytran.email}</td>
										<td>${paytran.decodedCurrency}</td>
										<td align="right"><fmt:formatNumber value="${paytran.totalAmount}" pattern="#,##0.00"/> </td>
										
										<td><span>${paytran.decodedStatus}
											<c:if test="${ paytran.statusStr eq '2'||paytran.statusStr eq '7'}"> 
										 	<a href="#" onmouseover="showDescription(this,'${paytran.description}')" onmouseleave="hideDescription(this);"><i class="fa fa-w fa-comment"></i></a>
										 	</c:if>
											</span>
										</td>
										
										<td>${paytran.remarks}</td>
										<td>
											<a class="color-black" href="javascript:window.location.href='${ctx}/paytran/update/${paytran.tranNum}'"><i class="fa  fa-search"></i></a>
											<c:if test="${paytran.statusStr == '1'}">
												<shiro:hasAnyRoles name="admin,finance">
													<a href="javascript:void(0);" onclick="jumpPass('${paytran.financeCheckUrl}');">
													<i class="fa fa-check-circle"></i>
													</a>
													<a href="#" data-toggle="modal" onclick="showReject(${paytran.tranNum},${paytran.status});">
													<i class="fa fa-times-circle" ></i>
													</a>
												</shiro:hasAnyRoles>
											</c:if>
											
											<c:if test="${paytran.statusStr == '3'}">
												<c:choose>
												 <c:when test="${paytran.amSupport}">
													 <shiro:hasAnyRoles name="admin,am">
														 <a href="javascript:void(0);" onclick="jumpPass('${paytran.amSupportUrl}');">
															 <i class="fa fa-check-circle"></i>
														 </a>
													 </shiro:hasAnyRoles>
												 </c:when>
											     <c:otherwise>
													 <shiro:hasAnyRoles name="admin,finance">
														 <a href="javascript:void(0);" onclick="jumpPass('${paytran.financeConfirmUrl}');">
															 <i class="fa fa-check-circle"></i>
														 </a>
														 <a href="#" data-toggle="modal" onclick="showReject(${paytran.tranNum},${paytran.status});">
															 <i class="fa fa-times-circle" ></i>
														 </a>
													 </shiro:hasAnyRoles>
												 </c:otherwise>
												</c:choose>
											</c:if>

											<c:if test="${paytran.statusStr == '8'}">
												<shiro:hasAnyRoles name="admin,finance">
													<a href="javascript:void(0);" onclick="jumpPass('${paytran.financeConfirmUrl}');">
														<i class="fa fa-check-circle"></i>
													</a>
													<a href="#" data-toggle="modal" onclick="showReject(${paytran.tranNum},${paytran.status});">
														<i class="fa fa-times-circle" ></i>
													</a>
												</shiro:hasAnyRoles>
											</c:if>


											<c:if test="${paytran.statusStr == '4'}">
												<shiro:hasAnyRoles name="admin,ops">
													<a href="javascript:void(0);" onclick="pass(${paytran.tranNum},${paytran.statusStr});">
													<i class="fa fa-check-circle"></i>
													</a>
												</shiro:hasAnyRoles>
											</c:if>
											
										</td>
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
	
	<div id="shield" style="position: fixed; left: 0px; top: 0px; display: none; z-index: 9998; opacity: 0.8; background: #7D7159; width: 100%; height: 100%;">
		<img src="${ctx}/static/images/loading_s.gif" style="position: absolute; top: 300px; left: 48%;" /></div>
	
	<jsp:include page="reject.jsp"></jsp:include>
	<script>
		$(document).ready(function() {
			$('[data-toggle="tooltip"]').tooltip();
			
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
			
			$("#searchForm").validate({
				errorPlacement:function(error, element) {
					error.insertAfter(element.prev());
				},
				rules: {
					tranNum:"number"
			    }
			});

			$("#resetButtom").on("click", function() {
				$("#tranNum").val("");
				$("#email").val("");
				$("#bdusername").val("");
				$("#tranStartDate").val("");
				$("#tranEndDate").val("");
				$("#status").val("0");
			});
			
			$(".form_date").datetimepicker({
				format: 'yyyy-mm-dd',
				weekStart: 1,
		        todayBtn:  1,
				autoclose: 1,
				todayHighlight: 1,
				startView: 2,
				minView: 2,
				forceParse: 0
			});
			
		});
		
		function link(url){
			window.location.href=url;
		};
		
		function pass(tranNum,status){
			if(confirm('<spring:message code="paytran.finance.check"/>?')){
				$("#shield").show();
				link("${ctx}/paytran/pass/"+tranNum+"/"+status);
			}
		};
		
		function jumpPass(url){
			if(url!=''){
				var windowOpen = window.open(url);
				if (windowOpen == null || typeof(windowOpen)=='undefined'){
					alert("please check your internet explorer")
				}
			}else{
				alert("<spring:message code='public.system.error' />!");
			}
		};
		
		function showReject(tranNum,status){
			$("#rejectModal").modal();
			$(".modal-body").find("#tranNum").val(tranNum);
			$(".modal-body").find("#status").val(status);
		};
		
		function showProcess(name,paytranNum){
			if($(name).attr('data-original-title')==''){
				$.post('${ctx}/paytran/show/process/'+paytranNum,{},function(data){
					var html = "<table class='table table-hover'><tr><td><spring:message code='payment.process.date'/></td><td><spring:message code='payment.process.oper'/></td><td><spring:message code='payment.process.by'/></td></tr>";
					if(data.tr1td0!=undefined){
						html += "<tr><td class='processTableTd'>"+data.tr1td0+"</td><td>"+data.tr1td1+"</td><td>"+data.tr1td2+"<i class='fa fa-check process-check'></i></td></tr>"
					}
					//status=2
					if(data.tr2td0!=undefined){
						var info = "";if(data.tr2td3==""){info="<i class='fa fa-check process-check'></i>";}
						html += "<tr><td>"+data.tr2td0+"</td><td>"+data.tr2td1+"</td><td>"+data.tr2td2+info+"</td></tr>"				
					}

					if(data.tr3td0!=undefined){
						var info = "";if(data.tr3td3==""){info="<i class='fa fa-check process-check'></i>";}
						html += "<tr class='"+data.tr3td3+"'><td>"+data.tr3td0+"</td><td>"+data.tr3td1+"</td><td>"+data.tr3td2+info+"</td></tr>"				
					}

                    //status=8
                    if(data.tr8td0!=undefined){
                        var info = "";if(data.tr8td3==""){info="<i class='fa fa-check process-check'></i>";}
                        html += "<tr class='"+data.tr8td3+"'><td>"+data.tr8td0+"</td><td>"+data.tr8td1+"</td><td>"+data.tr8td2+info+"</td></tr>"
                    }

					if(data.tr4td0!=undefined){
						var info = "";if(data.tr4td3==""){info="<i class='fa fa-check process-check'></i>";}
						html += "<tr class='"+data.tr4td3+"'><td>"+data.tr4td0+"</td><td>"+data.tr4td1+"</td><td>"+data.tr4td2+info+"</td></tr>"				
					}
					if(data.tr5td0!=undefined){
						var info = "";if(data.tr5td3==""){info="<i class='fa fa-check process-check'></i>";}
						html += "<tr class='"+data.tr5td3+"'><td>"+data.tr5td0+"</td><td>"+data.tr5td1+"</td><td>"+data.tr5td2+info+"</td></tr>"				
					}
					//status=7
					if(data.tr7td0!=undefined){
						var info = "";if(data.tr7td3==""){info="<i class='fa fa-check process-check'></i>";}
						html += "<tr><td>"+data.tr7td0+"</td><td>"+data.tr7td1+"</td><td>"+data.tr7td2+info+"</td></tr>"				
					}
					html += "</table>";
					$(name).attr('data-original-title',data.title);
					$(name).attr('data-content',html);
					$(name).popover('show');
				},"json");
			}else{
				$(name).popover('show');
			}
		};
		
		function hideProcess(name){
			$(name).popover('hide');
		};
		
		function showBDUsers(name,bdUsernames){
			var html = '';
			var names = bdUsernames.split(',');
			for(var i in names){
				html+=(names[i]+"<br/>");
			}
			
			$(name).attr('data-original-title','');
			$(name).attr('data-content',html);
			$(name).popover('show');
		};
		
		function showBDPorts(name,bdPorts){
			var html = '';
			var names = bdPorts.split(',');
			for(var i in names){
				html+=(names[i]+"<br/>");
			}
			
			$(name).attr('data-original-title','');
			$(name).attr('data-content',html);
			$(name).popover('show');
		};
		
		function showDescription(name,description){
			$(name).attr('data-original-title','<spring:message code="paytran.description"/>');
			$(name).attr('data-content',description);
			$(name).popover('show');
		}

		function hideDescription(name){
			$(name).popover('hide');
		};
		
	</script>
</body>
</html>
