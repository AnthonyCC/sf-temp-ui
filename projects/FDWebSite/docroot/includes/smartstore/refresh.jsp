<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.freshdirect.webapp.taglib.smartstore.Impression,java.util.Map,
java.util.Iterator,com.freshdirect.smartstore.RecommendationService,com.freshdirect.framework.webapp.ActionResult,
com.freshdirect.fdstore.customer.FDUserI,com.freshdirect.smartstore.fdstore.SmartStoreUtil,com.freshdirect.fdstore.util.EnumSiteFeature,
com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%--
	AJAX refresh page
	called from i_generic_recommendations.jspf

	@author segabor
--%>
<%@ taglib uri="freshdirect" prefix="fd"%><%
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


		ActionResult result = new ActionResult();
		request.setAttribute("actionResult", result);
		

		// TODO: maybe null (??)
		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
		
		Variant v = SmartStoreUtil.getVariant(request.getParameter("siteFeature"), request.getParameter("variant"));
		String impId = request.getParameter("impId");
		if (impId!=null) {
		    Impression.tabClick(impId);
		}
		request.setAttribute("genericRecommendationsVariant", v);
%><%@ include file='/includes/smartstore/i_generic_recommendations.jspf' %><%
	}
%>