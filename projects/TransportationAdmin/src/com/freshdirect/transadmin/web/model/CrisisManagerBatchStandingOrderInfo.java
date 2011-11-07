package com.freshdirect.transadmin.web.model;

import com.freshdirect.routing.model.ICrisisMngBatchOrder;

public class CrisisManagerBatchStandingOrderInfo implements java.io.Serializable {
	
	private final ICrisisMngBatchOrder model;
	
	public CrisisManagerBatchStandingOrderInfo(ICrisisMngBatchOrder model){
		this.model = model;
	}
	
	public String getStandingOrderId() {
		return this.model.getId();
	}
	
	public String getSaleId() {
		return this.model.getOrderNumber();
	}
	
	public int getLineItemCount() {
		return this.model.getLineItemCount();
	}
	
	public int getTempLineItemCount() {
		return this.model.getTempLineItemCount();
	}
	
	public String getCustomerId() {
		return this.model.getCustomerModel().getErpCustomerPK();
	}
	public String getArea() {
		return this.model.getArea();
	}
		
	public int getLineItemChangeCount(){
		return this.model.getLineItemChangeCount();
	}
	public String getErrorHeader() {
		return this.model.getErrorHeader();
	}
	public String getStatus() {
		return this.model.getStatus();
	}
	public String getOrderStatus() {
		return this.model.getOrderStatus().getStatusCode();
	}

}
