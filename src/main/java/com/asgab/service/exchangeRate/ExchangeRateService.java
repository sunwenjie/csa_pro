package com.asgab.service.exchangeRate;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.asgab.core.pagination.Page;
import com.asgab.entity.ExchangeRate;
import com.asgab.repository.ExchangeRateMapper;

// Spring Service Bean的标识.
@Component
@Transactional
public class ExchangeRateService {

  @Resource
  private ExchangeRateMapper exchangeRateMapper;

  public List<ExchangeRate> getAllAvailExchangeRates() {
    return (List<ExchangeRate>) exchangeRateMapper.search(null);
  }

  public Page<ExchangeRate> getAllAvailExchangeRates(Page<ExchangeRate> page) {
    List<ExchangeRate> list = exchangeRateMapper.search(page.getSearchMap(), page.getRowBounds());
    page.setContent(list);
    page.setTotal(getAllAvailExchangeRates().size());
    return page;
  }

  public List<ExchangeRate> getAllExchangeRates() {
    return (List<ExchangeRate>) exchangeRateMapper.searchAll(null);
  }

  public Page<ExchangeRate> getAllExchangeRates(Page<ExchangeRate> page) {
    List<ExchangeRate> list = exchangeRateMapper.searchAll(page.getSearchMap(), page.getRowBounds());
    page.setContent(list);
    page.setTotal(exchangeRateMapper.count(page.getSearchMap()));
    return page;
  }

  public ExchangeRate getExchangeRate(Long id) {
    return exchangeRateMapper.get(id);
  }

  public void addExchangeRate(ExchangeRate exchangeRate) {
    exchangeRateMapper.save(exchangeRate);
  }

  public void updateExchangeRate(ExchangeRate exchangeRate) {
    exchangeRateMapper.update(exchangeRate);
  }

  public void deleteExchangeRate(Long id) {
    exchangeRateMapper.delete(id);
  }

}
