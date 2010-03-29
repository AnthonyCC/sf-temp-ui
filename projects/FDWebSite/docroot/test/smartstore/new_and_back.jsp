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
DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

String formatDay(Date now, Date then) {
	double d = ((double) (now.getTime() - then.getTime())) / 1000. / 3600. / 24.;
	return decimalFormat.format(d);
}
 %>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.util.List"%>
<%@page import="com.freshdirect.erp.SkuAvailabilityHistory"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@page import="com.freshdirect.fdstore.content.ProductModel"%>
<%@page import="com.freshdirect.fdstore.content.SkuModel"%>
<%@page import="com.freshdirect.cms.ContentKey"%>
<%@page import="com.freshdirect.cms.fdstore.FDContentTypes"%>
<%@page import="java.util.Collections"%>
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

.lower-space {
	padding-bottom: 4px;
}

.lower-space2 {
	padding-bottom: 20px;
}

.upper-space {
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
	color: #FF6633 !important;
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
.overridden {
	background-color: #FF9999;
}

.legend {
	margin: 4px;
}

.legend td {
	margin: 4px;
}

.legend .color {
	width: 8px;
	height: 8px;
}
</style>
</head>
<body class="text12">
<%
String skuCode = request.getParameter("skuCode");
if (skuCode == null)
	skuCode = "";
boolean showProducts = request.getParameter("products") != null;
if (request.getParameter("reload") != null) {
	ContentFactory.getInstance().refreshNewAndBackCache();
	response.sendRedirect(request.getRequestURI() + (showProducts ? "?products=1" : ""));
	return;
} else if (request.getParameter("refreshViews") != null) {
	FDCachedFactory.refreshNewAndBackViews();
	response.sendRedirect(request.getRequestURI() + (showProducts ? "?products=1" : ""));
	return;
}
 %>
<form method="get" action="<%= request.getRequestURI() %>" id="form">
<div class="form-header">
<table>
	<tr>
		<td class="text12" colspan="2">
		<table style="width: auto;">
			<tr>
				<td class="text12 bold lower-space2">
				<% if (!showProducts) { %>
				<p class="upper-space lower-space"><a href="<%= request.getRequestURI() + "?products=1" %>">Switch to product view</a></p>
				<% } else { %>
				<p class="upper-space lower-space"><a href="<%= request.getRequestURI() %>">Switch to SKU view</a></p>
				<% } %>
				</td>
				<td class="text12 bold lower-space2">
				<% if (!showProducts) { %>
				<p class="upper-space lower-space"><a class="warning" href="<%= request.getRequestURI() + "?refreshViews=1" %>">Refresh materialized views</a> <span class="error text12" style="font-weight: bold;">&lt;&mdash; !!! USE WITH CARE !!!</span></p>
				<% } else { %>
				<p class="upper-space lower-space"><a href="<%= request.getRequestURI() + "?reload=1&products=1" %>">Reload cache</a></p>
				<% } %>
				</td>
			</tr>
		    <% if (!showProducts) { %>
			<tr>
				<td class="text12 bold">
				<p class="lower-space">To view the history of a SKU,<br>specify its SKU code</p>
				<p><input type="text" name="skuCode" value="<%= skuCode %>"></p>
				<p class="upper-space">or click on either SKU in the table(s) below</p>
				</td>
			</tr>
			<% } %>
		</table>
		</td>
	</tr>
	<% if (!showProducts) { %>
	<tr>
		<td class="text12">
			<p class="lower-space upper-space">
				<input type="submit" value="Submit">
			</p>
		</td>
	</tr>
	<% } %>
</table>
</div>
<div class="content-body">
	<%
	String nullValue = "<span style=\"color: gray;\">(null)</null>";
	Date now = new Date();
	if (!showProducts) {
	if (skuCode.length() > 0) {
	%>
	<div class="section-break">
	<h2><%= skuCode %> History</h2>
	<%
		List<SkuAvailabilityHistory> history = FDCachedFactory.getSkuAvailabilityHistory(skuCode);
		Collections.reverse(history);
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
	Map<String, Date> newSkus = FDCachedFactory.getNewSkus();
	Map<String, Date> newOverridden = FDCachedFactory.getOverriddenNewSkus();
	if (!newSkus.isEmpty()) {
	 %>
	<table class="data">
		<tr>
			<th class="text12">SKU</th>
			<th class="text12">Date</th>
			<th class="text12">Age</th>
			<th class="text12">Unavailable</th>
		</tr>
		<%
		for (Map.Entry<String, Date> entry : newSkus.entrySet()) {
			boolean overridden = newOverridden.containsKey(entry.getKey());
		 %>
		<tr>
			<td class="text12<%= overridden ? " overridden" : "" %>"><a href="<%= request.getRequestURI() + "?skuCode=" + entry.getKey() %>"><%= entry.getKey() %></a></td>
			<td class="text12<%= overridden ? " overridden" : "" %>"><%= dateFormat.format(entry.getValue()) %></td>
			<td class="text12<%= overridden ? " overridden" : "" %>"><%= formatDay(now, entry.getValue()) %></td>
			<%
			boolean unavailable = true;
			SkuModel sku = (SkuModel) ContentFactory.getInstance().getContentNodeByKey(new ContentKey(FDContentTypes.SKU, entry.getKey()));
			if (sku != null)
				unavailable = sku.isUnavailable();			
			 %>
			<td class="text12<%= overridden ? " overridden" : "" %>"><%= unavailable %></td>
		</tr>
		<%
		}
		 %>
	</table>
	<table class="legend">
		<tr>
			<td class="text12 color"><div class="overridden color">&nbsp;</div></td>
			<td class="text12">&nbsp;&ndash;&nbsp;manually overridden</td>
		</tr>
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
	Map<String, Date> backInStockSkus = FDCachedFactory.getBackInStockSkus();
	Map<String, Date> backOverridden = FDCachedFactory.getOverdiddenBackInStockSkus();
	if (!backInStockSkus.isEmpty()) {
	 %>
	<table class="data">
		<tr>
			<th class="text12">SKU</th>
			<th class="text12">Date</th>
			<th class="text12">Age</th>
			<th class="text12">Unavailable</th>
		</tr>
		<%
		for (Map.Entry<String, Date> entry : backInStockSkus.entrySet()) {
			boolean overridden = backOverridden.containsKey(entry.getKey());
		 %>
		<tr>
			<td class="text12<%= overridden ? " overridden" : "" %>"><a href="<%= request.getRequestURI() + "?skuCode=" + entry.getKey() %>"><%= entry.getKey() %></a></td>
			<td class="text12<%= overridden ? " overridden" : "" %>"><%= dateFormat.format(entry.getValue()) %></td>
			<td class="text12<%= overridden ? " overridden" : "" %>"><%= formatDay(now, entry.getValue()) %></td>
			<%
			boolean unavailable = true;
			SkuModel sku = (SkuModel) ContentFactory.getInstance().getContentNodeByKey(new ContentKey(FDContentTypes.SKU, entry.getKey()));
			if (sku != null)
				unavailable = sku.isUnavailable();			
			 %>
			<td class="text12<%= overridden ? " overridden" : "" %>"><%= unavailable %></td>
		</tr>
		<%
		}
		 %>
	</table>
	<table class="legend">
		<tr>
			<td class="text12 color"><div class="overridden color">&nbsp;</div></td>
			<td class="text12">&nbsp;&ndash;&nbsp;manually overridden</td>
		</tr>
	</table>
	<%
	} else {
	 %>
	<div>No back in stock SKUs.</div>
	<%
	}
	 %>
	</div>
	<%
	} else { // !showProducts
	 %>
	<div class="section-break">
	<h2>New Products</h2>
	<%
	Map<ProductModel, Date> newProducts = ContentFactory.getInstance().getNewProducts(Integer.MAX_VALUE);
	if (!newProducts.isEmpty()) {
	 %>
	<table class="data">
		<tr>
			<th class="text12">Product Id</th>
			<th class="text12">Date</th>
			<th class="text12">Age</th>
		</tr>
		<%
		for (Map.Entry<ProductModel, Date> entry : newProducts.entrySet()) {
		 %>
		<tr>
			<td class="text12"><%= entry.getKey().getContentName() %></td>
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
	<div>No new products.</div>
	<%
	}
	 %>
	</div>
	<div class="section-break">
	<h2>Back-in-Stock Products</h2>
	<%
	Map<ProductModel, Date> backInStockProducts = ContentFactory.getInstance().getBackInStockProducts(Integer.MAX_VALUE);
	if (!backInStockProducts.isEmpty()) {
	 %>
	<table class="data">
		<tr>
			<th class="text12">Product Id</th>
			<th class="text12">Date</th>
			<th class="text12">Age</th>
		</tr>
		<%
		for (Map.Entry<ProductModel, Date> entry : backInStockProducts.entrySet()) {
		 %>
		<tr>
			<td class="text12"><%= entry.getKey().getContentName() %></td>
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
	<div>No back in stock products.</div>
	<%
	}
	 %>
	</div>
	<%
	} // !showProducts
	 %>
</div>
</form>
</body>
</html>