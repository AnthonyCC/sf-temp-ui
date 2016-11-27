/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.common.pricing;

import com.freshdirect.affiliate.ErpAffiliate;

/**
 * Class representing a credit memo.
 *
 * @version $Revision$
 * @author $Author$
 */
public class CreditMemo implements java.io.Serializable {

    private ErpAffiliate affiliate;
    private String webNumber;
    private String reasonCode;
    private String reasonDesc;
    private String departmentName;
    private double amount;
    private int priority;

    public CreditMemo(String webNumber, String reasonCode, String reasonDesc, String departmentName, double amount, int priority, ErpAffiliate affiliate) {
        this.affiliate = affiliate;
        this.setWebNumber(webNumber);
        this.setReasonCode(reasonCode);
        this.setReasonDesc(reasonDesc);
        this.setDepartmentName(departmentName);
        this.setAmount(amount);
        this.setPriority(priority);
    }
    
    public ErpAffiliate getAffiliate(){
    	return this.affiliate;
    }
    
    public void setAffiliate(ErpAffiliate affiliate){
    	this.affiliate = affiliate;
    }

	/**
	 * Returns the webNumber for the CreditMemo.
	 *
	 * @return int webNumber
	 */	
	public String getWebNumber() {
		return this.webNumber;
	}

	/**
	 * Sets webNumber for a CreditMemo.
	 *
	 * @param webNumber webNumber to set
	 */	
	public void setWebNumber(String webNumber) {
		this.webNumber = webNumber;
	}
	
	/**
	 * Returns reason code for this object.
	 *
	 * @return String reasonCode
	 */	
	public String getReasonCode() {
		return this.reasonCode;
	}

	/**
	 * Sets reason code for this CreditMemo.
	 *
	 * @param reasonCode String reasonCode to set
	 */	
	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}
	
	/**
	 * Returns reason description for this CreditMemo.
	 *
	 * @return String reasonDesc
	 */	
	public String getReasonDesc() {
		return this.reasonDesc;
	}

	/**
	 * Sets reason Description for this CreditMemo.
	 *
	 * @param reasonDesc String to set
	 */	
	public void setReasonDesc(String reasonDesc) {
		this.reasonDesc = reasonDesc;
	}

	/**
	 * Returns department name for this CreditMemo.
	 *
	 * @return String departmentName
	 */	
	public String getDepartmentName() {
		return this.departmentName;
	}
	
	/**
	 * Sets department name for this CreditMemo.
	 *
	 * @param departmentName departmentName to set
	 */	
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	/**
	 * Returns amount for this CreditMemo.
	 *
	 * @return double amount
	 */	
	public double getAmount() {
		return this.amount;
	}

	/**
	 * Sets amount for this CreditMemo.
	 *
	 * @param amount to set
	 */	
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	/**
	 * Returns priority for this CreditMemo.
	 *
	 * @return int priority
	 */	
	public int getPriority() {
		return this.priority;
	}

	/**
	 * Sets priority for this CreditMemo.
	 * 
	 * @param priority int to set
	 */	
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
}
