package com.freshdirect.mobileapi.controller.data.response;

import java.text.ParseException;
import java.util.Date;

import com.freshdirect.mobileapi.controller.data.Message;

/**
 * Order review data.
 *   
 * @author Rob
 *
 */
public class Order extends Message {

    private String status;

    private Date modificationCutoffTime;
    
    public boolean isModifiable() {
        return modifiable;
    }

    public void setModifiable(boolean modifiable) {
        this.modifiable = modifiable;
    }

    private boolean modifiable;

    public String getModificationCutoffTime() {
        String formatterDate = null;
        if (this.modificationCutoffTime != null) {
            formatterDate = formatter.format(this.modificationCutoffTime);
        }
        return formatterDate;
    }

    public void setModificationCutoffTime(String modificationCutoffTime) {
        try {
            this.modificationCutoffTime = formatter.parse(modificationCutoffTime);
        } catch (ParseException e) {
            //Do nothing special.
            this.modificationCutoffTime = null;
        }
    }

    public void setModificationCutoffTime(Date modificationCutoffTime) {
        this.modificationCutoffTime = modificationCutoffTime;
    }

    private Date reservationDateTime;

    private String reservationTimeRange;

    private DeliveryAddress deliveryAddress;

    private PaymentMethod paymentMethod;

    private CartDetail cartDetail;

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    public String getReservationDateTime() {
        return (reservationDateTime != null ? formatter.format(reservationDateTime) : null);
    }

    public void setReservationDateTime(String reservationDateTime) throws ParseException {
        this.reservationDateTime = (reservationDateTime != null ? formatter.parse(reservationDateTime) : null);
    }

    public void setReservationDateTime(Date reservationDateTime) {
        this.reservationDateTime = reservationDateTime;
    }

    public DeliveryAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(DeliveryAddress deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public CartDetail getCartDetail() {
        return cartDetail;
    }

    public void setCartDetail(CartDetail cartDetail) {
        this.cartDetail = cartDetail;
    }

    public String getReservationTimeRange() {
        return reservationTimeRange;
    }

    public void setReservationTimeRange(String reservationTimeRange) {
        this.reservationTimeRange = reservationTimeRange;
    }

}
