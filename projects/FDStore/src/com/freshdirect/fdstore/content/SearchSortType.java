package com.freshdirect.fdstore.content;

public enum SearchSortType {
    NATURAL_SORT(-1, "natr", "", "", ""),	// don't
    // sort
    BY_NAME(0, "name", "Name", "Name (A-Z)", "Name (Z-A)"),
	BY_PRICE(1, "prce", "Price", "Price (low)", "Price (high)"),
	BY_RELEVANCY(2, "relv", "Relevance", "Most Relevant", "Least Relevant"),
	BY_POPULARITY(3, "pplr", "Popularity", "Most Popular", "Least Popular"),
	DEFAULT(4, "tdef", "Default", "Default", "Default"),			// 'default' sort on text view
	BY_SALE(5, "sale", "Sale", "Sale (yes)", "Sale (no)"),
	BY_RECENCY(6, "recency", "Recent", "Recent", "Recent"),
	BY_OURFAVES(6, "ourFaves", "Our Favorites", "Our Favorites", "Our Favorites"),
	BY_DEPARTMENT(7, "dept", "Department", "Department", "Department");
    
    private int type;
    private String label;
    private String text;
    private String textAsc;
    private String textDesc;

    SearchSortType(int t, String label, String text, String textAsc, String textDesc) {
    	this.type = t;
    	this.label = label;
    	this.text = text;
    	this.textAsc = textAsc;
    	this.textDesc = textDesc;
    }

    public int getType() {
    	return this.type;
    }
    
    public String getLabel() {
    	return this.label;
    }
    
    public String getText() {
		return text;
	}
    
    public String getTextAsc() {
		return textAsc;
	}
    
    public String getTextDesc() {
		return textDesc;
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
     * Various defaults per view specific to search page.
     */
    public static final SearchSortType DEF4TEXT = SearchSortType.DEFAULT;
    public static final SearchSortType DEF4NOTTEXT = SearchSortType.BY_RELEVANCY;
    public static final SearchSortType DEF4RECIPES = SearchSortType.BY_NAME;
}
