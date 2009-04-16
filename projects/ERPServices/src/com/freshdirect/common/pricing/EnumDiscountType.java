/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.common.pricing; 
 
/**
 * Type-safe enumeration for promotion types.
 *
 * @version $Revision$
 * @author $Author$
 */
public class EnumDiscountType implements java.io.Serializable {

	public final static EnumDiscountType PERCENT_OFF = new EnumDiscountType(0, "Percentage off");
	public final static EnumDiscountType DOLLAR_OFF = new EnumDiscountType(1, "Dollar off");;
	public final static EnumDiscountType FREE = new EnumDiscountType(2, "Free item");
	public final static EnumDiscountType SAMPLE = new EnumDiscountType(3, "Sample item");
	//public final static EnumDiscountType DCPD = new EnumDiscountType(4, "Sample item");

	protected final int id;
	private final String name;

	private EnumDiscountType(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public int getId() {
		return this.id;
	}

	public static EnumDiscountType getPromotionType(int t) {
		switch (t) {
			case 0:
				return PERCENT_OFF;
			case 1:
				return DOLLAR_OFF;
			case 2:
				return FREE;
			case 3:
				return SAMPLE;
			//case 4:
				//return DCPD;				
			default:
				return null;
		}
	}

	public String toString() {
		return this.name;
	}

	public boolean equals(Object o) {
		if (o instanceof EnumDiscountType) {
			return this.id == ((EnumDiscountType)o).id;
		}
		return false;
	}
	public boolean isSample(){
		if(this.equals(EnumDiscountType.FREE) || this.equals(EnumDiscountType.SAMPLE)){
			return true;
		}
		return false;
	}

}