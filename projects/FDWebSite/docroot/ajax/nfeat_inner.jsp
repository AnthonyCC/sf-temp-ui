<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ page import="com.freshdirect.framework.webapp.ActionError"
%><%@ page import="java.util.HashMap"
%><%@ page import="java.util.Map"
%><%@ page import="java.util.Collection"
%><%@ page import="java.util.ArrayList"
%><%@ page import="com.freshdirect.fdstore.customer.FDCartLineI"
%><%@ page import="com.freshdirect.fdstore.customer.FDCustomerManager"
%><%@ page import="com.freshdirect.fdstore.content.ContentFactory"
%><%@ page import="com.freshdirect.fdstore.content.ProductModel"
%><%@ page import="com.freshdirect.fdstore.customer.FDUserI"
%><%@ page import="com.freshdirect.fdstore.util.EnumSiteFeature"
%><%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName"
%><%@ page import="com.freshdirect.webapp.util.JspMethods"
%><%@ taglib uri="freshdirect" prefix="fd"
%><%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display'
%><%--

	@author segabor

	Renders content of NEW FEATURE ALERT popup box.
	
	@parameter frameId Unique identifier of popup.
--%><%
	// serve only AJAX requests!
	if (request.getHeader("X-Requested-With") != null) {
		// Prevent caching AJAX responses on browser-side
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		
		final String frameId = request.getParameter("frameId");
		final boolean testOnly = request.getParameter("test") != null;

		final FDUserI u = (FDUserI) session.getAttribute(SessionName.USER);
		if (u == null) {
			// General failure
			response.setStatus(500);
			%>ERROR<%
			return;
		}
		
		
		boolean showNewFeatPanel = false;
		if (FDUserI.SIGNED_IN == u.getLevel()) {
			showNewFeatPanel = testOnly ?
					FDCustomerManager.testCounter(u.getIdentity().getErpCustomerPK(), "4mm-landing-page-views", 5)
					:
					(FDCustomerManager.decrementCounter(u.getIdentity().getErpCustomerPK(), "4mm-landing-page-views", 5) > 0);
		} else {
			showNewFeatPanel = pageContext.getSession().getAttribute("NewFeatureAlert-closed") == null;			
		}
		
		if (showNewFeatPanel) {
		
		// Parent element should look like:
		//
		// <div ... class="nfeat">
%>		<div class="title16 boxTitle">NEW FEATURE ALERT!</div>
		<div class="text13gr">Quickbuy appears only in our 4-­Minute-­Meals department.</div>
		<div class="bframe"><img src="/media_stat/images/quickbuy/quickbuy_button_alrt_dont.gif" onclick="FD_QuickBuy.closeNewFeatBox('<%= frameId %>_nfeat');"></div>
<%
		// </div>
		} else {
			// return empty content!
			response.setStatus(204);
			%><%
		}
	} else {
		// Not AJAX request
		response.setStatus(500);
		%>ERROR<%
	}
%>