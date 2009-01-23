<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.ParseException"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Locale"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Set"%>
<%@page import="com.freshdirect.fdstore.content.CategoryModel"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@page import="com.freshdirect.fdstore.content.ContentNodeModel"%>
<%@page import="com.freshdirect.fdstore.content.ProductModel"%>
<%@page import="com.freshdirect.fdstore.util.EnumSiteFeature"%>
<%@page import="com.freshdirect.fdstore.util.URLGenerator"%>
<%@page import="com.freshdirect.mail.EmailUtil"%>
<%@page import="com.freshdirect.smartstore.RecommendationService"%>
<%@page import="com.freshdirect.smartstore.SessionInput"%>
<%@page import="com.freshdirect.smartstore.fdstore.SmartStoreServiceConfiguration"%>
<%@page import="com.freshdirect.smartstore.fdstore.VariantSelection"%>
<%@page import="com.freshdirect.test.TestSupport"%>
<%@page import="com.freshdirect.webapp.util.JspMethods"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.apache.commons.lang.math.NumberUtils"%>
<%@ taglib uri="freshdirect" prefix="fd"%>
<%

DateFormat paramFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
DateFormat displayFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);

Iterator it;
URLGenerator urlG = new URLGenerator(request);
String origURL = urlG.build();

String[] siteFeaturesArray = { "DYF", "FEATURED_ITEMS", "FAVORITES" };
List siteFeatures = Arrays.asList(siteFeaturesArray);

Map varMap = new HashMap();

VariantSelection helper = VariantSelection.getInstance();

Map cohorts = helper.getCohorts();

/* view */
List dates = helper.getStartDates();
String defaultDate = "(null)";
String date = urlG.get("date");
CHECK: {
	if (date == null || date.length() == 0) {
		date = defaultDate;
	} else {
		if (!date.equals(defaultDate)) {
			it = dates.iterator();
			while (it.hasNext()) {
				if (date.equals(paramFormat.format(it.next())))
					break CHECK;
			}
		}
		date = defaultDate;
	}
}
Date dateVal;
if (defaultDate.equals(date)) {
	urlG.remove("date");
	dateVal = null;
} else {
	urlG.set("date", date);
	try {
		dateVal = paramFormat.parse(date);
	} catch (ParseException e) {
		dateVal = null;
	}
}

/* redirect */
String newURL = urlG.build();

if (!origURL.equals(newURL)) {
	response.sendRedirect(StringEscapeUtils.unescapeHtml(newURL));	
}

%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>COHORTS SUMMARY PAGE</title>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<style type="text/css">

.test-page{margin:20px 60px;color:#333333;background-color:#fff;}
.test-page input{font-weight:normal;}
.test-page p{margin:0px;padding:0px 0px 8px;}
p.head{padding:10px 0px 20px;}
.test-page .bull{color:#cccccc;}
.test-page a{color:#336600;}.test-page a:VISITED{color:#336600;}
.test-page table{border-collapse:collapse;border-spacing:0px;width:100%;}
.test-page table.t1{width:auto;margin-bottom:20px;}
.test-page table.t1 td{border:1px solid black;padding:4px 8px;}

	</style>
</head>
<body class="test-page">
	<form>
	<p class="head">
	<select name="date" onchange="this.form.submit();">
	<option value="<%= defaultDate %>"<%= date.equals(defaultDate) ? " selected=\"selected\"" : ""%>>Current rules</option>
	<%
		it = dates.iterator();
		while (it.hasNext()) {
			Date d2 = (Date) it.next();
	%>
	<option value="<%= paramFormat.format(d2) %>"
		<%= date.equals(paramFormat.format(d2)) ? " selected=\"selected\"" : ""%>>Before <%= displayFormat.format(d2) %></option>
	<%
		}
	%>
	</select>
   	</p>
	</form>
   	<%
   		it = siteFeatures.iterator();
   		while (it.hasNext()) {
   			String sf = (String) it.next();
   			Map curVars = SmartStoreServiceConfiguration.getInstance().getServices(EnumSiteFeature.getEnum(sf));
   			varMap.put(sf, curVars);
   	%>
    <table class="t1">
    	<tr>
    	<td class="text13bold" <%= curVars.size() > 0 ? "colspan=\"" + curVars.size() + "\"" : "" %>
    			title="Site Feature"><%= sf %></td>
    	</tr>
    	<tr>
    	<%
   			Iterator it2 = curVars.keySet().iterator();
   			while (it2.hasNext()) {
   				String varId = (String) it2.next();
    	%>
    	<td class="text13" title="Variant"><%= varId %></td>
    	<%
    		}
    	%>
    	</tr>
    	<tr>
    	<%
   			it2 = curVars.keySet().iterator();
	    	Map assignment = helper.getVariantMap(EnumSiteFeature.getEnum(sf), dateVal);
   			while (it2.hasNext()) {
   				String varId = (String) it2.next();
   				Iterator it3 = cohorts.keySet().iterator();
   				int weight = 0;
   				String cohortsStr = "";
   				String color = "#999";
   				while (it3.hasNext()) {
   					String cohort = (String) it3.next();
   					if (varId.equals(assignment.get(cohort))) {
   						int curWeight = ((Integer) cohorts.get(cohort)).intValue();
   						weight += curWeight;
   						if (cohortsStr.length() == 0) {
   							cohortsStr += cohort + " (" + curWeight + "%)";
   						} else {
   							cohortsStr += ", " + cohort + " (" + curWeight + "%)";
   						}
   					}
   				}
   				if (weight > 0) {
   					cohortsStr = "Cohort percentage, in details: " + cohortsStr;
   					int green = 0x99 - 0x99 * weight / 100;
   					color = "rgb(" + green + "," + 0x99 + "," + green + ")";
   				} else {
   					cohortsStr = "Cohort percentage";
   				}
    	%>
    	<td class="text13" style="background-color: <%= color %>;"><span title="<%= cohortsStr %>"><%= Integer.toString(weight) %>%</span></td>
    	<%
    		}
    	%>
    	</tr>
    </table>
   	<%
   		}
   	%>
    <p>Hover over the percentages to view actual cohorts in effect.</p>
    <p>Hover over the other items in the table to view meanings.</p>
</body>
</html>