<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.ParseException"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Locale"%>
<%@page import="java.util.HashMap"%>
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
if (dates.size() > 0)
	dates.remove(0);

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

List cohortNames = CohortSelector.getCohortNames();
SmartStoreUtil.sortCohortNames(cohortNames);

Map assignments = new HashMap();
it = siteFeatures.iterator();
while (it.hasNext()) {
	String sf = (String) it.next();
	assignments.put(sf, helper.getVariantMap(EnumSiteFeature.getEnum(sf), dateVal));
}

String colors[] = {
		"#8A2BE2", "#A52A2A", "#DEB887", "#5F9EA0", "#7FFF00", 
		"#D2691E", "#FF7F50", "#6495ED", "#DC143C", "#008B8B", 
		"#8B008B", "#556B2F", "#FF8C00", "#8B0000", "#E9967A", 
		"#483D8B", "#2F4F4F", "#00CED1", "#FF1493", "#00BFFF", 
		"#1E90FF", "#B22222", "#228B22", "#FF00FF", "#FFD700", 
		"#DAA520", "#008000", "#ADFF2F", "#FF69B4", "#CD5C5C"
};

List varColors = new ArrayList();

%>

<%@page import="com.freshdirect.smartstore.fdstore.CohortSelector"%>
<%@page import="com.freshdirect.smartstore.fdstore.SmartStoreUtil"%><html>
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
	<option value="<%= defaultDate %>"<%= date.equals(defaultDate) ? " selected=\"selected\"" : "" %>>Current rules</option>
	<%
		it = dates.iterator();
		while (it.hasNext()) {
			Date d2 = (Date) it.next();
	%>
	<option value="<%= paramFormat.format(d2) %>"
		<%= date.equals(paramFormat.format(d2)) ? " selected=\"selected\"" : "" %>>Before <%= displayFormat.format(d2) %></option>
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
    		Map variants = SmartStoreUtil.getVariantsSortedInWeight(EnumSiteFeature.getEnum(sf), dateVal);
    		
   			Iterator it2 = variants.keySet().iterator();
   			Map assignment = (Map) assignments.get(sf);
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
   			it2 = variants.keySet().iterator();
   			while (it2.hasNext()) {
   				String varId = (String) it2.next();
   				Iterator it3 = cohortNames.iterator();
   				String cohortsStr = "";
   				while (it3.hasNext()) {
   					String cohort = (String) it3.next();
   					if (varId.equals(assignment.get(cohort))) {
   						int curWeight = ((Integer) cohorts.get(cohort)).intValue();
   						if (cohortsStr.length() == 0) {
   							cohortsStr += cohort + " (" + curWeight + "%)";
   						} else {
   							cohortsStr += ", " + cohort + " (" + curWeight + "%)";
   						}
   					}
   				}
   				String color = "#ccc";
   				int weight = ((Integer) variants.get(varId)).intValue();
   				if (weight > 0) {
   					cohortsStr = "Cohort percentage, in details: " + cohortsStr;
   					int green = 0x99 - 0x99 * weight / 100;
   					color = "rgb(" + green + "," + 0x99 + "," + green + ")";
   				} else {
   					cohortsStr = "Cohort percentage";;
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
    <p>&nbsp;</p>
    <table class="t1">
    	<tr>
    	<td class="text13" title="Cohort"></td>
    	<%
    		it = siteFeatures.iterator();
    		while (it.hasNext()) {
    			String sf = (String) it.next();
    	%>
    	<td class="text13bold" title="Site Feature"><%= sf %></td>
    	<%
    		}
    	%>
    	</tr>
    	<%
    		List sortedCohorts = CohortSelector.getCohortNames();
    		SmartStoreUtil.sortCohortNames(sortedCohorts);
    		it = sortedCohorts.iterator();
    		while (it.hasNext()) {
    			String cohort = (String) it.next();
    	%>
    	<tr>
    	<td class="text13bold" title="Cohort"><%= cohort %></td>
    	<%
    			Iterator it2 = siteFeatures.iterator();
    			while (it2.hasNext()) {
	    			String sf = (String) it2.next();
	    			Map assignment = (Map) assignments.get(sf);
	    			String variant = (String) assignment.get(cohort);
	    			int colIndex = -1;
	    			if (variant != null) {
	    				colIndex = varColors.indexOf(variant) % colors.length;
	    				if (colIndex == -1) {
	    					colIndex = varColors.size() % colors.length;
	    					varColors.add(variant);
	    				}
	    			}
    	%>
    	<td class="text13"<%= colIndex < 0 ? " title=\"&lt;Undefined&gt;\"" : 
    				" title=\"Variant\" style=\"background-color: " + colors[colIndex] + ";\"" %>><%= variant %></td>
    	<%
	    		}
    	%>
    	</tr>
    	<%
    		}
    	%>
    </table>
    <p>Hover over the items in the table to view their meanings.</p>
</body>
</html>