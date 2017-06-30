package com.asgab.entity;

import com.asgab.util.CommonUtil;

public class BlockTrade {

  private Long blockTradeId;
  private Long processId;
  private Long payTranNum;
  private double amount30;
  private double amount15;
  
  public Long getBlockTradeId() {
	  return blockTradeId;
  }
  public void setBlockTradeId(Long blockTradeId) {
	  this.blockTradeId = blockTradeId;
  }
  public Long getProcessId() {
	  return processId;
  }
  public void setProcessId(Long processId) {
	  this.processId = processId;
  }
  public Long getPayTranNum() {
	  return payTranNum;
  }
  public void setPayTranNum(Long payTranNum) {
	  this.payTranNum = payTranNum;
  }
  public double getAmount30() {
	  return amount30;
  }
  public void setAmount30(double amount30) {
	  this.amount30 = amount30;
  }
  public double getAmount15() {
	  return amount15;
  }
  public void setAmount15(double amount15) {
	  this.amount15 = amount15;
  }
  public String getAmount30Str(){
    return CommonUtil.digSeg(this.getAmount30());
  }
  public String getAmount15Str(){
    return CommonUtil.digSeg(this.getAmount15());
  }

}
