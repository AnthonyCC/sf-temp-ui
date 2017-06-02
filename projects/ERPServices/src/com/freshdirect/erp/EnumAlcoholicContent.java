/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.erp; 
 
/**
 * Type-safe enumeration for alcoholic content.
 *
 * @version $Revision$
 * @author $Author$
 */
public class EnumAlcoholicContent implements java.io.Serializable {

	public final static EnumAlcoholicContent NONE   = new EnumAlcoholicContent("",  "Non-alcoholic");
	public final static EnumAlcoholicContent BEER   = new EnumAlcoholicContent("B", "Beer");
	public final static EnumAlcoholicContent BC_WINE   = new EnumAlcoholicContent("W", "BC_Wine");
	public final static EnumAlcoholicContent USQ_WINE   = new EnumAlcoholicContent("U", "USQ_Wine");
	public final static EnumAlcoholicContent FD_WINE   = new EnumAlcoholicContent("V", "FD_Wine");


	private final String code;
	private final String name;
    
	public static boolean isBeer(String code) {
		return (BEER.getCode().equalsIgnoreCase(code));
	}
	
	public static boolean isBeer(EnumAlcoholicContent alcoholicContent) {
		if(alcoholicContent==null) return false;
		return isBeer(alcoholicContent.getCode());
	}
	public static boolean isWineOrSpirit(String code) {
		if(BC_WINE.getCode().equals(code)||
	 			   USQ_WINE.getCode().equals(code)||		
	 			   FD_WINE.getCode().equals(code)
	 					)
	 				return true;
	 		else 
	 				return false;
    }
	public static boolean isWineOrSpirit(EnumAlcoholicContent alcoholicContent) {
		if(alcoholicContent==null) return false;
		return isWineOrSpirit(alcoholicContent.getCode());
	}
	
	public static boolean isAlcoholic(String code) {
		return isBeer(code)||isWineOrSpirit(code);
	}
	
	public static boolean isAlcoholic(EnumAlcoholicContent alcoholicContent) {
		return isBeer(alcoholicContent)||isWineOrSpirit(alcoholicContent);
	}
	
    public static EnumAlcoholicContent getAlcoholicContent(String code) {
        if (isBeer(code))
            return BEER;
        else if (BC_WINE.getCode().equalsIgnoreCase(code))
            return BC_WINE;
        else if (USQ_WINE.getCode().equalsIgnoreCase(code))
            return USQ_WINE;
        else if (FD_WINE.getCode().equalsIgnoreCase(code))
            return FD_WINE;
        else
            return NONE;
    }

	private EnumAlcoholicContent(String code, String name) {
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
		if (o instanceof EnumAlcoholicContent) {
			return this.code.equals(((EnumAlcoholicContent)o).code);
		}
		return false;
	}

}
