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
	private final String plantId;

	public ATPFailureInfo(
		Date requestedDate,
		String materialNumber,
		double requestedQuantity,
		String salesUnit,
		double availableQuantity, 
		String erpCustomerId,
		String plantId) {

		this.requestedDate = requestedDate;
		this.materialNumber = materialNumber;
		this.requestedQuantity = requestedQuantity;
		this.salesUnit = salesUnit;
		this.availableQuantity = availableQuantity;
		this.erpCustomerId = erpCustomerId;
		this.plantId = plantId;
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

	
	public String getPlantId() {
		return plantId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ATPFailureInfo [requestedDate=" + requestedDate
				+ ", materialNumber=" + materialNumber + ", requestedQuantity="
				+ requestedQuantity + ", salesUnit=" + salesUnit
				+ ", availableQuantity=" + availableQuantity
				+ ", erpCustomerId=" + erpCustomerId + ", plantId=" + plantId
				+ "]";
	}

}
