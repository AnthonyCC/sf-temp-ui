package com.freshdirect.mobileapi.model;

import java.io.Serializable;
import java.util.List;

import com.freshdirect.mobileapi.controller.data.ProductSearchResult;

public class FeaturedCategory implements Serializable{
	
	private Category category;
	
	private List<ProductSearchResult> products ;

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public List<ProductSearchResult> getProducts() {
		return products;
	}

	public void setProducts(List<ProductSearchResult> products) {
		this.products = products;
	}
	
	

}
