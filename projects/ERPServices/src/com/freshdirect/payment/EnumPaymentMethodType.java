package com.freshdirect.payment;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.ValuedEnum;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

	
public class EnumPaymentMethodType extends ValuedEnum {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8764418168037406726L;
	public final static EnumPaymentMethodType CREDITCARD = new EnumPaymentMethodType("CC", "Credit Card", 0);
	public final static EnumPaymentMethodType ECHECK = new EnumPaymentMethodType("EC", "E-Check", 1);
	public final static EnumPaymentMethodType GIFTCARD = new EnumPaymentMethodType("GC", "Gift-Card", 1);
	public final static EnumPaymentMethodType EBT = new EnumPaymentMethodType("ET", "EBT", 1);
	public final static EnumPaymentMethodType DEBITCARD = new EnumPaymentMethodType("DC", "DEBIT", 4);
	public final static EnumPaymentMethodType MASTERPASS = new EnumPaymentMethodType("MP", "MASTERPASS", 5);
	public final static EnumPaymentMethodType PAYPAL = new EnumPaymentMethodType("PP", "PAYPAL", 6);

	private String description;
	
	private EnumPaymentMethodType(String code, String description, int id) {
		super(code, id);
		this.description = description;
	}
	
	@JsonCreator
	public static EnumPaymentMethodType getEnum(String code) {
		return (EnumPaymentMethodType) getEnum(EnumPaymentMethodType.class, code);
	}
	@JsonCreator
	public static EnumPaymentMethodType getEnum(@JsonProperty("value") int id) {
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
	
	@JsonValue
	public String getCode(){
		return super.getName();
		
	}

}
