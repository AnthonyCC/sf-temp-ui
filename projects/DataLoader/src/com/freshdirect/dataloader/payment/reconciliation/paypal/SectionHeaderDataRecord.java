package com.freshdirect.dataloader.payment.reconciliation.paypal;

import java.io.Serializable;
import java.util.Date;

public class SectionHeaderDataRecord implements Serializable {

	/**
	 * 
	 */
	private long serialVersionUID = -5487591844015835083L;

	private Date reportPeriodStartDate = null;
	private Date reportPeriodEndDate = null;
	private String accountId = null;
	private String partnerAccountId = null;
	
	public Date getReportPeriodStartDate() {
		return reportPeriodStartDate;
	}
	public void setReportPeriodStartDate(Date reportPeriodStartDate) {
		this.reportPeriodStartDate = reportPeriodStartDate;
	}
	public Date getReportPeriodEndDate() {
		return reportPeriodEndDate;
	}
	public void setReportPeriodEndDate(Date reportPeriodEndDate) {
		this.reportPeriodEndDate = reportPeriodEndDate;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getPartnerAccountId() {
		return partnerAccountId;
	}
	public void setPartnerAccountId(String partnerAccountId) {
		this.partnerAccountId = partnerAccountId;
	}
	
	
}
