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

	public ErpCustomerCreditModel() {
		super();
	}
	
	public ErpCustomerCreditModel(PrimaryKey complaintPk, String department, double amount, ErpAffiliate affiliate) {
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
}

