/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.bapi;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public interface BapiCreateCustomer extends BapiFunctionI {

	public static interface CopyReference {
		public String getSalesOrg();
		public String getDistrChan();
		public String getDivision();
		public String getRefCustomer();
	}

	public void setCopyReference(CopyReference copyReference);

	public void setFirstName(String firstName);
	public void setLastName(String lastName);
	public void setStreet(String street);
	public void setPostalCode(String zip);
	public void setCity(String city);
	public void setRegion(String region);
	public void setCountry(String country);

	public String getCustomerNo();

}
