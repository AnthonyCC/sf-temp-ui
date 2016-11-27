package com.freshdirect.webapp.ajax.reorder;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.standingorders.FDStandingOrdersManager;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.BaseJsonServlet;

public class QuickShopTabMetaServlet extends BaseJsonServlet{

	private static final long serialVersionUID = -2167113790077214304L;
	
	private static final Logger LOG = LoggerFactory.getInstance(QuickShopTabMetaServlet.class);
	
	@Override
	protected boolean synchronizeOnUser() {
		return false; //no need to synchronize
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
		
		Map<String, Map<String, Integer>> result = new HashMap<String, Map<String, Integer>>();
		Map<String, Integer> meta = new HashMap<String, Integer>();
		
		try {
			meta.put("topitems", QuickShopHelper.getTopItemsCount(user));
		} catch (FDResourceException exception) {
			LOG.error("Can't calculate top items tab meta! e: " + exception);
		}
		
		//past orders tab
		try {
			meta.put("pastorders", QuickShopHelper.getOrderCount(user));
		} catch (FDResourceException e) {
			LOG.error("Can't calculate past orders tab meta! e: " + e);
		}
	
		//shop from lists tab
		try {
			meta.put("lists", QuickShopHelper.getListCount(user));
		} catch (FDResourceException e) {
			LOG.error("Can't calculate custom lists tab meta! e: " + e);
		}
		
		//standing orders tab
		try {
			meta.put("so", FDStandingOrdersManager.getInstance().loadCustomerStandingOrders( user.getIdentity()).size());
		} catch (FDResourceException e) {
			LOG.error("Can't calculate standing orders tab meta! e: " + e);
		}
		
		result.put("tabMeta",meta);
		
		writeResponseData(response, result);
		
	}

}
