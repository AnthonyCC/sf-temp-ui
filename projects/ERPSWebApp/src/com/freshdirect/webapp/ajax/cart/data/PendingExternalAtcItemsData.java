package com.freshdirect.webapp.ajax.cart.data;

import com.freshdirect.webapp.ajax.product.data.ProductData;


public class PendingExternalAtcItemsData {
	private ProductData productData;
	private AddToCartItem addToCartItem;
	
	public ProductData getProductData() {
		return productData;
	}
	
	public void setProductData(ProductData productData) {
		this.productData = productData;
	}
	
	public AddToCartItem getAddToCartItem() {
		return addToCartItem;
	}

	public void setAddToCartItem(AddToCartItem addToCartItem) {
		this.addToCartItem = addToCartItem;
	}
}

