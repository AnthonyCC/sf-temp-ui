package com.freshdirect.mobileapi.controller.data.response;

import java.text.ParseException;
import java.util.Date;

/**
 * @author Rob
 *
 */
public class ModifiedOrder extends Cart {

    private Date modificationCutoffTime;
    private PaymentMethod paymentMethod;
    private DeliveryAddress deliveryAddress;
    private String reservationDateTime;
    private String reservationTimeRange;

    public String getModificationCutoffTime() {
        try {
			return formatter.format(this.modificationCutoffTime);
		} catch (Exception e) {
			return null;
		}
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

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public DeliveryAddress getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(DeliveryAddress deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public String getReservationDateTime() {
		return reservationDateTime;
	}

	public void setReservationDateTime(String reservationDateTime) {
		this.reservationDateTime = reservationDateTime;
	}

	public String getReservationTimeRange() {
		return reservationTimeRange;
	}

	public void setReservationTimeRange(String reservationTimeRange) {
		this.reservationTimeRange = reservationTimeRange;
	}

}
