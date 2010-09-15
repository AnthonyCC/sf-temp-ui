package com.freshdirect.fdstore.cache;

public interface FDAbstractCacheMBean {

    public int getSize();
    
    public int getHitCount();
    
    public void resetHitCount();
    
    public int refreshCache();
}
