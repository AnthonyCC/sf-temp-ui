/*
 * Created on Jan 7, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.freshdirect.cms.application;

/**
 * Simple implementation of {@link com.freshdirect.cms.application.UserI}.
 */
public class CmsUser implements UserI {

	private final String name;

	private boolean allowedToWrite; 

	private boolean admin; 
	
	public CmsUser(String name, boolean allowedToWrite, boolean admin) {
		this.name = name;
		this.allowedToWrite = allowedToWrite;
		this.admin = admin;
	}
	/**
	 * @deprecated 
	 * @param name
	 */
	public CmsUser(String name) {
		this.name = name;
		this.allowedToWrite = true;
	}

	/* (non-Javadoc)
	 * @see java.security.Principal#getName()
	 */
	public String getName() {
		return this.name;
	}

	public boolean isAllowedToWrite() {
		return allowedToWrite;
	}
	
	public boolean isAdmin() {
		return admin;
	}
}
