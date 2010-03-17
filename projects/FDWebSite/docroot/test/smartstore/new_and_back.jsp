<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="freshdirect" prefix="fd"%>
<%@page import="com.freshdirect.fdstore.FDCachedFactory"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.DateFormat"%>
<%!
DecimalFormat decimalFormat = new DecimalFormat("0.###");

String formatDay(Date now, Date then) {
	double d = ((double) (now.getTime() - then.getTime())) / 1000. / 3600. / 24.;
	return decimalFormat.format(d);
}
 %>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.util.List"%>
<%@page import="com.freshdirect.erp.SkuAvailabilityHistory"%>
<%@page import="java.text.SimpleDateFormat"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>NEW AND BACK IN STOCK PRODUCTS TEST PAGE</title>
<%@ include file="../../shared/template/includes/style_sheet_detect.jspf"%>
<style type="text/css">
body {
	margin: 20px 60px;
	color: #333333;
	background-color: #fff;
	height: auto;
}

input {
	font-weight: normal;
}

p {
	margin: 0px;
	padding: 0px;
}

a {
	color: #336600;
}

a:VISITED {
	color: #336600;
}

table {
	border-collapse: collapse;
	border-spacing: 0px;
	width: 100%;
}

.form-header table td p.lower-space {
	padding-bottom: 4px;
}

.form-header table td p.upper-space {
	padding-top: 4px;
}

.form-header table td {
	padding-right: 20px;
	vertical-align: top;
}

.content-body {
	padding-top: 40px;
	padding-bottom: 40px;
}

.error, .not-found {
	color: red;
}

.warning {
	color: #FF6633;
}

.comment {
	color: gray;
	font-weight: normal;
	font-style: italic;
}

.data {
	width: auto;
	margin: 0px;
	padding: 0px;
}

.data td,.data th {
	border: 1px solid black;
	padding: 4px;
}

.data th, .bold {
	font-weight: bold;
}

.section-break {
	margin-bottom: 40px;
}

.right {
	text-align: right;
}

.center {
	text-align: center;
}

.no-wrap {
	white-space: nowrap;
}
</style>
</head>
<body class="text12">
<%
String skuCode = request.getParameter("skuCode");
if (skuCode == null)
	skuCode = "";
 %>
<form method="get" action="<%= request.getRequestURI() %>" id="form">
<div class="form-header" class="rec-chooser title14">
<table>
	<tr>
		<td class="text12" colspan="2">
		<table style="width: auto;">
			<tr>
				<td class="text12 bold">
				<p class="lower-space">Specify a SKU code</p>
				<p><input type="text" name="skuCode" value="<%= skuCode %>"></p>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="text12">
			<p class="lower-space upper-space">
				<input type="submit" value="Submit">
			</p>
		</td>
	</tr>
</table>
</div>
<div class="content-body">
	<%
	DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm");
	String nullValue = "<span style=\"color: gray;\">(null)</null>";
	if (skuCode.length() > 0) {
	%>
	<div class="section-break">
	<h2><%= skuCode %> History</h2>
	<%
		List<SkuAvailabilityHistory> history = FDCachedFactory.getSkuAvailabilityHistory(skuCode);
		if (history.size() > 0) {
	 %>
	<table class="data">
		<tr>
			<th class="text12">Version</th>
			<th class="text12">Event Date</th>
			<th class="text12">Unavailability Status</th>
		</tr>
		<%
		for (SkuAvailabilityHistory entry : history) {
		 %>
		<tr>
			<td class="text12"><%= entry.getVersion() %></td>
			<td class="text12"><%= dateFormat.format(entry.getEventDate()) %></td>
			<td class="text12"><%= entry.getUnavailabilityStatus() == null ? nullValue : entry.getUnavailabilityStatus() %></td>
		</tr>
		<%
		}
		 %>
	</table>
	<%
		} else {
	 %>
	<div class="bold not-found">No such SKU found.</div>
	<%
		}
	 %>
	</div>
	<%
	}
	 %>
	<div class="section-break">
	<h2>New SKUs</h2>
	<%
	Date now = new Date();
	Map<String, Date> newSkus = FDCachedFactory.getNewSkusTest();
	if (!newSkus.isEmpty()) {
	 %>
	<table class="data">
		<tr>
			<th class="text12">SKU</th>
			<th class="text12">Date</th>
			<th class="text12">Age</th>
		</tr>
		<%
		for (Map.Entry<String, Date> entry : newSkus.entrySet()) {
		 %>
		<tr>
			<td class="text12"><%= entry.getKey() %></td>
			<td class="text12"><%= dateFormat.format(entry.getValue()) %></td>
			<td class="text12"><%= formatDay(now, entry.getValue()) %></td>
		</tr>
		<%
		}
		 %>
	</table>
	<%
	} else {
	 %>
	<div>No new SKUs.</div>
	<%
	}
	%>
	</div>
	<div class="section-break">
	<h2>Back-in-Stock SKUs</h2>
	<%
	Map<String, Date> backInStockSkus = FDCachedFactory.getBackInStockSkusTest();
	if (!backInStockSkus.isEmpty()) {
	 %>
	<table class="data">
		<tr>
			<th class="text12">SKU</th>
			<th class="text12">Date</th>
			<th class="text12">Age</th>
		</tr>
		<%
		for (Map.Entry<String, Date> entry : backInStockSkus.entrySet()) {
		 %>
		<tr>
			<td class="text12"><%= entry.getKey() %></td>
			<td class="text12"><%= dateFormat.format(entry.getValue()) %></td>
			<td class="text12"><%= formatDay(now, entry.getValue()) %></td>
		</tr>
		<%
		}
		 %>
	</table>
	<%
	} else {
	 %>
	<div>No back in stock SKUs.</div>
	<%
	}
	 %>
	</div>
</div>
</form>
</body>
</html>