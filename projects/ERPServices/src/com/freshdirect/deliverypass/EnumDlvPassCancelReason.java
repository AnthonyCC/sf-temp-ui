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
public class EnumDlvPassCancelReason  extends Enum {
	
	public static final EnumDlvPassCancelReason PURCHASED_WRONG_PASS = new EnumDlvPassCancelReason("DPWRONG", "Purchased wrong pass");
	public static final EnumDlvPassCancelReason POOR_EXPERIENCE = new EnumDlvPassCancelReason("DPPOORXP", "Request to close account - poor experience");
	public static final EnumDlvPassCancelReason MOVING = new EnumDlvPassCancelReason("DPMOVE", "Request to close account - moving");
	public static final EnumDlvPassCancelReason OTHER = new EnumDlvPassCancelReason("OTHER", "Other");

	private final String displayName;
	private EnumDlvPassCancelReason(String name, String displayName) {
		super(name);
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public static EnumDlvPassCancelReason getEnum(String name) {
		return (EnumDlvPassCancelReason) getEnum(EnumDlvPassCancelReason.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumDlvPassCancelReason.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumDlvPassCancelReason.class);
	}

	public static Iterator iterator() {
		return iterator(EnumDlvPassCancelReason.class);
	}
}
