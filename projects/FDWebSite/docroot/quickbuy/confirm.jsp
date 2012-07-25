<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="com.freshdirect.fdstore.customer.FDCartLineI"%>
<%@ page import="com.freshdirect.fdstore.content.ProductModel"%>
<%@ page import="com.freshdirect.webapp.util.JspMethods"%>
<%@ page import="com.freshdirect.fdstore.content.EnumProductLayout"%>
<%@ page import="com.freshdirect.fdstore.customer.EnumQuickbuyStatus"%>
<%@page import="com.freshdirect.fdstore.content.Image"%>
<%@ taglib uri='/WEB-INF/shared/tld/freshdirect.tld' prefix='fd' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<fd:CheckLoginStatus id="user"/>
<fd:FDShoppingCart id='cart' result='result'>
<html>
<head>
    <%@ include file="/common/template/includes/metatags.jspf" %>
    <fd:javascript src="/assets/javascript/common_javascript.js"/>
     <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
     <%@ include file="/shared/template/includes/ccl.jspf" %>
<%@ include file="/shared/template/includes/i_head_end.jspf" %>
</head>
<body class="qbBody">
<%@ include file="/shared/template/includes/i_body_start.jspf" %>
<%
	if (cart.getRecentOrderLines().size() > 0) {
		FDCartLineI orderLine = cart.getRecentOrderLines().get(0);
		ProductModel productNode = orderLine.getProductRef().lookupProductModel();

		final boolean __isWineLayout = EnumProductLayout.NEW_WINE_PRODUCT.equals(productNode.getProductLayout());
%>
<fd:CmShop5 wrapIntoScriptTag="true" cart="<%=cart%>"/>
<div class="left">
	<div class="title14 info" style="color: #ff9933">You have just<br>added to your cart</div>
	<div class="title14"><%= productNode.getFullName() %>&nbsp;<a href="/product_modify.jsp?cartLine=<%= orderLine.getRandomId() %>&amp;trk=conf&amp;referer=<%= URLEncoder.encode(request.getParameter("referer"), "UTF8") %>" target="_top"><img src="/media_stat/images/buttons/edit_product.gif" width="32" height="14" alt="Edit" border="0"></a></div>
	<div class="qbParam" style="margin-top:1em">Quantity: <span><display:OrderLineQuantity orderline="<%= orderLine %>" product="<%= productNode %>" customer="<%= user %>"/></span></div>
	<div class="qbParam">Options: <span><%= orderLine.getConfigurationDesc() %></span></div>
	<div class="qbParam" style="margin-bottom:1em">Est. Price: <span><%= JspMethods.formatPrice(orderLine.getPrice()) %></span></div>
    <fd:CCLCheck>
		<!-- Add to Shopping List  -->
		<a href="/unsupported.jsp" onclick="window.parent.CCL.add_recent_cart_items(); window.parent.document.quickbuyPanel.hide(); return false;"><img src="/media_stat/ccl/lists_link_with_icon_dfgs.gif" style="border: 0;"/></a></span>
		<div></div>
	</fd:CCLCheck>
   	<div class="prdsep">&nbsp;</div>
</div>
<div class="right">
	<%
	if (__isWineLayout) {
		Image descImage = productNode.getDescriptiveImage();
		Image altImage = productNode.getAlternateImage();
		if(descImage == null){
			descImage = new Image("/media/images/temp/soon_100x100.gif", 100, 100);
		}
		if(altImage == null){
			altImage = new Image("/media/images/temp/soon_100x100.gif", 100, 100);
		}
	%>		<div>
				<img src="<%= altImage.getPath() %>" style="width: <%= altImage.getWidth() %>px; height <%= altImage.getHeight() %>px; vertical-align: top;">
				<img src="<%= descImage.getPath() %>" style="width: <%= descImage.getWidth() %>px; height <%= descImage.getHeight() %>px; vertical-align: top;">
			</div>
	<%
	} else {
	%>		<img src="<%= productNode.getDetailImage().getPath() %>" style="width: <%= productNode.getDetailImage().getWidth() %>px; height <%= productNode.getDetailImage().getHeight() %>px; ">
	<%
	}
	%>
</div>
<img src="/media_stat/images/quickbuy/close.gif" style="position: absolute; bottom: 1em; left: 1em; color: grey; cursor: pointer;" onclick="window.parent.document.quickbuyPanel.hide();">
<%
	} else {
%>
ERROR!
<%
	}

	String iatcNamespace = request.getParameter("iatcNamespace");
%>
<script>
YAHOO.util.Event.onDOMReady(function(e) {
	window.parent.updateYourCartPanel();
	<% if (iatcNamespace != null) { %>
	window.parent.<%= iatcNamespace %>.updateStatus('<span class="ok"><%= EnumQuickbuyStatus.ADDED_TO_CART.getMessage() %></span>');
	<% } %>
});
</script>
</body>
</html>
</fd:FDShoppingCart>
