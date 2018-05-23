package com.freshdirect.fdstore.ecoupon;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EnumCouponTransactionType extends Enum implements Serializable {

	private static final long serialVersionUID = -7601243289963699647L;
	public final static EnumCouponTransactionType					CREATE_ORDER					= new EnumCouponTransactionType( "CREATE_ORDER", "Coupons Create Order" );
	public final static EnumCouponTransactionType					MODIFY_ORDER					= new EnumCouponTransactionType( "MODIFY_ORDER", "Coupons Modify Order" );
	public final static EnumCouponTransactionType					CANCEL_ORDER					= new EnumCouponTransactionType( "CANCEL_ORDER", "Coupons Cancel Order" );
	public final static EnumCouponTransactionType					CONFIRM_ORDER					= new EnumCouponTransactionType( "CONFIRM_ORDER", "Coupons Delivery Confirm Order" );
	public final static EnumCouponTransactionType					PREVIEW_CREATE_ORDER			= new EnumCouponTransactionType( "PREVIEW_CREATE_ORDER", "Coupons Preview Create Order" );
	public final static EnumCouponTransactionType					PREVIEW_MODIFY_ORDER			= new EnumCouponTransactionType( "PREVIEW_MODIFY_ORDER", "Coupons Preview Modify Order" );
	public final static EnumCouponTransactionType					GET_COUPON_META_DATA			= new EnumCouponTransactionType( "GET_COUPON_META_DATA", "Coupons Meta Data" );
	public final static EnumCouponTransactionType					GET_CUSTOMER_COUPONS			= new EnumCouponTransactionType( "GET_CUSTOMER_COUPONS", "Coupons Meta Data" );
	public final static EnumCouponTransactionType					CLIP_COUPON						= new EnumCouponTransactionType( "CLIP_COUPON", "Clip Coupon" );
	
	private final String description;
	public EnumCouponTransactionType(String name, String description) {
		super(name);
		this.description = description;
	}
	
	@JsonCreator
	public static EnumCouponTransactionType getEnum(@JsonProperty("name") String name) {
		return (EnumCouponTransactionType) getEnum(EnumCouponTransactionType.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumCouponTransactionType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumCouponTransactionType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumCouponTransactionType.class);
	}

	public String getDescription() {
		return description;
	}	

}
