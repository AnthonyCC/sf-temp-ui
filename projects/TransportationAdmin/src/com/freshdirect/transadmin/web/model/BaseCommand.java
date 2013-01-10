package com.freshdirect.transadmin.web.model;

import java.util.Date;

public class BaseCommand implements java.io.Serializable {
	
	private String userId;
	private Date transactionDate;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}	
	
}
