package com.freshdirect.transadmin.web.model;

import java.util.Date;

public class UnassignedCommand extends BaseCommand {
	
	private String reservationId;
	
	private String orderId;
	
	private String customerId;
	
	private String zone;
	
	private String timeWindow;
	
	private Date createModTime;
	
	private Date unassignedTime;
	
	private String unassignedAction;

	public String getReservationId() {
		return reservationId;
	}

	public void setReservationId(String reservationId) {
		this.reservationId = reservationId;
	}

	public String getUnassignedAction() {
		return unassignedAction;
	}

	public void setUnassignedAction(String unassignedAction) {
		this.unassignedAction = unassignedAction;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getTimeWindow() {
		return timeWindow;
	}

	public void setTimeWindow(String timeWindow) {
		this.timeWindow = timeWindow;
	}

	public Date getCreateModTime() {
		return createModTime;
	}

	public void setCreateModTime(Date createModTime) {
		this.createModTime = createModTime;
	}

	public Date getUnassignedTime() {
		return unassignedTime;
	}

	public void setUnassignedTime(Date unassignedTime) {
		this.unassignedTime = unassignedTime;
	}

}
