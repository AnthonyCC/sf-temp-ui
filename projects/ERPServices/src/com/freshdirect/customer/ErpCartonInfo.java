package com.freshdirect.customer;

import java.io.Serializable;
import java.util.*;

public class ErpCartonInfo implements Serializable {

	public static String CARTON_TYPE_REGULAR = "Regular";
	public static String CARTON_TYPE_BEER = "Beer";
	public static String CARTON_TYPE_FREEZER = "Freezer";
	public static String CARTON_TYPE_PLATTER = "Platter";
	
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
		return cartonType;
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
	/**
	 * @return Returns the sapNumber.
	 */
	public String getSapNumber() {
		return sapNumber;
	}
	/**
	 * @param sapNumber The sapNumber to set.
	 */
	public void setSapNumber(String sapNumber) {
		this.sapNumber = sapNumber;
	}
	
	public ErpCartonInfo(
		String orderNumber,
		String sapNumber,
		String cartonNumber,
		String cartonType
		) {
		this.orderNumber = orderNumber;
		this.sapNumber = sapNumber;
		this.cartonNumber = cartonNumber;
		this.cartonType = cartonType;
	}

	public void setDetails(List list) {
		if(list == null)
			list = new ArrayList();
		else
			details = list;
	}
	
	public List getDetails() {
		return details;
	}
	
	public String toString() {
		return "ErpCartonInfo[orderNumber: "
			+ this.orderNumber
			+ " sapNumber: "
			+ this.sapNumber
			+ " cartonNumber: "
			+ this.cartonNumber
			+ " cartonType: "
			+ this.cartonType
			+ " details: "
			+ details.toString();
	}

	private String orderNumber;
	private String sapNumber;
	private String cartonNumber;
	private String cartonType;
	private List details = new ArrayList();
}
