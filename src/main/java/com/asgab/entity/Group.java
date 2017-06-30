package com.asgab.entity;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class Group {

  private String id;
  private String group_name;
  private String group_type;
  private String description;
  private String status;
  private Date created_at;
  private Date updated_at;
  private Long agency_id;
  private String function_code;

  // for app
  // 用于用户勾选组
  private String checked;
  // 组内人数
  private int memberCount;

  private Map<String, String> groupTypes = new TreeMap<String, String>();
  private Map<String, String> statuses = new TreeMap<String, String>();


  public Group() {
    super();
  }

  public Group(String id) {
    super();
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getGroup_name() {
    return group_name;
  }


  public void setGroup_name(String group_name) {
    this.group_name = group_name;
  }


  public String getGroup_type() {
    return group_type;
  }


  public void setGroup_type(String group_type) {
    this.group_type = group_type;
  }


  public String getDescription() {
    return description;
  }


  public void setDescription(String description) {
    this.description = description;
  }


  public String getStatus() {
    return status;
  }


  public void setStatus(String status) {
    this.status = status;
  }


  public Date getCreated_at() {
    return created_at;
  }


  public void setCreated_at(Date created_at) {
    this.created_at = created_at;
  }


  public Date getUpdated_at() {
    return updated_at;
  }


  public void setUpdated_at(Date updated_at) {
    this.updated_at = updated_at;
  }


  public Long getAgency_id() {
    return agency_id;
  }


  public void setAgency_id(Long agency_id) {
    this.agency_id = agency_id;
  }

  public Map<String, String> getGroupTypes() {
    return groupTypes;
  }

  public void setGroupTypes(Map<String, String> groupTypes) {
    this.groupTypes = groupTypes;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Group other = (Group) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  public String getChecked() {
    return checked;
  }

  public void setChecked(String checked) {
    this.checked = checked;
  }

  public Map<String, String> getStatuses() {
    return statuses;
  }

  public void setStatuses(Map<String, String> statuses) {
    this.statuses = statuses;
  }

  public String getFunction_code() {
    return function_code;
  }

  public void setFunction_code(String function_code) {
    this.function_code = function_code;
  }

  public int getMemberCount() {
    return memberCount;
  }

  public void setMemberCount(int memberCount) {
    this.memberCount = memberCount;
  }

}
