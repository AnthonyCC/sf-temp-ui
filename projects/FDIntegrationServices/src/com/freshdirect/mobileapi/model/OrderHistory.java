package com.freshdirect.mobileapi.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.OrderHistoryI;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.customer.FDOrderHistory;
import com.freshdirect.fdstore.customer.FDOrderInfoI;

/**
 * Mobile API wrapper for com.freshdirect.fdstore.customer.FDOrderHistory 
 * which implements OrderHistoryI
 */
public class OrderHistory {

    private OrderHistoryI orderHistory;

    public static OrderHistory wrap(OrderHistoryI orderHistoryI) {
        OrderHistory newInstance = new OrderHistory();
        newInstance.orderHistory = orderHistoryI;
        return newInstance;
    }

    public int getValidOrderCount() {
        return orderHistory.getValidOrderCount();
    }

    public int getValidOrderCount(EnumEStoreId storeId) {
        return orderHistory.getValidOrderCount(storeId);
    }

    public List<OrderInfo> getRegularOrderInfos() {
        Collection<FDOrderInfoI> fDOrderInfos = getOrderInfos(EnumSaleType.REGULAR);

        List<OrderInfo> infos = new ArrayList<OrderInfo>();

        for (FDOrderInfoI info : fDOrderInfos) {
            infos.add(OrderInfo.wrap(info));
        }

        return infos;
    }

    private List<FDOrderInfoI> getOrderInfos(EnumSaleType type) {
        return new ArrayList<FDOrderInfoI>(((FDOrderHistory) orderHistory).getFDOrderInfos(type, EnumEStoreId.FDX));
    }

    /**
     * Comparator used for sort by order delivery slot time
     */
    private final static Comparator<FDOrderInfoI> FD_ORDER_INFO_DELIVERY_START_TIME_COMPARATOR = new Comparator<FDOrderInfoI>() {
        @Override
        public int compare(FDOrderInfoI o1, FDOrderInfoI o2) {
            return o2.getDeliveryStartTime().compareTo(o1.getDeliveryStartTime());
        }
    };

    private final static Comparator<OrderInfo> ORDER_INFO_REQUESTED_TIME_COMPARATOR = new Comparator<OrderInfo>() {
        @Override
        public int compare(OrderInfo o1, OrderInfo o2) {
            return o2.getRequestedDate().compareTo(o1.getRequestedDate());
        }
    };

    private final static Comparator<FDOrderInfoI> FD_ORDER_INFO_CUTOFF_TIME_COMPARATOR = new Comparator<FDOrderInfoI>() {
        @Override
        public int compare(FDOrderInfoI o1, FDOrderInfoI o2) {
            return o2.getDeliveryCutoffTime().compareTo(o1.getDeliveryCutoffTime());
        }
    };

    /**
     * Used mostly in homepage where pending and refused orders should be notified to users. 
     */
    public OrderInfo getClosestPendingOrderInfo(Date now) {

        //This should be returned in sorted order. Most recent delivery time to least recent (past)
        List<FDOrderInfoI> fDOrderInfos = getOrderInfos(EnumSaleType.REGULAR);
        Collections.sort(fDOrderInfos, FD_ORDER_INFO_DELIVERY_START_TIME_COMPARATOR);
        OrderInfo closestPendingOrderInfo = null;
        for (FDOrderInfoI info : fDOrderInfos) {
            OrderInfo orderInfo = OrderInfo.wrap(info);

            if (orderInfo.isPendingDeliveryOrder() && !orderInfo.isInPast(now)) {
                closestPendingOrderInfo = orderInfo;
            } else if (orderInfo.isInPast(now)) {
                break;
            }

        }
        return closestPendingOrderInfo;
    }

    public static List<OrderInfo> getCompletedOrderInfos(List<OrderInfo> infos) {
        List<OrderInfo> completedOrder = new ArrayList<OrderInfo>(infos);
        Collections.sort(completedOrder, ORDER_INFO_REQUESTED_TIME_COMPARATOR);
        return completedOrder;
    }

    public List<OrderInfo> getModifiableOrders(Date date) {
        List<OrderInfo> orderInfos = new ArrayList<OrderInfo>();
        List<FDOrderInfoI> fdOrderInfos = getOrderInfos(EnumSaleType.REGULAR);
        Collections.sort(fdOrderInfos, FD_ORDER_INFO_CUTOFF_TIME_COMPARATOR);
        for (FDOrderInfoI info : fdOrderInfos) {
            OrderInfo orderInfo = OrderInfo.wrap(info);
            if (orderInfo.isInPast(date)) {
                break;
            } else if (orderInfo.isModifiable()) {
                orderInfos.add(orderInfo);
            }
        }
        return orderInfos;
    }

}
