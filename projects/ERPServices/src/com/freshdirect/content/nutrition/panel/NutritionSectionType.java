package com.freshdirect.content.nutrition.panel;


public enum NutritionSectionType {
	
	INGREDIENT	( "Ingredients section (4 columns)", 	"ingredient" ),
	TABLE		( "Table section (2 columns)", 			"table" ),
	FREETEXT	( "Free-text section", 					"freetext" );
	
	private String name;
	private String jsName;
	
	private NutritionSectionType( String name, String jsName ) {
		this.name = name;
		this.jsName = jsName;
	}
	
	public String getName() {
		return name;
	}
	
	public String getJsName() {
		return jsName;
	}
}
