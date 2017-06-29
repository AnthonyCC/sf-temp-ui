package com.freshdirect.mobileapi.controller.data.response;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.freshdirect.common.pricing.PricingException;
import com.freshdirect.customer.EnumDeliveryType;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.mobileapi.controller.data.DateFormat;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.model.OrderInfo;
import com.freshdirect.mobileapi.model.SessionUser;

public class OrderHistory extends Message {

    private List<Order> orders;
    
    private Integer totalResultCount = 0;
    
    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Integer getTotalResultCount() {
		return totalResultCount;
	}

	public void setTotalResultCount(Integer totalResultCount) {
		this.totalResultCount = totalResultCount;
	}

	public static class Order {

        private String id;

        private Date orderDate;

        private String status;

        private double amount;
        
        private double pendingCreditAmount;
        
    	private double approvedCreditAmount;

        private Timeslot deliveryTimeslot;

        private boolean pendingDeliveryOrder;

        private boolean shoppable;

        private boolean modifiable;

        private EnumDeliveryType deliveryType;

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

		private DeliveryAddress deliveryAddress;

        public Order(OrderInfo orderInfo, SessionUser user) throws PricingException, FDException {
            this.id = orderInfo.getId();
            this.orderDate = orderInfo.getRequestedDate();
            this.status = orderInfo.getOrderStatusName();
            this.amount = orderInfo.getTotal();
            this.deliveryTimeslot = new Timeslot(orderInfo.getDeliveryStartTime(), orderInfo.getDeliveryEndTime(), orderInfo
                    .getDeliveryCutoffTime());
            this.pendingDeliveryOrder = orderInfo.isPendingDeliveryOrder();
            this.refused = orderInfo.isRefused();
            this.shoppable = orderInfo.isShoppable();
            this.modifiable = orderInfo.isModifiable();
            this.deliveryType = orderInfo.getDeliveryType();
            this.pendingCreditAmount = orderInfo.getPendingCreditAmount();
            this.approvedCreditAmount = orderInfo.getApprovedCreditAmount();
            }

        public static List<Order> createOrderList(List<OrderInfo> orderInfos, SessionUser user) {
            List<Order> infos = new ArrayList<Order>();
            Date currentDate = new Date();
            for (OrderInfo orderInfo : orderInfos) {
                try {
                	Date requestedDate = orderInfo.getRequestedDate();
                	int diffInMonths= DateUtil.monthsBetween(currentDate, requestedDate);
                	if(diffInMonths<FDStoreProperties.getOrderHistoryFromInMonths()){
                		infos.add(new Order(orderInfo, user));
                	}
                } catch (PricingException e) {

                } catch (FDException e) {

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
        
        public double getPendingCreditAmount() {
            return pendingCreditAmount;
        }

        public void setPendingCreditAmount(double pendingCreditAmount) {
            this.pendingCreditAmount = pendingCreditAmount;
        }
        
        public double getApprovedCreditAmount() {
            return approvedCreditAmount;
        }

        public void setApprovedCreditAmount(double approvedCreditAmount) {
            this.approvedCreditAmount = approvedCreditAmount;
        }

        public Timeslot getDeliveryTimeslot() {
            return deliveryTimeslot;
        }

        public void setDeliveryTimeslot(Timeslot deliveryTimeslot) {
            this.deliveryTimeslot = deliveryTimeslot;
        }

		public DeliveryAddress getDeliveryAddress() {
			return deliveryAddress;
		}

		public void setDeliveryAddress(DeliveryAddress deliveryAddress) {
			this.deliveryAddress = deliveryAddress;
		}

        public EnumDeliveryType getDeliveryType() {
            return deliveryType;
        }

        public void setDeliveryType(EnumDeliveryType deliveryType) {
            this.deliveryType = deliveryType;
        }

    }

}
