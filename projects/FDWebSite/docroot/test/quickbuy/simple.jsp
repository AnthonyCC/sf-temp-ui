<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.freshdirect.webapp.util.ProductImpression"%>
<%@ page import="com.freshdirect.webapp.util.FDURLUtil"%>
<%@ taglib uri='/WEB-INF/shared/tld/freshdirect.tld' prefix='fd' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<fd:CheckLoginStatus id="user"/>
<html>
<head>
	<title>QuickBuy Test Page</title>
	<%@ include file="/common/template/includes/metatags.jspf" %>
	<script language="javascript" src="/assets/javascript/common_javascript.js"></script>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<%@ include file="/shared/template/includes/ccl.jspf" %>
</head>
<body>
<fd:ProductGroup id='productNode' categoryId='usq_buylocal' productId='usq_usa_chan_daug_fre'>
<%
	String actionURL = FDURLUtil.getProductURI(productNode, "test");
%>
	<display:GetContentNodeWebId id="webId" product="<%= productNode %>" clientSafe="<%= false %>">
	<div style="float: left; text-align: center;">
		<display:ProductImage action="<%= actionURL %>" product="<%= productNode %>" enableQuickBuy="true" webId="<%= webId %>"/>
		<div class="title14"><display:ProductName action="<%= actionURL %>" product="<%= productNode %>"/></div>
		<display:ProductPrice impression="<%= new ProductImpression(productNode) %>"/>
	</div>
	</display:GetContentNodeWebId>
</fd:ProductGroup>

<fd:ProductGroup id='productNode' categoryId='hmr_fresh' productId='var_ds_pestocannl'>
<%
	String actionURL = FDURLUtil.getProductURI(productNode, "test");
%>
	<display:GetContentNodeWebId id="webId" product="<%= productNode %>" clientSafe="<%= false %>">
	<div style="float: left; text-align: center;">
		<display:ProductImage action="<%= actionURL %>" product="<%= productNode %>" enableQuickBuy="true" webId="<%= webId %>"/>
		<div class="title14"><display:ProductName action="<%= actionURL %>" product="<%= productNode %>"/></div>
		<display:ProductPrice impression="<%= new ProductImpression(productNode) %>"/>
	</div>
	</display:GetContentNodeWebId>
</fd:ProductGroup>
</body>
</html>
