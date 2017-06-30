package com.asgab.service.paytran;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.asgab.service.custMaster.CustMasterService;
import com.asgab.web.AutoEmailNotification;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.asgab.core.pagination.Page;
import com.asgab.entity.CustMaster;
import com.asgab.entity.CustPaymentInfo;
import com.asgab.entity.ExchangeRate;
import com.asgab.entity.Mail;
import com.asgab.entity.PayTranAttachement;
import com.asgab.entity.PayTranDetail;
import com.asgab.entity.PayTranHeader;
import com.asgab.entity.PaymentPurpose;
import com.asgab.entity.Process;
import com.asgab.repository.CustMasterMapper;
import com.asgab.repository.PayTranAttachmentMapper;
import com.asgab.repository.PayTranDetailMapper;
import com.asgab.repository.PayTranHeaderMapper;
import com.asgab.service.account.ShiroDbRealm.ShiroUser;
import com.asgab.service.exchangeRate.ExchangeRateService;
import com.asgab.service.paymentPurpose.PaymentPurposeService;
import com.asgab.service.process.ProcessService;
import com.asgab.util.CommonUtil;

@Component
@Transactional
public class PayTranHeaderService {

  @Resource
  private PayTranHeaderMapper payTranHeaderMapper;

  @Resource
  private PayTranDetailMapper payTranDetailMapper;

  @Resource
  private PayTranAttachmentMapper payTranAttachmentMapper;

  @Resource
  private CustMasterMapper custMasterMapper;

  @Resource
  private ProcessService processService;

  @Autowired
  private ExchangeRateService exchangeRateService;

  @Autowired
  private PaymentPurposeService paymentPurposeService;

  @Resource
  private CustMasterService custMasterService;


  public PayTranHeader get(Long tranNum) {
    PayTranHeader payTranHeader = payTranHeaderMapper.get(tranNum);
    if (payTranHeader != null) {
      payTranHeader.setPayTranDetails(payTranDetailMapper.get(tranNum));
      payTranHeader.setPayTranAttachements(payTranAttachmentMapper.get(tranNum));
    }
    return payTranHeader;
  }

  public List<PayTranHeader> getAllPayTranHeaders() {
    return payTranHeaderMapper.search(null);
  }

  public Page<PayTranHeader> getAllPayTranHeaders(Page<PayTranHeader> page) {
    List<PayTranHeader> payTranHeaders = payTranHeaderMapper.search(page.getSearchMap(), page.getRowBounds());
    resetPaytranHeaderDetails(payTranHeaders);
    int count = payTranHeaderMapper.count(page.getSearchMap());
    page.setContent(payTranHeaders);
    page.setTotal(count);
    return page;
  }
  
  /**
   * header添加detail
   * @param payTranHeaders
   */
  private void resetPaytranHeaderDetails(List<PayTranHeader> payTranHeaders){
    for(int i = 0 ; i<payTranHeaders.size();i++){
      List<PayTranDetail> details = payTranDetailMapper.get(payTranHeaders.get(i).getTranNum());
      payTranHeaders.get(i).setPayTranDetails(details);
      HashSet<Integer> types = new HashSet<>();
      for(int j = 0; j < details.size(); j++){
    	CustMaster user = custMasterService.findByCustUsername(details.get(j).getBdUserName());
    	if("N/A".equals(user.getCustName())){
    	  types.add(2);
    	}
    	types.add(user.is_credit_client() ? 1 : 0);
      }
      int k = types.size();
      if (k == 0){
    	payTranHeaders.get(i).setBdUserType("");
      }
      types.add(1);
      if (k == types.size()){
    	payTranHeaders.get(i).setBdUserType("账期");
      }
      else {
    	payTranHeaders.get(i).setBdUserType("预付");
      }
    }
  }

  public void save(PayTranHeader payTranHeader) {
    payTranHeader.setCreateBy(getCurrentUser());
    payTranHeader.setCreateDate(new Date());
    payTranHeaderMapper.save(payTranHeader);
  }


  @Transactional(rollbackFor = Exception.class)
  public Mail save(PayTranHeader payTranHeader, String[] details, String[] attachments) throws Exception {
    payTranHeader.setTranDate(new Date());
    setTotalAmount(payTranHeader, details);
    save(payTranHeader);
    // save detail
    dealDetailAndAttachment(payTranHeader, details, attachments);
    // update isAmSupport
    payTranHeaderMapper.update(isAmSupport(payTranHeader.getTranNum()));
    // 第一封给财务审批 check/reject 邮件
    return processService.noticeFinance(payTranHeader.getTranNum());
  }


  private void setTotalAmount(PayTranHeader payTranHeader, String[] details) {
    BigDecimal totamAmount = new BigDecimal(0);
    for (String detail : details) {
      String[] detailColums = detail.split("#");
      double amount = StringUtils.isNoneBlank(detailColums[2]) ? Double.parseDouble(detailColums[2]) : 0d;
      totamAmount = totamAmount.add(new BigDecimal(amount));
    }
    payTranHeader.setTotalAmount(totamAmount.doubleValue());
  }

  private void dealDetailAndAttachment(PayTranHeader payTranHeader, String[] details, String[] attachments) {
    final List<ExchangeRate> exchangeRates = exchangeRateService.getAllAvailExchangeRates();
    final List<PaymentPurpose> paymentPurposes = paymentPurposeService.getAllPaymentPurpose();
    for (String detail : details) {
      PayTranDetail payTranDetail = new PayTranDetail();
      String[] detailColums = detail.split("#");
      // bdUserName+"#"+payCode+"#"+amount+#amountINRMB
      payTranDetail.setTranNum(payTranHeader.getTranNum());
      payTranDetail.setCreateBy(getCurrentUser());
      payTranDetail.setCreateDate(new Date());
      payTranDetail.setBdUserName(detailColums[0]);
      payTranDetail.setPayCode(Long.parseLong(detailColums[1]));
      payTranDetail.setAmount(StringUtils.isNoneBlank(detailColums[2]) ? Double.parseDouble(detailColums[2]) : 0d);
      payTranDetail.setAmountInRMB(CommonUtil.transferMoneyToRMB(exchangeRates, payTranHeader.getCurrency(), payTranDetail.getAmount()));
      if (detailColums.length > 4) {
        payTranDetail.setRechargeOnline("1".equals(detailColums[4]) && payTranDetail.getPayCode() == 1 ? true : false);
      }
      // 获取百度用户信息
      CustMaster custMaster = custMasterMapper.findByCustUsername(payTranDetail.getBdUserName());
      if (custMaster != null) {
        Double manageFeePercent = Double.parseDouble(StringUtils.isNotBlank(custMaster.getMgtFeePercent()) ? custMaster.getMgtFeePercent() : "0");
        Double rewardsPercent = Double.parseDouble(StringUtils.isNotBlank(custMaster.getRewardsPercent()) ? custMaster.getRewardsPercent() : "0");
        payTranDetail.setMgtFee(manageFeePercent);
        payTranDetail.setRewards(rewardsPercent);
        // 判断是不是加款
        if (isTopup(paymentPurposes, payTranDetail.getPayCode())) {
          // 加款金额
          payTranDetail.setAdditionAmount(calAdditionAmount(payTranHeader.getCurrency(), payTranDetail, custMaster));
        } else {
          payTranDetail.setAdditionAmount(0);
        }
      } else {
        if (payTranDetail.getPayCode() == 1) {
          payTranDetail.setAdditionAmount(
              CommonUtil.transferMoneyToBigDecimalRMB(exchangeRates, payTranHeader.getCurrency(), payTranDetail.getAmount()).doubleValue());
        } else {
          payTranDetail.setAdditionAmount(0);
        }
      }
      // 返点金额
      payTranDetail.setRebateValue(getRebateValue(payTranHeader.getCurrency(), payTranDetail.getMgtFee(), payTranDetail, custMaster));

      payTranDetailMapper.save(payTranDetail);
    }

    // save attachment
    if (attachments != null && attachments.length > 0) {
      for (String attachmentId : attachments) {
        PayTranAttachement tmpAtta = payTranAttachmentMapper.getById(Long.parseLong(attachmentId));
        tmpAtta.setTranNum(payTranHeader.getTranNum());
        payTranAttachmentMapper.update(tmpAtta);
      }
    }
  }

  private boolean isTopup(List<PaymentPurpose> paymentPurposes, Long payCode) {
    boolean flag = false;
    if (paymentPurposes != null && paymentPurposes.size() > 0) {
      for (PaymentPurpose paymentPurpose : paymentPurposes) {
        if (paymentPurpose.getId().longValue() == payCode.longValue()
            && ("加款".equalsIgnoreCase(paymentPurpose.getPay_purpose()) || "Topup".equalsIgnoreCase(paymentPurpose.getPay_purposeEN()))) {
          flag = true;
          break;
        }
      }
    }
    return flag;
  }



  /**
   * 获取加款金额
   * 
   * @return
   */
  private double calAdditionAmount(String currency, PayTranDetail detail, CustMaster custMaster) {
    // 如果是SZ 货币是HKD 加款金额=客户入账金额/汇率/(1+管理费)*(1+续费返点) (前面的用amountInRMB解决)
    // 如果是SZ 货币不是HKD 加款金额=客户入账金额*汇率/(1+管理费)*(1+续费返点)
    // 如果是HK 货币是HKD 加款金额=客户入账金额/汇率/(1+管理费)/(1-续费返点)
    // 如果是HK 货币不是HKD 加款金额=客户入账金额*汇率/(1+管理费)/(1-续费返点)
    BigDecimal additionAmount = BigDecimal.ZERO;
    // 1+管理费
    BigDecimal onePlusMgt = BigDecimal.ONE.add(new BigDecimal(detail.getMgtFee()));
    // 1+续费返点
    BigDecimal onePlusReward = BigDecimal.ONE.add(new BigDecimal(detail.getRewards()));
    // 1-续费返点
    BigDecimal oneMinusReward = BigDecimal.ONE.subtract(new BigDecimal(detail.getRewards()));
    // //////
    if (custMaster.getFin_email().contains("SZ") && "HKD".equalsIgnoreCase(currency)) {
      //
      additionAmount = new BigDecimal(detail.getAmountInRMB()).divide(onePlusMgt, 2, BigDecimal.ROUND_HALF_UP).multiply(onePlusReward);

    } else if (custMaster.getFin_email().contains("SZ") && !"HKD".equalsIgnoreCase(currency)) {
      //
      additionAmount = new BigDecimal(detail.getAmountInRMB()).divide(onePlusMgt, 2, BigDecimal.ROUND_HALF_UP).multiply(onePlusReward);

    } else if (custMaster.getFin_email().contains("HK") && "HKD".equalsIgnoreCase(currency)) {

      additionAmount =
          new BigDecimal(detail.getAmountInRMB()).divide(onePlusMgt, 2, BigDecimal.ROUND_HALF_UP).divide(oneMinusReward, 2, BigDecimal.ROUND_HALF_UP);

    } else if (custMaster.getFin_email().contains("HK") && !"HKD".equalsIgnoreCase(currency)) {

      additionAmount =
          new BigDecimal(detail.getAmountInRMB()).divide(onePlusMgt, 2, BigDecimal.ROUND_HALF_UP).divide(oneMinusReward, 2, BigDecimal.ROUND_HALF_UP);

    }

    return additionAmount.doubleValue();
  }

  private double getRebateValue(String currency, Double mgtFee, PayTranDetail detail, CustMaster custMaster) {
    // 如果是SZ ,港币 返点金额= 客户入账金额/汇率/(1+管理费)*续费返点 (前面的一段通过AmountInRmb解决) AmountInRmb如果是香港就是/汇率,不是香港就是*汇率
    // 如果是SZ ,其他 返点金额= 客户入账金额*汇率/(1+管理费)*续费返点
    // 如果是HK, 港币 返点金额= 加款金额*续费返点
    // 如果是HK, 其他 返点金额= 加款金额*续费返点
    BigDecimal rebateValue = BigDecimal.ZERO;
    if (custMaster.getFin_email().contains("SZ") && "HKD".equalsIgnoreCase(currency)) {
      //
      rebateValue = new BigDecimal(detail.getAmountInRMB()).divide(BigDecimal.ONE.add(new BigDecimal(mgtFee)), 2, BigDecimal.ROUND_HALF_UP)
          .multiply(new BigDecimal(detail.getRewards()));

    } else if (custMaster.getFin_email().contains("SZ") && !"HKD".equalsIgnoreCase(currency)) {
      //
      rebateValue = new BigDecimal(detail.getAmountInRMB()).divide(BigDecimal.ONE.add(new BigDecimal(mgtFee)), 2, BigDecimal.ROUND_HALF_UP)
          .multiply(new BigDecimal(detail.getRewards()));

    } else if (custMaster.getFin_email().contains("HK") && "HKD".equalsIgnoreCase(currency)) {
      //
      rebateValue = new BigDecimal(detail.getAdditionAmount()).multiply(new BigDecimal(detail.getRewards()));

    } else if (custMaster.getFin_email().contains("HK") && !"HKD".equalsIgnoreCase(currency)) {

      rebateValue = new BigDecimal(detail.getAdditionAmount()).multiply(new BigDecimal(detail.getRewards()));

    }
    return rebateValue.doubleValue();
  }

  public void update(PayTranHeader payTranHeader) {
    payTranHeader.setUpdateBy(getCurrentUser());
    payTranHeader.setUpdateDate(new Date());
    payTranHeaderMapper.update(payTranHeader);
  }

  public void update(PayTranHeader payTranHeader, String[] details, String[] attachements) {
    update(payTranHeader);
    List<PayTranDetail> oldDetails = payTranDetailMapper.get(payTranHeader.getTranNum());
    for (PayTranDetail oldDetail : oldDetails) {
      payTranDetailMapper.delete(oldDetail.getTranNumDetail());
    }
    // 添加新的
    dealDetailAndAttachment(payTranHeader, details, attachements);
  }

  public void delete(Long tranNum) {
    List<PayTranDetail> details = payTranDetailMapper.get(tranNum);
    for (PayTranDetail detail : details) {
      payTranDetailMapper.delete(detail.getTranNumDetail());
    }
    List<PayTranAttachement> attachments = payTranAttachmentMapper.get(tranNum);
    for (PayTranAttachement attach : attachments) {
      payTranAttachmentMapper.delete(attach.getAttachmentId());
    }
    payTranHeaderMapper.delete(tranNum);
  }

  private String getCurrentUser() {
    ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
    return user != null ? user.id + "" : "-";
  }

  private String getCurrentUserName() {
    ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
    return user != null ? user.getName() + "" : "-";
  }

  public List<CustPaymentInfo> findCustPaymentInfos(Map<String, Object> parms) {
    List<CustPaymentInfo> custPaymentInfos = payTranHeaderMapper.getCustPaymentInfos(parms);
    resetCustPaymentInfos(custPaymentInfos);
    return custPaymentInfos;
  }

  public Page<CustPaymentInfo> findCustPaymentInfos(Page<CustPaymentInfo> page) {
    int count = payTranHeaderMapper.countCustPaymentInfos(page.getSearchMap());
    List<CustPaymentInfo> custPaymentInfos = payTranHeaderMapper.getCustPaymentInfos(page.getSearchMap(), page.getRowBounds());
    resetCustPaymentInfos(custPaymentInfos);
    page.setContent(custPaymentInfos);
    page.setTotal(count);
    return page;
  }

  /**
   * 不同paycode赋值
   * 
   * @param custPaymentInfo
   */
  private void resetCustPaymentInfos(List<CustPaymentInfo> custPaymentInfos) {
    if (custPaymentInfos != null && custPaymentInfos.size() > 0) {
      for (CustPaymentInfo custPaymentInfo : custPaymentInfos) {
        // 开户费
        if ("3".equalsIgnoreCase(custPaymentInfo.getPayCode())) {
          custPaymentInfo.setAcctCreateFeeAmount(custPaymentInfo.getAmount());
        }
        // 年费
        if ("2".equalsIgnoreCase(custPaymentInfo.getPayCode())) {
          custPaymentInfo.setAnnualFeeAmount(custPaymentInfo.getAmount());
        }
        // 保证金
        if ("4".equalsIgnoreCase(custPaymentInfo.getPayCode())) {
          custPaymentInfo.setGuaranteeFeeAmount(custPaymentInfo.getAmount());
        }
        // 管理
        if ("1".equalsIgnoreCase(custPaymentInfo.getPayCode())) {
          //custPaymentInfo.setMgtFeeAmount(custPaymentInfo.getTopupAmount() * custPaymentInfo.getMgtFee());
          //管理费金额=折合RMB/(1+管理费%)*管理费%
          custPaymentInfo.setMgtFeeAmount(custPaymentInfo.getAmountInRmb() / (1 + custPaymentInfo.getMgtFee()) * custPaymentInfo.getMgtFee());
        }
      }
    }

  }

  @Transactional(rollbackFor = Exception.class)
  public Mail pass(Long tranNum, Character status) {
    Character nextStatus = null;
    switch (status) {
      case '1':
        nextStatus = CommonUtil.STATUS_CHECK;
        break;
      case '3':
        nextStatus = CommonUtil.STATUS_FINANCE_CONFIRM;
        break;
      case '4':
        nextStatus = CommonUtil.STATUS_OPS_CONFIRM;
        break;
    }
    Mail mail = null;
    if (nextStatus != null) {
      PayTranHeader payTranHeader = payTranHeaderMapper.get(tranNum);
      payTranHeader.setStatus(nextStatus);
      payTranHeader.setUpdateDate(new Date());
      payTranHeader.setUpdateBy(getCurrentUser());
      payTranHeaderMapper.update(payTranHeader);
      // 新增一条process记录
      Process process = new Process();
      process.setPayTranNum(payTranHeader.getTranNum());
      process.setStatus(nextStatus);
      process.setCreateDate(new Date());
      process.setOperateDate(new Date());
      process.setOperateBy(getCurrentUser());
      process.setOperateByName(getCurrentUserName());
      process.setIsUpdated('1');
      processService.save(process);
      // 发送当前步骤的邮件
      if (status == CommonUtil.STATUS_NEW) {
        // 当前是new->check
        mail = processService.check(null, tranNum);
      } else if (status == CommonUtil.STATUS_CHECK) {
        mail = processService.financeConfirm(null, tranNum);
      } else if (status == CommonUtil.STATUS_FINANCE_CONFIRM) {
        mail = processService.opsConfirm(null, tranNum);
      }
    }
    return mail;
  }

  @Transactional(rollbackFor = Exception.class)
  public Mail reject(Long tranNum, String description) {
    PayTranHeader payTranHeaderDB = payTranHeaderMapper.get(tranNum);
    Character targetStatus = CommonUtil.STATUS_NEW;
    // 如果当前是new->2 如果是check->7
    if (payTranHeaderDB.getStatus() == CommonUtil.STATUS_NEW) {
      targetStatus = CommonUtil.STATUS_REJECT;
    } else {
      targetStatus = CommonUtil.STATUS_FINANCE_REJECT;
    }
    payTranHeaderDB.setDescription(description);
    payTranHeaderDB.setStatus(targetStatus);
    payTranHeaderDB.setUpdateDate(new Date());
    payTranHeaderDB.setUpdateBy(getCurrentUser());
    payTranHeaderMapper.update(payTranHeaderDB);
    return processService.reject(null, tranNum, description, targetStatus);
  }

  //是否账期客户且加款金额大于10k
  public  PayTranHeader isAmSupport(Long  tranNum){
    PayTranHeader payTranHeader = get(tranNum);
    Map<String,Object> params = new HashedMap();
    params.put("tranNum",tranNum);
    params.put("payCode",CommonUtil.ADD_PAY_CODE);
    List<PayTranDetail> payTranDetails =  payTranDetailMapper.getPayTranDetailByPayCode(params);
    Double totalAdditionAmount = totalAdditionAmount(tranNum);
    CustMaster custMaster = new CustMaster();
    if(payTranDetails !=null && payTranDetails.size() > 0){
      custMaster = custMasterService.findByCustUsername(payTranDetails.get(0).getBdUserName());
    }
    payTranHeader.setAmSupport(totalAdditionAmount > CommonUtil.ADDITION_AMOUNT && custMaster.is_credit_client());
    return  payTranHeader;
  }


  //交易总加款金额
 /**
  * @Description
  * @Author wenjie.sun
  * @date 2017/2/20 下午3:06
  */
  public Double totalAdditionAmount(Long  tranNum){
    return payTranHeaderMapper.sumTotalAddtionAmount(tranNum);
  }

}
