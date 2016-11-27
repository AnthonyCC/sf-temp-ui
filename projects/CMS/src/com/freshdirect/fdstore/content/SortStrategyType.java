package com.freshdirect.fdstore.content;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;

public enum SortStrategyType {
	CUSTOMER_RATING, EXPERT_RATING, NAME, POPULARITY, PRICE, SALE, SUSTAINABILITY_RATING,
	DEPARTMENT, E_COUPON_DOLLAR_DISCOUNT, E_COUPON_EXPIRATION_DATE, E_COUPON_PERCENT_DISCOUNT, E_COUPON_POPULARITY, E_COUPON_START_DATE, RECENCY,
	SEARCH_RELEVANCY;
	
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
