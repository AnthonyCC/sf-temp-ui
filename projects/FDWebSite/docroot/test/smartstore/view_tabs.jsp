<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.SortedMap"%>
<%@page import="java.util.TreeMap"%>
<%@page import="com.freshdirect.fdstore.util.EnumSiteFeature"%>
<%@page import="com.freshdirect.smartstore.CartTabStrategyPriority"%>
<%@page import="com.freshdirect.smartstore.RecommendationService"%>
<%@page import="com.freshdirect.smartstore.Variant"%>
<%@page import="com.freshdirect.smartstore.service.VariantRegistry"%>
<%@ taglib uri="freshdirect" prefix="fd"%>
<%@ taglib uri='logic' prefix='logic' %>
<fd:CheckLoginStatus noRedirect="true" />
<%
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>CART TAB STRATEGY CONFIGURATIONS PAGE</title>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<style type="text/css">

.test-page{margin:20px 60px;color:#333333;background-color:#fff;}
.test-page input{font-weight:normal;}
.test-page p{margin:0px;padding:0px;}
.test-page .bull{color:#cccccc;}
.test-page a{color:#336600;}.test-page a:VISITED{color:#336600;}
.test-page table{border-collapse:collapse;border-spacing:0px;width:100%;}
table.priorities {margin: 10px 0px;width: auto;}
table.priorities th {border:1px solid black;vertical-align: top; padding: 8px; font-weight: bold;}
table.priorities td {border:1px solid black;vertical-align: top; padding: 8px;}
.n_weight{font-weight:normal;}
.not-found{color:red;}
p.error{color:red !important;margin:20px 0px;}
p.fi{margin:20px 0px;}
	</style>
</head>
<body class="test-page">
<%
Iterator variants = VariantRegistry.getInstance().getServices(EnumSiteFeature.CART_N_TABS).values().iterator();
while (variants.hasNext()) {
%>
	<% 
	Variant variant = (Variant) variants.next();
	SortedMap priorities = variant != null ? variant.getTabStrategyPriorities() : new TreeMap();
	if (priorities != null) {
	%>
	<h2 title="Strategy"><%= variant.getId() %></h2>
	<table class="priorities">
		<thead>
			<tr>
				<th class="text13">Primary priority</th>
				<th class="text13">Secondary priority</th>
				<th class="text13">Site Feature</th>
			</tr>
		</thead>
	<%
		Iterator it = priorities.entrySet().iterator();
		while (it.hasNext()) {
			SortedMap.Entry entry = (SortedMap.Entry) it.next();
			Integer p1 = (Integer) entry.getKey();
			SortedMap sp = (SortedMap) entry.getValue();
	%>
		<tr>
			<td class="text13" rowspan="<%= sp.size() %>"><%= p1.toString() %></td>
	<%
			Iterator it2 = sp.entrySet().iterator();
			while (it2.hasNext()) {
				SortedMap.Entry e2 = (SortedMap.Entry) it2.next();
				Integer p2 = (Integer) e2.getKey();
				CartTabStrategyPriority priority = (CartTabStrategyPriority) e2.getValue();
				EnumSiteFeature siteFeature = EnumSiteFeature.getEnum(priority.getSiteFeatureId());
	%>
			<td class="text13"><%= p2.toString() %></td>
			<td class="text13" title="<%= siteFeature.getTitle() %>"><%= siteFeature.getName() %></td>
		</tr>
	<%
			}
		}
	%>
	</table>
	<%
	}
	%>
<%
}
%>
    <p>To reload strategies visit the <a href="/test/smartstore/view_config.jsp">View Configuration Page</a></p>
</body>
</html>