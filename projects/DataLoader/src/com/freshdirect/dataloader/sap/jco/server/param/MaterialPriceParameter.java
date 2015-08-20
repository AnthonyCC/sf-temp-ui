/**
 * 
 */
package com.freshdirect.dataloader.sap.jco.server.param;

import java.util.Date;

/**
 * @author kkanuganti
 */
public class MaterialPriceParameter extends AbstractMaterialParameter
{
	private static final long serialVersionUID = -2094484084401207694L;
	
	private String currencyCode;
	private String salesUnitCode;
	private double price;
	private String pricingUnitCode;
	private String customerNumber;
	private Date validToDate;
	private String priceType;
	private long scaleQuanity;
	private String scaleUnitCode;
	private Date validFromDate;
	private String zoneId;
	private String sapId;
	
	/**
	 * @return the currencyCode
	 */
	public String getCurrencyCode()
	{
		return currencyCode;
	}
	/**
	 * @param currencyCode the currencyCode to set
	 */
	public void setCurrencyCode(String currencyCode)
	{
		this.currencyCode = currencyCode;
	}
	/**
	 * @return the salesUnitCode
	 */
	public String getSalesUnitCode()
	{
		return salesUnitCode;
	}
	/**
	 * @param salesUnitCode the salesUnitCode to set
	 */
	public void setSalesUnitCode(String salesUnitCode)
	{
		this.salesUnitCode = salesUnitCode;
	}
	/**
	 * @return the price
	 */
	public double getPrice()
	{
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(double price)
	{
		this.price = price;
	}
	/**
	 * @return the pricingUnitCode
	 */
	public String getPricingUnitCode()
	{
		return pricingUnitCode;
	}
	/**
	 * @param pricingUnitCode the pricingUnitCode to set
	 */
	public void setPricingUnitCode(String pricingUnitCode)
	{
		this.pricingUnitCode = pricingUnitCode;
	}
	/**
	 * @return the customerNumber
	 */
	public String getCustomerNumber()
	{
		return customerNumber;
	}
	/**
	 * @param customerNumber the customerNumber to set
	 */
	public void setCustomerNumber(String customerNumber)
	{
		this.customerNumber = customerNumber;
	}
	/**
	 * @return the validToDate
	 */
	public Date getValidToDate()
	{
		return validToDate;
	}
	/**
	 * @param validToDate the validToDate to set
	 */
	public void setValidToDate(Date validToDate)
	{
		this.validToDate = validToDate;
	}
	/**
	 * @return the priceType
	 */
	public String getPriceType()
	{
		return priceType;
	}
	/**
	 * @param priceType the priceType to set
	 */
	public void setPriceType(String priceType)
	{
		this.priceType = priceType;
	}
	/**
	 * @return the scaleQuanity
	 */
	public long getScaleQuanity()
	{
		return scaleQuanity;
	}
	/**
	 * @param scaleQuanity the scaleQuanity to set
	 */
	public void setScaleQuanity(long scaleQuanity)
	{
		this.scaleQuanity = scaleQuanity;
	}
	/**
	 * @return the scaleUnitCode
	 */
	public String getScaleUnitCode()
	{
		return scaleUnitCode;
	}
	/**
	 * @param scaleUnitCode the scaleUnitCode to set
	 */
	public void setScaleUnitCode(String scaleUnitCode)
	{
		this.scaleUnitCode = scaleUnitCode;
	}
	/**
	 * @return the validFromDate
	 */
	public Date getValidFromDate()
	{
		return validFromDate;
	}
	/**
	 * @param validFromDate the validFromDate to set
	 */
	public void setValidFromDate(Date validFromDate)
	{
		this.validFromDate = validFromDate;
	}
	/**
	 * @return the zoneId
	 */
	public String getZoneId()
	{
		return zoneId;
	}
	/**
	 * @param zoneId the zoneId to set
	 */
	public void setZoneId(String zoneId)
	{
		this.zoneId = zoneId;
	}
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}
	/**
	 * @return the sapId
	 */
	public String getSapId() {
		return sapId;
	}
	/**
	 * @param sapId the sapId to set
	 */
	public void setSapId(String sapId) {
		this.sapId = sapId;
	}
}
