package com.freshdirect.framework.cache;

import java.util.Map;

public interface CacheStatisticsProvider {
    public Map<String, String> getStats();
}
