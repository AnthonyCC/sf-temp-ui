package com.freshdirect.erp;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumProductPromotionType extends Enum {

	private static final long serialVersionUID = 3857209226074690728L;

	public static final EnumProductPromotionType PRESIDENTS_PICKS = new EnumProductPromotionType(
			"PRESIDENTS_PICKS", 101,"President's Picks"); // President's Picks
	public static final EnumProductPromotionType NUTRITIONISTS_CHOICE = new EnumProductPromotionType(
			"NUTRITIONISTS_CHOICE",102, "Nutritionist's Choice"); // Nutritionist's Choice
	public static final EnumProductPromotionType PRODUCTS_ASSORTMENTS = new EnumProductPromotionType(
			"PRODUCTS_ASSORTMENTS",201, "Products Assortments"); // Products Assortments(DDPA)

	private final String description;
	private final Integer code;

	public EnumProductPromotionType(String name, Integer code, String description) {
		super(name);
		this.code = code;
		this.description = description;
	}

	public static EnumProductPromotionType getEnum(String name) {
		return (EnumProductPromotionType) getEnum(
				EnumProductPromotionType.class, name);
	}
	
	public static EnumProductPromotionType getEnum(Integer code){
		Iterator iterator = iterator();
		while(iterator.hasNext()){
			EnumProductPromotionType enumProductPromotionType=(EnumProductPromotionType)iterator.next();
			if(enumProductPromotionType.getCode().equals(code)){
				return enumProductPromotionType;
			}
		}
		return null;
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumProductPromotionType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumProductPromotionType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumProductPromotionType.class);
	}

	public String getDescription() {
		return description;
	}
	
	public Integer getCode() {
		return code;
	}

}
