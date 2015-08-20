/**
 * 
 */
package com.freshdirect.dataloader.sap.jco.server.param;

/**
 * @author kkanuganti
 */
public class MaterialPromoPriceParameter extends AbstractMaterialParameter
{
	private static final long serialVersionUID = 6859632711834889408L;
	
	private double price;
	private String pricingUnitCode;
	private String zoneId;
	
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
}
