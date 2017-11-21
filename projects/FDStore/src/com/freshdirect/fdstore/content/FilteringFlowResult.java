package com.freshdirect.fdstore.content;

import java.util.List;
import java.util.Map;

import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.FilteringMenuItem;
import com.freshdirect.storeapi.content.FilteringSortingItem;
import com.freshdirect.storeapi.content.FilteringValue;

public class FilteringFlowResult<N extends ContentNodeModel> {
	
	private List<FilteringSortingItem<N>> items;
	private Map<FilteringValue, Map<String, FilteringMenuItem>> menu;
	
	public FilteringFlowResult(List<FilteringSortingItem<N>> items, Map<FilteringValue, Map<String, FilteringMenuItem>> menu) {
		super();
		this.items = items;
		this.menu = menu;
	}

	public List<FilteringSortingItem<N>> getItems() {
		return items;
	}

	public void setItems(List<FilteringSortingItem<N>> items) {
		this.items = items;
	}

	public Map<FilteringValue, Map<String, FilteringMenuItem>> getMenu() {
		return menu;
	}

	public void setMenu(Map<FilteringValue, Map<String, FilteringMenuItem>> menu) {
		this.menu = menu;
	}

}
