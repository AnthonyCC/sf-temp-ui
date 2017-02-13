package com.freshdirect.webapp.ajax.reorder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.cache.EhCacheUtil;
import com.freshdirect.fdstore.content.EnumQuickShopFilteringValue;
import com.freshdirect.fdstore.content.FilteringFlowResult;
import com.freshdirect.fdstore.content.FilteringMenuItem;
import com.freshdirect.fdstore.content.FilteringSortingItem;
import com.freshdirect.fdstore.content.FilteringValue;
import com.freshdirect.fdstore.coremetrics.CmContextUtility;
import com.freshdirect.fdstore.customer.FDProductSelectionI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.OrderLineUtil;
import com.freshdirect.fdstore.customer.ejb.EnumCustomerListType;
import com.freshdirect.fdstore.lists.FDCustomerCreatedList;
import com.freshdirect.fdstore.lists.FDCustomerList;
import com.freshdirect.fdstore.lists.FDCustomerListInfo;
import com.freshdirect.fdstore.lists.FDCustomerListItem;
import com.freshdirect.fdstore.lists.FDCustomerProductListLineItem;
import com.freshdirect.fdstore.lists.FDListManager;
import com.freshdirect.fdstore.util.FilteringNavigator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.cart.data.AddToCartItem;
import com.freshdirect.webapp.ajax.product.ProductDetailPopulator;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItem;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItemWrapper;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopListDetails;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopPagerValues;
import com.freshdirect.webapp.ajax.reorder.data.EnumQuickShopTab;
import com.freshdirect.webapp.ajax.reorder.data.QuickShopListRequestObject;
import com.freshdirect.webapp.ajax.reorder.data.QuickShopReturnValue;
import com.freshdirect.webapp.ajax.reorder.service.QuickShopSearchService;
import com.freshdirect.webapp.ajax.shoppinglist.ShoppingListServlet;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class QSFromListFilterServlet extends QuickShopServlet {

	private static final long serialVersionUID = 6364193380447283594L;

	private static final Logger LOG = LoggerFactory.getInstance(QSFromListFilterServlet.class);
	
	//create filter
	private static final Set<FilteringValue> filters = new HashSet<FilteringValue>();
	static {
		filters.add(EnumQuickShopFilteringValue.DEPT);
		filters.add(EnumQuickShopFilteringValue.GLUTEN_FREE);
		filters.add(EnumQuickShopFilteringValue.KOSHER);
		filters.add(EnumQuickShopFilteringValue.LOCAL);
		filters.add(EnumQuickShopFilteringValue.ORGANIC);
		filters.add(EnumQuickShopFilteringValue.ON_SALE);
		filters.add(EnumQuickShopFilteringValue.YOUR_LISTS);
		filters.add(EnumQuickShopFilteringValue.BRAND);
	}
	
	@Override
	protected boolean synchronizeOnUser() {
		return false; //no need to synchronize
	}
	
	@Override
	protected QuickShopReturnValue process( FDUserI user, HttpServletRequest request, QuickShopListRequestObject requestData ) throws HttpErrorResponse {
		
		//transform request data
		FilteringNavigator nav = requestData.convertToFilteringNavigator();
		FilteringFlowResult<QuickShopLineItemWrapper> result = null;

		try {
			
			List<QuickShopLineItemWrapper> items = EhCacheUtil.getListFromCache(EhCacheUtil.QS_SHOP_FROM_LISTS_CACHE_NAME, user.getIdentity().getErpCustomerPK());
			
			if(items==null){
				items = QuickShopHelper.getWrappedCustomerCreatedLists(user, EnumQuickShopTab.CUSTOMER_LISTS);
				if(!items.isEmpty()){
					EhCacheUtil.putListToCache(EhCacheUtil.QS_SHOP_FROM_LISTS_CACHE_NAME, user.getIdentity().getErpCustomerPK(), new ArrayList<QuickShopLineItemWrapper>(items));					
				}
			}else{
				items = new ArrayList<QuickShopLineItemWrapper>(items);
			}
            QuickShopSearchService.defaultService().search(nav.getSearchTerm(), items, user, request);
			//set the default list id on the first page load OR if the current selected list is just deleted or empty
			selectList(user, nav);
			
			List<FilteringSortingItem<QuickShopLineItemWrapper>> filterItems = prepareForFiltering(items);
			
			QuickShopFilterImpl filter = new QuickShopFilterImpl(nav, user, filters, filterItems, QuickShopHelper.getActiveReplacements( request.getSession() ), null, null);
			result = filter.doFlow(nav, filterItems);
			
			// post-process
			QuickShopHelper.postProcessPopulate( user, result, request.getSession() );

		} catch (FDResourceException e) {
			returnHttpError( 500, "An error occured while working on the user's shopping lists: ", e );
		}
		
		// prepare return values
		QuickShopListDetails listDetails = null;
		if(result.getItems()!=null && !result.getItems().isEmpty()){
			QuickShopLineItemWrapper firstNode = result.getItems().get(0).getNode();
			listDetails = new QuickShopListDetails(firstNode.getRecipeId(), firstNode.getRecipeName(), firstNode.isRecipeAlive());			
		}

		QuickShopReturnValue responseData = new QuickShopReturnValue(unwrapResult(createPage(user, request, requestData, result.getItems())),
														result.getMenu(), 
														new QuickShopPagerValues(requestData.getPageSize(), result.getItems().size(), requestData.getActivePage()),
														generateSorter(nav),
														nav.getSearchTerm(),
														listDetails);
		
		postPopulateEmptyCustomerListsToYourLists(responseData.getMenu(), user);
		
		// sorting menu items where needed
		QuickShopMenuOrderUtil.sortMenuItems(responseData.getMenu());
		
		// Generate coremetrics 'element' tags for the menu - selected filters
		// [APPDEV-4558]
		if (CmContextUtility.isCoremetricsAvailable(user)) {
			generateCoremetricsElementTags( responseData, result.getMenu(), "quickshop | shopping_lists" );
		}
		
		return responseData;
	}
	
	private void postPopulateEmptyCustomerListsToYourLists(Map<String, Map<FilteringValue, List<FilteringMenuItem>>> menu, FDUserI user) throws HttpErrorResponse {
		try {
			Map<FilteringValue, List<FilteringMenuItem>> yourLists = menu.get(EnumQuickShopFilteringValue.YOUR_LISTS.getParent());
			if (yourLists == null) {
				yourLists = new HashMap<FilteringValue, List<FilteringMenuItem>>();
				List<FilteringMenuItem> emptyYourListsMenuItems = new ArrayList<FilteringMenuItem>();
				yourLists.put(EnumQuickShopFilteringValue.YOUR_LISTS, emptyYourListsMenuItems);
			}
			List<FilteringMenuItem> yourListsMenuItems = yourLists.get(EnumQuickShopFilteringValue.YOUR_LISTS);
			List<FDCustomerCreatedList> customerCreatedLists = FDListManager.getCustomerCreatedLists(user);
			for (FDCustomerCreatedList list : customerCreatedLists) {
				if (list.getCount() == 0) {
					yourListsMenuItems.add(new FilteringMenuItem(list.getName(), null, 0, EnumQuickShopFilteringValue.YOUR_LISTS));
				}
			}
		} catch (FDResourceException e) {
			returnHttpError( 500, "An error occured while working on the user's shopping lists: ", e );
		}
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
		
		// Parse request data
		AddToCartItem reqData = parseRequestData(request, AddToCartItem.class);

		String listId = reqData.getListId();
		String lineId = reqData.getLineId();

		if (listId == null) {
			returnHttpError(400, "No list id provided"); // 400 Bad Request
		}

		if (lineId == null) {
			returnHttpError(400, "No list line id provided"); // 400 Bad Request
		}

		// Get the list
		FDCustomerList list = null;
		try {
			list = FDListManager.getCustomerListById(user.getIdentity(), EnumCustomerListType.CC_LIST, listId);
		} catch (FDResourceException e) {
			returnHttpError(500, "System error (FDResourceException)", e); // 500 internal server error
		}
		
		//edit or delete item
		FDCustomerProductListLineItem newItem = null;
		boolean shoppingListPageRefreshNeeded = false;
		List<FDCustomerListItem> cclItems = list.getLineItems();
		
		if (cclItems != null) {
			try {
				
				Iterator<FDProductSelectionI> it = OrderLineUtil.getValidProductSelectionsFromCCLItems(cclItems).iterator();
				
				while (it.hasNext()) {
					
					FDProductSelectionI productSelection = it.next();
					
					if (lineId.equals(productSelection.getCustomerListLineId())) {
						
						// item found, delete it first
						deleteLineFromList(cclItems, listId, lineId, user.getIdentity().getErpCustomerPK());
						
						shoppingListPageRefreshNeeded=false; // need some cleanup
						if (!reqData.isDeleteItem()) { // if the request was an update then create the new data and put it back to the list
							
							newItem = ShoppingListServlet.createListLineItem(reqData, reqData.getRecipeId(), true);
							cclItems.add(newItem);
							
							 // if its an edit, then we wont refresh the full list
							shoppingListPageRefreshNeeded=false;
							
							// remove temp config from session if any
							HttpSession session = request.getSession();
							@SuppressWarnings("unchecked")
							Map<String, QuickShopLineItem> tempConfigs = (Map<String, QuickShopLineItem>) session.getAttribute(SessionName.SESSION_QS_CONFIG_REPLACEMENTS);
							if(tempConfigs!=null){
								tempConfigs.remove(reqData.getAtcItemId());								
							}
							
						}
						else{
							if(cclItems.size() == 0){
								shoppingListPageRefreshNeeded = true;
							}
						}
						
						// store the list and break the iteration
						FDListManager.storeCustomerList(list);
						break;
					}
				}
				user.invalidateCache();
			} catch (FDResourceException e) {
				returnHttpError(500, "System error (FDResourceException)", e); // 500 internal server error
			} catch (IllegalStateException e) {
				returnHttpError(500, "System error (FDResourceException)", e); // 500 internal server error
			}
		}
		
		// prepare the return values
		Map<String, Object> returnWrapper = new HashMap<String, Object>();
		returnWrapper.put("shoppingListPageRefreshNeeded", shoppingListPageRefreshNeeded);
		if(newItem!=null){
			//let's try to convert new item into QuickShopLineItem
			QuickShopLineItemWrapper rv;
			try {
				rv = QuickShopHelper.createItemCore(newItem.convertToSelection(), list, null, user, EnumQuickShopTab.CUSTOMER_LISTS);
				if(rv!=null){
					QuickShopLineItem item = rv.getItem();
					ProductDetailPopulator.postProcessPopulate(user, item, item.getSkuCode());
					returnWrapper.put("updateItem", rv.getItem());							
				}
			} catch (FDException e) {
				LOG.error("Cannot convert new item to selection. e: " + e);
			}
		}
		
		writeResponseData(response, returnWrapper);
	}
	
	private static void deleteLineFromList(List<FDCustomerListItem> cclItems, String listId, String lineId, String userId) {
		Iterator<FDCustomerListItem> it = cclItems.iterator();
		while(it.hasNext()){
			FDCustomerListItem item = it.next();
			if(item.getPK()!=null && lineId.equals(item.getPK().getId())){
				it.remove();
				break;
			}
		}
		
		//invalidate cache entry TODO: maybe enough to reload the actual list
		EhCacheUtil.removeFromCache(EhCacheUtil.QS_SHOP_FROM_LISTS_CACHE_NAME, userId);
		
	}
	
	/**
	 * @param user
	 * @param nav
	 * @throws FDResourceException
	 * 
	 * Method checks if we need to select a list for the user
	 */
	private static void selectList(FDUserI user, FilteringNavigator nav) throws FDResourceException {
		
		// select a list if ...
		if(nav.getFilterValues().get(EnumQuickShopFilteringValue.YOUR_LISTS)==null){ //there is no list selected
			findAListToBeSelected(nav, user);
		}else{ //OR if the current selected list is just deleted or empty
			boolean setToDefault = false;
			boolean foundList = false;
			Object selected = nav.getFilterValues().get(EnumQuickShopFilteringValue.YOUR_LISTS).get(0);
			List<FDCustomerListInfo> lists = user.getCustomerCreatedListInfos();
			for(FDCustomerListInfo info : lists){
				if(info.getId().equals(selected)){
					foundList = true;
					if(info.getCount()==0){
						setToDefault = true;							
					}
					break;
				}
			}
			if(!foundList || setToDefault){
				findAListToBeSelected(nav, user);
			}
			
		}
	}
	
	/**
	 * @param nav
	 * @param user
	 * @throws FDResourceException
	 * 
	 * Try to find a list that can be selected
	 */
	private static void findAListToBeSelected(FilteringNavigator nav, FDUserI user) throws FDResourceException{
		
		String defaultListId = user.getDefaultListId();  
		List<FDCustomerCreatedList> lists = FDListManager.getCustomerCreatedLists(user);
		List<Object> values = new ArrayList<Object>();
		
		//try to find the default list first
		if(defaultListId!=null){
			for(FDCustomerCreatedList list: lists){
				if(user.getDefaultListId().equals(list.getId()) && list.getCount()!=0){				
					values.add(list.getId());
					nav.getFilterValues().put(EnumQuickShopFilteringValue.YOUR_LISTS, values);
					return;
				}
			}			
		}
		
		//if no default list found (or it's empty) then set the first list (after A-Z sorting) as selected
		if(!lists.isEmpty()){
			Collections.sort(lists, LIST_COMPARATOR);
			String id = null;
			for(FDCustomerCreatedList list : lists){
				if(list.getCount()>0){
					id=list.getId();
					break;
				}
			}
			values.add(id);
			nav.getFilterValues().put(EnumQuickShopFilteringValue.YOUR_LISTS, values);
			return;
		}
		
	}
	
	/** Sorts FDCustomerCreatedLists by A-Z */
	private final static Comparator<FDCustomerCreatedList> LIST_COMPARATOR = new Comparator<FDCustomerCreatedList>() {
		@Override
		public int compare(FDCustomerCreatedList o0, FDCustomerCreatedList o1) {
			return o0.getName().compareToIgnoreCase(o1.getName());
		}
	};

}
