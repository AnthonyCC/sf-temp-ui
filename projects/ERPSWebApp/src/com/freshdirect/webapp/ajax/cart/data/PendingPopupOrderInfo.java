package com.freshdirect.webapp.ajax.cart.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.freshdirect.fdlogistics.model.FDTimeslot;

public class PendingPopupOrderInfo implements Serializable {
	
	private static final long	serialVersionUID	= -1823201329915341752L;
	
	private String erpSalesId;
	private Date requestedDate;
	private Date deliveryStartTime;
	private Date deliveryEndTime;
	private Date deliveryCutoffTime;
	
	
	public String getErpSalesId() {
		return erpSalesId;
	}
	public void setErpSalesId(String erpSalesId) {
		this.erpSalesId = erpSalesId;
	}
	public Date getRequestedDate() {
		return requestedDate;
	}
	public void setRequestedDate(Date requestedDate) {
		this.requestedDate = requestedDate;
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
	public String getFormattedDeliveryTime() {
		return new SimpleDateFormat("EEEE, MM/dd/yyyy").format(requestedDate) +" "+ FDTimeslot.format(deliveryStartTime, deliveryEndTime);
	}
	public void setDeliveryEndTime(Date deliveryEndTime) {
		this.deliveryEndTime = deliveryEndTime;
	}
	public Date getDeliveryCutoffTime() {
		return deliveryCutoffTime;
	}
	public void setDeliveryCutoffTime(Date deliveryCutoffTime) {
		this.deliveryCutoffTime = deliveryCutoffTime;
	}	

}
