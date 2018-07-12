package com.freshdirect.webapp.ajax.reorder;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.FilteringNavigator;
import com.freshdirect.storeapi.content.FilteringSortingItem;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.filtering.CmsFilteringNavigator;
import com.freshdirect.webapp.ajax.quickshop.QuickShopSortType;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItem;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItemWrapper;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopSorterValues;
import com.freshdirect.webapp.ajax.reorder.data.QuickShopListRequestObject;
import com.freshdirect.webapp.ajax.reorder.data.QuickShopReturnValue;


public abstract class QuickShopServlet extends BaseJsonServlet {

	private static final long	serialVersionUID	= -5842364142639570121L;

	@Override
	protected final void doGet( HttpServletRequest request, HttpServletResponse response, FDUserI user ) throws HttpErrorResponse {
		
		// parse request data
		QuickShopListRequestObject requestData = parseRequestData( request, QuickShopListRequestObject.class, true );		
		if ( requestData == null ) {
			requestData = new QuickShopListRequestObject();
		}
		requestData.setPageSize(CmsFilteringNavigator.increasePageSizeToFillLayoutFully(request, user, requestData.getPageSize()));
		requestData.setUserId(user.getUserId());
		
		// main processing
		QuickShopReturnValue responseData = process( user, request, requestData );
		
		// write out result
		writeResponseData( response, responseData );
		
	}
	
	/**
	 * Override this to do the quickshop filtering...
	 * 
	 * @param user
	 * @param requestData
	 * @return
	 * @throws HttpErrorResponse
	 */
	protected abstract QuickShopReturnValue process( FDUserI user, HttpServletRequest request, QuickShopListRequestObject requestData ) throws HttpErrorResponse;

	
	// ========================
	// Common utility methods
	// ========================
	
	public static List<QuickShopLineItem> unwrapResult(List<FilteringSortingItem<QuickShopLineItemWrapper>> result){
		
		List<QuickShopLineItem> unwrapped = new ArrayList<QuickShopLineItem>();
		
		for(FilteringSortingItem<QuickShopLineItemWrapper> wrapper : result){
			unwrapped.add(wrapper.getNode().getItem());
		}
		
		return unwrapped;
	}
	
	public static List<QuickShopSorterValues> generateSorter(FilteringNavigator nav){
		
		List<QuickShopSorterValues> sorter = new ArrayList<QuickShopSorterValues>();
		
		for(QuickShopSortType type: QuickShopSortType.values()){
			QuickShopSorterValues value = new QuickShopSorterValues(type.getLabel(), type.getText(), nav.isOrderAscending(), false);
			if(nav.getSortBy()!=null && type.getLabel().equals(nav.getSortBy().getLabel())){
				value.setSelected(true);
			}
			sorter.add(value);
		}
		
		return sorter;
		
	}
	
	public static final int DEFAULT_PAGE_NUMBER = 0;
	public static final int DEFAULT_PAGE_SIZE = FDStoreProperties.getQuickShopPageSize();
	
	public static List<FilteringSortingItem<QuickShopLineItemWrapper>> createPage(FDUserI user, HttpServletRequest request, QuickShopListRequestObject reqObj,
			List<FilteringSortingItem<QuickShopLineItemWrapper>> items) {

		int pageSize = CmsFilteringNavigator.increasePageSizeToFillLayoutFully(request, user, DEFAULT_PAGE_SIZE);
		int pageOffset = DEFAULT_PAGE_NUMBER;
		if (reqObj != null) {
			pageSize = reqObj.getPageSize();
			pageOffset = reqObj.getActivePage() * pageSize;
		}

		List<FilteringSortingItem<QuickShopLineItemWrapper>> pagedItems = new ArrayList<FilteringSortingItem<QuickShopLineItemWrapper>>(
				pageSize <= 0 ? items.size() : pageSize);

		int noOfPagedProducts = items.size();

		int max = pageSize == 0 ? pageOffset + noOfPagedProducts : pageOffset + pageSize;
		for (int i = pageOffset; i < max; i++) {
			if (i >= items.size()) {
				break;
			}
			pagedItems.add(items.get(i));
		}

		return pagedItems;
	}

	/**
	 * Wrap items in a FilteringSortingItem for the FilteringFlow.
	 * @param items
	 * @return
	 */
	public static List<FilteringSortingItem<QuickShopLineItemWrapper>> prepareForFiltering(List<QuickShopLineItemWrapper> items){
		return FilteringSortingItem.wrap(items);
	}
}
