<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.freshdirect.fdstore.util.EnumSiteFeature"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@ taglib uri="freshdirect" prefix="fd"%>
<fd:CheckLoginStatus noRedirect="true"/>
<%--
	Empty page to store the selected tab (AJAX)

	called from i_generic_recommendations.jspf

	@author segabor
--%>

<%
	FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
	if (user == null) {
		response.setStatus(500);
		return;			
	}

	// serve only AJAX requests!
	if (request.getHeader("X-Requested-With") != null) {
		// Prevent caching AJAX responses on browser-side
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");

		if (request.getParameter("tab") != null && !"".equals(request.getParameter("tab")) &&
				request.getParameter("variant") != null && !"".equals(request.getParameter("variant"))) {
			String tabPos = (String) request.getParameter("tab");
			String variantId = (String) request.getParameter("variant");

			// store change
			session.setAttribute(SessionName.SS_SELECTED_TAB, new Integer(tabPos));
			session.setAttribute(SessionName.SS_SELECTED_VARIANT, variantId);
		}
	}
%>
