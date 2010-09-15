package com.freshdirect.framework.cache;

import java.util.Map;

public interface StatRecorderCacheMBean {
    
    public String getName();
    
    public String getInternalType();
    
    public int getCacheHit();

    public int getCacheMiss();
    
    public int getCachePut();

    public int getSize();

    public void resetCacheHitStat();

    public void clear();
    
    public Map<String, String> getStats();

}
