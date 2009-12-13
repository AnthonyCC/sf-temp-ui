package com.freshdirect.mobileapi.controller.data.response;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.freshdirect.common.pricing.PricingException;
import com.freshdirect.mobileapi.controller.data.DateFormat;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.model.OrderInfo;

/**
 * 
 * @author fgarcia
 *
 */
public class OrderHistory extends Message {

    private List<Order> orders;

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public static class Order {

        private String id;

        private Date orderDate;

        private String status;

        private double amount;

        private Timeslot deliveryTimeslot;

        private boolean pendingDeliveryOrder;

        private boolean shoppable;

        private boolean modifiable;

        public boolean isShoppable() {
            return shoppable;
        }

        public boolean isModifiable() {
            return modifiable;
        }

        public boolean isPendingDeliveryOrder() {
            return pendingDeliveryOrder;
        }

        public boolean isRefused() {
            return refused;
        }

        private boolean refused;

        /**
         * @param orderInfo
         * @throws PricingException
         */
        public Order(OrderInfo orderInfo) throws PricingException {
            this.id = orderInfo.getId();
            this.orderDate = orderInfo.getRequestedDate();
            this.status = orderInfo.getOrderStatus();
            this.amount = orderInfo.getTotal();
            this.deliveryTimeslot = new Timeslot(orderInfo.getDeliveryStartTime(), orderInfo.getDeliveryEndTime(), orderInfo
                    .getDeliveryCutoffTime());
            this.pendingDeliveryOrder = orderInfo.isPendingDeliveryOrder();
            this.refused = orderInfo.isRefused();
            this.shoppable = orderInfo.isShoppable();
            this.modifiable = orderInfo.isModifiable();
        }

        /**
         * @param orderInfos
         * @return
         * @throws PricingException
         */
        public static List<Order> createOrderList(List<OrderInfo> orderInfos) {
            List<Order> infos = new ArrayList<Order>();
            for (OrderInfo orderInfo : orderInfos) {
                try {
                    infos.add(new Order(orderInfo));
                } catch (PricingException e) {

                }
            }
            return infos;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        private SimpleDateFormat formatter = new SimpleDateFormat(DateFormat.STANDARDIZED_DATE_FORMAT);

        public String getOrderDate() {
            String formatterDate = null;
            if (orderDate != null) {
                formatterDate = formatter.format(orderDate);
            }
            return formatterDate;
        }

        public void setOrderDate(Date orderDate) {
            this.orderDate = orderDate;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public Timeslot getDeliveryTimeslot() {
            return deliveryTimeslot;
        }

        public void setDeliveryTimeslot(Timeslot deliveryTimeslot) {
            this.deliveryTimeslot = deliveryTimeslot;
        }

    }

}
