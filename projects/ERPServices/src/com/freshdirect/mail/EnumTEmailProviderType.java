package com.freshdirect.mail;

/*
 * Created on Jun 2, 2005
 */

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

/**
 * @author jng
 *
 */
public class EnumTEmailProviderType extends Enum {
	public static final EnumTEmailProviderType CHEETAH = new EnumTEmailProviderType("CHEETAH", "Cheetah Provider");
	public static final EnumTEmailProviderType FDSTORE = new EnumTEmailProviderType("FDSTORE", "Internal Email provider");

    private String description;

    protected EnumTEmailProviderType(String name, String description) {
		super(name);
	    this.description = description;
	}
	
	public static EnumTEmailProviderType getEnum(String type) {
		return (EnumTEmailProviderType) getEnum(EnumTEmailProviderType.class, type);
	}
		
	public static Map getEnumMap() {
		return getEnumMap(EnumTEmailProviderType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumTEmailProviderType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumTEmailProviderType.class);
	}

	public String getDescription(){
		return this.description;
	}

	public String toString() {
		return this.description;		
	}	
}
