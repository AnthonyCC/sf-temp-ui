package com.freshdirect.webapp.ajax.shoppinglist;


public class ShoppingListChange extends ShoppingListInfo {

	private static final long	serialVersionUID	= -5332995811200620378L;
	
	private boolean delete = false;
	
	public boolean isDelete() {
		return delete;
	}
	
	public void setDelete( boolean delete ) {
		this.delete = delete;
	}
}
