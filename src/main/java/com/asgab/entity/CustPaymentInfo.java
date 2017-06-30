package com.asgab.entity;

import java.util.Date;

/***
 * 报表 - 客服付款信息
 * 
 * @author Siuvan
 *
 */
public class CustPaymentInfo {

  private Long tranNum;
  private Date paymentDate;// 付款期间
  private String custName;// 代理名称
  private String advertiser;// 广告主名称
  private String payerName;// 付款方名称
  private String bdUserName;// 百度用户名
  private String custPort;// 加款百度二级端口名称
  private Date topupDate;// 百度二级端口加款時間
  private Date opsDate;// 百度二级端口加款時間
  private double amount;// 付款原币金额
  private double amountInRmb;// 折合RMB
  private String payCode;
  private double topupAmount;// 加款金额
  private double rebateAmount;// 返点金额（续费返）
  private double acctCreateFeeAmount;// 开户费金额
  private double annualFeeAmount;// 年费金额
  private double guaranteeFeeAmount;// 保证金金额
  private double mgtFeeAmount;// 管理费金额
  private String custRemark;// 客户备注
  private double mgtFee;// 管理费率
  private String currency;
  private double realAddAmount;
  private String description;

  // for app
  private String currencyNameZH;
  private String currencyNameEN;

  public Date getPaymentDate() {
    return paymentDate;
  }

  public void setPaymentDate(Date paymentDate) {
    this.paymentDate = paymentDate;
  }

  public String getCustName() {
    return custName;
  }

  public void setCustName(String custName) {
    this.custName = custName;
  }

  public String getAdvertiser() {
    return advertiser;
  }

  public void setAdvertiser(String advertiser) {
    this.advertiser = advertiser;
  }

  public String getPayerName() {
    return payerName;
  }

  public void setPayerName(String payerName) {
    this.payerName = payerName;
  }

  public String getCustPort() {
    return custPort;
  }

  public void setCustPort(String custPort) {
    this.custPort = custPort;
  }

  public Date getTopupDate() {
    return topupDate;
  }

  public void setTopupDate(Date topupDate) {
    this.topupDate = topupDate;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public double getTopupAmount() {
    return topupAmount;
  }

  public void setTopupAmount(double topupAmount) {
    this.topupAmount = topupAmount;
  }

  public double getAcctCreateFeeAmount() {
    return acctCreateFeeAmount;
  }

  public void setAcctCreateFeeAmount(double acctCreateFeeAmount) {
    this.acctCreateFeeAmount = acctCreateFeeAmount;
  }

  public double getAnnualFeeAmount() {
    return annualFeeAmount;
  }

  public void setAnnualFeeAmount(double annualFeeAmount) {
    this.annualFeeAmount = annualFeeAmount;
  }

  public double getGuaranteeFeeAmount() {
    return guaranteeFeeAmount;
  }

  public void setGuaranteeFeeAmount(double guaranteeFeeAmount) {
    this.guaranteeFeeAmount = guaranteeFeeAmount;
  }

  public double getMgtFeeAmount() {
    return mgtFeeAmount;
  }

  public void setMgtFeeAmount(double mgtFeeAmount) {
    this.mgtFeeAmount = mgtFeeAmount;
  }

  public String getCustRemark() {
    return custRemark;
  }

  public void setCustRemark(String custRemark) {
    this.custRemark = custRemark;
  }

  public String getCurrencyNameZH() {
    return currencyNameZH;
  }

  public void setCurrencyNameZH(String currencyNameZH) {
    this.currencyNameZH = currencyNameZH;
  }

  public String getCurrencyNameEN() {
    return currencyNameEN;
  }

  public void setCurrencyNameEN(String currencyNameEN) {
    this.currencyNameEN = currencyNameEN;
  }

  public double getAmountInRmb() {
    return amountInRmb;
  }

  public void setAmountInRmb(double amountInRmb) {
    this.amountInRmb = amountInRmb;
  }

  public double getRebateAmount() {
    return rebateAmount;
  }

  public void setRebateAmount(double rebateAmount) {
    this.rebateAmount = rebateAmount;
  }

  public Long getTranNum() {
    return tranNum;
  }

  public void setTranNum(Long tranNum) {
    this.tranNum = tranNum;
  }

  public String getPayCode() {
    return payCode;
  }

  public void setPayCode(String payCode) {
    this.payCode = payCode;
  }

  public double getMgtFee() {
    return mgtFee;
  }

  public void setMgtFee(double mgtFee) {
    this.mgtFee = mgtFee;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public Date getOpsDate() {
    return opsDate;
  }

  public void setOpsDate(Date opsDate) {
    this.opsDate = opsDate;
  }

  public String getBdUserName() {
    return bdUserName;
  }

  public void setBdUserName(String bdUserName) {
    this.bdUserName = bdUserName;
  }

  public double getRealAddAmount() {
	return realAddAmount;
  }

  public void setRealAddAmount(double realAddAmount) {
	this.realAddAmount = realAddAmount;
  }

  public String getDescription() {
	return description;
  }

  public void setDescription(String description) {
	this.description = description;
  }

}
