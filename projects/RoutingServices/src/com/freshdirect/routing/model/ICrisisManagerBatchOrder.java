package com.freshdirect.routing.model;

import java.util.Date;

import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.routing.constants.EnumDeliveryType;
import com.freshdirect.routing.constants.EnumReservationType;

public interface ICrisisManagerBatchOrder {
	
	String getBatchId();
	void setBatchId(String batchId);
	
	String getArea();	
	void setArea(String area);

	String getFirstName();
	void setFirstName(String firstName);	

	String getLastName();
	void setLastName(String lastName);
	
	String getErpCustomerPK();
	void setErpCustomerPK(String erpCustomerPK);
	
	String getFdCustomerPK();	
	void setFdCustomerPK(String fdCustomerPK);

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

	String getEmail();
	void setEmail(String email);

	String getAmount();
	void setAmount(String amount);

	EnumReservationType getReservationType();
	void setReservationType(EnumReservationType reservationType);
	
	EnumSaleStatus getOrderStatus();
	void setOrderStatus(EnumSaleStatus orderStatus);
	
	EnumDeliveryType getDeliveryType();
	void setDeliveryType(EnumDeliveryType deliveryType);
	
	String getHomePhone();
	void setHomePhone(String homePhone);

	String getBusinessPhone();
	void setBusinessPhone(String businessPhone);

	String getCellPhone();
	void setCellPhone(String cellPhone);
	
	String getStandingOrderId();
	void setStandingOrderId(String standingOrderId);
	
	boolean isException();
	void setException(boolean isException);
	
	String getAddressId();
	void setAddressId(String addressId);
	
	String getBusinessExt();
	void setBusinessExt(String businessExt);
	
	String getCompanyName();
	void setCompanyName(String companyName);
	
	String getReservationId();
	void setReservationId(String reservationId);
}
