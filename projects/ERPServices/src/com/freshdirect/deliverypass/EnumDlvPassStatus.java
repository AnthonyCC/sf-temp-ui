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
public class EnumDlvPassStatus  extends Enum {
	
	public static final EnumDlvPassStatus NONE = new EnumDlvPassStatus("NONE", "None");
	public static final EnumDlvPassStatus PENDING = new EnumDlvPassStatus("PEN", "Pending");
	public static final EnumDlvPassStatus ACTIVE = new EnumDlvPassStatus("ACT", "Active");
	public static final EnumDlvPassStatus EXPIRED = new EnumDlvPassStatus("EXP", "Expired");
	public static final EnumDlvPassStatus CANCELLED = new EnumDlvPassStatus("CAN", "Cancelled");
	public static final EnumDlvPassStatus EXPIRED_PENDING = new EnumDlvPassStatus("EPG", "Expired Pending");
	public static final EnumDlvPassStatus SHORT_SHIPPED = new EnumDlvPassStatus("SSD", "Short Shipped");
	public static final EnumDlvPassStatus SETTLEMENT_FAILED = new EnumDlvPassStatus("STF", "Settlement Failed");
	public static final EnumDlvPassStatus ORDER_CANCELLED = new EnumDlvPassStatus("CAO", "Cancelled");
	public static final EnumDlvPassStatus PASS_RETURNED = new EnumDlvPassStatus("RET", "Returned");
	public static final EnumDlvPassStatus READY_TO_USE = new EnumDlvPassStatus("RTU", "Ready To Use");
	private final String displayName;
	private EnumDlvPassStatus(String name, String displayName) {
		super(name);
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public static EnumDlvPassStatus getEnum(String name) {
		return (EnumDlvPassStatus) getEnum(EnumDlvPassStatus.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumDlvPassStatus.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumDlvPassStatus.class);
	}

	public static Iterator iterator() {
		return iterator(EnumDlvPassStatus.class);
	}
}
