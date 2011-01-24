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
public class EnumWaveInstanceStatus extends Enum {		
	
	public static final EnumWaveInstanceStatus SYNCHRONIZED = new EnumWaveInstanceStatus("SYN","SYNC");

    public static final EnumWaveInstanceStatus NOTSYNCHRONIZED = new EnumWaveInstanceStatus("NYN","NOSYNC");
    
    private final String description;

	public EnumWaveInstanceStatus(String name, String description) {
		super(name);
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public static EnumWaveInstanceStatus getEnum(String name) {
		return (EnumWaveInstanceStatus) getEnum(EnumWaveInstanceStatus.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumWaveInstanceStatus.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumWaveInstanceStatus.class);
	}

	public static Iterator iterator() {
		return iterator(EnumWaveInstanceStatus.class);
	}

	public String toString() {
		return this.getName();
	}

}
