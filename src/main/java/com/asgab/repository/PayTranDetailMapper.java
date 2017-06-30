package com.asgab.repository;

import com.asgab.entity.PayTranDetail;
import com.asgab.repository.mybatis.MyBatisRepository;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface PayTranDetailMapper {

  List<PayTranDetail> get(Long tranNum);
  List<PayTranDetail> getPayTranDetailByPayCode(Map<String,Object> params);

  void save(PayTranDetail payTranDetail);

  void update(PayTranDetail payTranDetail);

  void delete(Long tranNumDetail);

  int count(Map<String, Object> map);

  int countByPayCode(String payCode);

}
