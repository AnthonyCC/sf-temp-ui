package com.freshdirect.backoffice.selfcredit.data;

import java.io.Serializable;
import java.util.List;

public class IssueSelfCreditRequest implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -548378967262846262L;

	private String orderId; 
	private List<SelfCreditOrderItemRequestData> orderLineParams;
	private String note;
	
	public String getOrderId() {
		return orderId;
	}
	
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	public List<SelfCreditOrderItemRequestData> getOrderLineParams() {
		return orderLineParams;
	}
	
	public void setOrderLineParams(List<SelfCreditOrderItemRequestData> orderLineParams) {
		this.orderLineParams = orderLineParams;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
