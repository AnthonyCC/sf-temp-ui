package com.freshdirect.mobileapi.catalog.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.freshdirect.common.pricing.ZoneInfo;

public class CatalogInfo {
	
	private List<Category> categories=new ArrayList<Category>();
	private List<Product> products=new ArrayList<Product>();
	
	public static class CatalogId {
		
		private String eStore;
		
		private String plantId;
		
		private ZoneInfo pricingZone;
		
		public CatalogId(String eStore, String plantId, ZoneInfo pricingZone) {
			this.eStore=eStore;
			this.plantId=plantId;
			this.pricingZone=pricingZone;
		}

		public String getPlantId() {
			return plantId;
		}

		public ZoneInfo getPricingZone() {
			return pricingZone;
		}

		public String geteStore() {
			return eStore;
		}
	}
	
	private final CatalogId key;
	
	public CatalogInfo(CatalogId key) {
		this.key=key;
	}
	
	public CatalogId getKey() {
		return key;
	}
	
	public void addCategory(Category category) {
		categories.add(category);
	}
	public void addCategories(List<Category> categories) {
		this.categories.addAll(categories);
	}

	public List<Category> getCategories() {
		return Collections.unmodifiableList(categories);
	}
	
	public void addProducts(Collection<Product> products) {
		this.products.addAll(products);
	}
	public List<Product> getProducts() {
		return Collections.unmodifiableList(products);
	}
}
