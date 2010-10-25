<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.freshdirect.webapp.taglib.smartstore.Impression,java.util.Map,
java.util.Iterator,com.freshdirect.smartstore.RecommendationService,com.freshdirect.framework.webapp.ActionResult,
com.freshdirect.fdstore.customer.FDUserI,com.freshdirect.smartstore.fdstore.SmartStoreUtil,com.freshdirect.fdstore.util.EnumSiteFeature,
com.freshdirect.webapp.taglib.fdstore.SessionName,com.freshdirect.smartstore.Variant"%>
<%@ taglib uri="freshdirect" prefix="fd"%>
<fd:CheckLoginStatus noRedirect="true"/>
<%--
	AJAX refresh page
	called from i_generic_recommendations.jspf

	@author segabor
--%>

<%
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

		// Clicked 'Click here to refresh' link
		boolean isContentRefresh = "true".equalsIgnoreCase(request.getParameter("refresh"));

		ActionResult result = new ActionResult();
		request.setAttribute("actionResult", result);
		
		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
		if (user == null) {
			response.setStatus(500);
			return;			
		}
		
		Variant v = SmartStoreUtil.getVariant(request.getParameter("siteFeature"), request.getParameter("variant"));
		String impId = request.getParameter("impId");
		if (impId!=null && !isContentRefresh) {
		    Impression.tabClick(impId);
		}

		request.setAttribute("genericRecommendationsVariant", v);
		String pimpId = request.getParameter("pImpId");
		if (pimpId != null) {
		    int indx = pimpId.indexOf(':');
		    if (indx != -1) {
		        String parentVariantId = pimpId.substring(indx + 1);
				request.setAttribute("parentVariantId", parentVariantId);
		        pimpId = pimpId.substring(0, indx);
		    }
			request.setAttribute("parentImpressionId", pimpId);
		}
			
		if ( session.isNew() ) {	// session timeout
			response.setStatus(HttpServletResponse.SC_RESET_CONTENT);
		} else {
			String smartStoreFacility = request.getParameter("facility");
			%><%@ include file='/includes/smartstore/i_generic_recommendations.jspf' %><%
		}
	}
%>