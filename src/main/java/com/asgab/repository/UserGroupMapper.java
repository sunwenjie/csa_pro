package com.asgab.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.asgab.entity.UserGroup;
import com.asgab.repository.mybatis.MyBatisRepository;

/**
 * 通过@MapperScannerConfigurer扫描目录中的所有接口, 动态在Spring Context中生成实现. 方法名称必须与Mapper.xml中保持一致.
 * 
 * @author calvin
 */
@MyBatisRepository
public interface UserGroupMapper {

  UserGroup get(Long id);

  List<UserGroup> search(Map<String, Object> parameters);

  List<UserGroup> search(Map<String, Object> parameters, RowBounds rowBounds);

  void save(UserGroup userGroup);

  void delete(Long id);

  int count(Map<String, Object> map);

}
