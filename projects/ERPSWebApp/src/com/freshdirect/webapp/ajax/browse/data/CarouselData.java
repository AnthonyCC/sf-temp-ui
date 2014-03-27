package com.freshdirect.webapp.ajax.browse.data;

import java.util.List;

import com.freshdirect.webapp.ajax.product.data.ProductData;


public class CarouselData extends BasicData {

	private static final long serialVersionUID = 6290792212688638049L;
	
	private List<ProductData> products;
	
	private String cmEventSource;

	public List<ProductData> getProducts() {
		return products;
	}
	public void setProducts(List<ProductData> products) {
		this.products = products;
	}
	public String getCmEventSource() {
		return cmEventSource;
	}
	public void setCmEventSource(String cmEventSource) {
		this.cmEventSource = cmEventSource;
	}
}
