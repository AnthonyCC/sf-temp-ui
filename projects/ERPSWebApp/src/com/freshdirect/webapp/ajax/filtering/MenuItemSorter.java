package com.freshdirect.webapp.ajax.filtering;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.freshdirect.webapp.ajax.browse.data.MenuBoxData;
import com.freshdirect.webapp.ajax.browse.data.MenuItemData;

public class MenuItemSorter {

    private static final Comparator<MenuItemData> HIT_COUNT_COMPARATOR = new Comparator<MenuItemData>() {

        @Override
        public int compare(MenuItemData o1, MenuItemData o2) {
            return o2.getHitCount().compareTo(o1.getHitCount());
        }

    };

    private static final Comparator<MenuItemData> NAME_COMPARATOR = new Comparator<MenuItemData>() {

        @Override
        public int compare(MenuItemData o1, MenuItemData o2) {
            return o1.getName().compareToIgnoreCase(o2.getName());
        }

    };

    private static final MenuItemSorter INSTANCE = new MenuItemSorter();

    private MenuItemSorter() {
    }

    public static MenuItemSorter getDefaultMenuItemSorter() {
        return INSTANCE;
    }

    public void sortItemsByHitCount(MenuBoxData menuBoxData) {
        if (menuBoxData != null) {
            List<MenuItemData> menuItems = menuBoxData.getItems();
            if (menuItems.size() > 1) {
                List<MenuItemData> menuItemsExceptTheAll = menuItems.subList(1, menuItems.size());
                Collections.sort(menuItemsExceptTheAll, HIT_COUNT_COMPARATOR);
            }
        }
    }

    public void sortItemsByName(MenuBoxData menuBoxData) {
        if (menuBoxData != null) {
            List<MenuItemData> menuItems = menuBoxData.getItems();
            if (menuItems.size() > 1) {
                List<MenuItemData> menuItemsExceptTheAll = menuItems.subList(1, menuItems.size());
                Collections.sort(menuItemsExceptTheAll, NAME_COMPARATOR);
            }
        }
    }
}
