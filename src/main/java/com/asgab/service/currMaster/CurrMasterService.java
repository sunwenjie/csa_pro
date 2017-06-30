package com.asgab.service.currMaster;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.asgab.core.pagination.Page;
import com.asgab.entity.CurrMaster;
import com.asgab.repository.CurrMasterMapper;

/**
 * 货币管理类.
 * 
 * @author siuvan
 */
// Spring Service Bean的标识.
@Component
@Transactional
public class CurrMasterService {

  @Resource
  private CurrMasterMapper currMasterMapper;

  public List<CurrMaster> getAllCurrMaster() {
    return (List<CurrMaster>) currMasterMapper.search(null);
  }

  public Page<CurrMaster> getAllCurrMaster(Page<CurrMaster> page) {
    List<CurrMaster> list = currMasterMapper.search(page.getSearchMap(), page.getRowBounds());
    int count = currMasterMapper.count(page.getSearchMap());
    page.setContent(list);
    page.setTotal(count);
    return page;
  }

  public CurrMaster getCurrMaster(Long id) {
    return currMasterMapper.get(id);
  }

  public void addCurrMaster(CurrMaster currMaster) {
    currMasterMapper.save(currMaster);
  }

  public void updateCurrMaster(CurrMaster currMaster) {
    currMasterMapper.update(currMaster);
  }

  public void deleteCurrMaster(Long id) {
    currMasterMapper.delete(id);
  }

}
