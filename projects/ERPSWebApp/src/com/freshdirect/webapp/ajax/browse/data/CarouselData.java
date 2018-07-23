package com.freshdirect.webapp.ajax.browse.data;

import java.util.List;

import com.freshdirect.webapp.ajax.product.data.ProductData;


public class CarouselData extends BasicData {

	private static final long serialVersionUID = 6290792212688638049L;
	
	private List<ProductData> products;
	
	private String eventSource;

	public List<ProductData> getProducts() {
		return products;
	}
	public void setProducts(List<ProductData> products) {
		this.products = products;
	}
	public String getEventSource() {
		return eventSource;
	}
	public void setEventSource(String eventSource) {
		this.eventSource = eventSource;
	}
}
