package com.freshdirect.fdstore.promotion;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumPromoStoreType extends Enum {
	
	public static final EnumPromoStoreType FD = new EnumPromoStoreType("FreshDirect","Fresh Direct");
	public static final EnumPromoStoreType FDX = new EnumPromoStoreType("FDX","fdx");
	
	private final String description;
	
	public EnumPromoStoreType(String name, String description){
		super(name);
		this.description = description;
	}
	
	public static EnumPromoStoreType getEnum(String name){
		return (EnumPromoStoreType) getEnum(EnumPromoStoreType.class, name);
	}
	
	public static Map EnumPromoStoreType() {
		return getEnumMap(EnumPromoStoreType.class);
	}
	
	public static List getEnumList(){
		return getEnumList(EnumPromoStoreType.class);
	}
	
	public static Iterator iterator(){
		return iterator(EnumPromoStoreType.class);
	}

}
