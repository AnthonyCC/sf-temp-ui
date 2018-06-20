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
public class EnumComplaintStatus implements java.io.Serializable {


	private static final long serialVersionUID = 4611799119177790539L;
	public final static EnumComplaintStatus PENDING			= new EnumComplaintStatus(0, "PEN", "Pending");
    public final static EnumComplaintStatus APPROVED		= new EnumComplaintStatus(1, "APP", "Approved");
    public final static EnumComplaintStatus REJECTED		= new EnumComplaintStatus(2, "REJ", "Rejected");

    private EnumComplaintStatus(int id, String statusCode, String name) {
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
	public static EnumComplaintStatus getComplaintStatus(@JsonProperty("statusCode") String code) {
		if ( "APP".equalsIgnoreCase(code) ) {
			return APPROVED;
		} else if ( "REJ".equalsIgnoreCase(code) ) {
			return REJECTED;
		} else {
			return PENDING;
        }
	}

	public boolean equals(EnumComplaintStatus s) {
		return s.getId() == this.id;
	}
	
	public String toString(){
		return this.statusCode;
	}

    private int id;
    private String statusCode;
    private String name;

} // class EnumComplaintStatus
