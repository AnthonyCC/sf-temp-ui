package com.freshdirect.dataloader.sap.jco.server.param;

public class CartonDetailParameter implements java.io.Serializable 
{
	private static final long serialVersionUID = 8557251026840286402L;
	
	private Double netWeight;
	private String materialNumber;
	private String cartonType;
	private String salesUnitCode;
	private Double actual_quantity;
	private String orderNo;
	private String barcode;
	private String cartonNumber;
	private String sapOrderNo;
	private String orderLineNo;
	private String childOrderLineNo;
		
	private String material_description;
	private Double ordered_quantity;
	private String packed_uom;
	private String shipping_status;
	private String sub_material_number;
	private String skuCode;
	
	public CartonDetailParameter()
	{
		// default constructor
	}
	
		
	public void setNetWeight(final Double netWeight)
	{
		this.netWeight = netWeight;
	}
	
		
	public Double getNetWeight() 
	{
		return netWeight;
	}
		
		
	public void setMaterialNumber(final String materialNumber)
	{
		this.materialNumber = materialNumber;
	}
	
		
	public String getMaterialNumber() 
	{
		return materialNumber;
	}
		
		
	public void setCartonType(final String cartonType)
	{
		this.cartonType = cartonType;
	}
	
		
	public String getCartonType() 
	{
		return cartonType;
	}
		
		
	public void setSalesUnitCode(final String salesUnitCode)
	{
		this.salesUnitCode = salesUnitCode;
	}
	
		
	public String getSalesUnitCode() 
	{
		return salesUnitCode;
	}
		
	public void setOrderNo(final String orderNo)
	{
		this.orderNo = orderNo;
	}
	
		
	public String getOrderNo() 
	{
		return orderNo;
	}
		
		
	public void setBarcode(final String barcode)
	{
		this.barcode = barcode;
	}
	
		
	public String getBarcode() 
	{
		return barcode;
	}
		
		
	public void setCartonNumber(final String cartonNumber)
	{
		this.cartonNumber = cartonNumber;
	}
	
		
	public String getCartonNumber() 
	{
		return cartonNumber;
	}
		
		
	public void setSapOrderNo(final String sapOrderNo)
	{
		this.sapOrderNo = sapOrderNo;
	}
	
		
	public String getSapOrderNo() 
	{
		return sapOrderNo;
	}


	/**
	 * @return the orderLineNo
	 */
	public String getOrderLineNo() {
		return orderLineNo;
	}


	/**
	 * @param orderLineNo the orderLineNo to set
	 */
	public void setOrderLineNo(String orderLineNo) {
		this.orderLineNo = orderLineNo;
	}

	public String getMaterial_description() {
		return material_description;
	}


	public void setMaterial_description(String material_description) {
		this.material_description = material_description;
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


	public String getSkuCode() {
		return skuCode;
	}


	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}


	public String getChildOrderLineNo() {
		return childOrderLineNo;
	}


	public void setChildOrderLineNo(String childOrderLineNo) {
		this.childOrderLineNo = childOrderLineNo;
	}


	public Double getActual_quantity() {
		return actual_quantity;
	}


	public void setActual_quantity(Double actual_quantity) {
		this.actual_quantity = actual_quantity;
	}	
}
