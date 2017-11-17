package com.freshdirect.webapp.ajax.quickshop;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.content.EnumQuickShopFilteringValue;
import com.freshdirect.fdstore.content.FilteringMenuItem;
import com.freshdirect.fdstore.content.FilteringSortingItem;
import com.freshdirect.fdstore.content.FilteringValue;
import com.freshdirect.fdstore.coremetrics.CmContext;
import com.freshdirect.fdstore.coremetrics.tagmodel.ElementTagModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.FilteringNavigator;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.filtering.CmsFilteringNavigator;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItem;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItemWrapper;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopListRequestObject;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopReturnValue;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopSorterValues;

/**
 * Deprecated with Quickshop 2.2 version. Replaced with {@link com.freshdirect.webapp.ajax.reorder.QuickShopServlet}
 */
@Deprecated
public abstract class QuickShopServlet extends BaseJsonServlet {

	private static final long	serialVersionUID	= -5842364142639570121L;

	@Override
	protected final void doGet( HttpServletRequest request, HttpServletResponse response, FDUserI user ) throws HttpErrorResponse {
		
		// parse request data
		QuickShopListRequestObject requestData = parseRequestData( request, QuickShopListRequestObject.class, true );		
		if ( requestData == null ) {
			requestData = new QuickShopListRequestObject();
		}
		requestData.setPageSize(CmsFilteringNavigator.increasePageSizeToFillLayoutFully(request, user, QuickShopServlet.DEFAULT_PAGE_SIZE));
		requestData.setUserId(user.getUserId());
		
		HttpSession session = request.getSession();
        Cookie[] cookies = request.getCookies();
		// main processing
        QuickShopReturnValue responseData = process(user, session, requestData, request);
		
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
    protected abstract QuickShopReturnValue process(FDUserI user, HttpSession session, QuickShopListRequestObject requestData, HttpServletRequest request) throws HttpErrorResponse;

	
	// ========================
	// Common utility methods
	// ========================
	
	protected static List<QuickShopLineItem> unwrapResult(List<FilteringSortingItem<QuickShopLineItemWrapper>> result){
		
		List<QuickShopLineItem> unwrapped = new ArrayList<QuickShopLineItem>();
		
		for(FilteringSortingItem<QuickShopLineItemWrapper> wrapper : result){
			unwrapped.add(wrapper.getNode().getItem());
		}
		
		return unwrapped;
	}
	
	protected static List<QuickShopSorterValues> generateSorter(FilteringNavigator nav){
		
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
	public static final int DEFAULT_PAGE_SIZE = 15;
	
	protected static List<FilteringSortingItem<QuickShopLineItemWrapper>> createPage(QuickShopListRequestObject reqObj,
			List<FilteringSortingItem<QuickShopLineItemWrapper>> items) {

		int pageSize = DEFAULT_PAGE_SIZE;
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

	protected static void generateCoremetricsElementTags( QuickShopReturnValue responseData, Map<FilteringValue, Map<String, FilteringMenuItem>> menu, String categoryName ) {		
		for ( Map<String, FilteringMenuItem> f : menu.values() ) {
			for( FilteringMenuItem i : f.values() ) {
				if ( i.isSelected() && !i.getFilter().equals( EnumQuickShopFilteringValue.TIME_FRAME_ALL )) {
					ElementTagModel eTagModel = new ElementTagModel();
					eTagModel.setElementCategory( CmContext.getContext().prefixedCategoryId( categoryName ) );
					eTagModel.setElementId( i.getName() );
					responseData.addCoremetrics( eTagModel.toStringList() );
				}
			}
		}
	}
}
