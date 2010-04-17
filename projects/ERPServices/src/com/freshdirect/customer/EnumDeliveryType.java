package com.freshdirect.customer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author  jmccarter
 * @version
 */
public class EnumDeliveryType implements Serializable {

	private static final long	serialVersionUID	= -1948272245997890928L;

	private final static Map<String,EnumDeliveryType> DELIVERY_TYPE_MAP = new HashMap<String,EnumDeliveryType>();
	
	private static int idCounter = 0;

	public final static EnumDeliveryType	HOME				= new EnumDeliveryType( "H", "Home Delivery", "003" );
	public final static EnumDeliveryType	DEPOT				= new EnumDeliveryType( "D", "Depot Delivery", "002" );
	public final static EnumDeliveryType	PICKUP				= new EnumDeliveryType( "P", "Pickup", "002" );
	public final static EnumDeliveryType	CORPORATE			= new EnumDeliveryType( "C", "Corporate Delivery", "004" );
	// Added for Gift cards.
	public static final EnumDeliveryType	GIFT_CARD_PERSONAL	= new EnumDeliveryType( "GP", "Personal Delivery", "003" );
	public static final EnumDeliveryType	GIFT_CARD_CORPORATE	= new EnumDeliveryType( "GC", "Professional Delivery", "004" );

	// Added for Robin Hood - Donation..
	public static final EnumDeliveryType	DONATION_INDIVIDUAL	= new EnumDeliveryType( "DI", "Personal Delivery", "003" );
	public static final EnumDeliveryType	DONATION_BUSINESS	= new EnumDeliveryType( "DB", "Professional Delivery", "004" );

	private final int id;
	private final String code;
	private final String name;
	private final String deliveryModel;

	private EnumDeliveryType(String code, String name, String deliveryModel) {
		this.id = idCounter++;
		this.code = code;
		this.name = name;
		this.deliveryModel = deliveryModel;

		DELIVERY_TYPE_MAP.put(code, this);
	}

	public String getCode() {
		return this.code;
	}

	public String getName() {
		return this.name;
	}

	public String getDeliveryModel() {
		return this.deliveryModel;
	}

	public String toString() {
		return this.code;
	}

	public static EnumDeliveryType getDeliveryType(String code) {
		return DELIVERY_TYPE_MAP.get(code);
	}

	public boolean equals(Object o) {
		if ( o != null && o instanceof EnumDeliveryType ) {
			return this.id == ((EnumDeliveryType) o).id;
		}
		return false;
	}
}