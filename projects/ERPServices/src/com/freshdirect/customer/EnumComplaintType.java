/*
 * EnumComplaintLineType.java
 *
 * Created on June 26, 2006, 10:15 AM
 */

package com.freshdirect.customer;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.ValuedEnum;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EnumComplaintType extends ValuedEnum {

	private static final long serialVersionUID = 2668998329611702133L;
	public final static EnumComplaintType STORE_CREDIT = new EnumComplaintType("FDC", 0);
	public final static EnumComplaintType CASH_BACK = new EnumComplaintType("CSH", 1);
	public final static EnumComplaintType MIXED = new EnumComplaintType("MIX", 2);

	private EnumComplaintType(String name, int value) {
		super(name, value);
	}
	
	public static EnumComplaintType getEnum(String type) {
		return (EnumComplaintType) getEnum(EnumComplaintType.class, type);
	}

	@JsonCreator
	public static EnumComplaintType getEnum(@JsonProperty("value") int value) {
		return (EnumComplaintType) getEnum(EnumComplaintType.class, value);
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
