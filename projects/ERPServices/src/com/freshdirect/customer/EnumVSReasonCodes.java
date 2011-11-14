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

public class EnumVSReasonCodes extends ValuedEnum {

	private static final long serialVersionUID = 1L;
	public final static EnumVSReasonCodes REASONCODE1 = new EnumVSReasonCodes("Traffic Delay", 1);
	public final static EnumVSReasonCodes REASONCODE2 = new EnumVSReasonCodes("Ralley", 2);
	public final static EnumVSReasonCodes REASONCODE3 = new EnumVSReasonCodes("General Reason", 3);
	public final static EnumVSReasonCodes REASONCODE4 = new EnumVSReasonCodes("Plant Delay", 4);

	private EnumVSReasonCodes(String name, int value) {
		super(name, value);
	}
	
	public static EnumVSReasonCodes getEnum(String type) {
		return (EnumVSReasonCodes) getEnum(EnumVSReasonCodes.class, type);
	}

	public static EnumVSReasonCodes getEnum(int type) {
		return (EnumVSReasonCodes) getEnum(EnumVSReasonCodes.class, type);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumVSReasonCodes.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumVSReasonCodes.class);
	}

	public static Iterator iterator() {
		return iterator(EnumVSReasonCodes.class);
	}
}
