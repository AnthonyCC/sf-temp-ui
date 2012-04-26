package com.freshdirect.fdstore.content;

import java.util.Set;

public abstract class GenericFilterDecorator<N> {
	
	protected Set<EnumFilteringValue> filters;
	
	public GenericFilterDecorator(Set<EnumFilteringValue> filters){
		this.filters=filters;
	}
	
	public abstract void decorateItem(N item);

}
