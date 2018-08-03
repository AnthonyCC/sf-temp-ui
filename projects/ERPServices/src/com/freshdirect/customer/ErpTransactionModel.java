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

	private static final long	serialVersionUID	= -5023224974259751148L;
	
	private final EnumTransactionType transactionType;
	private Date transactionDate;
    private EnumTransactionSource transactionSource;
    private String transactionInitiator;
    //Added as part of PERF-27 task.
    private String customerId;
    
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

    //Added as part of PERF-27 task.	
    public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	@Override
	public void setId(String id) {
		if (id != null)
			super.setId(id);
	}

}

