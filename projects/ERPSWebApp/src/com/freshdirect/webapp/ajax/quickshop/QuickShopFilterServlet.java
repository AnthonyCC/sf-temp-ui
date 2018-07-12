package com.freshdirect.webapp.ajax.quickshop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.FilteringFlowResult;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.FilteringNavigator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItemWrapper;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopListRequestObject;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopPagerValues;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopReturnValue;

/**
 * Deprecated with Quickshop 2.2 version. Replaced with {@link com.freshdirect.webapp.ajax.reorder.QuickShopFilterServlet}
 */
@Deprecated
public class QuickShopFilterServlet extends QuickShopServlet {

	private static final long serialVersionUID = -2275457565029351766L;
	
	private static final Logger LOG = LoggerFactory.getInstance(QuickShopFilterServlet.class);
			
	@Override
	protected boolean synchronizeOnUser() {
		return false; //no need to synchronize
	}
	
	@Override
    protected QuickShopReturnValue process(FDUserI user, HttpSession session, QuickShopListRequestObject requestData, HttpServletRequest request) throws HttpErrorResponse {
		
		LOG.info("Start processing request...");
		QuickShopReturnValue responseData = null;
		FilteringNavigator nav = requestData.convertToFilteringNavigator();
		
		try {
            FilteringFlowResult<QuickShopLineItemWrapper> result = QuickShopHelper.getQuickShopPastOrderItems(user, session, requestData, nav, request);
	
			responseData = new QuickShopReturnValue(unwrapResult(createPage(requestData, result.getItems())),
					result.getMenu(), 
					new QuickShopPagerValues(requestData.getPageSize(), result.getItems().size(), requestData.getActivePage()),
					generateSorter(nav),
					nav.getSearchTerm(),
					null);
			
			// sorting menu items where needed
			QuickShopMenuOrderUtil.sortMenuItems(responseData.getMenu());
			
		} catch (FDResourceException e) {
			LOG.error("Error while collecting order history", e);
			returnHttpError( 500, "Error while collecting order history", e );
		}		
		return responseData;
	}
	
}
