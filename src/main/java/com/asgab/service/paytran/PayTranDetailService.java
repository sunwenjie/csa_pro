package com.asgab.service.paytran;

import com.asgab.entity.PayTranDetail;
import com.asgab.repository.PayTranDetailMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Component
@Transactional
public class PayTranDetailService {

  @Resource
  private PayTranDetailMapper payTranDetailMapper;

  public List<PayTranDetail> get(Long tranNum) {
    return payTranDetailMapper.get(tranNum);
  }
  public void save(PayTranDetail payTranDetail) {
    payTranDetailMapper.save(payTranDetail);
  }

  public void update(PayTranDetail payTranDetail) {
    payTranDetailMapper.update(payTranDetail);
  }

  public void delete(Long tranNumDetail) {
    payTranDetailMapper.delete(tranNumDetail);
  }

}
