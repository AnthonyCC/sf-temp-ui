package com.freshdirect.content.nutrition;

import java.io.Serializable;

public class NutritionDrugItem  implements Serializable {

	/**
	 * 
	 */
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

}
