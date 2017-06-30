package com.asgab.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.asgab.core.pagination.Page;
import com.asgab.entity.Log;
import com.asgab.repository.LogMapper;

// Spring Service Bean的标识.
@Component
@Transactional
public class LogService {

  @Resource
  private LogMapper logMapper;

  public Log get(Long id) {
    return logMapper.get(id);
  }

  public void addLog(Log log) {
    logMapper.save(log);
  }

  public List<Log> getAllLog() {
    return (List<Log>) logMapper.search(null);
  }

  public Page<Log> getAllLog(Page<Log> page) {
    List<Log> list = logMapper.search(page.getSearchMap(), page.getRowBounds());
    int count = logMapper.count(page.getSearchMap());
    page.setContent(list);
    page.setTotal(count);
    return page;
  }

}
