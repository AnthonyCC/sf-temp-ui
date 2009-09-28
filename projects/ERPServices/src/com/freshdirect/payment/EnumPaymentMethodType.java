package com.freshdirect.payment;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.ValuedEnum;

public class EnumPaymentMethodType extends ValuedEnum {
	
	public final static EnumPaymentMethodType CREDITCARD = new EnumPaymentMethodType("CC", "Credit Card", 0);
	public final static EnumPaymentMethodType ECHECK = new EnumPaymentMethodType("EC", "E-Check", 1);
	public final static EnumPaymentMethodType GIFTCARD = new EnumPaymentMethodType("GC", "Gift-Card", 1);

	private String description;
	
	private EnumPaymentMethodType(String code, String description, int id) {
		super(code, id);
		this.description = description;
	}
	
	public static EnumPaymentMethodType getEnum(String code) {
		return (EnumPaymentMethodType) getEnum(EnumPaymentMethodType.class, code);
	}

	public static EnumPaymentMethodType getEnum(int id) {
		return (EnumPaymentMethodType) getEnum(EnumPaymentMethodType.class, id);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumPaymentMethodType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumPaymentMethodType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumPaymentMethodType.class);
	}
	
	public String getDescription(){
		return this.description;
	}

	public String toString() {
		return this.description;		
	}

}
