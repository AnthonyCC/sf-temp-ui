<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.SortedMap"%>
<%@page import="java.util.TreeMap"%>
<%@page import="com.freshdirect.cms.ContentNodeI"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@page import="com.freshdirect.fdstore.content.ContentNodeModel"%>
<%@page import="com.freshdirect.fdstore.content.ProductModel"%>
<%@page import="com.freshdirect.fdstore.customer.FDCartLineI"%>
<%@page import="com.freshdirect.fdstore.customer.FDCartLineModel"%>
<%@page import="com.freshdirect.fdstore.customer.FDCustomerManager"%>
<%@page import="com.freshdirect.fdstore.customer.FDIdentity"%>
<%@page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@page import="com.freshdirect.fdstore.util.EnumSiteFeature"%>
<%@page import="com.freshdirect.fdstore.util.URLGenerator"%>
<%@page import="com.freshdirect.mail.EmailUtil"%>
<%@page import="com.freshdirect.smartstore.CartTabRecommender"%>
<%@page import="com.freshdirect.smartstore.CartTabStrategyPriority"%>
<%@page import="com.freshdirect.smartstore.RecommendationService"%>
<%@page import="com.freshdirect.smartstore.SessionInput"%>
<%@page import="com.freshdirect.smartstore.Variant"%>
<%@page import="com.freshdirect.smartstore.fdstore.CohortSelector"%>
<%@page import="com.freshdirect.smartstore.fdstore.FDStoreRecommender"%>
<%@page import="com.freshdirect.smartstore.fdstore.SmartStoreUtil"%>
<%@page import="com.freshdirect.smartstore.fdstore.VariantSelectorFactory"%>
<%@page import="com.freshdirect.smartstore.ymal.YmalUtil"%>
<%@page import="com.freshdirect.test.TestSupport"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ taglib uri="freshdirect" prefix="fd"%>
<%@ taglib uri='logic' prefix='logic' %>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>VIEW PRODUCT NEWNESS</title>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<style type="text/css">
table{border-collapse:collapse;border-spacing:0px;}
.products {width:auto;margin:0px;padding:0px;}
.products td, .products th {border:1px solid black;padding:4px;}
.products th {font-weight: bold;}
.right {text-align: right;}
	</style>
</head>
<body>
	<table class="products">
		<thead><tr>
			<th class="text12">Product</th>
			<th class="text12 right">Newness</th>
		</tr></thead>
		<%
			Iterator it = ContentFactory.getInstance().getProductNewnesses().entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry e = (Map.Entry) it.next();
				ProductModel p = (ProductModel) e.getKey();
				Integer v = (Integer) e.getValue();
		%>
		<tr>
			<td class="text12" title="<%= p.getFullName() %>"><%= p.getContentKey().getId() %></td>
			<td class="text12 right"><%= v.toString() %></td>
		</tr>
		<%
			}
		%>
	</table>
</body>
</html>