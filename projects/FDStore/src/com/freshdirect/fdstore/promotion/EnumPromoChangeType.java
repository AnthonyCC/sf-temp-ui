package com.freshdirect.fdstore.promotion;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumPromoChangeType extends Enum {
	private static final long serialVersionUID = 5462364594504096445L;

	public static final EnumPromoChangeType CREATE = new EnumPromoChangeType("CREATE", "Create Promotion");
	public static final EnumPromoChangeType MODIFY = new EnumPromoChangeType("MODIFY", "Modify Promotion");
	public static final EnumPromoChangeType APPROVE = new EnumPromoChangeType("APPROVE", "Change Status - Approve");
	public static final EnumPromoChangeType CANCEL = new EnumPromoChangeType("CANCEL", "Change Status - Cancel");
	public static final EnumPromoChangeType STATUS_PROGRESS = new EnumPromoChangeType("STATUS_PROGRESS", "Change Status - In Progress");
	public static final EnumPromoChangeType STATUS_TEST = new EnumPromoChangeType("STATUS_TEST", "Change Status - Test");
	public static final EnumPromoChangeType PUBLISH = new EnumPromoChangeType("PUBLISH", "Publish Promotion");
	public static final EnumPromoChangeType HOLD = new EnumPromoChangeType("HOLD", "Change Status - Hold");
	public static final EnumPromoChangeType UNHOLD = new EnumPromoChangeType("UNHOLD", "Change Status - Release Hold");

	public static final EnumPromoChangeType CLONE = new EnumPromoChangeType("CLONE", "Clone Promotion");
	public static final EnumPromoChangeType IMPORT = new EnumPromoChangeType("IMPORT", "Import Promotion");
	
	private final String description;
	
	public EnumPromoChangeType(String name, String description) {
		super(name);
		this.description = description;
	}
	
	public static EnumPromoChangeType getEnum(String name) {
		return (EnumPromoChangeType) getEnum(EnumPromoChangeType.class, name);
	}

	@SuppressWarnings("unchecked")
	public static Map getEnumMap() {
		return getEnumMap(EnumPromoChangeType.class);
	}

	@SuppressWarnings("unchecked")
	public static List getEnumList() {
		return getEnumList(EnumPromoChangeType.class);
	}

	@SuppressWarnings("unchecked")
	public static Iterator iterator() {
		return iterator(EnumPromoChangeType.class);
	}

	public String getDescription() {
		return description;
	}
}
