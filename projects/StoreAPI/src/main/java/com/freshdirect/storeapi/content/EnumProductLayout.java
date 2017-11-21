
package com.freshdirect.storeapi.content;

import java.io.Serializable;

public enum EnumProductLayout implements Serializable {
	PERISHABLE("Perishable Product Layout", 1, "/includes/product/perishable_product.jsp", false),
	@Deprecated
	COMPOSITE("Composite Product Layout", 2, "/includes/product/transac_composite.jsp", false),
	WINE("Wine Product Layout", 3, "/includes/product/wine_product.jsp", false),
	@Deprecated
	COMPOSITE_PLUS("Compoiste-Plus Product Layout", 4, "/includes/product/transac_composite_plus.jsp", true),
	PARTY_PLATTER("Party Platter Product Layout", 5, "/includes/product/party_platter.jsp", true),
	MULTI_ITEM_MEAL("Multi Item Meal Option Layout", 6, "/includes/product/party_platter.jsp", true),
	COMPONENTGROUP_MEAL("Meal Layout using Component groups", 7, "/includes/product/componentGroupMeal.jsp", true),
	NEW_WINE_PRODUCT("New Wine Product", 8, "/includes/product/new_wine_product.jsp", false),
	@Deprecated
	CONFIGURED_PRODUCT("Configured Product Layout", 9, "/includes/product/configProduct.jsp", false),
    HOLIDAY_MEAL_BUNDLE_PRODUCT("Holiday Meal Bundle Product Layout", 10, "/hmb/product.jsp", true),
    RECIPE_MEALKIT_PRODUCT("Recipe MealKit Product Layout", 11, "/handpick/product.jsp", true);

    private int id;
	private String	name;
	private String	layoutPath;
	private boolean	canAddMultipleToCart;

    private EnumProductLayout(String name, int id, String layoutPath, boolean addMultipleToCart) {
		this.name = name;
        this.id = id;
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

	@Override
    public String toString() {
		return "EnumProductLayout:[ Name: "+ this.getName()+ " id: " + this.getId()+" Path:"+getLayoutPath();
	}
}
