package com.asgab.web.currMaster;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.asgab.core.pagination.Page;
import com.asgab.entity.CurrMaster;
import com.asgab.service.account.ShiroDbRealm.ShiroUser;
import com.asgab.service.currMaster.CurrMasterService;
import com.asgab.util.Servlets;

/***
 * 货币管理的Controller
 * 
 * @author Siuvan
 */
@Controller
@RequestMapping(value = "/currMaster")
public class CurrMasterController {

  private static final String PAGE_SIZE = "10";
  @Resource
  CurrMasterService currMasterService;

  @RequestMapping(method = RequestMethod.GET)
  public String list(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
      @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize, @RequestParam(value = "sort", defaultValue = "") String sort,
      ServletRequest request, Model model) {

    Map<String, Object> params = new HashMap<>();
    if (StringUtils.isNotBlank(request.getParameter("curr_code"))) {
      params.put("curr_code", request.getParameter("curr_code"));
    }
    if (StringUtils.isNotBlank(request.getParameter("curr_name"))) {
      params.put("curr_name", request.getParameter("curr_name"));
    }

    // 将搜索条件编码成字符串，用于排序，分页的URL
    model.addAttribute("search", Servlets.encodeParameterString(params));
    params.put("sort", sort);
    Page<CurrMaster> page = new Page<>(pageNumber, pageSize, sort, params);
    model.addAttribute("pages", currMasterService.getAllCurrMaster(page));
    return "currMaster/currMastersList";
  }

  @RequestMapping(value = "create", method = RequestMethod.GET)
  public String toCreate(Model model) {
    model.addAttribute("currMaster", new CurrMaster());
    model.addAttribute("action", "create");
    return "currMaster/currMasterForm";
  }

  @RequestMapping(value = "create", method = RequestMethod.POST)
  public String create(CurrMaster currMaster, RedirectAttributes redirectAttributes) {
    currMaster.setUpdateDate(new Date());
    ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
    currMaster.setUpdateBy(String.valueOf(user.id));
    currMaster.setLastUpdateBy(String.valueOf(user.id));
    currMaster.setLastUpdateDate(new Date());
    currMaster.setLastUpdateRate(currMaster.getCurr_rate());
    currMasterService.addCurrMaster(currMaster);
    redirectAttributes.addFlashAttribute("message", "create success");
    return "redirect:/currMaster/update/" + currMaster.getId();
  }

  @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
  public String toUpdate(@PathVariable("id") Long id, Model model) {
    model.addAttribute("currMaster", currMasterService.getCurrMaster(id));
    model.addAttribute("action", "update");
    return "currMaster/currMasterForm";
  }

  @RequestMapping(value = "update", method = RequestMethod.POST)
  public String update(@ModelAttribute("currMaster") CurrMaster currMaster, RedirectAttributes redirectAttributes) {
    CurrMaster lastUpdate = currMasterService.getCurrMaster(currMaster.getId());
    currMaster.setLastUpdateBy(lastUpdate.getUpdateBy());
    currMaster.setLastUpdateDate(lastUpdate.getUpdateDate());
    currMaster.setLastUpdateRate(lastUpdate.getCurr_rate());
    currMaster.setUpdateDate(new Date());
    ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
    currMaster.setUpdateBy(String.valueOf(user.id));
    currMasterService.updateCurrMaster(currMaster);
    redirectAttributes.addFlashAttribute("message", "update success");
    return "redirect:/currMaster/update/" + currMaster.getId();
  }

  @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
  public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
    currMasterService.deleteCurrMaster(id);
    redirectAttributes.addFlashAttribute("message", "delete success");
    return "redirect:/currMaster";
  }

  @ModelAttribute
  public void getCurrMaster(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
    if (id != -1) {
      model.addAttribute("currMaster", currMasterService.getCurrMaster(id));
    }
  }

}
