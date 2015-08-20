/**
 * 
 */
package com.freshdirect.dataloader.sap.jco.server.param;

/**
 * @author kkanuganti
 */
public class MaterialUOMParameter extends AbstractMaterialParameter
{
	private static final long serialVersionUID = -5777928410091037663L;

	private String unitDimension;
	private String description;
	private Boolean displayIndicator;
	private Integer numerator;
	private Integer denominator;
	private String baseUnitCode;
	private String unitCode;
	/**
	 * @return the unitDimension
	 */
	public String getUnitDimension()
	{
		return unitDimension;
	}
	/**
	 * @param unitDimension the unitDimension to set
	 */
	public void setUnitDimension(String unitDimension)
	{
		this.unitDimension = unitDimension;
	}
	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
	/**
	 * @return the displayIndicator
	 */
	public Boolean getDisplayIndicator()
	{
		return displayIndicator;
	}
	/**
	 * @param displayIndicator the displayIndicator to set
	 */
	public void setDisplayIndicator(Boolean displayIndicator)
	{
		this.displayIndicator = displayIndicator;
	}
	/**
	 * @return the numerator
	 */
	public Integer getNumerator()
	{
		return numerator;
	}
	/**
	 * @param numerator the numerator to set
	 */
	public void setNumerator(Integer numerator)
	{
		this.numerator = numerator;
	}
	/**
	 * @return the denominator
	 */
	public Integer getDenominator()
	{
		return denominator;
	}
	/**
	 * @param denominator the denominator to set
	 */
	public void setDenominator(Integer denominator)
	{
		this.denominator = denominator;
	}
	/**
	 * @return the baseUnitCode
	 */
	public String getBaseUnitCode()
	{
		return baseUnitCode;
	}
	/**
	 * @param baseUnitCode the baseUnitCode to set
	 */
	public void setBaseUnitCode(String baseUnitCode)
	{
		this.baseUnitCode = baseUnitCode;
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
}
