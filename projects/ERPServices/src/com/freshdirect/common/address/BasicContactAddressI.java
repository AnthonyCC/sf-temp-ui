/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.common.address;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public interface BasicContactAddressI extends BasicAddressI {

	public String getFirstName();
	public String getLastName();
	public PhoneNumber getPhone();
	public String getCustomerId();
	
	
}

