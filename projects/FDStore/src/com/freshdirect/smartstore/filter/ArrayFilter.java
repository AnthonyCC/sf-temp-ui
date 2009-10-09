package com.freshdirect.smartstore.filter;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.fdstore.content.ProductModel;

public class ArrayFilter extends ProductFilter {
	List<ProductFilter> filters;
	
	protected ArrayFilter() {
		filters = new ArrayList<ProductFilter>();
	}
	
        public ProductModel filter(ProductModel model) {
            for (int i = 0; i < filters.size(); i++) {
                model = filters.get(i).filter(model);
                if (model == null) {
                    return null;
                }
            }
            return model;
        }
	
	public void addFilter(ProductFilter filter) {
		filters.add(filter);
	}
}
