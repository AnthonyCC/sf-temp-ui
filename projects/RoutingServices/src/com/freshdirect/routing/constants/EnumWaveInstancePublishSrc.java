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
public class EnumWaveInstancePublishSrc extends Enum {		
	
	public static final EnumWaveInstancePublishSrc SCRIB = new EnumWaveInstancePublishSrc("SCRIB","SCRIB");

    public static final EnumWaveInstancePublishSrc PLAN = new EnumWaveInstancePublishSrc("PLAN","PLAN");
    
    private final String description;

	public EnumWaveInstancePublishSrc(String name, String description) {
		super(name);
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public static EnumWaveInstancePublishSrc getEnum(String name) {
		return (EnumWaveInstancePublishSrc) getEnum(EnumWaveInstancePublishSrc.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumWaveInstancePublishSrc.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumWaveInstancePublishSrc.class);
	}

	public static Iterator iterator() {
		return iterator(EnumWaveInstancePublishSrc.class);
	}

	public String toString() {
		return this.getName();
	}

}
