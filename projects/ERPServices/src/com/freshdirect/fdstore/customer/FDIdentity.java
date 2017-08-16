package com.freshdirect.fdstore.customer;

public class FDIdentity implements java.io.Serializable {
    
	private static final long	serialVersionUID	= -2907395079408342601L;
	
	private final String erpCustomerPK;
	private final String fdCustomerPK;
	private boolean active;
    
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
	
	public boolean isActive() {
		return this.active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	public String toString() {
		return "FDIdentity[" + this.erpCustomerPK + "," + this.fdCustomerPK + "]";
	}

}
