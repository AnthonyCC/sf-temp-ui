package com.freshdirect.payment.fraud;


import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enum.ValuedEnum;

public class EnumRestrictedPaymentMethodStatus extends ValuedEnum {

    public final static EnumRestrictedPaymentMethodStatus BAD		= new EnumRestrictedPaymentMethodStatus("BAD", "Bad", 0);
	public final static EnumRestrictedPaymentMethodStatus PENDING	= new EnumRestrictedPaymentMethodStatus("PND", "Pending Approval", 1);
    public final static EnumRestrictedPaymentMethodStatus DELETED	= new EnumRestrictedPaymentMethodStatus("DEL", "Deleted", 2);

	private String description;
	
	private EnumRestrictedPaymentMethodStatus(String code, String description, int id) {
		super(code, id);
		this.description = description;
	}
	
	public static EnumRestrictedPaymentMethodStatus getEnum(String code) {
		return (EnumRestrictedPaymentMethodStatus) getEnum(EnumRestrictedPaymentMethodStatus.class, code);
	}

	public static EnumRestrictedPaymentMethodStatus getEnum(int id) {
		return (EnumRestrictedPaymentMethodStatus) getEnum(EnumRestrictedPaymentMethodStatus.class, id);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumRestrictedPaymentMethodStatus.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumRestrictedPaymentMethodStatus.class);
	}

	public static Iterator iterator() {
		return iterator(EnumRestrictedPaymentMethodStatus.class);
	}
	
	public String getDescription(){
		return this.description;
	}
	
}
