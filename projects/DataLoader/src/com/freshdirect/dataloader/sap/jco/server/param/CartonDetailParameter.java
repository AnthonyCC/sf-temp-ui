package com.freshdirect.dataloader.sap.jco.server.param;

public class CartonDetailParameter implements java.io.Serializable 
{
	private static final long serialVersionUID = 8557251026840286402L;
	
	private Double netWeight;
	private String materialNumber;
	private String cartonType;
	private String salesUnitCode;
	private Double packedQuantity;
	private String orderNo;
	private String barcode;
	private String cartonNumber;
	private String sapOrderNo;
	private String orderLineNo;
		
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
		
		
	public void setPackedQuantity(final Double packedQuantity)
	{
		this.packedQuantity = packedQuantity;
	}
	
		
	public Double getPackedQuantity() 
	{
		return packedQuantity;
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
}
