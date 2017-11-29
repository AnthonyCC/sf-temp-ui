package com.freshdirect.webapp.ajax.filtering;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.freshdirect.storeapi.content.FilteringProductItem;
import com.freshdirect.storeapi.content.ProductItemFilterI;
import com.freshdirect.webapp.ajax.browse.data.MenuBoxData;
import com.freshdirect.webapp.ajax.browse.data.MenuItemData;

public class MenuItemSorter {

	private class HitCountComparator implements Comparator<MenuItemData> {

		private HitCountComparator(List<FilteringProductItem> items, Map<String, ProductItemFilterI> allFilters, List<MenuItemData> menuItems, String menuBoxId) {
			for (MenuItemData menuItem : menuItems) {
				if (menuItem.getHitCount() == null) {
					String menuItemId = menuItem.getId();
					ProductItemFilterI filter = allFilters.get(ProductItemFilterUtil.createCompositeId(menuBoxId, menuItemId));
					final int hitCount = ProductItemFilterUtil.countItemsForFilter(items, filter);
					menuItem.setHitCount(hitCount);
				}
			}
		}

		@Override
		public int compare(MenuItemData o1, MenuItemData o2) {
			return o2.getHitCount().compareTo(o1.getHitCount());
		}

	}

	private class NameComparator implements Comparator<MenuItemData> {

		@Override
		public int compare(MenuItemData o1, MenuItemData o2) {
			return o1.getName().compareToIgnoreCase(o2.getName());
		}

	}

	private static final MenuItemSorter INSTANCE = new MenuItemSorter();

	private MenuItemSorter() {
	}

	public static MenuItemSorter getDefaultMenuItemSorter() {
		return INSTANCE;
	}

	public void sortItemsByHitCount(MenuBoxData menuBoxData, List<FilteringProductItem> items, Map<String, ProductItemFilterI> allFilters) {
		HitCountComparator hitCountComparator = new HitCountComparator(items, allFilters, menuBoxData.getItems(), menuBoxData.getId());
		Collections.sort(menuBoxData.getItems(), hitCountComparator);
	}

	public void sortItemsByName(MenuBoxData menuBoxData) {
		NameComparator nameComparator = new NameComparator();
		List<MenuItemData> menuItems = menuBoxData.getItems();
		if (menuItems.size() > 1) {
			List<MenuItemData> menuItemsExceptTheAll = menuItems.subList(1, menuItems.size());
			Collections.sort(menuItemsExceptTheAll, nameComparator);
		}
	}
}
