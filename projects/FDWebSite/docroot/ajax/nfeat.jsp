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
%><fd:CheckLoginStatus noRedirect="true"/><%--

	@author segabor

	This is invoked when Close box of New Feature alert is clicked.
	
--%><%
	// serve only AJAX requests!
	if (request.getHeader("X-Requested-With") != null) {
		// Prevent caching AJAX responses on browser-side
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		
		FDUserI u = (FDUserI) session.getAttribute(SessionName.USER);
		if (u != null) {
			// reset counter
			
			if (FDUserI.SIGNED_IN == u.getLevel()) {
				FDCustomerManager.updateCounter(u.getIdentity().getErpCustomerPK(), "4mm-landing-page-views", 0);
			} else {
				request.getSession().setAttribute("NewFeatureAlert-closed", Boolean.TRUE);
			}
			%>OK<%
		} else {
			// not logged in...
			response.setStatus(500);
			%>ERROR<%
		}
	} else {
		// General failure
		response.setStatus(500);
		%>ERROR<%
	}
%>