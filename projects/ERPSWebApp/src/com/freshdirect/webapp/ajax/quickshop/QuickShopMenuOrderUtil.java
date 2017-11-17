package com.freshdirect.webapp.ajax.quickshop;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.storeapi.content.EnumQuickShopFilteringValue;
import com.freshdirect.storeapi.content.FilteringMenuItem;
import com.freshdirect.storeapi.content.FilteringValue;

/**
 * Deprecated with Quickshop 2.2 version. Replaced with {@link com.freshdirect.webapp.ajax.reorder.QuickShopMenuOrderUtil}
 */
@Deprecated
public class QuickShopMenuOrderUtil {
	
	public static void sortMenuItems(Map<String, Map<FilteringValue, List<FilteringMenuItem>>> menu){
		if(menu.get(EnumQuickShopFilteringValue.ORDERS_BY_DATE.getParent())!=null){
			Collections.sort(menu.get(EnumQuickShopFilteringValue.ORDERS_BY_DATE.getParent()).get(EnumQuickShopFilteringValue.ORDERS_BY_DATE), ORDER_COMPARATOR);			
		}
		if(menu.get(EnumQuickShopFilteringValue.YOUR_LISTS.getParent())!=null){
			Collections.sort(menu.get(EnumQuickShopFilteringValue.YOUR_LISTS.getParent()).get(EnumQuickShopFilteringValue.YOUR_LISTS), AZ_COMPARATOR);
		}
		if(menu.get(EnumQuickShopFilteringValue.STARTER_LISTS.getParent())!=null){
			Collections.sort(menu.get(EnumQuickShopFilteringValue.STARTER_LISTS.getParent()).get(EnumQuickShopFilteringValue.STARTER_LISTS), AZ_COMPARATOR);
		}
		if(menu.get(EnumQuickShopFilteringValue.DEPT.getParent())!=null){
			Collections.sort(menu.get(EnumQuickShopFilteringValue.DEPT.getParent()).get(EnumQuickShopFilteringValue.DEPT), AZ_COMPARATOR);
		}
		if(menu.get(EnumQuickShopFilteringValue.GLUTEN_FREE.getParent())!=null){
			//these are actualy domains so the sorting is a bit more complicated
			Map<FilteringValue, List<FilteringMenuItem>> subMenu = menu.get(EnumQuickShopFilteringValue.GLUTEN_FREE.getParent());
			List<FilteringValue> filters = new ArrayList<FilteringValue>(subMenu.keySet());
			Collections.sort(filters, AZ_FILTERINGVALUE_COMPARATOR);
			for(FilteringValue fv : filters){
				subMenu.put(fv, subMenu.get(fv));				
			}
		}
	}
	
	/** Sorts orders by dlv. start time, descending */
	private final static Comparator<FilteringMenuItem> ORDER_COMPARATOR = new Comparator<FilteringMenuItem>() {
		private final SimpleDateFormat formatter = new SimpleDateFormat(DateUtil.MONTH_DAY_YEAR_DAYOFWEEK_FORMATTER_STRING);
		@Override
		public int compare(FilteringMenuItem o1, FilteringMenuItem o2) {
			try {
				Date d1 = formatter.parse(o1.getName());
				Date d2 = formatter.parse(o2.getName());
				return d2.compareTo(d1);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return 0;
		}
	};
	
	/** Sorts menu items by A-Z */
	private final static Comparator<FilteringMenuItem> AZ_COMPARATOR = new Comparator<FilteringMenuItem>() {

		@Override
		public int compare(FilteringMenuItem o1, FilteringMenuItem o2) {
			return o1.getName().compareToIgnoreCase(o2.getName());
		}
	};
	
	/** Sorts menu items (domains) by A-Z */
	private final static Comparator<FilteringValue> AZ_FILTERINGVALUE_COMPARATOR = new Comparator<FilteringValue>() {

		@Override
		public int compare(FilteringValue o1, FilteringValue o2) {
			return o1.getDisplayName().compareTo(o2.getDisplayName());
		}
	};

}
