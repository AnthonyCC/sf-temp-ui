package com.freshdirect.mobileapi.controller.data;

import java.util.ArrayList;
import java.util.List;

public class AllProductsResult extends Message{
	//This will have products
	private List<ProductSearchResult> products=new ArrayList<ProductSearchResult>();
	private List<String> productIds = new ArrayList<String>();
	
	
	public List<ProductSearchResult> getProducts() {
		return products;
	}



	public void setProducts(List<ProductSearchResult> products) {
		this.products = products;
	}

	public List<String> getProductIds(){
		return this.productIds;
	}
	
	public void setProductIds(List<String> productIds){
		this.productIds = productIds;
	}

	public void setProductsFromModel(List<com.freshdirect.mobileapi.model.Product> products){
		if(this.products!=null && !this.products.isEmpty()){
			this.products.clear();
		}
        for (com.freshdirect.mobileapi.model.Product product : products) {
            this.products.add(ProductSearchResult.wrap(product));
        }
	}
	
	
	

}
