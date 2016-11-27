package com.freshdirect.content.nutrition;

import java.util.*;

/**
 * Type-safe enumeration for attributes' data types.
 */
public class EnumKosherTypeValue implements NutritionValueEnum {
    
	private static final long	serialVersionUID	= -5550049563740958900L;
	
	public final static EnumKosherTypeValue NOT_KOSHER  = new EnumKosherTypeValue("NOT_KOSHER", "Not Kosher");
    public final static EnumKosherTypeValue NONE		= new EnumKosherTypeValue("NONE", "None");
    public final static EnumKosherTypeValue DAIRY       = new EnumKosherTypeValue("DAIRY","Dairy");
	public final static EnumKosherTypeValue PARVE       = new EnumKosherTypeValue("PARVE","Parve");
	public final static EnumKosherTypeValue FISH        = new EnumKosherTypeValue("FISH","Fish");
	public final static EnumKosherTypeValue MEAT        = new EnumKosherTypeValue("MEAT","Meat");
	public final static EnumKosherTypeValue DAIRY_EQ    = new EnumKosherTypeValue("DAIRY_EQ","Dairy Equipment");
	public final static EnumKosherTypeValue GLATT       = new EnumKosherTypeValue("GLATT","Glatt");
	
	private final String code;
    private final String name;

	private EnumKosherTypeValue(String code, String name) {
		this.code = code;
        this.name = name;
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

	public boolean equals(Object o) {
		if (o instanceof EnumKosherTypeValue) {
			return this.code.equals(((EnumKosherTypeValue)o).getCode());
		}
		return false;
	}
    
    private final static List<EnumKosherTypeValue> types = new ArrayList<EnumKosherTypeValue>();
    static {
        types.add(NONE);
        types.add(NOT_KOSHER);
        types.add(DAIRY);
        types.add(PARVE);
        types.add(FISH);
        types.add(MEAT);
        types.add(DAIRY_EQ);
        types.add(GLATT);
    }

    public static List<EnumKosherTypeValue> getValues() {
        return Collections.unmodifiableList(types);
    }
    
    public static EnumKosherTypeValue getValueForCode(String code) {
        for ( EnumKosherTypeValue value : types ) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
    
	public boolean display(){
		if (this.equals(NONE) || this.equals(NOT_KOSHER)) {
			return false;
		}
		return true;
	}
    
}
