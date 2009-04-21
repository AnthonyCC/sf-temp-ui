package com.freshdirect.smartstore.filter;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.fdstore.content.ProductModel;

public class ArrayFilter extends ProductFilter {
	List filters;
	
	protected ArrayFilter() {
		filters = new ArrayList();
	}
	
	public ProductModel filter(ProductModel model) {
		for (int i = 0; i < filters.size(); i++) {
			model = ((ProductFilter) filters.get(i)).filter(model);
			if (model == null)
				return null;
		}
		return model;
	}
	
	public void addFilter(ProductFilter filter) {
		filters.add(filter);
	}
}
