package com.freshdirect.mobileapi.controller.data.response;

import java.text.ParseException;
import java.util.Date;

public class ModifiedOrder extends Cart {

    private Date modificationCutoffTime;
    private PaymentMethod paymentMethod;
    private DeliveryAddress deliveryAddress;
    private Date reservationTime;
    private String reservationTimeRange;
    private int viewCount = -1; 
	private int viewCountLimit = -1; 
	private String media = ""; 
	private boolean showingOrderModifyOverlay = false;

    public Date getModificationCutoffTimeMs() {
        return modificationCutoffTime;
    }
    
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

	public Date getReservationTimeMs() {
	    return reservationTime;
	}
	
	public void setReservationTime(Date reservationTime) {
	    this.reservationTime = reservationTime;
	}

	public String getReservationDateTime() {
		return (reservationTime != null ? formatter.format(reservationTime) : null);
	}

	public void setReservationDateTime(String reservationDateTime) throws ParseException {
	    this.reservationTime = (reservationDateTime != null ? formatter.parse(reservationDateTime) : null);
	}

	public String getReservationTimeRange() {
		return reservationTimeRange;
	}

	public void setReservationTimeRange(String reservationTimeRange) {
		this.reservationTimeRange = reservationTimeRange;
	}

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	public int getViewCountLimit() {
		return viewCountLimit;
	}

	public void setViewCountLimit(int viewCountLimit) {
		this.viewCountLimit = viewCountLimit;
	}

	public String getMedia() {
		return media;
	}

	public void setMedia(String media) {
		this.media = media;
	}

	public boolean isShow() {
		return showingOrderModifyOverlay;
	}

	public void setShow(boolean show) {
		this.showingOrderModifyOverlay = show;
	}

}
