package com.asgab.entity;

public class MailReceiver {
  private Long id;
  private Long mailId;
  private String mailAddress;
  private String copyTo;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getMailId() {
    return mailId;
  }

  public void setMailId(Long mailId) {
    this.mailId = mailId;
  }

  public String getMailAddress() {
    return mailAddress;
  }

  public void setMailAddress(String mailAddress) {
    this.mailAddress = mailAddress;
  }

  public String getCopyTo() {
    return copyTo;
  }

  public void setCopyTo(String copyTo) {
    this.copyTo = copyTo;
  }
}
