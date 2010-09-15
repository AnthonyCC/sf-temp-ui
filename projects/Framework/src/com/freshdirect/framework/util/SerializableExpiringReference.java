package com.freshdirect.framework.util;

import java.io.Serializable;

public abstract class SerializableExpiringReference<X> implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    transient X model;
    long lastRefresh;

    public SerializableExpiringReference(X model) {
        this.model = model;
        this.lastRefresh = System.currentTimeMillis();
    }
    
    
    public X get() {
        if (model == null || System.currentTimeMillis() - lastRefresh > getRefreshPeriod()) {
            model = load();
        }
        return model;
    }


    protected abstract long getRefreshPeriod();

    protected abstract X load();
    

}
