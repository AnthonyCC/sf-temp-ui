package com.freshdirect.webapp.ajax.quickshop;


import static com.freshdirect.fdstore.content.EnumQuickShopFilteringValue.DEPT;
import static com.freshdirect.fdstore.content.EnumQuickShopFilteringValue.GLUTEN_FREE;
import static com.freshdirect.fdstore.content.EnumQuickShopFilteringValue.KOSHER;
import static com.freshdirect.fdstore.content.EnumQuickShopFilteringValue.LOCAL;
import static com.freshdirect.fdstore.content.EnumQuickShopFilteringValue.ON_SALE;
import static com.freshdirect.fdstore.content.EnumQuickShopFilteringValue.ORGANIC;
import static com.freshdirect.fdstore.content.EnumQuickShopFilteringValue.STARTER_LISTS;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.FilteringFlowResult;
import com.freshdirect.fdstore.content.FilteringSortingItem;
import com.freshdirect.fdstore.content.FilteringValue;
import com.freshdirect.fdstore.content.QuickShopCacheUtil;
import com.freshdirect.fdstore.content.StarterList;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.FilteringNavigator;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItemWrapper;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopListRequestObject;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopPagerValues;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopReturnValue;

public class QSFromStarterListFilterServlet extends QuickShopServlet {

	private static final long serialVersionUID = -8334161962346998311L;

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getInstance(QSFromStarterListFilterServlet.class);
	
	//create filter
	private static final Set<FilteringValue> filters = new HashSet<FilteringValue>();
	static {
		filters.add(STARTER_LISTS);
		filters.add(DEPT);
		filters.add(GLUTEN_FREE);
		filters.add(KOSHER);
		filters.add(LOCAL);
		filters.add(ORGANIC);
		filters.add(ON_SALE);		
	}
	
	@Override
	protected QuickShopReturnValue process(FDUserI user, HttpSession session, QuickShopListRequestObject requestData ) throws HttpErrorResponse {
		
		QuickShopReturnValue responseData = null;
		try {
			List<StarterList> starterLists = QuickShopHelper.getStarterLists();
			if(StringUtil.isEmpty(requestData.getStarterListId()) && starterLists.size() > 0) {
				requestData.setStarterListId(starterLists.get(0).getContentKey().getId());
			}
			
			//transform request data
			FilteringNavigator nav = requestData.convertToFilteringNavigator();
			FilteringFlowResult<QuickShopLineItemWrapper> result = null;
			
			List<FilteringSortingItem<QuickShopLineItemWrapper>> items = QuickShopCacheUtil.getListFromCache(QuickShopCacheUtil.STARTER_LISTS_CACHE_NAME, QuickShopCacheUtil.STARTER_LISTS_CACHE_KEY);
			
			if(items==null){
				items = QuickShopHelper.getWrappedProductFromStarterList(user, starterLists, QuickShopHelper.getActiveReplacements( session ) );
				if(!items.isEmpty()){
					QuickShopCacheUtil.putListToCache(QuickShopCacheUtil.STARTER_LISTS_CACHE_NAME, QuickShopCacheUtil.STARTER_LISTS_CACHE_KEY,  new ArrayList<FilteringSortingItem<QuickShopLineItemWrapper>>(items));					
				}
			}else{
				items = new ArrayList<FilteringSortingItem<QuickShopLineItemWrapper>>(items);
			}

			QuickShopFilterImpl filter = new QuickShopFilterImpl(nav, user, filters, items, QuickShopHelper.getActiveReplacements( session ));
			result = filter.doFlow(nav, items);
			
			// post-process
			QuickShopHelper.postProcessPopulate( user, result, session );

			responseData = new QuickShopReturnValue(unwrapResult(createPage(requestData, result.getItems())),
														result.getMenu(), 
														new QuickShopPagerValues(requestData.getPageSize(), result.getItems().size(), requestData.getActivePage()),
														generateSorter(nav),
														nav.getSearchTerm(),
														null);
			
			QuickShopMenuOrderUtil.sortMenuItems(responseData.getMenu());
			
			// Generate coremetrics 'element' tags for the menu - selected filters
			generateCoremetricsElementTags( responseData, result.getMenu(), "quickshop | fd_lists" );

		} catch (FDResourceException e) {
			returnHttpError( 500, "Error while collecting starter lists", e );
		}
		
		return responseData;
	}

}
