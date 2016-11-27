package com.freshdirect.framework.util;

import java.io.Serializable;

/**
 * This is a type safe basic reference objects, which can be serialized, however the original Model object is not a serializable object.
 * @author zsombor
 *
 * @param <X>
 * @param <Y>
 */
public abstract class Reference<X,Y extends Serializable> implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    transient X model;
    private Y key;
    
    
    public Reference(X model) {
        this.model = model;
        this.key = model != null ? getKey(model) : null;
    }
    
    protected abstract Y getKey(X model);
    
    protected abstract X lookup(Y key);
    
    
    public final X get() {
        if (model == null && key != null) {
            model = lookup(key);
        }
        return model;
    }
    
    public final void set(X model) {
        this.model = model;
        this.key = model != null ? getKey(model) : null;
    }
    

}
