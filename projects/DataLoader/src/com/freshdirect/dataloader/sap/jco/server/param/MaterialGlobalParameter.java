/**
 * 
 */
package com.freshdirect.dataloader.sap.jco.server.param;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 */
public class MaterialGlobalParameter extends AbstractMaterialParameter implements Serializable
{
	private static final long serialVersionUID = 4628370420333210910L;
	
	private String materialTypeDescription;
	private Boolean configurableItem;
	private Boolean taxable;
	private String taxId;
	private String crossChainSaleStatus;
	private Date salesStatusDate;
	private String materialType;
	private Date unAvailabilityDate;
	private String materialGroup;
	private Boolean deleted;
	private String unitCode;
	private String upc;
	private String daysFresh;
	private boolean variantConfigExists;
	
	/**
	 * @return the materialTypeDescription
	 */
	public String getMaterialTypeDescription()
	{
		return materialTypeDescription;
	}
	/**
	 * @param materialTypeDescription the materialTypeDescription to set
	 */
	public void setMaterialTypeDescription(String materialTypeDescription)
	{
		this.materialTypeDescription = materialTypeDescription;
	}
	/**
	 * @return the configurableItem
	 */
	public Boolean getConfigurableItem()
	{
		return configurableItem;
	}
	/**
	 * @param configurableItem the configurableItem to set
	 */
	public void setConfigurableItem(Boolean configurableItem)
	{
		this.configurableItem = configurableItem;
	}
	/**
	 * @return the taxId
	 */
	public String getTaxId()
	{
		return taxId;
	}
	/**
	 * @param taxId the taxId to set
	 */
	public void setTaxId(String taxId)
	{
		this.taxId = taxId;
	}
	/**
	 * @return the crossChainSaleStatus
	 */
	public String getCrossChainSaleStatus()
	{
		return crossChainSaleStatus;
	}
	/**
	 * @param crossChainSaleStatus the crossChainSaleStatus to set
	 */
	public void setCrossChainSaleStatus(String crossChainSaleStatus)
	{
		this.crossChainSaleStatus = crossChainSaleStatus;
	}
	/**
	 * @return the salesStatusDate
	 */
	public Date getSalesStatusDate()
	{
		return salesStatusDate;
	}
	/**
	 * @param salesStatusDate the salesStatusDate to set
	 */
	public void setSalesStatusDate(Date salesStatusDate)
	{
		this.salesStatusDate = salesStatusDate;
	}
	/**
	 * @return the materialType
	 */
	public String getMaterialType()
	{
		return materialType;
	}
	/**
	 * @param materialType the materialType to set
	 */
	public void setMaterialType(String materialType)
	{
		this.materialType = materialType;
	}
	/**
	 * @return the unAvailabilityDate
	 */
	public Date getUnAvailabilityDate()
	{
		return unAvailabilityDate;
	}
	/**
	 * @param unAvailabilityDate the unAvailabilityDate to set
	 */
	public void setUnAvailabilityDate(Date unAvailabilityDate)
	{
		this.unAvailabilityDate = unAvailabilityDate;
	}
	/**
	 * @return the materialGroup
	 */
	public String getMaterialGroup()
	{
		return materialGroup;
	}
	/**
	 * @param materialGroup the materialGroup to set
	 */
	public void setMaterialGroup(String materialGroup)
	{
		this.materialGroup = materialGroup;
	}
	/**
	 * @return the deleted
	 */
	public Boolean getDeleted()
	{
		return deleted;
	}
	/**
	 * @param deleted the deleted to set
	 */
	public void setDeleted(Boolean deleted)
	{
		this.deleted = deleted;
	}
	/**
	 * @return the unitCode
	 */
	public String getUnitCode()
	{
		return unitCode;
	}
	/**
	 * @param unitCode the unitCode to set
	 */
	public void setUnitCode(String unitCode)
	{
		this.unitCode = unitCode;
	}
	/**
	 * @return the taxable
	 */
	public Boolean getTaxable()
	{
		return taxable;
	}
	/**
	 * @param taxable the taxable to set
	 */
	public void setTaxable(Boolean taxable)
	{
		this.taxable = taxable;
	}
	/**
	 * @return the daysFresh
	 */
	public String getDaysFresh()
	{
		return daysFresh;
	}
	/**
	 * @param daysFresh the daysFresh to set
	 */
	public void setDaysFresh(String daysFresh)
	{
		this.daysFresh = daysFresh;
	}
	/**
	 * @return the variantConfigExists
	 */
	public boolean isVariantConfigExists()
	{
		return variantConfigExists;
	}
	/**
	 * @param variantConfigExists the variantConfigExists to set
	 */
	public void setVariantConfigExists(boolean variantConfigExists)
	{
		this.variantConfigExists = variantConfigExists;
	}
	/**
	 * @return the upc
	 */
	public String getUpc()
	{
		return upc;
	}
	/**
	 * @param upc the upc to set
	 */
	public void setUpc(String upc)
	{
		this.upc = upc;
	}
	
	
	
}
