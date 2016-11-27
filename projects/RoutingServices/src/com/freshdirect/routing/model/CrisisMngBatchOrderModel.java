package com.freshdirect.routing.model;

import java.util.Date;

public class CrisisMngBatchOrderModel extends CrisisMngBatchOrder {
		
	private String id;	
	private int lineItemCount;
	private int tempLineItemCount;
	private ICustomerModel customerModel;
	private String errorHeader;
	private Date altDeliveryDate;
	private String status;
		
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getLineItemCount() {
		return lineItemCount;
	}
	public void setLineItemCount(int lineItemCount) {
		this.lineItemCount = lineItemCount;
	}
	public int getTempLineItemCount() {
		return tempLineItemCount;
	}
	public void setTempLineItemCount(int tempLineItemCount) {
		this.tempLineItemCount = tempLineItemCount;
	}
	public ICustomerModel getCustomerModel() {
		return customerModel;
	}
	public void setCustomerModel(ICustomerModel customerModel) {
		this.customerModel = customerModel;
	}	
	public int getLineItemChangeCount(){
		return this.lineItemCount - this.tempLineItemCount;
	}
	public String getErrorHeader() {
		return errorHeader;
	}
	public void setErrorHeader(String errorHeader) {
		this.errorHeader = errorHeader;
	}
	public Date getAltDeliveryDate() {
		return altDeliveryDate;
	}
	public void setAltDeliveryDate(Date altDeliveryDate) {
		this.altDeliveryDate = altDeliveryDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}	
	
}
