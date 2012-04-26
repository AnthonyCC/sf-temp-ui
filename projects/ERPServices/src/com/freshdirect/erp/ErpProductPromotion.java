package com.freshdirect.erp;

import java.util.Date;

import com.freshdirect.framework.core.ModelSupport;

public class ErpProductPromotion extends ModelSupport {

	private String erpPromtoionId;
	private String ppType;
	private String ppName;
	private String ppStatus;
	private String ppDescription;
	private Date startDate;
	private Date endDate;
	private Integer version;
	
	
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public String getErpPromtoionId() {
		return erpPromtoionId;
	}
	public void setErpPromtoionId(String erpPromtoionId) {
		this.erpPromtoionId = erpPromtoionId;
	}
	public String getPpType() {
		return ppType;
	}
	public void setPpType(String ppType) {
		this.ppType = ppType;
	}
	public String getPpName() {
		return ppName;
	}
	public void setPpName(String ppName) {
		this.ppName = ppName;
	}
	public String getPpStatus() {
		return ppStatus;
	}
	public void setPpStatus(String ppStatus) {
		this.ppStatus = ppStatus;
	}
	public String getPpDescription() {
		return ppDescription;
	}
	public void setPpDescription(String ppDescription) {
		this.ppDescription = ppDescription;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	
	
	
}
