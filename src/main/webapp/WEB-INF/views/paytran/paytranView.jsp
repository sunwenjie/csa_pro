<%@page import="com.asgab.entity.Process"%>
<%@page import="com.asgab.entity.PayTranHeader"%>
<%@page import="java.util.List"%>
<%@page import="com.asgab.util.CommonUtil"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title><spring:message code="paytran.title"/></title>
</head>

<body>
	<div class="row">
	    <div class="col-lg-12">
	        <h3 class="page-header"><spring:message code="paytran.title"/>
	        (<spring:message code="paytran.trannum"/> : ${payTranHeader.tranNum})</h3>
	    </div>
	    <!-- /.col-lg-12 -->
	</div>
	<!-- /.row -->
	<div class="row">
	  <form role="form"  id="inputForm" action="" method="post">
	    <div class="col-lg-12">
	        <div class="panel panel-default">
	            <div class="panel-body">
	                <div class="row">
	                	<div class="col-lg-6">
	                	
                			<div class="form-group">
                                <label for="currency"><spring:message code="paytran.currency" />:</label>
                      			<span  class="view-span">${payTranHeader.decodedCurrency}</span>
                            </div>
                            
                            <div class="form-group">
                                <label for="totalAmount"><spring:message code="paytran.totalamount" />:</label>
                    			<span><fmt:formatNumber value="${payTranHeader.totalAmount}" pattern="#,##0.00"/></span>
	                        </div>
	                        
	                        <div class="form-group">
                                <label for="status"><spring:message code="paytran.status" />:</label>
                      			<span>${payTranHeader.decodedStatus}</span>
                            </div>
                            
                            <div class="form-group">
							    <label for="inputFile"><spring:message code="paytran.attachment"/></label>
							    <ul id="uploadedFiles">
							    	<c:forEach items="${payTranHeader.payTranAttachements }" var="attach" varStatus="status">
							    			<li>${attach.showName}
							    			<a href="${ctx}/paytran/downloadAttach/${attach.attachmentId}"><i class="fa fa-download"></i></a><br/>
							    			</li>
							    	</c:forEach>
							    </ul>
						  	</div>
						  	
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
	                	
	                	<div class="col-lg-6">
	                	
	                		<div class="form-group">
                                <label for="email"><spring:message code="paytran.email" />:</label>
                      			<span>${payTranHeader.email}</span>
                            </div>
                            
                            <input type="hidden" name="tranNum" value="${payTranHeader.tranNum}"/>
                            <div class="form-group">
                                <label for="tranDate"><spring:message code="paytran.trandate" />:</label>
                            	<span class="view-span"><fmt:formatDate value="${payTranHeader.tranDate}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></span>
                            </div>
                            
                            <div class="form-group">
                                <label for="remarks"><spring:message code="paytran.remarks" />:</label>
                      			<span>${payTranHeader.remarks}</span>
                            </div>
                            
                            <c:if test="${not empty  payTranHeader.description}">
                            <div class="form-group">
                                <label for="description"><spring:message code="paytran.description" />:</label>
                      			<span>${payTranHeader.description}</span>
                            </div>
                            </c:if>
                            
	                	</div>
	                	
                        <div class="col-lg-12">
			             	<h4 class="page-header" style="margin-top: 10px;"><spring:message code="paytran.detail.title" /><span id="detailIndex"></span></h4>
			             </div>
			             
                        <!-- pay details -->
                        <div class="col-lg-12">
                        <div class="panel panel-default">
						<div class="panel-body">
						<div class="table-responsive">
							<table class="table table-striped  table-hover">
	                     		<thead>
								<tr>
									<th><spring:message code="paytran.bdusername" /></th>
									<th><spring:message code="paytran.paycode" /></th>
									<th><spring:message code="paytran.amount" /></th>
									<th><spring:message code="paytran.amountinrmb" /></th>
									<th><spring:message code="paytran.autoOnline" /></th>
								</tr>
								</thead>
								<tbody>
									<c:forEach items="${payTranHeader.payTranDetails}" var="payTranDetail">
										<tr class="detailTR">
								   			<td>${payTranDetail.bdUserName}</td>
								   			<td>${payTranDetail.decodePayCode}</td>
								   			<td align="right"><fmt:formatNumber value="${payTranDetail.amount}" pattern="#,##0.00"/></td>
								   			<td align="right">
								   			<fmt:formatNumber value="${payTranDetail.amountInRMB}" pattern="#,##0.00"/>&nbsp;&nbsp;&nbsp;&nbsp;
								   			</td>
								   			<td>
								   				<c:if test="${payTranDetail.isRechargeOnline}">
								   					<spring:message code='public.yes'/>
								   				</c:if>
								   				<c:if test="${!payTranDetail.isRechargeOnline}">
								   					<spring:message code='public.no'/>
								   				</c:if>
								   			</td>
						   				</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
						</div>
						</div>
						</div>
	                   <!-- paydetails  end -->
	                   
	                   <!-- finance check -->
	                   <c:if test="${not empty processCheck}">
	                   <div class="col-lg-12">
			             	<h4 class="page-header" style="margin-top: 10px;"><spring:message code="finance.check.opinion" /></h4>
			            </div>	
			            
		             	<div class="col-lg-6">
		             		<div class="form-group">
		             			<label><spring:message code="report.CustPaymentInfo.realAmount"/>:</label>
		             			<fmt:formatNumber value="${processCheck.realAddAmount}" pattern="#,##0.00"/>
		             		</div>
		             		<div class="form-group">
		             			<label><spring:message code="finance.check.comment"/>:</label>
		             			${processCheck.description}
		             		</div>
		             		<div class="form-group">
		             			<label><spring:message code="finance.check.operator"/>:</label>
		             			${processCheck.operateByName}
		             		</div>
		             		<div class="form-group">
		             			<label><spring:message code="finance.check.date"/>:</label>
		             			<fmt:formatDate value="${processCheck.operateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
		             		</div>
		             	</div>
		             	
		             	<div class="col-lg-12">
			             	<div class="form-group" id="dumpImageDiv">
			             		<label><spring:message code="finance.check.attach"/>:</label>
			             		
			             		<ul id="uploadedFiles">
			             		<c:if test="${not empty processCheckAttach}">
						    	<c:forEach items="${processCheckAttach}" var="attach" varStatus="status">
						    			<li>${attach.showName}
						    			<a href="${ctx}/paytran/downloadAttach/${attach.attachmentId}"><i class="fa fa-download"></i></a><br/>
						    			</li>
						    	</c:forEach>
						    	</c:if>
						    	</ul>
                            </div>
		             	</div>
		             	
		             	<div class="col-lg-12">
		             		<div class="form-group" id="dumpImageDiv">
		             		<c:if test="${not empty processCheckAttach}">
                      				<c:forEach items="${processCheckAttach}" var="attach">
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
		             	</c:if>
	                   <!-- finance check  end -->
	                   
	                   <!-- finance confirm -->
	                   <c:if test="${not empty processConfirm}">
	                   <div class="col-lg-12">
			             	<h4 class="page-header" style="margin-top: 10px;"><spring:message code="finance.confirm.opinion" /></h4>
			            </div>	
			            
		             	<div class="col-lg-6">
		             		<div class="form-group">
		             			<label><spring:message code="finance.confirm.comment"/>:</label>
		             			${processConfirm.description}
		             		</div>
		             		<div class="form-group">
		             			<label><spring:message code="finance.confirm.operator"/>:</label>
		             			${processConfirm.operateByName}
		             		</div>
		             		<div class="form-group">
		             			<label><spring:message code="finance.confirm.date"/>:</label>
		             			<fmt:formatDate value="${processConfirm.operateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
		             		</div>
		             	</div>
		             	
		             	<div class="col-lg-12">
			             	<div class="form-group" id="dumpImageDiv">
			             		<label><spring:message code="finance.confirm.attach"/>:</label>
			             		
			             		<ul id="uploadedFiles">
			             		<c:if test="${not empty processConfirmAttach}">
						    	<c:forEach items="${processConfirmAttach}" var="attach" varStatus="status">
						    			<li>${attach.showName}
						    			<a href="${ctx}/paytran/downloadAttach/${attach.attachmentId}"><i class="fa fa-download"></i></a><br/>
						    			</li>
						    	</c:forEach>
						    	</c:if>
						    	</ul>
                            </div>
		             	</div>
		             	
		             	<div class="col-lg-12">
		             		<div class="form-group" id="dumpImageDiv">
		             		<c:if test="${not empty processConfirmAttach}">
                      				<c:forEach items="${processConfirmAttach}" var="attach">
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
		             	</c:if>
	                   <!-- finance confirm  end -->
                   </div>
               </div>
           </div>
       </div>
     </form>
   </div>
   
	<script>
	</script>
</body>
</html>
