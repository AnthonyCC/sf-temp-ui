package com.freshdirect.fdstore.content;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public abstract class GenericFilteringMenuBuilder<N> {

	//left side filtering menu - the key in the map inside is for simplified usage only (it's the same name as the name property in the FilteringMenuItem)
	protected Map<EnumFilteringValue, Map<String,FilteringMenuItem>> domains=new HashMap<EnumFilteringValue, Map<String,FilteringMenuItem>>();

	protected Set<EnumFilteringValue> filters;
	
	Map<EnumFilteringValue, List<Object>> filterValues;
	
	public GenericFilteringMenuBuilder(Map<EnumFilteringValue, List<Object>> filterValues, Set<EnumFilteringValue> filters) {
		this.filterValues = filterValues;
		this.filters = filters;
	}

	public abstract void buildMenu(List<N> items);

	public Map<EnumFilteringValue, Map<String, FilteringMenuItem>> getDomains() {
		return domains;
	}
	
}
