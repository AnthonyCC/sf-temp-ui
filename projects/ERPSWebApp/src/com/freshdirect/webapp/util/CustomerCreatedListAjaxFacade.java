package com.freshdirect.webapp.util;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.LogManager;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDProductSelectionI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.QuickCart;
import com.freshdirect.fdstore.customer.ejb.EnumCustomerListType;
import com.freshdirect.fdstore.lists.FDCustomerCreatedList;
import com.freshdirect.fdstore.lists.FDCustomerCreatedListInfo;
import com.freshdirect.fdstore.lists.FDCustomerList;
import com.freshdirect.fdstore.lists.FDCustomerListExistsException;
import com.freshdirect.fdstore.lists.FDCustomerProductListLineItem;
import com.freshdirect.fdstore.lists.FDCustomerCreatedList;
import com.freshdirect.fdstore.lists.FDListManager;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.metaparadigm.jsonrpc.JSONRPCBridge;



/**
 * Facade class to be invoked by Ajax clients to manage CCLs.
 */
public class CustomerCreatedListAjaxFacade implements Serializable {

	private static final long serialVersionUID = 1L;

	private final static Category LOGGER = LoggerFactory.getInstance(CustomerCreatedListAjaxFacade.class);
	
	protected CustomerCreatedListAjaxFacade() {
	}
	
	public String createList(HttpSession session, String name) throws AjaxFacadeException, FDResourceException {
		LOGGER.debug("createList() called with name="+name);
		FDUserI user = getUser(session, FDUserI.RECOGNIZED);
		name = name.trim();
		if (name.equals("")) {
			throw new NameEmpty();
		} else if (name.length() > FDCustomerCreatedList.MAX_NAME_LENGTH) {
			throw new NameTooLong();
		}
		
		try {
		   FDListManager.createCustomerCreatedList(user, name);
  		   user.invalidateCache(); // Update CCL experience metrics
		} catch (FDCustomerListExistsException ex) {
			throw new NameExists();
		}
		
		return name;
	}
	
	
	public void deleteList(HttpSession session, String listName) throws FDResourceException, AjaxFacadeException {		
		LOGGER.debug("deleteList() called with name="+listName);
		FDUserI user = getUser(session, FDUserI.SIGNED_IN);
		FDListManager.deleteCustomerCreatedList(user,listName);
		user.invalidateCache(); // Update CCL experience metrics
	}

	/**
	 *  Return the names of the current user's customer created lists.
	 *  
	 *  @return the names of the current user's lists
	 *  @throws FDResourceException if there are resource acquisition problems
	 *  @throws FDCustomerListExistsException if the user does not have lists (?)
	 *  @throws UnauthorizedRequest if the user is not authorized
	 */
	public String[] getListNames(HttpSession session) throws FDResourceException, FDCustomerListExistsException, AjaxFacadeException {
		LOGGER.debug("getListNames() called");
		FDUserI user = getUser(session, FDUserI.RECOGNIZED);
		List lists = user.getCustomerCreatedListInfos();
		String [] result = new String[lists.size()];
		int c = 0;
		for (Iterator i = lists.iterator(); i.hasNext();) {
			result[c++] = (((FDCustomerCreatedList) i.next()).getName());
		}
	    return result;
	}
	
	/**
	 *  Return the names of the current user's customer created lists,
	 *  with the number of items in each list as well.
	 *  The returned lists are sorted by the list names.
	 *  
	 *  @return a 2-dimensional array, where each entry in the outer array
	 *          has two values: the name of the list, and the number of items
	 *          in that list
	 *  @throws FDResourceException if there are resource acquisition problems
	 *  @throws FDCustomerListExistsException if the user does not have lists (?)
	 * @throws UnauthorizedRequest 
	 */
	public CustomerCreatedListNames getListNamesWithItemCount(HttpSession session) throws FDResourceException,
	                                                     FDCustomerListExistsException, AjaxFacadeException {
		LOGGER.debug("getListsWithItemCount() called");
		FDUserI user = getUser(session, FDUserI.RECOGNIZED);
	    List lists = user.getCustomerCreatedListInfos();
	    if (lists == null) {
	    	throw new FDResourceException("Could not retrieve lists");
	    }
	    TreeSet sorted = new TreeSet(FDCustomerCreatedList.getNameComparator());
	    sorted.addAll(lists);
	    
	    return getCustomerCreatedListNamesFromSet(sorted);
	}
		
	/**
	 *  Return the names of the current user's customer created lists, except one,
	 *  with the number of items in each list as well.
	 *  The returned lists are sorted by the list names.
	 *  
	 *  @param srcListName omit the list of this name from the array of lists
	 *         returned. if this is null, return all lists.
	 *  @return a 2-dimensional array, where each entry in the outer array
	 *          has two values: the name of the list, and the number of items
	 *          in that list
	 *  @throws FDResourceException if there are resource acquisition problems
	 *  @throws FDCustomerListExistsException if the user does not have lists (?)
	 * @throws UnauthorizedRequest 
	 */
	public CustomerCreatedListNames getListNamesWithItemCount(HttpSession session, String srcListName) throws FDResourceException,
    							   									       FDCustomerListExistsException, AjaxFacadeException {
		LOGGER.debug("getListsWithItemCount() called with "+srcListName);
		FDUserI user = getUser(session, FDUserI.RECOGNIZED);
		if (srcListName==null) {
			return getListNamesWithItemCount(session);
		}
		List filteredLists = new ArrayList();
	    
		List allLists      = user.getCustomerCreatedListInfos();

	    if (allLists == null) {
	    	throw new FDResourceException("Could not retrieve lists");
	    } else {
		    for(int j = 0; j < allLists.size(); j++) {
		    	FDCustomerCreatedList list = (FDCustomerCreatedList) allLists.get(j);
		    	if(!list.getName().equals(srcListName)) {
		    		filteredLists.add(list);
		    	}
		    }
	    }
	    TreeSet sorted = new TreeSet(FDCustomerCreatedList.getNameComparator());
	    sorted.addAll(filteredLists);
	    
	    return getCustomerCreatedListNamesFromSet(sorted);
	}

	
	public String renameList(HttpSession session, String oldList, String newList) throws NameEmpty, NameExists, FDResourceException, AjaxFacadeException { 
		LOGGER.debug("renameList called:"+oldList+"->"+newList);
		FDUserI user = getUser(session, FDUserI.SIGNED_IN);
		oldList = oldList.trim();
		if (oldList.equals("")) {
			throw new NameEmpty();
		}
		newList = newList.trim();
		if (newList.equals("")) {
			throw new NameEmpty();
		} else if (newList.length() > FDCustomerCreatedList.MAX_NAME_LENGTH) {
			throw new NameTooLong();
		}

		try {
			FDListManager.renameCustomerCreatedList(user.getIdentity(), oldList, newList);
		}
		catch (FDCustomerListExistsException e) {
			throw new NameExists();
		}
		
		QuickCartCache.invalidateOnChange(session, QuickCart.PRODUCT_TYPE_CCL, null, oldList);
		user.invalidateCache(); // Update CCL experience metrics
		return newList;
	}
	
	
	/** Remove an item from a list.
	 * 
	 * @param session 
	 * @param lineId the line Id of the item (which is unique across all items)
	 * @throws FDResourceException
	 */
	public void removeLineItem(HttpSession session, String lineId) throws FDResourceException, AjaxFacadeException {
		FDUserI user = getUser(session, FDUserI.SIGNED_IN);
		FDListManager.removeCustomerListItem(user, new PrimaryKey(lineId));
		QuickCartCache.invalidateOnChange(session, QuickCart.PRODUCT_TYPE_CCL,null,null);

		user.invalidateCache();
	}

	
	/** Add item selection to the list.
	 * 
	 * @param listName name of CCL
	 * @param items
	 * @return list id corresponding to ListName
	 * @throws FDResourceException
	 * @throws IllegalStateException 
	 * @throws FDSkuNotFoundException 
	 * @throws NameEmpty no such list
	 * @throws UnauthorizedRequest 
	 */
	public FDCustomerCreatedList addItemsToList(HttpSession session, String listName, FDCustomerCreatedList items) throws FDResourceException, AjaxFacadeException, FDSkuNotFoundException, IllegalStateException {
		LOGGER.debug("addItemToList called:"+listName);
		
		FDUserI user = getUser(session, FDUserI.RECOGNIZED);
		
		// load list
		FDCustomerList list = FDListManager.getCustomerList(user.getIdentity(), EnumCustomerListType.CC_LIST, listName);
		
	    if (list == null) throw new NameEmpty();

		FDCustomerCreatedList ccl = (FDCustomerCreatedList)list;
		for(Iterator I = items.getLineItems().iterator(); I.hasNext(); ) {
			FDCustomerProductListLineItem item = (FDCustomerProductListLineItem)I.next();
			ccl.addLineItem(item);
			// LINE ID = item.getPK().getId();
		}

		// storeList
		FDListManager.storeCustomerList(ccl);
		
		// reload list for PKs
		list = FDListManager.getCustomerList(user.getIdentity(), EnumCustomerListType.CC_LIST, listName);
		Set storedItems = new LinkedHashSet();
		storedItems.addAll(list.getLineItems());
		storedItems.retainAll(items.getLineItems());
		
		// Create a fake list containing needed list items
		FDCustomerCreatedList resultList = new FDCustomerCreatedList();
		resultList.setId(list.getId());
		for (Iterator I = storedItems.iterator(); I.hasNext(); ) {
			FDCustomerProductListLineItem it = (FDCustomerProductListLineItem) I.next();
			// don't forget to call this to initialize product dependent properties of FDCustomerProductListLineItem
			//   such as categoryID and productID
			resultList.addLineItem(it);
		}


		QuickCartCache.invalidateOnChange(session, QuickCart.PRODUCT_TYPE_CCL, null, listName);
		user.invalidateCache(); // Update CCL experience metrics

	    return resultList;
	}



	/**
	 * This function allows to add recent cart items to shopping list. Called from ccl.js
	 * 
	 * @author segabor
	 * @param session
	 * @param listName name of CCL
	 * @return
	 * @throws FDResourceException 
	 * @throws AjaxFacadeException 
	 * @throws IllegalStateException 
	 * @throws FDSkuNotFoundException 
	 */
	public FDCustomerCreatedList addRecentItemsToList(HttpSession session, String listName) throws FDResourceException, FDSkuNotFoundException, IllegalStateException, AjaxFacadeException {
		FDUserI user = getUser(session, FDUserI.RECOGNIZED);
		FDCustomerCreatedList items = new FDCustomerCreatedList();
		
		// item transformation
		FDCartModel cart = user.getShoppingCart();
		Iterator it = cart.getRecentOrderLines().iterator();
		while (it.hasNext()) {
			FDProductSelectionI item = (FDProductSelectionI) it.next();
			items.addLineItem(new FDCustomerProductListLineItem(item));
		}
		
		return addItemsToList(session, listName, items);
	}
	
	
	
	public static class NameExists extends AjaxFacadeException {
		private static final long serialVersionUID = 8944714939658910310L;

		public NameExists() {
			super();
		}
	}
	
	public static class NameTooLong extends AjaxFacadeException {


		private static final long serialVersionUID = 7682369258365639969L;

		public NameTooLong() {
			super();
		}
	}

	public static class NameEmpty extends AjaxFacadeException {
		private static final long serialVersionUID = 8723154537493154425L;

		public NameEmpty() {
			super();
		}
	}
	
	public static class UnauthorizedRequest extends AjaxFacadeException {

		private static final long serialVersionUID = 7978237143245047030L;

		public UnauthorizedRequest() {
			super();
		}
	}
	
	public static class SessionTimeout extends AjaxFacadeException {
		
		private static final long serialVersionUID = 2153027305630377736L;

		public SessionTimeout() {
			super();
		}
	}
	
	public static class InternalError extends AjaxFacadeException {
		private static final long serialVersionUID = 8723154537493154427L;

		public InternalError() {
			super();
		}
	}

	public static CustomerCreatedListAjaxFacade create() {
		if (FDStoreProperties.isCclAjaxDebugFacade()) {
			return new CustomerCreatedListAjaxFacadeDebugWrapper();
		}
		return new CustomerCreatedListAjaxFacade();
	}
	
	public static class CustomerCreatedListNames {
		private String[][] listNames;
		private String mostRecentList;

		public String[][] getListNames() {
			return listNames;
		}
		
		public void setListNames(String[][] listNames) {
			this.listNames = listNames;
		}
		
		public String getMostRecentList() {
			return mostRecentList;
		}
		
		public void setMostRecentList(String mostRecentList) {
			this.mostRecentList = mostRecentList;
		}
	}

	private CustomerCreatedListNames getCustomerCreatedListNamesFromSet(Set sorted) {
	    String [][] listNames = new String[sorted.size()][2];
	    int c = 0;
	    String mostRecentList = null;
	    Date mostRecentMod = null;
	    for(Iterator i = sorted.iterator(); i.hasNext(); ++c) {
	    	FDCustomerCreatedList clist = (FDCustomerCreatedList) i.next();	    	
	    	listNames[c][0] = clist.getName();	    	
	    	listNames[c][1] = Integer.toString(((FDCustomerCreatedListInfo) clist).getCount());
	    	if (mostRecentList == null || clist.getModificationDate().compareTo(mostRecentMod) > 0) {
	    		mostRecentList = clist.getName();
	    		mostRecentMod = clist.getModificationDate();
	    	}
	    }
	    
	    CustomerCreatedListNames result = new CustomerCreatedListNames();
	    result.setListNames(listNames);
	    result.setMostRecentList(mostRecentList);
	    
	    return result;		
	}
	
	private static FDUserI getUser(HttpSession session, int authLevel) throws UnauthorizedRequest, SessionTimeout {
		if ("true".equals(session.getAttribute("timeout"))) {
			LOGGER.debug("Invalidating session because of timeout");
			session.invalidate();
			throw new SessionTimeout();
		}
		
		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER); 
		if (user == null || user.getLevel() < authLevel) {
			throw new UnauthorizedRequest();
		}
		return user;
	}

	public static void setJsonRpcDebug(JSONRPCBridge bridge, boolean debug) {
		try {
			if (debug) {
					LogManager.getLogManager().readConfiguration(ClassLoader.getSystemResourceAsStream("jsonrpc-logging.properties"));
			} else {
					LogManager.getLogManager().readConfiguration();
			}
		} catch (Exception e) {
			LOGGER.warn("Unable to set JSON-RPC logging properties", e);
		}
		bridge.setDebug(debug);
	}
}
