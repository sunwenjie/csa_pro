package com.asgab.repository;

import com.asgab.entity.BlockTrade;
import com.asgab.repository.mybatis.MyBatisRepository;


@MyBatisRepository
public interface BlockTradeMapper {
	void insert(BlockTrade blockTrade);
	BlockTrade get(Long processId);
}
