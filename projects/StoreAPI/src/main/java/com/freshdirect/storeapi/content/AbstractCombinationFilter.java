package com.freshdirect.storeapi.content;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCombinationFilter extends AbstractProductItemFilter {
	
	protected List<ProductItemFilterI> filters;

	public AbstractCombinationFilter(String id, String parentId, String name, boolean invert, List<ProductItemFilterI> filters) {
		super(id, parentId, name, invert);
		this.filters = filters;
	}

	@Override
	public FilterCacheStrategy getCacheStrategy() {
		List<FilterCacheStrategy> cacheStrategies = new ArrayList<FilterCacheStrategy>();
		
		for (ProductItemFilterI filter : filters){
			cacheStrategies.add(filter.getCacheStrategy());
		}
		return FilterCacheStrategy.getCompoundCacheStrategy(cacheStrategies);
	}

}
