package com.freshdirect.transadmin.constants;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumServiceStatus  extends Enum {
	
	public static final EnumServiceStatus ACTIVE = new EnumServiceStatus("ACT","Active");

    public static final EnumServiceStatus OUTSERVICCE = new EnumServiceStatus("OOS","Out of Service");
     
    private final String description;

	public EnumServiceStatus(String name, String description) {
		super(name);
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public static EnumServiceStatus getEnum(String name) {
		return (EnumServiceStatus) getEnum(EnumServiceStatus.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumServiceStatus.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumServiceStatus.class);
	}

	public static Iterator iterator() {
		return iterator(EnumServiceStatus.class);
	}

	public String toString() {
		return this.getDescription();
	}
}
