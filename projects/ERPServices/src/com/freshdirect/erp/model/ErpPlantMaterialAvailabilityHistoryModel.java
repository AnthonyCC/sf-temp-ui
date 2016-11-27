/**
 * 
 */
package com.freshdirect.erp.model;

import java.util.Date;

/**
 * @author ksriram
 *
 */
public class ErpPlantMaterialAvailabilityHistoryModel {

	private String id;
	private String plantMaterialId;
	private String sapId;
	private String skuCode;
	private String matId;
	private int version;
	private String unavailabilityStatus;
	private Date dateCreated;
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the plantMaterialId
	 */
	public String getPlantMaterialId() {
		return plantMaterialId;
	}
	/**
	 * @param plantMaterialId the plantMaterialId to set
	 */
	public void setPlantMaterialId(String plantMaterialId) {
		this.plantMaterialId = plantMaterialId;
	}
	/**
	 * @return the sapId
	 */
	public String getSapId() {
		return sapId;
	}
	/**
	 * @param sapId the sapId to set
	 */
	public void setSapId(String sapId) {
		this.sapId = sapId;
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
	 * @return the matId
	 */
	public String getMatId() {
		return matId;
	}
	/**
	 * @param matId the matId to set
	 */
	public void setMatId(String matId) {
		this.matId = matId;
	}
	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
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
	 * @return the dateCreated
	 */
	public Date getDateCreated() {
		return dateCreated;
	}
	/**
	 * @param dateCreated the dateCreated to set
	 */
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
}
