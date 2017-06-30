package com.asgab.entity;

import java.util.Date;

public class PaymentPurpose {

  private Long id;
  private String pay_code;
  private String pay_purpose;
  private String pay_purposeEN;
  private String createBy;
  private Date createDate;
  private int deleted;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPay_code() {
    return pay_code;
  }

  public void setPay_code(String pay_code) {
    this.pay_code = pay_code;
  }

  public String getPay_purpose() {
    return pay_purpose;
  }

  public void setPay_purpose(String pay_purpose) {
    this.pay_purpose = pay_purpose;
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

  public int getDeleted() {
    return deleted;
  }

  public void setDeleted(int deleted) {
    this.deleted = deleted;
  }

  public String getPay_purposeEN() {
    return pay_purposeEN;
  }

  public void setPay_purposeEN(String pay_purposeEN) {
    this.pay_purposeEN = pay_purposeEN;
  }

}
