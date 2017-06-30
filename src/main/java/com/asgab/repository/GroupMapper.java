package com.asgab.repository;


import com.asgab.entity.Group;
import com.asgab.repository.mybatis.MyBatisRepository;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * 通过@MapperScannerConfigurer扫描目录中的所有接口, 动态在Spring Context中生成实现. 方法名称必须与Mapper.xml中保持一致.
 * 
 * @author calvin
 */
@MyBatisRepository
public interface GroupMapper {

  Group get(Long id);

  List<Group> search(Map<String, Object> parameters);

  List<Group> search(Map<String, Object> parameters, RowBounds rowBounds);

  int count(Map<String, Object> map);

  List<Group> findGroupsByGroupIds(List<Long> ids);

  List<Group> findGroupByGroupName(String group_name);

  List<String> findGroupIdsByGroupType(String group_type);

  List<Group> findGroupByUserId(Long user_id);

  void save(Group group);

  void update(Group group);

  void delete(Long id);

}
