package com.freshdirect.analytics;

import java.util.Date;
import java.util.List;


/**
 * 
 * @author tbalumuri
 *
 */

public class TimeslotEventModel extends EventModel {

	
	private String orderId;
	private String customerId;
	private EventType eventType;
	private Date eventDate;
	private int responseTime;
	private String address;
	private String reservationId;
	private String transactionSource;
	private boolean dlvPassApplied;
	private double deliveryCharge;
	private boolean deliveryChargeWaived;
	private boolean zoneCtActive;
	private String id;
	private boolean filter;
	private long logoutTime;
	private String sector;
	private double latitude;
	private double longitude;
	private String serviceType;
	private List<TimeslotEventDetailModel> detail;
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

	public TimeslotEventModel() {
		// TODO Auto-generated constructor stub
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
	public List<TimeslotEventDetailModel> getDetail() {
		return detail;
	}
	public void setDetail(List<TimeslotEventDetailModel> detail) {
		this.detail = detail;
	}
	public boolean isFilter() {
		return filter;
	}
	public void setFilter(boolean filter) {
		this.filter = filter;
	}

	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}

	public String getReservationId() {
		return reservationId;
	}

	public void setReservationId(String reservationId) {
		this.reservationId = reservationId;
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

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	public int getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(int responseTime) {
		this.responseTime = responseTime;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(long logoutTime) {
		this.logoutTime = logoutTime;
	}
	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
}
