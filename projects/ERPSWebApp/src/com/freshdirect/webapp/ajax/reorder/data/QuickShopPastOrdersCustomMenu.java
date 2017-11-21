package com.freshdirect.webapp.ajax.reorder.data;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.storeapi.content.FilteringMenuItem;

public class QuickShopPastOrdersCustomMenu {

	private List<FilteringMenuItem> firstMenuItems = new ArrayList<FilteringMenuItem>();
	private List<PastOrdersGroupedByYear> pastOrdersGroupedByYear = new ArrayList<PastOrdersGroupedByYear>();
	
	public List<FilteringMenuItem> getFirstMenuItems() {
		return firstMenuItems;
	}
	public void setFirstMenuItems(List<FilteringMenuItem> firstMenuItems) {
		this.firstMenuItems = firstMenuItems;
	}
	public List<PastOrdersGroupedByYear> getPastOrdersGroupedByYear() {
		return pastOrdersGroupedByYear;
	}
	public void setPastOrdersGroupedByYear(List<PastOrdersGroupedByYear> pastOrdersGroupedByYear) {
		this.pastOrdersGroupedByYear = pastOrdersGroupedByYear;
	}
}
