package com.freshdirect.transadmin.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.freshdirect.transadmin.web.model.BaseCommand;

public class VerificationLog extends BaseCommand {
	private String id;	
	private String serviceStatus;	
	private Date estimatedRepairDate;
	private Date actualRepairDate;
	private Date verificationDate;
	private String verifiedBy;
	private String repairedBy;	
	
	public VerificationLog(){
		
	}	
	
	public VerificationLog(String id, String serviceStatus,
			Date estimatedRepairDate, Date actualRepairDate,
			Date verificationDate, String verifiedBy, String repairedBy) {
		super();
		this.id = id;
		this.serviceStatus = serviceStatus;
		this.estimatedRepairDate = estimatedRepairDate;
		this.actualRepairDate = actualRepairDate;
		this.verificationDate = verificationDate;
		this.verifiedBy = verifiedBy;
		this.repairedBy = repairedBy;
	}

	public VerificationLog(String id) {
		super();
		this.id = id;
	}	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}		
	
	public String getServiceStatus() {
		return serviceStatus;
	}
	public void setServiceStatus(String serviceStatus) {
		this.serviceStatus = serviceStatus;
	}
	
	public Date getEstimatedRepairDate() {
		return estimatedRepairDate;
	}
	public void setEstimatedRepairDate(Date estimatedRepairDate) {
		this.estimatedRepairDate = estimatedRepairDate;
	}
	public Date getActualRepairDate() {
		return actualRepairDate;
	}
	public void setActualRepairDate(Date actualRepairDate) {
		this.actualRepairDate = actualRepairDate;
	}
	public Date getVerificationDate() {
		return verificationDate;
	}
	public void setVerificationDate(Date verificationDate) {
		this.verificationDate = verificationDate;
	}
	public String getVerifiedBy() {
		return verifiedBy;
	}
	public void setVerifiedBy(String verifiedBy) {
		this.verifiedBy = verifiedBy;
	}
	
	public String getRepairedBy() {
		return repairedBy;
	}

	public void setRepairedBy(String repairedBy) {
		this.repairedBy = repairedBy;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VerificationLog other = (VerificationLog) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}	
	
}
