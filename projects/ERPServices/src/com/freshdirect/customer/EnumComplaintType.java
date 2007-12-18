/*
 * EnumComplaintLineType.java
 *
 * Created on June 26, 2006, 10:15 AM
 */

package com.freshdirect.customer;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enum.ValuedEnum;

public class EnumComplaintType extends ValuedEnum {

	public final static EnumComplaintType STORE_CREDIT = new EnumComplaintType("FDC", 0);
	public final static EnumComplaintType CASH_BACK = new EnumComplaintType("CSH", 1);
	public final static EnumComplaintType MIXED = new EnumComplaintType("MIX", 2);

	private EnumComplaintType(String name, int value) {
		super(name, value);
	}
	
	public static EnumComplaintType getEnum(String type) {
		return (EnumComplaintType) getEnum(EnumComplaintType.class, type);
	}

	public static EnumComplaintType getEnum(int type) {
		return (EnumComplaintType) getEnum(EnumComplaintType.class, type);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumComplaintType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumComplaintType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumComplaintType.class);
	}
}
