/*
 * Created on Sep 1, 2004
 */
package com.freshdirect.crm;

import com.freshdirect.customer.EnumSaleStatus;

/**
 * @author ekracoff
 */
public class CrmOrderStatusReportLine {
	private final String saleId;
	private final EnumSaleStatus status;
	private final String sapNumber;
	
	public CrmOrderStatusReportLine (String saleId, EnumSaleStatus status, String sapNumber){
		this.saleId = saleId;
		this.status = status;
		this.sapNumber = sapNumber;
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
}
