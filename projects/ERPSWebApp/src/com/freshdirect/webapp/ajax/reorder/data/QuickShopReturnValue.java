package com.freshdirect.webapp.ajax.reorder.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.freshdirect.storeapi.content.EnumQuickShopFilteringValue;
import com.freshdirect.storeapi.content.FilteringMenuItem;
import com.freshdirect.storeapi.content.FilteringValue;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItem;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopListDetails;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopPagerValues;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopSorterValues;

/**
 * Grouping return values for quickshop (JSON ready)
 *
 */
public class QuickShopReturnValue implements Serializable {

	private static final long serialVersionUID = -1974804524813407050L;
	
	private Map<String, List<QuickShopLineItem>> items;
	private Map<String, Map<FilteringValue, List<FilteringMenuItem>>> menu;
	private QuickShopPagerValues pager;
	private Map<String,List<QuickShopSorterValues>> sorter;
	private String searchTerm;
	private QuickShopListDetails listDetails;
	private QuickShopPastOrdersCustomMenu orders;
	
	public QuickShopReturnValue(List<QuickShopLineItem> items, Map<FilteringValue, Map<String, FilteringMenuItem>> menu, QuickShopPagerValues pager, List<QuickShopSorterValues> sorter, String searchTerm, QuickShopListDetails listDetails) {
		super();
		prepareSorter(sorter);
		prepareItems(items);
		redesignMenu(menu);
		this.pager = pager;
		this.searchTerm = searchTerm;
		this.listDetails = listDetails;
	}
	
	private void prepareSorter(List<QuickShopSorterValues> sorterSource){
		
		sorter=new HashMap<String,List<QuickShopSorterValues>>();
		sorter.put("data", sorterSource);
	}
	
	/**
	 * @param itemsSource
	 * Transform return value into convenience format for frontend
	 */
	private void prepareItems(List<QuickShopLineItem> itemsSource){
		
		items=new HashMap<String, List<QuickShopLineItem>>();
		items.put("data", itemsSource);
	}
	
	/**
	 * @param menuSource
	 * Transform return value into convenience format for frontend
	 */
	private void redesignMenu(Map<FilteringValue, Map<String, FilteringMenuItem>> menuSource){
		
		menu = new HashMap<String, Map<FilteringValue, List<FilteringMenuItem>>>();
		
		for(FilteringValue filteringValue: menuSource.keySet()){
			EnumQuickShopFilteringValue fv = (EnumQuickShopFilteringValue) filteringValue;
			if(menu.get(fv.getParent())!=null){
				menu.get(fv.getParent()).put(filteringValue, new ArrayList<FilteringMenuItem>(menuSource.get(filteringValue).values()));
			}else{
				Map<FilteringValue, List<FilteringMenuItem>> subMenu = new TreeMap<FilteringValue, List<FilteringMenuItem>>();
				subMenu.put(filteringValue, new ArrayList<FilteringMenuItem>(menuSource.get(filteringValue).values()));
				menu.put(fv.getParent(), subMenu);
			}
		}
	}

	public QuickShopReturnValue(List<QuickShopLineItem> items, QuickShopPagerValues pager, List<QuickShopSorterValues> sorter) {
		super();
		prepareItems(items);
		prepareSorter(sorter);
		this.pager = pager;
	}

	public Map<String, List<QuickShopLineItem>> getItems() {
		return items;
	}

	public void setItems(List<QuickShopLineItem> items) {
		prepareItems(items);
	}

	public Map<String, Map<FilteringValue, List<FilteringMenuItem>>> getMenu() {
		return menu;
	}

	public void setMenu(Map<FilteringValue, Map<String, FilteringMenuItem>> menu) {
		redesignMenu(menu);
	}

	public QuickShopPagerValues getPager() {
		return pager;
	}

	public void setPager(QuickShopPagerValues pager) {
		this.pager = pager;
	}

	public Map<String, List<QuickShopSorterValues>> getSorter() {
		return sorter;
	}

	public void setSorter(Map<String, List<QuickShopSorterValues>> sorter) {
		this.sorter = sorter;
	}

	public String getSearchTerm() {
		return searchTerm;
	}

	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}

	public QuickShopListDetails getListDetails() {
		return listDetails;
	}

	public void setListDetails(QuickShopListDetails listDetails) {
		this.listDetails = listDetails;
	}

	public QuickShopPastOrdersCustomMenu getOrders() {
		return orders;
	}

	public void setOrders(QuickShopPastOrdersCustomMenu orders) {
		this.orders = orders;
	}

}
