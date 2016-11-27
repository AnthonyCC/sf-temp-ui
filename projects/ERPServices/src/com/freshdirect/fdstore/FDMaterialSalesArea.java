/**
 * 
 */
package com.freshdirect.fdstore;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ksriram
 *
 */
public class FDMaterialSalesArea implements Serializable {
	
	private SalesAreaInfo salesAreaInfo;
	
	private String unavailabilityStatus;
	private Date unavailabilityDate;
	private String unavailabilityReason;
	private EnumDayPartValueType dayPartValueType;
	private String pickingPlantId;
	/**
	 * 
	 */
	public FDMaterialSalesArea() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param salesAreaInfo
	 * @param unavailabilityStatus
	 * @param unavailabilityDate
	 * @param unavailabilityReason
	 */
	public FDMaterialSalesArea(SalesAreaInfo salesAreaInfo,
			String unavailabilityStatus, Date unavailabilityDate,
			String unavailabilityReason, EnumDayPartValueType dayPartValueType,String pickingPlantId) {
		this.salesAreaInfo = salesAreaInfo;
		this.unavailabilityStatus = unavailabilityStatus;
		this.unavailabilityDate = unavailabilityDate;
		this.unavailabilityReason = unavailabilityReason;
		this.dayPartValueType = dayPartValueType;
		this.pickingPlantId = pickingPlantId;
	}

	/**
	 * @return the salesAreaInfo
	 */
	public SalesAreaInfo getSalesAreaInfo() {
		return salesAreaInfo;
	}
	/**
	 * @param salesAreaInfo the salesAreaInfo to set
	 */
	public void setSalesAreaInfo(SalesAreaInfo salesAreaInfo) {
		this.salesAreaInfo = salesAreaInfo;
	}
	/**
	 * @return the unavailabilityStatus
	 */
	public String getUnavailabilityStatus() {
		return unavailabilityStatus;
	}
	/**
	 * @param unavailabilityStatus the unavailabilityStatus to set
	 */
	public void setUnavailabilityStatus(String unavailabilityStatus) {
		this.unavailabilityStatus = unavailabilityStatus;
	}
	/**
	 * @return the unavailabilityDate
	 */
	public Date getUnavailabilityDate() {
		return unavailabilityDate;
	}
	/**
	 * @param unavailabilityDate the unavailabilityDate to set
	 */
	public void setUnavailabilityDate(Date unavailabilityDate) {
		this.unavailabilityDate = unavailabilityDate;
	}
	/**
	 * @return the unavailabilityReason
	 */
	public String getUnavailabilityReason() {
		return unavailabilityReason;
	}
	/**
	 * @param unavailabilityReason the unavailabilityReason to set
	 */
	public void setUnavailabilityReason(String unavailabilityReason) {
		this.unavailabilityReason = unavailabilityReason;
	}

	/**
	 * @return the dayPartSellingType
	 */
	public EnumDayPartValueType getDayPartValueType() {
		return dayPartValueType;
	}

	/**
	 * @param dayPartSellingType the dayPartSellingType to set
	 */
	public void setDayPartValueType(EnumDayPartValueType dayPartValueType) {
		this.dayPartValueType = dayPartValueType;
	}

	/**
	 * @return the pickingPlantId
	 */
	public String getPickingPlantId() {
		return pickingPlantId;
	}

	/**
	 * @param pickingPlantId the pickingPlantId to set
	 */
	public void setPickingPlantId(String pickingPlantId) {
		this.pickingPlantId = pickingPlantId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FDMaterialSalesArea [salesAreaInfo=" + salesAreaInfo + ", unavailabilityStatus=" + unavailabilityStatus
				+ ", unavailabilityDate=" + unavailabilityDate + ", unavailabilityReason=" + unavailabilityReason
				+ ", dayPartSellingType=" + dayPartValueType + "]";
	}
	
	
}
