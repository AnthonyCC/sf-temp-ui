package com.freshdirect.dataloader.payment.reconciliation.paymentech;

import java.io.Serializable;

public class DFRStartEnd implements Serializable {
	private String presenterId;
	private String frequency;
	private String companyId;
	
	public String getPresenterId() {
		return this.presenterId;
	}
	
	public void setPresenterId(String presenterId){
		this.presenterId = presenterId;
	}
	
	public String getFrequency() {
		return this.frequency;
	}
	
	public void setFrequency(String frequency){
		this.frequency = frequency;
	}
	
	public String getCompanyId(){
		return this.companyId;
	}
	
	public void setCompanyId(String companyId){
		this.companyId = companyId;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("Presenter Id: ").append(this.presenterId).append("\n");
		buf.append("Frequency: ").append(this.frequency).append("\n");
		buf.append("Company Id: ").append(this.companyId);
		return buf.toString();
	}
}
