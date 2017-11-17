package com.freshdirect.webapp.ajax.quickshop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.ContentType;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.EnumQuickShopFilteringValue;
import com.freshdirect.fdstore.content.FilteringFlow;
import com.freshdirect.fdstore.content.FilteringMenuItem;
import com.freshdirect.fdstore.content.FilteringSortingItem;
import com.freshdirect.fdstore.content.FilteringValue;
import com.freshdirect.fdstore.content.GenericFilterDecorator;
import com.freshdirect.fdstore.content.GenericFilteringMenuBuilder;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.lists.FDListManager;
import com.freshdirect.fdstore.util.FilteringNavigator;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItem;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItemWrapper;

/**
 * Deprecated with Quickshop 2.2 version. Replaced with {@link com.freshdirect.webapp.ajax.reorder.QuickShopFilterImpl}
 */
@Deprecated
public class QuickShopFilterImpl extends FilteringFlow<QuickShopLineItemWrapper>{
	
	private FilteringNavigator nav;
	private FDUserI user;
	private List<FilteringSortingItem<QuickShopLineItemWrapper>> unfilteredItems;
	
	private Set<FilteringValue> filters;
	
	private List<String> activeReplacements;

	public QuickShopFilterImpl(FilteringNavigator nav, FDUserI user, Set<FilteringValue> filters, List<FilteringSortingItem<QuickShopLineItemWrapper>> items, List<String> activeReplacements) {
		super();
		this.nav = nav;
		this.user = user;
		this.filters = filters;
		this.unfilteredItems = new ArrayList<FilteringSortingItem<QuickShopLineItemWrapper>>(items);
		this.activeReplacements = activeReplacements;
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
	protected Comparator<FilteringSortingItem<QuickShopLineItemWrapper>> createComparator(List<FilteringSortingItem<QuickShopLineItemWrapper>> items) {
		return QuickShopComparatorUtil.createQuickShopItemComparator(items, user.getPricingContext(), nav);
	}

	@Override
	protected List<FilteringSortingItem<QuickShopLineItemWrapper>> reOrganizeFavourites(List<FilteringSortingItem<QuickShopLineItemWrapper>> items) {
		return null;
	}

	@Override
	protected List<FilteringSortingItem<QuickShopLineItemWrapper>> getItems() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Set<FilteringValue> getFilterEnums() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getUserId() {
		if (user != null) {
			FDIdentity identity = user.getIdentity();
			if (identity != null)
				return identity.getErpCustomerPK();
		}
		return null;
	}
	
	public PricingContext getPricingContext() {
		if (user != null)
			return user.getPricingContext();
		return PricingContext.DEFAULT;
	}

	@Override
	protected void postProcess(List<FilteringSortingItem<QuickShopLineItemWrapper>> items, GenericFilteringMenuBuilder<FilteringSortingItem<QuickShopLineItemWrapper>> menuBuilder) {
		

		Map<FilteringValue, Map<String, FilteringMenuItem>> menu = menuBuilder.getDomains();
		
		//all departments should be selected (if there is no dept filter selected)
//		if(nav.getFilterValues().get(EnumQuickShopFilteringValue.DEPT)==null || nav.getFilterValues().get(EnumQuickShopFilteringValue.DEPT).isEmpty()){
//			Map<String, FilteringMenuItem> subMenu = menu.get(EnumQuickShopFilteringValue.DEPT);
//			for(String key: subMenu.keySet()){
//				subMenu.get(key).setSelected(true);
//			}
//		}
		
		if(filters.contains(EnumQuickShopFilteringValue.TIME_FRAME_ALL)){ //time frame filters only exists on past orders tab
			
			//all orders by date menu item should be selected if no filter coming with the request 
//			if(nav.getFilterValues().get(EnumQuickShopFilteringValue.ORDERS_BY_DATE)==null || nav.getFilterValues().get(EnumQuickShopFilteringValue.ORDERS_BY_DATE).isEmpty()){
//				Map<String, FilteringMenuItem> subMenu = menu.get(EnumQuickShopFilteringValue.ORDERS_BY_DATE);
//				for(String key: subMenu.keySet()){
//					subMenu.get(key).setSelected(true);
//				}
//			}
			
			
			//time range filter counters counting the orders in it not the items
			//this algorithm counting the orders based on orderId
			Map<EnumQuickShopFilteringValue, Set<String>> orderCounter = new HashMap<EnumQuickShopFilteringValue, Set<String>>();
			for(FilteringSortingItem<QuickShopLineItemWrapper> item: unfilteredItems){
				for(EnumQuickShopFilteringValue filter: EnumQuickShopFilteringValue.values()){
					if("TIME FRAME".equals(filter.getParent())){
						if(item.getFilteringValue(filter)!=null){
							if(orderCounter.get(filter)!=null){
								orderCounter.get(filter).add(item.getModel().getOrderId());
							}else{
								Set<String> time = new HashSet<String>();
								time.add(item.getModel().getOrderId());
								orderCounter.put(filter, time);
							}
						}					
					}
				}
			}
			
			for(EnumQuickShopFilteringValue filter: EnumQuickShopFilteringValue.values()){
				if("TIME FRAME".equals(filter.getParent())){
					for(String key: menu.get(filter).keySet()){
						Set<String> filterCounts = orderCounter.get(filter);
						int counter = filterCounts == null ? 0 : filterCounts.size();
						menu.get(filter).get(key).setCounter( counter );
						
						//change ALL - PAST YEAR label in case of order limitation
						if(EnumQuickShopFilteringValue.TIME_FRAME_ALL == filter && counter>=FDListManager.QUICKSHOP_ORDER_LIMIT){
							menu.get(filter).get(key).setName("LAST " + FDListManager.QUICKSHOP_ORDER_LIMIT + " ORDERS");
						}
						
					}
				}
			}	
			
			//re count the items in orders also (because of duplicated sku filtering)
			Map<String, Set<String>> itemCounter = new HashMap<String, Set<String>>();
			for(FilteringSortingItem<QuickShopLineItemWrapper> item : unfilteredItems){
				String orderId = item.getNode().getOrderId();
				if(itemCounter.get(orderId)!=null){
					itemCounter.get(orderId).add(item.getNode().getItem().getSkuCode());
				}else{
					Set<String> skus = new HashSet<String>();
					skus.add(item.getNode().getItem().getSkuCode());
					itemCounter.put(orderId, skus);
				}
			}
			
			Map<String, FilteringMenuItem> subMenu = menu.get(EnumQuickShopFilteringValue.ORDERS_BY_DATE);
			for(String key: subMenu.keySet()){
				if(itemCounter.get(subMenu.get(key).getFilteringUrlValue())!=null){
					subMenu.get(key).setCounter(itemCounter.get(subMenu.get(key).getFilteringUrlValue()).size());					
				}
			}
		}		

		// Set temporary replacement flags
		for ( FilteringSortingItem<QuickShopLineItemWrapper> wrapper : items ) {
			QuickShopLineItem item = wrapper.getNode().getItem();
			if ( activeReplacements != null && activeReplacements.contains( item.getItemId() ) ) {
				item.setUseReplacement( true );
			}			
		}
		
		// Quickshop department filter special		
		Map<String, FilteringMenuItem> deptMenu = menu.get( EnumQuickShopFilteringValue.DEPT );
		if ( nav != null ) {
			Map<FilteringValue, List<Object>> fv = nav.getFilterValues();
			if ( fv != null ) {
				List<Object> fs = fv.get( EnumQuickShopFilteringValue.DEPT );
				if ( fs != null ) {
					String deptId = (String)fs.get( 0 );
					if ( deptId != null ) {
						if ( !deptMenu.containsKey( deptId ) ) {
							DepartmentModel dept = (DepartmentModel)ContentFactory.getInstance().getContentNode( ContentType.get("Department"), deptId );
							if ( dept != null ) {
								FilteringMenuItem fmi = new FilteringMenuItem( dept.getFullName(), deptId, 0, EnumQuickShopFilteringValue.DEPT );
								fmi.setSelected( true );
								deptMenu.put( deptId, fmi );
								
							}
						}
					}
				}
			}
		}
	}

	@Override
	protected void preProcess(List<FilteringSortingItem<QuickShopLineItemWrapper>> items) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Set<FilteringValue> getFilters() {
		return filters;
	}

	public void setFilters(Set<FilteringValue> filters) {
		this.filters = filters;
	}

	public FilteringNavigator getNav() {
		return nav;
	}

	public void setNav(FilteringNavigator nav) {
		this.nav = nav;
	}

	@Override
	protected void midProcess(List<FilteringSortingItem<QuickShopLineItemWrapper>> items) {
		
		Collections.sort(items, DELIVERY_DATE_COMPARATOR);
		
		//remove sku duplicates
		Set<String> skus = new HashSet<String>();
		Iterator<FilteringSortingItem<QuickShopLineItemWrapper>> it = items.iterator();
		while(it.hasNext()){
			FilteringSortingItem<QuickShopLineItemWrapper> item = it.next();
			if(skus.contains(item.getNode().getItem().getSkuCode())){
				it.remove();
				continue;
			}
			skus.add(item.getNode().getItem().getSkuCode());
		}
		
	}
	
	/** Sorts items by delivery date */
	private final static Comparator<FilteringSortingItem<QuickShopLineItemWrapper>> DELIVERY_DATE_COMPARATOR = new Comparator<FilteringSortingItem<QuickShopLineItemWrapper>>() {
		@Override
		public int compare(FilteringSortingItem<QuickShopLineItemWrapper> o0, FilteringSortingItem<QuickShopLineItemWrapper> o1) {
			return o1.getNode().getDeliveryDate().compareTo(o0.getNode().getDeliveryDate());
		}
	};

}
