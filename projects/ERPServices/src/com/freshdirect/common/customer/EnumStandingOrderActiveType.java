package com.freshdirect.common.customer;

import org.apache.commons.lang.enums.ValuedEnum;

public class EnumStandingOrderActiveType extends ValuedEnum{

	public final static EnumStandingOrderActiveType ACTIVE = new EnumStandingOrderActiveType(0, "Y");
	public final static EnumStandingOrderActiveType NON_ACTIVE = new EnumStandingOrderActiveType(1, "N");
	
	int value;
	String name;
	
	public  EnumStandingOrderActiveType( int value,String name) {
		super(name,value);
        this.value=value;
        this.name=name;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static EnumStandingOrderActiveType getSOType(int code) {
		return getEnum(code);
	}
	
	public static EnumStandingOrderActiveType getEnum(int code) {
		return (EnumStandingOrderActiveType) getEnum(EnumStandingOrderActiveType.class, code);
	}
}
