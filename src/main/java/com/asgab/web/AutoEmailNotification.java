package com.asgab.web;

import com.asgab.core.mail.MailUtil;
import com.asgab.entity.*;
import com.asgab.entity.Process;
import com.asgab.service.paytran.BlockTradeService;
import com.asgab.util.CommonUtil;
import com.asgab.util.MoneyToChinese;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;

import javax.mail.MessagingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;

public class AutoEmailNotification {

  public final static String EMAIL_TEMPLATE_NAME_1 = "example1.ftl";

  public final static String EMAIL_TEMPLATE_NAME_2 = "example2.ftl";

  public final static String EMAIL_TEMPLATE_NAME_3 = "example3.ftl";

  public final static String EMAIL_TEMPLATE_NAME_4 = "example4.ftl";

  // 发送给创建paytran的email
  public final static String EMAIL_TEMPLATE_NAME_5 = "example5.ftl";

  // 发送给AM的email
  public final static String EMAIL_TEMPLATE_NAME_6 = "example6.ftl";


  public final static String EMAIL_TEMPLATE_NAME_REJECT = "reject.ftl";

  /***
   * send1 发送邮件 > Finance
   *
   * @param custMaster 客户信息
   * @param header 交易信息
   * @param parms 参数集合
   * @return success
   * @throws MessagingException
   * @throws TemplateException
   * @throws IOException
   */
  public static void send1(Map<String, CustMaster> custMasterMap, PayTranHeader header, Mail mail) throws Exception {

    Map<String, Object> map = new HashMap<String, Object>();
    // 公共头部
    getPublicHeader(map, header, null);
    // 收据上传日期
    map.put("uploadDate", header.getTranDateFormat());
    // 确认到账 / 拒绝到账 连接
    map.put("confirmArrivalURL", mail.getUrlConfirm() + "?showDescription=false");
    map.put("confirmArrivalURL2", mail.getUrlConfirm());
    map.put("rejectArrivalURL", mail.getUrlReject());

    map.put("custUserList", getCustUserList(header, custMasterMap));
    List<String> tmpFilePaths = getFilePaths(header);
    MailUtil.sendMailAndFileByTemplate(mail.getReceivers(), mail.getSubject(), tmpFilePaths, map, EMAIL_TEMPLATE_NAME_1);
    rmTmpFile(tmpFilePaths);
  }

  /***
   * send2 发送邮件 > Finance
   * 
   * @param custMaster 客户信息
   * @param header 交易信息
   * @param parms 参数集合
   * @return success
   */
  public static void send2(Map<String, CustMaster> custMasterMap, PayTranHeader header, Process process, Mail mail,BlockTrade blockTrade) throws Exception {

    Map<String, Object> map = new HashMap<String, Object>();
    // 公共头部
    getPublicHeader(map, header, process);
    // 收据上传日期
    map.put("uploadDate", header.getTranDateFormat());
    // 财务确认查账日期
    map.put("finConfirmAuditDate", mail.getFinConfirmAuditDate());
    // 财务查账人员
    map.put("finAuditBy", mail.getFinAuditBy());
    // 确认完成打款
    map.put("confirmPayTranURL", mail.getUrlConfirm());
    // 拒绝完成打款
    map.put("rejectPayTranURL", mail.getUrlReject());
    map.put("custUserList", getCustUserList(header, custMasterMap));
    if (blockTrade != null){
      map.put("blockTrade", blockTrade);
    }
    List<String> tmpFilePaths = getFilePaths(header);
    MailUtil.sendMailAndFileByTemplate(mail.getReceivers(), mail.getSubject(), tmpFilePaths, map, EMAIL_TEMPLATE_NAME_2);
    rmTmpFile(tmpFilePaths);

  }

  /***
   * send3 发送邮件 > Ops
   * 
   * @param custMaster 客户信息
   * @param header 交易信息
   * @return success
   */
  public static void send3(Map<String, CustMaster> custMasterMap, PayTranHeader header, Process process,Process finProcess, Mail mail) throws Exception {

    Map<String, Object> map = new HashMap<String, Object>();
    // 公共头部
    getPublicHeader(map, header, process);
    // 财务确认收款日期
    map.put("finConfirmReceivableDate", mail.getFinConfirmReceivableDate());
    // 财务确认打款日期
    map.put("finPayTranDate", mail.getFinPayTranDate());
    // 财务确收人员
    map.put("finConfirmReceivableBy", mail.getFinConfirmReceivableBy());
    // 财务打款人员
    map.put("finPayTranBy", mail.getFinPayTranBy());

    // 确认入账至客户用户名连接
    map.put("confirmCustArrivalURL", mail.getUrlConfirm());

    map.put("custUserList", getCustUserList(header, custMasterMap));
    map.put("annualInfoList", getAnnualInfoList(header, custMasterMap));

    if (finProcess != null){
      String totalRealAddAmount =  finProcess.getRealAddAmount() == 0.0 ? map.get("totalAmountTopup").toString() : CommonUtil.digSeg(finProcess.getRealAddAmount());
      map.put("totalRealAddAmount",totalRealAddAmount);
    }

    List<String> tmpFilePaths = getFilePaths(header);
    MailUtil.sendMailAndFileByTemplate(mail.getReceivers(), mail.getSubject(), tmpFilePaths, map, EMAIL_TEMPLATE_NAME_3);
    rmTmpFile(tmpFilePaths);

  }

  /***
   * send4 发送邮件 > AM & Sales
   * 
   * @param custMaster 客户信息
   * @param header 交易信息
   * @param parms 参数集合
   * @return success
   */
  public static void send4(Map<String, CustMaster> custMasterMap, PayTranHeader header, Process process,Process finProcess, Mail mail) throws Exception {

    Map<String, Object> map = new HashMap<String, Object>();
    // 公共头部
    getPublicHeader(map, header, process);
    // 财务查账日期
    map.put("finConfirmReceivableDate", mail.getFinConfirmReceivableDate());
    // 财务确认打款日期
    map.put("finPayTranDate", mail.getFinPayTranDate());
    // ops确认加款日期
    map.put("opsPayTranDate", mail.getOpsPayTranDate());

    // 财务确收人员
    map.put("finConfirmReceivableBy", mail.getFinConfirmReceivableBy());
    // 财务打款人员
    map.put("finPayTranBy", mail.getFinPayTranBy());
    // ops 确认加款人
    map.put("opsPayTranBy", mail.getOpsPayTranBy());

    map.put("custUserList", getCustUserList(header, custMasterMap));
    map.put("annualInfoList", getAnnualInfoList(header, custMasterMap));

    if (finProcess != null){
      String totalRealAddAmount =  finProcess.getRealAddAmount() == 0.0 ? map.get("totalAmountTopup").toString() : CommonUtil.digSeg(finProcess.getRealAddAmount());
      map.put("totalRealAddAmount",totalRealAddAmount);
    }

    List<String> tmpFilePaths = getFilePaths(header);
    MailUtil.sendMailAndFileByTemplate(mail.getReceivers(), mail.getSubject(), tmpFilePaths, map, EMAIL_TEMPLATE_NAME_4);
    rmTmpFile(tmpFilePaths);
  }

  /**
   * 发送邮件给paytran email
   * 
   * @param custMasterMap
   * @param header
   * @param parms
   * @throws Exception
   */
  public static void send5(PayTranHeader header, Mail mail, Process process,Process finProcess) throws Exception {

    Map<String, Object> map = new HashMap<String, Object>();
    // 公共头部
    getPublicHeader(map, header, process);
    map.put("currency_en", getDecodedCurrency(header.getCurrency(), "en"));
    // 数据上传时间
    map.put("uploadDate", CommonUtil.formatDate(header.getCreateDate()));
    // 确认日期
    map.put("finPayTranDate", CommonUtil.formatDate(header.getUpdateDate()));

    if (finProcess != null){
      String totalRealAddAmount =  finProcess.getRealAddAmount() == 0.0 ? map.get("totalAmountTopup").toString() : CommonUtil.digSeg(finProcess.getRealAddAmount());
      map.put("totalRealAddAmount",totalRealAddAmount);
    }

    List<Map<String, Object>> details = new ArrayList<Map<String, Object>>();
    for (PayTranDetail detail : header.getPayTranDetails()) {
      Map<String, Object> detailMap = new HashMap<String, Object>();
      detailMap.put("bdUserName", detail.getBdUserName());
      detailMap.put("payCode", detail.getPayCodeZH());
      detailMap.put("payCode_en", detail.getPayCodeEN());
      detailMap.put("amount", CommonUtil.digSeg(detail.getAmount()));
      detailMap.put("amountInRMB", CommonUtil.digSeg(detail.getAmountInRMB()));
      detailMap.put("additionAmount", CommonUtil.digSeg(detail.getAdditionAmount()));
      detailMap.put("additionAmount", CommonUtil.digSeg(detail.getAdditionAmount()));
      // 充值完成后自动上线推广消费？
      String is_recharge_online_zh = detail.isRechargeOnline() ? "是" : "否";
      String is_recharge_online_en = detail.isRechargeOnline() ? "Yes" : "No";
      detailMap.put("is_recharge_online_zh",is_recharge_online_zh);
      detailMap.put("is_recharge_online_en",is_recharge_online_en);
      details.add(detailMap);
    }
    map.put("details", details);

    List<String> tmpFilePaths = getFilePaths(header, false);
    MailUtil.sendMailAndFileByTemplate(mail.getReceivers(), mail.getSubject(), tmpFilePaths, map, EMAIL_TEMPLATE_NAME_5);
    rmTmpFile(tmpFilePaths);

  }

  /***
   * 财务拒绝到账
   * 
   * @param receiver 收件人
   * @param subject 主题
   * @param content 内容
   * @return
   */
  public static void send_reject(Mail mail, Map<String, CustMaster> custMasterMap, PayTranHeader header) throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    // 公共头部
    getPublicHeader(map, header, null);

    map.put("title", header.getStatus() == '2' ? "财务查账邮件拒绝" : "财务加款邮件拒绝");
    map.put("paytranNum", header.getTranNum() + "");
    map.put("description", StringUtils.isNotBlank(mail.getDescription()) ? mail.getDescription() : "");
    map.put("custEmail", header.getEmail());
    map.put("custUserList", getCustUserList(header, custMasterMap));
    map.put("annualInfoList", getAnnualInfoList(header, custMasterMap));

    List<String> tmpFilePaths = getFilePaths(header);
    MailUtil.sendMailAndFileByTemplate(mail.getReceivers(), mail.getSubject(), tmpFilePaths, map, EMAIL_TEMPLATE_NAME_REJECT);
    rmTmpFile(tmpFilePaths);
  }


  /***
   * send6 发送邮件 > AM，让AM上传客户消费证明
   *
   * @param custMasterMap 客户信息
   * @param header 交易信息
   * @param parms 参数集合
   * @return success
   */
  public static void send6(Map<String, CustMaster> custMasterMap, PayTranHeader header, Process process, Mail mail) throws Exception {

    Map<String, Object> map = new HashMap<String, Object>();
    // 公共头部
    getPublicHeader(map, header, process);
    // 收据上传日期
    map.put("uploadDate", header.getTranDateFormat());
    // 财务确认查账日期
    map.put("finConfirmAuditDate", mail.getFinConfirmAuditDate());
    // 财务查账人员
    map.put("finAuditBy", mail.getFinAuditBy());
    // 确认完成打款
    map.put("confirmPayTranURL", mail.getUrlConfirm());
    // 拒绝完成打款
    map.put("rejectPayTranURL", mail.getUrlReject());
    map.put("custUserList", getCustUserList(header, custMasterMap));

    List<String> tmpFilePaths = getFilePaths(header);
    MailUtil.sendMailAndFileByTemplate(mail.getReceivers(), mail.getSubject(), tmpFilePaths, map, EMAIL_TEMPLATE_NAME_6);
    rmTmpFile(tmpFilePaths);

  }

  /***
   * 返回一个字符串
   * 
   * @param object
   * @return
   */
  private static String getValue(Object object) {
    if (object == null)
      return "";

    if (object instanceof String)
      return object.toString();

    if (object instanceof Date)
      return CommonUtil.formatDate((Date) object, "yyyy-MM-dd HH:mm:ss");

    return "";
  }

  private static void getPublicHeader(Map<String, Object> map, PayTranHeader header, Process process) {
    // 交易号
    map.put("tradeNo", String.valueOf(header.getTranNum()));
    // 币种
    map.put("currency", getDecodedCurrency(header.getCurrency()));
    map.put("totalAmount", CommonUtil.digSeg(header.getTotalAmount()));
    // 加款总计
    String totalAmountTopup = CommonUtil.digSeg(getTotalAmountTopup(header));
    map.put("totalAmountTopup", totalAmountTopup);
    map.put("totalAmountInRMB", CommonUtil.digSeg(getTotalamountInRmb(header)));
    // 账单备注
    map.put("paymentRemark", getValue(header.getRemarks()));
    String process1Description = (process == null ? "" : StringUtils.isNotBlank(process.getDescription()) ? process.getDescription() : "");
    String totalRealAddAmount = (process == null ? "" : process.getRealAddAmount() == 0.0 ? "" : CommonUtil.digSeg(process.getRealAddAmount()));
    if( StringUtils.isBlank(totalRealAddAmount)){
      totalRealAddAmount = totalAmountTopup;
    }
    map.put("totalRealAddAmount",totalRealAddAmount);
    map.put("process1Description", process1Description);
    map.put("isTest", CommonUtil.isTest() ? "(来自测试环境)" : "");
  }

  private static Double getTotalamountInRmb(PayTranHeader header) {
    BigDecimal sum = BigDecimal.ZERO;
    for (int i = 0; i < header.getPayTranDetails().size(); i++) {
      PayTranDetail payTranDetail = header.getPayTranDetails().get(i);
      sum = sum.add(new BigDecimal(payTranDetail.getAmountInRMB()));
    }
    return sum.doubleValue();
  }

  private static Double getTotalAmountTopup(PayTranHeader header) {
    BigDecimal sum = BigDecimal.ZERO;
    for (int i = 0; i < header.getPayTranDetails().size(); i++) {
      PayTranDetail payTranDetail = header.getPayTranDetails().get(i);
      sum = sum.add(new BigDecimal(payTranDetail.getAdditionAmount()));
    }
    return sum.doubleValue();
  }

  /**
   * 公共的部分
   */
  private static List<Map<String, String>> getCustUserList(PayTranHeader header, Map<String, CustMaster> custMasterMap) {
    List<Map<String, String>> custUserList = new ArrayList<Map<String, String>>();
    for (int i = 0; i < header.getPayTranDetails().size(); i++) {
      PayTranDetail detail = header.getPayTranDetails().get(i);
      String baiduUserName = detail.getBdUserName();
      double amount = detail.getAmount();
      // 每一个百度用户添加一列数据
      Map<String, String> custUserMap = new HashMap<String, String>();
      // 获取对于的百度客户信息
      CustMaster custMaster = custMasterMap.get(baiduUserName);
      boolean existCustMaster = custMaster != null;
      // 客户用户名
      custUserMap.put("custUsername", baiduUserName);
      // 广告主名称
      custUserMap.put("custName", existCustMaster ? custMaster.getAdvertiser() : "");
      // 客户类型
      custUserMap.put("custType",existCustMaster ? custMaster.getRemark1() : "");

      // 入账金额小写
      custUserMap.put("rzjeLowercase", CommonUtil.digSeg(amount));
      // 入账金额大写
      custUserMap.put("rzjeCapital", MoneyToChinese.getMoneyString(amount));
      // 加款金额小写
      custUserMap.put("jkjeLowercase", CommonUtil.digSeg(detail.getAdditionAmount()));
      // 加款金额大写
      custUserMap.put("jkjeCapital", MoneyToChinese.getMoneyString(detail.getAdditionAmount()));
      // 入账项目
      String item = "";
      boolean isTopup = false;
      if ("加款".equalsIgnoreCase(detail.getPayCodeZH()) || "Topup".equalsIgnoreCase(detail.getPayCodeEN())) {
        // 如果是加款
        isTopup = true;
        item = "管理费 " + CommonUtil.digSeg(Double.valueOf(detail.getMgtFee()) * 100) + "%";
      } else {
        item = detail.getDecodePayCode();
      }
      custUserMap.put("item", item);
      // 如果是年费,开户费,保证金不计算返点(金额)
      if (!isTopup) {
        // 返点率
        custUserMap.put("rebate", "0.00");
        // 返点金额
        custUserMap.put("rebateAmount", "0.00");
      } else {
        // 返点率
        custUserMap.put("rebate", CommonUtil.digSeg(detail.getRewards() * 100) + "%");
        // 返点金额
        custUserMap.put("rebateAmount", CommonUtil.digSeg(detail.getRebateValue()));
      }
      // 赠送金额
      custUserMap.put("giftAmount", "0.00");

      // 端口
      custUserMap.put("custPort", existCustMaster ? custMaster.getCustPort() : "");
      // 销售
      custUserMap.put("sales_contact", existCustMaster ? custMaster.getSales_contact() : "");
      // 客服
      custUserMap.put("custService", existCustMaster ? custMaster.getAm_contact() : "");
      // 代理商
      custUserMap.put("agent", existCustMaster ? custMaster.getCustName() : "");
      // 充值完成后自动上线推广消费？
      String is_recharge_online_zh = detail.isRechargeOnline() ? "是" : "否";
      custUserMap.put("is_recharge_online",is_recharge_online_zh);
      custUserList.add(custUserMap);
    }

    return custUserList;
  }


  /*
   * private static String getRebateValue(String currency,Double mgtFee,PayTranDetail detail,
   * CustMaster custMaster) { // 如果是SZ ,港币 返点金额= 客户入账金额/汇率/(1+管理费)*续费返点 (前面的一段通过AmountInRmb解决)
   * AmountInRmb如果是香港就是/汇率,不是香港就是*汇率 // 如果是SZ ,其他 返点金额= 客户入账金额*汇率/(1+管理费)*续费返点 // 如果是HK, 港币 返点金额=
   * 加款金额*续费返点 // 如果是HK, 其他 返点金额= 加款金额*续费返点 BigDecimal rebateValue = BigDecimal.ZERO;
   * if(custMaster.getFin_email().contains("SZ")&&"HKD".equalsIgnoreCase(currency)){ // rebateValue
   * = new BigDecimal(detail.getAmountInRMB()).divide(BigDecimal.ONE.add(new BigDecimal(mgtFee)) ,2,
   * BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(detail.getRewards()));
   * 
   * }else if(custMaster.getFin_email().contains("SZ") && !"HKD".equalsIgnoreCase(currency) ){ //
   * rebateValue = new BigDecimal(detail.getAmountInRMB()).divide(BigDecimal.ONE.add(new
   * BigDecimal(mgtFee)) ,2, BigDecimal.ROUND_HALF_UP).multiply(new
   * BigDecimal(detail.getRewards()));
   * 
   * }else if(custMaster.getFin_email().contains("HK") && "HKD".equalsIgnoreCase(currency)){ //
   * rebateValue = new BigDecimal(detail.getAdditionAmount()).multiply(new
   * BigDecimal(detail.getRewards()));
   * 
   * }else if(custMaster.getFin_email().contains("HK") && !"HKD".equalsIgnoreCase(currency)){
   * 
   * rebateValue = new BigDecimal(detail.getAdditionAmount()).multiply(new
   * BigDecimal(detail.getRewards()));
   * 
   * } return CommonUtil.reserved2Digit(rebateValue.doubleValue()); }
   */

  /**
   * 公共部分2 用于模板34
   * 
   * @param header
   * @param custMasterMap
   * @return
   */
  private static List<Map<String, String>> getAnnualInfoList(PayTranHeader header, Map<String, CustMaster> custMasterMap) {
    List<Map<String, String>> annualInfoList = new ArrayList<Map<String, String>>();
    List<String> distinctBDUserNameList = getDistinctCustUserName(header.getPayTranDetails());
    for (String bdUserName : distinctBDUserNameList) {
      Map<String, String> tmpMap = new HashMap<String, String>();
      // 获取对于的百度客户信息
      CustMaster custMaster = custMasterMap.get(bdUserName);
      boolean existCustMaster = custMaster != null;
      // 广告主名称
      tmpMap.put("custName", existCustMaster ? custMaster.getAdvertiser() : "");
      // 收取年服务费时间
      tmpMap.put("annualSvcFeeDate", existCustMaster ? CommonUtil.formatDate(custMaster.getAnnualSvcFeeDate(), "yyyy年MM月") : "");
      // 续费返点率
      Double tmpRewardsPercent = Double.parseDouble(custMaster.getRewardsPercent());
      tmpMap.put("rewardsPercent", tmpRewardsPercent.doubleValue() == 0 ? "0" : CommonUtil.reserved2Digit(tmpRewardsPercent * 100) + "%");
      // 收取的年服务费
      Double tmpAnnualSvcFee = Double.parseDouble(custMaster.getAnnualSvcFee());
      tmpMap.put("annualSvcFee", tmpAnnualSvcFee.doubleValue() == 0 ? "0" : tmpAnnualSvcFee + "");
      // 管理费率
      Double tmpMgtFeePercent = Double.parseDouble(custMaster.getMgtFeePercent());
      tmpMap.put("mgtFeePercent", tmpMgtFeePercent.doubleValue() == 0 ? "0" : CommonUtil.reserved2Digit(tmpMgtFeePercent * 100) + "%");
      // 备注
      tmpMap.put("remark1", StringUtils.isNotBlank(custMaster.getRemark1()) ? custMaster.getRemark1() : "");
      annualInfoList.add(tmpMap);
    }
    return annualInfoList;
  }

  private static List<String> getDistinctCustUserName(List<PayTranDetail> payTranDetails) {
    Map<String, String> map = new HashMap<String, String>();
    for (PayTranDetail detail : payTranDetails) {
      map.put(detail.getBdUserName(), detail.getBdUserName());
    }
    List<String> bdUserNameList = new ArrayList<String>();
    Set<Entry<String, String>> bdSet = map.entrySet();
    Iterator<Entry<String, String>> bdIterator = bdSet.iterator();
    while (bdIterator.hasNext()) {
      bdUserNameList.add(bdIterator.next().getKey());
    }
    return bdUserNameList;
  }

  private static String getDecodedCurrency(String currency) {
    return getDecodedCurrency(currency, "zh");
  }

  private static String getDecodedCurrency(String currency, String language) {
    Iterator<Entry<String, String>> iterator = ("zh".equalsIgnoreCase(language) ? CommonUtil.CURRENCY_ZH : CommonUtil.CURRENCY_EN).entrySet().iterator();
    while (iterator.hasNext()) {
      Entry<String, String> entry = iterator.next();
      if (entry.getKey().equalsIgnoreCase(currency)) {
        return entry.getValue();
      }
    }

    return "";
  }

  /*
   * 复制到临时目录改成实际显示名, 因为实际目录名称是UUID
   * 
   * @param payTranHeader
   * 
   * @return
   */
  private static List<String> getFilePaths(PayTranHeader payTranHeader) {
    return getFilePaths(payTranHeader, true);
  }

  private static List<String> getFilePaths(PayTranHeader payTranHeader, boolean includeProcessFile) {
    List<String> filePaths = new ArrayList<String>();
    List<PayTranAttachement> attachments = payTranHeader.getPayTranAttachements();
    // 添加check,confirm附件
    if (includeProcessFile) {
      attachments.addAll(payTranHeader.getProcessAttachements());
    }
    for (PayTranAttachement attachment : attachments) {
      String cpFilePath = cpFile(attachment.getPath() + "/" + attachment.getFileName(), attachment.getShowName());
      filePaths.add(cpFilePath);
    }
    return filePaths;
  }

  /*
   * 附件显示实际名称
   * 
   * @param fileName return 显示名字的绝对路径
   */
  private static String tmpDirStr = "/tmp";

  public static String cpFile(String fileName, String showName) {
    File tmpDir = new File(tmpDirStr);
    if (!tmpDir.exists()) {
      tmpDir.mkdirs();
    }

    // 创建临时目录
    File tmpUuidDir = new File(tmpDirStr + "/" + UUID.randomUUID().toString());
    tmpUuidDir.mkdirs();

    File fileSource = new File(fileName);
    if (fileSource.exists()) {
      try {
        File fileTarget = new File(tmpUuidDir.getAbsolutePath() + "/" + showName);
        FileInputStream fis = new FileInputStream(fileSource);
        FileOutputStream fos = new FileOutputStream(fileTarget);

        byte[] data = new byte[1024];
        int len = 0;
        while ((len = fis.read(data, 0, 1024)) != -1) {
          fos.write(data, 0, len);
        }
        fis.close();
        fos.close();
        return fileTarget.getAbsolutePath();
      } catch (Exception ex) {
        ex.printStackTrace();
        // 附件出问题
        return fileName;
      }
    }
    return fileName;
  }

  /*
   * 删除临时文件
   * 
   * @param tmpFilePaths
   */
  public static void rmTmpFile(List<String> tmpFilePaths) {
    try {
      if (tmpFilePaths != null && tmpFilePaths.size() > 0) {
        for (String tmpFilePath : tmpFilePaths) {
          // 只删除/tmp的. 防止误删除
          if (!tmpFilePath.startsWith(tmpDirStr)) {
            continue;
          }
          File fileDir = new File(tmpFilePath).getParentFile();
          File[] dirFiles = fileDir.listFiles();
          if (dirFiles != null && dirFiles.length > 0) {
            for (File dirFile : dirFiles) {
              // 删除目录下的文件
              dirFile.delete();
            }
          }
          // 删除文件夹
          fileDir.delete();
        }
      }
    } catch (Exception ex) {
      // 删除临时文件失败错误可以忽略
      ex.printStackTrace();
    }
  }
}
