package com.freshdirect.backoffice.selfcredit.data;

import java.io.Serializable;
import java.util.List;

public class SelfCreditOrderItemRequestData implements Serializable{
	
	private static final long serialVersionUID = 2072350513979639723L;
	
	private String orderLineId;
    private String complaintId;
    private double quantity;
    private List<String> cartonNumbers;
    
	public String getOrderLineId() {
		return orderLineId;
	}
	
	public void setOrderLineId(String orderLineId) {
		this.orderLineId = orderLineId;
	}
	
	public String getComplaintId() {
		return complaintId;
	}
	
	public void setComplaintId(String complaintId) {
		this.complaintId = complaintId;
	}
	
	public double getQuantity() {
		return quantity;
	}
	
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public List<String> getCartonNumbers() {
		return cartonNumbers;
	}

	public void setCartonNumbers(List<String> cartonNumbers) {
		this.cartonNumbers = cartonNumbers;
	}

}
