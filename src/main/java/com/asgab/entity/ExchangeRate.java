package com.asgab.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class ExchangeRate {
  private Long id;
  private String base_currency;
  private String currency;
  private double rate;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date start_date;
  private Date created_at;
  private Date updated_at;
  private int deleted;
  private String create_by;
  private String update_by;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getBase_currency() {
    return base_currency;
  }

  public void setBase_currency(String base_currency) {
    this.base_currency = base_currency;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public double getRate() {
    return rate;
  }

  public void setRate(double rate) {
    this.rate = rate;
  }

  public Date getStart_date() {
    return start_date;
  }

  public void setStart_date(Date start_date) {
    this.start_date = start_date;
  }

  public Date getCreated_at() {
    return created_at;
  }

  public void setCreated_at(Date created_at) {
    this.created_at = created_at;
  }

  public Date getUpdated_at() {
    return updated_at;
  }

  public void setUpdated_at(Date updated_at) {
    this.updated_at = updated_at;
  }

  public int getDeleted() {
    return deleted;
  }

  public void setDeleted(int deleted) {
    this.deleted = deleted;
  }

  public String getCreate_by() {
    return create_by;
  }

  public void setCreate_by(String create_by) {
    this.create_by = create_by;
  }

  public String getUpdate_by() {
    return update_by;
  }

  public void setUpdate_by(String update_by) {
    this.update_by = update_by;
  }

}
