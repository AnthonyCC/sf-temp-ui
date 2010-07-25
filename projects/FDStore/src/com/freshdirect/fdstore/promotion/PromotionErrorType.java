package com.freshdirect.fdstore.promotion;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;



public  class PromotionErrorType extends Enum{
	
	public static final PromotionErrorType ERROR_GENERIC = new PromotionErrorType("100", 100, "Generic Error");
	public static final PromotionErrorType ERROR_REDEMPTION_EXCEEDED = new PromotionErrorType("101", 101, "Redemption exceeded");
	public static final PromotionErrorType NO_ELIGIBLE_CART_LINES = new PromotionErrorType("102", 102, "No Eligible Cart Lines");
	
	private int code; 	
	private String description;
	
	public PromotionErrorType(String name, int code, String description) {
		super(name);
		this.code = code;
		this.description = description;
	}

	public static PromotionErrorType getEnum(String name) {
		return (PromotionErrorType) getEnum(PromotionErrorType.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(PromotionErrorType.class);
	}

	public static List getEnumList() {
		return getEnumList(PromotionErrorType.class);
	}

	public static Iterator iterator() {
		return iterator(PromotionErrorType.class);
	}

	  	
	public String getDescription() {
		return this.description;
	}
	
	public int getErrorCode() {
		return this.code;
	}
	
	public String toString() {
		return this.getName();
	}
	
	public String getInfo() {
		return this.getName()+" - "+this.getDescription();
	}

}
