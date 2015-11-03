package com.freshdirect.fdstore;

import java.util.HashMap;
import java.util.Map;

/**
 * Type-safe enumeration for availability statuses.
 */
public enum EnumAvailabilityStatus implements java.io.Serializable {

    AVAILABLE        (0, "AVAL", "Available"),
    DISCONTINUED     (1, "DISC", "Discontinued"),
    OUT_OF_SEASON    (2, "SEAS", "Out Of Season"),
    TEMP_UNAV        (3, "UNAV", "Temporarily Unavailable");
    

	private static final long	serialVersionUID	= -1531192923805339870L;

	private final static Map<String, EnumAvailabilityStatus> STATUSCODE_MAP = new HashMap<String, EnumAvailabilityStatus>();
	
	static {
	    for (EnumAvailabilityStatus e : values()) {
	        STATUSCODE_MAP.put(e.statusCode, e);
	    }
	}
    
    public int getId() {
		return id;
	}

	protected final int id;
    private final String statusCode;
    private final String shortDescription;
    
    private EnumAvailabilityStatus(int id, String statusCode, String shortDescription) {
        this.id = id;
        this.statusCode = statusCode;
        this.shortDescription = shortDescription;
    }
    
    public String getStatusCode() {
        return this.statusCode;
    }
    
    public String getShortDescription() {
        return this.shortDescription;
    }
    
    public static EnumAvailabilityStatus getEnumByStatusCode(String statusCode){
    	return statusCode == null ? null : STATUSCODE_MAP.get( statusCode.toUpperCase() );
    }
    
    public String toString() {
        return this.statusCode + " : " + this.shortDescription;
    }
    
}