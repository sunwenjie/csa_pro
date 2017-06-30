package com.asgab.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asgab.entity.CustMaster;
import com.asgab.repository.PayTranAttachmentMapper;
import com.asgab.service.custMaster.CustMasterService;
import com.asgab.service.exchangeRate.ExchangeRateService;
import com.asgab.util.CommonUtil;

@Controller
@RequestMapping(value = "/ajax")
public class AjaxController {

  @Resource
  private PayTranAttachmentMapper payTranAttachmentMapper;

  @Autowired
  private CustMasterService custMasterService;

  @Autowired
  private ExchangeRateService exchangeRateService;

  @RequestMapping(value = "delAttach/{attachmentId}", method = RequestMethod.POST)
  public void delAttach(@PathVariable("attachmentId") Long attachmentId, HttpServletRequest request, HttpServletResponse response) {
    PrintWriter out = null;
    try {
      payTranAttachmentMapper.delete(attachmentId);
      out = response.getWriter();
      out.print(CommonUtil.i18nStr(request, "删除附件成功", "Attachment deleted successfully"));
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (out != null) {
        out.flush();
        out.close();
      }
    }
  }

  @RequestMapping(value = "checkBaiduUsername")
  @ResponseBody
  public String checkLoginName(@RequestParam("bdUserName") String bdUserName) {
    return custMasterService.existByCustUsername(bdUserName) ? "true" : "false";
  }
  
  @RequestMapping(value = "checkBaiduUsertype")
  @ResponseBody
  public String checkBaiduUsertype(@RequestParam("names") String bdUserNames) {
	if (StringUtils.isBlank(bdUserNames)){
		return "true";
	}
	String[] names = bdUserNames.split(",");
	HashSet<Integer> types = new HashSet<>();
	for(String name : names){
	  CustMaster user = custMasterService.findByCustUsername(name);
	  if ("N/A".equals(user.getCustName())){
		types.add(2);
	  }
	  types.add(user.is_credit_client() ? 1 : 0);
	}
    return types.size() > 1 ? "false" : "true";
  }

  @RequestMapping(value = "latestExchangeRate", method = RequestMethod.GET)
  public String lastestExchangeRate(Model model) {
    model.addAttribute("exchangeRates", exchangeRateService.getAllAvailExchangeRates());
    return "exchangeRate/lastestExchangeRate";
  }

}
