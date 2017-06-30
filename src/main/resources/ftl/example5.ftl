<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title></title>
</head>

<body>

	<div style="padding: 10px;">
		
		<table style="border-color:#ddd; border-collapse:collapse; font-size: 14px; text-align: inherit; font-family: inherit; line-height: 1.66667;font-weight: 600;
		color: #333;pointer-events: auto;padding: 0; width: 100%;" border="1" >
			<tr style="font-size: 20px;">
				<td colspan=6 style="padding: 10px;">客户交易入账通知${isTest}</td>
			</tr>
			<tr>
				<td style="padding: 8px; background: #ccc;">交易号：</td>
				<td style="padding: 8px; color: #666; " colspan=5>${tradeNo}</td>
			</tr>
			<tr>
				<td style="padding: 8px; background: #ccc;">收据上传时间：</td>
				<td style="padding: 8px; color: #666;">${uploadDate}</td>
				<td></td>
				<td style="padding: 8px; background: #ccc;">确认日期:</td>
				<td style="padding: 8px; color: #666;" colspan=2>${finPayTranDate}</td>
			</tr>
			<tr>
				<td style="padding: 8px; background: #ccc;">币种：</td>
				<td style="padding: 8px; color: #666;">${currency}</td>
				<td></td>
				<td style="padding: 8px; background: #ccc;">收据总计（CNY）：</td>
				<td style="padding: 8px; color: #666; text-align: right;" colspan="2">${totalAmountInRMB}</td>
			</tr>

			<tr>
				<td style="padding: 8px; background: #ccc;">交易备注：</td>
				<td style="padding: 8px; color: #666;" colspan=5>${paymentRemark}</td>
			</tr>
			<tr style="word-break: keep-all;white-space:nowrap; background: #ccc;" >
				  <td style="padding: 5px;">百度用户名</td>
				  <td style="padding: 5px;">交易方式</td>
				  <td style="padding: 5px;">收据金额</td>
				  <td style="padding: 5px;">收据金额（CNY）</td>
				  <td style="padding: 5px;">加款金额（CNY）</td>
				  <td style="padding: 5px;color: red;">充值完成后自动开始推广消费？</td>
			</tr>
			
			<#list details as detail>
			<tr style="word-break: keep-all;white-space:nowrap; color: #666;">
				  <td style="padding: 8px;">${detail.bdUserName}</td>
				  <td style="padding: 8px;">${detail.payCode}</td>
				  <td style="padding: 8px; text-align: right;">${detail.amount}</td>
				  <td style="padding: 8px; text-align: right;">${detail.amountInRMB}</td>
				  <td style="padding: 8px; text-align: right;">${detail.additionAmount}</td>
				  <td style="padding: 8px;color: red;">${detail.is_recharge_online_zh}</td>
			</tr>
			</#list>
			<tr style="word-break: keep-all;white-space:nowrap; color: #666; ">
				<td style="padding: 8px; text-align: right;" >总计(${currency})：</td>
				<td style="padding: 8px; text-align: right;" >
					<span>${totalAmount}</span>
				</td>
				<td style="padding: 8px; text-align: right;font-weight:bold;">实际加款总计（CNY）</td>
				<td style="padding: 8px; text-align: right;">
				    <span>${totalRealAddAmount}</span>
				</td>
                <td style="padding: 8px; text-align: right;font-weight:bold;">实际加款备注</td>
                <td style="padding: 8px;">
                    <span>${process1Description}</span>
                </td>
			</tr>
		</table>
		<h4 style="color:red">如交易涉及银行手续费问题，款项处理会以此封系统邮件通知为准。谢谢！</h4>
	</div>
	<hr/>
	<br/>
	<div style="padding: 10px;">
		
		<table style="border-color:#ddd; border-collapse:collapse; font-size: 14px; text-align: inherit; font-family: inherit; line-height: 1.66667;font-weight: 600;
		color: #333;pointer-events: auto;padding: 0; width: 100%;" border="1" >
			<tr style="font-size: 20px;">
				<td colspan=6 style="padding: 10px;">Customer Notice</td>
			</tr>
			<tr>
				<td style="padding: 8px; background: #ccc;">Transaction no:</td>
				<td style="padding: 8px; color: #666; " colspan=5>${tradeNo}</td>
			</tr>
			<tr>
				<td style="padding: 8px; background: #ccc;">Receipt Submission Time:</td>
				<td style="padding: 8px; color: #666;">${uploadDate}</td>
				<td></td>
				<td style="padding: 8px; background: #ccc;">Receipt Confirmation Time:</td>
				<td style="padding: 8px; color: #666;" colspan=2>${finPayTranDate}</td>
			</tr>
			<tr>
				<td style="padding: 8px; background: #ccc;">Receipt Currency:</td>
				<td style="padding: 8px; color: #666;">${currency_en}</td>
				<td></td>
				<td style="padding: 8px; background: #ccc;">Receipt Total Amount (CNY):</td>
				<td style="padding: 8px; color: #666; text-align: right;" colspan=2>${totalAmountInRMB}</td>
			</tr>
			<tr>
				<td style="padding: 8px; background: #ccc;">Remarks:</td>
				<td style="padding: 8px; color: #666;" colspan=5>${paymentRemark}</td>
			</tr>
			<tr style="word-break: keep-all;white-space:nowrap; background: #ccc;" >
				  <td style="padding: 5px;">Baidu Username</td>
				  <td style="padding: 5px;">Payment Type</td>
				  <td style="padding: 5px;">Receipt Amount</td>
				  <td style="padding: 5px;">Receipt Amount（CNY）</td>
				  <td style="padding: 5px;">Actual Top Up Amount (CNY)</td>
				  <td style="padding: 5px;color: red;">Launch and run the campaign automatically </br>once the top-up process is completed?</td>
			</tr>
			
			<#list details as detail>
			<tr style="word-break: keep-all;white-space:nowrap; color: #666;">
				  <td style="padding: 8px;">${detail.bdUserName}</td>
				  <td style="padding: 8px;">${detail.payCode_en}</td>
				  <td style="padding: 8px; text-align: right;">${detail.amount}</td>
				  <td style="padding: 8px; text-align: right;">${detail.amountInRMB}</td>
				  <td style="padding: 8px; text-align: right;">${detail.additionAmount}</td>
				  <td style="padding: 8px;color: red;">${detail.is_recharge_online_en}</td>
			</tr>
			</#list>
			<tr style="word-break: keep-all;white-space:nowrap; color: #666; ">
				<td style="padding: 8px; text-align: right;">Total(${currency_en}):</td>
				<td style="padding: 8px; text-align: right;">
					<span>${totalAmount}</span>
				</td>
				<td style="padding: 8px; text-align: right;font-weight:bold;">Actual Top Up Amount (CNY)</td>
				<td style="padding: 8px; text-align: right;">
				    <span>${totalRealAddAmount}</span>
				</td>
                <td style="padding: 8px; text-align: right;font-weight:bold;">Actual Top Up Remarks</td>
                <td style="padding: 8px;">
                    <span>${process1Description}</span>
                </td>
			</tr>
		</table>
	<h4 style="color:red">For any amount disputation led by bank charge, the record shown on our system notification email shall prevail. Thank you for your attention.</h4>
	</div>

</body>
</html>