<%--

	AJAX SUPPORT FOR PROMO PUBLISH
	[APPDEV-1091]
	
	@author segabor

--%><%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ page import="java.util.List"
%><%@ page import="java.util.ArrayList"
%><%@ page import="com.freshdirect.fdstore.promotion.management.FDPromotionNewModel"
%><%@ page import="com.freshdirect.fdstore.promotion.management.FDPromotionNewManager"
%><%@ page import="com.freshdirect.fdstore.promotion.FDPromotionNewModelFactory"
%><%@ page import="com.freshdirect.fdstore.promotion.pxp.PromoPublisher"
%><%@ page import="com.freshdirect.crm.CrmAgentModel"
%><%@ page import="com.freshdirect.webapp.taglib.crm.CrmSession"
%><%
	// serve only AJAX requests!
	if (request.getHeader("X-Requested-With") != null) {
		// Prevent caching AJAX responses on browser-side
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");

		String action = request.getParameter("action");
		
		try {
			if ("publish".equalsIgnoreCase(action)) {
				String promoCode = request.getParameter("promoCode");
				
				CrmAgentModel agent = CrmSession.getCurrentAgent(session);
	
				PromoPublisher p = new PromoPublisher();
				p.setAgent(agent);
				
				List<FDPromotionNewModel> l = new ArrayList<FDPromotionNewModel>();
				l.add(FDPromotionNewModelFactory.getInstance().getPromotion(promoCode));
				p.setPromoList(l);
				
				final boolean result = p.doPublish();
				%><%= Boolean.toString(result)%><%
			} else if ("checkPromoCode".equalsIgnoreCase(action)) {
				String promoCode = request.getParameter("promoCode");

				final boolean result = FDPromotionNewManager.isPromotionCodeUsed(promoCode);
				%><%= Boolean.toString(result)%><%
			} else if ("checkPromoName".equalsIgnoreCase(action)) {
				String promoName = request.getParameter("promoName");

				final boolean result = FDPromotionNewManager.isPromotionNameUsed(promoName);
				%><%= Boolean.toString(result)%><%
			} else if ("checkRedemptionCode".equalsIgnoreCase(action)) {
				String redemptionCode = request.getParameter("redemptionCode");

				final boolean result = FDPromotionNewManager.isRedemptionCodeExists(redemptionCode);
				%><%= Boolean.toString(result)%><%
			}
		} catch (Exception exc) {
			exc.printStackTrace();
			response.setStatus(500);
			%>Internal error occurred.<%
		}
	}
%>