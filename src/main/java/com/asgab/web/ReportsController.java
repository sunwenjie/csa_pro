package com.asgab.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.asgab.core.pagination.Page;
import com.asgab.entity.CustBaseInfo;
import com.asgab.entity.CustPaymentInfo;
import com.asgab.service.custMaster.CustMasterService;
import com.asgab.service.paytran.PayTranHeaderService;
import com.asgab.util.CommonUtil;
import com.asgab.util.Servlets;

@Controller
@RequestMapping(value = "/reports")
public class ReportsController {

  // 每页显示记录
  private static final String PAGE_SIZE = "10";

  @Autowired
  private CustMasterService custMasterService;

  @Autowired
  private PayTranHeaderService payTranHeaderService;

  @RequestMapping(value = "custBaseInfoList", method = RequestMethod.GET)
  public String custBaseInfoList(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
      @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize, @RequestParam(value = "sort", defaultValue = "") String sort,
      ServletRequest request, Model model) {

    Map<String, Object> params = new HashMap<>();
    if (StringUtils.isNotBlank(request.getParameter("advertiser"))) {
      params.put("advertiser", request.getParameter("advertiser"));
    }
    if (StringUtils.isNotBlank(request.getParameter("custUsername"))) {
      params.put("custUsername", request.getParameter("custUsername"));
    }

    Page<CustBaseInfo> page = new Page<>(pageNumber, pageSize, sort, params);
    // 将搜索条件编码成字符串，用于排序，分页的URL
    model.addAttribute("search", Servlets.encodeParameterString(params));
    params.put("sort", sort);
    model.addAttribute("pages", custMasterService.findCustBaseInfos(page));
    return "reports/custBaseInfoList";
  }

  @RequestMapping(value = "downloadCustBaseInfo", method = RequestMethod.GET)
  public String downloadCustBaseInfo(ServletRequest request, HttpServletResponse response) throws IOException {

    Map<String, Object> params = new HashMap<>();
    if (StringUtils.isNotBlank(request.getParameter("advertiser"))) {
      params.put("advertiser", request.getParameter("advertiser"));
    }
    if (StringUtils.isNotBlank(request.getParameter("custUsername"))) {
      params.put("custUsername", request.getParameter("custUsername"));
    }

    List<CustBaseInfo> lists = custMasterService.findCustBaseInfos(params);

    response.setCharacterEncoding("utf-8");
    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=\"Customer Basic Information.xlsx\"");

    InputStream is = ReportsController.class.getClassLoader().getResourceAsStream("excel/report1.xlsx");
    XSSFWorkbook workbook = new XSSFWorkbook(is);

    if (lists != null && lists.size() > 0 && workbook != null) {

      XSSFSheet sheet = workbook.getSheetAt(0);
      for (int i = 0; i < lists.size(); i++) {
        CustBaseInfo info = lists.get(i);
        XSSFRow row = sheet.createRow(i + 2);
        if (row != null) {
          row.createCell(0).setCellValue(info.getCustName());
          row.createCell(1).setCellValue(info.getAdvertiser());
          row.createCell(2).setCellValue(info.getCustUsername());
          row.createCell(3).setCellValue("");
          row.createCell(4).setCellValue("");
          row.createCell(5).setCellValue(info.getCustPort());
          row.createCell(6).setCellValue(info.getAm_contact());
          row.createCell(7).setCellValue(info.getRewardsPercent());
          row.createCell(8).setCellValue(info.getMgtFeePercent());
        }
      }
    }

    workbook.write(response.getOutputStream());
    response.getOutputStream().flush();
    response.getOutputStream().close();
    is.close();

    return null;
  }

  @RequestMapping(value = "custPaymentInfoList", method = RequestMethod.GET)
  public String custPaymentInfoList(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
      @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize, @RequestParam(value = "sort", defaultValue = "") String sort,
      ServletRequest request, Model model) {

    Map<String, Object> params = new HashMap<>();
    if (StringUtils.isNotBlank(request.getParameter("advertiser"))) {
      params.put("advertiser", request.getParameter("advertiser"));
    }
    if (StringUtils.isNotBlank(request.getParameter("paymentDateStart"))) {
      params.put("paymentDateStart", request.getParameter("paymentDateStart"));
    }
    if (StringUtils.isNotBlank(request.getParameter("paymentDateEnd"))) {
      params.put("paymentDateEnd", request.getParameter("paymentDateEnd"));
    }
    if (StringUtils.isNotBlank(request.getParameter("bdUserName"))) {
      params.put("bdUserName", request.getParameter("bdUserName"));
    }

    Page<CustPaymentInfo> page = new Page<>(pageNumber, pageSize, sort, params);
    // 将搜索条件编码成字符串，用于排序，分页的URL
    model.addAttribute("search", Servlets.encodeParameterString(params));
    params.put("sort", sort);
    model.addAttribute("pages", payTranHeaderService.findCustPaymentInfos(page));
    return "reports/custPaymentInfoList";
  }

  @RequestMapping(value = "downloadCustPaymentInfo", method = RequestMethod.GET)
  public String downloadCustPaymentInfo(ServletRequest request, HttpServletResponse response) throws IOException {

    Map<String, Object> params = new HashMap<>();
    if (StringUtils.isNotBlank(request.getParameter("advertiser"))) {
      params.put("advertiser", request.getParameter("advertiser"));
    }
    if (StringUtils.isNotBlank(request.getParameter("paymentDateStart"))) {
      params.put("paymentDateStart", request.getParameter("paymentDateStart") + " 00:00:00");
    }
    if (StringUtils.isNotBlank(request.getParameter("paymentDateEnd"))) {
      params.put("paymentDateEnd", request.getParameter("paymentDateEnd") + " 23:59:59");
    }

    List<CustPaymentInfo> lists = payTranHeaderService.findCustPaymentInfos(params);

    response.setCharacterEncoding("utf-8");
    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=\"Customer Payment Information.xlsx\"");

    InputStream is = ReportsController.class.getClassLoader().getResourceAsStream("excel/report2.xlsx");
    XSSFWorkbook workbook = new XSSFWorkbook(is);
    XSSFCellStyle dateStyle = workbook.createCellStyle();
    XSSFDataFormat format = workbook.createDataFormat();
    dateStyle.setDataFormat(format.getFormat("yyyy-MM-dd HH:mm:ss"));

    // 数字靠右,千位分割
    XSSFCellStyle moneyStyle = workbook.createCellStyle();
    moneyStyle.setDataFormat(format.getFormat("#,##0.00"));
    moneyStyle.setAlignment(XSSFCellStyle.ALIGN_RIGHT);

    if (lists != null && lists.size() > 0 && workbook != null) {

      XSSFSheet sheet = workbook.getSheetAt(0);

      for (int i = 0; i < lists.size(); i++) {
        CustPaymentInfo info = lists.get(i);
        XSSFRow row = sheet.createRow(i + 2);
        if (row != null) {
          int index = 0;
          XSSFCell cell0 = row.createCell(index++);
          cell0.setCellValue(info.getPaymentDate());
          cell0.setCellStyle(dateStyle);

          row.createCell(index++).setCellValue(info.getCustName());
          row.createCell(index++).setCellValue(info.getAdvertiser());
          row.createCell(index++).setCellValue("N / A");
          row.createCell(index++).setCellValue(info.getCustPort());

          XSSFCell cell5 = row.createCell(index++);
          cell5.setCellValue(getFormatDate(info.getTopupDate()));
          cell5.setCellStyle(dateStyle);

          row.createCell(index++).setCellValue(info.getBdUserName());
          row.createCell(index++).setCellValue(getFormatDate(info.getOpsDate()));
          row.createCell(index++).setCellValue(info.getCurrency());
          // money style
          int moneyIndexStart = index;
          row.createCell(index++).setCellValue(info.getAmount());
          row.createCell(index++).setCellValue(info.getAmountInRmb());
          row.createCell(index++).setCellValue(info.getTopupAmount());
          if (info.getPayCode().equals("1")){
        	double realAddAmount = 0.0;
        	if (info.getRealAddAmount() > 0.0)
        	  realAddAmount = info.getRealAddAmount();
        	else if (StringUtils.isBlank(info.getDescription())){
        	  realAddAmount = info.getTopupAmount();
        	}
        	row.createCell(index++).setCellValue(realAddAmount);
        	row.getCell(index-1).setCellStyle(moneyStyle);
          }
          else {
        	row.createCell(index++).setCellValue("");
          }
          int descIndex = index;
          row.createCell(index++).setCellValue(info.getDescription());
          row.createCell(index++).setCellValue(info.getRebateAmount());
          row.createCell(index++).setCellValue(info.getAcctCreateFeeAmount());
          row.createCell(index++).setCellValue(info.getAnnualFeeAmount());
          row.createCell(index++).setCellValue(info.getGuaranteeFeeAmount());
          row.createCell(index++).setCellValue(info.getMgtFeeAmount());
          int moneyIndexEnd = index;
          for (int moneyStyleIndex = moneyIndexStart; moneyStyleIndex < moneyIndexEnd; moneyStyleIndex++) {
        	if (moneyStyleIndex != descIndex)
              row.getCell(moneyStyleIndex).setCellStyle(moneyStyle);
          }
          // money style end
          row.createCell(index++).setCellValue(info.getCustRemark());
        }
      }
    }

    workbook.write(response.getOutputStream());
    response.getOutputStream().flush();
    response.getOutputStream().close();
    is.close();

    return null;
  }

  private String getFormatDate(Date date) {
    String tmpDate = "";
    if (date != null) {
      tmpDate = CommonUtil.formatDate(date);
    }
    return tmpDate;
  }
}
