/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.customer;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.framework.core.PrimaryKey;

/**
 * Credit interface
 *
 * @version    $Revision$
 * @author     $Author$
 * @stereotype fd-model
 */
public class ErpCustomerCreditModel extends ErpCreditModel {

	private PrimaryKey complaintPk;
	private double remainingAmount = 0;
	private Date createDate;
	private String cDate;
	private String saleId;
	private String eStore;
	private String customerId;

	public ErpCustomerCreditModel() {
		super();
	}

	public ErpCustomerCreditModel(@JsonProperty("complaintPk") PrimaryKey complaintPk,
			@JsonProperty("department") String department, @JsonProperty("amount") double amount,
			@JsonProperty("affiliate") ErpAffiliate affiliate) {
		super(department, amount, affiliate);
		this.complaintPk = complaintPk;
		this.remainingAmount = amount;
	}


    /** Get the PK of the Complaint this credit was issued for */
	public PrimaryKey getComplaintPk() {
		return this.complaintPk;
	}

    /** Set the PK of the Complaint this credit was issued for */
	public void setComplaintPk(PrimaryKey complaintPk) {
		this.complaintPk = complaintPk;
	}

	/** Get the amount remaining for this credit */
	public double getRemainingAmount() {
		return this.remainingAmount ;
	}

	/** Set the amount remaining for this credit */
	public void setRemainingAmount(double d) {
		this.remainingAmount = d;
	}
	
	public Date getCreateDate(){
		return this.createDate;
	}
	
	public void setCreateDate(Date createDate){
		this.createDate = createDate;
	}

	public void setcDate(String cDate) {
		this.cDate = cDate;
	}

	public String getcDate() {
		return cDate;
	}

	public void setSaleId(String saleId) {
		this.saleId = saleId;
	}

	public String getSaleId() {
		return saleId;
	}

	public String geteStore() {
		return eStore;
	}

	public void seteStore(String eStore) {
		this.eStore = eStore;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
}

