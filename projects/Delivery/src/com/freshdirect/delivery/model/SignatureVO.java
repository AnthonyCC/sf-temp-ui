package com.freshdirect.delivery.model;

import java.io.Serializable;
import java.util.Date;

public class SignatureVO implements Serializable {

	private String orderNo;
	private String deliveredTo;
	private String recipient;
	private boolean contailsAlcohol;
	//private byte[] signature;
	private Date signatureTime;
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getDeliveredTo() {
		return deliveredTo;
	}
	public void setDeliveredTo(String deliveredTo) {
		this.deliveredTo = deliveredTo;
	}
	public String getRecipient() {
		return recipient;
	}
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	public boolean isContailsAlcohol() {
		return contailsAlcohol;
	}
	public void setContailsAlcohol(boolean contailsAlcohol) {
		this.contailsAlcohol = contailsAlcohol;
	}
	
	public Date getSignatureTime() {
		return signatureTime;
	}
	public void setSignatureTime(Date signatureTime) {
		this.signatureTime = signatureTime;
	}
	
	/*public byte[] getSignature() {
		return signature;
	}
	public void setSignature(byte[] signature) {
		this.signature = signature;
	}*/
}
