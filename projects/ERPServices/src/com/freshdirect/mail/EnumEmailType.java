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
public class EnumEmailType extends Enum {
	public static final EnumEmailType TEXT = new EnumEmailType("TEXT", "Text Version");
	public static final EnumEmailType HTML = new EnumEmailType("HTML", "Html version");
	public static final EnumEmailType TEXT_MULTIPART = new EnumEmailType("TEXT_MULTI", "Text Multipart");	
	
    private String description;

    protected EnumEmailType(String name, String description) {
		super(name);
	    this.description = description;
	}
	
	public static EnumEmailType getEnum(String type) {
		return (EnumEmailType) getEnum(EnumEmailType.class, type);
	}
		
	public static Map getEnumMap() {
		return getEnumMap(EnumEmailType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumEmailType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumEmailType.class);
	}

	public String getDescription(){
		return this.description;
	}

	public String toString() {
		return this.description;		
	}	
}
