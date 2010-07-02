package com.freshdirect.dataloader.payment.reconciliation.paymentech;

import java.io.Serializable;

public class DFRDataRecord implements Serializable {
	
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String entityType;
	private String entityNumber;
	private String fundTransferInsNum;
	private String secureBANum;
	private String currency;
	private String mop;
	private EnumPaymentechCategory category;
	private String settledConveyed;
	private int count;
	private double amount;
	
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public EnumPaymentechCategory getCategory() {
		return category;
	}
	public void setCategory(EnumPaymentechCategory category) {
		this.category = category;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getEntityNumber() {
		return entityNumber;
	}
	public void setEntityNumber(String entityNumber) {
		this.entityNumber = entityNumber;
	}
	public String getEntityType() {
		return entityType;
	}
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	public String getFundTransferInsNum() {
		return fundTransferInsNum;
	}
	public void setFundTransferInsNum(String fundTransferInsNum) {
		this.fundTransferInsNum = fundTransferInsNum;
	}
	public String getMop() {
		return mop;
	}
	public void setMop(String mop) {
		this.mop = mop;
	}
	public String getSecureBANum() {
		return secureBANum;
	}
	public void setSecureBANum(String secureBANum) {
		this.secureBANum = secureBANum;
	}
	public String getSettledConveyed() {
		return settledConveyed;
	}
	public void setSettledConveyed(String settledConveyed) {
		this.settledConveyed = settledConveyed;
	}
	
	@Override
    public String toString() {
		StringBuffer buf = new StringBuffer();
		
		buf.append("Entity Type: ").append(entityType).append("\n");
		buf.append("Entity Number: ").append(entityNumber).append("\n");
		buf.append("Funds Transfer Instruction Number: ").append(fundTransferInsNum).append("\n");
		buf.append("Secure BA Number: ").append(secureBANum).append("\n");
		buf.append("Currency: ").append(currency).append("\n");
		buf.append("MOP: ").append(mop).append("\n");
		buf.append("Category: ").append(category.getName()).append("\n");
		buf.append("Settled or Conveyed: ").append(settledConveyed).append("\n");
		buf.append("Count: ").append(count).append("\n");
		buf.append("Amoubt: ").append(amount).append("\n");
		
		return buf.toString();
	}
}
