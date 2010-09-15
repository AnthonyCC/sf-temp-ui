package com.freshdirect.erp;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.freshdirect.fdstore.FDVersion;

public class ErpCOOLInfo implements Serializable, FDVersion<Date> {

	private static final long	serialVersionUID	= -1120548872587933250L;

	private String sapID;
	private String sapDesc;
	private List<String> countryInfo;
	private Date lastModifiedDate;

	public ErpCOOLInfo(String sapID,String sapDesc, List<String> countryInfo) {
		this.sapID=sapID;
		this.sapDesc=sapDesc;
		this.countryInfo=countryInfo;
	}

	public ErpCOOLInfo(String sapID,String sapDesc, List<String> countryInfo,Date lastModifiedDate) {
		this.sapID=sapID;
		this.sapDesc=sapDesc;
		this.countryInfo=countryInfo;
		this.lastModifiedDate=lastModifiedDate;
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
			.append(" countryInfo: ")
			.append(countryInfo.toString())
			.append("]").toString();
	}

	@Override
	public Date getVersion() {
		return lastModifiedDate;
	}
}
