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
public class EnumArithmeticOperator extends Enum {		
	
	public static final EnumArithmeticOperator ADD = new EnumArithmeticOperator("+","ADD");

    public static final EnumArithmeticOperator SUB = new EnumArithmeticOperator("-","SUB");
    
    private final String description;

	public EnumArithmeticOperator(String name, String description) {
		super(name);
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public static EnumArithmeticOperator getEnum(String name) {
		return (EnumArithmeticOperator) getEnum(EnumArithmeticOperator.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumArithmeticOperator.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumArithmeticOperator.class);
	}

	public static Iterator iterator() {
		return iterator(EnumArithmeticOperator.class);
	}

	public String toString() {
		return this.getName();
	}

}
