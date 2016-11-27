package com.freshdirect.mobileapi.controller.data;

import java.util.List;

public class DepartmentCarouselResult extends Message {
	
	private String title;
	
	private String siteFeature;
	
	private List products;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSiteFeature() {
		return siteFeature;
	}

	public void setSiteFeature(String siteFeature) {
		this.siteFeature = siteFeature;
	}

	public List getProducts() {
		return products;
	}

	public void setProducts(List products) {
		this.products = products;
	}
	
	

}
