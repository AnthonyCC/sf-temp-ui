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
				
	private String referenceId;
	
	private boolean isRegion;

	
	private int openCount = 0;
	private int closedCount = 0;
	private int dynamicActiveCount = 0;
	private int dynamicInActiveCount = 0;
	
	private int noOfResources;
		
	private boolean isDiscounted;
		
	public boolean isDiscounted() {
		return isDiscounted;
	}

	public void setDiscounted(boolean isDiscounted) {
		this.isDiscounted = isDiscounted;
	}

	public int getOpenCount() {
		return openCount;
	}

	public void setOpenCount(int openCount) {
		this.openCount = openCount;
	}

	public int getClosedCount() {
		return closedCount;
	}

	public void setClosedCount(int closedCount) {
		this.closedCount = closedCount;
	}

	public int getDynamicActiveCount() {
		return dynamicActiveCount;
	}

	public void setDynamicActiveCount(int dynamicActiveCount) {
		this.dynamicActiveCount = dynamicActiveCount;
	}

	

	public int getDynamicInActiveCount() {
		return dynamicInActiveCount;
	}

	public void setDynamicInActiveCount(int dynamicInActiveCount) {
		this.dynamicInActiveCount = dynamicInActiveCount;
	}

	public boolean isRegion() {
		return isRegion;
	}

	public void setRegion(boolean isRegion) {
		this.isRegion = isRegion;
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

	public int getNoOfResources() {
		return noOfResources;
	}

	public void setNoOfResources(int noOfResources) {
		this.noOfResources = noOfResources;
	}
	
	
}
