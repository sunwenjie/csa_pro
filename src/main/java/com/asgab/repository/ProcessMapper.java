package com.asgab.repository;

import java.util.List;
import java.util.Map;

import com.asgab.entity.Process;
import com.asgab.repository.mybatis.MyBatisRepository;

@MyBatisRepository
public interface ProcessMapper {

  List<Process> get(Map<String, Object> parms);

  Process getById(Long processId);

  Process getLastProcess(Long processId);

  void save(Process process);

  void update(Process process);

  List<Process> getProcessesByPaytranNums(Long paytranNum);

}
