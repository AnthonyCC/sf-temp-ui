package com.freshdirect.delivery.announcement;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumUserLevel extends Enum {

	public static final EnumUserLevel GUEST = new EnumUserLevel("GUEST");
	public static final EnumUserLevel RECOGNIZED = new EnumUserLevel("RECOGNIZED");

	public EnumUserLevel(String name) {
		super(name);
	}

	public static EnumUserLevel getEnum(String name) {
		return (EnumUserLevel) getEnum(EnumUserLevel.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumUserLevel.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumUserLevel.class);
	}

	public static Iterator iterator() {
		return iterator(EnumUserLevel.class);
	}

}
