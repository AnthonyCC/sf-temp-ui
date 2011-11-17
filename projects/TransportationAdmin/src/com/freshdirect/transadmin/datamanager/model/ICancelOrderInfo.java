package com.freshdirect.transadmin.datamanager.model;

public interface ICancelOrderInfo {
	
	public String getCustomerId();
	public void setCustomerId(String customerId);
	
	public String getFirstName() ;
	public void setFirstName(String firstName);
	
	public String getLastName();
	public void setLastName(String lastName);
	
	public String getPhoneNumber();
	public void setPhoneNumber(String phoneNumber);
	
	public String getEmail();
	public void setEmail(String email);
	
	public String getCompanyName();
	public void setCompanyName(String companyName);
	
	public String getOrderNumber() ;
	public void setOrderNumber(String orderNumber) ;
	
	public String getDeliveryWindow() ;
	public void setDeliveryWindow(String deliveryWindow);
	
	public String getBusinessPhone();
	public void setBusinessPhone(String businessPhone);
	
	public String getCellPhone();
	public void setCellPhone(String cellPhone);
	
	String getFullName();
	
	String getOrderStatus();
	void setOrderStatus(String orderStatus);
	
	 int getLineItemCount();
	 void setLineItemCount(int lineItemCount) ;
	
	 int getTempLineItemCount();
	 void setTempLineItemCount(int tempLineItemCount);
	
	 int getLineItemChangeCount();
 
	 String getStandingOrderId();
	 void setStandingOrderId(String standingOrderId);
	 
	 String getErrorDetail();
     void setErrorDetail(String errorDetail);
}
