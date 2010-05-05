<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="freshdirect" prefix="fd"%>
<%@page import="com.freshdirect.fdstore.FDCachedFactory"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.DateFormat"%>
<%!
class SkuEntry {
	String sku = null;
	String sap = null;
	String matdesc = null;
	String product = null;
	Date date = null;
	String age = null;
	boolean overridden = false;
	boolean unavailable = false;
}

class ProductEntry {
	ProductModel product = null;
	String fullName = null;
	Date date = null;
	String age = null;
}

DecimalFormat decimalFormat = new DecimalFormat("0.###");
DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
String asc = "&#9650;";
String desc = "&#9660;";
String unknown = "<span class=\"warning\">&lt;&lt;unknown&gt;&gt;</span>";

String formatDay(Date now, Date then) {
	double d = ((double) (now.getTime() - then.getTime())) / 1000. / 3600. / 24.;
	return decimalFormat.format(d);
}

String calcOrder(String orderBy, boolean orderAsc, String _for) {
	return "order=" + _for + "_" + (orderBy.equals(_for) && orderAsc ? "desc" : "asc"); 
}

String calcArrow(String orderBy, boolean orderAsc, String _for) {
	return orderBy.equals(_for) ? (orderAsc ? " " + asc : " " + desc) : "";
}

List<SkuEntry> calcSkuEntries(Map<String, Date> skus, Map<String, Date> overridden, Date now, final String orderBy, final boolean orderAsc) {
	List<SkuEntry> entries = new ArrayList<SkuEntry>(skus.size());
	for (Map.Entry<String, Date> entry : skus.entrySet()) {
		SkuEntry e = new SkuEntry();
		e.sku = entry.getKey();
		e.date = entry.getValue();
		e.age = formatDay(now, e.date);
		try {
			ErpProductModel product = ErpFactory.getInstance().getProduct(e.sku);
			ErpMaterialModel material = product.getProxiedMaterial();
			if (material != null) {
				e.sap = material.getSapId();
				e.matdesc = material.getDescription();
			}
		} catch (Exception e1) {
		}
		e.unavailable = true;
		SkuModel sku = (SkuModel) ContentFactory.getInstance().getContentNodeByKey(new ContentKey(FDContentTypes.SKU, entry.getKey()));
		if (sku != null) {
			e.unavailable = sku.isUnavailable();
			e.product = sku.getProductModel() != null ? sku.getProductModel().getContentKey().getId() : null;
		}
		e.overridden = overridden.containsKey(e.sku);
		entries.add(e);
	}
	Collections.sort(entries, new Comparator<SkuEntry>() {
		public int compare(SkuEntry e1, SkuEntry e2) {
			int asc = orderAsc ? 1 : -1;
			if (orderBy.equals("sku")) {
				return asc * e1.sku.compareTo(e2.sku);
			} else if (orderBy.equals("sap")) {
				if (e1.sap == null) {
					if (e2.sap == null) {
						return 0;
					} else {
						return asc;
					}
				} else {
					if (e2.sap == null) {
						return -asc; 
					} else {
						return asc * e1.sap.compareTo(e2.sap);
					}
				}
			} else if (orderBy.equals("matdesc")) {
				if (e1.matdesc == null) {
					if (e2.matdesc == null) {
						return 0;
					} else {
						return asc;
					}
				} else {
					if (e2.matdesc == null) {
						return -asc; 
					} else {
						return asc * e1.matdesc.compareTo(e2.sap);
					}
				}
			} else if (orderBy.equals("prod")) {
				if (e1.product == null) {
					if (e2.product == null) {
						return 0;
					} else {
						return asc;
					}
				} else {
					if (e2.product == null) {
						return -asc; 
					} else {
						return asc * e1.product.compareTo(e2.product);
					}
				}
			} else if (orderBy.equals("date")) {
					return asc * e1.date.compareTo(e2.date);
			} else if (orderBy.equals("age")) {
				return asc * e2.date.compareTo(e1.date);
			} else if (orderBy.equals("unav")) {
				if (e1.unavailable) {
					if (e2.unavailable) {
						return asc * e1.sku.compareTo(e2.sku);
					} else {
						return asc;
					}
				} else {
					if (e2.unavailable) {
						return -asc;	
					} else {
						return asc * e1.sku.compareTo(e2.sku);
					}
				}
			} else
				return asc * e1.sku.compareTo(e2.sku);
		}
	});
	return entries;
}

List<ProductEntry> calcProductEntries(Map<ProductModel, Date> products, Date now, final String orderBy, final boolean orderAsc) {
	List<ProductEntry> entries = new ArrayList<ProductEntry>(products.size());
	for (Map.Entry<ProductModel, Date> entry : products.entrySet()) {
		ProductEntry e = new ProductEntry();
		e.product = entry.getKey();
		e.fullName = entry.getKey().getFullName();
		e.date = entry.getValue();
		e.age = formatDay(now, e.date);
		entries.add(e);
	}
	Collections.sort(entries, new Comparator<ProductEntry>() {
		public int compare(ProductEntry e1, ProductEntry e2) {
			int asc = orderAsc ? 1 : -1;
			if (orderBy.equals("prod")) {
				return asc * e1.product.getContentKey().getId().compareTo(e2.product.getContentKey().getId());
			} else if (orderBy.equals("fullName")) {
				return asc * e1.fullName.compareTo(e2.fullName);
			} else if (orderBy.equals("date")) {
				return asc * e1.date.compareTo(e2.date);
			} else if (orderBy.equals("age")) {
				return asc * e2.date.compareTo(e1.date);
			} else
				return asc * e1.product.getContentKey().getId().compareTo(e2.product.getContentKey().getId());
		}
	});
	return entries;
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
<%@page import="com.freshdirect.erp.model.ErpMaterialModel"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.freshdirect.erp.ErpFactory"%>
<%@page import="com.freshdirect.erp.model.ErpProductModel"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.Collection"%>
<%@page import="com.freshdirect.erp.model.ErpMaterialInfoModel"%>
<%@page import="com.freshdirect.erp.model.ErpProductInfoModel"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.freshdirect.fdstore.FDStoreProperties"%><html>
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

.data, .search-results {
	width: auto;
	margin: 0px;
	padding: 0px;
}

.data td,.data th {
	border: 1px solid black;
	padding: 4px;
}

.search-results td, .search-results th {
	padding: 2px;
}

.data th, .search-results th, .bold {
	font-weight: bold;
}

.regular {
	font-weight: normal;
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
<body class="text12"><%
if (FDStoreProperties.isAnnotationMode()) {
	%>
<div id="overDiv" style="position: absolute; visibility: hidden; z-index: 10000;"></div>
<script language="JavaScript" src="/assets/javascript/overlib_mini.js"></script><%	
}

String searchTerm = request.getParameter("searchTerm");
if (searchTerm == null)
	searchTerm = "";
String searchOption = request.getParameter("searchOption");
if (searchOption == null || (!searchOption.equals("sku") && !searchOption.equals("sap")))
	searchOption = "sku";

boolean showProducts = request.getParameter("products") != null;
String requestUri = request.getRequestURI() + "?" + (showProducts ? "products=1" : (searchTerm.length() > 0 ? "searchTerm=" + searchTerm : "") + (searchOption.equals("sap") ? "&searchOption=sap" : "")) + "&";
if (request.getParameter("reload") != null) {
	ContentFactory.getInstance().refreshNewAndBackCache();
	response.sendRedirect(request.getRequestURI() + (showProducts ? "?products=1" : ""));
	return;
} else if (request.getParameter("refreshViews") != null) {
	FDCachedFactory.refreshNewAndBackViews();
	response.sendRedirect(request.getRequestURI() + (showProducts ? "?products=1" : ""));
	return;
}

String order = request.getParameter("order");
if (order == null)
	order = showProducts ? "prod_asc" : "sku_asc";

String orderBy = showProducts ? "product" : "sku";
boolean orderAsc = true;
{
	String[] split = order.split("_");
	if (split.length >= 2) {
		String by = split[0];
		if (showProducts) {
			if (by.equals("prod") || by.equals("fullName") || by.equals("date") || by.equals("age")) {
				orderBy = by;
			}
		} else {
			if (by.equals("sku") || by.equals("sap") || by.equals("matdesc") || by.equals("prod") ||by.equals("date") || by.equals("age") || by.equals("unav")) {
				orderBy = by;
			}
		}
		String asc = split[1];
		if (asc.equals("asc")) {
			orderAsc = true;
		} else if (asc.equals("desc")) {
			orderAsc = false;
		}
	}
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
				<p class="lower-space">To view the history of a SKU, specify its SKU code or SAP ID</p>
				<p class="upper-space"><input type="text" name="searchTerm" value="<%= searchTerm %>"> <input type="submit" value="Find"></p>
				<p class="regular lower-space">
					<input type="radio" name="searchOption" value="sap"<% if (searchOption.equals("sap")) { %> checked="checked"<% } %>> SAP ID
					<input type="radio" name="searchOption" value="sku"<% if (searchOption.equals("sku")) { %> checked="checked"<% } %>> SKU code (Web ID)
					</p>
				<p class="upper-space">or click on either SKU in the table(s) below</p>
				</td>
			</tr>
			<% } %>
		</table>
		</td>
	</tr>
</table>
</div>
<div class="content-body">
	<%
	String nullValue = "<span style=\"color: gray;\">(null)</null>";
	Date now = new Date();
	Map<String, Date> newSkus = FDCachedFactory.getNewSkus();
	Map<String, Date> backInStockSkus = FDCachedFactory.getBackInStockSkus();
	if (!showProducts) {
	if (searchTerm.length() > 0) {
	%>
	<div class="section-break">
	<%
		String skuCode;
		if (searchOption.equals("sku")) {
			skuCode = searchTerm;
		} else if (searchOption.equals("sap")) {
			skuCode = null;
			Collection results = ErpFactory.getInstance().findProductsBySapId(searchTerm);
			// results.add(ErpFactory.getInstance().findProductBySku("VEG0058758"));
			if (results.size() == 0) {
	%>
	<h2><%= searchTerm %> History</h2>
		<div class="bold not-found">No such material found.</div>
	<%
			} else if (results.size() == 1) {
				ErpProductInfoModel product = (ErpProductInfoModel) results.iterator().next();
				skuCode = product.getSkuCode();
			} else {
	%>
	<h2>Multiple SKUs found, please choose one</h2>
		<table class="search-results">
	    	<tr><th class="text12">SKU</th><th class="text12">Description</th></tr><%
				Iterator it = results.iterator();
				while (it.hasNext()) {
					ErpProductInfoModel product = (ErpProductInfoModel) it.next();
	%>
			<tr><td class="text12"><a href="<%= request.getRequestURI() %>?searchTerm=<%= product.getSkuCode() %>"><%= product.getSkuCode() %></a></td><td class="text12"><%= product.getDescription() %></td></tr>
	<%
				}
			}
	%>
		</table><%
		} else {
			skuCode = null;
		}
		if (skuCode != null) {
	%>
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
	}
	 %>
	<div class="section-break">
	<h2>New SKUs</h2>
	<%
	Map<String, Date> newOverridden = FDCachedFactory.getOverriddenNewSkus();
	if (!newSkus.isEmpty()) {
	 %>
	<table class="data">
		<tr>
			<th class="text12"><a href="<%= requestUri + calcOrder(orderBy, orderAsc, "sku") %>">SKU (Web ID)<%= calcArrow(orderBy, orderAsc, "sku") %></a></th>
			<th class="text12"><a href="<%= requestUri + calcOrder(orderBy, orderAsc, "sap") %>">SAP ID<%= calcArrow(orderBy, orderAsc, "sap") %></a></th>
			<th class="text12"><a href="<%= requestUri + calcOrder(orderBy, orderAsc, "matdesc") %>">Material Description<%= calcArrow(orderBy, orderAsc, "matdesc") %></a></th> 
			<th class="text12"><a href="<%= requestUri + calcOrder(orderBy, orderAsc, "prod") %>">Product ID<%= calcArrow(orderBy, orderAsc, "prod") %></a></th> 
			<th class="text12"><a href="<%= requestUri + calcOrder(orderBy, orderAsc, "date") %>">Date<%= calcArrow(orderBy, orderAsc, "date") %></a></th>
			<th class="text12"><a href="<%= requestUri + calcOrder(orderBy, orderAsc, "age") %>">Age<%= calcArrow(orderBy, orderAsc, "age") %></a></th>
			<th class="text12"><a href="<%= requestUri + calcOrder(orderBy, orderAsc, "unav") %>">Unavailable<%= calcArrow(orderBy, orderAsc, "unav") %></a></th>
			<% if (FDStoreProperties.isAnnotationMode()) { %><th class="text12"></th><% } %>
		</tr>
		<%
		List<SkuEntry> entries = calcSkuEntries(newSkus, newOverridden, now, orderBy, orderAsc);
		for (SkuEntry entry: entries) {
		 %>
		<%
			entry.sap = entry.sap != null ? entry.sap : unknown;
			entry.matdesc = entry.matdesc != null ? entry.matdesc : unknown;
		%>
		<tr>
			<td class="text12<%= entry.overridden ? " overridden" : "" %>"><a href="<%= request.getRequestURI() + "?searchTerm=" + entry.sku %>"><%= entry.sku %></a></td>
			<td class="text12<%= entry.overridden ? " overridden" : "" %>"><% if (entry.sap != null) { %><a href="<%= request.getRequestURI() + "?searchTerm=" + entry.sap + "&searchOption=sap" %>"><%= entry.sap %></a><% } %></td>
			<td class="text12<%= entry.overridden ? " overridden" : "" %>"><%= entry.matdesc %></td>
			<td class="text12<%= entry.overridden ? " overridden" : "" %>"><% if (entry.product != null) { %><%= entry.product %><% } %></td>
			<td class="text12<%= entry.overridden ? " overridden" : "" %>"><%= dateFormat.format(entry.date) %></td>
			<td class="text12<%= entry.overridden ? " overridden" : "" %>"><%= entry.age %></td>
			<td class="text12<%= entry.overridden ? " overridden" : "" %>"><%= entry.unavailable %></td>
			<% if (FDStoreProperties.isAnnotationMode()) { %><td class="text12<%= entry.overridden ? " overridden" : "" %>"><% if (entry.overridden) { %><a href="<%= FDStoreProperties.getAnnotationErpsy() %>/product/product_view.jsp?skuCode=<%= entry.sku %>">ERPSy-Link</a><% } %></td><% } %>
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
	<% if (!FDStoreProperties.isAnnotationMode()) { %><div class="warning">Erspy-Link feature is available only if annotation mode is enabled. Please consult with the administrators about the details.</div><% } %>
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
	Map<String, Date> backOverridden = FDCachedFactory.getOverdiddenBackInStockSkus();
	if (!backInStockSkus.isEmpty()) {
	 %>
	<table class="data">
		<tr>
			<th class="text12"><a href="<%= requestUri + calcOrder(orderBy, orderAsc, "sku") %>">SKU (Web ID)<%= calcArrow(orderBy, orderAsc, "sku") %></a></th>
			<th class="text12"><a href="<%= requestUri + calcOrder(orderBy, orderAsc, "sap") %>">SAP ID<%= calcArrow(orderBy, orderAsc, "sap") %></a></th>
			<th class="text12"><a href="<%= requestUri + calcOrder(orderBy, orderAsc, "matdesc") %>">Material Description<%= calcArrow(orderBy, orderAsc, "matdesc") %></a></th> 
			<th class="text12"><a href="<%= requestUri + calcOrder(orderBy, orderAsc, "prod") %>">Product ID<%= calcArrow(orderBy, orderAsc, "prod") %></a></th> 
			<th class="text12"><a href="<%= requestUri + calcOrder(orderBy, orderAsc, "date") %>">Date<%= calcArrow(orderBy, orderAsc, "date") %></a></th>
			<th class="text12"><a href="<%= requestUri + calcOrder(orderBy, orderAsc, "age") %>">Age<%= calcArrow(orderBy, orderAsc, "age") %></a></th>
			<th class="text12"><a href="<%= requestUri + calcOrder(orderBy, orderAsc, "unav") %>">Unavailable<%= calcArrow(orderBy, orderAsc, "unav") %></a></th>
			<% if (FDStoreProperties.isAnnotationMode()) { %><th class="text12"></th><% } %>
		</tr>
		<%
		List<SkuEntry> entries = calcSkuEntries(backInStockSkus, backOverridden, now, orderBy, orderAsc);
		for (SkuEntry entry: entries) {
		 %>
		<%
			entry.sap = entry.sap != null ? entry.sap : unknown;
			entry.matdesc = entry.matdesc != null ? entry.matdesc : unknown;
		%>
		<tr>
			<td class="text12<%= entry.overridden ? " overridden" : "" %>"><a href="<%= request.getRequestURI() + "?searchTerm=" + entry.sku %>"><%= entry.sku %></a></td>
			<td class="text12<%= entry.overridden ? " overridden" : "" %>"><% if (entry.sap != null) { %><a href="<%= request.getRequestURI() + "?searchTerm=" + entry.sap + "&searchOption=sap" %>"><%= entry.sap %></a><% } %></td>
			<td class="text12<%= entry.overridden ? " overridden" : "" %>"><%= entry.matdesc %></td>
			<td class="text12<%= entry.overridden ? " overridden" : "" %>"><% if (entry.product != null) { %><%= entry.product %><% } %></td>
			<td class="text12<%= entry.overridden ? " overridden" : "" %>"><%= dateFormat.format(entry.date) %></td>
			<td class="text12<%= entry.overridden ? " overridden" : "" %>"><%= entry.age %></td>
			<td class="text12<%= entry.overridden ? " overridden" : "" %>"><%= entry.unavailable %></td>
			<% if (FDStoreProperties.isAnnotationMode()) { %><td class="text12<%= entry.overridden ? " overridden" : "" %>"><% if (entry.overridden) { %><a href="<%= FDStoreProperties.getAnnotationErpsy() %>/product/product_view.jsp?skuCode=<%= entry.sku %>">ERPSy-Link</a><% } %></td><% } %>
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
	<% if (!FDStoreProperties.isAnnotationMode()) { %><div class="warning">Erspy-Link feature is available only if annotation mode is enabled. Please consult with the administrators about the details.</div><% } %>
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
		List<ProductEntry> entries = calcProductEntries(newProducts, now, orderBy, orderAsc);
	 %>
	<table class="data">
		<tr>
			<th class="text12"><a href="<%= requestUri + calcOrder(orderBy, orderAsc, "prod") %>">Product ID<%= calcArrow(orderBy, orderAsc, "prod") %></a></th>
			<th class="text12"><a href="<%= requestUri + calcOrder(orderBy, orderAsc, "fullName") %>">Full Name<%= calcArrow(orderBy, orderAsc, "fullName") %></a></th>
			<th class="text12"><a href="<%= requestUri + calcOrder(orderBy, orderAsc, "date") %>">Date<%= calcArrow(orderBy, orderAsc, "date") %></a></th>
			<th class="text12"><a href="<%= requestUri + calcOrder(orderBy, orderAsc, "age") %>">Age<%= calcArrow(orderBy, orderAsc, "age") %></a></th>
		</tr>
		<%
		for (ProductEntry entry : entries) {
			List<SkuModel> skus = entry.product.getSkus();
			Iterator<SkuModel> it = skus.iterator();
			while (it.hasNext()) {
				SkuModel sku = it.next();
				if (sku.isUnavailable()) {
					it.remove();
					continue;
				}
				if (!newSkus.containsKey(sku.getContentKey().getId()))
					it.remove();
			}
			Collections.sort(skus, new Comparator<SkuModel>() {
				public int compare(SkuModel o1, SkuModel o2) {
					return o1.getContentKey().getId().compareTo(o2.getContentKey().getId());
				}
			});
			String href = "#";
			String onclick = null;
			String onmouseover = null;
			String onmouseout = null;
			if (skus.size() == 1) {
				href = request.getRequestURI() + "?searchTerm=" + skus.get(0).getContentKey().getId();
			} else if (skus.size() > 0) {
				StringBuilder content = new StringBuilder();
				content.append("<table>");
				for (SkuModel sku : skus) {
					content.append("<tr>");
					content.append("<td>");
					content.append("<a href=&quot;");
					content.append(request.getRequestURI());
					content.append("?searchTerm=");
					content.append(sku.getContentKey().getId());
					content.append("&quot;>");
					content.append(sku.getContentKey().getId());
					content.append("</a>");
					content.append("</td>");
					content.append("</tr>");
				}
				content.append("</table>");
				onclick = "return overlib('" + content + "', STICKY, CLOSECLICK, CAPTION, 'Multiple SKUs&nbsp;&nbsp;&nbsp;', WIDTH, 240);";
			} else {
				onmouseover = "return overlib('<span class=&quot;error&quot;>We\\'re sorry but no available matching SKU found.</span><BR><BR>"
						+ "You may have to refresh the cache to make the products\\' state synchronized with SKUs.', WIDTH, 240);";
				onclick = "return false;";
				onmouseout = "return nd();";
			}
		 %>
		<tr>
			<td class="text12">
				<a style="z-index: 9999;"<% if (href != null) { %> href="<%= href %>"<% } %><% if (onclick != null) { %> onclick="<%= onclick %>"<% } %>
					<% if (onmouseover != null) { %> onmouseover="<%= onmouseover %>"<% } %><% if (onmouseout != null) { %> onmouseout="<%= onmouseout %>"<% } %>>
					<%= entry.product.getContentKey().getId() %>
				</a>
			</td>
			<td class="text12"><%= entry.fullName %></td>
			<td class="text12"><%= dateFormat.format(entry.date) %></td>
			<td class="text12"><%= entry.age %></td>
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
		List<ProductEntry> entries = calcProductEntries(backInStockProducts, now, orderBy, orderAsc);
	 %>
	<table class="data">
		<tr>
			<th class="text12"><a href="<%= requestUri + calcOrder(orderBy, orderAsc, "prod") %>">Product ID<%= calcArrow(orderBy, orderAsc, "prod") %></a></th>
			<th class="text12"><a href="<%= requestUri + calcOrder(orderBy, orderAsc, "fullName") %>">Full Name<%= calcArrow(orderBy, orderAsc, "fullName") %></a></th>
			<th class="text12"><a href="<%= requestUri + calcOrder(orderBy, orderAsc, "date") %>">Date<%= calcArrow(orderBy, orderAsc, "date") %></a></th>
			<th class="text12"><a href="<%= requestUri + calcOrder(orderBy, orderAsc, "age") %>">Age<%= calcArrow(orderBy, orderAsc, "age") %></a></th>
		</tr>
		<%
		for (ProductEntry entry : entries) {
			List<SkuModel> skus = entry.product.getSkus();
			Iterator<SkuModel> it = skus.iterator();
			while (it.hasNext()) {
				SkuModel sku = it.next();
				if (sku.isUnavailable()) {
					it.remove();
					continue;
				}
				if (!backInStockSkus.containsKey(sku.getContentKey().getId()))
					it.remove();
			}
			Collections.sort(skus, new Comparator<SkuModel>() {
				public int compare(SkuModel o1, SkuModel o2) {
					return o1.getContentKey().getId().compareTo(o2.getContentKey().getId());
				}
			});
			String href = "#";
			String onclick = null;
			String onmouseover = null;
			String onmouseout = null;
			if (skus.size() == 1) {
				href = request.getRequestURI() + "?searchTerm=" + skus.get(0).getContentKey().getId();
			} else if (skus.size() > 0) {
				StringBuilder content = new StringBuilder();
				content.append("<table>");
				for (SkuModel sku : skus) {
					content.append("<tr>");
					content.append("<td>");
					content.append("<a href=&quot;");
					content.append(request.getRequestURI());
					content.append("?searchTerm=");
					content.append(sku.getContentKey().getId());
					content.append("&quot;>");
					content.append(sku.getContentKey().getId());
					content.append("</a>");
					content.append("</td>");
					content.append("</tr>");
				}
				content.append("</table>");
				onclick = "return overlib('" + content + "', STICKY, CLOSECLICK, CAPTION, 'Multiple SKUs&nbsp;&nbsp;&nbsp;', WIDTH, 240);";
			} else {
				onmouseover = "return overlib('<span class=&quot;error&quot;>We\\'re sorry but no available matching SKU found.</span><BR><BR>"
						+ "You may have to refresh the cache to make the products\\' state synchronized with SKUs.', WIDTH, 240);";
				onclick = "return false;";
				onmouseout = "return nd();";
			}
		 %>
		<tr>
			<td class="text12">
				<a style="z-index: 9999;"<% if (href != null) { %> href="<%= href %>"<% } %><% if (onclick != null) { %> onclick="<%= onclick %>"<% } %>
					<% if (onmouseover != null) { %> onmouseover="<%= onmouseover %>"<% } %><% if (onmouseout != null) { %> onmouseout="<%= onmouseout %>"<% } %>>
					<%= entry.product.getContentKey().getId() %>
				</a>
			</td>
			<td class="text12"><%= entry.fullName %></td>
			<td class="text12"><%= dateFormat.format(entry.date) %></td>
			<td class="text12"><%= entry.age %></td>
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