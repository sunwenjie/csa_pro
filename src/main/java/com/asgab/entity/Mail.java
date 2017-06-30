package com.asgab.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Mail {
  private Long id;
  private String receiver;
  private String copyto;
  private String subject;
  private Long paytranNum;
  private Long processId;
  private String urlConfirm;
  private String urlReject;
  private String template;
  private Date createDate;
  private Date sendDate;
  private Character status;

  // 插入从表的
  private List<String> receivers = new ArrayList<String>();

  // for app
  private String description;
  private String finConfirmAuditDate;
  private String finAuditBy;

  private String finConfirmReceivableDate;
  private String finConfirmReceivableBy;

  private String finPayTranDate;
  private String finPayTranBy;

  private String opsPayTranDate;
  private String opsPayTranBy;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getReceiver() {
    return receiver;
  }

  public void setReceiver(String receiver) {
    this.receiver = receiver;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public Long getProcessId() {
    return processId;
  }

  public void setProcessId(Long processId) {
    this.processId = processId;
  }

  public String getUrlConfirm() {
    return urlConfirm;
  }

  public void setUrlConfirm(String urlConfirm) {
    this.urlConfirm = urlConfirm;
  }

  public String getUrlReject() {
    return urlReject;
  }

  public void setUrlReject(String urlReject) {
    this.urlReject = urlReject;
  }

  public String getTemplate() {
    return template;
  }

  public void setTemplate(String template) {
    this.template = template;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Date getSendDate() {
    return sendDate;
  }

  public void setSendDate(Date sendDate) {
    this.sendDate = sendDate;
  }

  public Character getStatus() {
    return status;
  }

  public void setStatus(Character status) {
    this.status = status;
  }

  public Long getPaytranNum() {
    return paytranNum;
  }

  public void setPaytranNum(Long paytranNum) {
    this.paytranNum = paytranNum;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getCopyto() {
    return copyto;
  }

  public void setCopyto(String copyto) {
    this.copyto = copyto;
  }

  public List<String> getReceivers() {
    return receivers;
  }

  public void setReceivers(List<String> receivers) {
    this.receivers = receivers;
  }

  public String getFinConfirmAuditDate() {
    return finConfirmAuditDate;
  }

  public void setFinConfirmAuditDate(String finConfirmAuditDate) {
    this.finConfirmAuditDate = finConfirmAuditDate;
  }

  public String getFinAuditBy() {
    return finAuditBy;
  }

  public void setFinAuditBy(String finAuditBy) {
    this.finAuditBy = finAuditBy;
  }

  public String getFinConfirmReceivableDate() {
    return finConfirmReceivableDate;
  }

  public void setFinConfirmReceivableDate(String finConfirmReceivableDate) {
    this.finConfirmReceivableDate = finConfirmReceivableDate;
  }

  public String getFinConfirmReceivableBy() {
    return finConfirmReceivableBy;
  }

  public void setFinConfirmReceivableBy(String finConfirmReceivableBy) {
    this.finConfirmReceivableBy = finConfirmReceivableBy;
  }

  public String getFinPayTranDate() {
    return finPayTranDate;
  }

  public void setFinPayTranDate(String finPayTranDate) {
    this.finPayTranDate = finPayTranDate;
  }

  public String getFinPayTranBy() {
    return finPayTranBy;
  }

  public void setFinPayTranBy(String finPayTranBy) {
    this.finPayTranBy = finPayTranBy;
  }

  public String getOpsPayTranDate() {
    return opsPayTranDate;
  }

  public void setOpsPayTranDate(String opsPayTranDate) {
    this.opsPayTranDate = opsPayTranDate;
  }

  public String getOpsPayTranBy() {
    return opsPayTranBy;
  }

  public void setOpsPayTranBy(String opsPayTranBy) {
    this.opsPayTranBy = opsPayTranBy;
  }

}
