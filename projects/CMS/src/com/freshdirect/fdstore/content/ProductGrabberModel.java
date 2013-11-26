package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.ContentKey;

public class ProductGrabberModel extends ContentNodeModelImpl {
	
	private List<ProductFilterModel> productFilters = new ArrayList<ProductFilterModel>();
	private List<ProductContainer> scope = new ArrayList<ProductContainer>();
	
	public ProductGrabberModel(ContentKey key) {
		super(key);
	}

	public List<ProductFilterModel> getProductFilterModels() {
		ContentNodeModelUtil.refreshModels(this, "productFilters", productFilters, false);
		return new ArrayList<ProductFilterModel>(productFilters);
	}

	public List<ProductContainer> getScope() {
		ContentNodeModelUtil.refreshModels(this, "scope", scope, false);
		return new ArrayList<ProductContainer>(scope);
	}

	public List<ProductFilterI> getProductFilters(){
		List<ProductFilterI> productFilters = new ArrayList<ProductFilterI>();
		return productFilters;
	}
	
	public Set<ProductModel> getProducts() {
		Set<ProductModel> products = new HashSet<ProductModel>();
		return products;
	}

}
