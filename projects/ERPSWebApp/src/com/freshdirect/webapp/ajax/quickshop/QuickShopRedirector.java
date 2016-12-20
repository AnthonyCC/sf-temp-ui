package com.freshdirect.webapp.ajax.quickshop;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.standingorders.FDStandingOrdersManager;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.webapp.features.service.FeaturesService;
import com.freshdirect.webapp.util.FDURLUtil;

public class QuickShopRedirector extends BodyTagSupport {

	private static final Logger LOG = LoggerFactory.getInstance(QuickShopRedirector.class);

	public static final String URL_NEW_QS_PAST_ORDERS = "/quickshop/qs_past_orders.jsp";
	public static final String URL_NEW_QS_SHOPPING_LISTS = "/quickshop/qs_shop_from_list.jsp";
	public static final String URL_NEW_QS_FD_LISTS = "/quickshop/qs_fd_lists.jsp";
	public static final String URL_NEW_QS_STANDING_ORDERS = "/quickshop/qs_standing_orders.jsp";
	public static final String URL_NEW_QS_SO_DETAILS = "/quickshop/qs_so_details.jsp";

	public static final String URL_NEW_QS_TOP_ITEMS = "/quickshop/qs_top_items.jsp";

	public static final String URL_OLD_QS_PAST_ORDERS = "/quickshop/previous_orders.jsp";
	public static final String URL_OLD_QS_ORDER_SHOP_FROM = "/quickshop/shop_from_order.jsp";
	public static final String URL_OLD_QS_SHOPPING_LISTS = "/quickshop/all_lists.jsp";
	public static final String URL_OLD_QS_SHOPPING_LIST_DETAIL = "/quickshop/shop_from_list.jsp";
	public static final String URL_OLD_QS_FD_LISTS = "/quickshop/all_starter_lists.jsp";
	public static final String URL_QS_STANDING_ORDERS3 = "/quickshop/standing_orders.jsp";
	public static final String URL_OLD_QS_SO_DETAILS = "/quickshop/so_details.jsp";

	public static enum FROM {
		NEW_PAST_ORDERS, NEW_LISTS, NEW_FD_LISTS, NEW_SO, NEW_SO_DETAILS, OLD_INDEX, OLD_SO, OLD_SO_DETAILS, OLD_QS_ORDER_SHOP_FROM, OLD_QS_LIST_SHOP_FROM, OLD_QS_ALL_LISTS,NEW_SO3_DETAIL
	};

	private static final long serialVersionUID = 6322447917892657222L;

	private FDUserI user;
	private FROM from = FROM.OLD_INDEX;

	private boolean isNewQs = false;
	private boolean redirected = false;

	public FROM getFrom() {
		return from;
	}

	public void setFrom(FROM from) {
		this.from = from;
	}

	public FDUserI getUser() {
		return user;
	}

	public void setUser(FDUserI user) {
		this.user = user;
	}

	/*
	 * ======================================================= JSP-TAG
	 * doStartTag =======================================================
	 */

	@Override
	public int doStartTag() throws JspException {

		// first check property that disables the whole partial rollout stuff -
		// do not redirect anything
		/*
		 * if ( FDStoreProperties.isQuickshopIgnorePartialRollout() ) { return
		 * EVAL_BODY_BUFFERED; }
		 */

		// partial rollout check
		isNewQs = isEligibleForNewQuickShop(user);
		String redirectUrl = null;

		switch (from) {

		// redirects from NEW to OLD

		case NEW_PAST_ORDERS:
			redirectUrl = getRedirectForNewPastOrdersPage();
			break;

		case NEW_LISTS:
			redirectUrl = getRedirectForNewListsPage();
			break;

		case NEW_FD_LISTS:
			redirectUrl = getRedirectForNewFdListsPage();
			break;

		case NEW_SO:
			redirectUrl = getRedirectForNewStandingOrderPage();
			break;

		case NEW_SO_DETAILS:
			redirectUrl = getRedirectForNewStandingOrderDetailPage();
			break;

		// redirects from OLD to NEW

		case OLD_INDEX:
			redirectUrl = getRedirectForOldIndexPage();
			break;

		case OLD_SO:
			redirectUrl = getRedirectForOldStandingPage();
			break;

		case OLD_SO_DETAILS:
			redirectUrl = getRedirectForOldStandingOrderDetailPage();
			break;

		case OLD_QS_ORDER_SHOP_FROM:
			redirectUrl = getRedirectForOldShopFromOrderPage();
			break;

		case OLD_QS_LIST_SHOP_FROM:
			redirectUrl = getRedirectForOldShopFromListPage();
			break;

		case OLD_QS_ALL_LISTS:
			redirectUrl = getRedirectForOldAllListPage();
			break;
		
		case NEW_SO3_DETAIL:
			redirectUrl =getRedirectForNewSO3Page();
		}

		// No need for redirecting, (continue rendering the old page)
		if (redirectUrl == null) {
			return EVAL_BODY_BUFFERED;
		}

		// Do the redirect for real, and skip processing this page further
		redirect(redirectUrl);
		return SKIP_PAGE;
	}

	/*
	 * ======================================================= REDIRECTS from
	 * OLD to NEW quickshop pages
	 * =======================================================
	 */

	private String getRedirectForOldAllListPage() {
		if (!isNewQs) {
			// Not eligible for the new stuff, do no redirect
			return null;
		}

		return URL_NEW_QS_SHOPPING_LISTS;
	}

	private String getRedirectForOldShopFromListPage() {
		if (!isNewQs) {
			// Not eligible for the new stuff, do no redirect
			return null;
		}

		String listId = pageContext.getRequest().getParameter("ccListId");
		return URL_NEW_QS_SHOPPING_LISTS + "#{\"yourListId\":\"" + listId + "\"}";
	}

	private String getRedirectForOldShopFromOrderPage() {
		if (!isNewQs) {
			// Not eligible for the new stuff, do no redirect
			return null;
		}

		String orderId = pageContext.getRequest().getParameter("orderId");
		return URL_NEW_QS_PAST_ORDERS + "#{\"orderIdList\":[\"" + orderId + "\"]}";
	}

	private String getRedirectForOldStandingPage() {
		if (!isNewQs) {
			// Not eligible for the new stuff, do no redirect
			return null;
		}
        if(user.isNewSO3Enabled()){
    		return URL_QS_STANDING_ORDERS3;
        }
		return URL_NEW_QS_STANDING_ORDERS;
	}

	private String getRedirectForOldStandingOrderDetailPage() {
		if (!isNewQs) {
			// Not eligible for the new stuff, do no redirect
			return null;
		}
        if(user.isNewSO3Enabled()){
    		return URL_QS_STANDING_ORDERS3;
        }
		String listId = pageContext.getRequest().getParameter("ccListId");
		return URL_NEW_QS_SO_DETAILS + "?ccListId=" + listId;
	}

	private String getRedirectForOldIndexPage() {
		if (!isNewQs) {
			// Just display the old quickshop (continue rendering the old page)
			return null;
		}

		// Eligible for new quickshop - we need to redirect to some new page

		// COS check
		boolean isCorporate = isEligibleForStandingOrders(user);
		Cookie[] cookies = ((HttpServletRequest) pageContext.getRequest()).getCookies();
		boolean isQuickShop2_2Active = FeaturesService.defaultService().isFeatureActive(EnumRolloutFeature.quickshop2_2, cookies, user);
		if (isCorporate) {
			// COS

			// Count the active standing orders
			int soCount = 0;
			try {
				soCount = FDStandingOrdersManager.getInstance().loadCustomerNewStandingOrders(user.getIdentity()).size();
			} catch (FDResourceException e) {
				LOG.error("Failed to get standing orders for " + user.getUserId(), e);
			}

			if (soCount > 0) {
				// Happiness, we have standing orders, display them
		        if(user.isNewSO3Enabled()){
		    		return URL_QS_STANDING_ORDERS3;
		        }
				return URL_NEW_QS_STANDING_ORDERS;
			}

			// No actual standing orders, show past orders instead
			return isQuickShop2_2Active ? URL_NEW_QS_TOP_ITEMS : URL_NEW_QS_PAST_ORDERS;

		}

		if (isQuickShop2_2Active) {
			return URL_NEW_QS_TOP_ITEMS;
		} else {
			// Count the orders
			int orderCount = 0;
			try {
				orderCount = QuickShopHelper.getRecentOrderHistoryInfoIds(user).size();
			} catch (FDResourceException ignore) {
			}

			if (orderCount > 0) {
				// Customer has real orders, show them!
				return URL_NEW_QS_PAST_ORDERS;
			}

			// Customer has no orders at all, must be new here, show them the FD
			// lists
			return URL_NEW_QS_FD_LISTS;
		}
	}

	/*
	 * ======================================================= REDIRECTS from
	 * NEW to OLD quickshop pages
	 * =======================================================
	 */

	private String getRedirectForNewStandingOrderPage() {
		if (isNewQs && user.isNewSO3Enabled()) {
			// Eligible for the new stuff, just let him through
			return URL_QS_STANDING_ORDERS3;
		} else if(isNewQs){
			return null; // to open qs_standing_order.jsp 
		}
			

		// Not allowed to see the new stuff, send him back to the old and ugly
		// page

		// Just send him back to the SO main page
		return FDURLUtil.getStandingOrderMainPage(user);
	}

	private String getRedirectForNewStandingOrderDetailPage() {
		if (isNewQs && user.isNewSO3Enabled()) {
			// Eligible for the new stuff, just let him through
			return URL_QS_STANDING_ORDERS3;
		} else if(isNewQs){
			return null; // to open qs_standing_order.jsp 
		}

		// Not allowed to see the new stuff, send him back to the old and ugly
		// page

		String listId = pageContext.getRequest().getParameter("ccListId");
		return URL_OLD_QS_SO_DETAILS + "?ccListId=" + listId;
	}

	private String getRedirectForNewFdListsPage() {
		if (isNewQs) {
			// Eligible for the new stuff, just let him through
			return null;
		}

		// Not allowed to see the new stuff, send him back to the old and ugly
		// page
		return URL_OLD_QS_FD_LISTS;
	}

	private String getRedirectForNewListsPage() {
		if (isNewQs) {
			// Eligible for the new stuff, just let him through
			return null;
		}

		// Not allowed to see the new stuff, send him back to the old and ugly
		// page
		String listId = pageContext.getRequest().getParameter("yourListId");
		if (listId != null) {
			return URL_OLD_QS_SHOPPING_LIST_DETAIL + "?ccListId=" + listId;
		}
		return URL_OLD_QS_SHOPPING_LISTS;
	}

	private String getRedirectForNewPastOrdersPage() {
		if (isNewQs) {
			// Eligible for the new stuff, just let him through
			return null;
		}

		// Not allowed to see the new stuff, send him back to the old and ugly
		// page
		return URL_OLD_QS_PAST_ORDERS;
	}

	/*
	 * ======================================================= ELIGIBILITY
	 * testing logic =======================================================
	 */

	public static boolean isEligibleForStandingOrders(FDUserI user) {

		// For "COS check" the same logic is used as for
		// "eligible for standing orders"

		return user.isEligibleForStandingOrders();
	}

	public static boolean isEligibleForNewQuickShop(FDUserI user) {

		// If quickshop property is not enabled, customers with 'QS_ELIGIBLE'
		// profile are only eligible for the new qs.
		// If quickshop property is enabled, everyone will be eligible for the
		// new qs.
		if (FDStoreProperties.isQuickshopEnabled()) {
			return true;
		}

		// Currently this is an "employee" check for "partial rollout"...
		// Change below code to modify or extend partial rollout rules

		// 'employee check' (actually a customer profile attribute)
		try {
			FDCustomerModel fdCust = FDCustomerFactory.getFDCustomer(user.getIdentity());

			String eligibleAttr = fdCust.getProfile().getAttribute("QS_ELIGIBLE");
			if ("true".equalsIgnoreCase(eligibleAttr)) {
				return true;
			}

		} catch (FDResourceException e) {
			LOG.warn("Failed to get customer profile", e);
		} catch (Exception e) {
			LOG.warn("Failed to get customer profile", e);
		}

		return false;
	}

	/*
	 * ======================================================= REDIRECT utility
	 * method =======================================================
	 */

	private void redirect(String redirectUrl) throws JspException {
		try {

			HttpServletResponse response = ((HttpServletResponse) pageContext.getResponse());
			response.sendRedirect(response.encodeRedirectURL(redirectUrl));
			//pageContext.getOut().close();
			this.redirected = true;
		} catch (IOException ioe) {
			throw new JspException(ioe.getMessage());
		}
	}

	public int doEndTag() throws JspException {
		if (this.redirected) {
			return SKIP_PAGE;
		} else {
			return super.doEndTag();
		}

	}

	private String getRedirectForNewSO3Page() {
		if (user.isNewSO3Enabled()) {
			// Eligible for the new stuff, just let him through
			return null;
		}

		// Not allowed to see the new stuff, send him back to the old and ugly
		// page
		return URL_NEW_QS_STANDING_ORDERS;
	}
}
