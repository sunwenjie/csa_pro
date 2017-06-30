package com.asgab.service.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.asgab.entity.Group;
import com.asgab.entity.User;
import com.asgab.entity.UserGroup;
import com.asgab.repository.GroupMapper;
import com.asgab.repository.UserGroupMapper;
import com.asgab.repository.xmo.UserXMOMapper;

@Component
@Transactional
public class UserXMOService {

  @Autowired
  private UserXMOMapper userXMOMapper;

  @Autowired
  private GroupMapper groupMapper;

  @Autowired
  private UserGroupMapper userGroupMapper;

  public List<Group> findUserGroupByLoginName(String loginName) {
    List<User> users = userXMOMapper.findUserByLoginName(loginName);
    List<Group> groups = new ArrayList<Group>();
    if (users != null && users.size() > 0) {
      User user = users.get(0);
      Map<String, Object> userGroupMap = new HashMap<String, Object>();
      userGroupMap.put("user_id", user.getId());
      List<UserGroup> userGroups = userGroupMapper.search(userGroupMap);
      List<Long> groupIds = new ArrayList<Long>();
      for (int i = 0; i < userGroups.size(); i++) {
        groupIds.add(userGroups.get(i).getGroup_id());
      }
      if (groupIds.size() > 0) {
        groups.addAll(groupMapper.findGroupsByGroupIds(groupIds));
      }
    }
    return groups;
  }

  public List<User> findUsersByGroupName(String group_name) {
    List<Group> groups = groupMapper.findGroupByGroupName(group_name);
    List<User> users = new ArrayList<User>();
    if (groups != null && groups.size() > 0) {
      Group group = groups.get(0);
      Map<String, Object> userGroupMap = new HashMap<String, Object>();
      userGroupMap.put("group_id", group.getId());
      List<UserGroup> userGroups = userGroupMapper.search(userGroupMap);
      if (userGroups != null && userGroups.size() > 0) {
        List<Long> userIds = new ArrayList<Long>();
        for (int i = 0; i < userGroups.size(); i++) {
          userIds.add(userGroups.get(i).getUser_id());
        }
        users.addAll(userXMOMapper.findUsersByUserIds(userIds));
      }
    }
    return users;
  }

  public List<Group> findGroupByGroupName(String groupName) {
    return groupMapper.findGroupByGroupName(groupName);
  }

  public List<User> findUsersByGroupId(Long id) {
    List<User> users = new ArrayList<User>();
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("group_id", id);
    List<UserGroup> userGroups = userGroupMapper.search(map);
    if (userGroups != null && userGroups.size() > 0) {
      List<Long> userIds = new ArrayList<Long>();
      for (int i = 0; i < userGroups.size(); i++) {
        userIds.add(userGroups.get(i).getUser_id());
      }
      users.addAll(userXMOMapper.findUsersByUserIds(userIds));
    }
    return users;
  }
}
