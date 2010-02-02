package com.freshdirect.transadmin.web.model;

import java.util.Date;
import java.util.List;

import com.freshdirect.routing.model.IRoutingSchedulerIdentity;

public class EarlyWarningCommand extends BaseCommand {
	
	private String code;
	
	private String name;
	
	private String totalCapacity;
	
	private String confirmedCapacity;
		
	private String percentageConfirmed;
	
	private String allocatedCapacity;
	
	private String percentageAllocated;
	
	private List<EarlyWarningCommand> timeslotDetails;

	private boolean manuallyClosed;
	
	private String referenceId;
	
	private boolean isRegion;

	private boolean dynamicActive;
	
	
	

	public boolean isDynamicActive() {
		return dynamicActive;
	}

	public void setDynamicActive(boolean dynamicActive) {
		this.dynamicActive = dynamicActive;
	}

	public boolean isRegion() {
		return isRegion;
	}

	public void setRegion(boolean isRegion) {
		this.isRegion = isRegion;
	}

	public boolean isManuallyClosed() {
		return manuallyClosed;
	}

	public void setManuallyClosed(boolean manuallyClosed) {
		this.manuallyClosed = manuallyClosed;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public List<EarlyWarningCommand> getTimeslotDetails() {
		return timeslotDetails;
	}

	public void setTimeslotDetails(List<EarlyWarningCommand> timeslotDetails) {
		this.timeslotDetails = timeslotDetails;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTotalCapacity() {
		return totalCapacity;
	}

	public void setTotalCapacity(String totalCapacity) {
		this.totalCapacity = totalCapacity;
	}

	public String getConfirmedCapacity() {
		return confirmedCapacity;
	}

	public void setConfirmedCapacity(String confirmedCapacity) {
		this.confirmedCapacity = confirmedCapacity;
	}

	public String getPercentageConfirmed() {
		return percentageConfirmed;
	}

	public void setPercentageConfirmed(String percentageConfirmed) {
		this.percentageConfirmed = percentageConfirmed;
	}

	public String getAllocatedCapacity() {
		return allocatedCapacity;
	}

	public void setAllocatedCapacity(String allocatedCapacity) {
		this.allocatedCapacity = allocatedCapacity;
	}

	public String getPercentageAllocated() {
		return percentageAllocated;
	}

	public void setPercentageAllocated(String percentageAllocated) {
		this.percentageAllocated = percentageAllocated;
	}
}
