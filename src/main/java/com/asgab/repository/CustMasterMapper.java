package com.asgab.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.asgab.entity.CustBaseInfo;
import com.asgab.entity.CustMaster;
import com.asgab.repository.mybatis.MyBatisRepository;

/**
 * 通过@MapperScannerConfigurer扫描目录中的所有接口, 动态在Spring Context中生成实现. 方法名称必须与Mapper.xml中保持一致.
 * 
 * @author calvin
 */
@MyBatisRepository
public interface CustMasterMapper {

  CustMaster get(Long id);

  List<CustMaster> search(Map<String, Object> parameters);

  List<CustMaster> search(Map<String, Object> parameters, RowBounds rowBounds);

  void save(CustMaster custMaster);

  void update(CustMaster custMaster);

  void delete(Long id);

  int count(Map<String, Object> map);

  int existByCustUsername(String custUsername);

  CustMaster findByCustUsername(String custUsername);
  
  CustMaster findByCustUsernameIncludeDeleted(String custUsername);

  int countCustBaseInfos(Map<String, Object> map);

  List<CustBaseInfo> findCustBaseInfos(Map<String, Object> parameters);

  List<CustBaseInfo> findCustBaseInfos(Map<String, Object> parameters, RowBounds rowBounds);
}
