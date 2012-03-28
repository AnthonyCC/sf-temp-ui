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
 * Type-safe enumeration for sustainability rating.
 * 
 * @version $Revision$
 * @author $Author$
 */
public enum EnumSustainabilityRating {
	NEVER_RATED		( 0, "00", "Not Yet Rated"),
	NO_RATING		( 1,   "X", "No rating"), //shouldn't show up on web front
	COULD_USE_SIGNIFICANT_IMPROVMENT		( 2, "01", "Avoid"),
	COULD_USE_IMPROVMENT	(3, "02", "Good Alternative"),
	OK_ALTERNATIVE			(4, "03", "Best Choice"),
	OCEAN_FRIENDLY	(5, "04", "On The Path To Sustainability");
	/*VERY_OCEAN_FRIENDLY	(6, "05", "Very Ocean-Friendly"),*/
	/*BEST_PRACTICE	(7, "06", "Best Practice");*/

	private final static Map<String, EnumSustainabilityRating> STATUSCODE_MAP = new HashMap<String, EnumSustainabilityRating>();

	static {
		for (EnumSustainabilityRating r : values()) {
			STATUSCODE_MAP.put(r.statusCode, r);
		}
	}

	public static EnumSustainabilityRating getEnumByStatusCode(String statusCode) {
		if ("".equals(statusCode) || statusCode == null || (EnumSustainabilityRating) STATUSCODE_MAP.get(statusCode.toUpperCase()) == null) {
			statusCode = "00";
		}
		return statusCode == null ? null : (EnumSustainabilityRating) STATUSCODE_MAP.get(statusCode.toUpperCase());
	}

	protected final int id;
	private final String statusCode;
	private final String shortDescription;
	

	private EnumSustainabilityRating(int id, String statusCode, String shortDescription) {
		this.id = id;
		this.statusCode = statusCode;
		this.shortDescription = shortDescription;
	}

	public int getId() {
		return this.id;
	}

	public String getStatusCode() {
		return this.statusCode;
	}

	public String getShortDescription() {
		return this.shortDescription;
	}

	public boolean isEligibleToDisplay() {
		/* APPDEV-1979 changes 0 to be an allowed value */
			if (id < 2 && id != 0) {
				return false;
			}
		return true;
	}

	public String getStatusCodeInDisplayFormat() {
		if (this.statusCode.length() == 3)
			return this.statusCode.substring(1);
		else
			return this.statusCode;
	}

	public String toString() {
		return this.statusCode + " : " + this.shortDescription;
	}

	
}