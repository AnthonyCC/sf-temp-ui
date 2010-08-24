/*
 * $Workfile:EnumKosherSymbolValue.java$
 *
 * $Date:8/19/2003 8:25:12 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.content.nutrition;

import java.util.*;

/**
 * Type-safe enumeration for attributes' data types.
 *
 * @version $Revision:5$
 * @author $Author:Mike Rose$
 */
public class EnumKosherSymbolValue implements NutritionValueEnum {
    
	private static final long	serialVersionUID	= 5993438447662242875L;
	
	public final static EnumKosherSymbolValue NONE          = new EnumKosherSymbolValue("NONE","NONE", 0);
    public final static EnumKosherSymbolValue K             = new EnumKosherSymbolValue("K","K", 5);
	public final static EnumKosherSymbolValue OU            = new EnumKosherSymbolValue("OU","OU", 1);
	public final static EnumKosherSymbolValue OK            = new EnumKosherSymbolValue("OK","OK", 2);
	public final static EnumKosherSymbolValue KOF_K         = new EnumKosherSymbolValue("KOF_K","KOF_K", 3);
	public final static EnumKosherSymbolValue BOX_K         = new EnumKosherSymbolValue("BOX_K","BOX_K", 9);
	public final static EnumKosherSymbolValue STAR_K        = new EnumKosherSymbolValue("STAR_K","STAR_K", 4);
	public final static EnumKosherSymbolValue HALFMOON_K    = new EnumKosherSymbolValue("HALFMOON_K","HALFMOON_K", 6);
	public final static EnumKosherSymbolValue TRIANGLE_K    = new EnumKosherSymbolValue("TRIANGLE_K","TRIANGLE_K", 7);
	public final static EnumKosherSymbolValue TABLET_K      = new EnumKosherSymbolValue("TABLET_K","TABLET_K", 10);
	public final static EnumKosherSymbolValue SHIELD_K      = new EnumKosherSymbolValue("SHIELD_K","SHIELD_K", 14);
	public final static EnumKosherSymbolValue KSA           = new EnumKosherSymbolValue("KSA","KSA", 12);
	public final static EnumKosherSymbolValue KAJ           = new EnumKosherSymbolValue("KAJ","KAJ", 15);
	public final static EnumKosherSymbolValue KVH           = new EnumKosherSymbolValue("KVH","KVH", 8);
	public final static EnumKosherSymbolValue CRC           = new EnumKosherSymbolValue("CRC","CRC", 13);
	public final static EnumKosherSymbolValue RCC_K         = new EnumKosherSymbolValue("RCC_K","RCC_K", 20);
	public final static EnumKosherSymbolValue WK            = new EnumKosherSymbolValue("WK","WK", 17);
	public final static EnumKosherSymbolValue STOPSIGN_KF   = new EnumKosherSymbolValue("STOPSIGN_KF","STOPSIGN_KF", 16);
    public final static EnumKosherSymbolValue SCROLL_K      = new EnumKosherSymbolValue("SCROLL_K","SCROLL_K", 11);
    public final static EnumKosherSymbolValue OV            = new EnumKosherSymbolValue("OV","OV", 26);
    public final static EnumKosherSymbolValue TEXAS_K       = new EnumKosherSymbolValue("TEXAS_K","TEXAS_K", 28);
    public final static EnumKosherSymbolValue NK            = new EnumKosherSymbolValue("NK","NK", 25);
    public final static EnumKosherSymbolValue AKC           = new EnumKosherSymbolValue("AKC","AKC", 19);
    public final static EnumKosherSymbolValue MK            = new EnumKosherSymbolValue("MK","MK", 24);
    public final static EnumKosherSymbolValue VKS           = new EnumKosherSymbolValue("VKS","VKS", 29);
    public final static EnumKosherSymbolValue COR           = new EnumKosherSymbolValue("COR","COR", 22);
    public final static EnumKosherSymbolValue DK            = new EnumKosherSymbolValue("DK","DK", 23);
    public final static EnumKosherSymbolValue RIBBON_K      = new EnumKosherSymbolValue("RIBBON_K","RIBBON_K", 21);
    public final static EnumKosherSymbolValue STAR_D        = new EnumKosherSymbolValue("STAR_D","STAR_D", 27);
    public final static EnumKosherSymbolValue QK            = new EnumKosherSymbolValue("QK","QK",30);
    public final static EnumKosherSymbolValue K_ORC         = new EnumKosherSymbolValue("K_ORC","K_ORC",31);
	
	private final String code;
    private final String name;
    private final int priority;

	private EnumKosherSymbolValue(String code, String name, int priority) {
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
    
    public int getPriority() {
        return this.priority;
    }
	
	public String toString() {
		return this.name;
	}

	public boolean equals(Object o) {
		if (o instanceof EnumKosherSymbolValue) {
			return this.code.equals(((EnumKosherSymbolValue)o).getCode());
		}
		return false;
	}
    
    private final static List<EnumKosherSymbolValue> symbols = new ArrayList<EnumKosherSymbolValue>();
    static {
    	symbols.add(NONE);
        symbols.add(K);
        symbols.add(OU);
        symbols.add(OK);
        symbols.add(KOF_K);
        symbols.add(BOX_K);
        symbols.add(STAR_K);
        symbols.add(HALFMOON_K);
        symbols.add(TRIANGLE_K);
        symbols.add(TABLET_K);
        symbols.add(SHIELD_K);
        symbols.add(KSA);
        symbols.add(KAJ);
        symbols.add(KVH);
        symbols.add(CRC);
        symbols.add(RCC_K);
        symbols.add(WK);
        symbols.add(STOPSIGN_KF);
        symbols.add(SCROLL_K);
        symbols.add(OV);
        symbols.add(TEXAS_K);
        symbols.add(NK);
        symbols.add(AKC);
        symbols.add(MK);
        symbols.add(VKS);
        symbols.add(COR);
        symbols.add(DK);
        symbols.add(RIBBON_K);
        symbols.add(STAR_D);
        symbols.add(QK);
        symbols.add(K_ORC);
    }

    public static List<EnumKosherSymbolValue> getValues() {
        return Collections.unmodifiableList(symbols);
    }
    
    public static EnumKosherSymbolValue getValueForCode(String code) {
        for ( EnumKosherSymbolValue value : symbols ) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
    
    public boolean display(){
    	if (this.equals(NONE)) {
    		return false;
    	}
    	return true;
    	
    }
    
}
