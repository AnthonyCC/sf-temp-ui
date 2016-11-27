package com.freshdirect.fdstore.ecoupon;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumCouponOfferType extends Enum implements Serializable {
	
	public final static EnumCouponOfferType					DOLLAR_OFF						= new EnumCouponOfferType( "0", "Dollars off item" );
	public final static EnumCouponOfferType					PERCENT_OFF						= new EnumCouponOfferType( "5", "Percent off item" );
	
	private final String description;
	public EnumCouponOfferType(String name, String description) {
		super(name);
		this.description = description;
	}
	
	public static EnumCouponOfferType getEnum(String name) {
		return (EnumCouponOfferType) getEnum(EnumCouponOfferType.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumCouponOfferType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumCouponOfferType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumCouponOfferType.class);
	}

	public String getDescription() {
		return description;
	}	

}
