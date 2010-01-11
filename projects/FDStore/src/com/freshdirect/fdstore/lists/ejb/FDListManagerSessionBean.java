package com.freshdirect.fdstore.lists.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.OrderLineUtil;
import com.freshdirect.fdstore.customer.ejb.EnumCustomerListType;
import com.freshdirect.fdstore.customer.ejb.FDUserDAO;
import com.freshdirect.fdstore.lists.FDCustomerCreatedList;
import com.freshdirect.fdstore.lists.FDCustomerList;
import com.freshdirect.fdstore.lists.FDCustomerListExistsException;
import com.freshdirect.fdstore.lists.FDCustomerProductList;
import com.freshdirect.fdstore.lists.FDCustomerProductListLineItem;
import com.freshdirect.fdstore.lists.FDCustomerRecipeList;
import com.freshdirect.fdstore.lists.FDCustomerRecipeListLineItem;
import com.freshdirect.fdstore.lists.FDCustomerShoppingList;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FDListManagerSessionBean extends SessionBeanSupport {

	private final static Category LOGGER = LoggerFactory.getInstance(FDListManagerSessionBean.class);

	public List getEveryItemEverOrdered(FDIdentity identity) throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			FDCustomerListDAO dao = new FDCustomerListDAO();

			List retList = new ArrayList();
			FDCustomerShoppingList list = (FDCustomerShoppingList) dao.load(conn, identity /* new PrimaryKey(identity.getErpCustomerPK()) */, EnumCustomerListType.SHOPPING_LIST, FDCustomerShoppingList.EVERY_ITEM_LIST);

			if (list == null) {
				list = dao.generateEveryItemEverOrderedList(conn, identity);
				dao.store(conn, list);
				list = (FDCustomerShoppingList) dao.load(conn, identity /* new PrimaryKey(identity.getErpCustomerPK()) */, EnumCustomerListType.SHOPPING_LIST, FDCustomerShoppingList.EVERY_ITEM_LIST);
			}

			for (Iterator i = list.getLineItems().iterator(); i.hasNext();) {
				FDCustomerProductListLineItem item = (FDCustomerProductListLineItem) i.next();
				try {
					if(item.getSkuCode().equals(FDStoreProperties.getGiftcardSkucode())){
						i.remove();
						continue;
					}
					if(item.getSkuCode().equals(FDStoreProperties.getRobinHoodSkucode())){
						i.remove();
						continue;
					}
					if (item.getDeleted() == null)
						retList.add(item.convertToSelection());
				} catch (FDSkuNotFoundException e) {
					LOGGER.warn("Loaded an invalid sku - skipping", e);
				}
			}

			return retList;
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
		    close(conn);
		}
	}
	
	public FDCustomerShoppingList generateEveryItemEverOrderedList(FDIdentity identity) throws FDResourceException{
		Connection conn = null;
		try {
			conn = getConnection();
			FDCustomerListDAO dao = new FDCustomerListDAO();
			return dao.generateEveryItemEverOrderedList(conn, identity);
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
                    close(conn);
		}
	}

	public FDCustomerList getCustomerList(FDIdentity identity, EnumCustomerListType type, String listName) throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			FDCustomerListDAO dao = new FDCustomerListDAO();
			return dao.load(conn, identity /* new PrimaryKey(identity.getErpCustomerPK()) */, type, listName);
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
                    close(conn);
		}
	}

	public void storeCustomerList(FDCustomerList list) throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			FDCustomerListDAO dao = new FDCustomerListDAO();
			dao.store(conn, list);
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
                    close(conn);
		}
	}

	public boolean removeCustomerListItem(FDIdentity identity, PrimaryKey id) throws FDResourceException {
		Connection conn = null;
		boolean result = false;
		try {
			conn = getConnection();
			FDCustomerListDAO dao = new FDCustomerListDAO();
			result = dao.removeItem(conn, identity, id);
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
                    close(conn);
		}
		return result;
	}

    // CCL
	public void createCustomerCreatedList(FDIdentity identity, String listName) throws FDResourceException, RemoteException, FDCustomerListExistsException {
		Connection conn = null;
		try {
			conn = getConnection();
			FDCustomerListDAO dao = new FDCustomerListDAO();
			if (dao.isCustomerCreatedList(conn,identity,listName)) throw new FDCustomerListExistsException();
			dao.createCustomerCreatedList(conn, identity, listName);
		} catch (FDCustomerListExistsException e) {
			this.getSessionContext().setRollbackOnly();
			throw e;
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
                    close(conn);
		}
	}
	
    // CCL
	public void deleteCustomerCreatedList(FDIdentity identity, String listName) throws FDResourceException, RemoteException {
		Connection conn = null;
		if (getCustomerCreatedLists(identity).size() <= 1) {
			// don't delete the last list.
			return;
		}
		try {
			conn = getConnection();
			FDCustomerListDAO dao = new FDCustomerListDAO();
			dao.deleteCustomerCreatedList(conn, identity, listName);
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
                    close(conn);
		}
	}
	
    // CCL
	public boolean isCustomerCreatedList(FDIdentity identity, String listName) throws FDResourceException, RemoteException {
		Connection conn = null;
		try {
			conn = getConnection();
			FDCustomerListDAO dao = new FDCustomerListDAO();
			return dao.isCustomerCreatedList(conn, identity, listName);
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
                    close(conn);
		}
	}

	private static final String DEFAULT_CCL_NAME_SUFFIX = "'s List";
	
	// CCL
	public List getCustomerCreatedLists(FDIdentity identity) throws FDResourceException, RemoteException {
		Connection conn = null;
		try {
			conn = getConnection();
			FDCustomerListDAO dao = new FDCustomerListDAO();

			List lists = dao.getCustomerCreatedLists(conn, identity); 
			if (lists.isEmpty()) { 
				FDUser user = FDUserDAO.recognizeWithIdentity(conn, identity);
				if (user.isAnonymous()) {
					throw new FDResourceException("User does not exists with identity "+identity);
				}
				String defaultName = user.getFirstName()+ DEFAULT_CCL_NAME_SUFFIX;
				dao.createCustomerCreatedList(conn, identity, defaultName);
				lists = dao.getCustomerCreatedLists(conn, identity);
			}
			OrderLineUtil.cleanProductLists(lists);
			return lists;
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
                    close(conn);
		}
	}
	
	// CCL
	public List getCustomerCreatedListInfos(FDIdentity identity) throws FDResourceException, RemoteException {
		Connection conn = null;
		try {
			conn = getConnection();
			FDCustomerListDAO dao = new FDCustomerListDAO();

			List lists = dao.getCustomerCreatedListInfos(conn, identity); 
			if (lists.isEmpty()) { 
				FDUserI user = FDCustomerManager.recognize(identity);
				String defaultName = user.getFirstName()+ DEFAULT_CCL_NAME_SUFFIX;
				dao.createCustomerCreatedList(conn, identity, defaultName);
				lists = dao.getCustomerCreatedListInfos(conn, identity);
			}
			return lists;
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} catch (FDAuthenticationException e) {
			throw new FDResourceException(e);
		} finally {
                    close(conn);
		}
	}

	// this is a generic api modify the customer list. just make sure name does not duplicate before calling this 
	public void modifyCustomerCreatedList(FDCustomerList list) throws FDResourceException, RemoteException {
		Connection conn = null;
		try {
			conn = getConnection();
			// need to decide static or this way
			FDCustomerListDAO dao = new FDCustomerListDAO();
			dao.updateCustomerList(conn, list);
		} catch(SQLException e) {
			throw new FDResourceException(e);
		} finally {
                    close(conn);
		}
	}


	// this API will copy the customer list from source to target.
	// currently this is not getting used	
	public void copyCustomerCreatedList(FDCustomerList sourceCCList,FDCustomerList targetCCList) throws FDResourceException, RemoteException, FDCustomerListExistsException {
		Connection conn = null;
		try {
			conn = getConnection();
			// need to decide static or this way
			FDCustomerListDAO dao = new FDCustomerListDAO();
			// first figure out it is product or recipe before merging
			if(sourceCCList instanceof FDCustomerProductList && targetCCList instanceof FDCustomerProductList) 
			{
				FDCustomerProductList targerList=(FDCustomerProductList)targetCCList;
				//	iterate through the new list and add indivisual Item to the OldList
				for (Iterator i = sourceCCList.getLineItems().iterator(); i.hasNext();)
				{						
					FDCustomerProductListLineItem item = (FDCustomerProductListLineItem) i.next();
					item.setLastPurchase(new Date());
					targerList.mergeLineItem(item);
				}					
			}
			else if(sourceCCList instanceof FDCustomerRecipeList && targetCCList instanceof FDCustomerRecipeList)
			{
				FDCustomerRecipeList targerList=(FDCustomerRecipeList)targetCCList;
				//	iterate through the new list and add indivisual Item to the OldList
				for (Iterator i = sourceCCList.getLineItems().iterator(); i.hasNext();)
				{						
					FDCustomerRecipeListLineItem item = (FDCustomerRecipeListLineItem) i.next();
					item.setLastPurchase(new Date());
					targerList.mergeRecipe(item.getRecipeId(),false);
				}					
			}
			else
			{
				// cannot copy since expected customer list type cannot be found
				throw new FDResourceException("unexpected customer List type found while copying");
			}
			
			// store the updated list
			dao.store(conn, targetCCList);
		}
		catch(SQLException e)
		{
			throw new FDResourceException(e);
		} finally {
                    close(conn);
		}
	}

	public FDCustomerCreatedList getCustomerCreatedList(FDIdentity identity,String ccListId) throws FDResourceException, RemoteException
	{
		Connection conn = null;			
		try {
			conn = getConnection();
			FDCustomerListDAO dao = new FDCustomerListDAO();
			FDCustomerCreatedList ccl = dao.getCustomerCreatedList(conn, identity,ccListId);
			ccl.cleanList();
			return ccl;
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
                    close(conn);
		}			
	}
	
	public String getListName(FDIdentity identity, EnumCustomerListType type, String ccListId) throws FDResourceException, RemoteException {
		Connection conn = null;			
		try {
			conn = getConnection();
			FDCustomerListDAO dao = new FDCustomerListDAO();
			return dao.getListName(conn, identity, type, ccListId);
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
                    close(conn);
		}			
	}
	
	public void renameCustomerCreatedList(FDIdentity identity, String oldName, String newName) throws FDCustomerListExistsException, FDResourceException, RemoteException {
		Connection conn = null;			
		try {
			conn = getConnection();
			FDCustomerListDAO dao = new FDCustomerListDAO();
			if (dao.isCustomerCreatedList(conn, identity, newName)) throw new FDCustomerListExistsException();
			dao.renameCustomerCreatedList(conn, identity, oldName, newName);
		}catch (FDCustomerListExistsException e) {
			getSessionContext().setRollbackOnly();
			throw e;
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
                    close(conn);
		}	
	}
	
	public FDCustomerProductList getOrderDetails(String erpCustomerId, List skus) throws FDResourceException, RemoteException {
		Connection conn = null;			
		try {
			conn = getConnection();
			FDCustomerListDAO dao = new FDCustomerListDAO();

            return dao.getOrderDetails(conn,erpCustomerId,skus);
		} catch (SQLException e) {
			System.out.println(">>> " + e);
			throw new FDResourceException(e);
		} finally {
                    close(conn);
		}	
	}
}
