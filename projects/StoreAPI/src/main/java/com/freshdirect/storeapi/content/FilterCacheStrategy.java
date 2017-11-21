package com.freshdirect.storeapi.content;

import java.util.List;

public enum FilterCacheStrategy {
	CMS_ONLY, ERPS, ERPS_PRICING_ZONE, NO_CACHING;
	
	public static FilterCacheStrategy getCompoundCacheStrategy (List<FilterCacheStrategy> cacheStrategies){
		FilterCacheStrategy maxLevelCacheStrategy = CMS_ONLY;
		
		for (FilterCacheStrategy cacheStrategy : cacheStrategies){
			if (cacheStrategy.ordinal() > maxLevelCacheStrategy.ordinal()){
				maxLevelCacheStrategy = cacheStrategy;
			}
			if (maxLevelCacheStrategy == NO_CACHING){
				break;
			}
		}
		return maxLevelCacheStrategy;
	}
}
