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

	public CmsUser(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see java.security.Principal#getName()
	 */
	public String getName() {
		return this.name;
	}

}
