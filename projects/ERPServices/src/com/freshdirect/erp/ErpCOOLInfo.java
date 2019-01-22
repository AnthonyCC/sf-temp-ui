package com.freshdirect.erp;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErpCOOLInfo implements Serializable {	
	
	private static final long	serialVersionUID	= -1120548872587933250L;
	
	private String sapID;
	private String sapDesc;
	private List<String> countryInfo;
	private Date lastModifiedDate;
	private String plantId;
	
	public ErpCOOLInfo(String sapID, String sapDesc, List<String> countryInfo, String plantId)
	{
		this.sapID = sapID;
		this.sapDesc = sapDesc;
		this.countryInfo = countryInfo;
		this.plantId = plantId;
	}

	public ErpCOOLInfo(@JsonProperty("sapID") String sapID, @JsonProperty("sapDesc") String sapDesc,
			@JsonProperty("countryInfo") List<String> countryInfo,
			@JsonProperty("lastModifiedDate") Date lastModifiedDate, @JsonProperty("plantId") String plantId) {
		this.sapID = sapID;
		this.sapDesc = sapDesc;
		this.countryInfo = countryInfo;
		this.lastModifiedDate = lastModifiedDate;
		this.plantId = plantId;
	}
	
	public List<String> getCountryInfo() {
		return countryInfo;
	}
	public String getSapID() {
		return sapID;
	}
	public String getSapDesc() {
		return sapDesc;
	}
		
	public Date getLastModifiedDate(){
		return this.lastModifiedDate;
	}
	
	public String toString() {
		return new StringBuffer(300).append("ErpCOOLInfo[sapID: ")
			.append( this.sapID)
			.append(" sapDesc: ")
			.append(sapDesc)
			.append(" lastModifiedDate: ")
			.append(lastModifiedDate)
			.append(" plantId: ")
			.append(plantId)
			.append(" countryInfo: ")
			.append(countryInfo.toString())
			.append("]").toString();
	}

	/**
	 * @return the plantId
	 */
	public String getPlantId() {
		return plantId;
	}

}
