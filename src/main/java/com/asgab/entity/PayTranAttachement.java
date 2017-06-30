package com.asgab.entity;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class PayTranAttachement {
  private Long attachmentId;
  private Long tranNum;
  private Long processId;
  private String fileName;
  private String showName;
  private String path;
  private int deleted;
  private Date createDate;
  private String createBy;
  private Date updateDate;
  private String updateBy;

  public Long getAttachmentId() {
    return attachmentId;
  }

  public void setAttachmentId(Long attachmentId) {
    this.attachmentId = attachmentId;
  }

  public Long getTranNum() {
    return tranNum;
  }

  public void setTranNum(Long tranNum) {
    this.tranNum = tranNum;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getShowName() {
    return showName;
  }

  public void setShowName(String showName) {
    this.showName = showName;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public int getDeleted() {
    return deleted;
  }

  public void setDeleted(int deleted) {
    this.deleted = deleted;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public String getCreateBy() {
    return createBy;
  }

  public void setCreateBy(String createBy) {
    this.createBy = createBy;
  }

  public String getUpdateBy() {
    return updateBy;
  }

  public void setUpdateBy(String updateBy) {
    this.updateBy = updateBy;
  }
  
  public Long getProcessId() {
    return processId;
  }

  public void setProcessId(Long processId) {
    this.processId = processId;
  }

  public boolean getBooleanPDF() {
    if (StringUtils.isNotBlank(showName)) {
      String[] tmps = showName.split("\\.");
      String ext = tmps[tmps.length - 1];
      return "pdf".equalsIgnoreCase(ext);
    }
    return true;
  }

}
