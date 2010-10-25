
package com.freshdirect.fdstore.content;

import java.io.Serializable;

public enum EnumProductLayout implements Serializable {
	PERISHABLE("Perishable Product Layout", 1, "/includes/product/perishable_product.jsp", false),
	COMPOSITE("Composite Product Layout", 2, "/includes/product/transac_composite.jsp", false),
	WINE("Wine Product Layout", 3, "/includes/product/wine_product.jsp", false),
	COMPOSITE_PLUS("Compoiste-Plus Product Layout", 4, "/includes/product/transac_composite_plus.jsp", true),
	PARTY_PLATTER("Party Platter Product Layout", 5, "/includes/product/party_platter.jsp", true),
	MULTI_ITEM_MEAL("Multi Item Meal Option Layout", 6, "/includes/product/party_platter.jsp", true),
	COMPONENTGROUP_MEAL("Meal Layout using Component groups", 7, "/includes/product/componentGroupMeal.jsp", true),
	NEW_WINE_PRODUCT("New Wine Product", 8, "/includes/product/new_wine_product.jsp", false),
	CONFIGURED_PRODUCT("Configured Product Layout", 9, "/includes/product/configProduct.jsp", false);



	private int			id;
	private String	name;
	private String	layoutPath;
	private boolean	canAddMultipleToCart;
    

	
	EnumProductLayout(String name, int i, String layoutPath, boolean addMultipleToCart) {
		this.name = name;
		this.id = i;
		this.layoutPath = layoutPath;
		this.canAddMultipleToCart = addMultipleToCart;
	}


	/**
	 * Lookup category layout type by its ID
	 * 
	 * @param lid
	 * @return
	 */
	public static EnumProductLayout getLayoutType(int lid) {
		for (EnumProductLayout l : EnumProductLayout.values()) {
			if (lid == l.getId())
				return l;
		}
		
		return null;
	}


	public int getId(){
		return id;
	}

	public String getName(){
		return name;
	}

	public String getLayoutPath() {
		return this.layoutPath;
	}
	
	public boolean canAddMultipleToCart() {
		return this.canAddMultipleToCart;
	}



	public String toString() {
		return "EnumProductLayout:[ Name: "+ this.getName()+ " id: " + this.getId()+" Path:"+getLayoutPath();
	}
}
