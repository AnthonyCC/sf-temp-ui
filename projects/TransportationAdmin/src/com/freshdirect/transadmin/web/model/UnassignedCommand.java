package com.freshdirect.transadmin.web.model;

import java.util.Date;

import com.freshdirect.transadmin.util.TransStringUtil;

public class UnassignedCommand extends BaseCommand {
	
	private String reservationId;
	
	private String orderId;
	
	private String customerId;
	
	private String zone;
	
	private String timeWindow;
	
	private Date createModTime;
	
	private Date unassignedTime;
	
	private String unassignedAction;
	
	private String updateStatus;
		
	private double orderSize;
		
	private double serviceTime;
	
	private double unassignedOrderSize;
	
	private double unassignedServiceTime;
	
	private boolean manuallyClosed;
	private boolean dynamicActive;
	
	private String isForced;
	
	private String isChefsTable;
	
	
	
	public String getIsForced() {
		return isForced;
	}

	public void setIsForced(String isForced) {
		this.isForced = isForced;
	}

	public String getIsChefsTable() {
		return isChefsTable;
	}

	public void setIsChefsTable(String isChefsTable) {
		this.isChefsTable = isChefsTable;
	}

	public double getOrderSize() {
		return orderSize;
	}

	public void setOrderSize(double orderSize) {
		this.orderSize = orderSize;
	}

	public double getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(double serviceTime) {
		this.serviceTime = serviceTime;
	}
	
	public double getUnassignedOrderSize() {
		return unassignedOrderSize;
	}

	public void setUnassignedOrderSize(double unassignedOrderSize) {
		this.unassignedOrderSize = unassignedOrderSize;
	}

	public double getUnassignedServiceTime() {
		return unassignedServiceTime;
	}

	public void setUnassignedServiceTime(double unassignedServiceTime) {
		this.unassignedServiceTime = unassignedServiceTime;
	}

	public boolean isManuallyClosed() {
		return manuallyClosed;
	}

	public void setManuallyClosed(boolean manuallyClosed) {
		this.manuallyClosed = manuallyClosed;
	}

	public boolean isDynamicActive() {
		return dynamicActive;
	}

	public void setDynamicActive(boolean dynamicActive) {
		this.dynamicActive = dynamicActive;
	}

	public String getUpdateStatus() {
		return updateStatus;
	}

	public void setUpdateStatus(String updateStatus) {
		this.updateStatus = updateStatus;
	}

	
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
	
	public String getFormattedUnassignedTime() {
		return formatDateTime(getUnassignedTime());
	}
	
	public String getFormattedCreateModTime() {
		return formatDateTime(getCreateModTime());
	}
	
	
	
	

	private String formatDateTime(Date dateVal) {
		try {
			return TransStringUtil.getDatewithTime(dateVal);
		} catch (Exception e) {
			return null;
		}
	}

}
