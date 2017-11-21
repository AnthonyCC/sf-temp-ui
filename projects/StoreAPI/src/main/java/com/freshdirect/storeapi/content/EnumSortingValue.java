package com.freshdirect.storeapi.content;

/**
 * This enum represents the type of factors which may be used for sorting search results.
 * 
 * Sorting factors are descending by default (the item with highest value comes first).
 * 
 * @author csongor
 */
public enum EnumSortingValue {
	PHRASE(0), LUCENE_SCORE(0.0f), NEWNESS(Float.NEGATIVE_INFINITY, true), DEAL(0), AVAILABILITY(0),
			ORIGINAL_TERM(0), CATEGORY_RELEVANCY(0), TERM_SCORE(0);

	private final boolean ascending;
	private final Number defaultValue;

	private EnumSortingValue(Number defaultValue) {
		this.defaultValue = defaultValue;
		this.ascending = false;
	}

	private EnumSortingValue(Number defaultValue, boolean ascending) {
		this.defaultValue = defaultValue;
		this.ascending = ascending;
	}

	public Number getDefaultValue() {
		return defaultValue;
	}

	public boolean isAscending() {
		return ascending;
	}
}
