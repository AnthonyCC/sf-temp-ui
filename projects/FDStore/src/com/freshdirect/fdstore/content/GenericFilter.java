package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class GenericFilter<T> {
	
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
	
	@Override
	public abstract GenericFilter<T> clone();
	
	public abstract void applyAllFilterAnd(List<T> items);
	public abstract void applyAllFilterOr(List<T> items);
	
	public abstract void applyFilter(List<T> items, EnumFilteringValue filter);
	
	public Map<EnumFilteringValue, List<Object>> getFilterValues(){
		return filterValues;
	}

}
