package com.freshdirect.fdstore.cache;

public interface ExternalSharedCacheMBean extends FDAbstractCacheMBean {

    /**
     * Ensure that the external cache has all the items, which is in the VM.
     * @return
     */
    public int storeDataToExternalCache();
    
    /**
     * Calculate the number of items which are already in the external cache.
     * @return
     */
    public int getAlreadyStoredItemCount();
    
}
