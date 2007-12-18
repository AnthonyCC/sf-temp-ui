package com.freshdirect.fdstore.lists;

import java.util.List;

public class FDCustomerCreatedListInfo extends FDCustomerCreatedList {

	private int count;
	
	public FDCustomerCreatedListInfo() {
	}
	
	
	public List getLineItems() {
		throw new UnsupportedOperationException();
	}

	
	public void setCount(int count) {
		this.count = count;
	}
	
	public int getCount() {
		return count;
	}
}
