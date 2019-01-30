package com.freshdirect.fdstore.lists;

import java.rmi.RemoteException;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.common.context.StoreContext;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDProductSelectionI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.ejb.EnumCustomerListType;
import com.freshdirect.fdstore.ecomm.gateway.FDListManagerService;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FDListManager {

	private static final Category LOGGER = LoggerFactory.getInstance(FDListManager.class);

	public static final int QUICKSHOP_ORDER_LIMIT = 100;

	public static List<FDProductSelectionI> getEveryItemEverOrdered(FDIdentity identity) throws FDResourceException {

		try {

			return FDListManagerService.getInstance().getEveryItemEverOrdered(identity);

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static FDCustomerRecipeList getEveryRecipeList(FDIdentity identity) throws FDResourceException {
		return (FDCustomerRecipeList) getCustomerList(identity, EnumCustomerListType.RECIPE_LIST,
				FDCustomerRecipeList.EVERY_RECIPE_LIST);
	}

	public static FDCustomerShoppingList generateEveryItemEverOrdered(FDIdentity identity) throws FDResourceException {

		try {

			return FDListManagerService.getInstance().generateEveryItemEverOrderedList(identity);

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static List<FDProductSelectionI> getQsSpecificEveryItemEverOrderedList(FDIdentity identity,
			StoreContext storeContext) throws FDResourceException {

		try {
			return FDListManagerService.getInstance().getQsSpecificEveryItemEverOrderedList(identity, storeContext);

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	// APPDEV-4179 - Item quantities should NOT be honored in "Your Top Items"
	public static List<FDProductSelectionI> getQsSpecificEveryItemEverOrderedListTopItems(FDIdentity identity,
			StoreContext storeContext) throws FDResourceException {

		try {
			return FDListManagerService.getInstance().getQsSpecificEveryItemEverOrderedListTopItems(identity,
					storeContext);

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static FDCustomerList getCustomerList(FDIdentity identity, EnumCustomerListType type, String listName)
			throws FDResourceException {

		try {
			return FDListManagerService.getInstance().getCustomerList(identity, type, listName);

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static FDCustomerList getCustomerListById(FDIdentity identity, EnumCustomerListType type, String listId)
			throws FDResourceException {

		try {
			return FDListManagerService.getInstance().getCustomerListById(identity, type, listId);

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static FDCustomerList storeCustomerList(FDCustomerList list) throws FDResourceException {

		try {
			return FDListManagerService.getInstance().storeCustomerList(list);

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static void removeCustomerListItem(FDUserI user, PrimaryKey id) throws FDResourceException {

		try {
			FDListManagerService.getInstance().removeCustomerListItem(user.getIdentity(), id);

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	// CCL
	// create new list
	public static String createCustomerCreatedList(FDUserI user, String listName)
			throws FDResourceException, FDCustomerListExistsException {

		try {
			return FDListManagerService.getInstance().createCustomerCreatedList(user.getIdentity(),
					user.getUserContext().getStoreContext(), listName);

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	// CCL
	// rename list: use store with new list name

	// CCL
	// delete: use store with no items
	public static void deleteCustomerCreatedList(FDUserI user, String listName) throws FDResourceException {

		try {
			FDListManagerService.getInstance().deleteCustomerCreatedList(user.getIdentity(), listName,
					user.getUserContext().getStoreContext());

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static void deleteShoppingList(String listId) throws FDResourceException {

		try {
			FDListManagerService.getInstance().deleteShoppingList(listId);

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	// CCL
	// get all customer created lists
	public static List<FDCustomerCreatedList> getCustomerCreatedLists(FDUserI user) throws FDResourceException {

		try {
			return FDListManagerService.getInstance().getCustomerCreatedLists(user.getIdentity(),
					user.getUserContext().getStoreContext());

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	// CCL
	// get all customer created list infos
	public static List<FDCustomerListInfo> getCustomerCreatedListInfos(FDUserI user) throws FDResourceException {

		try {
			return FDListManagerService.getInstance().getCustomerCreatedListInfos(user.getIdentity(),
					user.getUserContext().getStoreContext());

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	// SO
	// get all customer created list infos
	public static List<FDCustomerListInfo> getStandingOrderListInfos(FDUserI user) throws FDResourceException {

		try {
			return FDListManagerService.getInstance().getStandingOrderListInfos(user.getIdentity());

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	// CCL
	public static void modifyCustomerCreatedList(FDCustomerList list) throws FDResourceException {

		try {

			FDListManagerService.getInstance().modifyCustomerCreatedList(list);

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		} catch (FDResourceException e) {

			throw new FDResourceException(e, "Error modifying customer created list");
		}
	}

	// CCL
	/**
	 * @deprecated Use
	 *             {@link FDListManager#isCustomerList(FDUserI, EnumCustomerListType, String)}
	 *             instead.
	 */
	@Deprecated
	public static boolean isCustomerCreatedList(FDUserI user, String listName) throws FDResourceException {
		return isCustomerList(user, EnumCustomerListType.CC_LIST, listName);
	}

	// CCL
	public static boolean isCustomerList(FDUserI user, EnumCustomerListType type, String listName)
			throws FDResourceException {

		try {
			return FDListManagerService.getInstance().isCustomerList(user.getIdentity(),
					type != null ? type : EnumCustomerListType.CC_LIST, listName);

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		} catch (FDResourceException e) {

			throw new FDResourceException(e, "Error checking the existence of customer created list");
		}
	}

	// CCL
	public static void copyCustomerCreatedList(FDCustomerList oldList, FDCustomerList newList)
			throws FDResourceException, FDCustomerListExistsException {

		try {
			FDListManagerService.getInstance().copyCustomerCreatedList(oldList, newList);

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		} catch (FDResourceException e) {

			throw new FDResourceException(e, "Error modifying customer created list");
		}

	}

	// CCL
	public static FDCustomerCreatedList getCustomerCreatedList(FDIdentity identity, String ccListId)
			throws FDResourceException {

		try {
			return FDListManagerService.getInstance().getCustomerCreatedList(identity, ccListId);

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static String getListName(FDIdentity identity, String ccListId) throws FDResourceException {

		try {
			return FDListManagerService.getInstance().getListName(identity, ccListId);

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		} catch (FDResourceException e) {

			throw new FDResourceException(e, "Error retrieving list name");
		}
	}

	public static void renameCustomerCreatedList(FDIdentity identity, String oldName, String newName)
			throws FDCustomerListExistsException, FDResourceException {

		try {
			FDListManagerService.getInstance().renameCustomerCreatedList(identity, oldName, newName);

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		} catch (FDResourceException e) {

			throw new FDResourceException(e, "Error renaming list");
		}
	}

	public static void renameCustomerList(FDActionInfo info, EnumCustomerListType type, String oldName, String newName)
			throws FDCustomerListExistsException, FDResourceException {

		try {
			FDListManagerService.getInstance().renameCustomerList(info, type, oldName, newName);

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		} catch (FDResourceException e) {

			throw new FDResourceException(e, "Error renaming list");
		}
	}

	public static void renameShoppingList(String listId, String newName) throws FDResourceException {

		try {
			FDListManagerService.getInstance().renameShoppingList(listId, newName);

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		} catch (FDResourceException e) {

			throw new FDResourceException(e, "Error renaming list");
		}
	}

	// SmartStore
	public static FDCustomerProductList getOrderDetails(String erpCustomerId, List<String> skus)
			throws FDResourceException {

		try {
			return FDListManagerService.getInstance().getOrderDetails(erpCustomerId, skus);

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		} catch (FDResourceException e) {

			throw new FDResourceException(e, "Error retrieving product details for customer " + erpCustomerId);
		}
	}

	/**
	 * Returns a standing order list
	 * 
	 * @param identity
	 * @param soListId
	 * @return
	 * @throws FDResourceException
	 */
	public static FDStandingOrderList getStandingOrderList(FDIdentity identity, String soListId)
			throws FDResourceException {

		try {
			return FDListManagerService.getInstance().getStandingOrderList(identity, soListId);

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
}
