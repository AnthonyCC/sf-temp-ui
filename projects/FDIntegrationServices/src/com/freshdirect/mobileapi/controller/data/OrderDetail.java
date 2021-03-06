package com.freshdirect.mobileapi.controller.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.freshdirect.mobileapi.controller.data.response.CartDetail;
import com.freshdirect.mobileapi.controller.data.response.DeliveryAddress;
import com.freshdirect.mobileapi.controller.data.response.PaymentMethod;

public class OrderDetail implements DateFormat{
	protected final SimpleDateFormat formatter = new SimpleDateFormat(STANDARDIZED_DATE_FORMAT);

	private String orderNumber;
	
	private String status;

    private Date modificationCutoffTime;
    
    private String deliveryZone;
    
    private Date reservationDateTime;

    private String reservationTimeRange;

    private DeliveryAddress deliveryAddress;

    private PaymentMethod paymentMethod;

    private CartDetail cartDetail;
    
    private Integer modifycount;
    
    public Integer getModifycount() {
		return modifycount;
	}

	public void setModifycount(Integer modifycount) {
		this.modifycount = modifycount;
	}
    
    public String getDeliveryZone() {
		return deliveryZone;
	}

	public void setDeliveryZone(String deliveryZone) {
		this.deliveryZone = deliveryZone;
	}

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

    public String getStatus() {
        return status;
    }

   
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

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

}
