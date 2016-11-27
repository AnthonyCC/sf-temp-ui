package com.freshdirect.webapp.taglib.fdstore;

import java.util.Date;

import org.apache.log4j.Category;

import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.adapter.FDOrderAdapter;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;

public class OrderUtil {
	
	private static Category LOGGER = LoggerFactory.getInstance( OrderUtil.class );
	
	public static boolean isModifiable(String orderId, ActionResult results) throws FDResourceException{
		
		FDOrderAdapter order = (FDOrderAdapter) FDCustomerManager.getOrder(orderId);
		
		if(EnumEStoreId.FDX.name().equalsIgnoreCase(order.getEStoreId().name())){
			if (EnumSaleStatus.INPROCESS.equals(order.getSaleStatus()) || new Date().after(order.getDeliveryInfo().getDeliveryCutoffTime())) {
				return false;
			}
		}
		return true;
		
	}

	
}
