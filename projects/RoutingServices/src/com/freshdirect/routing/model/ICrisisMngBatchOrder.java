package com.freshdirect.routing.model;

import java.util.Date;

import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.routing.constants.EnumDeliveryType;
import com.freshdirect.routing.constants.EnumReservationType;

public interface ICrisisMngBatchOrder {
	
	String getBatchId();
	void setBatchId(String batchId);
	
	ICustomerModel getCustomerModel();
	void setCustomerModel(ICustomerModel customerModel);

	String getArea();	
	void setArea(String area);
	
	Date getDeliveryDate();
	void setDeliveryDate(Date deliveryDate);

	Date getCutOffTime();
	void setCutOffTime(Date cutOffTime);

	Date getStartTime();
	void setStartTime(Date startTime);

	Date getEndTime();
	void setEndTime(Date endTime);

	String getOrderNumber();
	void setOrderNumber(String orderNumber);

	String getErpOrderNumber();
	void setErpOrderNumber(String erpOrderNumber);

	String getOrderAmount();
	void setOrderAmount(String orderAmount);

	EnumReservationType getReservationType();
	void setReservationType(EnumReservationType reservationType);
	
	EnumSaleStatus getOrderStatus();
	void setOrderStatus(EnumSaleStatus orderStatus);
	
	EnumDeliveryType getDeliveryType();
	void setDeliveryType(EnumDeliveryType deliveryType);
	
	boolean isException();
	void setException(boolean isException);
	
	String getAddressId();
	void setAddressId(String addressId);
	
	String getReservationId();
	void setReservationId(String reservationId);
	
	 String getId();
	 void setId(String id);
	
	 int getLineItemCount();
	 void setLineItemCount(int lineItemCount) ;
	
	 int getTempLineItemCount();
	 void setTempLineItemCount(int tempLineItemCount);
	
	 int getLineItemChangeCount();
	 
	 String getErrorHeader();
	 void setErrorHeader(String errorHeader);
	 
	 Date getAltDeliveryDate();
	 void setAltDeliveryDate(Date altDeliveryDate);
		
	 String getStatus();
	 void setStatus(String status);
}
