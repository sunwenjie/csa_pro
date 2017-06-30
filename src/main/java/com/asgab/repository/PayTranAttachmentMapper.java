package com.asgab.repository;

import java.util.List;

import com.asgab.entity.PayTranAttachement;
import com.asgab.repository.mybatis.MyBatisRepository;

@MyBatisRepository
public interface PayTranAttachmentMapper {

  /**
   * 根据paytrannum获取
   * @param tranNum
   * @return
   */
  List<PayTranAttachement> get(Long tranNum);
  
  /**
   * 根据processId获取
   * @param processId
   * @return
   */
  List<PayTranAttachement> getByProcessId(Long processId);
  
  
  /**
   * 根据主键获取
   * @param attachmentId
   * @return
   */
  PayTranAttachement getById(Long attachmentId);

  void save(PayTranAttachement payTranAttachement);

  void delete(Long attachmentId);

  void update(PayTranAttachement payTranAttachement);

}
