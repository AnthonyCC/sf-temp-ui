/*
 * $Workfile:ErpTransactionModel.java$
 *
 * $Date:4/2/03 8:31:35 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.customer;

import java.util.*;

import com.freshdirect.framework.core.ModelSupport;

/**
 * Transaction interface
 *
 * @version    $Revision:4$
 * @author     $Author:Viktor Szathmary$
 * @stereotype fd-model
 */
public abstract class ErpTransactionModel extends ModelSupport implements ErpTransactionI {

    private final EnumTransactionType transactionType;
	private Date transactionDate;
    private EnumTransactionSource transactionSource;
    private String transactionInitiator;
    
	public ErpTransactionModel(EnumTransactionType transType) {
		this.transactionType = transType;
		this.transactionDate = new Date();
    }


    public EnumTransactionType getTransactionType(){
		return this.transactionType;
	}

	public Date getTransactionDate(){
		return this.transactionDate;
	}

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public EnumTransactionSource getTransactionSource() {
		return this.transactionSource;
	}

	public void setTransactionSource(EnumTransactionSource transactionSource){
        this.transactionSource = transactionSource;
	}
	
	public String getTransactionInitiator() {
		return this.transactionInitiator;
	}
	
	public void setTransactionInitiator(String ti) {
		this.transactionInitiator = ti;
	}
    public abstract double getAmount();

}

