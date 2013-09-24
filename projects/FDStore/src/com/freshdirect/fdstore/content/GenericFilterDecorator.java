package com.freshdirect.fdstore.content;

import java.util.Set;

public abstract class GenericFilterDecorator<N> {
	
	protected Set<FilteringValue> filters;
	
	public GenericFilterDecorator(Set<FilteringValue> filters){
		this.filters=filters;
	}
	
	public abstract void decorateItem(N item);

	public void setFilters(Set<FilteringValue> filters) {
		this.filters = filters;
	}

}
