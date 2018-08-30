package com.freshdirect.common.pricing; 

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
 
/**
 * 
 * @author ksriram
 *
 */
public class EnumTaxationType extends Enum implements java.io.Serializable {

	private static final long serialVersionUID = 5260939614258808057L;
	public final static EnumTaxationType TAX_AFTER_ALL_DISCOUNTS = new EnumTaxationType("0","ZEC2","Apply tax on the final price of line item, after all discounts");;
	public final static EnumTaxationType TAX_AFTER_INTERNAL_DISCOUNTS = new EnumTaxationType("1","ZEC1","Apply tax on the price of line item, after giving only internal discounts");
//	public final static EnumTaxationType TAX_AFTER_EXTERNAL_DISCOUNTS = new EnumTaxationType("2","AFT_EXT_DISC", "Apply tax on the price of line item, after giving only external discounts");
//	public final static EnumTaxationType TAX_ON_ORIGINAL_PRICE = new EnumTaxationType("3","ON_ORIG_PRICE" ,"Apply tax on the original price of line item");


	private final String code;
	private final String description;
	
	public EnumTaxationType(String name, String code, String description) {
		super(name);
		this.code = code;
		this.description = description;
	}
	
	@JsonCreator
	public static EnumTaxationType getEnum(String name) {
		return (EnumTaxationType) getEnum(EnumTaxationType.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumTaxationType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumTaxationType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumTaxationType.class);
	}

	public String getDescription() {
		return description;
	}

	public String getCode() {
		return code;
	}
	@JsonValue
	public String toString() {
		return super.getName();
	}

}