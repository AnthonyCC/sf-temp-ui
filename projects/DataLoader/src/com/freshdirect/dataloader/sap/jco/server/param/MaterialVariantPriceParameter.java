package com.freshdirect.dataloader.sap.jco.server.param;

import java.io.Serializable;
import java.util.Date;

/**
 * @author kkanuganti
 */
public class MaterialVariantPriceParameter extends MaterialVariantParameter implements Serializable
{
	private static final long serialVersionUID = 6154979967858321833L;

	private String characteristicValueDescription;
	private Double price;
	private String pricingUnitCode;
	private String conditionType;
	private String conditionRecordNumber;
	private Date validStartDate;
	private Date validEndDate;
		
	/**
	 * @return the characteristicValueDescription
	 */
	public String getCharacteristicValueDescription()
	{
		return characteristicValueDescription;
	}
	/**
	 * @param characteristicValueDescription the characteristicValueDescription to set
	 */
	public void setCharacteristicValueDescription(String characteristicValueDescription)
	{
		this.characteristicValueDescription = characteristicValueDescription;
	}
	/**
	 * @return the price
	 */
	public Double getPrice()
	{
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(Double price)
	{
		this.price = price;
	}
	/**
	 * @return the validEndDate
	 */
	public Date getValidEndDate()
	{
		return validEndDate;
	}
	/**
	 * @param validEndDate the validEndDate to set
	 */
	public void setValidEndDate(Date validEndDate)
	{
		this.validEndDate = validEndDate;
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
	 * @return the conditionType
	 */
	public String getConditionType()
	{
		return conditionType;
	}
	/**
	 * @param conditionType the conditionType to set
	 */
	public void setConditionType(String conditionType)
	{
		this.conditionType = conditionType;
	}
	/**
	 * @return the validStartDate
	 */
	public Date getValidStartDate()
	{
		return validStartDate;
	}
	/**
	 * @param validStartDate the validStartDate to set
	 */
	public void setValidStartDate(Date validStartDate)
	{
		this.validStartDate = validStartDate;
	}
	/**
	 * @return the conditionRecordNumber
	 */
	public String getConditionRecordNumber()
	{
		return conditionRecordNumber;
	}
	/**
	 * @param conditionRecordNumber the conditionRecordNumber to set
	 */
	public void setConditionRecordNumber(String conditionRecordNumber)
	{
		this.conditionRecordNumber = conditionRecordNumber;
	}
}
