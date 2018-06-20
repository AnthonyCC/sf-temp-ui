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
public class EnumComplaintLineMethod implements java.io.Serializable {

    
	private static final long serialVersionUID = -1753503997775340011L;
	public final static EnumComplaintLineMethod STORE_CREDIT	= new EnumComplaintLineMethod(0, "FDC", "Store Credit");
    public final static EnumComplaintLineMethod CASH_BACK		= new EnumComplaintLineMethod(1, "CSH", "Cash Back");

    private EnumComplaintLineMethod(int id, String statusCode, String name) {
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
    @JsonCreator
	public static EnumComplaintLineMethod getComplaintLineMethod(@JsonProperty("statusCode") String code) {
		if ( "FDC".equalsIgnoreCase(code) ) {
			return STORE_CREDIT;
		} else if ( "CSH".equalsIgnoreCase(code) ) {
			return CASH_BACK;
		} else
			return null;
	}

	public boolean equals(Object m) {
		if (m instanceof EnumComplaintLineMethod) {
			return this.id == ((EnumComplaintLineMethod)m).id;
		}
		return false;
	}
	
	public String toString(){
		return this.statusCode;
	}

    private int id;
    private String statusCode;
    private String name;

} // class EnumComplaintLineMethod
