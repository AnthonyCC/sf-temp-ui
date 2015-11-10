package com.freshdirect.webapp.crm;

import javax.servlet.http.HttpServletRequest;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAgentRole;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.security.ticket.MasqueradeParams;
import com.freshdirect.security.ticket.MasqueradePurposeBuilder;
import com.freshdirect.security.ticket.Ticket;
import com.freshdirect.security.ticket.TicketService;
import com.freshdirect.webapp.crm.security.CrmSecurityManager;
import com.freshdirect.webapp.crm.security.MenuManager;
import com.freshdirect.webapp.taglib.crm.CrmSession;

public class CrmMasqueradeUtil {
	public static String generateLaunchURL(CrmAgentModel agent, HttpServletRequest request, FDUserI user, String eStoreId) throws FDResourceException, IllegalArgumentException {
		EnumEStoreId storeId = eStoreId != null ? EnumEStoreId.valueOf(eStoreId) : EnumEStoreId.FD;
		if (storeId == null) {
			return null;
		}

		// get base url
		String url = guessStorefrontBaseUrl(storeId);
		if (url == null) {			
			return null;
		}


		CrmAgentRole agentRole = agent.getRole();
		
		final MasqueradeParams params = buildParams(agentRole, request, user);

		String purpose = MasqueradePurposeBuilder.buildPurpose(params);
		
		Ticket token = TicketService.getInstance().create( agent.getUserId(), purpose, ErpServicesProperties.getMasqueradeSecurityTicketExpiration() );

		// append parameters
		url = url + "?"
				+ "agentId=" + agent.getUserId()
				+ "&customerId=" + user.getUserId()
				+ "&case=" + Boolean.toString(params.hasCustomerCase) 
				+ "&forceOrderAvailable=" + params.forceOrderAvailable
				+ "&autoApproveAuthorized=" + params.autoApproveAuthorized
				+ "&loginKey=" + token.getKey();

		if (params.makeGoodFromOrderId!=null){
			url += "&makeGoodFromOrderId=" + params.makeGoodFromOrderId;
		}
		if (params.parentOrderId!=null){
			url += "&parentOrderId=" + params.parentOrderId;
		}

		if (params.autoApprovalLimit!=null){
			url += "&autoApprovalLimit=" + params.autoApprovalLimit;
		}
		
		if (params.destination != null) {
			url += "&destination=" + params.destination;
		} else {
			if (params.shopFromOrderId!=null){
				url += "&shopFromOrderId=" + params.shopFromOrderId;
			}
			
			if (params.modifyOrderId!=null){
				url += "&modifyOrderId=" + params.modifyOrderId;
			}
		}

		return url;
	}

	private static String guessStorefrontBaseUrl(EnumEStoreId storeId) {
		switch (storeId) {
		case FD:
			return ErpServicesProperties.getMasqueradeStoreFrontBaseUrl()	+ "980ff88b1adf961750ca413752af6f10/d56b699830e77ba53855679cb1d252da.jsp";
		case FDX:
			return ErpServicesProperties.getMasqueradeFDXStoreFrontBaseUrl()	+ "980ff88b1adf961750ca413752af6f10/d56b699830e77ba53855679cb1d252da.jsp";
		}

		return null;
	}

	/**
	 * Make redirection based on params
	 * 
	 * @param params
	 * @return
	 */
	public static String getRedirectionUri(MasqueradeParams params) {
		String redirectUri = "/index.jsp";
		
		// make redirection based on params
		if (params.destination != null) {
			if ("gc".equalsIgnoreCase(params.destination)) {
				redirectUri = "/gift_card/purchase/landing.jsp";
			} else if ("gc_bulk".equalsIgnoreCase(params.destination)) {
				redirectUri = "/gift_card/purchase/add_bulk_giftcard.jsp";
			} else if ("timeslots".equalsIgnoreCase(params.destination)) {
				redirectUri = "/help/delivery_info.jsp";
			} else if ("top_faqs".equalsIgnoreCase(params.destination)) {
				redirectUri = "/agent/admintools/top_faqs.jsp";
			} else if ("coupon_savings_history".equalsIgnoreCase(params.destination)) {
				redirectUri = "/agent/coupon_savings_history.jsp";
			} else if ("product_promos".equalsIgnoreCase(params.destination)) {
				redirectUri = "/agent/ppicks_email_products.jsp";
			} else if ("dp_search_results".equalsIgnoreCase(params.destination)) {
				redirectUri = "/srch.jsp?pageType=search&searchParams=delivery+pass";
			}
		}
		// Legacy cases
		else if (params.makeGoodFromOrderId != null) {
			redirectUri = "/quickshop/shop_from_order.jsp?orderId="+params.makeGoodFromOrderId;
		} else {
			if (params.shopFromOrderId != null) {
				redirectUri = "/quickshop/shop_from_order.jsp?orderId="+params.shopFromOrderId;
			}
			if (params.modifyOrderId != null) {
				redirectUri = "/your_account/modify_order.jsp?orderId="+params.modifyOrderId+"&action=modify";
			}
			if (params.parentOrderId != null) {
				redirectUri = "/checkout/view_cart.jsp?orderId="+params.parentOrderId;
			}
		}
		return redirectUri;
	}
	

	/**
	 * Build params POJO in CRM side
	 * 
	 * @param agentRole
	 * @param request
	 * @param user
	 * @return
	 */
	private static MasqueradeParams buildParams(CrmAgentRole agentRole, HttpServletRequest request, FDUserI user) {
		MasqueradeParams params = new MasqueradeParams();
		
		params.userId = user.getUserId();

		params.hasCustomerCase = CrmSession.hasCustomerCase(request.getSession());
		
		params.forceOrderAvailable = CrmSecurityManager.hasAccessToPage(agentRole.getLdapRoleName(),"forceorder");
		params.makeGoodFromOrderId = request.getParameter("makeGoodFromOrderId");
		params.autoApproveAuthorized = CrmSecurityManager.isAutoApproveAuthorized(agentRole.getLdapRoleName());
		params.autoApprovalLimit = MenuManager.getInstance().getAutoApprovalLimit(agentRole.getLdapRoleName());
		params.shopFromOrderId = request.getParameter("shopFromOrderId");
		params.modifyOrderId = request.getParameter("modifyOrderId");
		
		params.destination = request.getParameter("destination");
		params.parentOrderId = request.getParameter("parentOrderId");

		return params;
	}
}
