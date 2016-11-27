package com.freshdirect.webapp.taglib.fdstore;

import java.util.Date;

import com.freshdirect.fdlogistics.model.FDDeliveryETAModel;
import com.freshdirect.fdstore.customer.FDOrderInfoI;
import com.freshdirect.framework.util.TimeOfDay;

public class DeliveryETAInfo {
	
	private final String orderNo;
	private final Date deliveryDate;
	private final TimeOfDay now;
	
	private boolean hasMultipleScheduledOrders;
	private Date nextEarliestETAStart;
	private Date nextEarliestETAEnd;
	
	public DeliveryETAInfo (FDOrderInfoI orderInfo, FDDeliveryETAModel etaInfo) {
		this.orderNo = orderInfo.getErpSalesId();
		this.deliveryDate = orderInfo.getRequestedDate();
		this.nextEarliestETAStart = etaInfo.getStartTime();
		this.nextEarliestETAEnd = etaInfo.getEndTime();
		this.now = new TimeOfDay(new Date());
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public boolean isHasMultipleScheduledOrders() {
		return hasMultipleScheduledOrders;
	}

	public void setHasMultipleScheduledOrders(boolean hasMultipleScheduledOrders) {
		this.hasMultipleScheduledOrders = hasMultipleScheduledOrders;
	}

	public Date getNextEarliestETAStart() {
		return nextEarliestETAStart;
	}

	public Date getNextEarliestETAEnd() {
		return nextEarliestETAEnd;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public TimeOfDay getNow() {
		return now;
	}
}
