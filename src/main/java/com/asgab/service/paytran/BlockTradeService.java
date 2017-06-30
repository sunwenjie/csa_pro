package com.asgab.service.paytran;

import com.asgab.entity.BlockTrade;
import com.asgab.repository.BlockTradeMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Component
@Transactional
public class BlockTradeService {
  
  @Resource
  private BlockTradeMapper blockTradeMapper;
  
  public void insert(double amount30, double amount15, Long payTranNum, Long processId){
	BlockTrade blockTrade = new BlockTrade();
	blockTrade.setAmount30(amount30);
	blockTrade.setAmount15(amount15);
	blockTrade.setPayTranNum(payTranNum);
	blockTrade.setProcessId(processId);
	blockTradeMapper.insert(blockTrade);
  }

  public BlockTrade getBlockTrade(Long processId){return blockTradeMapper.get(processId);}
}
