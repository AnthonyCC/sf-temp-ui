package com.freshdirect.affiliate;

import org.apache.log4j.Category;
import com.freshdirect.framework.util.log.LoggerFactory;

/** Agencies which promote FD products on 3rd party pages **/
public enum ExternalAgency {
	FOODILY, STOREPOWER, IBOTTA, SMARTCOMMERCE, ZIPONGO;
	
	private static final Category LOGGER = LoggerFactory.getInstance(ExternalAgency.class);

	public static ExternalAgency safeValueOf(String externalAgencyStr){
		if (externalAgencyStr!=null){
			try {
				return ExternalAgency.valueOf(externalAgencyStr);
			} catch (Exception e){
				LOGGER.error("ExternalAgency does not exist: " + externalAgencyStr, e);
			}
		}
		return null;
	}
}

