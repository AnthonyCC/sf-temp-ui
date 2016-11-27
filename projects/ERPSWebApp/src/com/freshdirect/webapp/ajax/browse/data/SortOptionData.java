package com.freshdirect.webapp.ajax.browse.data;

public class SortOptionData extends SelectableData {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2269075873786147626L;

	/**if option is clicked this will be the new order*/
	private boolean orderAscending;
	
	

	public boolean isOrderAscending() {
		return orderAscending;
	}

	public void setOrderAscending(boolean orderAscending) {
		this.orderAscending = orderAscending;
	}

}
