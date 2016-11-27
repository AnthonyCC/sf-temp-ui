package com.freshdirect.erp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.freshdirect.enums.EnumModel;
import com.freshdirect.erp.ejb.EnumFeaturedHeaderTypeDAO;

public class EnumFeaturedHeaderType extends EnumModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6133505737616585293L;
	
	private static Map enums = null;
	/*public static final EnumFeaturedHeaderType BEST_BET = new EnumFeaturedHeaderType(
			"BEST_BET", "001","Best Bet"); 
	public static final EnumFeaturedHeaderType PEAK_SEASON = new EnumFeaturedHeaderType(
			"PEAK_SEASON","002", "Peak Season"); 
	public static final EnumFeaturedHeaderType NUTRITIONISTS_CHOICE = new EnumFeaturedHeaderType(
			"NUTRITIONISTS_CHOICE", "003","Nutritionist's Choice"); 
	public static final EnumFeaturedHeaderType HIDDEN_GEM = new EnumFeaturedHeaderType(
			"HIDDEN_GEM","004", "Hidden Gem");
	public static final EnumFeaturedHeaderType BACK_IN_SEASON = new EnumFeaturedHeaderType(
			"BACK_IN_SEASON", "005","Back In Season"); 
	public static final EnumFeaturedHeaderType FLASH_SALE = new EnumFeaturedHeaderType(
			"FLASH_SALE","006", "Flash Sale"); 
	public static final EnumFeaturedHeaderType USQ_BEST_BET = new EnumFeaturedHeaderType(
			"USQ_BEST_BET", "007","Union Square Best Bet"); 
	public static final EnumFeaturedHeaderType CHEFS_CHOICE = new EnumFeaturedHeaderType(
			"CHEFS_CHOICE","008", "Chef's Choice");
	public static final EnumFeaturedHeaderType FD_EXCLUSIVE = new EnumFeaturedHeaderType(
			"FD_EXCLUSIVE", "009","FD Exclusive"); 
	public static final EnumFeaturedHeaderType WOW_DEAL = new EnumFeaturedHeaderType(
			"WOW_DEAL","010", "Wow! Deal"); 
	public static final EnumFeaturedHeaderType GREAT_GIFT = new EnumFeaturedHeaderType(
			"GREAT_GIFT", "011","Great Gift"); 
	public static final EnumFeaturedHeaderType LIMITED_QTY = new EnumFeaturedHeaderType(
			"LIMITED_QTY","012", "Limited Quantities");
	public static final EnumFeaturedHeaderType NEW = new EnumFeaturedHeaderType(
			"NEW","013", "New");*/


	public EnumFeaturedHeaderType(String code, String name, String description) {
		super(code,name,description);
	}

	public static EnumFeaturedHeaderType getEnum(String name) {
		loadEnums();
		return (EnumFeaturedHeaderType) enums.get(name);
	}
	
	public static EnumFeaturedHeaderType getEnum(Integer code){
		loadEnums();
		Iterator iterator = enums.keySet().iterator();
		while(iterator.hasNext()){			
			EnumFeaturedHeaderType enumFeaturedHeaderType=(EnumFeaturedHeaderType)enums.get(iterator.next());
			if(Integer.valueOf(enumFeaturedHeaderType.getCode()).equals(code)){
				return enumFeaturedHeaderType;
			}
		}
		return null;
	}
	
	private static void loadEnums() {
		if (null==enums) {
			enums = new HashMap();
			List lst = loadEnums(EnumFeaturedHeaderTypeDAO.class);
			for (Iterator i = lst.iterator(); i.hasNext();) {
				EnumFeaturedHeaderType e = (EnumFeaturedHeaderType) i.next();
				enums.put(e.getCode(), e);
			}
		}
	}
	
}
