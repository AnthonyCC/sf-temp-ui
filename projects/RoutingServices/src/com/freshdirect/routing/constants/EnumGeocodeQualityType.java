package com.freshdirect.routing.constants;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumGeocodeQualityType extends Enum {
	
	public static final EnumGeocodeQualityType AUTO = new EnumGeocodeQualityType("grAuto", "Auto Street");
	public static final EnumGeocodeQualityType MANUAL = new EnumGeocodeQualityType("grManual", "Self Geocode from Map");
	public static final EnumGeocodeQualityType GPS = new EnumGeocodeQualityType("grGPS", "From GPS");
	public static final EnumGeocodeQualityType CITY = new EnumGeocodeQualityType("grCity", "City Geocode");
	public static final EnumGeocodeQualityType ZIP = new EnumGeocodeQualityType("grZip", "Postal Geocode");
	public static final EnumGeocodeQualityType ZIPPLUS4 = new EnumGeocodeQualityType("grZipPlus4", "Postal+4 Geocode");
	public static final EnumGeocodeQualityType UNSUCCESSFUL = new EnumGeocodeQualityType("grUnsuccessful", "Unsuccessful");
	
	public static final EnumGeocodeQualityType USERMANUAL = new EnumGeocodeQualityType("grUserManual", "User Manually Entered Geocode");
	public static final EnumGeocodeQualityType STOREFRONTEXCEPTION = new EnumGeocodeQualityType("grStoreFrontException", "Storefront Exception");
	public static final EnumGeocodeQualityType STOREFRONTGEOCODE = new EnumGeocodeQualityType("grStoreFrontGeocode", "Storefront Geocode");
	public static final EnumGeocodeQualityType STOREFRONTUNSUCCESSFULGEOCODE = new EnumGeocodeQualityType("grStoreFrontUnsuccessful", "Storefront Unsuccessful");
	

	private final String description;
	
	public EnumGeocodeQualityType(String code, String description) {
		super(code);
		this.description = description;
	}
	
	public static EnumGeocodeQualityType getEnum(String code) {
		return (EnumGeocodeQualityType) getEnum(EnumGeocodeQualityType.class, code);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumGeocodeQualityType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumGeocodeQualityType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumGeocodeQualityType.class);
	}

	public String toString() {
		return this.getName();
	}

	public String getDescription() {
		return description;
	}

}

