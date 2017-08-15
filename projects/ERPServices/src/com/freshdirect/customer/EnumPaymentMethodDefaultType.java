package com.freshdirect.customer;

public enum EnumPaymentMethodDefaultType {

	/** Default by customer. */
	DEFAULT_CUST("DC"),	
	
	/** Default by system. */
	DEFAULT_SYS("DS"),
	
	/**UNDEFINED for other cases */
	UNDEFINED("UD");
	
	String name;
	
	/**
	 *  Constructor.
	 *  
	 *  @param typeName the name of the list type
	 */
	private EnumPaymentMethodDefaultType(String typeName) {
		this.name = typeName;
	}

	public String getName() {
		return name;
	}

	
	/**
	 *  Return a symbol for default payment method type.
	 *  
	 *  @return the name of default payment method type.
	 */
	@Override
	public String toString() {
		return this.getName();
	}
	
	public static EnumPaymentMethodDefaultType getByName(String name) {
		if ("DC".equals(name)) {
			return EnumPaymentMethodDefaultType.DEFAULT_CUST;
		} else if ("DS".equals(name)) {
			return EnumPaymentMethodDefaultType.DEFAULT_SYS;
		} else {
			return EnumPaymentMethodDefaultType.UNDEFINED;
		}
	}
}
