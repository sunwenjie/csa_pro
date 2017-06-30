package com.asgab.web.exchangeRate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.asgab.core.pagination.Page;
import com.asgab.entity.ExchangeRate;
import com.asgab.entity.Log;
import com.asgab.service.LogService;
import com.asgab.service.account.ShiroDbRealm.ShiroUser;
import com.asgab.service.exchangeRate.ExchangeRateService;
import com.asgab.util.CommonUtil;
import com.asgab.util.JsonMapper;
import com.asgab.util.MapCompareUtil;
import com.asgab.util.Operate;
import com.asgab.util.Servlets;

@Controller
@RequestMapping(value = "/exchangeRate")
public class ExchangeRateController {

  // Json工具类
  private final static JsonMapper jsonMapper = new JsonMapper();

  // JSON比较key
  private final static String COMPARE_KEY = "log.exchangeRate.keys";

  private static final String PAGE_SIZE = "10";

  @Autowired
  ExchangeRateService exchangeRateService;

  @Autowired
  LogService logService;

  @RequestMapping(method = RequestMethod.GET)
  public String list(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
      @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize, @RequestParam(value = "sort", defaultValue = "") String sort,
      ServletRequest request, Model model) {

    Map<String, Object> params = new HashMap<>();
    if (StringUtils.isNotBlank(request.getParameter("base_currency"))) {
      params.put("base_currency", request.getParameter("base_currency"));
    }
    if (StringUtils.isNotBlank(request.getParameter("currency"))) {
      params.put("currency", request.getParameter("currency"));
    }

    // 将搜索条件编码成字符串，用于排序，分页的URL
    model.addAttribute("search", Servlets.encodeParameterString(params));
    params.put("sort", sort);
    Page<ExchangeRate> page = new Page<>(pageNumber, pageSize, sort, params);
    model.addAttribute("pages", exchangeRateService.getAllExchangeRates(page));
    model.addAttribute("currencys", "zh".equals(request.getLocale().getLanguage()) ? CommonUtil.CURRENCY_ZH : CommonUtil.CURRENCY_EN);
    return "exchangeRate/exchangeRateList";
  }

  @RequestMapping(value = "create", method = RequestMethod.GET)
  public String toCreate(Model model, HttpServletRequest request) {
    model.addAttribute("exchangeRate", new ExchangeRate());
    model.addAttribute("action", "create");
    model.addAttribute("currencys", "zh".equals(request.getLocale().getLanguage()) ? CommonUtil.CURRENCY_ZH : CommonUtil.CURRENCY_EN);
    return "exchangeRate/exchangeRateForm";
  }

  @RequestMapping(value = "create", method = RequestMethod.POST)
  public String create(ExchangeRate exchangeRate, RedirectAttributes redirectAttributes, HttpServletRequest request) {
    exchangeRate.setCreated_at(new Date());
    exchangeRate.setCreate_by(getShiroUser() == null ? null : String.valueOf(getShiroUser().id));
    exchangeRateService.addExchangeRate(exchangeRate);
    insertLog(exchangeRate, Operate.ADD.getIndex(), exchangeRate.getId(), null);
    redirectAttributes.addFlashAttribute("message", CommonUtil.getProperty(request, "msg.add.success"));
    return "redirect:/exchangeRate/update/" + exchangeRate.getId();
  }

  @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
  public String toUpdate(@PathVariable("id") Long id, Model model, HttpServletRequest request) {
    model.addAttribute("exchangeRate", exchangeRateService.getExchangeRate(id));
    model.addAttribute("action", "update");
    model.addAttribute("currencys", "zh".equals(request.getLocale().getLanguage()) ? CommonUtil.CURRENCY_ZH : CommonUtil.CURRENCY_EN);
    return "exchangeRate/exchangeRateForm";
  }

  @RequestMapping(value = "update", method = RequestMethod.POST)
  public String update(@ModelAttribute("exchangeRate") ExchangeRate exchangeRate, RedirectAttributes redirectAttributes, HttpServletRequest request) {
    ExchangeRate exchangeRateBefore = exchangeRateService.getExchangeRate(exchangeRate.getId());
    exchangeRate.setUpdated_at(new Date());
    exchangeRate.setUpdate_by(getShiroUser() == null ? null : String.valueOf(getShiroUser().id));
    exchangeRateService.updateExchangeRate(exchangeRate);
    insertLog(exchangeRate, Operate.MODIFY.getIndex(), exchangeRate.getId(), exchangeRateBefore);
    redirectAttributes.addFlashAttribute("message", CommonUtil.getProperty(request, "msg.update.success"));
    return "redirect:/exchangeRate/update/" + exchangeRate.getId();
  }

  @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
  public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes, HttpServletRequest request) {
    exchangeRateService.deleteExchangeRate(id);
    insertLog(new ExchangeRate(), Operate.DELETE.getIndex(), id, null);
    redirectAttributes.addFlashAttribute("message", CommonUtil.getProperty(request, "msg.delete.success"));
    return "redirect:/exchangeRate";
  }

  @ModelAttribute
  public void getExchangeRate(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
    if (id != -1) {
      model.addAttribute("exchangeRate", exchangeRateService.getExchangeRate(id));
    }
  }

  public ShiroUser getShiroUser() {
    ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
    return user;
  }

  /***
   * 汇率修改插入日志
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

    if (object instanceof ExchangeRate) {
      log.setModule(CommonUtil.LOG_MODULE_EXCHANGE_RATE);
    }

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

}
