/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.erp; 
 
/**
 * Type-safe enumeration for approval statuses.
 *
 * @version $Revision$
 * @author $Author$
 */
public class EnumApprovalStatus implements java.io.Serializable {

    public final static EnumApprovalStatus LOADING  = new EnumApprovalStatus(-1, "L", "Loading");
	public final static EnumApprovalStatus NEW      = new EnumApprovalStatus( 0, "N", "New");
	public final static EnumApprovalStatus REJECTED = new EnumApprovalStatus( 1, "R", "Rejected");
	public final static EnumApprovalStatus APPROVED = new EnumApprovalStatus( 2, "A", "Approved");
	public final static EnumApprovalStatus PROMOTED = new EnumApprovalStatus( 3, "P", "Promoted");

	protected final int id;
	private final String statusCode;
	private final String displayName;

	private EnumApprovalStatus(int id, String statusCode, String displayName) {
		this.id = id;
		this.statusCode = statusCode;
		this.displayName = displayName;
	}

	public String getStatusCode() {
		return this.statusCode;
	}
    
    public String getDisplayName() {
        return this.displayName;
    }
	
	public String toString() {
		return this.displayName;
	}

	public boolean equals(Object o) {
		if (o instanceof EnumApprovalStatus) {
			return this.id == ((EnumApprovalStatus)o).id;
		}
		return false;
	}

}