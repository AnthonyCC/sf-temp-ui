package com.freshdirect.fdstore.content;

public enum SearchSortType {
    NATURAL_SORT(-1, "natr"),	// don't
    // sort
    BY_NAME(0, "name"),
	BY_PRICE(1, "prce"),
	BY_RELEVANCY(2, "relv"),
	BY_POPULARITY(3, "pplr"),
	DEFAULT(4, "tdef"),			// 'default' sort on text view
	BY_SALE(5, "sale");
	
    private int type;
    private String label;

    SearchSortType(int t, String label) {
    	this.type = t;
    	this.label = label;
    }

    public int getType() {
    	return this.type;
    }
    
    public String getLabel() {
    	return this.label;
    }
    
    public static SearchSortType findByLabel(String label) {
    	for (SearchSortType e : SearchSortType.values()) {
    		if (e.getLabel().equalsIgnoreCase(label)) {
    			return e;
    		}
    	}
    	return null;
    }
    
    public static SearchSortType findByType(int type) {
    	for (SearchSortType e : SearchSortType.values()) {
    		if (e.getType() == type) {
    			return e;
    		}
    	}
    	return null;
    }

    
    /**
     * Various defaults per view
     */
    public static final SearchSortType DEF4TEXT = SearchSortType.DEFAULT;
    public static final SearchSortType DEF4NOTTEXT = SearchSortType.BY_RELEVANCY;
    public static final SearchSortType DEF4RECIPES = SearchSortType.BY_NAME;
}
