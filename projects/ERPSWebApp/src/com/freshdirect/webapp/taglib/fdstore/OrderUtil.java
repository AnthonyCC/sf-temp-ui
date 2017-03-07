package com.freshdirect.webapp.taglib.fdstore;

import java.util.Date;

import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.adapter.FDOrderAdapter;

public class OrderUtil {

    // TODO: refreshed order within cutofftime because order history cache provide stale order data sometimes 
    // (order state is set SUBmitted from NEW state when order is confirmed by SAP in DB).
    public static boolean isModifiable(String orderId, Date deliveryCutoffTime) {
        boolean modifiable = false;
        
        if (new Date().before(deliveryCutoffTime)) {
            try {
                FDOrderAdapter updatedOrder = (FDOrderAdapter) FDCustomerManager.getOrder(orderId);
                modifiable = isModifiable(updatedOrder.getDeliveryInfo().getDeliveryCutoffTime(), updatedOrder.getSaleStatus(), updatedOrder.getOrderType(),
                        updatedOrder.isMakeGood());
            } catch (FDResourceException e) {
                modifiable = false;
            }
        }

        return modifiable;
    }

    private static boolean isModifiable(Date deliveryCutoffTime, EnumSaleStatus orderStatus, EnumSaleType saleType, boolean isMakeGood) {
        if (isMakeGood) {
            return false;
        }
        Date now = new Date(); // now
        boolean beforeCutoffTime = now.before(deliveryCutoffTime);

        return (EnumSaleStatus.NEW.equals(orderStatus) || EnumSaleStatus.SUBMITTED.equals(orderStatus) || EnumSaleStatus.AUTHORIZED.equals(orderStatus) || EnumSaleStatus.AVS_EXCEPTION.equals(orderStatus))
                && !EnumSaleType.DONATION.equals(saleType) && beforeCutoffTime;
    }
    
    /*private static boolean isModifiable(Date deliveryCutoffTime, EnumSaleStatus orderStatus, EnumSaleType saleType, boolean isMakeGood, String orderId) {
        if (isMakeGood) {
            return false;
        }
        Date now = new Date(); // now

		try {
			FDOrderAdapter order = (FDOrderAdapter) FDCustomerManager
					.getOrder(orderId);
			if (EnumEStoreId.FDX.name().equalsIgnoreCase(
					order.getEStoreId().name())) {
				if (EnumSaleStatus.INPROCESS.equals(order.getSaleStatus())
						|| now.after(order.getDeliveryInfo()
								.getDeliveryCutoffTime())) {
					return false;
				}
			}
		} catch (FDResourceException e) {
			return false;
		}

		return true;

	}*/
}
