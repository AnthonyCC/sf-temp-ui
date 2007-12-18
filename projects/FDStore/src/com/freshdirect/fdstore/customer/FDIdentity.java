/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore.customer;

public class FDIdentity implements java.io.Serializable {
    
	private final String erpCustomerPK;
	private final String fdCustomerPK;
    
	public FDIdentity(String erpCustomerPK, String fdCustomerPK) {
		this.erpCustomerPK = erpCustomerPK;
		this.fdCustomerPK = fdCustomerPK;
	}
	
	public FDIdentity(String erpCustomerPK) {
		this(erpCustomerPK, null);
	}
    
	public String getErpCustomerPK() {
		return this.erpCustomerPK;
	}
    
	public String getFDCustomerPK() {
		return this.fdCustomerPK;
	}

	public String toString() {
		return "FDIdentity[" + this.erpCustomerPK + "," + this.fdCustomerPK + "]";
	}

}
