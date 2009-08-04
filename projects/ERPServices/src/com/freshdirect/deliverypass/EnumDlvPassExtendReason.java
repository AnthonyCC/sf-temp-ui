package com.freshdirect.deliverypass;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;
/**
*
* @author  skrishnasamy
* @version 1.0
* @created 05-Jun-2006
* 
*/
public class EnumDlvPassExtendReason  extends Enum {
	
	public static final EnumDlvPassExtendReason GENERAL_DISAPPOINTMENT = new EnumDlvPassExtendReason("DPDIS", "General disappointment with delivery pass");
	public static final EnumDlvPassExtendReason DISAPPOINTMENT_SERVICE = new EnumDlvPassExtendReason("GENDIS", "Disappointed with service");
	public static final EnumDlvPassExtendReason DLV_PROBLEM_LATE_DELIVERY = new EnumDlvPassExtendReason("LATEDEL", "Delivery problem - late delivery");
	public static final EnumDlvPassExtendReason DLV_PROBLEM_OTHER = new EnumDlvPassExtendReason("DLVPRB", "Delivery problem - other");
	public static final EnumDlvPassExtendReason DLV_PROMOTION_APPLIED= new EnumDlvPassExtendReason("DLVPRA","Delivery promotion applied.");
	public static final EnumDlvPassExtendReason DLV_PROMOTION_REMOVED= new EnumDlvPassExtendReason("DLVPRR","Delivery promotion removed.");
	public static final EnumDlvPassExtendReason OTHER = new EnumDlvPassExtendReason("OTHER", "Other");

	private final String displayName;
	private EnumDlvPassExtendReason(String name, String displayName) {
		super(name);
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public static EnumDlvPassExtendReason getEnum(String name) {
		return (EnumDlvPassExtendReason) getEnum(EnumDlvPassExtendReason.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumDlvPassExtendReason.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumDlvPassExtendReason.class);
	}

	public static Iterator iterator() {
		return iterator(EnumDlvPassExtendReason.class);
	}
}
