<%@tag import="org.apache.commons.lang3.StringUtils"%>
<%@tag import="java.util.Map"%>
<%@tag import="java.util.Iterator"%>
<%@tag pageEncoding="UTF-8"%>
<%@ attribute name="name" type="java.lang.String" required="true"%>
<%@ attribute name="value" type="java.lang.String" %>
<%@ attribute name="map" type="java.util.Map" required="true"%>
<%@ attribute name="add0" type="java.lang.Boolean" %>
<%@ attribute name="onchange" type="java.lang.String" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<select name="<%=name%>" class="form-control <%=name%>" id="<%=name%>" onchange="<%=onchange%>">
	<%
		if(add0!=null && add0){
			boolean isZH = "zh".equalsIgnoreCase(request.getLocale().getLanguage());
			%>
			<option value="0" ><%= isZH?"请选择":"Please Select" %></option>
			<%
		}
		Iterator iterator = map.entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry<Integer,String> entry = (Map.Entry<Integer,String>)iterator.next();
			String selected = "";
			if(StringUtils.isNoneBlank(value) && value.equals(String.valueOf(entry.getKey()))){
				selected="selected=\"selected\"";
			}
	%>
			<option value="<%=entry.getKey() %>" <%=selected %> ><%=entry.getValue() %></option>
	<%
		}
	%>
</select>