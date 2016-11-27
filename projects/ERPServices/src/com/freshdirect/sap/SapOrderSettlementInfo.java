package com.freshdirect.sap;

import java.io.Serializable;
import java.util.Date;

public class SapOrderSettlementInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3647117568838616718L;
	
	private Date deliveryDate;
	private String acctNumber;
	private String sapSalesOrder;
	private String webSalesOrder;
	private Double amount;
	private String currency;
	private String companyCode;
	
	public Date getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public String getAcctNumber() {
		return acctNumber;
	}
	public void setAcctNumber(String acctNumber) {
		this.acctNumber = acctNumber;
	}
	public String getSapSalesOrder() {
		return sapSalesOrder;
	}
	public void setSapSalesOrder(String sapSalesOrder) {
		this.sapSalesOrder = sapSalesOrder;
	}
	public String getWebSalesOrder() {
		return webSalesOrder;
	}
	public void setWebSalesOrder(String webSalesOrder) {
		this.webSalesOrder = webSalesOrder;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "SapOrderSettlementInfo["+webSalesOrder+"]";
	}
}
