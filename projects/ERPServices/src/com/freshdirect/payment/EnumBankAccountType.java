package com.freshdirect.payment;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.ValuedEnum;

public class EnumBankAccountType extends ValuedEnum {

	public final static EnumBankAccountType PERSONAL_CHECKING = new EnumBankAccountType("C", "Checking Account", 0);
	public final static EnumBankAccountType PERSONAL_SAVINGS = new EnumBankAccountType("S", "Savings Account", 1);
//	public final static EnumBankAccountType CORPORATE_CHECKING = new EnumBankAccountType("X", "Corporate Checking Account", 2);

	private String description;
	
	private EnumBankAccountType(String code, String description, int id) {
		super(code, id);
		this.description = description;
	}
	
	public static EnumBankAccountType getEnum(String code) {
		return (EnumBankAccountType) getEnum(EnumBankAccountType.class, code);
	}

	public static EnumBankAccountType getEnum(int id) {
		return (EnumBankAccountType) getEnum(EnumBankAccountType.class, id);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumBankAccountType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumBankAccountType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumBankAccountType.class);
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public String toString() {
		return this.description;		
	}
	
}
