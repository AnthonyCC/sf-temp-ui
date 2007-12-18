package com.freshdirect.customer;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enum.Enum;

public class EnumFraudReason extends Enum {

	public static final EnumFraudReason DEACTIVATED_ACCOUNT = new EnumFraudReason("DISABLED_ACCOUNT", "Deactivated account");
	public static final EnumFraudReason DUP_ACCOUNT_NUMBER = new EnumFraudReason("DUP_ACCOUNT_NUMBER", "Duplicate account number");
	public static final EnumFraudReason MAX_ORDER_TOTAL = new EnumFraudReason("MAX_ORDER_TOTAL", "Order amount above maximum ($750)");
	public static final EnumFraudReason MAX_MAKEGOOD = new EnumFraudReason("MAX_MAKEGOOD", "Make-good order amount too large");
	public static final EnumFraudReason MAX_PASSWORD_HINT = new EnumFraudReason("MAX_PASSWORD_HINT", "Too many unsuccessful password hint guesses.");
	public static final EnumFraudReason DUP_SHIPTO_ADDRESS = new EnumFraudReason("DUP_SHIPTO_ADDRESS","Duplicate Ship-to address.")	;
	public static final EnumFraudReason DUP_PHONE = new EnumFraudReason("DUP_PHONE","Duplicate phone number.")	;
	private final String description;

	public EnumFraudReason(String name, String description) {
		super(name);
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public static EnumFraudReason getEnum(String name) {
		return (EnumFraudReason) getEnum(EnumFraudReason.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumFraudReason.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumFraudReason.class);
	}

	public static Iterator iterator() {
		return iterator(EnumFraudReason.class);
	}

}
