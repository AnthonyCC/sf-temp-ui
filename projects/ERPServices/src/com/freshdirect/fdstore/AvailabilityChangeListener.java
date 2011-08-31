package com.freshdirect.fdstore;

public interface AvailabilityChangeListener {

    public void availabilityInfoReceived(int version);
    
    public void cacheReloaded();
    
}
