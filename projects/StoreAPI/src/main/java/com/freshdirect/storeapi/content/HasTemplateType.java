package com.freshdirect.storeapi.content;

/**
 * Mark that the node contains a TEMPLATE_TYPE attribute.
 * 
 * @author zsombor
 *
 */
public interface HasTemplateType {
    
    public int getTemplateType(int defaultValue);
    
}
