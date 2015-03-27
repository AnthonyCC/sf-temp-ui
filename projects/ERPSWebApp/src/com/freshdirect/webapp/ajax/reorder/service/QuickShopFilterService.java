package com.freshdirect.webapp.ajax.reorder.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.EnumQuickShopFilteringValue;
import com.freshdirect.fdstore.content.FilteringFlowResult;
import com.freshdirect.fdstore.content.FilteringMenuItem;
import com.freshdirect.fdstore.content.FilteringSortingItem;
import com.freshdirect.fdstore.content.FilteringValue;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.FilteringNavigator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.reorder.QuickShopFilterImpl;
import com.freshdirect.webapp.ajax.reorder.QuickShopHelper;
import com.freshdirect.webapp.ajax.reorder.QuickShopServlet;
import com.freshdirect.webapp.ajax.reorder.data.EnumQuickShopTab;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItemWrapper;
import com.freshdirect.webapp.ajax.reorder.data.QuickShopListRequestObject;
import com.freshdirect.webapp.ajax.reorder.data.QuickShopPastOrdersCustomMenu;

public class QuickShopFilterService {

	private static final Logger LOG = LoggerFactory.getInstance(QuickShopFilterService.class);

	private static final QuickShopFilterService INSTANCE = new QuickShopFilterService();

	private QuickShopFilterService() {
	}

	public static QuickShopFilterService defaultService() {
		return INSTANCE;
	}

	public FilteringFlowResult<QuickShopLineItemWrapper> collectQuickShopLineItemForPastOrders(FDUserI user, HttpSession session, FilteringNavigator nav, Set<FilteringValue> filters,
			QuickShopListRequestObject requestData) throws FDResourceException {
		EnumQuickShopTab tab = EnumQuickShopTab.PAST_ORDERS;
		return collectQuickShopLineItem(user, session, nav, filters, tab.cacheName, tab, requestData);
	}

	public FilteringFlowResult<QuickShopLineItemWrapper> collectQuickShopLineItemForTopItems(FDUserI user, HttpSession session, FilteringNavigator nav, Set<FilteringValue> filters)
			throws FDResourceException {
		EnumQuickShopTab tab = EnumQuickShopTab.TOP_ITEMS;
		return collectQuickShopLineItem(user, session, nav, filters, tab.cacheName, tab, null);
	}

	public QuickShopPastOrdersCustomMenu transformMenuIntoPastOrdersCustom(Map<String, Map<FilteringValue, List<FilteringMenuItem>>> menu, String lastOrderId) {
		QuickShopPastOrdersCustomMenu result = new QuickShopPastOrdersCustomMenu();
		Map<FilteringValue, List<FilteringMenuItem>> originalPastOrdersMenu = menu.get(EnumQuickShopFilteringValue.ORDERS_BY_DATE.getParent());
		if (originalPastOrdersMenu != null) {
			List<FilteringMenuItem> originalPastOrdersMenuItems = originalPastOrdersMenu.get(EnumQuickShopFilteringValue.ORDERS_BY_DATE);
			List<FilteringMenuItem> selectedMenuItems = new ArrayList<FilteringMenuItem>();
			for (FilteringMenuItem menuItem : originalPastOrdersMenuItems) {
				if (result.getFirstMenuItems().size() < FDStoreProperties.getPastOrdersVisibleItemsCount()) {
					result.getFirstMenuItems().add(menuItem);
				} else {
					if (menuItem.isSelected()) {
						selectedMenuItems.add(menuItem);
					} else {
						QuickShopFilteringMenuItemService.defaultService().addMenuItemToPastOrdersGroupedByYear(result.getPastOrdersGroupedByYear(), menuItem);
					}
				}
			}
			QuickShopFilteringMenuItemService.defaultService().sortPastOrdersByYear(result.getPastOrdersGroupedByYear());
			result.getFirstMenuItems().addAll(selectedMenuItems);
			QuickShopFilteringMenuItemService.defaultService().updateLatestMenuItem(result.getFirstMenuItems(), lastOrderId);
		}
		return result;
	}

	private FilteringFlowResult<QuickShopLineItemWrapper> collectQuickShopLineItem(FDUserI user, HttpSession session, FilteringNavigator nav, Set<FilteringValue> filters, String cacheName,
			EnumQuickShopTab tab, QuickShopListRequestObject requestData) throws FDResourceException {
		FilteringFlowResult<QuickShopLineItemWrapper> result = null;
		List<QuickShopLineItemWrapper> items = QuickShopHelper.getWrappedOrderHistoryUsingCache(user, tab, cacheName);
		if (EnumQuickShopTab.PAST_ORDERS.equals(tab)) {
			String yourLastOrderId = getYourLastOrderId(items);
			requestData.setYourLastOrderId(yourLastOrderId);
		}
		QuickShopSearchService.defaultService().search(nav.getSearchTerm(), items);
		List<FilteringSortingItem<QuickShopLineItemWrapper>> filterItems = QuickShopServlet.prepareForFiltering(items);
		QuickShopFilterImpl filter = new QuickShopFilterImpl(nav, user, filters, filterItems, QuickShopHelper.getActiveReplacements(session), tab, requestData);
		LOG.info("Start filtering process");
		result = filter.doFlow(nav, filterItems);
		QuickShopHelper.postProcessPopulate(user, result, session);
		return result;
	}

	private String getYourLastOrderId(List<QuickShopLineItemWrapper> items) {
		String result = null;
		if (items != null && !items.isEmpty()) {
			QuickShopSortingService.defaultService().sortByDeliveryDateAndOrderId(items);
			result = items.get(0).getOrderId();
		}
		return result;
	}

}
