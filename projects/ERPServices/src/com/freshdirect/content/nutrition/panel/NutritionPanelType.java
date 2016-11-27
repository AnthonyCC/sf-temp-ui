package com.freshdirect.content.nutrition.panel;

/**
 *	Enum for different nutrition panel types/styles.
 *
 *	name - a 'nice' name to be displayed for humans
 *	jsName - javascript 'identifier' 
 *	jsInclude - javascript includes
 * 
 * @author treer
 */
public enum NutritionPanelType {
	
	CLASSIC ( "Classic nutrition panel",	""), 
	DRUG	( "Drug nutrition panel", 		"drug"),
	PET		( "Pet food nutrition panel", 	"pet"),
	BABY	( "Baby food nutrition panel", 	"baby"),
	SUPPL	( "Supplement nutrition panel",	"suppl"); 
	
	
	private String name;
	private String jsName;
	
	NutritionPanelType( String name, String jsName ) {
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
