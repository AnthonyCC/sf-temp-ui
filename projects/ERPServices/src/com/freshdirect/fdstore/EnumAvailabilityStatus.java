/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore;

import java.util.HashMap;
import java.util.Map;

/**
 * Type-safe enumeration for availability statuses.
 *
 * @version $Revision$
 * @author $Author$
 */
public class EnumAvailabilityStatus implements java.io.Serializable {
	
	private final static Map STATUSCODE_MAP = new HashMap();
    
    public final static EnumAvailabilityStatus AVAILABLE        = new EnumAvailabilityStatus(0, "AVAL", "Available");
    public final static EnumAvailabilityStatus DISCONTINUED     = new EnumAvailabilityStatus(1, "DISC", "Discontinued");
    public final static EnumAvailabilityStatus OUT_OF_SEASON    = new EnumAvailabilityStatus(2, "SEAS", "Out Of Season");
    public final static EnumAvailabilityStatus TEMP_UNAV        = new EnumAvailabilityStatus(3, "UNAV", "Temporarily Unavailable");
    
    
    protected final int id;
    private final String statusCode;
    private final String shortDescription;
    
    private EnumAvailabilityStatus(int id, String statusCode, String shortDescription) {
        this.id = id;
        this.statusCode = statusCode;
        this.shortDescription = shortDescription;
        
        STATUSCODE_MAP.put( this.statusCode, this );
    }
    
    public String getStatusCode() {
        return this.statusCode;
    }
    
    public String getShortDescription() {
        return this.shortDescription;
    }
    
    public static EnumAvailabilityStatus getEnumByStatusCode(String statusCode){
    	return statusCode == null ? null : (EnumAvailabilityStatus) STATUSCODE_MAP.get( statusCode.toUpperCase() );
    }
    
    public String toString() {
        return this.statusCode + " : " + this.shortDescription;
    }
    
    public boolean equals(Object o) {
        if (o instanceof EnumAvailabilityStatus) {
            return this.id == ((EnumAvailabilityStatus)o).id;
        }
        return false;
    }
    
}