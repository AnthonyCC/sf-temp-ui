package com.freshdirect.framework.util;

import java.util.List;
import java.util.Map;
import org.apache.commons.lang.enums.Enum;

public class EnumSearchType extends Enum{
	
	public static EnumSearchType COMPANY_SEARCH = new EnumSearchType("COMPANY_SEARCH");
	public static EnumSearchType EXEC_SUMMARY_SEARCH = new EnumSearchType("EXEC_SUMMARY_SEARCH");
	public static EnumSearchType RESERVATION_SEARCH = new EnumSearchType("RESERVATION_SEARCH");
	public static EnumSearchType ORDERS_BY_RESV_SEARCH = new EnumSearchType("ORDERS_BY_RESV_SEARCH");
	public static EnumSearchType BROKEN_ACCOUNT_SEARCH = new EnumSearchType("BROKEN_ACCOUNT_SEARCH");
	public static EnumSearchType CANCEL_ORDER_SEARCH = new EnumSearchType("CANCEL_ORDER_SEARCH");
	public static EnumSearchType RETURN_ORDER_SEARCH = new EnumSearchType("RETURN_ORDER_SEARCH");
	public static EnumSearchType DEL_RESTRICTION_SEARCH = new EnumSearchType("DEL_RESTRICTION_SEARCH");
	public static EnumSearchType ADDR_RESTRICTION_SEARCH = new EnumSearchType("ADDR_RESTRICTION_SEARCH");
	public static EnumSearchType SETTLEMENT_BATCH_SEARCH = new EnumSearchType("SETTLEMENT_BATCH_SEARCH");
	public static EnumSearchType GIFTCARD_SEARCH = new EnumSearchType("GIFTCARD_SEARCH");

	protected EnumSearchType(String name) {
		super(name);
	}
	
	public static EnumSearchType getEnum(String name) {
		return (EnumSearchType) getEnum(EnumSearchType.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumSearchType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumSearchType.class);
	}
}
