package com.freshdirect.cms.ui.model.publish;

import java.io.Serializable;

public class GwtValidationError implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    String key;
    String attribute;
    String message;

    public GwtValidationError() {
    }
    
    public GwtValidationError(String key, String attribute, String message) {
        this.key = key;
        this.attribute = attribute;
        this.message = message;
    }

    public String getKey() {
        return key;
    }

    public String getAttribute() {
        return attribute;
    }

    public String getMessage() {
        return message;
    }

    public String getHumanReadable() {
        return "Problem in node '"+key+"' " + (attribute!=null ? " with the "+attribute +" property : " : " : " ) + message; 
    }
}
