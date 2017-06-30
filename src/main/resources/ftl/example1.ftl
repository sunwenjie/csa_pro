<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title></title>
</head>

<body>

	<div style="padding: 10px;">
		
		<table style="border-color:#ddd; border-collapse:collapse; font-size: 14px; text-align: inherit; font-family: inherit; line-height: 1.66667;font-weight: 600;
		color: #333;pointer-events: auto;padding: 0;" border="1" >
			<tr style="font-size: 20px;">
				<td colspan=17 style="padding: 10px;">客户入账通知-----财务查账用${isTest}</td>
			</tr>
			<tr>
				<td style="padding: 8px;">交易号：</td>
				<td style="padding: 8px;" colspan=16>${tradeNo}</td>
			</tr>
			<tr>
				<td style="padding: 8px;">收据上传时间：</td>
				<td style="padding: 8px;" colspan=16>${uploadDate}</td>
			</tr>
			<tr>
				<td style="padding: 8px;">总计(人民币)：</td>
				<td style="padding: 8px;" colspan=16>${totalAmountInRMB}</td>
			</tr>
			<tr>
				<td style="padding: 8px;">交易备注：</td>
				<td style="padding: 8px;" colspan=16>${paymentRemark}</td>
			</tr>
			<tr style="padding: 5px; background: #22FF08; text-align: center; ">
				  <td style="padding: 25px;" colspan=3>用户信息</td>
				  <td style="padding: 25px;" colspan=4>入账金额</td>
				  <td style="padding: 25px;" colspan=3>优惠</td>
				  <td style="padding: 25px; background: #17B9F4;" colspan=2>加款金额（CNY）</td>
				  <td style="padding: 25px;" colspan=5>申请</td>
			</tr>
			<tr style="word-break: keep-all;white-space:nowrap; background: yellow;" >
				  <td style="padding: 5px;">用户名</td>
				  <td style="padding: 5px;">广告主</td>
                  <td style="padding: 5px;color: red;">客户类型</td>
				  <td style="padding: 5px;">币种</td>
				  <td style="padding: 5px;">小写</td>
				  <td style="padding: 5px;">大写</td>
				  <td style="padding: 5px; color: red;">入账项目</td>
				  <td style="padding: 5px;">返点（%）</td>
				  <td style="padding: 5px;">返点金额</td>
				  <td style="padding: 5px;">赠送金额</td>
				  <td style="padding: 5px; background: #17B9F4">小写</td>
				  <td style="padding: 5px; background: #17B9F4">大写</td>
				  <td style="padding: 5px;">端口</td>
				  <td style="padding: 5px;">销售员</td>
				  <td style="padding: 5px;">客服</td>
				  <td style="padding: 5px;">代理商</td>
                  <td style="padding: 5px;color: red;">充值完成后自动开始推广消费?</td>
            </tr>
			</tr>
			
			<#list custUserList as user>
			<tr style="word-break: keep-all;white-space:nowrap; color: #666; ">
				  <td style="padding: 8px;">${user.custUsername}</td>
				  <td style="padding: 8px;">${user.custName}</td>
				  <td style="padding: 8px;">${user.custType}</td>
				  <td style="padding: 8px;">${currency}</td>
				  <td style="padding: 8px; text-align: right;">${user.rzjeLowercase}</td>
				  <td style="padding: 8px;">${user.rzjeCapital}</td>
				  <td style="padding: 8px;">${user.item}</td>
				  <td style="padding: 8px; text-align: right;">${user.rebate}</td>
				  <td style="padding: 8px; text-align: right;">${user.rebateAmount}</td>
				  <td style="padding: 8px; text-align: right;">${user.giftAmount}</td>
				  <td style="padding: 8px; text-align: right;">${user.jkjeLowercase}</td>
				  <td style="padding: 8px;">${user.jkjeCapital}</td>
				  <td style="padding: 8px;">${user.custPort}</td>
				  <td style="padding: 8px;">${user.sales_contact}</td>
				  <td style="padding: 8px;">${user.custService}</td>
				  <td style="padding: 8px;">${user.agent}</td>
                  <td style="padding: 8px;">${user.is_recharge_online}</td>
			</tr>
			</#list>
			<tr style="word-break: keep-all;white-space:nowrap; color: #666; ">
				<td style="padding: 8px; text-align: right;" colspan=3>总计(${currency})：</td>
				<td style="padding: 8px; " colspan=4>
					<span>${totalAmount}</span>
				</td>
				<td style="padding: 8px; text-align: right;" colspan=3>申请加款总计（CNY）：</td>
				<td style="padding: 8px; " colspan=7>
					<span>${totalAmountTopup}</span>
				</td>
			</tr>
			
		</table>
		
		<div style="width: 600px; padding: 20px 20px 20px 5px; font-size: 14px; text-align: inherit; font-family: inherit; line-height: 1.66667;font-weight: 600;
		color: #333;pointer-events: auto;">
			<a href="${confirmArrivalURL}">财务查账确认</a>&nbsp;&nbsp;
			<a href="${confirmArrivalURL2}">财务查账确认(涉及银行手续费)</a>&nbsp;&nbsp;
			<a href="${rejectArrivalURL}">财务查账拒绝</a>
		</div>
	
	</div>

</body>
</html>