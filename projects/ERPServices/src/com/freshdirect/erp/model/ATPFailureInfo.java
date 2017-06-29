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
	private final String actionType;

	public ATPFailureInfo(
		Date requestedDate,
		String materialNumber,
		double requestedQuantity,
		String salesUnit,
		double availableQuantity, 
		String erpCustomerId,
		String plantId,
		String actionType) {

		this.requestedDate = requestedDate;
		this.materialNumber = materialNumber;
		this.requestedQuantity = requestedQuantity;
		this.salesUnit = salesUnit;
		this.availableQuantity = availableQuantity;
		this.erpCustomerId = erpCustomerId;
		this.plantId = plantId;
		this.actionType = actionType;
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
	
	public String getActionType() {
		return actionType;
	}
	@Override
	public String toString() {
		return "ATPFailureInfo [requestedDate=" + requestedDate + ", materialNumber=" + materialNumber
				+ ", requestedQuantity=" + requestedQuantity + ", salesUnit=" + salesUnit + ", availableQuantity="
				+ availableQuantity + ", erpCustomerId=" + erpCustomerId + ", plantId=" + plantId + ", actionType="
				+ actionType + "]";
	}

	

}
