package com.asgab.service.custMaster;

import com.asgab.core.pagination.Page;
import com.asgab.entity.CustBaseInfo;
import com.asgab.entity.CustMaster;
import com.asgab.repository.CustMasterMapper;
import com.asgab.service.account.ShiroDbRealm.ShiroUser;
import com.asgab.service.account.UserXMOService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 客户管理类.
 * 
 * @author siuvan
 */
// Spring Service Bean的标识.
@Component
@Transactional
public class CustMasterService {

  @Resource
  private CustMasterMapper custMasterMapper;

  @Autowired
  private UserXMOService userXMOService;

  public List<CustMaster> getAllCustMaster() {
    return (List<CustMaster>) custMasterMapper.search(null);
  }

  public Page<CustMaster> getAllCustMaster(Page<CustMaster> page) {
    List<CustMaster> list = custMasterMapper.search(page.getSearchMap(), page.getRowBounds());
    int count = custMasterMapper.count(page.getSearchMap());
    page.setContent(list);
    page.setTotal(count);
    return page;
  }

  public CustMaster getCustMaster(Long id) {
    return custMasterMapper.get(id);
  }

  public void addCustMaster(CustMaster custMaster) {
    custMasterMapper.save(custMaster);
  }

  public void updateCustMaster(CustMaster custMaster) {
    ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
    custMaster.setUpdateDate(new Date());
    custMaster.setUpdateBy(user == null ? "" : user.id + "");
    custMasterMapper.update(custMaster);
  }

  public void deleteCustMaster(Long id) {
    custMasterMapper.delete(id);
  }

  public boolean existByCustUsername(String custUsername) {
    return custMasterMapper.existByCustUsername(custUsername) >= 1 ? true : false;
  }

  public CustMaster findByCustUsername(String custUsername) {
    return custMasterMapper.findByCustUsername(custUsername);
  }

  public boolean existGroupByGroupName(String groupName) {
    return userXMOService.findGroupByGroupName(groupName).size() >= 1 ? true : false;
  }

  public String addCustMasters(List<CustMaster> list) {
    int addRows = 0;
    int updateRows = 0;
    int delRows = 0;
    for (CustMaster custMaster : list) {

      CustMaster db_CustMaster = custMasterMapper.findByCustUsernameIncludeDeleted(custMaster.getCustUsername());
      ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
      if (db_CustMaster == null) {
        custMaster.setCreateDate(new Date());
        custMaster.setCreateBy(user == null ? "" : user.id + "");
        addCustMaster(custMaster);
        addRows++;
      } else {
        if (StringUtils.isNotBlank(custMaster.getIsDeleted()) && "Y".equalsIgnoreCase(custMaster.getIsDeleted())) {
          db_CustMaster.setDeleted(2);
          updateCustMaster(db_CustMaster);
          delRows++;
        } else {
          custMaster.setId(db_CustMaster.getId());
          custMaster.setDeleted(1);
          updateCustMaster(custMaster);
          updateRows++;
        }
      }

    }
    return addRows + ":" + updateRows + ":" + delRows;
  }

  public List<CustBaseInfo> findCustBaseInfos(Map<String, Object> parms) {
    return (List<CustBaseInfo>) custMasterMapper.findCustBaseInfos(parms);
  }

  public Page<CustBaseInfo> findCustBaseInfos(Page<CustBaseInfo> page) {
    List<CustBaseInfo> list = custMasterMapper.findCustBaseInfos(page.getSearchMap(), page.getRowBounds());
    int count = custMasterMapper.countCustBaseInfos(page.getSearchMap());
    page.setContent(list);
    page.setTotal(count);
    return page;
  }
}
