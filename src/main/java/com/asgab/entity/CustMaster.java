package com.asgab.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class CustMaster {

  private Long id;

  private String custPort;
  private String custUsername;
  private String custName;
  private String webName;
  private String advertiser;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date acctCreateDate;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date annualSvcFeeDate;
  private String annualSvcFee;
  private String rewardsPercent;
  private String mgtFeePercent;
  private String consumption;

  private String remark1;
  private String remark2;

  private String am_contact;
  private String fin_contact;
  private String ops_contact;
  private String sales_contact;

  private String am_email;
  private String fin_email;
  private String ops_email;
  private String sales_email;

  private int deleted;
  private String createBy;
  private Date createDate;
  private String updateBy;
  private Date updateDate;

  // for app excel上传.如果是Y则是删除. 其他不删除
  private String isDeleted;

  public CustMaster() {}

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCustPort() {
    return custPort;
  }

  public void setCustPort(String custPort) {
    this.custPort = custPort;
  }

  public String getCustUsername() {
    return custUsername;
  }

  public void setCustUsername(String custUsername) {
    this.custUsername = custUsername;
  }

  public String getCustName() {
    return custName;
  }

  public void setCustName(String custName) {
    this.custName = custName;
  }

  public String getWebName() {
    return webName;
  }

  public void setWebName(String webName) {
    this.webName = webName;
  }

  public String getAdvertiser() {
    return advertiser;
  }

  public void setAdvertiser(String advertiser) {
    this.advertiser = advertiser;
  }

  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
  public Date getAcctCreateDate() {
    return acctCreateDate;
  }

  public void setAcctCreateDate(Date acctCreateDate) {
    this.acctCreateDate = acctCreateDate;
  }

  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
  public Date getAnnualSvcFeeDate() {
    return annualSvcFeeDate;
  }

  public void setAnnualSvcFeeDate(Date annualSvcFeeDate) {
    this.annualSvcFeeDate = annualSvcFeeDate;
  }

  public String getAnnualSvcFee() {
    return annualSvcFee;
  }

  public void setAnnualSvcFee(String annualSvcFee) {
    this.annualSvcFee = annualSvcFee;
  }

  public String getRewardsPercent() {
    return rewardsPercent;
  }

  public void setRewardsPercent(String rewardsPercent) {
    this.rewardsPercent = rewardsPercent;
  }

  public String getMgtFeePercent() {
    return mgtFeePercent;
  }

  public void setMgtFeePercent(String mgtFeePercent) {
    this.mgtFeePercent = mgtFeePercent;
  }

  public String getConsumption() {
    return consumption;
  }

  public void setConsumption(String consumption) {
    this.consumption = consumption;
  }

  public String getRemark1() {
    return remark1;
  }

  public void setRemark1(String remark1) {
    this.remark1 = remark1;
  }

  public String getRemark2() {
    return remark2;
  }

  public void setRemark2(String remark2) {
    this.remark2 = remark2;
  }

  public String getAm_contact() {
    return am_contact;
  }

  public void setAm_contact(String am_contact) {
    this.am_contact = am_contact;
  }

  public String getFin_contact() {
    return fin_contact;
  }

  public void setFin_contact(String fin_contact) {
    this.fin_contact = fin_contact;
  }

  public String getOps_contact() {
    return ops_contact;
  }

  public void setOps_contact(String ops_contact) {
    this.ops_contact = ops_contact;
  }

  public String getSales_contact() {
    return sales_contact;
  }

  public void setSales_contact(String sales_contact) {
    this.sales_contact = sales_contact;
  }

  public String getAm_email() {
    return am_email;
  }

  public void setAm_email(String am_email) {
    this.am_email = am_email;
  }

  public String getFin_email() {
    return fin_email;
  }

  public void setFin_email(String fin_email) {
    this.fin_email = fin_email;
  }

  public String getOps_email() {
    return ops_email;
  }

  public void setOps_email(String ops_email) {
    this.ops_email = ops_email;
  }

  public String getSales_email() {
    return sales_email;
  }

  public void setSales_email(String sales_email) {
    this.sales_email = sales_email;
  }

  public int getDeleted() {
    return deleted;
  }

  public void setDeleted(int deleted) {
    this.deleted = deleted;
  }

  public String getCreateBy() {
    return createBy;
  }

  public void setCreateBy(String createBy) {
    this.createBy = createBy;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public String getUpdateBy() {
    return updateBy;
  }

  public void setUpdateBy(String updateBy) {
    this.updateBy = updateBy;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public String getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(String isDeleted) {
    this.isDeleted = isDeleted;
  }

  public Boolean is_credit_client(){
    String remark1 = this.getRemark1();
    return (remark1.indexOf("账期") > -1) && !(remark1.indexOf("预付") > -1);
  }

}
