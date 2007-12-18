package com.freshdirect.mktAdmin.constants;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enum.Enum;

public class EnumCompetitorType extends Enum {

	public static final EnumCompetitorType MAJOR = new EnumCompetitorType("MAJOR", "Major");
	public static final EnumCompetitorType MINOR = new EnumCompetitorType("MINOR", "Minor");	
	public static final EnumCompetitorType WHOLE_FOODS = new EnumCompetitorType("WHOLE_FOODS", "Whole Foods");
	

	private final String description;
	
	public EnumCompetitorType(String name, String description) {
		super(name);
		this.description = description;
	}
	
	public static EnumCompetitorType getEnum(String name) {
		return (EnumCompetitorType) getEnum(EnumCompetitorType.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumCompetitorType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumCompetitorType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumCompetitorType.class);
	}

	public String toString() {
		return this.getName();
	}
	
	public String getDescription(){
		return this.description;
	}
    		
}

