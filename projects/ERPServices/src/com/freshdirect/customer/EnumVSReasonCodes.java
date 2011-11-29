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
	public final static EnumVSReasonCodes REASONCODE1 = new EnumVSReasonCodes("Up to/Approx 15 minutes Delays due to inclement weather", 1);
	public final static EnumVSReasonCodes REASONCODE2 = new EnumVSReasonCodes("Up to/Approx 30 minutes Delays due to inclement weather", 2);
	public final static EnumVSReasonCodes REASONCODE3 = new EnumVSReasonCodes("Up to/Approx 60 minutes Delays due to inclement weather", 3);
	public final static EnumVSReasonCodes REASONCODE4 = new EnumVSReasonCodes("Up to/Approx 90 minutes Delays due to inclement weather", 4);
	public final static EnumVSReasonCodes REASONCODE5 = new EnumVSReasonCodes("Up to/Approx 2hr plus Delays due to inclement weather", 5);
	public final static EnumVSReasonCodes REASONCODE6 = new EnumVSReasonCodes("Up to/Approx 15 minutes Delays due to technical issues at the facility", 6);
	public final static EnumVSReasonCodes REASONCODE7 = new EnumVSReasonCodes("Up to/Approx 30 minutes Delays due to technical issues at the facility", 7);
	public final static EnumVSReasonCodes REASONCODE8 = new EnumVSReasonCodes("Up to/Approx 60 minutes Delays due to technical issues at the facility", 8);
	public final static EnumVSReasonCodes REASONCODE9 = new EnumVSReasonCodes("Up to/Approx 90 minutes Delays due to technical issues at the facility", 9);
	public final static EnumVSReasonCodes REASONCODE10 = new EnumVSReasonCodes("Up to/Approx 2hr plus Delays due to technical issues at the facility", 10);
	public final static EnumVSReasonCodes REASONCODE11 = new EnumVSReasonCodes("Up to/Approx 15 minutes In Field Delays", 11);
	public final static EnumVSReasonCodes REASONCODE12 = new EnumVSReasonCodes("Up to/Approx 30 minutes In Field Delays", 12);
	public final static EnumVSReasonCodes REASONCODE13 = new EnumVSReasonCodes("Up to/Approx 60 minutes In Field Delays", 13);
	public final static EnumVSReasonCodes REASONCODE14 = new EnumVSReasonCodes("Up to/Approx 90 minutes In Field Delays", 14);
	public final static EnumVSReasonCodes REASONCODE15 = new EnumVSReasonCodes("Up to/Approx 2hr plus In Field Delays", 15);
	public final static EnumVSReasonCodes REASONCODE16 = new EnumVSReasonCodes("Up to/Approx 15 minutes Unforeseen Traffic Delays", 16);
	public final static EnumVSReasonCodes REASONCODE17 = new EnumVSReasonCodes("Up to/Approx 30 minutes Unforeseen Traffic Delays", 17);
	public final static EnumVSReasonCodes REASONCODE18 = new EnumVSReasonCodes("Up to/Approx 60 minutes Unforeseen Traffic Delays", 18);
	public final static EnumVSReasonCodes REASONCODE19 = new EnumVSReasonCodes("Up to/Approx 90 minutes Unforeseen Traffic Delays", 19);
	public final static EnumVSReasonCodes REASONCODE20 = new EnumVSReasonCodes("Up to/Approx 2hr plus Unforeseen Traffic Delays", 20);	

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
	
	public static String getReasonString(int intValue) {
		Map rCodes = EnumVSReasonCodes.getEnumMap();
		java.util.Set keys = rCodes.keySet();
		Iterator iter = keys.iterator();
		while(iter.hasNext()) {
			String key = (String) iter.next();
			EnumVSReasonCodes value = (EnumVSReasonCodes) rCodes.get(key);
			String name = value.getName();
			int val = value.getValue();
			if(val == intValue) {
				return name;
			}
		}
		return "";
	}
}
