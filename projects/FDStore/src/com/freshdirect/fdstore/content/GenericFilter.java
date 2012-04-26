package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class GenericFilter<N> {
	
	//List<GenericFilterValue> filterValues;
	Map<EnumFilteringValue, List<Object>> filterValues;
	Set<EnumFilteringValue> filters;
	
	public GenericFilter(Map<EnumFilteringValue, List<Object>>  filterValues, Set<EnumFilteringValue> filters) { 
		this.filters=filters;
		this.filterValues=filterValues;
	}
	
	public void addFilterValue(EnumFilteringValue key, Object value){
		if(filterValues.get(key)!=null){
			filterValues.get(key).add(value);
		}else{
			List<Object> values=new ArrayList<Object>();
			values.add(value);
			filterValues.put(key, values);
		}
	}

	public void removeFilterValues(FilteringValue key){
		filterValues.remove(key);
	}
	
	public abstract GenericFilter<N> clone();
	
	public abstract void applyAllFilter(List<N> items);
	
	public abstract void applyFilter(List<N> items, EnumFilteringValue filter);
	
	public Map<EnumFilteringValue, List<Object>> getFilterValues(){
		return filterValues;
	}

}
