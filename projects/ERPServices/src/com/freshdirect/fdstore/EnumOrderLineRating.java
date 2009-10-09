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
public enum EnumOrderLineRating  {
	
    
    NEVER_RATED         (0, "000", "Never Rated/Rating Expired", 5),
    NO_RATING           (1, "X", "No rating", 6),
    TERRIBLE            (2, "001", "Terrible", 1),
    BELOW_AVG           (3, "002", "Below Average", 2),
    BELOW_AVG_PLUS      (4, "003", "Inconsistent", 3),
    
    AVERAGE             (5, "004", "Average ", 4),
    AVERAGE_PLUS        (6, "005", "Above Average", 7),
    GOOD                (7, "006", "Good", 8),
    GOOD_PLUS           (8, "007", "Very Good", 9),

    VERY_GOOD           (9, "008", "Great", 10),
    VERY_GOOD_PLUS      (10, "009", "Almost Perfect", 12),
    PERFECT             (11, "010", "Never Better", 14),
    PEAK_PRODUCE_8      (12, "P08", "Great", 11),
    PEAK_PRODUCE_9      (13, "P09", "Almost Perfect", 13),    
    PEAK_PRODUCE_10     (14, "P10", "Never Better", 15);

    private final static Map<String, EnumOrderLineRating> STATUSCODE_MAP = new HashMap<String, EnumOrderLineRating> ();
    
    static {
        for (EnumOrderLineRating r : values()) {
            STATUSCODE_MAP.put(r.statusCode, r);
        }
    }
    

    
    protected final int id;
    private final String statusCode;
    private final String shortDescription;
    private final int qualityRating;
    
    private EnumOrderLineRating(int id, String statusCode, String shortDescription, int qualityRating) {
        this.id = id;
        this.statusCode = statusCode;
        this.shortDescription = shortDescription;
        this.qualityRating = qualityRating;
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
    
    public int getQualityRating() {
        return qualityRating;
    }
    
    
}