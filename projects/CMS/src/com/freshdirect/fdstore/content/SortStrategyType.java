package com.freshdirect.fdstore.content;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;

public enum SortStrategyType {
	CUSTOMER_RATING, EXPERT_RATING, NAME, POPULARITY, PRICE, SALE, SUSTAINABILITY_RATING;
	
	private static final Logger LOGGER = LoggerFactory.getInstance(SortStrategyType.class);

	public static SortStrategyType toEnum(String type){
		try {
			return valueOf(type);
		
		} catch (IllegalArgumentException e){
			LOGGER.error("CMS configuration error, type "+type+" does not exist in SortStrategyType enum", e);
			return null;
		}
	}
}
