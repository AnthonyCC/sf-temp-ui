package com.freshdirect.webapp.taglib.fdstore;

import java.util.Date;

import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.adapter.FDOrderAdapter;
import com.freshdirect.framework.webapp.ActionResult;

public class OrderUtil {

    public static boolean isModifiable(String orderId, ActionResult results) throws FDResourceException {
        FDOrderAdapter order = (FDOrderAdapter) FDCustomerManager.getOrder(orderId);
        return isModifiable(order.getDeliveryInfo().getDeliveryCutoffTime(), order.getSaleStatus(), order.getOrderType(), order.isMakeGood());
    }

    public static boolean isModifiable(Date deliveryCutoffTime, EnumSaleStatus orderStatus, EnumSaleType saleType, boolean isMakeGood) {
        if (isMakeGood){
            return false;
        }
        Date now = new Date(); // now
        boolean beforeCutoffTime = now.before(deliveryCutoffTime);

        return (EnumSaleStatus.SUBMITTED.equals(orderStatus) || EnumSaleStatus.AUTHORIZED.equals(orderStatus) || EnumSaleStatus.AVS_EXCEPTION.equals(orderStatus))
                && !EnumSaleType.DONATION.equals(saleType) && beforeCutoffTime;
    }
}
