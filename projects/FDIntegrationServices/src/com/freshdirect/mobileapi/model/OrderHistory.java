package com.freshdirect.mobileapi.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.OrderHistoryI;
import com.freshdirect.fdstore.customer.FDOrderHistory;
import com.freshdirect.fdstore.customer.FDOrderInfoI;

/**
 * Mobile API wrapper for com.freshdirect.fdstore.customer.FDOrderHistory 
 * which implements OrderHistoryI
 * 
 * @author Rob
 *
 */
public class OrderHistory {

    private OrderHistoryI orderHistory;

    public static OrderHistory wrap(OrderHistoryI orderHistoryI) {
        OrderHistory newInstance = new OrderHistory();
        newInstance.orderHistory = orderHistoryI;
        return newInstance;
    }

    /**
     * @return
     */
    public int getValidOrderCount() {
        return orderHistory.getValidOrderCount();
    }

    /**
     * @return
     */
    public List<OrderInfo> getRegularOrderInfos() {
        Collection<FDOrderInfoI> fDOrderInfos = getOrderInfos(EnumSaleType.REGULAR);

        List<OrderInfo> infos = new ArrayList<OrderInfo>();

        for (FDOrderInfoI info : fDOrderInfos) {
            infos.add(OrderInfo.wrap(info));
        }

        return infos;
    }

    private List<FDOrderInfoI> getOrderInfos(EnumSaleType type) {
        return new ArrayList<FDOrderInfoI>(((FDOrderHistory) orderHistory).getFDOrderInfos(type));
    }

    /**
     * Comparator used for sort by order delivery slot time
     */
    private final static Comparator HOME_ORDER_COMPARATOR = new Comparator() {
        public int compare(Object o1, Object o2) {
            return ((FDOrderInfoI) o2).getDeliveryStartTime().compareTo(((FDOrderInfoI) o1).getDeliveryStartTime());
        }
    };

    private final static Comparator ORDER_INFO_COMPARATOR = new Comparator() {
        public int compare(Object o1, Object o2) {
            return ((OrderInfo) o2).getRequestedDate().compareTo(((OrderInfo) o1).getRequestedDate());
        }
    };

    /**
     * Used mostly in homepage where pending and refused orders should be notified to users. 
     * @return
     */
    public OrderInfo getClosestPendingOrderInfo(Date now) {

        //This should be returned in sorted order. Most recent delivery time to least recent (past)
        List<FDOrderInfoI> fDOrderInfos = getOrderInfos(EnumSaleType.REGULAR);
        Collections.sort(fDOrderInfos, HOME_ORDER_COMPARATOR);
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

    /**
     * @return
     */
    public static List<OrderInfo> getCompletedOrderInfos(List<OrderInfo> infos) {
        List<OrderInfo> completedOrder = new ArrayList<OrderInfo>();

        for (OrderInfo info : infos) {
            info.getOrderStatus();
            if (!info.isPending()) {
                completedOrder.add(info);
            }
        }
        Collections.sort(completedOrder, ORDER_INFO_COMPARATOR);

        return completedOrder;
    }

}
