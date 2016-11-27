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
public enum EnumOrderLineRating {
	NEVER_RATED		( 0, "000", "Never Rated/Rating Expired", 5, 0),
	NO_RATING		( 1,   "X", "No rating", 6, 0),
	TERRIBLE		( 2, "001", "Terrible", 1, 1),
	BELOW_AVG		( 3, "002", "Below Average", 2, 2),
	BELOW_AVG_PLUS	( 4, "003", "Inconsistent", 3, 3),
	AVERAGE			( 5, "004", "Average ", 4, 4),
	AVERAGE_PLUS	( 6, "005", "Above Average", 7, 5),
	GOOD			( 7, "006", "Good", 8, 6),
	GOOD_PLUS		( 8, "007", "Very Good", 9, 7),
	VERY_GOOD		( 9, "008", "Great", 10, 8),
	VERY_GOOD_PLUS	(10, "009", "Almost Perfect", 12, 9),
	PERFECT			(11, "010", "Never Better", 14, 10),
	PEAK_PRODUCE_8	(12, "P08", "Great", 11, 8),
	PEAK_PRODUCE_9	(13, "P09", "Almost Perfect", 13, 9),
	PEAK_PRODUCE_10	(14, "P10", "Never Better", 15, 10);

	private final static Map<String, EnumOrderLineRating> STATUSCODE_MAP = new HashMap<String, EnumOrderLineRating>();

	static {
		for (EnumOrderLineRating r : values()) {
			STATUSCODE_MAP.put(r.statusCode, r);
		}
	}

	public static EnumOrderLineRating getEnumByStatusCode(String statusCode) {
		return statusCode == null ? null : (EnumOrderLineRating) STATUSCODE_MAP.get(statusCode.toUpperCase());
	}

	protected final int id;
	private final String statusCode;
	private final String shortDescription;
	private final int qualityRating;
	private final int value;

	private EnumOrderLineRating(int id, String statusCode, String shortDescription, int qualityRating, int value) {
		this.id = id;
		this.statusCode = statusCode;
		this.shortDescription = shortDescription;
		this.qualityRating = qualityRating;
		this.value = value;
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
		if (value == 0) {
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

	public int getQualityRating() {
		return qualityRating;
	}

	public int getValue() {
		return value;
	}
}