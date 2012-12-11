package com.freshdirect.fdstore.content;

public enum EnumFilteringValue implements FilteringValue {
	
	DEPT("dept",false),
	CAT("cat",false),
	SUBCAT("subcat",false),
	BRAND("brand",false),
	EXPERT_RATING("expRating",true),
	CUSTOMER_RATING("custRating",true),
	ON_SALE("onSale",false),
	NEW_OR_BACK("newORBack",false),
	KOSHER("kosher",false),
	GLUTEN_FREE("glutenFree",false),
	RECIPE_CLASSIFICATION("classification",false);
	
	private String name;
	private boolean multiSelect;
	
	private EnumFilteringValue( String name, boolean multiSelect ) {
		this.name = name;
		this.multiSelect = multiSelect;
	}

	public String getName() {
		return name;
	}

	/**
	 *	Multi-select filters have an OR logic, single-select filters have an AND logic 
	 */
	public boolean isMultiSelect() {
		return multiSelect;
	}

}
