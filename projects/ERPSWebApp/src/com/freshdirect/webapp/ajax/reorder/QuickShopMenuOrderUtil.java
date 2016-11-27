package com.freshdirect.webapp.ajax.reorder;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.freshdirect.fdstore.content.ComparatorChain;
import com.freshdirect.fdstore.content.EnumQuickShopFilteringValue;
import com.freshdirect.fdstore.content.FilteringMenuItem;
import com.freshdirect.fdstore.content.FilteringValue;
import com.freshdirect.framework.util.DateUtil;


public class QuickShopMenuOrderUtil {
	
	public static void sortMenuItems(Map<String, Map<FilteringValue, List<FilteringMenuItem>>> menu){
		if(menu.get(EnumQuickShopFilteringValue.ORDERS_BY_DATE.getParent())!=null){
			Collections.sort(menu.get(EnumQuickShopFilteringValue.ORDERS_BY_DATE.getParent()).get(EnumQuickShopFilteringValue.ORDERS_BY_DATE), ORDER_DATE_AND_ID_COMPARATOR);
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
		@Override
		public int compare(FilteringMenuItem o1, FilteringMenuItem o2) {
			return o2.getDeliveryDate().compareTo(o1.getDeliveryDate());
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
	
	private static final Comparator<FilteringMenuItem> ORDER_ID_COMPARATOR = new Comparator<FilteringMenuItem>() {

		@Override
		public int compare(FilteringMenuItem o1, FilteringMenuItem o2) {
			BigInteger orderId1 = new BigInteger(o1.getFilteringUrlValue());
			BigInteger orderId2 = new BigInteger(o2.getFilteringUrlValue());
			return orderId2.compareTo(orderId1);
		}
	};
	
	@SuppressWarnings("unchecked")
	private static final ComparatorChain<FilteringMenuItem> ORDER_DATE_AND_ID_COMPARATOR = ComparatorChain.create(ORDER_COMPARATOR, ORDER_ID_COMPARATOR);

}
