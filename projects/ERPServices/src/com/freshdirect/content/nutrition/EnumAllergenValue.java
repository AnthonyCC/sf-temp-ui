/*
 * $Workfile:EnumAllergenValue.java$
 *
 * $Date:7/21/2003 7:05:57 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.content.nutrition;

import java.util.*;

/**
 * Type-safe enumeration for attributes' data types.
 *
 * @version $Revision:4$
 * @author $Author:Mike Rose$
 */
public class EnumAllergenValue implements NutritionValueEnum {
	
	private static final long	serialVersionUID	= -225573183246293681L;
	
	private final String code;
    private final String name;
    private final int priority;

	private EnumAllergenValue(String code, String name, int priority) {
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
		if (o instanceof EnumAllergenValue) {
			return this.code.equals(((EnumAllergenValue)o).getCode());
		}
		return false;
	}
    
    private final static List<EnumAllergenValue> types = new ArrayList<EnumAllergenValue>();
    static {
    	types.add(new EnumAllergenValue("NONE", "None", 0));
        types.add(new EnumAllergenValue("C_EGG", "Contains Eggs", 1));
        types.add(new EnumAllergenValue("C_PNT", "Contains Peanuts", 1));
        types.add(new EnumAllergenValue("C_TNT", "Contains Tree Nuts", 1));
        types.add(new EnumAllergenValue("C_FSH", "Contains Fish", 1));
        types.add(new EnumAllergenValue("C_SHL", "Contains Shellfish", 1));
        types.add(new EnumAllergenValue("C_DAI", "Contains Dairy", 1));
        types.add(new EnumAllergenValue("C_SOY", "Contains Soy", 1));
        types.add(new EnumAllergenValue("C_SYB", "Contains Soybean", 1));
        types.add(new EnumAllergenValue("C_WHT", "Contains Wheat", 1));
        types.add(new EnumAllergenValue("C_GLT", "Contains Gluten", 1));
        types.add(new EnumAllergenValue("C_ALM", "Contains Almonds", 1));
        types.add(new EnumAllergenValue("C_MLK", "Contains Milk Ingredients", 1));
        types.add(new EnumAllergenValue("C_PIN", "Contains Pine Nuts", 1));
        types.add(new EnumAllergenValue("C_PHN", "Phenylketonurics: Contains Phenylalanine", 1));
        types.add(new EnumAllergenValue("C_WNT", "Contains Walnuts", 1));
        types.add(new EnumAllergenValue("C_SES", "Contains Sesame Seed and/or Sesame Oil", 1));
        types.add(new EnumAllergenValue("C_CSY", "Corn Used In This Product Contains Traces Of Soybeans", 1));
		types.add(new EnumAllergenValue("C_SUL",  "Contains Sulfites", 1));
		types.add(new EnumAllergenValue("C_MAL",  "Contains Malt", 1));
		types.add(new EnumAllergenValue("C_LAC",  "Contains Lactose", 1));
		types.add(new EnumAllergenValue("C_CAS",  "Contains Cashews", 1));
		types.add(new EnumAllergenValue("C_PEC",  "Contains Pecans", 1));
		types.add(new EnumAllergenValue("C_GLY",  "Contains Glycerin", 1));
		
        types.add(new EnumAllergenValue("MC_EGG", "May Contain Eggs", 1));
        types.add(new EnumAllergenValue("MC_PNT", "May Contain Peanuts", 1));
        types.add(new EnumAllergenValue("MC_TNT", "May Contain Tree Nuts", 1));
        types.add(new EnumAllergenValue("MC_FSH", "May Contain Fish", 1));
        types.add(new EnumAllergenValue("MC_SHL", "May Contain Shellfish", 1));
        types.add(new EnumAllergenValue("MC_SOY", "May Contain Soy", 1));
        types.add(new EnumAllergenValue("MC_WHT", "May Contain Wheat", 1));
        types.add(new EnumAllergenValue("MC_ALM", "May Contain Almonds", 1));
        types.add(new EnumAllergenValue("MC_MLK", "May Contain Milk Ingredients", 1));
        types.add(new EnumAllergenValue("MC_NUT", "May Contain Traces of Peanuts and Other Nuts", 1));
        types.add(new EnumAllergenValue("MC_NTE", "Made On Equipment That Processes Peanuts and Other Nuts", 1));
        types.add(new EnumAllergenValue("MC_NTO", "May Contain Traces of Nut Oils", 1));
        types.add(new EnumAllergenValue("MC_NMP", "Manufactured in a plant that processes nuts and milk products", 1));
        types.add(new EnumAllergenValue("MC_SES", "May contain sesame seed and/or sesame oil", 1));
        

        types.add(new EnumAllergenValue("MC_PEA", "Produced in our bakery, where tree nuts and peanuts are used.", 1));
    }

    public static List<EnumAllergenValue> getValues() {
    	Collections.sort(types, AllergenValueComparator);
        return Collections.unmodifiableList(types);
    }
    
    public static EnumAllergenValue getValueForCode(String code) {
        for ( EnumAllergenValue value : types ) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
    
	public static Comparator<EnumAllergenValue> AllergenValueComparator = new Comparator<EnumAllergenValue>(){
		public int compare(EnumAllergenValue allergen1, EnumAllergenValue allergen2) {
			int diff = allergen1.getPriority()- allergen2.getPriority();
			if (diff==0) {
				diff = allergen1.getName().compareTo(allergen2.getName());
			}
			return diff;
		}
	};
    
}
