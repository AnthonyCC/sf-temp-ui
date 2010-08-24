<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ page import="com.freshdirect.framework.webapp.ActionError"
%><%@ page import="java.util.HashMap"
%><%@ page import="java.util.Map"
%><%@ page import="java.util.Collection"
%><%@ page import="java.util.ArrayList"
%><%@ page import="com.freshdirect.fdstore.customer.FDCartLineI"
%><%@ page import="com.freshdirect.fdstore.content.ContentFactory"
%><%@ page import="com.freshdirect.fdstore.content.ProductModel"
%><%@ page import="com.freshdirect.fdstore.customer.FDUserI"
%><%@ page import="com.freshdirect.fdstore.customer.FDCustomerManager"
%><%@ page import="com.freshdirect.fdstore.util.EnumSiteFeature"
%><%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName"
%><%@ page import="com.freshdirect.webapp.util.JspMethods"
%><%@ taglib uri="freshdirect" prefix="fd"
%><%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display'
%><%--

	@author segabor
--%><%
	// serve only AJAX requests!
	if (request.getHeader("X-Requested-With") != null) {
		// Prevent caching AJAX responses on browser-side
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		
		FDUserI u = (FDUserI) session.getAttribute(SessionName.USER);
		if (u != null) {
%><fd:FDShoppingCart id='cart' result='actionResult' action='addMultipleToCart' source='Browse'><%
			if (actionResult.isSuccess()) {
				final String frameId = request.getParameter("frameId");
				final String catId = request.getParameter("catId_0");
				final String prdId = request.getParameter("productId_0");

				ProductModel prd = ContentFactory.getInstance().getProduct(catId, prdId);
				FDCartLineI orderLine = cart.getRecentOrderLines().get(0);
%><%-- CART CONFIRM START --%>
<div class="left" style="text-align:left;">
	<div class="title14 info">You have just<br>added to your cart</div>
	<div class="title16"><%= prd.getFullName() %>&nbsp;<a href="/product_modify.jsp?cartLine=<%= orderLine.getRandomId() %>&trk=conf"><img src="/media_stat/images/buttons/edit_product.gif" width="32" height="14" alt="Edit" border="0"></a></div>
	<div class="qbParam" style="margin-top:1em">Quantity: <span><display:OrderLineQuantity orderline="<%= orderLine %>" product="<%= prd %>" customer="<%= u %>"/></span></div>
	<div class="qbParam">Options: <span><%= orderLine.getConfigurationDesc() %></span></div>
	<div class="qbParam" style="margin-bottom:1em">Est. Price: <span><%= JspMethods.currencyFormatter.format(orderLine.getPrice()) %></span></div>
    <fd:CCLCheck>
		<!-- Add to Shopping List  -->
		<a href="/unsupported.jsp" onclick="document.quickbuyPanel.hide(); CCL.add_recent_cart_items(); return false;"><img src="/media_stat/ccl/lists_link_with_icon_dfgs.gif" style="border: 0;"/></a><span style="padding-left: 15px"><fd:CCLNew/></span>
		<div><fd:CCLNew template="/common/template/includes/ccl_moreabout.jspf"/></div>
	</fd:CCLCheck>
</div>
<div class="right">
	<img src="<%= prd.getDetailImage().getPath() %>" style="width: <%= prd.getDetailImage().getWidth() %>px; height <%= prd.getDetailImage().getHeight() %>px; ">
</div>
<div onclick="document.quickbuyPanel.hide();" class="close"></div>
<%-- CART CONFIRM END --%><%
			} else {
				// ==== ERROR branch ====
				response.setStatus(409);
				response.setContentType("application/json");

				int k = actionResult.getErrors().size();
%>[<%
   
				for (ActionError e : actionResult.getErrors()) {
					k--;
					%>{"errorType":"<%= JspMethods.javaScriptEscape(e.getType()) %>", "errorDesc":"<%= JspMethods.javaScriptEscape(e.getDescription()) %>"}<%= k == 0 ? "" : ", " %><%
				}
%>]<%
			}
		// JspMethods.dumpErrors(actionResult);
%></fd:FDShoppingCart><%
		} else {
			// not logged in...
			response.setStatus(500);
			response.setContentType("application/json");
			%>[]<%
		}
	} else {
		// General failure
		response.setStatus(500);
		response.setContentType("application/json");
		%>[]<%
	}
%>