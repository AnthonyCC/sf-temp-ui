package com.freshdirect.fdstore;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enum.Enum;
/**
*
* @author  skrishnasamy
* @version 1.0
* @created 05-Jun-2006
* 
*/
public class EnumSpecialProductType  extends Enum {
	
	public static final EnumSpecialProductType DELIVERY_PASS = new EnumSpecialProductType("DeliveryPass");
 
	
	private EnumSpecialProductType(String name) {
		super(name);
	}


	public static EnumSpecialProductType getEnum(String name) {
		return (EnumSpecialProductType) getEnum(EnumSpecialProductType.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumSpecialProductType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumSpecialProductType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumSpecialProductType.class);
	}
}
