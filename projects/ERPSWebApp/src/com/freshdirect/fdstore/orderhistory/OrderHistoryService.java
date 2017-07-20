package com.freshdirect.fdstore.orderhistory;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.customer.EnumDeliveryType;
import com.freshdirect.customer.ErpOrderHistory;
import com.freshdirect.customer.ErpSaleInfo;
import com.freshdirect.customer.OrderHistoryI;
import com.freshdirect.framework.util.log.LoggerFactory;


public class OrderHistoryService {

    private static final Category LOGGER = LoggerFactory.getInstance(OrderHistoryService.class);

    public static OrderHistoryService defaultService() {
        return INSTANCE;
    }

    private static final OrderHistoryService INSTANCE = new OrderHistoryService();

    public ErpSaleInfo getLastOrderByDeliveryType(OrderHistoryI orderHistory, EnumDeliveryType deliveryType) {
        ErpSaleInfo latestSaleInfo = null;

        if (orderHistory instanceof ErpOrderHistory) {
            List<ErpSaleInfo> erpSaleInfos = (List<ErpSaleInfo>) ((ErpOrderHistory) orderHistory).getErpSaleInfos();
            Date date = null;

            for (Iterator<ErpSaleInfo> iter = erpSaleInfos.iterator(); iter.hasNext();) {
                ErpSaleInfo erpSaleInfo = iter.next();
                Date createDate = erpSaleInfo.getCreateDate();

                if ((date == null || createDate.after(date)) && deliveryType.equals(erpSaleInfo.getDeliveryType())) {
                    date = createDate;
                    latestSaleInfo = erpSaleInfo;
                }
            }
        }

        return latestSaleInfo;
    }

    public Date getLastOrderDateByDeliveryType(OrderHistoryI orderHistory, EnumDeliveryType deliveryType) {
        Date requestedDate = null;
        ErpSaleInfo erpSaleInfo = getLastOrderByDeliveryType(orderHistory, deliveryType);

        if (erpSaleInfo != null) {
            requestedDate = erpSaleInfo.getRequestedDate();
        }

        return requestedDate;
    }

    public String getLastOrderDeliveryZoneByDeliveryType(OrderHistoryI orderHistory, EnumDeliveryType deliveryType) {
        String lastZone = null;
        ErpSaleInfo erpSaleInfo = getLastOrderByDeliveryType(orderHistory, deliveryType);

        if (erpSaleInfo != null) {
            lastZone = erpSaleInfo.getZone();
        }
        return lastZone;
    }

    public Date getLastOrderDateByDeliveryTypeForCollectedFD(OrderHistoryI orderHistory) {
        Date requestedDate = null;
        ErpSaleInfo erpSaleInfo = loadLatestErpSaleInfoForFD(orderHistory);

        if (erpSaleInfo != null) {
            requestedDate = erpSaleInfo.getRequestedDate();
        }

        return requestedDate;
    }

    public String getLastOrderDeliveryZoneByDeliveryTypeForCollectedFD(OrderHistoryI orderHistory) {
        String lastZone = null;
        ErpSaleInfo erpSaleInfo = loadLatestErpSaleInfoForFD(orderHistory);

        if (erpSaleInfo != null) {
            lastZone = erpSaleInfo.getZone();
        }

        return lastZone;
    }

    private ErpSaleInfo loadLatestErpSaleInfoForFD(OrderHistoryI orderHistory) {
        List<ErpSaleInfo> latestErpSaleInfos = new ArrayList<ErpSaleInfo>();
        ErpSaleInfo latestErpSaleInfo=null;

        latestErpSaleInfos.add(getLastOrderByDeliveryType(orderHistory, EnumDeliveryType.HOME));
        latestErpSaleInfos.add(getLastOrderByDeliveryType(orderHistory, EnumDeliveryType.PICKUP));
        latestErpSaleInfos.add(getLastOrderByDeliveryType(orderHistory, EnumDeliveryType.DEPOT));

        latestErpSaleInfo = latestErpSaleInfos.get(0);
        
        for (ErpSaleInfo erpSaleInfo : latestErpSaleInfos) {
            if (erpSaleInfo != null && (erpSaleInfo.getCreateDate().after(latestErpSaleInfo.getCreateDate()))) {
                latestErpSaleInfo = erpSaleInfo;
            }
        }
        
        return latestErpSaleInfo;
    }

}
