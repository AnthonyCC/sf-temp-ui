package com.freshdirect.mktAdmin.constants;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumFileContentType  extends Enum{

//	 DEFAULT TYPES
	public static final EnumFileContentType CUSTOMER_FILE_TYPE = new EnumFileContentType("CUSTOMER", "Customer File Type");
	public static final EnumFileContentType COMPETITOR_FILE_TYPE = new EnumFileContentType("COMPETITOR", "Competitor File Type");
	public static final EnumFileContentType RESTRICTION_LIST_FILE_TYPE = new EnumFileContentType("RESTRICTION_LIST", "Restricted Customer List");
//	public static final EnumFileContentType MULTI_PROMO_RESTRICTION_LIST_FILE_TYPE = new EnumFileContentType("MULTI_PROMO_RESTRICTION_LIST", "Restricted Customer List For Multiple Promotions");

	private final String description;

	public EnumFileContentType(String name, String description) {
		super(name);
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public static EnumFileContentType getEnum(String name) {
		return (EnumFileContentType) getEnum(EnumFileContentType.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumFileContentType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumFileContentType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumFileContentType.class);
	}

	public String toString() {
		return this.getName();
	}
}