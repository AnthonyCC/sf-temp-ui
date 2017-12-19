package com.freshdirect.webapp.ajax.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItemWrapper;
import com.freshdirect.webapp.ajax.reorder.QuickShopHelper;
import com.freshdirect.webapp.ajax.reorder.data.EnumQuickShopTab;

public class OrderServlet extends BaseJsonServlet {

	private static final long serialVersionUID = 133898689987220127L;
	private static final String ACTION_GET_ORDER_HISTORY = "orderhistory";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
		try {
			String action = request.getParameter("action");
			if(action == null) {
				return;
			}
			if (action.toLowerCase().equals(ACTION_GET_ORDER_HISTORY)) {
				List<QuickShopLineItemWrapper> pastOrderItems = QuickShopHelper.getWrappedOrderHistoryUsingCache(user, EnumQuickShopTab.PAST_ORDERS, EnumQuickShopTab.PAST_ORDERS.cacheName);
				Map<String, OrderData> result = new HashMap<String, OrderData>();
				if ( pastOrderItems != null) {
					for (QuickShopLineItemWrapper pastOrderItem: pastOrderItems) {
						// check if order has the product info and make sure the product is still available
						if (pastOrderItem == null || pastOrderItem.getOrderId()== null || pastOrderItem.getItem() == null ||
								pastOrderItem.getProduct() == null || 
								!pastOrderItem.getItem().isAvailable() || 
								pastOrderItem.getProduct().isDiscontinued() || 
								pastOrderItem.getProduct().isOutOfSeason()) {
									continue;
								}
						OrderData addedOrderData = result.get(pastOrderItem.getOrderId());
						
						ProductData productData = new ProductData(pastOrderItem.getItem().getSkuCode(), pastOrderItem.getItem().getProductName(), pastOrderItem.getItem().getSalesUnit());
						if (addedOrderData == null) {
							addedOrderData = new OrderData(pastOrderItem.getOrderId(), pastOrderItem.getDeliveryDate(), productData);
							result.put(pastOrderItem.getOrderId(), addedOrderData);
						} else {
							addedOrderData.addProduct(productData);
						}
						
					}
				}
				writeResponseData(response, result.values());
			}
		} catch (FDResourceException e) {
			returnHttpError(500, "Failed to load resource", e);
		} 
	}
	
	@Override
	protected boolean isOAuthEnabled() {
		return true;
	}

	@Override
	protected boolean synchronizeOnUser() {
		return false;
	}
}
