package com.freshdirect.transadmin.model;

import java.math.BigDecimal;

public class TrnRouteNumber implements java.io.Serializable, TrnBaseEntityI {

	private TrnRouteNumberId routeNumberId;
	
	private BigDecimal currentVal;
	
	public TrnRouteNumber() {
	}

	public TrnRouteNumber(TrnRouteNumberId id) {
		this.routeNumberId = id;		
	}

	public TrnRouteNumber(TrnRouteNumberId id, BigDecimal currentVal) {
		this.routeNumberId = id;		
		this.currentVal = currentVal;		
	}
		
	
	public TrnRouteNumberId getRouteNumberId() {
		return routeNumberId;
	}

	public void setRouteNumberId(TrnRouteNumberId routeNumberId) {
		this.routeNumberId = routeNumberId;
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
	

}