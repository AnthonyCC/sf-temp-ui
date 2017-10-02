<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@page import="java.util.List"%>
<%@ page import="java.util.*"%>
<%@page import="com.freshdirect.fdstore.content.ProductModel"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.freshdirect.fdstore.content.ContentNodeModel"%>
<%@page import="com.freshdirect.cms.fdstore.FDContentTypes"%>
<%@page import="com.freshdirect.fdstore.content.CategoryModel"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%><html lang="en-US" xml:lang="en-US">
<%@ page import='com.freshdirect.webapp.util.*'%>
<%@ page import="com.freshdirect.webapp.util.prodconf.DefaultProductConfigurationStrategy"%>
<fd:CheckLoginStatus guestAllowed='true' pixelNames="TheSearchAgency" id="user" />
<%

ConfigurationContext confContext = new ConfigurationContext();
confContext.setFDUser(user);
ConfigurationStrategy confStrat = new DefaultProductConfigurationStrategy();
final String trk = "srch"; // tracking code
%>
<head>
	<title>Grid Test Page</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge" lang="en-US"/>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" lang="en-US"/>
	<%@ include file="/common/template/includes/metatags.jspf" %>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
    <%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
    <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
    <%@ include file="/shared/template/includes/ccl.jspf" %>
	<fd:javascript src="/assets/yui-2.9.0/selector/selector-min.js" />
	<fd:javascript src="/assets/yui-2.9.0/event-delegate/event-delegate-min.js" />
	<fd:javascript src="/assets/javascript/search.js" />
	<link rel="stylesheet" type="text/css" href="/assets/css/common/product_grid.css" />
	<style type="text/css">
		.product-grid { width:967px; }
		.items { width:968px; }
		.grid-view .grid-item-container { width:242px; }
	</style>
	<link rel="stylesheet" type="text/css" href="/assets/css/common/custom_fonts.css" />
</head>
<body>
<%
	String catId = request.getParameter("catId");
	CategoryModel category = null;
	if (catId != null)
		category = (CategoryModel) ContentFactory.getInstance().getContentNode("Category" , catId);
%>
<fd:ItemGrabber id="items" category="<%= category %>" depth="99">
<%
	List<ProductModel> products = new ArrayList<ProductModel>(items.size());
	for (ContentNodeModel node : items)
		if (node.getContentKey().getType().equals(FDContentTypes.PRODUCT))
			products.add((ProductModel) node);
%>
	<div class="product-grid grid-view">
		<div class="items">
<%
	for (Iterator<ProductModel> it=products.iterator() ; it.hasNext();) {
		ProductImpression pi = confStrat.configure(it.next(), confContext);
		%><div class="grid-item-container"><%@ include file="/includes/product/i_product_box.jspf" %></div><%
	}
%>	
			<div class="clear"></div>
		</div>
	</div>
	<hr />
	<div class="product-grid list-view">
		<div class="items">
<%
	for (Iterator<ProductModel> it=products.iterator() ; it.hasNext();) {
		ProductImpression pi = confStrat.configure(it.next(), confContext);
		%><div class="grid-item-container"><%@ include file="/includes/product/i_product_box.jspf" %></div><%
	}
%>	
			<div class="clear"></div>
		</div>
	</div>
	</fd:ItemGrabber>
</body>
</html>