/*
 * EnumComplaintLineType.java
 *
 * Created on November 15, 2001, 10:38 PM
 */

package com.freshdirect.customer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author  jmccarter
 * @version
 */
public class EnumComplaintLineType implements java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7178044712465471285L;
	public final static EnumComplaintLineType ORDER_LINE		= new EnumComplaintLineType(0, "ORLN", "Order Line");
    public final static EnumComplaintLineType DEPARTMENT		= new EnumComplaintLineType(1, "DEPT", "Department");
    public final static EnumComplaintLineType MISCELLANEOUS		= new EnumComplaintLineType(2, "MISC", "Miscellaneous");
    public final static EnumComplaintLineType FULL_REFUND		= new EnumComplaintLineType(3, "FULL", "Full Refund");
    public final static EnumComplaintLineType SEVENTY_FIVE_PCT	= new EnumComplaintLineType(4, "75PT", "75% Refund");
    public final static EnumComplaintLineType REFERRAL	        = new EnumComplaintLineType(5, "RAF", "Refer A Friend");

    private EnumComplaintLineType(int id, String statusCode, String name) {
        this.id = id;
        this.statusCode = statusCode;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public String getStatusCode() {
		return this.statusCode;
	}

    public String getName() {
        return this.name;
    }

    private int id;
    private String statusCode;
    private String name;

    @JsonCreator
    public static EnumComplaintLineType getComplaintLineType(@JsonProperty("statusCode") String code) {
    	//System.out.println("Checking code: " + code);
		if ( "ORLN".equalsIgnoreCase(code) ) {
			return ORDER_LINE;
		} else if ( "DEPT".equalsIgnoreCase(code) ) {
			return DEPARTMENT;
		} else if ( "MISC".equalsIgnoreCase(code) ) {
			return MISCELLANEOUS;
		} else if ( "FULL".equalsIgnoreCase(code) ) {
			return FULL_REFUND;
		} else if ( "75PT".equalsIgnoreCase(code) ) {
			return SEVENTY_FIVE_PCT;
		} else if ( "RAF".equalsIgnoreCase(code) ) {
			return REFERRAL;
		} else
			return null;
	}

   	public boolean equals(Object o) {
		if (o instanceof EnumComplaintLineType) {
			return this.id == ((EnumComplaintLineType)o).id;
		}
		return false;
	}
	
	public String toString(){
		return this.statusCode;
	}

} // class EnumComplaintLineType
