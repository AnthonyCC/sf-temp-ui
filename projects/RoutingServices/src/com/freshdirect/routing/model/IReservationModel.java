package com.freshdirect.routing.model;

import java.util.Date;

import com.freshdirect.routing.constants.EnumReservationType;

public interface IReservationModel {
	
	public Date getDeliveryDate();
	public void setDeliveryDate(Date deliveryDate);
	
	public String getId();
	public void setId(String id);
	
	public EnumReservationType getType();
	public void setType(EnumReservationType type);
	
	public Date getStartTime();
	public void setStartTime(Date startTime);
	
	public Date getEndTime();
	public void setEndTime(Date endTime);
	
	public Date getCutOffTime();
	public void setCutOffTime(Date cutOffTime);
	
	public Date getExpirationDateTime();
	public void setExpirationDateTime(Date expirationDateTime);
	
	public ICustomerModel getCustomerModel();
	public void setCustomerModel(ICustomerModel customerModel);
	
	public String getAddressId();
	public void setAddressId(String addressId);
	
	public String getOrderId();
	public void setOrderId(String orderId);
	
	public String getArea() ;
	public void setArea(String area);
	
	public boolean isChefsTable();
	public void setChefsTable(boolean chefsTable);
	
	public boolean isForced();
	public void setForced(boolean isForced);
	
	public int getStatusCode();
	public void setStatusCode(int statusCode);
	
	public String getTimeSlotId();
	public void setTimeSlotId(String timeSlotId);
}
