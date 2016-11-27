package com.freshdirect.mobileapi.util;

/**
 * Defines a interface for a specific type of object T, determining if the oject should be filtered or not. 
 * @author fgarcia
 *
 * @param <T>
 */
public interface Filter<T> {
    /**
     * The implementation decides if the object should be kept on the list returning <code>true</code>, <code>false</code> otherwise
     * @param object
     * @return
     */
    public boolean isFiltered(T object);
}
