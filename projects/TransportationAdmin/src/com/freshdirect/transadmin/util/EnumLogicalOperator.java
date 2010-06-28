/*
 * Created on Jun 30, 2005
 *
 */
package com.freshdirect.transadmin.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

/**
 * @author Sivachandar
 *
 */
public class EnumLogicalOperator extends Enum {		
	
	public static final EnumLogicalOperator ADD = new EnumLogicalOperator("+","ADD");

    public static final EnumLogicalOperator SUB = new EnumLogicalOperator("-","SUB");
    
    private final String description;

	public EnumLogicalOperator(String name, String description) {
		super(name);
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public static EnumLogicalOperator getEnum(String name) {
		return (EnumLogicalOperator) getEnum(EnumLogicalOperator.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumLogicalOperator.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumLogicalOperator.class);
	}

	public static Iterator iterator() {
		return iterator(EnumLogicalOperator.class);
	}

	public String toString() {
		return this.getName();
	}

}
