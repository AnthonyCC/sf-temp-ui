package com.freshdirect.smartstore.filter;

import java.util.HashSet;
import java.util.Set;

import com.freshdirect.fdstore.content.ProductModel;

/**
 * This filter removes item duplicates producing a list of unique items
 * 
 * @author segabor
 *
 */
public final class UnicityFilter extends ProductFilter {
	Set keys;
	
	public UnicityFilter() {
		this.keys = new HashSet();
	}
	
	public ProductModel filter(ProductModel model) {
		if (!keys.contains(model)) {
			keys.add(model);
			return model;
		}
		return null;
	}
	
	public void reset() {
		keys.clear();
	}
}