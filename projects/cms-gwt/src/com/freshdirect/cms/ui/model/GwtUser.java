package com.freshdirect.cms.ui.model;

import java.io.Serializable;

/**
 * This is a duplicate of CmsUser, but needed separately because of the GWT serialization requirements.
 * 
 * @author zsombor
 *
 */
public class GwtUser implements Serializable, Cloneable {

    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String name;

    private boolean      allowedToWrite;

    private boolean      admin;

    public GwtUser() {
        
    }
    
    public GwtUser(String name, boolean allowedToWrite, boolean admin) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        this.name = name;
        this.allowedToWrite = allowedToWrite;
        this.admin = admin;
    }

    public String getName() {
        return this.name;
    }

    public boolean isAllowedToWrite() {
        return allowedToWrite;
    }

    public boolean isAdmin() {
        return admin;
    }
    
    public boolean isPublishAllowed() {
        return isAllowedToWrite();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof GwtUser) ? name.equals(((GwtUser) obj).name) : false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

}
