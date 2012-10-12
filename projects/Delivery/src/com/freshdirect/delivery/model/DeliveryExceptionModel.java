package com.freshdirect.delivery.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeliveryExceptionModel implements Serializable {
	
	private String orderId;
	
	private String returnReason;
	private Date lastRefusedScan;
	private List<String> refusedCartons = new ArrayList();
	
	private boolean isEarlyDeliveryReq;
	private String earlyDlvStatus;
	
	private Date lateBoxScantime;
	private List<String> lateBoxes = new ArrayList();

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getReturnReason() {
		return returnReason;
	}

	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}

	public List<String> getRefusedCartons() {
		return refusedCartons;
	}

	public void setRefusedCartons(List<String> refusedCartons) {
		this.refusedCartons = refusedCartons;
	}

	public boolean isEarlyDeliveryReq() {
		return isEarlyDeliveryReq;
	}

	public void setEarlyDeliveryReq(boolean isEarlyDeliveryReq) {
		this.isEarlyDeliveryReq = isEarlyDeliveryReq;
	}

	public Date getLastRefusedScan() {
		return lastRefusedScan;
	}

	public void setLastRefusedScan(Date lastRefusedScan) {
		this.lastRefusedScan = lastRefusedScan;
	}

	public String getEarlyDlvStatus() {
		return earlyDlvStatus;
	}

	public void setEarlyDlvStatus(String earlyDlvStatus) {
		this.earlyDlvStatus = earlyDlvStatus;
	}

	public Date getLateBoxScantime() {
		return lateBoxScantime;
	}

	public void setLateBoxScantime(Date lateBoxScantime) {
		this.lateBoxScantime = lateBoxScantime;
	}

	public List<String> getLateBoxes() {
		return lateBoxes;
	}

	public void setLateBoxes(List<String> lateBoxes) {
		this.lateBoxes = lateBoxes;
	}
}
