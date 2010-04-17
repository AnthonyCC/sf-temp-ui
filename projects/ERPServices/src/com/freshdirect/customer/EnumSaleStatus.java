package com.freshdirect.customer;

import java.util.*;

/**
 * Type-safe enumeration for order statuses.
 */

public class EnumSaleStatus implements java.io.Serializable {

	private static final long	serialVersionUID	= -5157276834260761792L;

	/** Maps EnumSaleStatus status codes to the proper EnumSaleStatus */
	private final static Map<String,EnumSaleStatus> STATUSCODE_MAP = new HashMap<String,EnumSaleStatus>();

	private static int idCounter = 0;

	/** New order, not in SAP yet, cannot be changed */
	public final static EnumSaleStatus					NEW							= new EnumSaleStatus( "NEW", "Processing", "Processing", true );

	/** Modified order (change requested), not in SAP yet */
	public final static EnumSaleStatus					MODIFIED					= new EnumSaleStatus( "MOD", "Modified", "Processing", true );

	/** Modified order (cancel requested), not in SAP yet */
	public final static EnumSaleStatus					MODIFIED_CANCELED			= new EnumSaleStatus( "MOC", "Modified Canceled", "Processing", true );

	/** Submitted order, in SAP, can be changed */
	public final static EnumSaleStatus					SUBMITTED					= new EnumSaleStatus( "SUB", "Submitted", "Submitted", true );

	/** Order was canceled by customer */
	public final static EnumSaleStatus					CANCELED					= new EnumSaleStatus( "CAN", "Cancelled", "Cancelled", false );

	/** Order in process, after cut-off time, cannot be change */
	public final static EnumSaleStatus					INPROCESS					= new EnumSaleStatus( "PRC", "In process", "In process", true );

	/** Order en-route to customer */
	public final static EnumSaleStatus					ENROUTE						= new EnumSaleStatus( "ENR", "En-route", "En-route", true );

	/** Order is pending redelivery */
	public final static EnumSaleStatus					PENDING						= new EnumSaleStatus( "PEN", "Pending redelivery", "Pending redelivery", true );

	public final static EnumSaleStatus					RETURNED					= new EnumSaleStatus( "RET", "Returned", "Returned", false );

	public final static EnumSaleStatus					AUTHORIZED					= new EnumSaleStatus( "AUT", "Authorized", "Submitted", true );

	public final static EnumSaleStatus					AUTHORIZATION_FAILED		= new EnumSaleStatus( "AUF", "Authorization Failed", "Contact Customer Service", true );

	public final static EnumSaleStatus					NOT_SUBMITTED				= new EnumSaleStatus( "NSM", "Not Submitted", "Processing", true );
	public final static EnumSaleStatus					PAYMENT_PENDING				= new EnumSaleStatus( "PPG", "Payment pending", "Delivered", false );
	public final static EnumSaleStatus					SETTLED						= new EnumSaleStatus( "STL", "Settled", "Delivered", false );
	public final static EnumSaleStatus					SETTLEMENT_FAILED			= new EnumSaleStatus( "STF", "Settlement failed", "Contact Customer Service", false );
	public final static EnumSaleStatus					CHARGEBACK					= new EnumSaleStatus( "CBK", "Chargeback", "Contact Customer Service", true );
	public final static EnumSaleStatus					LOCKED						= new EnumSaleStatus( "LOC", "Locked", "In process", true );
	public final static EnumSaleStatus					AVS_EXCEPTION				= new EnumSaleStatus( "AVE", "AVS Exception", "Submitted", true );
	public final static EnumSaleStatus					CAPTURE_PENDING				= new EnumSaleStatus( "CPG", "Capture Pending", "Delivered", false );
	public final static EnumSaleStatus					SETTLED_RETURNED			= new EnumSaleStatus( "STR", "Settled Returned", "Delivered", false );
	public final static EnumSaleStatus					REFUSED_ORDER				= new EnumSaleStatus( "REF", "Refused Delivery", "Contact Customer Service", true );
	public final static EnumSaleStatus					REDELIVERY					= new EnumSaleStatus( "RED", "Scheduled for Redelivery", "En-route", true );
	public final static EnumSaleStatus					INPROCESS_NO_AUTHORIZATION	= new EnumSaleStatus( "PNA", "In process but no authorization", "In process", true );
	public final static EnumSaleStatus					EMAIL_PENDING				= new EnumSaleStatus( "EPG", "Email Pending", "In Process", true );
	public final static EnumSaleStatus					REG_PENDING					= new EnumSaleStatus( "RPG", "Registration Pending", "In Process", true );
	public final static EnumSaleStatus					POST_AUTH_PENDING			= new EnumSaleStatus( "POG", "Post Auth Pending", "Completed", true );
	public final static EnumSaleStatus					SETTLEMENT_PENDING			= new EnumSaleStatus( "STP", "Settlement Pending", "In Process", true );

	private final int id;
	private final String statusCode;
	private final String name;
	private final String displayName;
	private final boolean pending;

	private EnumSaleStatus(String statusCode, String name, String displayName, boolean pending) {
		this.id = idCounter++;
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

	public static EnumSaleStatus getSaleStatus(String statusCode) {
		return STATUSCODE_MAP.get( statusCode.toUpperCase() );
	}
	
	public boolean isCapturable(){
		return this.equals(ENROUTE) || this.equals(CAPTURE_PENDING) || this.equals(PENDING) || this.equals(REDELIVERY);
	}
	
	@Override
	public String toString() {
		return this.name;
	}

	@Override
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
