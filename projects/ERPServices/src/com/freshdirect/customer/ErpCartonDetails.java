package com.freshdirect.customer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
	 * @return Returns the actualQuantity.
	 */
	public double getActualQuantity() {
		return actualQuantity;
	}
	/**
	 * @param actualQuantity The actualQuantity to set.
	 */
	public void setActualQuantity(double actualQuantity) {
		this.actualQuantity = actualQuantity;
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

	public List<ErpCartonDetails> getComponents() {
		return components;
	}
	public void setComponents(List<ErpCartonDetails> components) {
		this.components = components;
	}
	
	public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}	
	public String getMaterialDesc() {
		return materialDesc;
	}
	public void setMaterialDesc(String materialDesc) {
		this.materialDesc = materialDesc;
	}
	public ErpCartonDetails(
		ErpCartonInfo cartonInfo,
		String orderLineNumber,
		String materialNumber,
		String barcode,
		double actualQuantity,
		double netWeight,
		String weightUnit) {
		this.cartonInfo = cartonInfo;
		this.orderLineNumber = orderLineNumber;
		this.materialNumber = materialNumber;
		this.barcode = barcode;
		this.actualQuantity = actualQuantity;
		this.netWeight = netWeight;
		this.weightUnit = weightUnit;
	}
	
	public ErpCartonDetails(
			ErpCartonInfo cartonInfo,
			String orderLineNumber,
			String materialNumber,
			String barcode,
			double actualQuantity,
			double netWeight,
			String weightUnit, String skuCode, String materialDesc, boolean shortShipped) {
			this.cartonInfo = cartonInfo;
			this.orderLineNumber = orderLineNumber;
			this.materialNumber = materialNumber;
			this.barcode = barcode;
			this.actualQuantity = actualQuantity;
			this.netWeight = netWeight;
			this.weightUnit = weightUnit;
			this.skuCode = skuCode;
			this.materialDesc = materialDesc;
			components = new ArrayList<ErpCartonDetails>();
			this.shortShipped = shortShipped;
	}

	public ErpCartonDetails(ErpCartonInfo cartonInfo, String orderLineNumber, String materialNumber,
			String barcode, double actualQuantity,
			double netWeight, String weightUnit, String skuCode,
			String materialDesc, 
			boolean shortShipped, String childOrderLineNo,
			Double ordered_quantity, String packed_uom, String shipping_status,
			String sub_material_number) {
		super();
		this.cartonInfo = cartonInfo;
		this.orderLineNumber = orderLineNumber;
		this.materialNumber = materialNumber;
		this.barcode = barcode;
		this.actualQuantity = actualQuantity;
		this.netWeight = netWeight;
		this.weightUnit = weightUnit;
		this.skuCode = skuCode;
		this.materialDesc = materialDesc;
		this.components = new ArrayList<ErpCartonDetails>();
		this.shortShipped = shortShipped;
		this.childOrderLineNo = childOrderLineNo;
		this.ordered_quantity = ordered_quantity;
		this.packed_uom = packed_uom;
		this.shipping_status = shipping_status;
		this.sub_material_number = sub_material_number;
	}
	
	public ErpCartonDetails() {
	}
	public String toString() {
		return "ErpCartonDetails[orderLineNumber: "
			+ this.orderLineNumber
			+ " Material#: "
			+ this.materialNumber
			+ " barcode: "
			+ this.barcode
			+ " actualQuantity: "
			+ this.actualQuantity
			+ " netWeight: "
			+ this.netWeight
			+ " weightUnit: "
			+ this.weightUnit;
	}

	private ErpCartonInfo cartonInfo;
	private String materialNumber;
	private String orderLineNumber;
	private String barcode;
	private double actualQuantity;
	private double netWeight;
	private String weightUnit;
	private String skuCode;
	private String materialDesc;
	private List<ErpCartonDetails> components = new ArrayList<ErpCartonDetails>();
	private boolean shortShipped;
	
	private String childOrderLineNo;
	
	private Double ordered_quantity;
	private String packed_uom;
	private String shipping_status;
	private String sub_material_number;
	private boolean first_carton_orln;
	
	public boolean isShortShipped() {
		return shortShipped;
	}
	public void setShortShipped(boolean shortShipped) {
		this.shortShipped = shortShipped;
	}
	
	public Double getOrdered_quantity() {
		return ordered_quantity;
	}
	public void setOrdered_quantity(Double ordered_quantity) {
		this.ordered_quantity = ordered_quantity;
	}
	public String getPacked_uom() {
		return packed_uom;
	}
	public void setPacked_uom(String packed_uom) {
		this.packed_uom = packed_uom;
	}
	public String getShipping_status() {
		return shipping_status;
	}
	public void setShipping_status(String shipping_status) {
		this.shipping_status = shipping_status;
	}
	public String getSub_material_number() {
		return sub_material_number;
	}
	public void setSub_material_number(String sub_material_number) {
		this.sub_material_number = sub_material_number;
	}
	public String getChildOrderLineNo() {
		return childOrderLineNo;
	}
	public void setChildOrderLineNo(String childOrderLineNo) {
		this.childOrderLineNo = childOrderLineNo;
	}
	public void setFirstCartonWithORLN(boolean first_carton_orln) {
		this.first_carton_orln = first_carton_orln;
	}
	public boolean isFirstCartonWithORLN() {
		return first_carton_orln;
	}

}
