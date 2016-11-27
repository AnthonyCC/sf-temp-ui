package com.freshdirect.webapp.ajax.browse;

public enum SearchPageType {
	PRODUCT("product", "Products"), RECIPE("recipe", "Recipes");
	
	public final String name;
	public final String label;
	
	private SearchPageType(String name, String label) {
		this.name = name;
		this.label = label;
	}
}
