package com.freshdirect.delivery.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.freshdirect.delivery.constants.EnumCartonType;

public class AirclicCartonInfo implements Serializable {
	
	private String orderNumber;
	private String cartonNumber;
	private String cartonType;
	private List<AirclicCartonScanDetails> details;

	/**
	 * @return Returns the cartonNumber.
	 */
	public String getCartonNumber() {
		return cartonNumber;
	}
	/**
	 * @param cartonNumber The cartonNumber to set.
	 */
	public void setCartonNumber(String cartonNumber) {
		this.cartonNumber = cartonNumber;
	}
	/**
	 * @return Returns the cartonType.
	 */
	public String getCartonType() {
		return EnumCartonType.getEnum(this.cartonType);
	}
	/**
	 * @param cartonType The cartonType to set.
	 */
	public void setCartonType(String cartonType) {
		this.cartonType = cartonType;
	}
	/**
	 * @return Returns the orderNumber.
	 */
	public String getOrderNumber() {
		return orderNumber;
	}
	/**
	 * @param orderNumber The orderNumber to set.
	 */
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	
	
	public AirclicCartonInfo(
		String orderNumber,
		String cartonNumber,
		String cartonType
		) {
		this.orderNumber = orderNumber;
		this.cartonNumber = cartonNumber;
		this.cartonType = cartonType;
	}

	public void setDetails(List<AirclicCartonScanDetails> list) {
		if(list == null)
			list = new ArrayList<AirclicCartonScanDetails>();
		else
			details = list;
	}
	
	public List<AirclicCartonScanDetails> getDetails() {
		return details;
	}	
	
	public String toString() {
		return "AirclicCartonInfo[orderNumber: "
			+ orderNumber			
			+ " cartonNumber: "
			+ cartonNumber
			+ " cartonType: "
			+ cartonType;
	}
}
