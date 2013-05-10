package com.freshdirect.fdstore.ecoupon;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.ValuedEnum;

public class EnumCouponTransactionStatus extends ValuedEnum {
	
	public final static EnumCouponTransactionStatus SUCCESS = new EnumCouponTransactionStatus("S", "Approved", 0);
	public final static EnumCouponTransactionStatus FAILURE = new EnumCouponTransactionStatus("F", "Declined", 1);
	public final static EnumCouponTransactionStatus PENDING = new EnumCouponTransactionStatus("P", "Pending", 2);
	public final static EnumCouponTransactionStatus CANCEL = new EnumCouponTransactionStatus("C", "Cancelled", 3);

	private String description;
	
	private EnumCouponTransactionStatus(String code, String description, int id) {
		super(code, id);
		this.description = description;
	}
	
	public static EnumCouponTransactionStatus getEnum(String code) {
		return (EnumCouponTransactionStatus) getEnum(EnumCouponTransactionStatus.class, code);
	}

	public static EnumCouponTransactionStatus getEnum(int id) {
		return (EnumCouponTransactionStatus) getEnum(EnumCouponTransactionStatus.class, id);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumCouponTransactionStatus.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumCouponTransactionStatus.class);
	}

	public static Iterator iterator() {
		return iterator(EnumCouponTransactionStatus.class);
	}
	
	public String getDescription(){
		return this.description;
	}

	public String toString() {
		return this.description;		
	}

}
