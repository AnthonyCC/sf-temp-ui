package com.freshdirect.routing.model;

import java.util.Date;

public class StandingOrderModel extends BaseModel implements IStandingOrderModel{
		
	private static final long serialVersionUID = 4837425644599397996L;
	
	private String id;
	private String saleId;
	private int lineItemCount;
	private int tempLineItemCount;
	private ICustomerModel customerModel;
	private String area;
	private String errorHeader;
	private Date altDeliveryDate;
	private String status;
	
	private String batchId;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSaleId() {
		return saleId;
	}
	public void setSaleId(String saleId) {
		this.saleId = saleId;
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
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}	
	public int getLineItemChangeCount(){
		return this.tempLineItemCount - this.lineItemCount;
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
