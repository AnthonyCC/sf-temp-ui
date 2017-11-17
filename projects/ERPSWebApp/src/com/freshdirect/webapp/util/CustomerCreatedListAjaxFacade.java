package com.freshdirect.webapp.util;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.LogManager;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.cache.EhCacheUtil;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.QuickCart;
import com.freshdirect.fdstore.customer.ejb.EnumCustomerListType;
import com.freshdirect.fdstore.lists.FDCustomerCreatedList;
import com.freshdirect.fdstore.lists.FDCustomerList;
import com.freshdirect.fdstore.lists.FDCustomerListExistsException;
import com.freshdirect.fdstore.lists.FDCustomerListInfo;
import com.freshdirect.fdstore.lists.FDCustomerListItem;
import com.freshdirect.fdstore.lists.FDCustomerProductListLineItem;
import com.freshdirect.fdstore.lists.FDListManager;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.metaparadigm.jsonrpc.JSONRPCBridge;



/**
 * Facade class to be invoked by Ajax clients to manage CCLs.
 */
@Deprecated
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
  		   
  			//invalidate quickshop cache
  			EhCacheUtil.removeFromCache(EhCacheUtil.QS_SHOP_FROM_LISTS_CACHE_NAME, user.getIdentity().getErpCustomerPK());

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
		
		//invalidate quickshop cache
		EhCacheUtil.removeFromCache(EhCacheUtil.QS_SHOP_FROM_LISTS_CACHE_NAME, user.getIdentity().getErpCustomerPK());

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

		List<FDCustomerListInfo> lists = user.getCustomerCreatedListInfos();
		String [] result = new String[lists.size()];

		int c = 0;
		for (FDCustomerListInfo l : lists) {
			result[c++] = l.getName();
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
	public CustomerListNames getListNamesWithItemCount(HttpSession session) throws FDResourceException,
	                                                     FDCustomerListExistsException, AjaxFacadeException {
		LOGGER.debug("getListsWithItemCount() called");
		FDUserI user = getUser(session, FDUserI.RECOGNIZED);
	    // get ccl lists
	    List<FDCustomerListInfo> lists = user.getCustomerCreatedListInfos();
	    if (lists == null) {
	    	throw new FDResourceException("Could not retrieve lists");
	    }
	    lists = new ArrayList<FDCustomerListInfo>( lists );
	    
	    TreeSet<FDCustomerListInfo> sorted = new TreeSet<FDCustomerListInfo>(new Comparator<FDCustomerListInfo>() {
			public int compare(FDCustomerListInfo l1, FDCustomerListInfo l2) {
				return l1.getName().compareToIgnoreCase(l2.getName()) < 0 ? -1 : 1;
			}
		});
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
	public CustomerListNames getListNamesWithItemCount(HttpSession session, String srcListName) throws FDResourceException,
    							   									       FDCustomerListExistsException, AjaxFacadeException {
		LOGGER.debug("getListsWithItemCount() called with "+srcListName);
		FDUserI user = getUser(session, FDUserI.RECOGNIZED);
		if (srcListName==null) {
			return getListNamesWithItemCount(session);
		}
		List<FDCustomerListInfo> filteredLists = new ArrayList<FDCustomerListInfo>();
	    
		List<FDCustomerListInfo> allLists      = user.getCustomerCreatedListInfos();

	    if (allLists == null) {
	    	throw new FDResourceException("Could not retrieve lists");
	    } else {
		    for(int j = 0; j < allLists.size(); j++) {
		    	FDCustomerListInfo list = (FDCustomerListInfo) allLists.get(j);
		    	if(!list.getName().equals(srcListName)) {
		    		filteredLists.add(list);
		    	}
		    }
	    }

	    TreeSet<FDCustomerListInfo> sorted = new TreeSet<FDCustomerListInfo>(new Comparator<FDCustomerListInfo>() {
			public int compare(FDCustomerListInfo l1, FDCustomerListInfo l2) {
				return l1.getName().compareToIgnoreCase(l2.getName()) < 0 ? -1 : 1;
			}
		});
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
		
		//invalidate quickshop cache
		EhCacheUtil.removeFromCache(EhCacheUtil.QS_SHOP_FROM_LISTS_CACHE_NAME, user.getIdentity().getErpCustomerPK());

		return newList;
	}
	
	
	/**
	 * Renames a Standing Order List
	 * 
	 * @param session
	 * @param oldList
	 * @param newList
	 */
	public String renameSOList(HttpSession session, String oldList, String newList) throws NameEmpty, NameExists, FDResourceException, AjaxFacadeException { 
		LOGGER.debug("renameSOList called:"+oldList+"->"+newList);
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
			FDActionInfo info = AccountActivityUtil.getActionInfo(session);
			info.setNote("Standing Order Renamed (from " + oldList + " to " + newList + ")");
			FDListManager.renameCustomerList(info, EnumCustomerListType.SO, oldList, newList);
		}
		catch (FDCustomerListExistsException e) {
			throw new NameExists();
		}
		
		QuickCartCache.invalidateOnChange(session, QuickCart.PRODUCT_TYPE_SO, null, oldList);
		user.invalidateCache(); // Update CCL experience metrics
		
		//invalidate quickshop cache
		EhCacheUtil.removeFromCache(EhCacheUtil.QS_SHOP_FROM_LISTS_CACHE_NAME, user.getIdentity().getErpCustomerPK());

		return newList;
	}

	/**
	 * Processes fields from Standing Order Help Popup
	 * 
	 * @param session
	 * @param oldList
	 * @param newList
	 */
	public void processHelpSo(HttpSession session, String entityLabel, String entityId, String sourcePage, String custName, String companyName, String custEmail, String custPhone, String custIssue) throws NameEmpty, NameExists, FDResourceException, AjaxFacadeException { 
		LOGGER.debug(String.format("processHelpSo called: %s; %s; %s; %s; %s; %s; %s; %s", entityLabel, entityId, sourcePage, custName, companyName, custEmail, custPhone, custIssue));
		FDUserI user = getUser(session, FDUserI.SIGNED_IN);
		try {
			String userId = user.getUserId();
			String message = String.format(
				"<table cellspacing=\"0\" border=\"0\" cellpadding=\"5\" width=\"100%%\">\r\n" + 
				"<tr style=\"background-color: #996699;color: #ffffff;font-weight: bold\"><td style=\"text-align: right\">Request Context</td><td></td></tr>\r\n" + 
				"<tr style=\"background-color: #f6f6f6\"><td width=\"200\" style=\"color: #996699;text-align: right;font-weight: bold\">Source Page</td><td>%s</td></tr>\r\n" + 
				"<tr style=\"background-color: #ffffff\"><td style=\"color: #996699;text-align: right;font-weight: bold\">Customer</td><td>%s</td></tr>\r\n", 
				StringUtil.escapeHTML(sourcePage), userId); 

			if (entityLabel != null){
				message += String.format(
						"<tr style=\"background-color: #f6f6f6\"><td style=\"color: #996699;text-align: right;font-weight: bold\">%s</td><td>%s</td></tr>\r\n", 
						StringUtil.escapeHTML(entityLabel), StringUtil.escapeHTML(entityId));
			}

			message += String.format(
				"<tr style=\"background-color: #ffffff\"><td colspan=\"2\">&nbsp;</td></tr>\r\n" + 
				"<tr style=\"background-color: #996699;color: #ffffff;font-weight: bold\"><td style=\"text-align: right\">User Edited Fields</td><td></td></tr>\r\n" + 
				"<tr style=\"background-color: #f6f6f6\"><td style=\"color: #996699;text-align: right;font-weight: bold\">Name</td><td>%s</td></tr>\r\n" + 
				"<tr style=\"background-color: #ffffff\"><td style=\"color: #996699;text-align: right;font-weight: bold\">Company</td><td>%s</td></tr>\r\n" + 
				"<tr style=\"background-color: #f6f6f6\"><td style=\"color: #996699;text-align: right;font-weight: bold\">Email</td><td>%s</td></tr>\r\n" + 
				"<tr style=\"background-color: #ffffff\"><td style=\"color: #996699;text-align: right;font-weight: bold\">Phone</td><td>%s</td></tr>\r\n" + 
				"<tr style=\"background-color: #f6f6f6\"><td style=\"color: #996699;text-align: right;font-weight: bold; vertical-align:text-top\">Issue</td><td><pre>%s</pre></td></tr>\r\n" + 
				"</table>",		
				StringUtil.escapeHTML(custName), 
				StringUtil.escapeHTML(companyName),
				StringUtil.escapeHTML(custEmail), 
				StringUtil.escapeHTML(custPhone), 
				StringUtil.escapeHTML(custIssue));
			
			//new ErpMailSender().sendMail(FDStoreProperties.getStandingOrderCsEmail(), FDStoreProperties.getStandingOrderCsEmail(), "", "Need Help Form - "+ userId, message, true, "");

		}catch (Exception e) {
			LOGGER.warn("Error Sending Need Help Standing Order Email: ", e);
		}
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

		//invalidate quickshop cache
		EhCacheUtil.removeFromCache(EhCacheUtil.QS_SHOP_FROM_LISTS_CACHE_NAME, user.getIdentity().getErpCustomerPK());

		user.invalidateCache();
	}

	
	/** Remove an item from a list.
	 * 
	 * @param session 
	 * @param lineId the line Id of the item (which is unique across all items)
	 * @throws FDResourceException
	 */
	public void removeSOLineItem(HttpSession session, String lineId) throws FDResourceException, AjaxFacadeException {
		FDUserI user = getUser(session, FDUserI.SIGNED_IN);
		FDListManager.removeCustomerListItem(user, new PrimaryKey(lineId));
		QuickCartCache.invalidateOnChange(session, QuickCart.PRODUCT_TYPE_SO,null,null);

		user.invalidateCache();
	}

	
	/** Add item selection to the list.
	 * 
	 * @param listName name of CCL
	 * @param typeName CCL or SO
	 * @param items
	 * 
	 * @return list id corresponding to ListName
	 * @throws FDResourceException
	 * @throws IllegalStateException 
	 * @throws FDSkuNotFoundException 
	 * @throws NameEmpty no such list
	 * @throws UnauthorizedRequest 
	 */
	public FDCustomerCreatedList addItemsToList(HttpSession session, String listName, String typeName, FDCustomerCreatedList items) throws FDResourceException, AjaxFacadeException, FDSkuNotFoundException, IllegalStateException {
		LOGGER.debug("addItemToList called: " + listName + " type: " + typeName);
		
		FDUserI user = getUser(session, FDUserI.RECOGNIZED);
		
		// determine list type
		EnumCustomerListType type = EnumCustomerListType.getEnum(typeName);
		if (type == null)
			type = EnumCustomerListType.CC_LIST; // default case

		// load list
		FDCustomerList list = FDListManager.getCustomerList(user.getIdentity(), type, listName);
	    if (list == null)
	    	throw new NameEmpty();

		for (FDCustomerListItem item  : items.getLineItems()) {
			list.addLineItem(item);
		}

		// storeList
		FDListManager.storeCustomerList(list);

		// reload list for PKs
		list = FDListManager.getCustomerList(user.getIdentity(), type, listName);

		Set<FDCustomerListItem> storedItems = new LinkedHashSet<FDCustomerListItem>();
		storedItems.addAll(list.getLineItems());
		storedItems.retainAll(items.getLineItems());
		
		// Create a fake list containing needed list items
		FDCustomerCreatedList resultList = new FDCustomerCreatedList();
		resultList.setId(list.getId());
		
		for (FDCustomerListItem it : storedItems) {
			// don't forget to call this to initialize product dependent properties of FDCustomerProductListLineItem
			//   such as categoryID and productID
			resultList.addLineItem(it);
		}

		QuickCartCache.invalidateOnChange(session, EnumCustomerListType.CC_LIST == type ? QuickCart.PRODUCT_TYPE_CCL : QuickCart.PRODUCT_TYPE_SO, null, listName);
		user.invalidateCache(); // Update CCL or SO experience metrics
		
		//invalidate quickshop cache
		EhCacheUtil.removeFromCache(EhCacheUtil.QS_SHOP_FROM_LISTS_CACHE_NAME, user.getIdentity().getErpCustomerPK());

	    return resultList;
	}



	/**
	 * This function allows to add recent cart items to shopping list. Called from ccl.js
	 * 
	 * @param session
	 * @param listName name
	 * @param typeName CCL or SO
	 * 
	 * @return
	 * @throws FDResourceException 
	 * @throws AjaxFacadeException 
	 * @throws IllegalStateException 
	 * @throws FDSkuNotFoundException 
	 */
	public FDCustomerCreatedList addRecentItemsToList(HttpSession session, String listName, String typeName) throws FDResourceException, FDSkuNotFoundException, IllegalStateException, AjaxFacadeException {
		FDUserI user = getUser(session, FDUserI.RECOGNIZED);
		FDCustomerCreatedList items = new FDCustomerCreatedList();

		// item transformation
		FDCartModel cart = user.getShoppingCart();

		for (FDCartLineI item : cart.getRecentOrderLines()) {
			items.addLineItem(new FDCustomerProductListLineItem(item));
		}

		return addItemsToList(session, listName, typeName, items);
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
	
	
	/**
	 * Transport type
	 */
	public static class CustomerListNames {
		/**
		 * List infos
		 * 
		 * [i][0] = list name
		 * [i][1] = count
		 * [i][2] = type, see {@link EnumCustomerListType} (CCL or SO)
		 * 
		 */
		private String[][] listNames;

		/**
		 * Name of most recent list
		 */
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

	private CustomerListNames getCustomerCreatedListNamesFromSet(Set<FDCustomerListInfo> sorted) {
	    String [][] listNames = new String[sorted.size()][3];
	    int c = 0;
	    String mostRecentList = null;
	    Date mostRecentMod = null;

	    for (FDCustomerListInfo clist : sorted) {
	    	listNames[c][0] = clist.getName();
	    	listNames[c][1] = Integer.toString(clist.getCount());
	    	listNames[c][2] = clist.getType().getName();
	    	if (mostRecentList == null || clist.getModificationDate().compareTo(mostRecentMod) > 0) {
	    		mostRecentList = clist.getName();
	    		mostRecentMod = clist.getModificationDate();
	    	}
	    	
	    	c++;
	    }
	    
	    CustomerListNames result = new CustomerListNames();
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
