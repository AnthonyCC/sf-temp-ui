/*
 * Created on Jun 30, 2005
 *
 */
package com.freshdirect.routing.constants;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

/**
 * @author Sivachandar
 *
 */
public class EnumTransportationFacilitySrc extends Enum {		
	
	public static final EnumTransportationFacilitySrc DELIVERYZONE = new EnumTransportationFacilitySrc("SIT", "REGULARDELIVERY");
	public static final EnumTransportationFacilitySrc CROSSDOCK = new EnumTransportationFacilitySrc("CD", "CROSS DOCK");
	public static final EnumTransportationFacilitySrc DEPOTDELIVERY = new EnumTransportationFacilitySrc("DPT", "REGULARDEPOT");
	 
    private final String description;

	public EnumTransportationFacilitySrc(String name, String description) {
		super(name);
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public static EnumTransportationFacilitySrc getEnum(String name) {
		return (EnumTransportationFacilitySrc) getEnum(EnumTransportationFacilitySrc.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumTransportationFacilitySrc.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumTransportationFacilitySrc.class);
	}

	public static Iterator iterator() {
		return iterator(EnumTransportationFacilitySrc.class);
	}

	public String toString() {
		return this.getName();
	}

}
