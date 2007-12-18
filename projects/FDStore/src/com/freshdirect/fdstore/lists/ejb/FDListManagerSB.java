package com.freshdirect.fdstore.lists.ejb;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJBObject;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.ejb.EnumCustomerListType;
import com.freshdirect.fdstore.lists.FDCustomerCreatedList;
import com.freshdirect.fdstore.lists.FDCustomerList;
import com.freshdirect.fdstore.lists.FDCustomerListExistsException;
import com.freshdirect.fdstore.lists.FDCustomerShoppingList;
import com.freshdirect.framework.core.PrimaryKey;

public interface FDListManagerSB extends EJBObject {

    public List getEveryItemEverOrdered(FDIdentity identity) throws FDResourceException, RemoteException;
    
    public FDCustomerList getCustomerList(FDIdentity identity, EnumCustomerListType type, String listName) throws FDResourceException, RemoteException;
    
    public FDCustomerShoppingList generateEveryItemEverOrderedList(FDIdentity identity) throws FDResourceException, RemoteException;

    public void storeCustomerList(FDCustomerList list) throws FDResourceException, RemoteException;
    
    public void removeCustomerListItem(PrimaryKey id) throws FDResourceException, RemoteException;

	public void createCustomerCreatedList(FDIdentity identity, String listName) throws FDResourceException, RemoteException,  FDCustomerListExistsException;
	
	public void deleteCustomerCreatedList(FDIdentity identity, String listName) throws FDResourceException, RemoteException;
	
	public List getCustomerCreatedLists(FDIdentity identity) throws FDResourceException, RemoteException;

	public List getCustomerCreatedListInfos(FDIdentity identity) throws FDResourceException, RemoteException;
	
	public void modifyCustomerCreatedList(FDCustomerList list) throws FDResourceException, RemoteException;
	
	public void copyCustomerCreatedList(FDCustomerList oldList,FDCustomerList newList) throws FDResourceException, RemoteException, FDCustomerListExistsException ;
	
	public boolean isCustomerCreatedList(FDIdentity identity, String listName) throws FDResourceException, RemoteException ;
	
	public FDCustomerCreatedList getCustomerCreatedList(FDIdentity identity,String ccListId) throws FDResourceException, RemoteException;
	
	public String getListName(EnumCustomerListType type, String ccListId) throws FDResourceException, RemoteException;
	
	public void renameCustomerCreatedList(FDIdentity identity, String oldName, String newName) throws FDCustomerListExistsException, FDResourceException, RemoteException;

	
}
