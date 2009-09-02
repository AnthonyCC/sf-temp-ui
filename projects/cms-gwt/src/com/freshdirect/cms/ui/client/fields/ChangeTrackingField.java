package com.freshdirect.cms.ui.client.fields;

import java.io.Serializable;

/**
 * This interface is used by custom components to report that their internal value is changed or not. 
 * @author zsombor
 *
 */
public interface ChangeTrackingField {

    boolean isFieldValueChanged();
    
    Serializable getChangedValue();
    
}
