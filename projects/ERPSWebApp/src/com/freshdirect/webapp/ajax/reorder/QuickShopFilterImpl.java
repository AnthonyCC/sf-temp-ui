package com.freshdirect.webapp.ajax.reorder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.fdstore.content.FilteringFlow;
import com.freshdirect.fdstore.content.GenericFilterDecorator;
import com.freshdirect.fdstore.content.GenericFilteringMenuBuilder;
import com.freshdirect.fdstore.lists.FDListManager;
import com.freshdirect.fdstore.util.FilteringNavigator;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.DepartmentModel;
import com.freshdirect.storeapi.content.EnumQuickShopFilteringValue;
import com.freshdirect.storeapi.content.FilteringMenuItem;
import com.freshdirect.storeapi.content.FilteringSortingItem;
import com.freshdirect.storeapi.content.FilteringValue;
import com.freshdirect.webapp.ajax.quickshop.QuickShopComparatorUtil;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItem;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItemWrapper;
import com.freshdirect.webapp.ajax.reorder.data.EnumQuickShopTab;
import com.freshdirect.webapp.ajax.reorder.data.QuickShopListRequestObject;
import com.freshdirect.webapp.ajax.reorder.service.QuickShopSortingService;

public class QuickShopFilterImpl extends FilteringFlow<QuickShopLineItemWrapper> {

	private FilteringNavigator nav;
	private List<FilteringSortingItem<QuickShopLineItemWrapper>> unfilteredItems;
	private Set<FilteringValue> filters;
	private List<String> activeReplacements;
	private EnumQuickShopTab tabType;
	private QuickShopListRequestObject requestData;

    public QuickShopFilterImpl(FilteringNavigator nav, Set<FilteringValue> filters, List<FilteringSortingItem<QuickShopLineItemWrapper>> items, List<String> activeReplacements,
			EnumQuickShopTab tabType, QuickShopListRequestObject requestData) {
		this.nav = nav;
		this.filters = filters;
		this.unfilteredItems = new ArrayList<FilteringSortingItem<QuickShopLineItemWrapper>>(items);
		this.activeReplacements = activeReplacements;
		this.tabType = tabType;
		this.requestData = requestData;
	}

	public FilteringNavigator getNav() {
		return nav;
	}

	public QuickShopListRequestObject getRequestData() {
		return requestData;
	}

	public EnumQuickShopTab getTabType() {
		return tabType;
	}

	public void setFilters(Set<FilteringValue> filters) {
		this.filters = filters;
	}

	public void setNav(FilteringNavigator nav) {
		this.nav = nav;
	}

	public void setRequestData(QuickShopListRequestObject requestData) {
		this.requestData = requestData;
	}

	public void setTabType(EnumQuickShopTab tabType) {
		this.tabType = tabType;
	}

	@Override
	protected Comparator<FilteringSortingItem<QuickShopLineItemWrapper>> createComparator(List<FilteringSortingItem<QuickShopLineItemWrapper>> items) {
        return QuickShopComparatorUtil.createQuickShopItemComparator(items, nav);
	}

	@Override
	protected GenericFilterDecorator<FilteringSortingItem<QuickShopLineItemWrapper>> createFilterValueDecorator() {
		return new QuickShopFilterValueDecorator(filters);
	}

	@Override
	protected GenericFilterDecorator<FilteringSortingItem<QuickShopLineItemWrapper>> createMenuValueDecorator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Set<FilteringValue> getFilterEnums() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Set<FilteringValue> getFilters() {
		return filters;
	}

	@Override
	protected List<FilteringSortingItem<QuickShopLineItemWrapper>> getItems() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void midProcess(List<FilteringSortingItem<QuickShopLineItemWrapper>> items) {
		QuickShopSortingService.defaultService().sortByWrappedDeliveryDateAndOrderId(items);
	}

	@Override
	protected void postProcess(List<FilteringSortingItem<QuickShopLineItemWrapper>> items, GenericFilteringMenuBuilder<FilteringSortingItem<QuickShopLineItemWrapper>> menuBuilder) {

		Map<FilteringValue, Map<String, FilteringMenuItem>> menu = menuBuilder.getDomains();

		// all departments should be selected (if there is no dept filter
		// selected)
		// if(nav.getFilterValues().get(EnumQuickShopFilteringValue.DEPT)==null
		// ||
		// nav.getFilterValues().get(EnumQuickShopFilteringValue.DEPT).isEmpty()){
		// Map<String, FilteringMenuItem> subMenu =
		// menu.get(EnumQuickShopFilteringValue.DEPT);
		// for(String key: subMenu.keySet()){
		// subMenu.get(key).setSelected(true);
		// }
		// }

		if (filters.contains(EnumQuickShopFilteringValue.TIME_FRAME_ALL)) { // time
																			// frame
																			// filters
																			// only
																			// exists
																			// on
																			// past
																			// orders
																			// tab

			// all orders by date menu item should be selected if no filter
			// coming with the request
			// if(nav.getFilterValues().get(EnumQuickShopFilteringValue.ORDERS_BY_DATE)==null
			// ||
			// nav.getFilterValues().get(EnumQuickShopFilteringValue.ORDERS_BY_DATE).isEmpty()){
			// Map<String, FilteringMenuItem> subMenu =
			// menu.get(EnumQuickShopFilteringValue.ORDERS_BY_DATE);
			// for(String key: subMenu.keySet()){
			// subMenu.get(key).setSelected(true);
			// }
			// }

			// time range filter counters counting the orders in it not the
			// items
			// this algorithm counting the orders based on orderId
			Map<EnumQuickShopFilteringValue, Set<String>> orderCounter = new HashMap<EnumQuickShopFilteringValue, Set<String>>();
			for (FilteringSortingItem<QuickShopLineItemWrapper> item : unfilteredItems) {
				for (EnumQuickShopFilteringValue filter : EnumQuickShopFilteringValue.values()) {
					if ("TIME FRAME".equals(filter.getParent())) {
						if (item.getFilteringValue(filter) != null) {
							if (orderCounter.get(filter) != null) {
								orderCounter.get(filter).add(item.getModel().getOrderId());
							} else {
								Set<String> time = new HashSet<String>();
								time.add(item.getModel().getOrderId());
								orderCounter.put(filter, time);
							}
						}
					}
				}
			}

			for (EnumQuickShopFilteringValue filter : EnumQuickShopFilteringValue.values()) {
				if ("TIME FRAME".equals(filter.getParent())) {
					for (String key : menu.get(filter).keySet()) {
						Set<String> filterCounts = orderCounter.get(filter);
						int counter = filterCounts == null ? 0 : filterCounts.size();
						menu.get(filter).get(key).setCounter(counter);

						// change ALL - PAST YEAR label in case of order
						// limitation
						if (EnumQuickShopFilteringValue.TIME_FRAME_ALL == filter && counter >= FDListManager.QUICKSHOP_ORDER_LIMIT) {
							menu.get(filter).get(key).setName("LAST " + FDListManager.QUICKSHOP_ORDER_LIMIT + " ORDERS");
						}

					}
				}
			}

			// re count the items in orders also (because of duplicated sku
			// filtering)
			Map<String, Set<String>> itemCounter = new HashMap<String, Set<String>>();
			for (FilteringSortingItem<QuickShopLineItemWrapper> item : unfilteredItems) {
				String orderId = item.getNode().getOrderId();
				if (itemCounter.get(orderId) != null) {
					itemCounter.get(orderId).add(item.getNode().getItem().getSkuCode());
				} else {
					Set<String> skus = new HashSet<String>();
					skus.add(item.getNode().getItem().getSkuCode());
					itemCounter.put(orderId, skus);
				}
			}

			Map<String, FilteringMenuItem> subMenu = menu.get(EnumQuickShopFilteringValue.ORDERS_BY_DATE);
			for (String key : subMenu.keySet()) {
				if (itemCounter.get(subMenu.get(key).getFilteringUrlValue()) != null) {
					subMenu.get(key).setCounter(itemCounter.get(subMenu.get(key).getFilteringUrlValue()).size());
				}
			}
		}

		// Set temporary replacement flags
		for (FilteringSortingItem<QuickShopLineItemWrapper> wrapper : items) {
			QuickShopLineItem item = wrapper.getNode().getItem();
			if (activeReplacements != null && activeReplacements.contains(item.getItemId())) {
				item.setUseReplacement(true);
			}
		}

		// Quickshop department filter special
		Map<String, FilteringMenuItem> deptMenu = menu.get(EnumQuickShopFilteringValue.DEPT);
		if (nav != null) {
			Map<FilteringValue, List<Object>> fv = nav.getFilterValues();
			if (fv != null) {
				List<Object> fs = fv.get(EnumQuickShopFilteringValue.DEPT);
				if (fs != null) {
					String deptId = (String) fs.get(0);
					if (deptId != null) {
						if (!deptMenu.containsKey(deptId)) {
							DepartmentModel dept = (DepartmentModel) ContentFactory.getInstance().getContentNode(ContentType.Department, deptId);
							if (dept != null) {
								FilteringMenuItem fmi = new FilteringMenuItem(dept.getFullName(), deptId, 0, EnumQuickShopFilteringValue.DEPT);
								fmi.setSelected(true);
								deptMenu.put(deptId, fmi);

							}
						}
					}
				}
			}
		}
	}

	@Override
	protected void preProcess(List<FilteringSortingItem<QuickShopLineItemWrapper>> items) {
		if (EnumQuickShopTab.PAST_ORDERS.equals(tabType) && items != null && !items.isEmpty()) {
			if (requestData.getOrderIdList() == null || requestData.getOrderIdList().isEmpty()) {
				QuickShopSortingService.defaultService().sortByWrappedDeliveryDateAndOrderId(items);
				requestData.setOrderIdList(Arrays.<Object> asList(items.get(0).getNode().getOrderId()));
				Map<FilteringValue, List<Object>> filterValues = nav.getFilterValues();
				filterValues.put(EnumQuickShopFilteringValue.ORDERS_BY_DATE, requestData.getOrderIdList());
			}
		}
	}

	@Override
	protected List<FilteringSortingItem<QuickShopLineItemWrapper>> reOrganizeFavourites(List<FilteringSortingItem<QuickShopLineItemWrapper>> items) {
		return null;
	}

}
