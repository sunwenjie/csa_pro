package com.asgab.repository;


import org.springframework.data.repository.PagingAndSortingRepository;

import com.asgab.entity.User;

public interface UserDao extends PagingAndSortingRepository<User, Long> {
  User findByLoginName(String loginName);
}
