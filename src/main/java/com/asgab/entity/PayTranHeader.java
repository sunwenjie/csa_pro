package com.asgab.entity;

import com.asgab.util.CommonUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class PayTranHeader {
  private Long tranNum;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date tranDate;
  private String currency;
  private String email;
  private double totalAmount;
  private String imageSysName;
  private String imageName;
  private Character status;
  private String remarks;
  private String description;
  private int deleted;
  private Date createDate;
  private String createBy;
  private Date updateDate;
  private String updateBy;
  private Boolean isAmSupport;

  private List<PayTranDetail> payTranDetails = new ArrayList<PayTranDetail>();
  private List<PayTranAttachement> payTranAttachements = new ArrayList<PayTranAttachement>();
  private List<PayTranAttachement> processAttachements = new ArrayList<PayTranAttachement>();
  private List<ExchangeRate> exchangeRates = new ArrayList<ExchangeRate>();

  private Map<String, String> currencys = new TreeMap<String, String>();
  private Map<Character, String> statuses = new TreeMap<Character, String>();
  private Map<Long, String> payCodes = new TreeMap<Long, String>();

  private String financeCheckUrl;
  private String financeConfirmUrl;
  private String amSupportUrl;
  private String bdUserType;

  public Long getTranNum() {
    return tranNum;
  }

  public void setTranNum(Long tranNum) {
    this.tranNum = tranNum;
  }

  public Date getTranDate() {
    return tranDate;
  }

  public String getTranDateFormat() {
    return CommonUtil.formatDate(getTranDate());
  }

  public String getTranDateDate() {
    if (getTranDate() != null) {
      DateFormat dateFormat = new SimpleDateFormat("MMM", Locale.ENGLISH);
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(getTranDate());
      return calendar.get(Calendar.DAY_OF_MONTH) + "-" + dateFormat.format(getTranDate()) + "-" + calendar.get(Calendar.YEAR);
    }
    return "";
  }

  public String getTranDateTime() {
    if (getTranDate() != null) {
      SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
      return sdf.format(getTranDate());
    }
    return "";
  }

  public void setTranDate(Date tranDate) {
    this.tranDate = tranDate;
  }


  public String getDecodedCurrency() {
    String tmp = currencys.get(currency);
    return tmp == null ? "" : tmp;
  }


  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public double getTotalAmount() {
    return totalAmount;
  }

  public String getFmtTotalAmount() {
    return CommonUtil.digSeg(totalAmount);
  }

  public String getFmtTotalAmountInRMB() {
    BigDecimal result = BigDecimal.ZERO;
    if (payTranDetails != null && payTranDetails.size() > 0) {
      for (PayTranDetail detail : payTranDetails) {
        result = result.add(new BigDecimal(detail.getAmountInRMB()));
      }
    }
    return CommonUtil.digSeg(result.doubleValue());
  }

  public void setTotalAmount(double totalAmount) {
    this.totalAmount = totalAmount;
  }

  public String getImageSysName() {
    return imageSysName;
  }

  public void setImageSysName(String imageSysName) {
    this.imageSysName = imageSysName;
  }

  public String getImageName() {
    return imageName;
  }

  public void setImageName(String imageName) {
    this.imageName = imageName;
  }


  public String getDecodedStatus() {
    String tmp = statuses.get(status);
    return tmp == null ? "" : tmp;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public List<PayTranDetail> getPayTranDetails() {
    return payTranDetails;
  }

  public int getPayTranDetailSize() {
    return getPayTranDetails().size();
  }

  public void setPayTranDetails(List<PayTranDetail> payTranDetails) {
    this.payTranDetails = payTranDetails;
  }

  public List<PayTranAttachement> getPayTranAttachements() {
    return payTranAttachements;
  }

  public void setPayTranAttachements(List<PayTranAttachement> payTranAttachements) {
    this.payTranAttachements = payTranAttachements;
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

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public Character getStatus() {
    return status;
  }

  public String getStatusStr() {
    return String.valueOf(status);
  }

  public void setStatus(Character status) {
    this.status = status;
  }

  public int getDeleted() {
    return deleted;
  }

  public void setDeleted(int deleted) {
    this.deleted = deleted;
  }


  public Map<String, String> getCurrencys() {
    return currencys;
  }

  public void setCurrencys(Map<String, String> currencys) {
    this.currencys = currencys;
  }

  public Map<Character, String> getStatuses() {
    return statuses;
  }

  public void setStatuses(Map<Character, String> statuses) {
    this.statuses = statuses;
  }

  public Map<Long, String> getPayCodes() {
    return payCodes;
  }

  public void setPayCodes(Map<Long, String> payCodes) {
    this.payCodes = payCodes;
  }

  public List<ExchangeRate> getExchangeRates() {
    return exchangeRates;
  }

  public void setExchangeRates(List<ExchangeRate> exchangeRates) {
    this.exchangeRates = exchangeRates;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<PayTranAttachement> getProcessAttachements() {
    return processAttachements;
  }

  public void setProcessAttachements(List<PayTranAttachement> processAttachements) {
    this.processAttachements = processAttachements;
  }

  public String getFinanceCheckUrl() {
    return financeCheckUrl;
  }

  public void setFinanceCheckUrl(String financeCheckUrl) {
    this.financeCheckUrl = financeCheckUrl;
  }

  public String getFinanceConfirmUrl() {
    return financeConfirmUrl;
  }

  public void setFinanceConfirmUrl(String financeConfirmUrl) {
    this.financeConfirmUrl = financeConfirmUrl;
  }



  public String getBdUserNames() {
    List<String> bdUserNames = new ArrayList<String>();
    String bdUserNamesStr = "";
    String bdUserName = "";
    if (payTranDetails != null && payTranDetails.size() > 0) {
      for (int i = 0; i < payTranDetails.size(); i++) {
        if (i == 0) {
          bdUserName = payTranDetails.get(i).getBdUserName();
        }
        bdUserNames.add(payTranDetails.get(i).getBdUserName());
      }
    }
    CommonUtil.distinctList(bdUserNames);
    if (bdUserNames != null && bdUserNames.size() > 0) {
      for (int i = 0; i < bdUserNames.size(); i++) {
        if (i != 0) {
          bdUserNamesStr += ",";
        }
        bdUserNamesStr += bdUserNames.get(i);
      }
    }
    if (bdUserNames.size() > 1) {
      return bdUserName + "<a onmouseover=\"showBDUsers(this,'" + bdUserNamesStr + "');\" onmouseout=\"$(this).popover('hide');\" data-html='true'><img class='moreIcon' src='static/images/more-outline.png'/></a>";
    } else {
      return bdUserNamesStr;
    }
  }

  public String getBdUserNamesSplit() {
    List<String> bdUserNames = new ArrayList<String>();
    for(PayTranDetail payTranDetail : payTranDetails){
      String bdUserName = payTranDetail.getBdUserName();
      bdUserNames.add(bdUserName);
    }
    CommonUtil.distinctList(bdUserNames);
    String bdUserNamesStr = "";
    if (bdUserNames != null && bdUserNames.size() > 0){
      int bdUserNamesSize = bdUserNames.size();
      for(int i=0; i< bdUserNamesSize; i++){
        if (i < 3)
          bdUserNamesStr += bdUserNames.get(i)+",";
      }
      bdUserNamesStr = bdUserNamesStr.substring(0,bdUserNamesStr.length() -1);
      if (bdUserNamesSize >= 3)
        bdUserNamesStr += "...";

    }
    return bdUserNamesStr;
  }

  public String getBdPort() {
    List<String> bdPorts = new ArrayList<String>();
    String bdPortsStr = "";
    String bdPort = "";
    if (payTranDetails != null && payTranDetails.size() > 0) {
      for (int i = 0; i < payTranDetails.size(); i++) {
        if (i == 0) {
          bdPort = payTranDetails.get(i).getBdPort();
        }
        bdPorts.add(payTranDetails.get(i).getBdPort());
      }
    }
    CommonUtil.distinctList(bdPorts);
    if (bdPorts != null && bdPorts.size() > 0) {
      for (int i = 0; i < bdPorts.size(); i++) {
        if (i != 0) {
          bdPortsStr += ",";
        }
        bdPortsStr += bdPorts.get(i);
      }
    }
    if (bdPorts.size() > 1) {
      return bdPort + "<a onmouseover=\"showBDPorts(this,'" + bdPortsStr + "');\" onmouseout=\"$(this).popover('hide');\" data-html='true'><img class='moreIcon' src='static/images/more-outline.png'/></a>";
    } else {
      return bdPortsStr;
    }
  }


  public Boolean getAmSupport() {
    return isAmSupport;
  }

  public void setAmSupport(Boolean amSupport) {
    isAmSupport = amSupport;
  }

  public String getAmSupportUrl() {
    return amSupportUrl;
  }

  public void setAmSupportUrl(String amSupportUrl) {
    this.amSupportUrl = amSupportUrl;
  }

  public String getBdUserType() {
	return bdUserType;
  }

  public void setBdUserType(String bdUserType) {
	this.bdUserType = bdUserType;
  }
}
