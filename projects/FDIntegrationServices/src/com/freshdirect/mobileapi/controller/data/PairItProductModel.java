package com.freshdirect.mobileapi.controller.data;

import java.util.ArrayList;
import java.util.List;

public class PairItProductModel extends Message {
	private List<String> pairItProductIds = new ArrayList<String>();

	public List<String> getPairItProductIds() {
		return pairItProductIds;
	}

	public void setPairItProductIds(List<String> pairItProductIds) {
		this.pairItProductIds = pairItProductIds;
	}
	
}
