package com.freshdirect.customer;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumATCContext extends Enum {

	public final static EnumATCContext DDPP			= new EnumATCContext("DDPP", "DDPP Page");
	public final static EnumATCContext SEARCH		= new EnumATCContext("SRCH", "Search Page");
	public final static EnumATCContext ECOUPON		= new EnumATCContext("ECOU", "Ecoupon Circular Page");
	public final static EnumATCContext NEWPRODUCTS	= new EnumATCContext("NEWP", "New Products Page");

	private final String description;

	private EnumATCContext(String name, String description) {
		super(name);
		this.description = description;
	}

	public String getCode() {
		return this.getName();
	}

	public String getDescription() {
		return this.description;
	}

	public static EnumATCContext getEnum(String name) {
		return (EnumATCContext) getEnum(EnumATCContext.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumATCContext.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumATCContext.class);
	}

	public String toString() {
		return this.getName();
	}

}
