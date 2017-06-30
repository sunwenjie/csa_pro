package com.asgab.service.account;

import com.asgab.core.pagination.Page;
import com.asgab.entity.Group;
import com.asgab.entity.Log;
import com.asgab.entity.User;
import com.asgab.repository.GroupMapper;
import com.asgab.repository.xmo.UserXMOMapper;
import com.asgab.service.LogService;
import com.asgab.service.account.ShiroDbRealm.ShiroUser;
import com.asgab.util.CommonUtil;
import com.asgab.util.JsonMapper;
import com.asgab.util.Operate;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户管理类.
 * 
 * @author calvin
 */
// Spring Service Bean的标识.
@Component
@Transactional
public class GroupService {

  @Resource
  private GroupMapper groupMapper;

  @Autowired
  private LogService logService;

  @Resource
  private UserXMOMapper userXMOMapper;

  public List<Group> search(Map<String, Object> parameters) {
    return groupMapper.search(parameters);
  }

  public List<Group> search(Map<String, Object> parameters, RowBounds rowBounds) {
    return groupMapper.search(parameters, rowBounds);
  }

  public List<Group> getAllGroup() {
    return (List<Group>) groupMapper.search(null);
  }

  public Page<Group> getAllGroup(Page<Group> page) {
    List<Group> list = groupMapper.search(page.getSearchMap(), page.getRowBounds());
    int count = groupMapper.count(page.getSearchMap());
    page.setContent(list);
    page.setTotal(count);
    return page;
  }

  public Group getGroup(Long id) {
    return groupMapper.get(id);
  }

  public void update(Group group) {
    Group originGroup = getGroup(Long.parseLong(group.getId()));
    groupMapper.update(group);
    saveGroupLog(originGroup, group);
  }

  public void save(Group group) {
    groupMapper.save(group);
    saveGroupLog(null, group);
  }

  public void delete(Long id) {
    Group group = groupMapper.get(id);
    groupMapper.delete(id);
    saveGroupLog(group, null);
  }

  public List<Group> findGroupsByGroupIds(List<Long> ids) {
    if (ids.size() == 0) {
      ids.add(-1l);
    }
    return groupMapper.findGroupsByGroupIds(ids);
  }

  public List<Group> findGroupByGroupName(String groupName) {
    return groupMapper.findGroupByGroupName(groupName);
  }

  public List<String> findGroupIdsByGroupType(String group_type) {
    return groupMapper.findGroupIdsByGroupType(group_type);
  }

  /**
   * @Description
   * @Author wenjie.sun
   * @date 2017/2/22 下午2:53
   */

  public List<Group> findGroupByUserId(Long userId){ return groupMapper.findGroupByUserId(userId); }

/**
 * @Description
 * @Author wenjie.sun
 * @date 2017/2/22 下午3:08
 */

  public List<String> getGroupTypeByUserId(Long userId){
    List<String> groupTypes = new ArrayList<String>();
    List<Group> groups = findGroupByUserId(userId);
    for (Group group : groups) {
      groupTypes.add(group.getGroup_type());
    }
    CommonUtil.distinctList(groupTypes);
    return groupTypes;
  }

  /**
   * 保存用户组日志
   * 
   * @param originGroupIds
   * @param newGroupIds
   * @param userId
   */
  public void saveUserGroupLog(List<Long> originGroupIds, List<Long> newGroupIds, String userId) {
    StringBuffer sb = new StringBuffer();
    List<Group> originGroups = findGroupsByGroupIds(originGroupIds);
    List<Group> newGroups = findGroupsByGroupIds(newGroupIds);
    User user = userXMOMapper.get(Long.parseLong(userId));
    sb.append("用户:" + user.getName() + "<br/>");
    sb.append("用户原所在组为[");
    for (int i = 0; i < originGroups.size(); i++) {
      if (i != 0) {
        sb.append(", ");
      }
      sb.append(originGroups.get(i).getGroup_name() + "(" + originGroups.get(i).getId() + ")");
    }
    sb.append("]<br/>");
    sb.append("用户现所在组为[");
    for (int i = 0; i < newGroups.size(); i++) {
      if (i != 0) {
        sb.append(", ");
      }
      sb.append(newGroups.get(i).getGroup_name() + "(" + newGroups.get(i).getId() + ")");
    }
    sb.append("]");

    Log log = new Log();
    log.setOperateType(Operate.MODIFY.getIndex());
    log.setOperateBy(getShiroUser() == null ? "" : getShiroUser().getName());
    log.setOperateUserId(getShiroUser() != null ? String.valueOf(getShiroUser().id) : null);
    log.setOperateTime(new Date());
    log.setpKey(userId);
    log.setRemark1(sb.toString());
    log.setRemark2(sb.toString());
    log.setModule(CommonUtil.LOG_MODULE_USER_GROUP);
    logService.addLog(log);
  }

  /**
   * 保存组日志
   */
  public void saveGroupLog(Group originGroup, Group newGroup) {
    StringBuffer sb = new StringBuffer();
    sb.append("原用户组信息:");
    sb.append(originGroup != null ? JsonMapper.nonDefaultMapper().toJson(originGroup) : "");
    sb.append("<br/>");
    sb.append("现用户组信息:");
    sb.append(newGroup != null ? JsonMapper.nonDefaultMapper().toJson(newGroup) : "");

    Log log = new Log();
    log.setOperateType(Operate.MODIFY.getIndex());
    log.setOperateBy(getShiroUser() == null ? "" : getShiroUser().getName());
    log.setOperateUserId(getShiroUser() != null ? String.valueOf(getShiroUser().id) : null);
    log.setOperateTime(new Date());
    log.setpKey(originGroup != null ? originGroup.getId() : "");
    log.setRemark1(sb.toString());
    log.setRemark2(sb.toString());
    log.setModule(CommonUtil.LOG_MODULE_GROUP);
    logService.addLog(log);
  }

  public ShiroUser getShiroUser() {
    ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
    return user;
  }
}
