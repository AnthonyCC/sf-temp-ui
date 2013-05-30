package com.freshdirect.content.nutrition.panel;

import java.io.Serializable;

public class NutritionItem  implements Serializable {

	private static final long serialVersionUID = -5072393994059441104L;
	
	private String id;
	private String value1;
	private String value2;
	private double ingredientValue;
	private String uom;
	private int position;
	private boolean bulleted;
	private boolean important;
	private boolean newline;
	private boolean separator;
	
	/** package protected - used by NutritionPanel.deepCopy() */
	static NutritionItem deepCopy( NutritionItem oldI ) {
		NutritionItem newI = new NutritionItem();
		newI.id = null;		
		newI.value1 = oldI.value1;
		newI.value2 = oldI.value2;
		newI.ingredientValue = oldI.ingredientValue;
		newI.uom = oldI.uom;
		newI.position = oldI.position;
		newI.bulleted = oldI.bulleted;
		newI.important = oldI.important;
		newI.newline = oldI.newline;
		newI.separator = oldI.separator;		
		return newI;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue1() {
		return value1;
	}
	
	public void setValue1(String value1) {
		this.value1 = value1;
	}
	
	public String getValue2() {
		return value2;
	}
	
	public void setValue2(String value2) {
		this.value2 = value2;
	}
	
	public double getIngredientValue() {
		return ingredientValue;
	}
	
	public void setIngredientValue(double ingredientValue) {
		this.ingredientValue = ingredientValue;
	}
	
	public String getUom() {
		return uom;
	}
	
	public void setUom(String uom) {
		this.uom = uom;
	}
	
	public int getPosition() {
		return position;
	}
	
	public void setPosition(int position) {
		this.position = position;
	}
	
	public boolean isBulleted() {
		return bulleted;
	}
	
	public void setBulleted(boolean bulleted) {
		this.bulleted = bulleted;
	}
	
	public boolean isImportant() {
		return important;
	}
	
	public void setImportant(boolean important) {
		this.important = important;
	}
	
	public boolean isNewline() {
		return newline;
	}
	
	public void setNewline(boolean newline) {
		this.newline = newline;
	}
	
	public boolean isSeparator() {
		return separator;
	}
	
	public void setSeparator(boolean separator) {
		this.separator = separator;
	}

	
	
	
	public static final char FLAG_BULLETED 	= 'B';	// Add a 'bullet' character 
	public static final char FLAG_IMPORTANT 	= 'I';	// Important item or title row
	public static final char FLAG_NEWLINE 	= 'N';	// Start item on new line
	public static final char FLAG_SEPARATOR 	= 'S';	// Item is a separator line
	
	/**
	 * 	Helper method to construct the string representation of style flags for this item
	 * 
	 * @return flags string
	 */
	public String constructFlags() {
		StringBuilder sb = new StringBuilder();
		
		if ( isBulleted() ) 	sb.append( FLAG_BULLETED );
		if ( isImportant() ) 	sb.append( FLAG_IMPORTANT );
		if ( isNewline() ) 		sb.append( FLAG_NEWLINE );
		if ( isSeparator() ) 	sb.append( FLAG_SEPARATOR );
		
		return sb.toString();
	}

	/**
	 * 	Helper method to populate this item from the string representation of style flags
	 * 
	 * @param flags string
	 */
	public void populateFlags( String flags ) {		
		if ( flags == null ) flags = "";
		
		setBulleted		( flags.indexOf( FLAG_BULLETED ) 	>=0 );
		setImportant	( flags.indexOf( FLAG_IMPORTANT )	>=0 );
		setNewline		( flags.indexOf( FLAG_NEWLINE ) 	>=0 );
		setSeparator	( flags.indexOf( FLAG_SEPARATOR )	>=0 );
		
	}
	

}
