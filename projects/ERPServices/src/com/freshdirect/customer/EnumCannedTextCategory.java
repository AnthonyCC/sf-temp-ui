package com.freshdirect.customer;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumCannedTextCategory extends Enum {
	private static final long serialVersionUID = 961894923142364007L;
	
	public final static EnumCannedTextCategory CREDIT_ISSUE = new EnumCannedTextCategory("CREDIT_ISSUE", "Credit Issue");
	
	private final String description;

	protected EnumCannedTextCategory(String name, String description) {
		super(name);
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String toString() {
		return getName();
	}
	
	public static EnumCannedTextCategory getEnum(String name) {
		return (EnumCannedTextCategory) getEnum(EnumCannedTextCategory.class, name);
	}
	
	public static Map getEnumMap() {
		return getEnumMap(EnumCannedTextCategory.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumCannedTextCategory.class);
	}
}
