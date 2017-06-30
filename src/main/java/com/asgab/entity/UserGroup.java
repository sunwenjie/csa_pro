package com.asgab.entity;

import java.util.Date;

public class UserGroup {
  private Long id;

  private Long user_id;

  private Long group_id;

  private Date created_at;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getUser_id() {
    return user_id;
  }

  public void setUser_id(Long user_id) {
    this.user_id = user_id;
  }

  public Long getGroup_id() {
    return group_id;
  }

  public void setGroup_id(Long group_id) {
    this.group_id = group_id;
  }

  public Date getCreated_at() {
    return created_at;
  }

  public void setCreated_at(Date created_at) {
    this.created_at = created_at;
  }

}
