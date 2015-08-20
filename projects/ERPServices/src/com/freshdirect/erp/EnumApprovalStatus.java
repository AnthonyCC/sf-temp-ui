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
 * @author kkanuganti
 */
@SuppressWarnings("javadoc")
public class EnumApprovalStatus implements java.io.Serializable {

	private static final long serialVersionUID = -5419291639876154517L;

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
	
	public static EnumApprovalStatus getApprovalStatus(String code) {
      if (LOADING.getStatusCode().equalsIgnoreCase(code))
      {
          return LOADING;
      }
      else if (NEW.getStatusCode().equalsIgnoreCase(code))
      {
          return NEW;
      }
      else if (REJECTED.getStatusCode().equalsIgnoreCase(code))
      {
          return REJECTED;
      }
      else if (APPROVED.getStatusCode().equalsIgnoreCase(code))
      {
          return APPROVED;
      }
      else if (PROMOTED.getStatusCode().equalsIgnoreCase(code))
      {
          return PROMOTED;
      }
      return null;
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