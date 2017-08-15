package com.freshdirect.fdstore.orderhistory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.customer.EnumDeliveryType;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.ErpOrderHistory;
import com.freshdirect.customer.ErpOrderHistoryUtil;
import com.freshdirect.customer.ErpSaleInfo;
import com.freshdirect.customer.OrderHistoryI;


public class OrderHistoryService {

    private static final OrderHistoryService INSTANCE = new OrderHistoryService();

    public static OrderHistoryService defaultService() {
        return INSTANCE;
    }

    private OrderHistoryService() {
    }

    private ErpSaleInfo getLastOrderByDeliveryType(OrderHistoryI orderHistory, EnumDeliveryType deliveryType) {
        ErpSaleInfo latestSaleInfo = null;

        if (orderHistory instanceof ErpOrderHistory) {
            Collection<ErpSaleInfo> erpSaleInfos = ((ErpOrderHistory) orderHistory).getErpSaleInfos();
            Date date = null;

            for (Iterator<ErpSaleInfo> iter = erpSaleInfos.iterator(); iter.hasNext();) {
                ErpSaleInfo erpSaleInfo = iter.next();
                Date createDate = erpSaleInfo.getCreateDate();

                if ((date == null || createDate.after(date)) && (deliveryType.equals(erpSaleInfo.getDeliveryType()) && EnumSaleType.REGULAR.equals(erpSaleInfo.getSaleType()))) {
                    date = createDate;
                    latestSaleInfo = erpSaleInfo;
                }
            }
        }

        return latestSaleInfo;
    }

    public Date getLastOrderDateByDeliveryTypes(OrderHistoryI orderHistory, EnumDeliveryType... deliveryTypes) {
        Date requestedDate = null;

        ErpSaleInfo latestErpSaleInfo = getLatestErpSaleInfo(orderHistory, deliveryTypes);

        if (latestErpSaleInfo != null) {
            requestedDate = latestErpSaleInfo.getRequestedDate();
        }

        return requestedDate;
    }

    public String getLastOrderDeliveryZoneByDeliveryTypes(OrderHistoryI orderHistory, EnumDeliveryType... deliveryTypes) {
        String lastZone = null;

        ErpSaleInfo latestErpSaleInfo = getLatestErpSaleInfo(orderHistory, deliveryTypes);

        if (latestErpSaleInfo != null) {
            lastZone = latestErpSaleInfo.getZone();
        }
        return lastZone;
    }

    private ErpSaleInfo getLatestErpSaleInfo(OrderHistoryI orderHistory, EnumDeliveryType... deliveryTypes) {
        List<ErpSaleInfo> latestErpSaleInfos = new ArrayList<ErpSaleInfo>();

        for (EnumDeliveryType deliveryType : deliveryTypes) {
            ErpSaleInfo lastOrderByDeliveryType = getLastOrderByDeliveryType(orderHistory, deliveryType);
            if (lastOrderByDeliveryType != null) {
                latestErpSaleInfos.add(lastOrderByDeliveryType);
            }
        }

        return ErpOrderHistoryUtil.getLastSale(latestErpSaleInfos);
    }

}
