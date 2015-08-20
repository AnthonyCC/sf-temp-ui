package com.freshdirect.dataloader.sap.jco.server.param;

/**
 * 
 */
public class GroupScalePriceParameter extends AbstractMaterialParameter 
{
	private static final long serialVersionUID = -2269445689066126945L;

	private String groupId;
	private double price;
	private String pricingUnitCode;
	private long grpScaleQuantity;
	private String grpShortDesc;
	private String grpExpiryIndicator;
	private String scaleUnitCode;
	private String zoneId;
	private String grpLongDesc;
	
	/**
	 * @return the groupId
	 */
	public String getGroupId()
	{
		return groupId;
	}
	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(String groupId)
	{
		this.groupId = groupId;
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
	 * @return the grpScaleQuantity
	 */
	public long getGrpScaleQuantity()
	{
		return grpScaleQuantity;
	}
	/**
	 * @param grpScaleQuantity the grpScaleQuantity to set
	 */
	public void setGrpScaleQuantity(long grpScaleQuantity)
	{
		this.grpScaleQuantity = grpScaleQuantity;
	}
	/**
	 * @return the grpShortDesc
	 */
	public String getGrpShortDesc()
	{
		return grpShortDesc;
	}
	/**
	 * @param grpShortDesc the grpShortDesc to set
	 */
	public void setGrpShortDesc(String grpShortDesc)
	{
		this.grpShortDesc = grpShortDesc;
	}
	/**
	 * @return the grpExpiryIndicator
	 */
	public String getGrpExpiryIndicator()
	{
		return grpExpiryIndicator;
	}
	/**
	 * @param grpExpiryIndicator the grpExpiryIndicator to set
	 */
	public void setGrpExpiryIndicator(String grpExpiryIndicator)
	{
		this.grpExpiryIndicator = grpExpiryIndicator;
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
	 * @return the grpLongDesc
	 */
	public String getGrpLongDesc()
	{
		return grpLongDesc;
	}
	/**
	 * @param grpLongDesc the grpLongDesc to set
	 */
	public void setGrpLongDesc(String grpLongDesc)
	{
		this.grpLongDesc = grpLongDesc;
	}
			

}
