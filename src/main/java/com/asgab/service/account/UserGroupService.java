package com.asgab.service.account;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.asgab.entity.Group;
import com.asgab.entity.Log;
import com.asgab.entity.User;
import com.asgab.entity.UserGroup;
import com.asgab.repository.GroupMapper;
import com.asgab.repository.UserGroupMapper;
import com.asgab.repository.xmo.UserXMOMapper;
import com.asgab.service.LogService;
import com.asgab.service.account.ShiroDbRealm.ShiroUser;
import com.asgab.util.CommonUtil;
import com.asgab.util.Operate;

@Component
@Transactional
public class UserGroupService {
  @Resource
  UserGroupMapper userGroupMapper;

  @Resource
  GroupMapper groupMapper;

  @Autowired
  LogService logService;

  @Resource
  UserXMOMapper userXMOMapper;

  public UserGroup get(Long id) {
    return userGroupMapper.get(id);
  }

  public List<UserGroup> search(Map<String, Object> parameters) {
    return userGroupMapper.search(parameters);
  }

  public List<UserGroup> search(Map<String, Object> parameters, RowBounds rowBounds) {
    return userGroupMapper.search(parameters, rowBounds);
  }

  public void save(UserGroup userGroup) {
    Map<String, Object> condMap = new HashMap<String, Object>();
    condMap.put("group_id", userGroup.getGroup_id());
    List<UserGroup> originUserGroups = userGroupMapper.search(condMap);
    // 删除原关系
    for (UserGroup tmpUserGroup : originUserGroups) {
      if (userGroup.getUser_id().longValue() == tmpUserGroup.getUser_id().longValue()) {
        userGroupMapper.delete(tmpUserGroup.getId());
      }
    }
    // 保存用户组数据
    userGroupMapper.save(userGroup);
    // 重新查询
    List<UserGroup> newUserGroups = userGroupMapper.search(condMap);
    // 保存日志
    saveLog(userGroup, originUserGroups, newUserGroups, true);
  }

  public void delete(Long id) {
    // 获取组
    UserGroup userGroup = get(id);
    // 获取组内用户
    Map<String, Object> condMap = new HashMap<String, Object>();
    condMap.put("group_id", userGroup.getGroup_id());
    List<UserGroup> originUserGroups = userGroupMapper.search(condMap);
    // 删除用户组数据
    userGroupMapper.delete(id);
    // 重新查询
    List<UserGroup> newUserGroups = userGroupMapper.search(condMap);
    // 保存日志
    saveLog(userGroup, originUserGroups, newUserGroups, false);
  }

  private void saveLog(UserGroup addedUserGroup, List<UserGroup> originUserGroups, List<UserGroup> newUserGroups, boolean isAdd) {
    StringBuffer sb = new StringBuffer();
    List<Long> originUserIds = new ArrayList<Long>();
    List<Long> newUserIds = new ArrayList<Long>();

    User updatedUser = userXMOMapper.get(addedUserGroup.getUser_id());
    Group updatedGroup = groupMapper.get(addedUserGroup.getGroup_id());

    for (int i = 0; i < originUserGroups.size(); i++) {
      originUserIds.add(originUserGroups.get(i).getUser_id());
    }

    for (int i = 0; i < newUserGroups.size(); i++) {
      newUserIds.add(newUserGroups.get(i).getUser_id());
    }

    sb.append("原组内用户ID列表:");
    sb.append(originUserIds.toString());
    sb.append("<br/>");
    sb.append("现组内用户ID列表:");
    sb.append(newUserIds.toString());
    sb.append("<br/>");
    sb.append("[" + updatedGroup.getGroup_name() + (isAdd ? "]新增用户:" : "]删除用户") + updatedUser.getName() + "(" + updatedUser.getId() + ")");

    Log log = new Log();
    log.setOperateType(Operate.MODIFY.getIndex());
    log.setOperateBy(getShiroUser() == null ? "" : getShiroUser().getName());
    log.setOperateUserId(getShiroUser() != null ? String.valueOf(getShiroUser().id) : null);
    log.setOperateTime(new Date());
    log.setpKey(addedUserGroup.getGroup_id() + "");
    log.setRemark1(sb.toString());
    log.setRemark2(sb.toString());
    log.setModule(CommonUtil.LOG_MODULE_GROUP_USER);
    logService.addLog(log);

  }

  public ShiroUser getShiroUser() {
    ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
    return user;
  }

  public int count(Map<String, Object> map) {
    return userGroupMapper.count(map);
  }
}
