package com.asgab.web.account;

import com.asgab.core.pagination.Page;
import com.asgab.entity.Group;
import com.asgab.entity.User;
import com.asgab.entity.UserGroup;
import com.asgab.repository.xmo.UserXMOMapper;
import com.asgab.service.account.GroupService;
import com.asgab.service.account.ShiroDbRealm.ShiroUser;
import com.asgab.service.account.UserGroupService;
import com.asgab.util.CommonUtil;
import com.asgab.util.Servlets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping(value = "/group")
public class GroupController {

  @Autowired
  private GroupService groupService;

  @Resource
  private UserXMOMapper userXMOMapper;

  @Autowired
  private UserGroupService userGroupService;

  private static final String PAGE_SIZE = "10";

  @RequestMapping(method = RequestMethod.GET)
  public String list(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
      @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize, @RequestParam(value = "sort", defaultValue = "") String sort,
      HttpServletRequest request, Model model) {

    Map<String, Object> params = new HashMap<>();
    if (StringUtils.isNotBlank(request.getParameter("group_name"))) {
      params.put("group_name", request.getParameter("group_name"));
    }
    if (StringUtils.isNotBlank(request.getParameter("status"))) {
      params.put("status", request.getParameter("status"));
    }
    ShiroUser currentUser = groupService.getShiroUser();
    List<String> userGroupTypes = groupService.getGroupTypeByUserId(currentUser.id);

    if (currentUser != null && userGroupTypes.size() == 0){
      params.put("user_id",CommonUtil.NO_USER);
    }

    if (currentUser != null && userGroupTypes.size()>0 && !userGroupTypes.contains(CommonUtil.GROUP_ADMIN)){
      params.put("user_id",currentUser.id);
    }

    // 将搜索条件编码成字符串，用于排序，分页的URL
    model.addAttribute("search", Servlets.encodeParameterString(params));
    params.put("sort", sort);
    Page<Group> page = new Page<>(pageNumber, pageSize, sort, params);
    model.addAttribute("pages", groupService.getAllGroup(page));
    model.addAttribute("statuses",
        "ZH".equalsIgnoreCase(request.getLocale().getLanguage()) ? CommonUtil.GROUP_STATUS_ZH : CommonUtil.GROUP_STATUS_EN);
    return "account/groupList";
  }

  // id = groupid
  // 查找组里的人
  @RequestMapping(value = "find/group/{id}", method = RequestMethod.POST)
  public String findUserByGroup(@PathVariable("id") long id, Model model) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("group_id", id);
    List<UserGroup> userGroups = userGroupService.search(map);
    List<Long> userIds = new ArrayList<Long>();
    for (int i = 0; i < userGroups.size(); i++) {
      userIds.add(userGroups.get(i).getUser_id());
    }
    List<User> users = null;
    if (userIds.size() == 0) {
      users = new ArrayList<User>();
    } else {
      users = userXMOMapper.findUsersByUserIds(userIds);
    }
    model.addAttribute("users", users);
    model.addAttribute("groupId", id);
    return "account/modalGroup";
  }

  /**
   * 添加用户
   * 
   * @param groupId
   * @param userId
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "add/{groupId}/{userId}", method = RequestMethod.POST)
  public String add(@PathVariable("groupId") long groupId, @PathVariable("userId") long userId) {
    UserGroup userGroup = new UserGroup();
    userGroup.setGroup_id(groupId);
    userGroup.setUser_id(userId);
    userGroup.setCreated_at(new Date());
    userGroupService.save(userGroup);
    return "success";
  }

  /**
   * 删除用户
   * 
   * @param groupId
   * @param userId
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "del/{groupId}/{userId}", method = RequestMethod.POST)
  public String delUser(@PathVariable("groupId") long groupId, @PathVariable("userId") long userId) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("group_id", groupId);
    map.put("user_id", userId);
    List<UserGroup> userGroups = userGroupService.search(map);
    if (userGroups != null && userGroups.size() > 0) {
      for (UserGroup userGroup : userGroups) {
        userGroupService.delete(userGroup.getId());
      }
    }
    return "success";
  }

  @RequestMapping(value = "del/{groupId}", method = RequestMethod.GET)
  public String delGroup(@PathVariable("groupId") long groupId, HttpServletRequest request, RedirectAttributes redirectAttributes) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("group_id", groupId);
    List<UserGroup> userGroups = userGroupService.search(map);
    if (userGroups != null && userGroups.size() > 0) {
      redirectAttributes.addFlashAttribute("errorMessage", CommonUtil.getProperty(request, "group.delete.error"));
    } else {
      groupService.delete(groupId);
      redirectAttributes.addFlashAttribute("message", CommonUtil.getProperty(request, "group.delete.success"));
    }
    return "redirect:/group";
  }

  // ************************ for modal user****************

  // 查找人所有的组
  @RequestMapping(value = "find/user/{userId}", method = RequestMethod.POST)
  public String findGroupByUser(@PathVariable("userId") long userId, Model model) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("user_id", userId);
    List<UserGroup> userGroups = userGroupService.search(map);
    Map<String, Object> groupCond = new HashMap<String, Object>();
    groupCond.put("status", "Active");
    List<Group> groups = groupService.search(groupCond);
    if (userGroups != null && userGroups.size() > 0) {
      for (Group group : groups) {
        for (UserGroup userGroup : userGroups) {
          if (group.getId().equals(userGroup.getGroup_id() + "")) {
            group.setChecked("checked");
            break;
          }
        }
      }
    }
    model.addAttribute("groups", groups);
    model.addAttribute("userId", userId);
    return "account/modalUser";
  }

  @RequestMapping(value = "update/user", method = RequestMethod.POST)
  public String updateUserGroups(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    // 原始组id
    List<Long> originGroupIds = new ArrayList<Long>();
    // 新组id
    List<Long> newGroupIds = new ArrayList<Long>();
    String[] userGroupStrs = request.getParameterValues("userGroups");
    String userId = request.getParameter("userId");
    String searchUrl = request.getParameter("searchUrl");
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("user_id", userId);
    List<UserGroup> userGroups = userGroupService.search(map);
    for (int i = 0; i < userGroups.size(); i++) {
      originGroupIds.add(userGroups.get(i).getGroup_id());
      userGroupService.delete(userGroups.get(i).getId());
    }
    if (userGroupStrs != null && userGroupStrs.length > 0) {
      for (String userGroupStr : userGroupStrs) {
        UserGroup tmpUserGroup = new UserGroup();
        tmpUserGroup.setUser_id(Long.parseLong(userId));
        tmpUserGroup.setGroup_id(Long.parseLong(userGroupStr));
        tmpUserGroup.setCreated_at(new Date());
        userGroupService.save(tmpUserGroup);
        newGroupIds.add(tmpUserGroup.getGroup_id());
      }
    }
    groupService.saveUserGroupLog(originGroupIds, newGroupIds, userId);
    int index = searchUrl.indexOf("?");
    redirectAttributes.addFlashAttribute("message", CommonUtil.getProperty(request, "public.update.success"));
    return "redirect:/user" + (index >= 0 ? searchUrl.substring(index, searchUrl.length()) : "");
  }

  // ************************ for modal user end ****************

  @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
  public String toUpdate(@PathVariable("id") Long id, Model model, HttpServletRequest request) {
    Group group = groupService.getGroup(id);
    boolean isZH = "ZH".equalsIgnoreCase(request.getLocale().getLanguage());
    group.getGroupTypes().put("", isZH ? "请选择" : "Please Select");
    resetGroup(group, request);
    model.addAttribute("group", group);
    model.addAttribute("action", "update");
    return "account/groupForm";
  }

  @RequestMapping(value = "update", method = RequestMethod.POST)
  public String update(@ModelAttribute("group") Group group, RedirectAttributes redirectAttributes, HttpServletRequest request) {
    groupService.update(group);
    redirectAttributes.addFlashAttribute("message", CommonUtil.getProperty(request, "group.update.success"));
    return "redirect:/group";
  }

  @RequestMapping(value = "create", method = RequestMethod.GET)
  public String toCreate(HttpServletRequest request, Model model) {
    Group group = new Group();
    boolean isZH = "ZH".equalsIgnoreCase(request.getLocale().getLanguage());
    group.getGroupTypes().put("", isZH ? "请选择" : "Please Select");
    resetGroup(group, request);
    model.addAttribute("group", group);
    model.addAttribute("action", "create");
    return "account/groupForm";
  }

  @RequestMapping(value = "create", method = RequestMethod.POST)
  public String create(Group group, RedirectAttributes redirectAttributes, HttpServletRequest request) {
    groupService.save(group);
    redirectAttributes.addFlashAttribute("message", CommonUtil.getProperty(request, "group.save.success"));
    return "redirect:/group";
  }

  @ResponseBody
  @RequestMapping(value = "checkGroupName", method = RequestMethod.POST)
  public String checkGroupName(HttpServletRequest request) {
    String group_name = request.getParameter("group_name");
    String id = request.getParameter("id");
    List<Group> groups = groupService.findGroupByGroupName(group_name);
    boolean findFlag = false;
    if (groups != null && groups.size() > 0) {
      for (Group group : groups) {
        if (!group.getId().equalsIgnoreCase(id)) {
          findFlag = true;
        }
      }
    }
    return findFlag ? "true" : "false";
  }

  @ModelAttribute
  public void getGroup(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
    if (id != -1) {
      model.addAttribute("group", groupService.getGroup(id));
    }
  }

  public void resetGroup(Group group, HttpServletRequest request) {
    boolean isZH = "ZH".equalsIgnoreCase(request.getLocale().getLanguage());
    if (isZH) {
      group.getStatuses().putAll(CommonUtil.GROUP_STATUS_ZH);
      group.getGroupTypes().putAll(CommonUtil.GROUP_TYPE_ZH);
    } else {
      group.getStatuses().putAll(CommonUtil.GROUP_STATUS_EN);
      group.getGroupTypes().putAll(CommonUtil.GROUP_TYPE_EN);
    }
  }
}
