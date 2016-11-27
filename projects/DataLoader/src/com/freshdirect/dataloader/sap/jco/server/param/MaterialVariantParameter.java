package com.freshdirect.dataloader.sap.jco.server.param;

import java.io.Serializable;

/**
 * @author kkanuganti
 */
public class MaterialVariantParameter extends AbstractMaterialParameter implements Serializable
{
	private static final long serialVersionUID = 6154979967858321833L;
	
	private String configProfileName;
	private String className;
	private String characteristicName;
	private String characteristicValueName;
	private String deafaultValue;
	private String unitOfDimension;
	
	/**
	 * @return the configProfileName
	 */
	public String getConfigProfileName()
	{
		return configProfileName;
	}
	/**
	 * @param configProfileName the configProfileName to set
	 */
	public void setConfigProfileName(String configProfileName)
	{
		this.configProfileName = configProfileName;
	}	
	/**
	 * @return the className
	 */
	public String getClassName()
	{
		return className;
	}
	/**
	 * @param className the className to set
	 */
	public void setClassName(String className)
	{
		this.className = className;
	}	
	/**
	 * @return the characteristicName
	 */
	public String getCharacteristicName()
	{
		return characteristicName;
	}
	/**
	 * @param characteristicName the characteristicName to set
	 */
	public void setCharacteristicName(String characteristicName)
	{
		this.characteristicName = characteristicName;
	}
	/**
	 * @return the characteristicValueName
	 */
	public String getCharacteristicValueName()
	{
		return characteristicValueName;
	}
	/**
	 * @param characteristicValueName the characteristicValueName to set
	 */
	public void setCharacteristicValueName(String characteristicValueName)
	{
		this.characteristicValueName = characteristicValueName;
	}
	/**
	 * @return the deafaultValue
	 */
	public String getDeafaultValue()
	{
		return deafaultValue;
	}
	/**
	 * @param deafaultValue the deafaultValue to set
	 */
	public void setDeafaultValue(String deafaultValue)
	{
		this.deafaultValue = deafaultValue;
	}
	public String getUnitOfDimension() {
		return unitOfDimension;
	}
	public void setUnitOfDimension(String unitOfDimension) {
		this.unitOfDimension = unitOfDimension;
	}
}
