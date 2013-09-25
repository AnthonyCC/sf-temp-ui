package com.freshdirect.webapp.ajax.shoppinglist;

import java.io.Serializable;
import java.util.List;

/**
 *	Simple java bean for shopping list requests. 
 *	Class structure is representing the received JSON structure. 	
 * 
 * @author treer
 */
public class ShoppingListRequestData implements Serializable {

	private static final long	serialVersionUID	= -6594762236272807294L;
	
	private List<ShoppingListChange> listInfos;
	
	public List<ShoppingListChange> getListInfos() {
		return listInfos;
	}	
	public void setListInfos( List<ShoppingListChange> listInfos ) {
		this.listInfos = listInfos;
	}	

}
