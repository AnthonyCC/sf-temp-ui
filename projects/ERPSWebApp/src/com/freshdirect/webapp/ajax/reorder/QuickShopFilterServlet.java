package com.freshdirect.webapp.ajax.reorder;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.EnumQuickShopFilteringValue;
import com.freshdirect.fdstore.content.FilteringFlowResult;
import com.freshdirect.fdstore.content.FilteringValue;
import com.freshdirect.fdstore.coremetrics.CmContext;
import com.freshdirect.fdstore.coremetrics.CmContextUtility;
import com.freshdirect.fdstore.coremetrics.tagmodel.PageViewTagModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.FilteringNavigator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItemWrapper;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopPagerValues;
import com.freshdirect.webapp.ajax.reorder.data.EnumQuickShopTab;
import com.freshdirect.webapp.ajax.reorder.data.QuickShopListRequestObject;
import com.freshdirect.webapp.ajax.reorder.data.QuickShopPastOrdersCustomMenu;
import com.freshdirect.webapp.ajax.reorder.data.QuickShopReturnValue;
import com.freshdirect.webapp.ajax.reorder.service.QuickShopFilterService;

public class QuickShopFilterServlet extends QuickShopServlet {

	private static final long serialVersionUID = -2275457565029351766L;

	private static final Logger LOG = LoggerFactory.getInstance(QuickShopFilterServlet.class);

	public static final Set<FilteringValue> TOP_ITEMS_FILTERS = new HashSet<FilteringValue>();
	static {
		TOP_ITEMS_FILTERS.add(EnumQuickShopFilteringValue.DEPT);
		TOP_ITEMS_FILTERS.add(EnumQuickShopFilteringValue.GLUTEN_FREE);
		TOP_ITEMS_FILTERS.add(EnumQuickShopFilteringValue.KOSHER);
		TOP_ITEMS_FILTERS.add(EnumQuickShopFilteringValue.LOCAL);
		TOP_ITEMS_FILTERS.add(EnumQuickShopFilteringValue.ORGANIC);
		TOP_ITEMS_FILTERS.add(EnumQuickShopFilteringValue.ON_SALE);
		TOP_ITEMS_FILTERS.add(EnumQuickShopFilteringValue.BRAND);
	}
	
	public static final Set<FilteringValue> PAST_ORDERS_FILTERS = new HashSet<FilteringValue>();
	static {
		PAST_ORDERS_FILTERS.add(EnumQuickShopFilteringValue.ORDERS_BY_DATE);
		PAST_ORDERS_FILTERS.add(EnumQuickShopFilteringValue.DEPT);
		PAST_ORDERS_FILTERS.add(EnumQuickShopFilteringValue.GLUTEN_FREE);
		PAST_ORDERS_FILTERS.add(EnumQuickShopFilteringValue.KOSHER);
		PAST_ORDERS_FILTERS.add(EnumQuickShopFilteringValue.LOCAL);
		PAST_ORDERS_FILTERS.add(EnumQuickShopFilteringValue.ORGANIC);
		PAST_ORDERS_FILTERS.add(EnumQuickShopFilteringValue.ON_SALE);
	}

	@Override
	protected boolean synchronizeOnUser() {
		return false; //no need to synchronize
	}
	
	@Override
	protected QuickShopReturnValue process(FDUserI user, HttpServletRequest request, QuickShopListRequestObject requestData) throws HttpErrorResponse {
		LOG.info("Start processing request...");

		FilteringNavigator nav = requestData.convertToFilteringNavigator();
		FilteringFlowResult<QuickShopLineItemWrapper> result = null;
		try {
			EnumQuickShopTab tab = requestData.getTabType();
			if (EnumQuickShopTab.TOP_ITEMS.equals(tab)) {
                result = QuickShopFilterService.defaultService().collectQuickShopLineItemForTopItems(request, user, request.getSession(), nav, TOP_ITEMS_FILTERS);
			} else {
                result = QuickShopFilterService.defaultService().collectQuickShopLineItemForPastOrders(request, user, request.getSession(), nav, PAST_ORDERS_FILTERS, requestData);
			}
		} catch (FDResourceException e) {
			LOG.error("Error while collecting order history", e);
			returnHttpError(500, "Error while collecting order history", e);
		}

		QuickShopReturnValue responseData = new QuickShopReturnValue(unwrapResult(createPage(user, request, requestData, result.getItems())), result.getMenu(), new QuickShopPagerValues(requestData.getPageSize(),
				result.getItems().size(), requestData.getActivePage()), generateSorter(nav), nav.getSearchTerm(), null);
		QuickShopMenuOrderUtil.sortMenuItems(responseData.getMenu());
		QuickShopPastOrdersCustomMenu transformMenuIntoPastOrdersCustom = QuickShopFilterService.defaultService().transformMenuIntoPastOrdersCustom(responseData.getMenu(), requestData.getYourLastOrderId());
		responseData.setOrders(transformMenuIntoPastOrdersCustom);
		// [APPDEV-4558]
		if (CmContextUtility.isCoremetricsAvailable(user)) {
			addCoremetricsTags(nav, result, responseData);
		}
		return responseData;
	}

	private void addCoremetricsTags(FilteringNavigator nav, FilteringFlowResult<QuickShopLineItemWrapper> result, QuickShopReturnValue responseData) {
		// Generate coremetrics 'element' tags for the menu - selected filters
		generateCoremetricsElementTags(responseData, result.getMenu(), "quickshop | past_orders");

		// Generate coremetrics 'pageview' tag - only when searching
		String searchTerm = nav.getSearchTerm();
		if (searchTerm != null && searchTerm.trim().length() > 0) {
			PageViewTagModel pvTagModel = new PageViewTagModel();
			pvTagModel.setCategoryId( CmContext.getContext().prefixedCategoryId( "quickshop | search"));
			pvTagModel.setPageId("past_orders");
			pvTagModel.setSearchTerm(searchTerm);
			pvTagModel.setSearchResults(Integer.toString(result.getItems().size()));
			responseData.addCoremetrics(pvTagModel.toStringList());
		}
	}

}
