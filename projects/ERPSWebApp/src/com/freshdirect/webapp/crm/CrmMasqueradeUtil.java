package com.freshdirect.webapp.crm;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.ErpServicesProperties;
import com.freshdirect.common.context.MasqueradeContext;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAgentRole;
import com.freshdirect.customer.EnumPaymentMethodDefaultType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartonDetail;
import com.freshdirect.fdstore.customer.FDCartonInfo;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.payments.util.PaymentMethodUtil;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rollout.FeatureRolloutArbiter;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.security.ticket.MasqueradeParams;
import com.freshdirect.security.ticket.MasqueradePurposeBuilder;
import com.freshdirect.security.ticket.Ticket;
import com.freshdirect.security.ticket.TicketService;
import com.freshdirect.webapp.ajax.expresscheckout.csr.service.CustomerServiceRepresentativeService;
import com.freshdirect.webapp.ajax.filtering.CmsFilteringNavigator;
import com.freshdirect.webapp.ajax.reorder.QuickShopServlet;
import com.freshdirect.webapp.ajax.reorder.data.EnumQuickShopTab;
import com.freshdirect.webapp.ajax.reorder.data.QuickShopListRequestObject;
import com.freshdirect.webapp.crm.security.CrmSecurityManager;
import com.freshdirect.webapp.crm.security.MenuManager;
import com.freshdirect.webapp.crm.util.MakeGoodOrderUtility;
import com.freshdirect.webapp.taglib.crm.CrmSession;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class CrmMasqueradeUtil {
	private static final Logger LOGGER = LoggerFactory.getInstance( CrmMasqueradeUtil.class );
	
	
    /**
     * Build masquerade context
     * 
     * @param identity user identity @param params @param agentId CRM Agent ID @return
     *
     * @throws FDResourceException
     */
    public static MasqueradeContext build(final FDIdentity identity, final MasqueradeParams params, final String agentId) throws FDResourceException {
        // masquerade
        // FIXME context builing should be extracted to a builder class
        MasqueradeContext ctx = new MasqueradeContext();
        ctx.setAgentId(agentId);
        ctx.setHasCustomerCase(params.hasCustomerCase);
        ctx.setForceOrderAvailable(params.forceOrderAvailable);
        ctx.setAutoApprovalLimit(params.autoApprovalLimit);
        ctx.setAutoApproveAuthorized(params.autoApproveAuthorized);
        ctx.setCsrWaivedDeliveryCharge(false);
        ctx.setCsrWaivedDeliveryPremium(false);
        ctx.setSilentMode(false);

        // Make-Good Order: collect order line IDs
        if (params.makeGoodFromOrderId != null) {
            final FDOrderI order = FDCustomerManager.getOrder(identity, params.makeGoodFromOrderId);
            ctx.setMakeGoodFromOrderId(params.makeGoodFromOrderId);
            Map<String, Double> maximumQuantitiesBySkuCode = calculateMaximumQuantityBySkuCode(order);
            for (FDCartLineI mgOrderLine : order.getOrderLines()) {
                ctx.addMakeGoodOrderLineIdQuantity(mgOrderLine.getOrderLineId(), maximumQuantitiesBySkuCode.get(mgOrderLine.getSkuCode()));
            }

            // extract carton numbers for order lines
            CrmMasqueradeUtil.buildCartonNumberMap(ctx, order.getCartonContents());
        } else if (params.parentOrderId != null) {
            ctx.setParentOrderId(params.parentOrderId);
        }

        return ctx;
    }

    private static Map<String, Double> calculateMaximumQuantityBySkuCode(FDOrderI order) {
        Map<String, Double> maxQuantitiesBySkuCode = new HashMap<String, Double>();
        for (FDCartLineI orderLine : order.getOrderLines()) {
            String skuCode = orderLine.getSkuCode();
            double quantity = orderLine.getQuantity();
            if (maxQuantitiesBySkuCode.containsKey(skuCode)) {
                quantity += maxQuantitiesBySkuCode.get(skuCode);
            }
            maxQuantitiesBySkuCode.put(skuCode, quantity);
        }
        return maxQuantitiesBySkuCode;
    }

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
		if(FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.debitCardSwitch, user)){
				if(null == user.getFDCustomer().getDefaultPaymentType() || user.getFDCustomer().getDefaultPaymentType().getName().equals(EnumPaymentMethodDefaultType.UNDEFINED.getName())){
					FDActionInfo actionInfo = new FDActionInfo(EnumTransactionSource.ADMINISTRATOR, user.getIdentity(), "", "Masquerading from CRM", null, user.getFDCustomer().getId());
					ErpPaymentMethodI defaultPayment =  PaymentMethodUtil.getSystemDefaultPaymentMethod(actionInfo, user.getPaymentMethods(), true);
					PaymentMethodUtil.updateDefaultPaymentMethod(actionInfo, user.getPaymentMethods(), defaultPayment.getPK().getId(), EnumPaymentMethodDefaultType.DEFAULT_SYS, false);
			}
		}else if(!FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.debitCardSwitch, user) && null!=user.getFDCustomer().getDefaultPaymentType() 
      		  && !user.getFDCustomer().getDefaultPaymentType().getName().equals(EnumPaymentMethodDefaultType.UNDEFINED.getName())){
			user.resetDefaultPaymentValueType();
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
	public static String getRedirectionUri(HttpServletRequest request, FDUserI user, MasqueradeParams params) {
		String redirectUri = "/index.jsp";
		
		// make redirection based on params
		if (params.destination != null) {
			if ("gc".equalsIgnoreCase(params.destination)) {
				redirectUri = "/gift_card/purchase/landing.jsp";
			} else if ("gc_bulk".equalsIgnoreCase(params.destination)) {
				redirectUri = "/gift_card/purchase/add_bulk_giftcard.jsp";
			} else if ("checkout".equalsIgnoreCase(params.destination)) {
				final boolean canUseXCpages = FDStoreProperties.isExpressCheckoutEnabledForCSR();

				redirectUri = canUseXCpages ? "/expressco/view_cart.jsp" : "/checkout/view_cart.jsp";
			} else if ("timeslots".equalsIgnoreCase(params.destination)) {
				redirectUri = "/your_account/delivery_info_avail_slots.jsp";
			} else if ("top_faqs".equalsIgnoreCase(params.destination)) {
				redirectUri = "/agent/admintools/top_faqs.jsp";
			} else if ("coupon_savings_history".equalsIgnoreCase(params.destination)) {
				redirectUri = "/agent/coupon_savings_history.jsp";
			} else if ("product_promos".equalsIgnoreCase(params.destination)) {
				redirectUri = "/agent/ppicks_email_products.jsp";
			} else if ("dp_search_results".equalsIgnoreCase(params.destination)) {
				redirectUri = "/srch.jsp?pageType=search&searchParams=delivery+pass";
			} else if ("addon".equalsIgnoreCase(params.destination)) {
				redirectUri = "/";
			}
		}
		// Legacy cases
		else if (params.makeGoodFromOrderId != null) {

			final String payload = createPastOrderUrlPayload(request, user, params.makeGoodFromOrderId);
			
			if (payload != null) {
				try {
					String data = URLEncoder.encode(payload, "UTF-8");
					redirectUri = "/quickshop/qs_past_orders.jsp#"+data;
				} catch (UnsupportedEncodingException e) {
					redirectUri = "/quickshop/qs_past_orders.jsp";
				}
			} else {
				redirectUri = "/quickshop/qs_past_orders.jsp";
			}
		} else {
			if (params.shopFromOrderId != null) {
                redirectUri = "/your_account/order_details.jsp?orderId=" + params.shopFromOrderId;
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
	
	public static String createPastOrderUrlPayload(HttpServletRequest request, FDUserI user, final String makeGoodFromOrderId) {
		
		// Assumed, Quickshop 2.0 / REORDER feature is active
		List<Object> orderIdList = new ArrayList<Object>( Arrays.asList(new String[]{ makeGoodFromOrderId }) );

		QuickShopListRequestObject potato = new QuickShopListRequestObject();
		potato.setOrderIdList( orderIdList );
		potato.setTab( EnumQuickShopTab.PAST_ORDERS );
		potato.setActivePage( 0 );
		potato.setPageSize(CmsFilteringNavigator.increasePageSizeToFillLayoutFully(request, user, QuickShopServlet.DEFAULT_PAGE_SIZE));

		StringWriter writer = new StringWriter();
		try {
			new ObjectMapper().writeValue( writer, potato );

			return writer.toString();
		} catch (JsonGenerationException e) {
		} catch (JsonMappingException e) {
		} catch (IOException e) {
		}
		
		return null;
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

		if(CrmAgentRole.COS_CODE.equalsIgnoreCase(CrmSession.getCurrentAgent(request.getSession()).getRole().getCode()))
			params.hasCustomerCase=true;
		else
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
	
	
	
	public static void postInit(final MasqueradeContext ctx, final HttpSession session) {
		final FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);

    	if (ctx.isMakeGood()) {
        	// Make-Good Order: create complaint structure in session required for XC checkout
			user.getShoppingCart().clearOrderLines();

			try {
				FDOrderI referenceOrder = FDCustomerManager.getOrderForCRM(user.getIdentity(), ctx.getMakeGoodFromOrderId());
				prepareMakeGoodContext(ctx, session, referenceOrder.getOrderLines() );
			} catch (FDResourceException e) {
				LOGGER.error(e);
			}
		}

    	// preset certain CSR fields
    	CustomerServiceRepresentativeService.defaultService().presetCustomerServiceRepresentativeInfo(user);
	}
	
	
	public static void prepareMakeGoodContext( final MasqueradeContext ctx, final HttpSession session, final List<FDCartLineI> cartLines) throws FDResourceException {
		final Set<String> lineIds = ctx.getMakeGoodOrderLineIdQuantities();
		
        String orderLineReason[]	=  new String[lineIds.size()];
        String orderLineId[]		= new String[lineIds.size()];

        // populate order line reasons with a default value, say with the first in line
    	final String selReasonId = "nil" /* selReason.get(0).getId() */;
        for (int i=0; i<orderLineReason.length; i++) {
			orderLineReason[i] = selReasonId;
        }
        orderLineId = lineIds.toArray(orderLineId);

        // decorate cart lines with carton numbers
        //   this trick is required in order to get complaint lines accompanied with carton numbers.
        //   note, that action results below will show a lot of errors, but discard them for now
        if (!ctx.isEmptyMakeGoodOrderLineIdCartonNumbers()) {
        	assignCartonNumberToCartItems(ctx, cartLines);
        }

		try {
	        ActionResult result = new ActionResult();
			MakeGoodOrderUtility.handleMakeGood(orderLineReason, orderLineId, session, cartLines, result);
			
			if (!result.isSuccess()) {
				for (final ActionError err : result.getErrors()) {
					LOGGER.error("[" + err.getType() + "]: " + err.getDescription());
				}
			}
		} catch (FDResourceException e) {
			LOGGER.error(e);
		}
	}
	
	
    /**
     * Build carton number map
     * 
     * @param masqueradeContext @param cartonInfos
     */
    public static void buildCartonNumberMap(MasqueradeContext context, List<FDCartonInfo> cartonInfos) {
        if (context != null && context.isMakeGood() && cartonInfos != null) {

            for (final FDCartonInfo cartonInfo : cartonInfos) {
                final String cartonNumber = cartonInfo.getCartonInfo().getCartonNumber();
                for (FDCartonDetail cartonDetail : cartonInfo.getCartonDetails()) {
                    if (cartonDetail.getCartLine() != null) {
                        final String orderLineId = cartonDetail.getCartLine().getOrderLineId();
                        if (context.containsMakeGoodOrderLineIdQuantity(orderLineId)) {
                            context.addMakeGoodOrderLineIdCartonNumber(orderLineId, cartonNumber);
                        }
                    }
                }
            }
        }

    }
	
    /**
     * Utility method that assigns carton numbers to each cart lines
     * 
     * @param masqueradeContext @param cartLines
     */
    public static void assignCartonNumberToCartItems(MasqueradeContext context, Collection<FDCartLineI> cartLines) {
        for (final FDCartLineI cartLine : cartLines) {
            assignCartonNumberToCartLine(context, cartLine);
        }
    }

    /**
     * MAKE GOOD MODE CartonNumber -> CartLine
     * 
     * @param masqueradeContext @param cartLine
     */
    public static void assignCartonNumberToCartLine(MasqueradeContext context, FDCartLineI cartLine) {
        if (!(context != null && context.isMakeGood() && !context.isEmptyMakeGoodOrderLineIdCartonNumbers() && cartLine != null)) {
            return;
        }

        if (cartLine.getOrderLineId() != null && context.getMakeGoodOrderLineIdCartonNumbers(cartLine.getOrderLineId()) != null) {
            cartLine.setCartonNumber(context.getMakeGoodOrderLineIdCartonNumbers(cartLine.getOrderLineId()));
            LOGGER.debug("Carton Number " + cartLine.getCartonNumber() + " has been assigned to cart line (R_ID: " + cartLine.getRandomId() + ")");
        }
    }
}
