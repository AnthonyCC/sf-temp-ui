package com.freshdirect.webapp.ajax.shoppinglist;

import java.io.Serializable;
import java.util.List;

/**
 *	Simple java bean for shopping list responses. 
 *	Class structure is representing the JSON structure. 	
 * 
 * @author treer
 */
public class ShoppingListResponseData implements Serializable {
	
	private static final long	serialVersionUID	= 3973929651231480750L;
	
	private List<ShoppingListInfo> listInfos;
	private List<AddToListResponseItem> responseItems;
	private boolean shoppingListPageRefreshNeeded = false;
	
	public List<ShoppingListInfo> getListInfos() {
		return listInfos;
	}	
	public void setListInfos( List<ShoppingListInfo> listInfos ) {
		this.listInfos = listInfos;
	}
	public boolean isShoppingListPageRefreshNeeded() {
		return shoppingListPageRefreshNeeded;
	}
	public void setShoppingListPageRefreshNeeded(boolean pageRefreshNeeded) {
		this.shoppingListPageRefreshNeeded = pageRefreshNeeded;
	}
	public List<AddToListResponseItem> getResponseItems() {
		return responseItems;
	}
	public void setResponseItems(List<AddToListResponseItem> responseItems) {
		this.responseItems = responseItems;
	}	
	
}
