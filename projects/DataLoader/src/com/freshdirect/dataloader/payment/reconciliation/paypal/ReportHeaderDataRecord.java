package com.freshdirect.dataloader.payment.reconciliation.paypal;

import java.io.Serializable;
import java.util.Date;

public class ReportHeaderDataRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6934502720550050504L;

	private Date reportGenerationDate = null;
	private char reportingWindow = ' ';
	private String accountId = "";
	private String reportVersion = "";
	public Date getReportGenerationDate() {
		return reportGenerationDate;
	}
	public void setReportGenerationDate(Date reportGenerationDate) {
		this.reportGenerationDate = reportGenerationDate;
	}
	public char getReportingWindow() {
		return reportingWindow;
	}
	public void setReportingWindow(char reportingWindow) {
		this.reportingWindow = reportingWindow;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getReportVersion() {
		return reportVersion;
	}
	public void setReportVersion(String reportVersion) {
		this.reportVersion = reportVersion;
	}
	
	
}
