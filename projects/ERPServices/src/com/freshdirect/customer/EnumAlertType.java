/*
 * Created on Jun 30, 2005
 *
 */
package com.freshdirect.customer;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

/**
 * @author jng
 *
 */
public class EnumAlertType extends Enum {
	
	// DEFAULT TYPES
	public static final EnumAlertType ECHECK = new EnumAlertType("ECHECK", "ECheck Alert");
	public static final EnumAlertType REFERRER = new EnumAlertType("REFERRER", "Referrer Alert");

	private final String description;

	public EnumAlertType(String name, String description) {
		super(name);
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public static EnumAlertType getEnum(String name) {
		return (EnumAlertType) getEnum(EnumAlertType.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumAlertType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumAlertType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumAlertType.class);
	}

	public String toString() {
		return this.getName();
	}

}
