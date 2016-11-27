package com.freshdirect.mobileapi.controller.data.response;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.mobileapi.controller.data.Message;

/**
 * @author Rob
 *
 */
public class FilterGroup {

	private String name;
	private String id;
	private List<FilterGroupItem> filterGroupItems = new ArrayList<FilterGroupItem>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<FilterGroupItem> getFilterGroupItems() {
		return filterGroupItems;
	}
	public void setFilterGroupItems(List<FilterGroupItem> filterGroupItems) {
		this.filterGroupItems = filterGroupItems;
	}
	
	public void addFilterGroupItem(FilterGroupItem item){
		this.filterGroupItems.add(item);
	}
	

}
