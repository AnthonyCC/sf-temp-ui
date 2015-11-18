/*
 * Created on Sep 1, 2004
 */
package com.freshdirect.crm;

import java.io.Serializable;

import com.freshdirect.customer.EnumSaleStatus;

/**
 * @author ekracoff
 */
public class CrmOrderStatusReportLine implements Serializable {
	private final String saleId;
	private final EnumSaleStatus status;
	private final String sapNumber;
	private final String eStore;
	private final String facility;
	
	public CrmOrderStatusReportLine (String saleId, EnumSaleStatus status, String sapNumber, String eStore, String facility){
		this.saleId = saleId;
		this.status = status;
		this.sapNumber = sapNumber;
		this.eStore = eStore;
		this.facility = facility;
	}

	public String getSaleId() {
		return saleId;
	}

	public String getSapNumber() {
		return sapNumber;
	}

	public EnumSaleStatus getStatus() {
		return status;
	}
	
	public String geteStore() {
		return eStore;
	}
	
	public String getFacility() {
		return facility;
	}
}
