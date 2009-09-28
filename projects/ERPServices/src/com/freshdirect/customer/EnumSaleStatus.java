/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.customer;

import java.util.*;

/**
 * Type-safe enumeration for order statuses.
 *
 * @version $Revision$
 * @author $Author$
 */
public class EnumSaleStatus implements java.io.Serializable {

	/** Maps EnumSaleStatus status codes to the proper EnumSaleStatus */
	private final static Map STATUSCODE_MAP = new HashMap();

	/** New order, not in SAP yet, cannot be changed */
	public final static EnumSaleStatus NEW					= new EnumSaleStatus(0, "NEW", "Processing", "Processing", true);

    /** Modified order (change requested), not in SAP yet */
    public final static EnumSaleStatus MODIFIED				= new EnumSaleStatus(1, "MOD", "Modified", "Processing", true);

	/** Modified order (cancel requested), not in SAP yet */
	public final static EnumSaleStatus MODIFIED_CANCELED	= new EnumSaleStatus(2, "MOC", "Modified Canceled", "Processing", true);

	/** Submitted order, in SAP, can be changed */
	public final static EnumSaleStatus SUBMITTED				= new EnumSaleStatus(3, "SUB", "Submitted", "Submitted", true);

	/** Order was canceled by customer */
	public final static EnumSaleStatus CANCELED				= new EnumSaleStatus(4, "CAN", "Cancelled", "Cancelled", false);

	/** Order in process, after cut-off time, cannot be change */
	public final static EnumSaleStatus INPROCESS				= new EnumSaleStatus(5, "PRC", "In process", "In process", true);

	/** Order en-route to customer */
	public final static EnumSaleStatus ENROUTE				= new EnumSaleStatus(6, "ENR", "En-route", "En-route", true);

	/** Order is pending redelivery */
	public final static EnumSaleStatus PENDING				= new EnumSaleStatus(7, "PEN", "Pending redelivery", "Pending redelivery", true);

	public final static EnumSaleStatus RETURNED				= new EnumSaleStatus(9, "RET", "Returned", "Returned", false);

	public final static EnumSaleStatus AUTHORIZED			= new EnumSaleStatus(10, "AUT", "Authorized", "Submitted", true);

	public final static EnumSaleStatus AUTHORIZATION_FAILED= new EnumSaleStatus(11, "AUF", "Authorization Failed", "Contact Customer Service", true);

	public final static EnumSaleStatus NOT_SUBMITTED		= new EnumSaleStatus(12, "NSM", "Not Submitted", "Processing", true);
	public final static EnumSaleStatus PAYMENT_PENDING 		= new EnumSaleStatus(13, "PPG", "Payment pending", "Delivered", false);
	public final static EnumSaleStatus SETTLED 				= new EnumSaleStatus(14, "STL", "Settled", "Delivered", false);
	public final static EnumSaleStatus SETTLEMENT_FAILED 	= new EnumSaleStatus(15, "STF", "Settlement failed", "Contact Customer Service", false);
	public final static EnumSaleStatus CHARGEBACK 			= new EnumSaleStatus(17, "CBK", "Chargeback", "Contact Customer Service", true);
	public final static EnumSaleStatus LOCKED			 	= new EnumSaleStatus(19, "LOC", "Locked", "In process", true);
	public final static EnumSaleStatus AVS_EXCEPTION 		= new EnumSaleStatus(22, "AVE", "AVS Exception", "Submitted", true);
	public final static EnumSaleStatus CAPTURE_PENDING 		= new EnumSaleStatus(23, "CPG", "Capture Pending", "Delivered", false);
	public final static EnumSaleStatus SETTLED_RETURNED		= new EnumSaleStatus(24, "STR", "Settled Returned", "Delivered", false);
	public final static EnumSaleStatus REFUSED_ORDER		= new EnumSaleStatus(25, "REF", "Refused Delivery", "Contact Customer Service", true);
	public final static EnumSaleStatus REDELIVERY 			= new EnumSaleStatus(26, "RED", "Scheduled for Redelivery", "En-route", true);
	public final static EnumSaleStatus INPROCESS_NO_AUTHORIZATION = new EnumSaleStatus(27, "PNA", "In process but no authorization", "In process", true);
	public final static EnumSaleStatus EMAIL_PENDING = new EnumSaleStatus(28, "EPG", "Email Pending", "In Process", true);
	public final static EnumSaleStatus REG_PENDING = new EnumSaleStatus(28, "RPG", "Registration Pending", "In Process", true);
	public final static EnumSaleStatus POST_AUTH_PENDING = new EnumSaleStatus(29, "POG", "Post Auth Pending", "Completed", true); 
	public final static EnumSaleStatus SETTLEMENT_PENDING = new EnumSaleStatus(30, "STP", "Settlement Pending", "In Process", true); 

	private final int id;
	private final String statusCode;
	private final String name;
	private final String displayName;
	private final boolean pending;

	private EnumSaleStatus(int id, String statusCode, String name, String displayName, boolean pending) {
		this.id = id;
		this.statusCode = statusCode;
		this.name = name;
		this.displayName = displayName;
		this.pending = pending;
		
		STATUSCODE_MAP.put( this.statusCode, this );
	}

	public String getStatusCode() {
		return this.statusCode;
	}

    public String getName() {
        return this.name;
    }
    
    public String getDisplayName() {
    	return this.displayName;
    }
    
    public boolean isPending() {
    	return this.pending;
    }

	public static EnumSaleStatus getSaleStatus(String statusCode){
		return (EnumSaleStatus) STATUSCODE_MAP.get( statusCode.toUpperCase() );
	}
	
	public boolean isCapturable(){
		return this.equals(ENROUTE) || this.equals(CAPTURE_PENDING) || this.equals(PENDING) || this.equals(REDELIVERY);
	}
	
	public String toString() {
		return this.name;
	}

	public boolean equals(Object o) {
		if (o instanceof EnumSaleStatus) {
			return this.id == ((EnumSaleStatus)o).id;
		}
		return false;
	}

	public boolean isCanceled() {
		return this.equals(CANCELED) || this.equals(MODIFIED_CANCELED);
	}
	public boolean isReturned() {
		return this.equals(RETURNED);
	}
}
