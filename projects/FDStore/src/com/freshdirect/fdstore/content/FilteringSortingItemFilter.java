package com.freshdirect.fdstore.content;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FilteringSortingItemFilter extends GenericFilter<FilteringSortingItem> {

	public FilteringSortingItemFilter(Map<EnumFilteringValue, List<Object>> filterValues, Set<EnumFilteringValue> filters) {
		super(filterValues, filters);
	}

	public void applyAllFilter(List<FilteringSortingItem> items) {
		for (EnumFilteringValue filter : filters) {
			if(filterValues.get(filter) != null){
				applyFilter(items, filter);				
			}
		}
	}

	public void applyFilter(List<FilteringSortingItem> items, EnumFilteringValue filter) {

		Iterator<FilteringSortingItem> it = items.iterator();

		while (it.hasNext()) {

			FilteringSortingItem item = it.next();
			Object itemFilteringValue = item.getFilteringValue(filter);

				
			boolean passed = false;
			if (itemFilteringValue != null) {
				if(itemFilteringValue instanceof Set){
					Set<Object> fvSet=(Set<Object>)itemFilteringValue;
					for (Object filteringValue : filterValues.get(filter)) {
						for(Object fv: fvSet){
							if(fv.equals(filteringValue)){
								passed = true;
							}
						}
					}
				} else{
					for (Object filteringValue : filterValues.get(filter)) {
						if (itemFilteringValue.equals(filteringValue)) {
							passed = true;
						}
					}					
				}
			}

			if (!passed) {
				it.remove();
			}
		}
	}

	@Override
	public GenericFilter<FilteringSortingItem> clone() {
		return new FilteringSortingItemFilter(new HashMap<EnumFilteringValue, List<Object>>(this.filterValues), new HashSet<EnumFilteringValue>(this.filters));
	}

}
