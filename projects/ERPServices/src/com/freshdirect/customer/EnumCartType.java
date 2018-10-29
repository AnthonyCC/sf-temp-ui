package com.freshdirect.customer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class EnumCartType extends Enum implements Serializable {

private final static Map<String,EnumCartType> CART_TYPE_MAP = new HashMap<String,EnumCartType>();
	
	private static int idCounter = 0;

	public final static EnumCartType	REGULAR				= new EnumCartType( "REG", "Regular Grocery Cart");
	public final static EnumCartType	DLV_PASS				= new EnumCartType( "DPC", "DeliveryPass Cart" );
	public final static EnumCartType	GIFT_CARD				= new EnumCartType( "GFC", "Giftcard Cart" );
	public final static EnumCartType	DONATION			= new EnumCartType( "DGC", "Donation Cart");
	private final int id;
	private final String code;
	private final String displayName;

	private EnumCartType(String code, String name ) {
		super(code);
		this.id = idCounter++;
		this.code = code;
		this.displayName = name;

		CART_TYPE_MAP.put(code, this);
	}

	@JsonValue
	public String getCode() {
		return this.code;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public String toString() {
		return this.code;
	}

	@JsonCreator
	public static EnumCartType getShoppingCartType(String code) {
		return CART_TYPE_MAP.get(code);
	}
}
