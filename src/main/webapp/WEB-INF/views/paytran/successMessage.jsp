<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="alert alert-success alert-dismissable"><button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                <spring:message code="message.success.create.success" />：
                <br/>
                <spring:message code="message.success.create.paytran" />：<span style="font-size: 28px;">${successPaytran.tranNum}</span>
                <br/>
                <spring:message code="message.success.create.date" />：${successPaytran.tranDateDate}
                <br/>
                <spring:message code="message.success.create.time" />：${successPaytran.tranDateTime}
                <br/>
                <spring:message code="message.success.tran.amount" />：${successPaytran.currency}&nbsp;${successPaytran.fmtTotalAmount}
                <br/>
                <spring:message code="message.success.tran.amount.cny" />：<spring:message code="successPaytran.cny" />&nbsp;${successPaytran.fmtTotalAmountInRMB}
                <br/>
                <spring:message code="message.success.tran.content" />:
                <br/>
                <div class="table-responsive" style="margin-top: -30px;">
                <table class="table ">
                <thead><tr><th></th><th></th><th></th><th></th><th style='font-size: 14px;font-weight: normal;'><spring:message code='paytran.autoOnline'/>:</th></tr></thead>
                <c:forEach items="${successPaytran.payTranDetails}" var="detail">
                    <tr style="background-color: none;">
                        <td>${detail.bdUserName}</td>
                        <td>${detail.bdUserName}</td>
                        <td>CNY</td>
                        <td>${detail.fmtAmountInRMB}</td>
                        <td>
                        	<c:if test="${detail.isRechargeOnline}"><spring:message code="public.yes" /></c:if>
                        	<c:if test="${!detail.isRechargeOnline}"><spring:message code="public.no" /></c:if>
                        </td>
                    </tr>
                </c:forEach>
                </table>
                </div>
                <spring:message code="message.success.tran.notice" />
</div>