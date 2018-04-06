package com.freshdirect.webapp.ajax.browse.data;

import java.io.Serializable;

public class MySaleItemsData implements Serializable { 
	double ratingBaseLine;
	
	public double getRatingBaseLine() {
		return ratingBaseLine;
	}
	
	public void setRatingBaseLine(double ratingBaseLine) {
		this.ratingBaseLine = ratingBaseLine;
	}

	double dealsBaseLine;
	
	public double getDealsBaseLine() {
		return dealsBaseLine;
	}
	
	public void setDealsBaseLine(double dealsBaseLine) {
		this.dealsBaseLine = dealsBaseLine;
	}

	boolean considerBackInStock;
	
	public boolean isConsiderBackInStock() {
		return considerBackInStock;
	}
	
	public void setConsiderBackInStock(boolean considerBackInStock) {
		this.considerBackInStock = considerBackInStock;
	}
	
	BrowseData browsedata;

	public BrowseData getBrowsedata() {
		return browsedata;
	}

	public void setBrowsedata(BrowseData browsedata) {
		this.browsedata = browsedata;
	}
	
}
