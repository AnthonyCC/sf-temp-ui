package com.freshdirect.fdstore.content;


/**
*
* 	Filters for quickshop
*
*/
public enum EnumQuickShopFilteringValue implements FilteringValue {
	
	TIME_FRAME_ALL("timeFrameAll","All - Past Year",1,true,"TIME FRAME",true,false,false),
	TIME_FRAME_LAST("timeFrameLast","Your Last Order",1,true,"TIME FRAME",true,false,false),
	TIME_FRAME_30("timeFrame30","30 days",1,true,"TIME FRAME",true,false,false),
	TIME_FRAME_60("timeFrame60","60 days",1,true,"TIME FRAME",true,false,false),
	TIME_FRAME_90("timeFrame90","90 days",1,true,"TIME FRAME",true,false,false),
	TIME_FRAME_180("timeFrame180","180 days",1,true,"TIME FRAME",true,false,false),

	ORDERS_BY_DATE("ordersByDate",null,2,true,"ORDERS BY DATE",false,false,true),

	DEPT("dept",null,3,true,"DEPARTMENTS",false,true,false),

	GLUTEN_FREE("glutenFree","Gluten Free",4,false,"PREFERENCES",false,false,false),
	KOSHER("kosher","Kosher",4,false,"PREFERENCES",false,false,false),
	LOCAL("local","Local",4,false,"PREFERENCES",false,false,false),
	ORGANIC("organic","Organic",4,false,"PREFERENCES",false,false,false),
	ON_SALE("onSale","Sale Items",4,false,"PREFERENCES",false,false,false),

	YOUR_LISTS("yourLists", "YOUR LISTS,", 1, true, "YOUR LISTS", false, false, false),
	STARTER_LISTS("starterLists", "STARTER LISTS,", 1, true, "STARTER LISTS", false,false,false);
	
	
	private String name;
	private String displayName;
	private boolean multiSelect; //is the given domain supporting multiselect. If yes then we need to create the domain's menus before filtering
	private Integer position; //position determines filtering priority most of the times (lower number is the higher priority)
	private String parent; //the domain name
	private boolean showIfEmpty; //show the empty static menuitems (e.g preferences in quickshop)
	private boolean tempMenuNeeded; //temporary menu needed at some point of filtering (e.g departments on quickshop)
	private boolean midProcessingNeeded; //after some domains we need to modify the item list (e.g in quickshop, after orders by date domain, we have to remove the duplicated skus)
	
	private EnumQuickShopFilteringValue( String name, String displayName, Integer position, boolean multiSelect, String parent, boolean showIfEmpty, boolean tempMenuNeeded, boolean midProcessingNeeded ) {
		this.name = name;
		this.displayName = displayName;
		this.position = position;
		this.multiSelect = multiSelect;
		this.parent = parent;
		this.showIfEmpty = showIfEmpty;
		this.tempMenuNeeded = tempMenuNeeded;
		this.midProcessingNeeded = midProcessingNeeded;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}

	@Override
	public Integer getPosition() {
		return position;
	}

	@Override
	public boolean isMultiSelect() {
		return multiSelect;
	}
	
	public String getParent() {
		return parent;
	}
	
	public static EnumQuickShopFilteringValue getByName(String name){
		for(EnumQuickShopFilteringValue v: values()){
			if(name.equals(v.getName())){
				return v;
			}
		}
		throw new IllegalArgumentException("No enum found with name: " + name);
	}
	
	@Override
	public boolean isShowIfEmpty() {
		return showIfEmpty;
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
