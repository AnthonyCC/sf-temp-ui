/*
 * Created on Nov 15, 2004
 *
 */
package com.freshdirect.cms.application;

import java.security.Principal;

/**
 * Simple user identity.
 * 
 * @see java.security.Principal
 */
public interface UserI extends Principal {

	
	public boolean isAllowedToWrite();
	
}
