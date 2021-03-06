package com.freshdirect.delivery;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.ValuedEnum;

public class EnumReservationType extends ValuedEnum {

	private static final long	serialVersionUID	= -5777887358203546933L;
	
	public final static EnumReservationType STANDARD_RESERVATION 	= new EnumReservationType("STD", "Standard", 0);
	public final static EnumReservationType ONETIME_RESERVATION  	= new EnumReservationType("OTR", "One Time Pre-Reservation", 1);
	public final static EnumReservationType RECURRING_RESERVATION	= new EnumReservationType("WRR", "Weekly Recurring Reservation", 2);
	
	private String description;
	
	private EnumReservationType(String code, String description, int id) {
		super(code, id);
		this.description = description;
	}
	
	public static EnumReservationType getEnum(String code) {
		return (EnumReservationType) getEnum(EnumReservationType.class, code);
	}

	public static EnumReservationType getEnum(int id) {
		return (EnumReservationType) getEnum(EnumReservationType.class, id);
	}

	@SuppressWarnings( "unchecked" )
	public static Map<String,EnumReservationType> getEnumMap() {
		return getEnumMap(EnumReservationType.class);
	}

	@SuppressWarnings( "unchecked" )
	public static List<EnumReservationType> getEnumList() {
		return getEnumList(EnumReservationType.class);
	}

	@SuppressWarnings( "unchecked" )
	public static Iterator<EnumReservationType> iterator() {
		return iterator(EnumReservationType.class);
	}
	
	public String getDescription(){
		return this.description;
	}

}
