package com.freshdirect.fdstore.content;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FilteringSortingItemFilter<N extends ContentNodeModel> extends GenericFilter<FilteringSortingItem<N>> {

	public FilteringSortingItemFilter(Map<EnumFilteringValue, List<Object>> filterValues, Set<EnumFilteringValue> filters) {
		super(filterValues, filters);
	}

	public void applyAllFilterAnd( List<FilteringSortingItem<N>> items ) {
		for ( EnumFilteringValue filter : filters ) {
			if ( filterValues.get( filter ) != null ) {
				applyFilter( items, filter );
			}
		}
	}

	public void applyAllFilterOr(List<FilteringSortingItem<N>> items) {
		
		Iterator<FilteringSortingItem<N>> it = items.iterator();
		
		while ( it.hasNext() ) {
			FilteringSortingItem<N> item = it.next();
			boolean passed = false;
			
			filterLoop : 
			for ( EnumFilteringValue filter : filters ) {
				if ( filterValues.get(filter) != null ) {
					Object itemFilteringValue = item.getFilteringValue(filter);
					if (itemFilteringValue != null) {
						if(itemFilteringValue instanceof Set){
							Set<?> fvSet=(Set<?>)itemFilteringValue;
							for (Object filteringValue : filterValues.get(filter)) {
								if (fvSet.contains(filteringValue)) {
									passed = true;
									break filterLoop;
								}
							}
						} else{
							for (Object filteringValue : filterValues.get(filter)) {
								if (itemFilteringValue.equals(filteringValue)) {
									passed = true;
									break filterLoop;
								}
							}					
						}
					}
				}
			}
			
			if ( !passed ) {
				it.remove();
			}
		}
	}
	
	public void applyFilter(List<FilteringSortingItem<N>> items, EnumFilteringValue filter) {

		Iterator<FilteringSortingItem<N>> it = items.iterator();

		while (it.hasNext()) {

			FilteringSortingItem<N> item = it.next();
			Object itemFilteringValue = item.getFilteringValue(filter);
				
			boolean passed = false;
			if (itemFilteringValue != null) {
				if(itemFilteringValue instanceof Set){
					Set<?> fvSet=(Set<?>)itemFilteringValue;
					for (Object filteringValue : filterValues.get(filter)) {
						if (fvSet.contains(filteringValue)) {
							passed = true;
							break;
						}
					}
				} else{
					for (Object filteringValue : filterValues.get(filter)) {
						if (itemFilteringValue.equals(filteringValue)) {
							passed = true;
							break;
						}
					}					
				}
			}

			if ( !passed ) {
				it.remove();
			}
		}
	}

	@Override
	public GenericFilter<FilteringSortingItem<N>> clone() {
		return new FilteringSortingItemFilter<N>(new HashMap<EnumFilteringValue, List<Object>>(this.filterValues), new HashSet<EnumFilteringValue>(this.filters));
	}

}
