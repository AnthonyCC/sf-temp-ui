package com.freshdirect.mobileapi.controller.data;

import java.util.ArrayList;
import java.util.List;

public class PairItProductModel extends Message {
	private List<String> pairItProductIds = new ArrayList<String>();
	private String heading;
	private String text;
 
	public List<String> getPairItProductIds() {
		return pairItProductIds;
	}

	public void setPairItProductIds(List<String> pairItProductIds) {
		this.pairItProductIds = pairItProductIds;
	}

	public String getHeading() {
		return heading;
	}

	public void setHeading(String heading) {
		this.heading = heading;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	
	
}
