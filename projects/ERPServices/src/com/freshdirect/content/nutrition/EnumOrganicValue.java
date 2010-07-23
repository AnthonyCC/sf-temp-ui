package com.freshdirect.content.nutrition;

import java.util.*;

/**
 * Type-safe enumeration for attributes' data types.
 */
public class EnumOrganicValue implements NutritionValueEnum {
	
	private static final long	serialVersionUID	= -5777759811772680536L;
	
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
    
	//Changed for APPDEV-705
    private final static List<EnumOrganicValue> types = new ArrayList<EnumOrganicValue>();
    static {
    	types.add(new EnumOrganicValue("NONE", "None", 0));
		//types.add(new EnumOrganicValue("70_ORGN", "Made with Organic Ingredients ", 2));
        //types.add(new EnumOrganicValue("CALI_ORGN", "Organically grown and processed in accordance with California Organic Act of 1990", 2));
		//types.add(new EnumOrganicValue("CERT_ICS", "Certified organic by international certification services, inc. ", 2));
        types.add(new EnumOrganicValue("CERT_ORGN", "Certified Organic", 2));
		//types.add(new EnumOrganicValue("CERT_PCO", "Pennsylvania Certified Organic ", 2));
		//types.add(new EnumOrganicValue("CERT_QCS", "Quality certifying services (QCS)", 2));
		types.add(new EnumOrganicValue("MADEWORGN", "Made with Organic Ingredients ", 2));
		//types.add(new EnumOrganicValue("ORE_ORGN", "Oregon Tilth Certified Organic", 2));
		types.add(new EnumOrganicValue("ORGN", "Organic", 1));
        //types.add(new EnumOrganicValue("QAI_ORGN", "Certified organic by QAI (Quality Assurance International", 2));
        //types.add(new EnumOrganicValue("TEX_ORGN", "Certified organic by the Texas Department of Agriculture", 2));
		//types.add(new EnumOrganicValue("VER_ORGN", "Certified Organic by Vermont Organic Farmers", 2));
    }

    public static List<EnumOrganicValue> getValues() {
    	Collections.sort(types, OrganicValueComparator);
        return Collections.unmodifiableList(types);
    }
    
    public static EnumOrganicValue getValueForCode(String code) {
        for ( EnumOrganicValue value : types ) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
    
	public static Comparator<EnumOrganicValue> OrganicValueComparator = new Comparator<EnumOrganicValue>(){
		public int compare(EnumOrganicValue organic1, EnumOrganicValue organic2) {
			int diff = organic1.getPriority()- organic2.getPriority();
			if (diff==0) {
				diff = organic1.getName().compareTo(organic2.getName());
			}
			return diff;
		}
	};
    
}
