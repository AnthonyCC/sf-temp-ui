package com.freshdirect.deliverypass;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enum.Enum;
/**
*
* @author  skrishnasamy
* @version 1.0
* @created 05-Jun-2006
* 
*/
public class EnumDlvPassProfileType  extends Enum {
	
	public static final EnumDlvPassProfileType NOT_ELIGIBLE = new EnumDlvPassProfileType("NONE", "Not Eligible");
	public static final EnumDlvPassProfileType BSGS = new EnumDlvPassProfileType("BSGS", "Buy Some Get Some");
	public static final EnumDlvPassProfileType UNLIMITED = new EnumDlvPassProfileType("UNLIMITED", "Unlimited delivery");
	public static final EnumDlvPassProfileType PROMOTIONAL_UNLIMITED = new EnumDlvPassProfileType("UNLIMITED_PROMOTIONAL", "Unlimited delivery- Promotional");
	public static final EnumDlvPassProfileType AMAZON_PRIME = new EnumDlvPassProfileType("AMAZON_PRIME", "Unlimited delivery- Amazon prime");

	private final String displayName;
	private EnumDlvPassProfileType(String name, String displayName) {
		super(name);
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public static EnumDlvPassProfileType getEnum(String name) {
		return (EnumDlvPassProfileType) getEnum(EnumDlvPassStatus.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumDlvPassProfileType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumDlvPassProfileType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumDlvPassProfileType.class);
	}
}
