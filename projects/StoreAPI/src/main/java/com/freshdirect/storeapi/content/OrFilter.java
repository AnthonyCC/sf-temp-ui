package com.freshdirect.storeapi.content;

import java.util.List;

import com.freshdirect.fdstore.FDResourceException;

public class OrFilter extends AbstractCombinationFilter {

	public OrFilter(String id, String parentId, String name, boolean invert, List<ProductItemFilterI> filters) {
		super(id, parentId, name, invert, filters);
	}

	@Override
	public boolean apply(FilteringProductItem prod) throws FDResourceException {
		
		for(ProductItemFilterI filter : filters){
			if(filter.apply(prod)){
				return invertChecker(true);
			}
		}
		
		return invertChecker(false);
	}

}
