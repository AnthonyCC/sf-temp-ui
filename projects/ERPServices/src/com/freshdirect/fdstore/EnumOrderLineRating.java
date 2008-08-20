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
public class EnumOrderLineRating implements java.io.Serializable {
	
	private final static Map STATUSCODE_MAP = new HashMap();
    
    public final static EnumOrderLineRating NEVER_RATED        = new EnumOrderLineRating(0, "000", "Never Rated/Rating Expired");
    public final static EnumOrderLineRating TERRIBLE     = new EnumOrderLineRating(1, "001", "Terrible");
    public final static EnumOrderLineRating BELOW_AVG    = new EnumOrderLineRating(2, "002", "Below Average");
    public final static EnumOrderLineRating BELOW_AVG_PLUS        = new EnumOrderLineRating(3, "003", "Inconsistent");
    
    public final static EnumOrderLineRating AVERAGE        = new EnumOrderLineRating(4, "004", "Average ");
    public final static EnumOrderLineRating AVERAGE_PLUS     = new EnumOrderLineRating(5, "005", "Above Average");
    public final static EnumOrderLineRating GOOD    = new EnumOrderLineRating(6, "006", "Good");
    public final static EnumOrderLineRating GOOD_PLUS        = new EnumOrderLineRating(7, "007", "Very Good");

    public final static EnumOrderLineRating VERY_GOOD        = new EnumOrderLineRating(8, "008", "Great");
    public final static EnumOrderLineRating VERY_GOOD_PLUS     = new EnumOrderLineRating(9, "009", "Almost Perfect");
    public final static EnumOrderLineRating PERFECT    = new EnumOrderLineRating(10, "010", "Never Better");
    public final static EnumOrderLineRating PEAK_PRODUCE_8        = new EnumOrderLineRating(11, "P08", "Great");
    public final static EnumOrderLineRating PEAK_PRODUCE_9        = new EnumOrderLineRating(12, "P09", "Almost Perfect");    
    public final static EnumOrderLineRating PEAK_PRODUCE_10        = new EnumOrderLineRating(13, "P10", "Never Better");
    public final static EnumOrderLineRating NO_RATING        = new EnumOrderLineRating(14, "X", "No rating");
    
    protected final int id;
    private final String statusCode;
    private final String shortDescription;
    
    private EnumOrderLineRating(int id, String statusCode, String shortDescription) {
        this.id = id;
        this.statusCode = statusCode;
        this.shortDescription = shortDescription;
        
        STATUSCODE_MAP.put( this.statusCode, this );
    }
    
    public int getId(){
    	return this.id;
    }
    
    public String getStatusCode() {
        return this.statusCode;
    }
    
    public String getShortDescription() {
        return this.shortDescription;
    }
    
    public static EnumOrderLineRating getEnumByStatusCode(String statusCode){
    	return statusCode == null ? null : (EnumOrderLineRating) STATUSCODE_MAP.get( statusCode.toUpperCase() );
    }
    
    public boolean isEligibleToDisplay(){
    	if("X".equalsIgnoreCase(this.statusCode) || "000".equalsIgnoreCase(this.statusCode)){
    		return false;
    	}
    	return true;
    }
    
    public String getStatusCodeInDisplayFormat(){
    	if(this.statusCode.length()==3)
    	  return this.statusCode.substring(1);
    	else
    	  return this.statusCode;
    }
    
    
    public String toString() {    	
        return this.statusCode + " : " + this.shortDescription;
    }        
    
    public boolean equals(Object o) {
        if (o instanceof EnumOrderLineRating) {
            return this.id == ((EnumOrderLineRating)o).id;
        }
        return false;
    }
    
}