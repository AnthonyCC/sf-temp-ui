package com.freshdirect.fdstore.lists;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.OrderLineUtil;
import com.freshdirect.fdstore.customer.ejb.EnumCustomerListType;

public class FDCustomerShoppingList extends FDCustomerProductList {
	
	public final static String EVERY_ITEM_LIST = "QuickShop";

	/**
	 *  Return the type of list this implementation handles.
	 *  
	 *  @return the list type corresponding to shopping lists
	 *  @see EnumCustomerListType#SHOPPING_LIST
	 */
	public EnumCustomerListType getType() {
		return EnumCustomerListType.SHOPPING_LIST; 
	}

	
	
	public void cleanList() throws FDResourceException {
		List items = getLineItems();
		assert (items != null);
		List cleanList = OrderLineUtil.updateCCLItems(items);
		try {
			List undupList = removeDuplicates(cleanList);
			setLineItems(undupList);
			// the setLineItems call will mark the list as modified
		} catch (FDInvalidConfigurationException e) {
			throw new FDResourceException(e);
		}
	}



	//
	// remove duplicates
	//		
	private List removeDuplicates(List lineItems) throws FDResourceException, FDInvalidConfigurationException {
		ArrayList newList = new ArrayList(lineItems);
		for (int i = 0; i < newList.size() - 1; i++) {
			FDCustomerProductListLineItem ps1 = (FDCustomerProductListLineItem) newList.get(i);
			FDCustomerProductListLineItem ps2 = (FDCustomerProductListLineItem) newList.get(i+1);
			try {
				if (OrderLineUtil.isSameConfiguration(ps1.convertToSelection(), ps2.convertToSelection())) {
					newList.remove(ps2);				
					i--;
				}
			} catch (FDSkuNotFoundException e) {
				throw new FDInvalidConfigurationException(e);
			}
		}
		return newList;
	}
}
