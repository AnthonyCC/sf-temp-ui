package com.freshdirect.storeapi.content;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;

public enum ProductFilterGroupType {
	
	SINGLE, MULTI, DROPDOWN, POPUP; //TODO remove DROPDOWN

	private static final Logger LOGGER = LoggerFactory.getInstance(ProductFilterGroupType.class);
	
	public static ProductFilterGroupType toEnum(String type){
		try {
			return valueOf(type);
		
		} catch (IllegalArgumentException e){
			LOGGER.error("CMS configuration error, type "+type+" does not exist in ProductFilterGroupType enum", e);
			return null;
		}
	}

}
