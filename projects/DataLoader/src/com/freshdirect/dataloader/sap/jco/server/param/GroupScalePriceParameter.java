package com.freshdirect.dataloader.sap.jco.server.param;

public class GroupScalePriceParameter extends AbstractMaterialParameter 
{
	private String groupId;
	private double price;
	private String pricingUnitCode;
	private long grpScaleQuantity;
	private String grpShortDesc;
	private String grpExpiryIndicator;
	private String scaleUnitCode;
	private String zoneId;
	private String grpLongDesc;
			
	public void setGroupId(final String groupId)
	{
		this.groupId = groupId;
	}
		
	public String getGroupId() 
	{
		return groupId;
	}
		
		
	public void setPrice(final double price)
	{
		this.price = price;
	}
	
		
	public double getPrice() 
	{
		return price;
	}
		
		
	public void setPricingUnitCode(final String pricingUnitCode)
	{
		this.pricingUnitCode = pricingUnitCode;
	}
	
		
	public String getPricingUnitCode() 
	{
		return pricingUnitCode;
	}
		
		
	public void setGrpScaleQuantity(final long grpScaleQuantity)
	{
		this.grpScaleQuantity = grpScaleQuantity;
	}
	
		
	public long getGrpScaleQuantity() 
	{
		return grpScaleQuantity;
	}
		
		
	public void setGrpShortDesc(final String grpShortDesc)
	{
		this.grpShortDesc = grpShortDesc;
	}
	
		
	public String getGrpShortDesc() 
	{
		return grpShortDesc;
	}
		
		
	public void setGrpExpiryIndicator(final String grpExpiryIndicator)
	{
		this.grpExpiryIndicator = grpExpiryIndicator;
	}
	
		
	public String getGrpExpiryIndicator() 
	{
		return grpExpiryIndicator;
	}
		
		
	public void setScaleUnitCode(final String scaleUnitCode)
	{
		this.scaleUnitCode = scaleUnitCode;
	}
	
		
	public String getScaleUnitCode() 
	{
		return scaleUnitCode;
	}
		
		
	public void setZoneId(final String zoneId)
	{
		this.zoneId = zoneId;
	}
	
		
	public String getZoneId() 
	{
		return zoneId;
	}
		
		
	public void setGrpLongDesc(final String grpLongDesc)
	{
		this.grpLongDesc = grpLongDesc;
	}
	
		
	public String getGrpLongDesc() 
	{
		return grpLongDesc;
	}
}
