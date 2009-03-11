package com.freshdirect.erp;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ErpCOOLInfo implements Serializable {
	
	
	private String sapID;
	private String sapDesc;
	private List countryInfo;
	private Date lastModifiedDate;
	
	public ErpCOOLInfo(String sapID,String sapDesc, List countryInfo) {
		this.sapID=sapID;
		this.sapDesc=sapDesc;
		this.countryInfo=countryInfo;
	}
	
	public ErpCOOLInfo(String sapID,String sapDesc, List countryInfo,Date lastModifiedDate) {
		this.sapID=sapID;
		this.sapDesc=sapDesc;
		this.countryInfo=countryInfo;
		this.lastModifiedDate=lastModifiedDate;
	}
	
	public List getCountryInfo() {
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
			.append(" countryInfo: ")
			.append(countryInfo.toString())
			.append("]").toString();
	}

}
