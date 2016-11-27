package com.freshdirect.dataloader.payment.reconciliation.paymentech;

import java.io.Serializable;
import java.util.Date;

import com.freshdirect.framework.util.TimeOfDay;

public class PaymentechHeader implements Serializable {
	
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final EnumPaymentechRecordType recordType;
	private String companyId;
	private Date fromDate;
	private Date toDate;
	private Date generationDate;
	private TimeOfDay generationTime;
	
	public PaymentechHeader(EnumPaymentechRecordType recordType){
		this.recordType = recordType;
	}
	
	public EnumPaymentechRecordType getRecordType(){
		return this.recordType;
	}
	
	public String getCompanyId() {
		return this.companyId;
	}
	
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
	public Date getFromDate(){
		return this.fromDate;
	}
	
	public void setFromDate(Date fromDate){
		this.fromDate = fromDate;
	}
	
	public Date getToDate() {
		return this.toDate;
	}
	
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	
	public Date getGenerationDate(){
		return this.generationDate;
	}
	
	public void setGenerationDate(Date generationDate){
		this.generationDate = generationDate;
	}
	
	public TimeOfDay getGenerationTime() {
		return this.generationTime;
	}
	
	public void setGenerationTime(TimeOfDay generationTime){
		this.generationTime = generationTime;
	}
	
	@Override
    public String toString() {
		StringBuffer buf = new StringBuffer();
		
		buf.append("PaymentechHeader Type: ").append(this.recordType.getName()).append("\n");
		buf.append("Company Id: ").append(this.companyId).append("\n");
		buf.append("From Date: ").append(this.fromDate).append("\n");
		buf.append("To Date: ").append(this.toDate).append("\n");
		buf.append("Generation Date: ").append(this.generationDate).append("\n");
		buf.append("Generation Time: ").append(this.generationTime);
		
		return buf.toString();
	}

}
