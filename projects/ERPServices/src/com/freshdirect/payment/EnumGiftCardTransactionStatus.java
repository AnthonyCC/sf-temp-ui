package com.freshdirect.payment;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.ValuedEnum;

public class EnumGiftCardTransactionStatus extends ValuedEnum {
	
	public final static EnumGiftCardTransactionStatus SUCCESS = new EnumGiftCardTransactionStatus("S", "Approved", 0);
	public final static EnumGiftCardTransactionStatus FAILURE = new EnumGiftCardTransactionStatus("F", "Declined", 1);
	public final static EnumGiftCardTransactionStatus PENDING = new EnumGiftCardTransactionStatus("P", "Pending", 2);
	public final static EnumGiftCardTransactionStatus CANCEL = new EnumGiftCardTransactionStatus("C", "Cancelled", 3);

	private String description;
	
	private EnumGiftCardTransactionStatus(String code, String description, int id) {
		super(code, id);
		this.description = description;
	}
	
	public static EnumGiftCardTransactionStatus getEnum(String code) {
		return (EnumGiftCardTransactionStatus) getEnum(EnumGiftCardTransactionStatus.class, code);
	}

	public static EnumGiftCardTransactionStatus getEnum(int id) {
		return (EnumGiftCardTransactionStatus) getEnum(EnumGiftCardTransactionStatus.class, id);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumGiftCardTransactionStatus.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumGiftCardTransactionStatus.class);
	}

	public static Iterator iterator() {
		return iterator(EnumGiftCardTransactionStatus.class);
	}
	
	public String getDescription(){
		return this.description;
	}

	public String toString() {
		return this.description;		
	}

}
