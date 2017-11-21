package com.freshdirect.storeapi.content;

/**
 *
 * 	Filters for the search page
 *
 */
public enum EnumSearchFilteringValue implements FilteringValue {
	
	DEPT("dept",null,1,false,false,false,false),
	CAT("cat",null,2,false,false,false,false),
	SUBCAT("subcat",null,3,false,false,false,false),
	BRAND("brand",null,4,false,false,false,false),
	EXPERT_RATING("expRating",null,5,true,false,false,false),
	CUSTOMER_RATING("custRating",null,6,true,false,false,false),
	ON_SALE("onSale","On Sale",7,false,false,false,false),
	NEW_OR_BACK("newORBack","Back in Stock",7,false,false,false,false),
	KOSHER("kosher","Kosher",7,false,false,false,false),
	GLUTEN_FREE("glutenFree","Gluten Free",7,false,false,false,false),
	RECIPE_CLASSIFICATION("classification",null,7,false,false,false,false);
	
	private String name;
	private boolean multiSelect;
	private String displayName;
	private Integer position;
	private boolean showIfEmpty;
	private boolean tempMenuNeeded;
	private boolean midProcessingNeeded;
	
	private EnumSearchFilteringValue( String name, String displayName, Integer position, boolean multiSelect, boolean showIfEmpty, boolean tempMenuNeeded, boolean midProcessingNeeded ) {
		this.name = name;
		this.position = position;
		this.multiSelect = multiSelect;
		this.showIfEmpty = showIfEmpty;
		this.displayName = displayName;
		this.tempMenuNeeded = tempMenuNeeded;
		this.midProcessingNeeded = midProcessingNeeded;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public Integer getPosition() {
		return position;
	}

	/**
	 *	Multi-select filters have an OR logic, single-select filters have an AND logic 
	 */
	@Override
	public boolean isMultiSelect() {
		return multiSelect;
	}

	@Override
	public boolean isShowIfEmpty() {
		return showIfEmpty;
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}

	@Override
	public boolean isTempMenuNeeded() {
		return tempMenuNeeded;
	}

	@Override
	public boolean isMidProcessingNeeded() {
		return midProcessingNeeded;
	}

}
