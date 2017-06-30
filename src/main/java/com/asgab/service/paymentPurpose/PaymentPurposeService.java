package com.asgab.service.paymentPurpose;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.asgab.core.pagination.Page;
import com.asgab.entity.PaymentPurpose;
import com.asgab.repository.PayTranDetailMapper;
import com.asgab.repository.PaymentPurposeMapper;

/**
 * 支付方式管理类.
 * 
 * @author siuvan
 */
// Spring Service Bean的标识.
@Component
@Transactional
public class PaymentPurposeService {

  @Resource
  private PaymentPurposeMapper paymentPurposeMapper;

  @Resource
  private PayTranDetailMapper payTranDetailMapper;

  public List<PaymentPurpose> getAllPaymentPurpose() {
    return (List<PaymentPurpose>) paymentPurposeMapper.search(null);
  }

  public Page<PaymentPurpose> getAllPaymentPurpose(Page<PaymentPurpose> page) {
    List<PaymentPurpose> list = paymentPurposeMapper.search(page.getSearchMap(), page.getRowBounds());
    int count = paymentPurposeMapper.count(page.getSearchMap());
    page.setContent(list);
    page.setTotal(count);
    return page;
  }

  public PaymentPurpose getPaymentPurpose(Long id) {
    return paymentPurposeMapper.get(id);
  }

  public void addPaymentPurpose(PaymentPurpose paymentPurpose) {
    paymentPurposeMapper.save(paymentPurpose);
  }

  public void updatePaymentPurpose(PaymentPurpose paymentPurpose) {
    paymentPurposeMapper.update(paymentPurpose);
  }

  public void deletePaymentPurpose(Long id) {
    paymentPurposeMapper.delete(id);
  }

  public boolean canDelete(String payCode) {
    return payTranDetailMapper.countByPayCode(payCode) > 0 ? false : true;
  }

}
