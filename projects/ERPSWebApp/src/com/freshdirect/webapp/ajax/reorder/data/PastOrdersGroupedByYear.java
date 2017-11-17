package com.freshdirect.webapp.ajax.reorder.data;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.storeapi.content.FilteringMenuItem;

public class PastOrdersGroupedByYear {

	private Integer year;
	private List<FilteringMenuItem> orders = new ArrayList<FilteringMenuItem>();
	
	public PastOrdersGroupedByYear(Integer year) {
		this.year = year;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public List<FilteringMenuItem> getOrders() {
		return orders;
	}

	public void setOrders(List<FilteringMenuItem> orders) {
		this.orders = orders;
	}
}
