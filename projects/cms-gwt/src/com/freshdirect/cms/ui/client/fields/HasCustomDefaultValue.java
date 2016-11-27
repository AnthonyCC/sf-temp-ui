package com.freshdirect.cms.ui.client.fields;

/**
 * Interface to specify if the field has a non-null default value. For example an empty list.
 *  
 * @author zsombor
 *
 */
public interface HasCustomDefaultValue<X> {

    X getDefaultValue();
    
}
