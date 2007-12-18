package com.freshdirect.customer;

import java.io.Serializable;

public class ErpCartonDetails implements Serializable {

	/**
	 * @return Returns the cartonInfo.
	 */
	public ErpCartonInfo getCartonInfo() {
		return cartonInfo;
	}
	/**
	 * @param cartonInfo The cartonInfo to set.
	 */
	public void setCartonInfo(ErpCartonInfo cartonInfo) {
		this.cartonInfo = cartonInfo;
	}
	/**
	 * @return Returns the netWeight.
	 */
	public double getNetWeight() {
		return netWeight;
	}
	/**
	 * @param netWeight The netWeight to set.
	 */
	public void setNetWeight(double netWeight) {
		this.netWeight = netWeight;
	}
	/**
	 * @return Returns the orderLineNumber.
	 */
	public String getOrderLineNumber() {
		return orderLineNumber;
	}
	/**
	 * @param orderLineNumber The orderLineNumber to set.
	 */
	public void setOrderLineNumber(String orderLineNumber) {
		this.orderLineNumber = orderLineNumber;
	}

	/**
	 * @return Returns the materialNumber.
	 */
	public String getMaterialNumber() {
		return materialNumber;
	}
	/**
	 * @param materialNumber The materialNumber to set.
	 */
	public void setMaterialNumber(String materialNumber) {
		this.materialNumber = materialNumber;
	}

	/**
	 * @return Returns the packedQuantity.
	 */
	public double getPackedQuantity() {
		return packedQuantity;
	}
	/**
	 * @param packedQuantity The packedQuantity to set.
	 */
	public void setPackedQuantity(double packedQuantity) {
		this.packedQuantity = packedQuantity;
	}
	/**
	 * @return Returns the weightUnit.
	 */
	public String getWeightUnit() {
		return weightUnit;
	}
	/**
	 * @param weightUnit The weightUnit to set.
	 */
	public void setWeightUnit(String weightUnit) {
		this.weightUnit = weightUnit;
	}
	
	/**
	 * @return Returns the barcode.
	 */
	public String getBarcode() {
		return barcode;
	}
	/**
	 * @param barcode The barcode to set.
	 */
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public ErpCartonDetails(
		ErpCartonInfo cartonInfo,
		String orderLineNumber,
		String materialNumber,
		String barcode,
		double packedQuantity,
		double netWeight,
		String weightUnit) {
		this.cartonInfo = cartonInfo;
		this.orderLineNumber = orderLineNumber;
		this.materialNumber = materialNumber;
		this.barcode = barcode;
		this.packedQuantity = packedQuantity;
		this.netWeight = netWeight;
		this.weightUnit = weightUnit;
	}

	public String toString() {
		return "ErpCartonDetails[orderLineNumber: "
			+ this.orderLineNumber
			+ " Material#: "
			+ this.materialNumber
			+ " barcode: "
			+ this.barcode
			+ " packedQuantity: "
			+ this.packedQuantity
			+ " netWeight: "
			+ this.netWeight
			+ " weightUnit: "
			+ this.weightUnit;
	}

	private ErpCartonInfo cartonInfo;
	private String materialNumber;
	private String orderLineNumber;
	private String barcode;
	private double packedQuantity;
	private double netWeight;
	private String weightUnit;

}
