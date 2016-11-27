package com.freshdirect.framework.webapp;

public class ActionWarning {
    
    private final String type;
    private final String description;
    
	public ActionWarning(String type, String description) {
        this.type = type;
        this.description = description;
	}

    public String getType() {
        return type;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String toString() {
    	return "ActionWarning["+type+",'"+description+"']";
    }
}
