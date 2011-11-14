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

public class EnumVSStatus extends ValuedEnum {

	public final static EnumVSStatus SUCCESS = new EnumVSStatus("Success", 0);
	public final static EnumVSStatus UNSUCCESSFUL = new EnumVSStatus("UnSuccessful", 1);
	public final static EnumVSStatus ANS_MACHINE = new EnumVSStatus("Answering Machine", 2);
	public final static EnumVSStatus LIVE_ANS = new EnumVSStatus("Live Answer", 3);

	private EnumVSStatus(String name, int value) {
		super(name, value);
	}
	
	public static EnumVSStatus getEnum(String type) {
		return (EnumVSStatus) getEnum(EnumVSStatus.class, type);
	}

	public static EnumVSStatus getEnum(int type) {
		return (EnumVSStatus) getEnum(EnumVSStatus.class, type);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumVSStatus.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumVSStatus.class);
	}

	public static Iterator iterator() {
		return iterator(EnumVSStatus.class);
	}
}
