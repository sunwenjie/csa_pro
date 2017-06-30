package com.asgab.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Servlets {

  public static String encodeParameterString(Map<String, Object> params) {
    if (Collections3.isEmpty(params)) {
      return "";
    }

    StringBuilder queryStringBuilder = new StringBuilder();
    Iterator<Entry<String, Object>> it = params.entrySet().iterator();
    while (it.hasNext()) {
      Entry<String, Object> entry = it.next();
      if ("sort".equals(entry.getKey()))
        continue;
      queryStringBuilder.append(entry.getKey()).append('=').append(entry.getValue());
      if (it.hasNext()) {
        queryStringBuilder.append('&');
      }
    }
    return queryStringBuilder.toString();
  }
}
