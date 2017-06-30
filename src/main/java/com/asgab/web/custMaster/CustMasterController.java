package com.asgab.web.custMaster;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.asgab.core.pagination.Page;
import com.asgab.entity.CustMaster;
import com.asgab.entity.Group;
import com.asgab.entity.Log;
import com.asgab.service.LogService;
import com.asgab.service.account.GroupService;
import com.asgab.service.account.ShiroDbRealm.ShiroUser;
import com.asgab.service.custMaster.CustMasterService;
import com.asgab.util.CommonUtil;
import com.asgab.util.JsonMapper;
import com.asgab.util.MapCompareUtil;
import com.asgab.util.Operate;
import com.asgab.util.Servlets;
import com.asgab.web.ReportsController;

/***
 * user管理的Controller, 使用Restful风格的Urls:
 * 
 * List page : GET /user/ Create page : GET /user/create Create action : POST /user/create Update
 * page : GET /user/update/{id} Update action : POST /user/update Delete action : GET
 * /user/delete/{id}
 */
@Controller
@RequestMapping(value = "/custMaster")
public class CustMasterController {

  // Json工具类
  private final static JsonMapper jsonMapper = new JsonMapper();

  // JSON比较key
  private final static String COMPARE_KEY = "log.customer.keys";

  private static final String PAGE_SIZE = "10";

  @Resource
  CustMasterService custMasterService;

  @Autowired
  GroupService groupService;

  @Resource
  LogService logService;

  @RequestMapping(method = RequestMethod.GET)
  public String list(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
      @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize, @RequestParam(value = "sort", defaultValue = "") String sort,
      ServletRequest request, Model model) {

    Map<String, Object> params = new HashMap<>();
    if (StringUtils.isNotBlank(request.getParameter("custName"))) {
      params.put("custName", request.getParameter("custName"));
    }
    if (StringUtils.isNotBlank(request.getParameter("custUsername"))) {
      params.put("custUsername", request.getParameter("custUsername"));
    }
    if (StringUtils.isNotBlank(request.getParameter("custPort"))) {
      params.put("custPort", request.getParameter("custPort"));
    }

    // 将搜索条件编码成字符串，用于排序，分页的URL
    model.addAttribute("search", Servlets.encodeParameterString(params));
    params.put("sort", sort);
    Page<CustMaster> page = new Page<>(pageNumber, pageSize, sort, params);
    model.addAttribute("pages", custMasterService.getAllCustMaster(page));
    return "custMaster/custMastersList";
  }

  @RequestMapping(value = "create", method = RequestMethod.GET)
  public String toCreate(Model model) {
    model.addAttribute("custMaster", new CustMaster());
    model.addAttribute("action", "create");
    return "custMaster/custMasterForm";
  }

  @RequestMapping(value = "create", method = RequestMethod.POST)
  public String create(CustMaster custMaster, RedirectAttributes redirectAttributes, HttpServletRequest request) {
    custMasterService.addCustMaster(custMaster);
    redirectAttributes.addFlashAttribute("message", CommonUtil.getProperty(request, "msg.add.success"));
    return "redirect:/custMaster/update/" + custMaster.getId();
  }

  @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
  public String toUpdate(@PathVariable("id") Long id, Model model, HttpServletRequest request) {
    model.addAttribute("custMaster", custMasterService.getCustMaster(id));
    model.addAttribute("action", "update");
    setMails(model, request);
    return "custMaster/custMasterForm";
  }

  @RequestMapping(value = "update", method = RequestMethod.POST)
  public String update(@ModelAttribute("custMaster") CustMaster custMaster, RedirectAttributes redirectAttributes, HttpServletRequest request) {
    CustMaster custMasterBefore = custMasterService.getCustMaster(custMaster.getId());
    custMasterService.updateCustMaster(custMaster);
    // insertLog(custMaster, Operate.MODIFY.getIndex(), custMaster.getId(),
    // custMasterBefore); TODO
    redirectAttributes.addFlashAttribute("message", CommonUtil.getProperty(request, "msg.update.success"));
    return "redirect:/custMaster/update/" + custMaster.getId();
  }

  @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
  public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes, HttpServletRequest request) {
    custMasterService.deleteCustMaster(id);
    redirectAttributes.addFlashAttribute("message", CommonUtil.getProperty(request, "msg.delete.success"));
    return "redirect:/custMaster";
  }

  @RequestMapping(value = "checkCustUsername")
  @ResponseBody
  public String checkCustUsername(@RequestParam("id") Long id, @RequestParam("custUsername") String custUsername) {
    if (custUsername != null && !"".equals(custUsername) && id != null && !"".equals(id)) {
      CustMaster custMaster = custMasterService.findByCustUsername(custUsername.trim());
      if (custMaster != null && !custMaster.getId().equals(id)) {
        return "false";
      }
    }
    return "true";
  }

  @RequestMapping(value = "checkGroupName")
  @ResponseBody
  public String checkGroupName(@RequestParam("groupName") String groupName) {
    String result = "true";
    if (groupName != null && !"".equals(groupName)) {
      result = String.valueOf(custMasterService.existGroupByGroupName(groupName.trim()));
    }
    return result;
  }

  @RequestMapping(value = "download")
  public void downloadTemplate(HttpServletResponse response) {
    InputStream is = ReportsController.class.getClassLoader().getResourceAsStream("excel/加款系统原始数据上传模板.xlsx");
    try {
      response.setHeader("Content-Disposition",
          "attachment;filename=\"" + new String(URLDecoder.decode("加款系统原始数据上传模板.xlsx", "UTF-8").getBytes(), "iso-8859-1") + "\"");
      OutputStream out = response.getOutputStream();
      byte buffer[] = new byte[1024];
      int len = 0;
      while ((len = is.read(buffer)) > 0) {
        // 输出缓冲区的内容到浏览器，实现文件下载
        out.write(buffer, 0, len);
      }
      is.close();
      out.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @ModelAttribute
  public void getCustMaster(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
    if (id != -1) {
      model.addAttribute("custMaster", custMasterService.getCustMaster(id));
    }
  }

  public ShiroUser getShiroUser() {
    ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
    return user;
  }

  /***
   * 客户修改插入日志
   * 
   * @param object 修改后对象
   * @param beforeObject 修改前对象
   * @param operateType 操作类型
   * @param pKey 主键ID
   */
  @SuppressWarnings("rawtypes")
  public void insertLog(Object object, int operateType, Object pKey, Object beforeObject) {
    Log log = new Log();
    log.setOperateType(operateType);
    log.setOperateBy(getShiroUser() == null ? "" : getShiroUser().getName());
    log.setOperateUserId(getShiroUser() != null ? String.valueOf(getShiroUser().id) : null);
    log.setOperateTime(new Date());
    log.setpKey(String.valueOf(pKey));
    log.setContent(object == null ? null : new JsonMapper().toJson(object));
    log.setModule(CommonUtil.LOG_MODULE_COSTOM);

    // 如果是新增或者删除
    if (operateType == Operate.ADD.getIndex() || operateType == Operate.DELETE.getIndex()) {
      log.setRemark1(null);
      log.setRemark2(null);
    } else {
      Map map1 = jsonMapper.fromJson(jsonMapper.toJson(object), Map.class);
      Map map2 = jsonMapper.fromJson(jsonMapper.toJson(beforeObject), Map.class);
      String differentContent_zh = new MapCompareUtil().compareToDifferent(map1, map2, COMPARE_KEY, "zh");
      String differentContent_en = new MapCompareUtil().compareToDifferent(map1, map2, COMPARE_KEY, "en");
      log.setRemark1(differentContent_zh);
      log.setRemark2(differentContent_en);
    }

    logService.addLog(log);
  }

  /**
   * 设置finance ops am 邮箱
   */
  public void setMails(Model model, HttpServletRequest request) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("sort", "group_name asc");
    map.put("status", "Active");
    // finance
    map.put("group_type", CommonUtil.GROUP_FINANCE);
    model.addAttribute("financeGroups", list2Map(groupService.search(map), request));
    // ops
    map.put("group_type", CommonUtil.GROUP_OPS);
    model.addAttribute("opsGroups", list2Map(groupService.search(map), request));
    // am
    map.put("group_type", CommonUtil.GROUP_AM);
    model.addAttribute("amGroups", list2Map(groupService.search(map), request));
    // sales
    map.put("group_type", CommonUtil.GROUP_SALES);
    model.addAttribute("salesGroups", list2Map(groupService.search(map), request));
  }

  public Map<String, String> list2Map(List<Group> groups, HttpServletRequest request) {
    Map<String, String> map = new TreeMap<String, String>();
    map.put("", CommonUtil.getProperty(request, "public.please.select"));
    if (groups != null && groups.size() > 0) {
      for (Group group : groups) {
        map.put(group.getGroup_name(), group.getGroup_name());
      }
    }
    return map;
  }

}
