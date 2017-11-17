package com.freshdirect.webapp.ajax.reorder.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.FilteringFlowResult;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.FilteringNavigator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.EnumQuickShopFilteringValue;
import com.freshdirect.storeapi.content.FilteringMenuItem;
import com.freshdirect.storeapi.content.FilteringSortingItem;
import com.freshdirect.storeapi.content.FilteringValue;
import com.freshdirect.webapp.ajax.cart.data.CartData.SalesUnit;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItem;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItemWrapper;
import com.freshdirect.webapp.ajax.reorder.QuickShopFilterImpl;
import com.freshdirect.webapp.ajax.reorder.QuickShopHelper;
import com.freshdirect.webapp.ajax.reorder.QuickShopServlet;
import com.freshdirect.webapp.ajax.reorder.data.EnumQuickShopTab;
import com.freshdirect.webapp.ajax.reorder.data.QuickShopListRequestObject;
import com.freshdirect.webapp.ajax.reorder.data.QuickShopPastOrdersCustomMenu;

public class QuickShopFilterService {

	private static final Logger LOG = LoggerFactory.getInstance(QuickShopFilterService.class);

	private static final QuickShopFilterService INSTANCE = new QuickShopFilterService();

	private QuickShopFilterService() {
	}

	public static QuickShopFilterService defaultService() {
		return INSTANCE;
	}

    public FilteringFlowResult<QuickShopLineItemWrapper> collectQuickShopLineItemForPastOrders(HttpServletRequest request, FDUserI user, HttpSession session,
            FilteringNavigator nav, Set<FilteringValue> filters,
			QuickShopListRequestObject requestData) throws FDResourceException {
		EnumQuickShopTab tab = EnumQuickShopTab.PAST_ORDERS;
        return collectQuickShopLineItem(request, user, session, nav, filters, tab.cacheName, tab, requestData);
	}

    public FilteringFlowResult<QuickShopLineItemWrapper> collectQuickShopLineItemForTopItems(HttpServletRequest request, FDUserI user, HttpSession session, FilteringNavigator nav,
            Set<FilteringValue> filters)
			throws FDResourceException {
		EnumQuickShopTab tab = EnumQuickShopTab.TOP_ITEMS;
        return collectQuickShopLineItem(request, user, session, nav, filters, tab.cacheName, tab, null);
	}

	public QuickShopPastOrdersCustomMenu transformMenuIntoPastOrdersCustom(Map<String, Map<FilteringValue, List<FilteringMenuItem>>> menu, String lastOrderId) {
		QuickShopPastOrdersCustomMenu result = new QuickShopPastOrdersCustomMenu();
		Map<FilteringValue, List<FilteringMenuItem>> originalPastOrdersMenu = menu.get(EnumQuickShopFilteringValue.ORDERS_BY_DATE.getParent());
		if (originalPastOrdersMenu != null) {
			List<FilteringMenuItem> originalPastOrdersMenuItems = originalPastOrdersMenu.get(EnumQuickShopFilteringValue.ORDERS_BY_DATE);
			List<FilteringMenuItem> selectedMenuItems = new ArrayList<FilteringMenuItem>();
			for (FilteringMenuItem menuItem : originalPastOrdersMenuItems) {
				if (result.getFirstMenuItems().size() < FDStoreProperties.getPastOrdersVisibleItemsCount()) {
					result.getFirstMenuItems().add(menuItem);
				} else {
					if (menuItem.isSelected()) {
						selectedMenuItems.add(menuItem);
					} else {
						QuickShopFilteringMenuItemService.defaultService().addMenuItemToPastOrdersGroupedByYear(result.getPastOrdersGroupedByYear(), menuItem);
					}
				}
			}
			QuickShopFilteringMenuItemService.defaultService().sortPastOrdersByYear(result.getPastOrdersGroupedByYear());
			result.getFirstMenuItems().addAll(selectedMenuItems);
			QuickShopFilteringMenuItemService.defaultService().updateLatestMenuItem(result.getFirstMenuItems(), lastOrderId);
		}
		return result;
	}

    private FilteringFlowResult<QuickShopLineItemWrapper> collectQuickShopLineItem(HttpServletRequest servletRequest, FDUserI user, HttpSession session, FilteringNavigator nav,
            Set<FilteringValue> filters, String cacheName,
			EnumQuickShopTab tab, QuickShopListRequestObject requestData) throws FDResourceException {
		FilteringFlowResult<QuickShopLineItemWrapper> result = null;
		List<QuickShopLineItemWrapper> items = QuickShopHelper.getWrappedOrderHistoryUsingCache(user, tab, cacheName);
		
		List<QuickShopLineItemWrapper> discontinuedandoosproducts = new ArrayList<QuickShopLineItemWrapper>();
		if(user!=null && user.getUserContext() != null && user.getUserContext().getStoreContext() != null && user.getUserContext().getStoreContext().getEStoreId() == EnumEStoreId.FDX){    			
	        for(QuickShopLineItemWrapper item : items){
	        	if(item!=null && item.getProduct()!=null && (item.getProduct().isDiscontinued() || item.getProduct().isOutOfSeason())){
	        		discontinuedandoosproducts.add(item);
	        	}
	        }
	        items.removeAll(discontinuedandoosproducts);
		}      
			
		if (EnumQuickShopTab.PAST_ORDERS.equals(tab)) {
			String yourLastOrderId = getYourLastOrderId(items);
			requestData.setYourLastOrderId(yourLastOrderId);
		}
        QuickShopSearchService.defaultService().search(nav.getSearchTerm(), items, user, servletRequest);
		List<FilteringSortingItem<QuickShopLineItemWrapper>> filterItems = QuickShopServlet.prepareForFiltering(items);
		QuickShopFilterImpl filter = new QuickShopFilterImpl(nav, user, filters, filterItems, QuickShopHelper.getActiveReplacements(session), tab, requestData);
		LOG.info("Start filtering process");
		result = filter.doFlow(nav, filterItems);
		eliminatePreviousProductDuplicatesFromPastOrders(result.getItems());
		QuickShopHelper.postProcessPopulate(user, result, session);
		setYourTopItemQuantityToDefaultValue(tab, result);
		return result;
	}

	private void setYourTopItemQuantityToDefaultValue(EnumQuickShopTab tab, FilteringFlowResult<QuickShopLineItemWrapper> result) {
		if (EnumQuickShopTab.TOP_ITEMS.equals(tab)) {
			for (FilteringSortingItem<QuickShopLineItemWrapper> filteringItem : result.getItems()) {
				QuickShopLineItem item = filteringItem.getModel().getItem();
				item.getQuantity().setQuantity(item.getQuantity().getqMin());
				for (SalesUnit salesUnit : item.getSalesUnit()) {
					salesUnit.setSelected(false);
				}
			}
		}
	}
	
	/**
	 * Remove older product duplicates from past orders.
	 *
	 * @param items
	 */
	public void eliminatePreviousProductDuplicatesFromPastOrders(List<FilteringSortingItem<QuickShopLineItemWrapper>> items) {
			Set<String> itemKeys = new HashSet<String>();
			Map<String, Integer> indicesOfItems = collectItemIndicesByOrderIdAndContentKeyId(items);
			List<FilteringSortingItem<QuickShopLineItemWrapper>> localItems = new ArrayList<FilteringSortingItem<QuickShopLineItemWrapper>>(items);
			QuickShopSortingService.defaultService().sortByWrappedDeliveryDateAndOrderId(localItems);
			Set<Integer> itemIndicesToBeRemoved = collectDuplicateItemIndicesToBeRemoved(itemKeys,
					indicesOfItems, localItems);
			removeDuplicateItemsByIndices(items, itemIndicesToBeRemoved);
	}

	private void removeDuplicateItemsByIndices(List<FilteringSortingItem<QuickShopLineItemWrapper>> items, Set<Integer> itemIndicesToBeRemoved) {
		Iterator<FilteringSortingItem<QuickShopLineItemWrapper>> resultIterator = items.iterator();
		int resultIndex = 0;
		while (resultIterator.hasNext()) {
			resultIterator.next();
			if (itemIndicesToBeRemoved.contains(resultIndex)) {
				resultIterator.remove();
			}
			resultIndex++;
		}
	}

	private Set<Integer> collectDuplicateItemIndicesToBeRemoved(Set<String> itemKeys, Map<String, Integer> indicesOfItems, List<FilteringSortingItem<QuickShopLineItemWrapper>> localItems) {
		Iterator<FilteringSortingItem<QuickShopLineItemWrapper>> iterator = localItems.iterator();
		Set<Integer> itemIndicesToBeRemoved = new HashSet<Integer>();
		while (iterator.hasNext()) {
			FilteringSortingItem<QuickShopLineItemWrapper> item = iterator.next();
			String contentKey = item.getModel().getProduct().getContentKey().getId();
			if (itemKeys.contains(contentKey)) {
				itemIndicesToBeRemoved.add(indicesOfItems.get(item.getModel().getOrderId() + contentKey));
			} else {
				itemKeys.add(contentKey);
			}
		}
		return itemIndicesToBeRemoved;
	}

	private Map<String, Integer> collectItemIndicesByOrderIdAndContentKeyId(List<FilteringSortingItem<QuickShopLineItemWrapper>> items) {
		Map<String, Integer> indicesOfItems = new HashMap<String, Integer>();
		int index=0;
		for (FilteringSortingItem<QuickShopLineItemWrapper> item : items) {
			indicesOfItems.put(item.getModel().getOrderId() + item.getModel().getProduct().getContentKey().getId(), index++);
		}
		return indicesOfItems;
	}

	/**
	 * Gives the order id of the latest order.
	 * <br>
	 * IMPORTANT: Delivery Date and OrderId Sort needs to be applied before!
	 * @param items
	 * @return
	 */
	private String getYourLastOrderId(List<QuickShopLineItemWrapper> items) {
		String result = null;
		if (items != null && !items.isEmpty()) {
			QuickShopSortingService.defaultService().sortByDeliveryDateAndOrderId(items);
			result = items.get(0).getOrderId();
		}
		return result;
	}

}
