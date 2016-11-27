/**
 * 
 */
package com.freshdirect.dataloader.sap.jco.server.param;

import java.io.Serializable;


/**
 * @author kkanuganti
 */
public class InvoiceEntryParameter implements Serializable
{
	private static final long serialVersionUID = -8004245710305496029L;

	private String salesOrderNo;
	private String materialNumber;
	private String status;
	private double shippedQuantity;
	private double unitPrice;
	private double couponDiscount;
	private String actualCost;
	private String invoiceNo;
	private String salesUOMCode;
	private int actualShippedQuantity;
	private double discount;
	private double amount;
	private double taxAmt;
	private double bottleDepositAmount;
	private double customizationPrice;
	private double orderQuantity;
	private String invoiceLineNo;
	private String weightUnitCode;
	private double grossWeight;
	
	/**
	 * @return the salesOrderNo
	 */
	public String getSalesOrderNo()
	{
		return salesOrderNo;
	}
	/**
	 * @param salesOrderNo the salesOrderNo to set
	 */
	public void setSalesOrderNo(String salesOrderNo)
	{
		this.salesOrderNo = salesOrderNo;
	}
	/**
	 * @return the materialNumber
	 */
	public String getMaterialNumber()
	{
		return materialNumber;
	}
	/**
	 * @param materialNumber the materialNumber to set
	 */
	public void setMaterialNumber(String materialNumber)
	{
		this.materialNumber = materialNumber;
	}
	/**
	 * @return the status
	 */
	public String getStatus()
	{
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status)
	{
		this.status = status;
	}
	/**
	 * @return the shippedQuantity
	 */
	public double getShippedQuantity()
	{
		return shippedQuantity;
	}
	/**
	 * @param shippedQuantity the shippedQuantity to set
	 */
	public void setShippedQuantity(double shippedQuantity)
	{
		this.shippedQuantity = shippedQuantity;
	}
	/**
	 * @return the unitPrice
	 */
	public double getUnitPrice()
	{
		return unitPrice;
	}
	/**
	 * @param unitPrice the unitPrice to set
	 */
	public void setUnitPrice(double unitPrice)
	{
		this.unitPrice = unitPrice;
	}
	/**
	 * @return the couponDiscount
	 */
	public double getCouponDiscount()
	{
		return couponDiscount;
	}
	/**
	 * @param couponDiscount the couponDiscount to set
	 */
	public void setCouponDiscount(double couponDiscount)
	{
		this.couponDiscount = couponDiscount;
	}
	/**
	 * @return the actualCost
	 */
	public String getActualCost()
	{
		return actualCost;
	}
	/**
	 * @param actualCost the actualCost to set
	 */
	public void setActualCost(String actualCost)
	{
		this.actualCost = actualCost;
	}
	/**
	 * @return the invoiceNo
	 */
	public String getInvoiceNo()
	{
		return invoiceNo;
	}
	/**
	 * @param invoiceNo the invoiceNo to set
	 */
	public void setInvoiceNo(String invoiceNo)
	{
		this.invoiceNo = invoiceNo;
	}
	/**
	 * @return the salesUOMCode
	 */
	public String getSalesUOMCode()
	{
		return salesUOMCode;
	}
	/**
	 * @param salesUOMCode the salesUOMCode to set
	 */
	public void setSalesUOMCode(String salesUOMCode)
	{
		this.salesUOMCode = salesUOMCode;
	}
	/**
	 * @return the actualShippedQuantity
	 */
	public int getActualShippedQuantity()
	{
		return actualShippedQuantity;
	}
	/**
	 * @param actualShippedQuantity the actualShippedQuantity to set
	 */
	public void setActualShippedQuantity(int actualShippedQuantity)
	{
		this.actualShippedQuantity = actualShippedQuantity;
	}
	/**
	 * @return the discount
	 */
	public double getDiscount()
	{
		return discount;
	}
	/**
	 * @param discount the discount to set
	 */
	public void setDiscount(double discount)
	{
		this.discount = discount;
	}
	/**
	 * @return the amount
	 */
	public double getAmount()
	{
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(double amount)
	{
		this.amount = amount;
	}
	/**
	 * @return the taxAmt
	 */
	public double getTaxAmt()
	{
		return taxAmt;
	}
	/**
	 * @param taxAmt the taxAmt to set
	 */
	public void setTaxAmt(double taxAmt)
	{
		this.taxAmt = taxAmt;
	}
	/**
	 * @return the bottleDepositAmount
	 */
	public double getBottleDepositAmount()
	{
		return bottleDepositAmount;
	}
	/**
	 * @param bottleDepositAmount the bottleDepositAmount to set
	 */
	public void setBottleDepositAmount(double bottleDepositAmount)
	{
		this.bottleDepositAmount = bottleDepositAmount;
	}
	/**
	 * @return the customizationPrice
	 */
	public double getCustomizationPrice()
	{
		return customizationPrice;
	}
	/**
	 * @param customizationPrice the customizationPrice to set
	 */
	public void setCustomizationPrice(double customizationPrice)
	{
		this.customizationPrice = customizationPrice;
	}
	/**
	 * @return the orderQuantity
	 */
	public double getOrderQuantity()
	{
		return orderQuantity;
	}
	/**
	 * @param orderQuantity the orderQuantity to set
	 */
	public void setOrderQuantity(double orderQuantity)
	{
		this.orderQuantity = orderQuantity;
	}
	/**
	 * @return the invoiceLineNo
	 */
	public String getInvoiceLineNo()
	{
		return invoiceLineNo;
	}
	/**
	 * @param invoiceLineNo the invoiceLineNo to set
	 */
	public void setInvoiceLineNo(String invoiceLineNo)
	{
		this.invoiceLineNo = invoiceLineNo;
	}
	/**
	 * @return the weightUnitCode
	 */
	public String getWeightUnitCode()
	{
		return weightUnitCode;
	}
	/**
	 * @param weightUnitCode the weightUnitCode to set
	 */
	public void setWeightUnitCode(String weightUnitCode)
	{
		this.weightUnitCode = weightUnitCode;
	}
	/**
	 * @return the grossWeight
	 */
	public double getGrossWeight()
	{
		return grossWeight;
	}
	/**
	 * @param grossWeight the grossWeight to set
	 */
	public void setGrossWeight(double grossWeight)
	{
		this.grossWeight = grossWeight;
	}
	
}
