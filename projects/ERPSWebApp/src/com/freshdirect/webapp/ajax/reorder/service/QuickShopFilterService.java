package com.freshdirect.webapp.ajax.reorder.service;

import java.util.ArrayList;
import java.util.HashMap;
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
		if(user!=null && user.getUserContext() != null && user.getUserContext().getStoreContext() != null && user.getUserContext().getStoreContext().getEStoreId() == EnumEStoreId.FDX
				&& items!=null){    			
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
        if (EnumQuickShopTab.PAST_ORDERS.equals(tab)) {
            result.setItems(aggregatesCustomizeItems(result.getItems(), user));
        }
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
	
    public List<FilteringSortingItem<QuickShopLineItemWrapper>> aggregatesCustomizeItems(List<FilteringSortingItem<QuickShopLineItemWrapper>> items, FDUserI user) {
        List<FilteringSortingItem<QuickShopLineItemWrapper>> filteredItems = new ArrayList<FilteringSortingItem<QuickShopLineItemWrapper>>();

        Map<String, Double> maxQuantityBySkuCode = aggregateProductQuantity(items);

        for (FilteringSortingItem<QuickShopLineItemWrapper> item : items) {
            QuickShopLineItem lineItem = item.getNode().getItem();
            Double aggregatedQuantity = maxQuantityBySkuCode.get(lineItem.getSkuCode());
            if (aggregatedQuantity != null) {
                if (lineItem.getConfiguration() != null) {
                    filteredItems.add(item);
                } else {
                    double maxQuantity = lineItem.getQuantity().getqMax();
                    double quantity = lineItem.getQuantity().getQuantity();
                    int quantityCompare = Double.compare(aggregatedQuantity, Math.min(quantity, maxQuantity));
                    if (quantityCompare >= 0) {
                        maxQuantityBySkuCode.remove(lineItem.getSkuCode());
                        try {
                            FilteringSortingItem<QuickShopLineItemWrapper> clonedItem = QuickShopHelper.copyQuickShopFilteringItemWrapper(item, user);
                            double quantityInCart = user.getShoppingCart().calculateAggregatedQuantityBySku(lineItem.getSkuCode());
                            clonedItem.getNode().getItem().getQuantity().setQuantity(Math.min(maxQuantity - quantityInCart, aggregatedQuantity));
                            filteredItems.add(clonedItem);
                        } catch (FDResourceException e) {
                            LOG.debug("Error when cloned filtering quickshop line item of contentkey " + item.getNode().getProduct().getContentKey(), e);
                        }
                    } else {
                        filteredItems.add(item);
                    }
                }
            }
        }
        return filteredItems;
    }

    private Map<String, Double> aggregateProductQuantity(List<FilteringSortingItem<QuickShopLineItemWrapper>> items) {
        Map<String, Double> maxQuantitiesBySkuCode = new HashMap<String, Double>();
        for (FilteringSortingItem<QuickShopLineItemWrapper> item : items) {
            QuickShopLineItem lineItem = item.getNode().getItem();
            String skuCode = lineItem.getSkuCode();
            double quantity = lineItem.getQuantity().getQuantity();
            double maxQuantity = lineItem.getQuantity().getqMax();
            if (maxQuantitiesBySkuCode.containsKey(skuCode)) {
                quantity += maxQuantitiesBySkuCode.get(skuCode);
            }
            maxQuantitiesBySkuCode.put(skuCode, Math.min(maxQuantity, quantity));
        }
        return maxQuantitiesBySkuCode;
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
