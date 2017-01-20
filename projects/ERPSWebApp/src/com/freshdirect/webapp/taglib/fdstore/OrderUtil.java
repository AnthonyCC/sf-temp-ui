package com.freshdirect.webapp.taglib.fdstore;

import java.util.Date;

import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.adapter.FDOrderAdapter;
import com.freshdirect.framework.webapp.ActionResult;

public class OrderUtil {

    public static boolean isModifiable(String orderId, ActionResult results) throws FDResourceException {
        FDOrderAdapter order = (FDOrderAdapter) FDCustomerManager.getOrder(orderId);
        return isModifiable(order.getEStoreId(), order.getSaleStatus(), order.getDeliveryInfo().getDeliveryCutoffTime());
    }

    public static boolean isModifiable(EnumEStoreId eStore, EnumSaleStatus orderStatus, Date deliveryCutoffTime) {
        if (EnumEStoreId.FDX == eStore) {
            if (EnumSaleStatus.INPROCESS.equals(orderStatus) || new Date().after(deliveryCutoffTime)) {
                return false;
            }
        }
        return true;
    }
}
