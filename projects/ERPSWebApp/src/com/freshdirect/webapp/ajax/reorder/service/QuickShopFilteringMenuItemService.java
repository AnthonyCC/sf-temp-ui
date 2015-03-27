package com.freshdirect.webapp.ajax.reorder.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.freshdirect.fdstore.content.FilteringMenuItem;
import com.freshdirect.webapp.ajax.reorder.data.PastOrdersGroupedByYear;

public class QuickShopFilteringMenuItemService {

	private static final String YOUR_LAST_ORDER_MENU_ITEM_LABEL = "Your Last Order";

	private static final QuickShopFilteringMenuItemService INSTANCE = new QuickShopFilteringMenuItemService();

	private static final Comparator<PastOrdersGroupedByYear> PAST_ORDERS_YEAR_COMPARATOR = new Comparator<PastOrdersGroupedByYear>() {

		@Override
		public int compare(PastOrdersGroupedByYear o1, PastOrdersGroupedByYear o2) {
			return o2.getYear().compareTo(o1.getYear());
		}
	};

	private QuickShopFilteringMenuItemService() {
	}

	public static QuickShopFilteringMenuItemService defaultService() {
		return INSTANCE;
	}

	public void addMenuItemToPastOrdersGroupedByYear(List<PastOrdersGroupedByYear> pastOrdersGroupedByYear, FilteringMenuItem menuItem) {
		Integer year = Integer.parseInt(menuItem.getYear());
		List<FilteringMenuItem> menuItemsGroupedByYear = null;
		for (PastOrdersGroupedByYear pastOrders : pastOrdersGroupedByYear) {
			if (year.equals(pastOrders.getYear())) {
				menuItemsGroupedByYear = pastOrders.getOrders();
			}
		}
		if (menuItemsGroupedByYear == null) {
			PastOrdersGroupedByYear pastOrders = new PastOrdersGroupedByYear(year);
			menuItemsGroupedByYear = new ArrayList<FilteringMenuItem>();
			pastOrders.setOrders(menuItemsGroupedByYear);
			pastOrdersGroupedByYear.add(pastOrders);
		}
		menuItemsGroupedByYear.add(menuItem);
	}
	
	public void sortPastOrdersByYear(List<PastOrdersGroupedByYear> pastOrders) {
		Collections.sort(pastOrders, PAST_ORDERS_YEAR_COMPARATOR);
	}

	public void updateLatestMenuItem(List<FilteringMenuItem> firstMenuItems, String lastOrderId) {
		if (firstMenuItems != null && 0 < firstMenuItems.size()) {
			FilteringMenuItem menuItem = firstMenuItems.get(0);
			if (lastOrderId != null && lastOrderId.equals(menuItem.getFilteringUrlValue())) {
				menuItem.setName(YOUR_LAST_ORDER_MENU_ITEM_LABEL);
			}
		}
	}

}
