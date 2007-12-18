package com.freshdirect.erp.model;

import java.io.Serializable;
import java.util.Date;

public class ATPFailureInfo implements Serializable {

	private final Date requestedDate;
	private final String materialNumber;
	private final double requestedQuantity;
	private final String salesUnit;
	private final double availableQuantity;
	private final String erpCustomerId;

	public ATPFailureInfo(
		Date requestedDate,
		String materialNumber,
		double requestedQuantity,
		String salesUnit,
		double availableQuantity, 
		String erpCustomerId) {

		this.requestedDate = requestedDate;
		this.materialNumber = materialNumber;
		this.requestedQuantity = requestedQuantity;
		this.salesUnit = salesUnit;
		this.availableQuantity = availableQuantity;
		this.erpCustomerId = erpCustomerId;
	}

	public Date getRequestedDate() {
		return requestedDate;
	}

	public String getMaterialNumber() {
		return materialNumber;
	}

	public double getRequestedQuantity() {
		return requestedQuantity;
	}

	public String getSalesUnit() {
		return salesUnit;
	}

	public double getAvailableQuantity() {
		return availableQuantity;
	}
	
	public String getErpCustomerId(){
		return erpCustomerId;
	}

	public String toString() {
		return "[ATPFailureInfo: RequestedDate: "
			+ this.requestedDate
			+ " materialNumber: "
			+ this.materialNumber
			+ " requestedQuantity: "
			+ this.requestedQuantity
			+ " salesUnit: "
			+ this.salesUnit
			+ " availableQuantity: "
			+ this.availableQuantity
			+ " erpCustomer ID is "
			+ this.erpCustomerId;
	}

}
