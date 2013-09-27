package com.freshdirect.webapp.ajax.quickshop;

import com.freshdirect.fdstore.content.SortTypeI;

public enum QuickShopSortType implements SortTypeI {

	    BY_FREQUENCY(3, "freq", "Ordered Frequently (most)","Ordered Frequently (most)","Ordered Frequently (least)" ),
		BY_SALE(1, "sale", "Sale", "Sale", "Sale"),
		BY_RECENCY(2, "recency", "Ordered Recently", "Ordered Recently", "Ordered Recently"),
		BY_NAME(0, "name", "A-Z", "A-Z", "Z-A"),
		BY_YOUR_FAVORITES(5, "favs", "Your Favorites", "Your Favorites", "Your Favorites"),
		BY_EXPERT_RATING(4, "expr", "Expert Rating", "Expert Rating", "Expert Rating");
	    
	    private int type;
	    private String label;
	    private String text;
	    private String textAsc;
	    private String textDesc;

	    QuickShopSortType(int t, String label, String text, String textAsc, String textDesc) {
	    	this.type = t;
	    	this.label = label;
	    	this.text = text;
	    	this.textAsc = textAsc;
	    	this.textDesc = textDesc;
	    }

	    @Override
		public int getType() {
	    	return this.type;
	    }
	    
	    @Override
		public String getLabel() {
	    	return this.label;
	    }
	    
	    @Override
		public String getText() {
			return text;
		}
	    
	    @Override
		public String getTextAsc() {
			return textAsc;
		}
	    
	    @Override
		public String getTextDesc() {
			return textDesc;
		}
	    
	    public static QuickShopSortType findByLabel(String label) {
	    	for (QuickShopSortType e : QuickShopSortType.values()) {
	    		if (e.getLabel().equalsIgnoreCase(label)) {
	    			return e;
	    		}
	    	}
	    	return null;
	    }
	    
	    public static QuickShopSortType findByType(int type) {
	    	for (QuickShopSortType e : QuickShopSortType.values()) {
	    		if (e.getType() == type) {
	    			return e;
	    		}
	    	}
	    	return null;
	    }

}
