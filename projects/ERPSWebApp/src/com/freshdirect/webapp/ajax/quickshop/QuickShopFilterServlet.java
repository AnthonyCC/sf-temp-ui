package com.freshdirect.webapp.ajax.quickshop;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ContentSearch;
import com.freshdirect.fdstore.content.EnumQuickShopFilteringValue;
import com.freshdirect.fdstore.content.FilteringFlowResult;
import com.freshdirect.fdstore.content.FilteringSortingItem;
import com.freshdirect.fdstore.content.FilteringValue;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.QuickShopCacheUtil;
import com.freshdirect.fdstore.content.SearchResults;
import com.freshdirect.fdstore.coremetrics.tagmodel.PageViewTagModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.FilteringNavigator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.quickshop.data.EnumQuickShopTab;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItemWrapper;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopListRequestObject;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopPagerValues;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopReturnValue;

public class QuickShopFilterServlet extends QuickShopServlet {

	private static final long serialVersionUID = -2275457565029351766L;
	
	private static final Logger LOG = LoggerFactory.getInstance(QuickShopFilterServlet.class);
	
	//create filter
	private static final Set<FilteringValue> filters=new HashSet<FilteringValue>();
	static {
		filters.add(EnumQuickShopFilteringValue.TIME_FRAME_ALL);
		filters.add(EnumQuickShopFilteringValue.TIME_FRAME_LAST);
		filters.add(EnumQuickShopFilteringValue.TIME_FRAME_30);
		filters.add(EnumQuickShopFilteringValue.TIME_FRAME_60);
		filters.add(EnumQuickShopFilteringValue.TIME_FRAME_90);
		filters.add(EnumQuickShopFilteringValue.TIME_FRAME_180);
		filters.add(EnumQuickShopFilteringValue.ORDERS_BY_DATE);
		filters.add(EnumQuickShopFilteringValue.DEPT);
		filters.add(EnumQuickShopFilteringValue.GLUTEN_FREE);
		filters.add(EnumQuickShopFilteringValue.KOSHER);
		filters.add(EnumQuickShopFilteringValue.LOCAL);
		filters.add(EnumQuickShopFilteringValue.ORGANIC);
		filters.add(EnumQuickShopFilteringValue.ON_SALE);	
	}
	
	@Override
	protected QuickShopReturnValue process( FDUserI user, HttpSession session, QuickShopListRequestObject requestData ) throws HttpErrorResponse {
		
		LOG.info("Start processing request...");
		
		//transform request data
		FilteringNavigator nav = requestData.convertToFilteringNavigator();
		FilteringFlowResult<QuickShopLineItemWrapper> result = null;

		List<QuickShopLineItemWrapper> items = QuickShopCacheUtil.getListFromCache(QuickShopCacheUtil.PAST_ORDERS_CACHE_NAME, user.getIdentity().getErpCustomerPK());
		try {
			if(items==null){
				LOG.info("Wrapping products");
				items = QuickShopHelper.getWrappedOrderHistory(user, EnumQuickShopTab.PAST_ORDERS);
				if(!items.isEmpty()){
					QuickShopCacheUtil.putListToCache(QuickShopCacheUtil.PAST_ORDERS_CACHE_NAME, user.getIdentity().getErpCustomerPK(), new ArrayList<QuickShopLineItemWrapper>(items));					
				}
			}else{
				LOG.info("Fetching items from cache");
				items = new ArrayList<QuickShopLineItemWrapper>(items);
			}
			
			search(nav.getSearchTerm(), items);
			
			List<FilteringSortingItem<QuickShopLineItemWrapper>> filterItems = prepareForFiltering(items);
			
			QuickShopFilterImpl filter = new QuickShopFilterImpl(nav, user, filters, filterItems, QuickShopHelper.getActiveReplacements( session ));
			
			LOG.info("Start filtering process");			
			result = filter.doFlow(nav, filterItems);
			
			// post-process
			QuickShopHelper.postProcessPopulate( user, result, session );

		} catch (FDResourceException e) {
			LOG.error("Error while collecting order history", e);
			returnHttpError( 500, "Error while collecting order history", e );
		} 

		QuickShopReturnValue responseData = new QuickShopReturnValue(unwrapResult(createPage(requestData, result.getItems())),
				result.getMenu(), 
				new QuickShopPagerValues(requestData.getPageSize(), result.getItems().size(), requestData.getActivePage()),
				generateSorter(nav),
				nav.getSearchTerm(),
				null);
		
		// sorting menu items where needed
		QuickShopMenuOrderUtil.sortMenuItems(responseData.getMenu());
		
		// Generate coremetrics 'element' tags for the menu - selected filters
		generateCoremetricsElementTags( responseData, result.getMenu(), "quickshop | past_orders" );
		
		// Generate coremetrics 'pageview' tag - only when searching
		String searchTerm = nav.getSearchTerm();
		if ( searchTerm != null && searchTerm.trim().length() > 0 ) {
			PageViewTagModel pvTagModel = new PageViewTagModel();
			pvTagModel.setCategoryId( "quickshop | search" );
			pvTagModel.setPageId( "past_orders" );
			pvTagModel.setSearchTerm( searchTerm );
			pvTagModel.setSearchResults( Integer.toString( result.getItems().size() ) );
			responseData.addCoremetrics( pvTagModel.toStringList() );
		}
		
		return responseData;
	}
	
	public static List<FilteringSortingItem<QuickShopLineItemWrapper>> prepareForFiltering(List<QuickShopLineItemWrapper> items){
		
		// Wrap items in a FilteringSortingItem for the FilteringFlow
		List<FilteringSortingItem<QuickShopLineItemWrapper>> result = new ArrayList<FilteringSortingItem<QuickShopLineItemWrapper>>();
		for(QuickShopLineItemWrapper item : items){
			FilteringSortingItem<QuickShopLineItemWrapper> fsi = new FilteringSortingItem<QuickShopLineItemWrapper>(item);
			result.add(fsi);
		}
		
		return result;
	}
	
	/**
	 * @param searchTerm
	 * @param items - to be merged with the search result
	 * 
	 * Merge the original search result with the user's order history
	 */
	private static void search(String searchTerm, List<QuickShopLineItemWrapper> items){
		
		List<String> productIds = null;
		if(searchTerm!=null){
			
			SearchResults results = ContentSearch.getInstance().searchProducts(searchTerm);
			productIds = new ArrayList<String>();
			for(FilteringSortingItem<ProductModel> product : results.getProducts()){
				productIds.add(product.getNode().getContentKey().getId());
			}
			
			Iterator<QuickShopLineItemWrapper> it = items.iterator();
			while(it.hasNext()){
				if(!productIds.contains(it.next().getProduct().getContentKey().getId())){
					it.remove();
				}
			}
		}
		
	}
		
}
