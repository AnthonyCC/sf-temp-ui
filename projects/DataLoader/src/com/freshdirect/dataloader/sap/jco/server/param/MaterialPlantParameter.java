/**
 * 
 */
package com.freshdirect.dataloader.sap.jco.server.param;

import java.io.Serializable;

/**
 * @author kkanuganti
 */
public class MaterialPlantParameter extends AbstractMaterialParameter implements Serializable
{
	private static final long serialVersionUID = 9196999292572348583L;

	private String expertRating;
	private boolean platterInd;
	private String department;
	private String availabilityCheck;
	private String blockedSaleInd;
	private int daysInHouse;
	private String leadTime;
	private String sustainabilityRating;
	private String salesUnitCode;
	private String materialStatusDescription;
	private String materialStatus;
	private String departmentDescription;
	private boolean kosherInd;
	/**
	 * @return the expertRating
	 */
	public String getExpertRating()
	{
		return expertRating;
	}
	/**
	 * @param expertRating the expertRating to set
	 */
	public void setExpertRating(String expertRating)
	{
		this.expertRating = expertRating;
	}
	/**
	 * @return the platterInd
	 */
	public boolean isPlatterInd()
	{
		return platterInd;
	}
	/**
	 * @param platterInd the platterInd to set
	 */
	public void setPlatterInd(boolean platterInd)
	{
		this.platterInd = platterInd;
	}
	/**
	 * @return the department
	 */
	public String getDepartment()
	{
		return department;
	}
	/**
	 * @param department the department to set
	 */
	public void setDepartment(String department)
	{
		this.department = department;
	}
	/**
	 * @return the availabilityCheck
	 */
	public String getAvailabilityCheck()
	{
		return availabilityCheck;
	}
	/**
	 * @param availabilityCheck the availabilityCheck to set
	 */
	public void setAvailabilityCheck(String availabilityCheck)
	{
		this.availabilityCheck = availabilityCheck;
	}
	/**
	 * @return the blockedSaleInd
	 */
	public String getBlockedSaleInd()
	{
		return blockedSaleInd;
	}
	/**
	 * @param blockedSaleInd the blockedSaleInd to set
	 */
	public void setBlockedSaleInd(String blockedSaleInd)
	{
		this.blockedSaleInd = blockedSaleInd;
	}
	/**
	 * @return the daysInHouse
	 */
	public int getDaysInHouse()
	{
		return daysInHouse;
	}
	/**
	 * @param daysInHouse the daysInHouse to set
	 */
	public void setDaysInHouse(int daysInHouse)
	{
		this.daysInHouse = daysInHouse;
	}
	/**
	 * @return the leadTime
	 */
	public String getLeadTime()
	{
		return leadTime;
	}
	/**
	 * @param leadTime the leadTime to set
	 */
	public void setLeadTime(String leadTime)
	{
		this.leadTime = leadTime;
	}
	/**
	 * @return the sustainabilityRating
	 */
	public String getSustainabilityRating()
	{
		return sustainabilityRating;
	}
	/**
	 * @param sustainabilityRating the sustainabilityRating to set
	 */
	public void setSustainabilityRating(String sustainabilityRating)
	{
		this.sustainabilityRating = sustainabilityRating;
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
	 * @return the materialStatusDescription
	 */
	public String getMaterialStatusDescription()
	{
		return materialStatusDescription;
	}
	/**
	 * @param materialStatusDescription the materialStatusDescription to set
	 */
	public void setMaterialStatusDescription(String materialStatusDescription)
	{
		this.materialStatusDescription = materialStatusDescription;
	}
	/**
	 * @return the materialStatus
	 */
	public String getMaterialStatus()
	{
		return materialStatus;
	}
	/**
	 * @param materialStatus the materialStatus to set
	 */
	public void setMaterialStatus(String materialStatus)
	{
		this.materialStatus = materialStatus;
	}
	/**
	 * @return the departmentDescription
	 */
	public String getDepartmentDescription()
	{
		return departmentDescription;
	}
	/**
	 * @param departmentDescription the departmentDescription to set
	 */
	public void setDepartmentDescription(String departmentDescription)
	{
		this.departmentDescription = departmentDescription;
	}
	/**
	 * @return the kosherInd
	 */
	public boolean isKosherInd()
	{
		return kosherInd;
	}
	/**
	 * @param kosherInd the kosherInd to set
	 */
	public void setKosherInd(boolean kosherInd)
	{
		this.kosherInd = kosherInd;
	}
}
