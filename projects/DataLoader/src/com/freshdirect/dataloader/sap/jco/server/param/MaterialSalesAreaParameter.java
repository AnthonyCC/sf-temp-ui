/**
 * 
 */
package com.freshdirect.dataloader.sap.jco.server.param;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ksriram
 *
 */
public class MaterialSalesAreaParameter implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8341688171525633686L;
	
	private String distributionChannelId;	
	private String salesOrganizationId;
	private String unavailabilityStatus;
	private String unavailabilityReason;
	private Date unavailabilityDate;
	private String skuCode;
	private String materialID;
	/**
	 * @return the distributionChannelId
	 */
	public String getDistributionChannelId() {
		return distributionChannelId;
	}
	/**
	 * @param distributionChannelId the distributionChannelId to set
	 */
	public void setDistributionChannelId(String distributionChannelId) {
		this.distributionChannelId = distributionChannelId;
	}
	/**
	 * @return the salesOrganizationId
	 */
	public String getSalesOrganizationId() {
		return salesOrganizationId;
	}
	/**
	 * @param salesOrganizationId the salesOrganizationId to set
	 */
	public void setSalesOrganizationId(String salesOrganizationId) {
		this.salesOrganizationId = salesOrganizationId;
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
	 * @return the skuCode
	 */
	public String getSkuCode() {
		return skuCode;
	}
	/**
	 * @param skuCode the skuCode to set
	 */
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	/**
	 * @return the materialID
	 */
	public String getMaterialID() {
		return materialID;
	}
	/**
	 * @param materialID the materialID to set
	 */
	public void setMaterialID(String materialID) {
		this.materialID = materialID;
	}
	
	
	
	

}
