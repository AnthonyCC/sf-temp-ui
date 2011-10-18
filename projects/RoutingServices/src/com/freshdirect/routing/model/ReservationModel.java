package com.freshdirect.routing.model;

import java.util.Date;

import com.freshdirect.routing.constants.EnumReservationType;

public class ReservationModel extends BaseModel implements IReservationModel {
	
	private String id;	
	private EnumReservationType type;
	private Date startTime;
	private Date endTime;
	private Date cutOffTime;
	private Date expirationDateTime;	
	private String addressId;
	private String orderId;
	private String area;
	private boolean chefsTable;
	private boolean isForced;
	private int statusCode;
	private Date deliveryDate;
	private ICustomerModel customerModel;
	private String timeSlotId;
	
	public String getTimeSlotId() {
		return timeSlotId;
	}
	public void setTimeSlotId(String timeSlotId) {
		this.timeSlotId = timeSlotId;
	}
	public Date getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public EnumReservationType getType() {
		return type;
	}
	public void setType(EnumReservationType type) {
		this.type = type;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Date getCutOffTime() {
		return cutOffTime;
	}
	public void setCutOffTime(Date cutOffTime) {
		this.cutOffTime = cutOffTime;
	}
	public Date getExpirationDateTime() {
		return expirationDateTime;
	}
	public void setExpirationDateTime(Date expirationDateTime) {
		this.expirationDateTime = expirationDateTime;
	}
	
	public ICustomerModel getCustomerModel() {
		return customerModel;
	}
	public void setCustomerModel(ICustomerModel customerModel) {
		this.customerModel = customerModel;
	}
	
	public String getAddressId() {
		return addressId;
	}
	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public boolean isChefsTable() {
		return chefsTable;
	}
	public void setChefsTable(boolean chefsTable) {
		this.chefsTable = chefsTable;
	}
	public boolean isForced() {
		return isForced;
	}
	public void setForced(boolean isForced) {
		this.isForced = isForced;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
}
