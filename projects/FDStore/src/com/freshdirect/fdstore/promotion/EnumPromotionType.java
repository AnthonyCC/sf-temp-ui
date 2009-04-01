package com.freshdirect.fdstore.promotion;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enum.Enum;

public class EnumPromotionType extends Enum {

	public static final EnumPromotionType SAMPLE = new EnumPromotionType("SAMPLE", "Sample Item Promotions", 0);
	public static final EnumPromotionType SIGNUP = new EnumPromotionType("SIGNUP", "Signup Promotions", 10);
	public static final EnumPromotionType REFERRAL= new EnumPromotionType("REFERRAL", "Referral Promotion", 20);
	public static final EnumPromotionType REFERRER= new EnumPromotionType("REFERRER", "Referrer Promotion", 30);
	public static final EnumPromotionType GIFT_CARD= new EnumPromotionType("GIFT_CARD", "Gift Card Promotions", 40);
	public static final EnumPromotionType DCP_DISCOUNT = new EnumPromotionType("DCPD", "Dept/Category Promotions", 50);
	public static final EnumPromotionType REDEMPTION = new EnumPromotionType("REDEMPTION", "Redemption Code Promotions", 60);
	public static final EnumPromotionType LINE_ITEM = new EnumPromotionType("LINE_ITEM", "Line Item Promotions", 70);
	
	private final String description;
	private final int priority;

	public EnumPromotionType(String name, String description, int priority) {
		super(name);
		this.description = description;
		this.priority = priority;
	}

	public String getDescription() {
		return this.description;
	}
	
	public int getPriority() {
		return priority;
	}

	public static EnumPromotionType getEnum(String name) {
		return (EnumPromotionType) getEnum(EnumPromotionType.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumPromotionType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumPromotionType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumPromotionType.class);
	}

}
