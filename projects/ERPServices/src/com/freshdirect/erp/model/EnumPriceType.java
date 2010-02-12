/*
 * Created on Jun 30, 2005
 *
 */
package com.freshdirect.erp.model;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

/**
 * @author jng
 *
 */
public class EnumPriceType extends Enum {
	
	// DEFAULT TYPES
	public static final EnumPriceType SELLING = new EnumPriceType("S", "Selling Price");
	public static final EnumPriceType PROMO = new EnumPriceType("P", "Promotional Price"); //item on sale

	private final String description;

	public EnumPriceType(String name, String description) {
		super(name);
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public static EnumPriceType getEnum(String name) {
		return (EnumPriceType) getEnum(EnumPriceType.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumPriceType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumPriceType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumPriceType.class);
	}

	public String toString() {
		return this.getName();
	}

}
