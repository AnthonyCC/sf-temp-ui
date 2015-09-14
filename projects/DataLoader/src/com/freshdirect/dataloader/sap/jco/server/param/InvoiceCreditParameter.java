/**
 * 
 */
package com.freshdirect.dataloader.sap.jco.server.param;

import java.io.Serializable;

/**
 * @author ksriram
 *
 */
public class InvoiceCreditParameter implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private double creditAmount; //CREDIT_AMOUNT 
	private String creditMemoNo; //CREDIT_MEMO_NO 
	private String webOrderNo; //WEB_ORDER
	private String salesOrderNo;//ORDER_NO - SAP Sales Order number
	private String creditWebReferenceNo;//ZZBMREF - Blue Martini Ref no.
	private String typeIndicator; //TYPE - Credit type Indicator
	private String invoiceNo;//INV_NUM - Billing document
	
	public double getCreditAmount() {
		return creditAmount;
	}
	public String getCreditMemoNo() {
		return creditMemoNo;
	}
	public String getWebOrderNo() {
		return webOrderNo;
	}
	public String getSalesOrderNo() {
		return salesOrderNo;
	}
	public String getCreditWebReferenceNo() {
		return creditWebReferenceNo;
	}
	public String getTypeIndicator() {
		return typeIndicator;
	}
	public void setCreditAmount(double creditAmount) {
		this.creditAmount = creditAmount;
	}
	public void setCreditMemoNo(String creditMemoNo) {
		this.creditMemoNo = creditMemoNo;
	}
	public void setWebOrderNo(String webOrderNo) {
		this.webOrderNo = webOrderNo;
	}
	public void setSalesOrderNo(String salesOrderNo) {
		this.salesOrderNo = salesOrderNo;
	}
	public void setCreditWebReferenceNo(String creditWebReferenceNo) {
		this.creditWebReferenceNo = creditWebReferenceNo;
	}
	public void setTypeIndicator(String typeIndicator) {
		this.typeIndicator = typeIndicator;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
		
}
