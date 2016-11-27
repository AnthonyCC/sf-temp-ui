package com.freshdirect.customer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class EnumSaleType implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4733656286253793374L;

	/** Maps EnumSaleStatus status codes to the proper EnumSaleStatus */
	private final static Map SALETYPE_MAP = new HashMap();

	public final static EnumSaleType REGULAR= new EnumSaleType(0, "REG", "Regular order");

	public final static EnumSaleType SUBSCRIPTION= new EnumSaleType(1, "SUB", "Subscription order");
	
	
	public final static EnumSaleType DONATION = new EnumSaleType(3, "DON", "Donation order");
	

    public final static EnumSaleType GIFTCARD= new EnumSaleType(2, "GCD", "GiftCard order");
        
	private final int id;
	private final String saleType;
	private final String name;
	
	private EnumSaleType(int id, String saleType, String name) {
		this.id = id;
		this.saleType = saleType;
		this.name = name;
		SALETYPE_MAP.put( this.saleType, this );
	}

	public String getSaleType() {
		return this.saleType;
	}

    public String getName() {
        return this.name;
    }
    

	public static EnumSaleType getSaleType(String saleType){
		return (EnumSaleType) SALETYPE_MAP.get( saleType.toUpperCase() );
	}
	
	public String toString() {
		return this.name;
	}

	public boolean equals(Object o) {
		if (o instanceof EnumSaleType) {
			return this.id == ((EnumSaleType)o).id;
		}
		return false;
	}
}