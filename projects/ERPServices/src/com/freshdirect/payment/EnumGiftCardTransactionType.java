package com.freshdirect.payment;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.ValuedEnum;

public class EnumGiftCardTransactionType extends ValuedEnum {
	
	public final static EnumGiftCardTransactionType REGISTER = new EnumGiftCardTransactionType("REG", "Register", 0);
	public final static EnumGiftCardTransactionType PRE_AUTH = new EnumGiftCardTransactionType("PRE", "Pre Authorization", 1);
	public final static EnumGiftCardTransactionType REVERSE_PRE_AUTH = new EnumGiftCardTransactionType("REV-PRE", "Pre Authorization", 2);
	public final static EnumGiftCardTransactionType POST_AUTH = new EnumGiftCardTransactionType("POST", "Post Authorization", 3);
	public final static EnumGiftCardTransactionType EMAIL = new EnumGiftCardTransactionType("EMAIL", "Sending Email", 4);
	public final static EnumGiftCardTransactionType BALANCE_TRANSFER = new EnumGiftCardTransactionType("BT", "Balance Transfer", 5);
	
	private String description;
	
	private EnumGiftCardTransactionType(String code, String description, int id) {
		super(code, id);
		this.description = description;
	}
	
	public static EnumGiftCardTransactionType getEnum(String code) {
		return (EnumGiftCardTransactionType) getEnum(EnumGiftCardTransactionType.class, code);
	}

	public static EnumGiftCardTransactionType getEnum(int id) {
		return (EnumGiftCardTransactionType) getEnum(EnumGiftCardTransactionType.class, id);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumGiftCardTransactionType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumGiftCardTransactionType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumGiftCardTransactionType.class);
	}
	
	public String getDescription(){
		return this.description;
	}

	public String toString() {
		return this.description;		
	}

}
