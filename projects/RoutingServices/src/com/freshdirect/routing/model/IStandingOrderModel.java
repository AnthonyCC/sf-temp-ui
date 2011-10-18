package com.freshdirect.routing.model;

import java.util.Date;

public interface IStandingOrderModel {
	 
	 String getId();
	 void setId(String id);
	
	 String getSaleId();
	 void setSaleId(String saleId);
	 
	 int getLineItemCount();
	 void setLineItemCount(int lineItemCount) ;
	
	 int getTempLineItemCount();
	 void setTempLineItemCount(int tempLineItemCount);
	
	 ICustomerModel getCustomerModel();
	 void setCustomerModel(ICustomerModel customerModel);
	 
	 String getBatchId();
	 void setBatchId(String batchId);
	 
	 String getArea();
	 void setArea(String area);
	 
	 int getLineItemChangeCount();
	 
	 String getErrorHeader();
	 void setErrorHeader(String errorHeader);
	 
	 Date getAltDeliveryDate();
	 void setAltDeliveryDate(Date altDeliveryDate);
		
	 String getStatus();
	 void setStatus(String status);
}
