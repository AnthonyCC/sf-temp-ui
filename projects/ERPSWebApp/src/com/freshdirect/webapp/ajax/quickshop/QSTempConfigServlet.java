package com.freshdirect.webapp.ajax.quickshop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.customer.FDProductSelection;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.cart.data.AddToCartItem;
import com.freshdirect.webapp.ajax.cart.data.AddToCartRequestData;
import com.freshdirect.webapp.ajax.product.ProductDetailPopulator;
import com.freshdirect.webapp.ajax.quickshop.data.EnumQuickShopTab;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItem;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.util.RequestUtil;

public class QSTempConfigServlet extends BaseJsonServlet {

	private static final long serialVersionUID = -7678311808717457332L;
	private static final Logger LOG = LoggerFactory.getInstance( QSTempConfigServlet.class );

	@Override
	protected boolean synchronizeOnUser() {
		return false; //no need to synchronize
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
		
		//Parse request data
        AddToCartRequestData reqData = parseRequestData( request, AddToCartRequestData.class );
        // [APPDEV-5353] Fill in required data
        reqData.setCookies(request.getCookies());
        reqData.setRequestUrl(RequestUtil.getFullRequestUrl(request));
        
        List<AddToCartItem> items = reqData.getItems(); 
		if ( items == null ) {
			returnHttpError( 400, "Bad JSON - item is missing" );	// 400 Bad Request
		}
        
        //transform add to cart data into quickshopline item
		QuickShopLineItem item = null;
		AddToCartItem source = items.get(0);
        try {
        	//prepare productSelection in order to reuse QuickShopHelper's createItemCore method
        	ProductModel pm = null;
        	FDProductInfo prodInfo = null;
    		if(source.getSkuCode()!=null){
    			pm = ContentFactory.getInstance().getProduct(source.getSkuCode());
    			prodInfo = FDCachedFactory.getProductInfo(source.getSkuCode());
    		}else{
    			throw new FDResourceException("Cannot create item");
    		}
    		FDConfiguration config = new FDConfiguration(Double.parseDouble(source.getQuantity()), source.getSalesUnit(), source.getConfiguration());
    		
        	FDProductSelection mockSelection = new FDProductSelection(new FDSku(prodInfo.getSkuCode(), prodInfo.getVersion()), pm, config, pm.getUserContext());
        	mockSelection.setCustomerListLineId(source.getLineId());
        	mockSelection.setOrderLineId(source.getLineId());
        	
        	EnumQuickShopTab tab = null;
        	if(reqData.getTab()!=null){
        		tab = EnumQuickShopTab.valueOf(reqData.getTab());        		
        	}
        	
			item = QuickShopHelper.createItemCore(mockSelection, null, null, user, tab).getItem();
			ProductDetailPopulator.postProcessPopulate(user, item, item.getSkuCode());
			item.setListId(source.getListId());
		} catch (FDException e) {
			LOG.error("Cannot create line item. e: " + e);
			returnHttpError(500, "Cannot create line item. e: " + e);
		}
        
        //put item into session
        updateSession(request.getSession(), item);
        
        //prepare the result
        Map<String, QuickShopLineItem> result = new HashMap<String, QuickShopLineItem>();
        result.put("updateItemTemp", item);
        writeResponseData( response, result );
        
	}

	private static void updateSession(HttpSession session, QuickShopLineItem item) {
        Map<String, QuickShopLineItem> tempConfigs = (Map<String, QuickShopLineItem>) session.getAttribute(SessionName.SESSION_QS_CONFIG_REPLACEMENTS);
        
        if(tempConfigs==null){
        	tempConfigs = new HashMap<String, QuickShopLineItem>();
        	session.setAttribute(SessionName.SESSION_QS_CONFIG_REPLACEMENTS, tempConfigs);
        }
        
        tempConfigs.put(item.getItemId(), item);
	}

}
