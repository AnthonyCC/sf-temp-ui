package com.freshdirect.transadmin.model;

import java.math.BigDecimal;

public class TrnRouteNumber implements java.io.Serializable, TrnBaseEntityI {

	private TrnRouteNumberId id;
	
	private BigDecimal currentVal;
	
	public TrnRouteNumber() {
	}

	public TrnRouteNumber(TrnRouteNumberId id) {
		this.id = id;		
	}

	public TrnRouteNumber(TrnRouteNumberId id, BigDecimal currentVal) {
		this.id = id;		
		this.currentVal = currentVal;		
	}

	public TrnRouteNumberId getId() {
		return this.id;
	}

	public void setId(TrnRouteNumberId id) {
		this.id = id;
	}
		
	
	public BigDecimal getCurrentVal() {
		return currentVal;
	}

	public void setCurrentVal(BigDecimal currentVal) {
		this.currentVal = currentVal;
	}

	

	public boolean isObsoleteEntity() {
		return false;
	}
	
	public String toString() {
		return id.toString()+"-"+currentVal.intValue();
	}

}