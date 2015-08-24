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
	private boolean isLimitedQuantity;
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
			String unavailabilityReason) {
		this.salesAreaInfo = salesAreaInfo;
		this.unavailabilityStatus = unavailabilityStatus;
		this.unavailabilityDate = unavailabilityDate;
		this.unavailabilityReason = unavailabilityReason;
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
	
	@Override
	public String toString() {
		return "FDMaterialSalesArea [salesAreaInfo=" + salesAreaInfo
				+ ", unavailabilityStatus=" + unavailabilityStatus
				+ ", unavailabilityDate=" + unavailabilityDate
				+ ", isLimitedQuantity=" + isLimitedQuantity
				+ ", unavailabilityReason=" + unavailabilityReason + "]";
	}

	public boolean isLimitedQuantity() {
		return isLimitedQuantity;
	}

	public void setLimitedQuantity(boolean isLimitedQuantity) {
		this.isLimitedQuantity = isLimitedQuantity;
	}
}
