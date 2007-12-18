/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.content.nutrition;

import java.util.*;

/**
 * Type-safe enumeration for attributes' data types.
 *
 * @version $Revision$
 * @author $Author$
 */
public class EnumOrganicValue implements NutritionValueEnum {
	
	private final String code;
    private final String name;
    private final int priority;

	private EnumOrganicValue(String code, String name, int priority) {
		this.code = code;
        this.name = name;
        this.priority = priority;
	}

	public String getCode() {
		return this.code;
	}
          
    public String getName() {
        return this.name;
    }
	
	public String toString() {
		return this.name;
	}
	
	public int getPriority(){
		return this.priority;
	}

	public boolean equals(Object o) {
		if (o instanceof EnumOrganicValue) {
			return this.code.equals(((EnumOrganicValue)o).getCode());
		}
		return false;
	}
    
    private final static List types = new ArrayList();
    static {
    	types.add(new EnumOrganicValue("NONE", "None", 0));
		types.add(new EnumOrganicValue("ORGN", "Organic", 1));
        types.add(new EnumOrganicValue("CERT_ORGN", "Certified Organic", 2));
        types.add(new EnumOrganicValue("CALI_ORGN", "Organically grown and processed in accordance with California Organic Act of 1990", 2));
        types.add(new EnumOrganicValue("QAI_ORGN", "Certified organic by QAI (Quality Assurance International", 2));
        types.add(new EnumOrganicValue("TEX_ORGN", "Certified organic by the Texas Department of Agriculture", 2));
		types.add(new EnumOrganicValue("VER_ORGN", "Certified Organic by Vermont Organic Farmers", 2));
		types.add(new EnumOrganicValue("ORE_ORGN", "Oregon Tilth Certified Organic", 2));
		types.add(new EnumOrganicValue("CERT_QCS", "Quality certifying services (QCS)", 2));
		types.add(new EnumOrganicValue("CERT_ICS", "Certified organic by international certification services, inc. ", 2));
		types.add(new EnumOrganicValue("CERT_PCO", "Pennsylvania Certified Organic ", 2));
		types.add(new EnumOrganicValue("MADEWORGN", "Made with Organic Ingredients ", 2));
		types.add(new EnumOrganicValue("70_ORGN", "70% Organic Ingredients ", 2));
    }

    public static List getValues() {
    	Collections.sort(types, OrganicValueComparator);
        return Collections.unmodifiableList(types);
    }
    
    public static EnumOrganicValue getValueForCode(String code) {
        for (Iterator i=types.iterator(); i.hasNext(); ) {
            EnumOrganicValue value = (EnumOrganicValue) i.next();
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
    
	public static Comparator OrganicValueComparator = new Comparator(){
		public int compare(Object o1, Object o2) {
			EnumOrganicValue organic1 = (EnumOrganicValue) o1;
			EnumOrganicValue organic2 = (EnumOrganicValue) o2;
		   	
			int diff = organic1.getPriority()- organic2.getPriority();
			if (diff==0) {
				diff = organic1.getName().compareTo(organic2.getName());
			}
			return diff;
		}
	};
    
}
