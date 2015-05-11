<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<fd:CheckLoginStatus id="user"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.freshdirect.webapp.util.ConfigurationContext"%>
<%@page import="com.freshdirect.webapp.util.ConfigurationStrategy"%>
<%@page import="com.freshdirect.webapp.util.prodconf.DefaultProductConfigurationStrategy"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="com.freshdirect.fdstore.content.ProductModel"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@page import="com.freshdirect.webapp.util.ProductImpression"%>
<%@page import="java.util.Collections"%>
<%@page import="com.freshdirect.webapp.util.TransactionalProductImpression"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" lang="en-US">
<title>Add to Cart w/o Page Reload Test Page</title>
<%@ include file="/common/template/includes/metatags.jspf" %>
<%@ include file="/common/template/includes/i_javascripts.jspf" %>
<%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
<%@ include file="/shared/template/includes/ccl.jspf" %>
<link rel="stylesheet" href="/assets/css/test/search/style.css" type="text/css">
<style type="text/css">
.grid-item {
	text-align: center;
	margin: 10px;
}
</style>
</head>
<%
	String productId = request.getParameter("productId");
	String catId = request.getParameter("catId");
%>
<body>
<div class="body">
<div class="left">
<div class="float-left">
<%  if (user != null) {
		if (user.getIdentity() != null) {
%>
Logged in as <%= user.getUserId() %>.
<%      } else { %>
Not logged in. You are in area  <%= user.getZipCode() %>.
<% 		}
	} else { %>
Not logged in.
<% 	} %>
</div>
<div class="float-close"></div>
<div style="padding-top: 15px;">
<form action="<%= request.getRequestURI() %>" method="get">
	<span style="font-weight: bold;">Category Id:</span>
	<input type="text" name="catId"<%= catId != null ? "value=\"" + StringEscapeUtils.escapeHtml(catId) + "\"" : "" %>><br>
	<span style="font-weight: bold;">Product Id:</span>
	<input type="text" name="productId"<%= productId != null ? "value=\"" + StringEscapeUtils.escapeHtml(productId) + "\"" : "" %>><br>
	<input type="submit" value="Submit">
</form>
</div>
<fd:javascript src="/assets/javascript/pricing.js"/>
<%
	ProductModel product = null;
	if (catId != null && productId != null) {
		product = (ProductModel) ContentFactory.getInstance().getProductByName(catId, productId);
	}

	ConfigurationContext confContext = new ConfigurationContext();
	confContext.setFDUser(user);
	ConfigurationStrategy confStrat = new DefaultProductConfigurationStrategy();
	String trk = "test";
	
	if (product != null) {
		// you have to pass a product and a product impression configured using DefaultProductConfigurationStrategy
		// this differs from SmartStoreConfigurationStrategy in that it does not use the customer's purchase
		// history to preconfigure products
		ProductImpression pi = confStrat.configure(product, confContext);
%>
<div class="section">
	<div class="section-header">Instant Add-to-Cart</div>
	<div class="section-body" style="width: 980px; background-color: #ffffdd;">

		<div class="float-left"><!-- container -->
		<!-- this is da box -->
		<div class="span-5" style="background-color: white;">
		<%@ include file="/includes/product/i_product_box.jspf" %>
		</div>
		
		<%
		{
			product = (ProductModel) ContentFactory.getInstance().getProductByName("ban", "ban_yllw");
			pi = confStrat.configure(product, confContext);
		%>
		<div class="span-5" style="background-color: white;">
		<%@ include file="/includes/product/i_product_box.jspf" %>
		</div>
		<%
		}
		%>

		<%
		{
			product = (ProductModel) ContentFactory.getInstance().getProductByName("mea_feat", "brst_tpchckbnls");
			pi = confStrat.configure(product, confContext);
		%>
		<div class="span-5" style="background-color: white;">
		<%@ include file="/includes/product/i_product_box.jspf" %>
		</div>
		<%
		}
		%>

		<%
		{
			product = (ProductModel) ContentFactory.getInstance().getProductByName("dai_yogur_regu", "dai_dannon_blueber_01");
			pi = confStrat.configure(product, confContext);
		%>
		<div class="span-5 clear" style="background-color: white;">
		<%@ include file="/includes/product/i_product_box.jspf" %>
		</div>
		<%
		}
		%>

		<%
		{
			product = (ProductModel) ContentFactory.getInstance().getProductByName("clry", "clry_clrystlk");
			pi = confStrat.configure(product, confContext);
		%>
		<div class="span-5" style="background-color: white;">
		<%@ include file="/includes/product/i_product_box.jspf" %>
		</div>
		<%
		}
		%>

		<%
		{
			product = (ProductModel) ContentFactory.getInstance().getProductByName("fro_veget_spin", "fro_seabro_creamed_01");
			pi = confStrat.configure(product, confContext);
		%>
		<div class="span-5" style="background-color: white;">
		<%@ include file="/includes/product/i_product_box.jspf" %>
		</div>
		<%
		}
		%>

		<%
		{
			product = (ProductModel) ContentFactory.getInstance().getProductByName("hmr_fresh", "var_ds_chxmsrlpnne");
			pi = confStrat.configure(product, confContext);
		%>
		<div class="span-5 clear" style="background-color: white;">
		<%@ include file="/includes/product/i_product_box.jspf" %>
		</div>
		<%
		}
		%>

		<%
		{
			product = (ProductModel) ContentFactory.getInstance().getProductByName("chicken_cubesstrips", "mea_chix_kbob");
			pi = confStrat.configure(product, confContext);
		%>
		<div class="span-5" style="background-color: white;">
		<%@ include file="/includes/product/i_product_box.jspf" %>
		</div>
		<%
		}
		%>

		</div><!-- container -->

		<div class="float-close"></div>
	</div>
</div>
<%
	} else {
%>
<div class="section">
	Please specify proper category and product ids
</div>
<%
	}
%>
</div><!--  left -->
</div><!--  body -->
</body>
</html>