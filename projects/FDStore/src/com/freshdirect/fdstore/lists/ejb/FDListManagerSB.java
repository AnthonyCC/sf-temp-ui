package com.freshdirect.fdstore.lists.ejb;

/**
 *@deprecated Please use the ListManagerController and ListManagerServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */
import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJBObject;

import com.freshdirect.common.context.StoreContext;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDProductSelectionI;
import com.freshdirect.fdstore.customer.ejb.EnumCustomerListType;
import com.freshdirect.fdstore.lists.FDCustomerCreatedList;
import com.freshdirect.fdstore.lists.FDCustomerList;
import com.freshdirect.fdstore.lists.FDCustomerListExistsException;
import com.freshdirect.fdstore.lists.FDCustomerListInfo;
import com.freshdirect.fdstore.lists.FDCustomerProductList;
import com.freshdirect.fdstore.lists.FDCustomerShoppingList;
import com.freshdirect.fdstore.lists.FDStandingOrderList;
import com.freshdirect.framework.core.PrimaryKey;

public interface FDListManagerSB extends EJBObject {

	@Deprecated
    public List<FDProductSelectionI> getEveryItemEverOrdered(FDIdentity identity) throws FDResourceException, RemoteException;
	@Deprecated
    public FDCustomerList getCustomerList(FDIdentity identity, EnumCustomerListType type, String listName) throws FDResourceException, RemoteException;
	@Deprecated
    public FDCustomerList getCustomerListById(FDIdentity identity, EnumCustomerListType type, String listId) throws FDResourceException, RemoteException;
	@Deprecated
    public FDCustomerShoppingList generateEveryItemEverOrderedList(FDIdentity identity) throws FDResourceException, RemoteException;
	@Deprecated
    public List<FDProductSelectionI> getQsSpecificEveryItemEverOrderedList(FDIdentity identity, StoreContext storeContext) throws FDResourceException, RemoteException;
    //APPDEV-4179 - Item quantities should NOT be honored in "Your Top Items"
	@Deprecated
	public List<FDProductSelectionI> getQsSpecificEveryItemEverOrderedListTopItems(FDIdentity identity, StoreContext storeContext) throws FDResourceException, RemoteException;
	@Deprecated
    public FDCustomerList storeCustomerList(FDCustomerList list) throws FDResourceException, RemoteException;
	@Deprecated
    public boolean removeCustomerListItem(FDIdentity identity, PrimaryKey id) throws FDResourceException, RemoteException;
	@Deprecated
	public String createCustomerCreatedList(FDIdentity identity, StoreContext storeContext, String listName) throws FDResourceException, RemoteException,  FDCustomerListExistsException;
	@Deprecated
	public void deleteCustomerCreatedList(FDIdentity identity, String listName, StoreContext storeContext) throws FDResourceException, RemoteException;
	@Deprecated
	public void deleteShoppingList( String listId ) throws FDResourceException, RemoteException;		
	@Deprecated
	public List<FDCustomerCreatedList> getCustomerCreatedLists(FDIdentity identity, StoreContext storeContext) throws FDResourceException, RemoteException;
	@Deprecated
	public List<FDCustomerListInfo> getCustomerCreatedListInfos(FDIdentity identity, StoreContext storeContext) throws FDResourceException, RemoteException;
	@Deprecated
	public List<FDCustomerListInfo> getStandingOrderListInfos(FDIdentity identity) throws FDResourceException, RemoteException;
	@Deprecated
	public void modifyCustomerCreatedList(FDCustomerList list) throws FDResourceException, RemoteException;
	@Deprecated
	public void copyCustomerCreatedList(FDCustomerList oldList,FDCustomerList newList) throws FDResourceException, RemoteException, FDCustomerListExistsException ;
	@Deprecated
	public boolean isCustomerList(FDIdentity identity, EnumCustomerListType type, String listName) throws FDResourceException, RemoteException ;
	@Deprecated
	public FDCustomerCreatedList getCustomerCreatedList(FDIdentity identity,String ccListId) throws FDResourceException, RemoteException;
	@Deprecated
	public String getListName(FDIdentity identity, String ccListId) throws FDResourceException, RemoteException;
	@Deprecated
	public void renameCustomerCreatedList(FDIdentity identity, String oldName, String newName) throws FDCustomerListExistsException, FDResourceException, RemoteException;
	@Deprecated
	public void renameCustomerList(FDActionInfo info, EnumCustomerListType type, String oldName, String newName) throws FDCustomerListExistsException, FDResourceException, RemoteException;
	@Deprecated
	public void renameShoppingList(String listId, String newName) throws FDResourceException, RemoteException;

	// SmartStore
	@Deprecated
	public FDCustomerProductList getOrderDetails(String erpCustomerId, List<String> skus) throws FDResourceException, RemoteException;
	@Deprecated
	public FDStandingOrderList getStandingOrderList(FDIdentity identity, String soListId) throws FDResourceException, RemoteException;
}
