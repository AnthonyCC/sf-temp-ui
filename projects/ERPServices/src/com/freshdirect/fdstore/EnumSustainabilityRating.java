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
	NEVER_RATED		( 0, "00", "Never Rated/Rating Expired"),
	NO_RATING		( 1,   "X", "No rating"),
	COULD_USE_SIGNIFICANT_IMPROVMENT		( 2, "01", "Could Use Significant Improvement"),
	COULD_USE_IMPROVMENT	(3, "02", "Could Use Improvement"),
	OK_ALTERNATIVE			(4, "03", "Ok Alternative"),
	OCEAN_FRIENDLY	(5, "04", "Ocean-Friendly"),
	VERY_OCEAN_FRIENDLY	(6, "05", "Very Ocean-Friendly"),
	BEST_PRACTICE	(7, "06", "Best Practice");

	private final static Map<String, EnumSustainabilityRating> STATUSCODE_MAP = new HashMap<String, EnumSustainabilityRating>();

	static {
		for (EnumSustainabilityRating r : values()) {
			STATUSCODE_MAP.put(r.statusCode, r);
		}
	}

	public static EnumSustainabilityRating getEnumByStatusCode(String statusCode) {
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
		if (id < 2) {
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