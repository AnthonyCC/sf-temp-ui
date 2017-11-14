package com.freshdirect.storeapi.content;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;

public enum ProductFilterType {
	
	AND,
	OR, 
	ALLERGEN,
	BACK_IN_STOCK,
	BRAND,
	CLAIM,
	CUSTOMER_RATING,
	DOMAIN_VALUE,
	EXPERT_RATING,
	FRESHNESS,
	GLUTEN_FREE,
	KOSHER,
	NEW,
	NUTRITION,
	ON_SALE,
	ORGANIC,
	PRICE,
	SUSTAINABILITY_RATING,
	TAG;
	
	private static final Logger LOGGER = LoggerFactory.getInstance(ProductFilterType.class);
	
	public static ProductFilterType toEnum(String type){
		try {
			return valueOf(type);
		
		} catch (IllegalArgumentException e){
			LOGGER.error("CMS configuration error, type "+type+" does not exist in ProductFilterType enum", e);
			return null;
		}
	}

}
