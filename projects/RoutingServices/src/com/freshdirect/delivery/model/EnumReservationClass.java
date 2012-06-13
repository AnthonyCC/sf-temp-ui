package com.freshdirect.delivery.model;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.enums.ValuedEnum;

public class EnumReservationClass extends ValuedEnum{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7350065941851551980L;
	public final static EnumReservationClass PREMIUM 	= new EnumReservationClass("P", "Premium", 0);
	public final static EnumReservationClass PREMIUMCT  	= new EnumReservationClass("PC", "Premium Chef table", 1);
	
	private String description;
	
	private EnumReservationClass(String code, String description, int id) {
		super(code, id);
		this.description = description;
	}
		
	public static EnumReservationClass getEnum(String code) {
		return (EnumReservationClass) getEnum(EnumReservationClass.class, code);
	}

	public static EnumReservationClass getEnum(int id) {
		return (EnumReservationClass) getEnum(EnumReservationClass.class, id);
	}

	@SuppressWarnings( "unchecked" )
	public static Map<String,EnumReservationClass> getEnumMap() {
		return getEnumMap(EnumReservationClass.class);
	}

	@SuppressWarnings( "unchecked" )
	public static List<EnumReservationClass> getEnumList() {
		return getEnumList(EnumReservationClass.class);
	}

	@SuppressWarnings( "unchecked" )
	public static Iterator<EnumReservationClass> iterator() {
		return iterator(EnumReservationClass.class);
	}
	
	public String getDescription(){
		return this.description;
	}


}
