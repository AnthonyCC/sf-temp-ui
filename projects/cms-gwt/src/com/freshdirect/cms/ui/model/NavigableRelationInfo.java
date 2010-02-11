package com.freshdirect.cms.ui.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class NavigableRelationInfo implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    String contentType;
    
    Map<String, String> navigableAttributeName = new HashMap<String, String>();

    
    public NavigableRelationInfo() {
    }
    
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
    public String getContentType() {
        return contentType;
    }
    
    public Collection<String> getNavigableTypes() {
        return navigableAttributeName.keySet();
    }
    
    public String getNavigableAttributeName(String type) {
        return navigableAttributeName.get(type);
    }

    public void addNavigableAttribute(String type, String attributeName) {
        navigableAttributeName.put(type, attributeName);
    }
    
}
