package com.freshdirect.fdstore.content;

import java.util.List;
import java.util.Map;
import java.util.Set;


public abstract class GenericFilterValueDecoder {
	
	protected Set<EnumFilteringValue> filters;

	public GenericFilterValueDecoder(Set<EnumFilteringValue> filters) {
		this.filters = filters;
	}
	
	public abstract Map<EnumFilteringValue, List<Object>> decode(String encoded);
	
	public abstract String getEncoded(Map<FilteringValue, List<Object>> filterValues);

}
