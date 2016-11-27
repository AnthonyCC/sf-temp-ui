package com.freshdirect.webapp.ajax.filtering;

import java.util.List;

import com.freshdirect.webapp.ajax.browse.data.MenuBoxData;
import com.freshdirect.webapp.ajax.browse.data.MenuItemData;

public class MenuBoxDataService {

	private static final MenuBoxDataService INSTANCE = new MenuBoxDataService();

	private MenuBoxDataService() {
	}

	public static MenuBoxDataService getDefaultMenuBoxDataService() {
		return INSTANCE;
	}

	public MenuBoxData getMenuBoxById(String id, List<MenuBoxData> menuBoxes) {
		MenuBoxData result = null;
		for (MenuBoxData item : menuBoxes) {
			if (id != null && id.equals(item.getId())) {
				result = item;
				break;
			}
		}
		return result;
	}
	
	public void removeHitCountFromAllFilters(MenuBoxData menuBoxData) {
		if (menuBoxData != null) {
			for (MenuItemData menuItemData : menuBoxData.getItems()) {
				if ("all".equals(menuItemData.getId())) {
					menuItemData.setHitCount(null);
					break;
				}
			}
		}
	}
}
