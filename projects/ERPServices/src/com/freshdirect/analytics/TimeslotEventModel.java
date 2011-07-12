package com.freshdirect.analytics;

import java.io.Serializable;
import java.util.List;


/**
 * 
 * @author tbalumuri
 *
 */

public class TimeslotEventModel extends EventModel {

	private String transactionSource;
	private boolean dlvPassApplied;
	private double deliveryCharge;
	private boolean deliveryChargeWaived;
	private boolean zoneCtActive;
	private boolean filter;
	private List<java.util.List<TimeslotEventDetailModel>> detail;
	
	public TimeslotEventModel(String transactionSource, boolean dlvPassApplied, double deliveryCharge, boolean deliveryChargeWaived, boolean zoneCtActive)
	{
		super();
		this.transactionSource = transactionSource;
		this.dlvPassApplied = dlvPassApplied;
		this.deliveryCharge = deliveryCharge;
		this.deliveryChargeWaived = deliveryChargeWaived;
		this.zoneCtActive = zoneCtActive;
		this.filter = false;
	}
	
	public String getTransactionSource() {
		return transactionSource;
	}
	public void setTransactionSource(String transactionSource) {
		this.transactionSource = transactionSource;
	}
	public boolean isDlvPassApplied() {
		return dlvPassApplied;
	}
	public void setDlvPassApplied(boolean dlvPassApplied) {
		this.dlvPassApplied = dlvPassApplied;
	}
	public double getDeliveryCharge() {
		return deliveryCharge;
	}
	public void setDeliveryCharge(double deliveryCharge) {
		this.deliveryCharge = deliveryCharge;
	}
	public boolean isDeliveryChargeWaived() {
		return deliveryChargeWaived;
	}
	public void setDeliveryChargeWaived(boolean deliveryChargeWaived) {
		this.deliveryChargeWaived = deliveryChargeWaived;
	}
	public boolean isZoneCtActive() {
		return zoneCtActive;
	}
	public void setZoneCtActive(boolean zoneCtActive) {
		this.zoneCtActive = zoneCtActive;
	}
	public List<java.util.List<TimeslotEventDetailModel>> getDetail() {
		return detail;
	}
	public void setDetail(List<java.util.List<TimeslotEventDetailModel>> detail) {
		this.detail = detail;
	}
	public boolean isFilter() {
		return filter;
	}
	public void setFilter(boolean filter) {
		this.filter = filter;
	}
	
	
}
