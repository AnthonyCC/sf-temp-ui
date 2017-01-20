package com.freshdirect.mobileapi.controller.data.response;

import java.util.Date;
import java.util.List;

import com.freshdirect.mobileapi.controller.data.Message;

public class ModifiedOrders extends Message {

    private Date reservationCutoff;
    private List<ModifiedOrder> modifiedOrders;

    public Date getReservationCutoff() {
        return reservationCutoff;
    }

    public void setReservationCutoff(Date reservationCutoff) {
        this.reservationCutoff = reservationCutoff;
    }

    public List<ModifiedOrder> getModifiedOrders() {
        return modifiedOrders;
    }

    public void setModifiedOrders(List<ModifiedOrder> modifiedOrders) {
        this.modifiedOrders = modifiedOrders;
    }

    public static class ModifiedOrder {

        private String orderId;
        private Date modificationCutoffTime;
        private Date deliveryStartTime;
        private Date deliveryEndTime;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public Date getModificationCutoffTime() {
            return modificationCutoffTime;
        }

        public void setModificationCutoffTime(Date modificationCutoffTime) {
            this.modificationCutoffTime = modificationCutoffTime;
        }

        public Date getDeliveryStartTime() {
            return deliveryStartTime;
        }

        public void setDeliveryStartTime(Date deliveryStartTime) {
            this.deliveryStartTime = deliveryStartTime;
        }

        public Date getDeliveryEndTime() {
            return deliveryEndTime;
        }

        public void setDeliveryEndTime(Date deliveryEndTime) {
            this.deliveryEndTime = deliveryEndTime;
        }

    }
}
