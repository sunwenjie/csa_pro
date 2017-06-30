package com.asgab.entity;

import java.util.Date;

public class CurrMaster {

  private Long id;
  private String curr_code;
  private String curr_name;
  private double curr_rate;
  private double lastUpdateRate;
  private Date lastUpdateDate;
  private String lastUpdateBy;
  private Date updateDate;
  private String updateBy;
  private int deleted;

  private String lastUpdateName;

  public Long getId() {
    return id;
  }

  public static void main(String[] args) {
    System.out.println(org.apache.shiro.codec.Base64.decodeToString("4AvVhmFLUs0KTA3Kprsdag=="));
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCurr_code() {
    return curr_code;
  }

  public void setCurr_code(String curr_code) {
    this.curr_code = curr_code;
  }

  public String getCurr_name() {
    return curr_name;
  }

  public void setCurr_name(String curr_name) {
    this.curr_name = curr_name;
  }

  public double getCurr_rate() {
    return curr_rate;
  }

  public void setCurr_rate(double curr_rate) {
    this.curr_rate = curr_rate;
  }

  public double getLastUpdateRate() {
    return lastUpdateRate;
  }

  public void setLastUpdateRate(double lastUpdateRate) {
    this.lastUpdateRate = lastUpdateRate;
  }

  public Date getLastUpdateDate() {
    return lastUpdateDate;
  }

  public void setLastUpdateDate(Date lastUpdateDate) {
    this.lastUpdateDate = lastUpdateDate;
  }

  public String getLastUpdateBy() {
    return lastUpdateBy;
  }

  public void setLastUpdateBy(String lastUpdateBy) {
    this.lastUpdateBy = lastUpdateBy;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public String getUpdateBy() {
    return updateBy;
  }

  public void setUpdateBy(String updateBy) {
    this.updateBy = updateBy;
  }

  public int getDeleted() {
    return deleted;
  }

  public void setDeleted(int deleted) {
    this.deleted = deleted;
  }

  public String getLastUpdateName() {
    return lastUpdateName;
  }

  public void setLastUpdateName(String lastUpdateName) {
    this.lastUpdateName = lastUpdateName;
  }


}
