/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001-2002 FreshDirect, Inc.
 *
 */
package com.freshdirect.framework.webapp;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class ActionError {
    
    public final static String GENERIC = "genericError";
    
    private final String type;
    private final String description;
    
    public ActionError(String description) {
    	this(GENERIC, description);
    }
    
	public ActionError(String type, String description) {
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
    	return "ActionError["+type+",'"+description+"']";
    }
    

}
