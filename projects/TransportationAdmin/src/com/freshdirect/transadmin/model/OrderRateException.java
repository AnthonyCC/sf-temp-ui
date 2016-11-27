package com.freshdirect.transadmin.model;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class OrderRateException implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6946155982018830336L;
	private Date exceptionDate;

	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getExceptionDate() {
		return exceptionDate;
	}
	@JsonDeserialize(using=JsonDateDeSerializer.class, as = Date.class)
	public void setExceptionDate(Date exceptionDate) {
		this.exceptionDate = exceptionDate;
	}

	
}
