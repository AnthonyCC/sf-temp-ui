package com.freshdirect.transadmin.web.model;

import java.util.List;

public class EarlyWarningCommand extends BaseCommand {
	
	private String code;
	
	private String name;
	
	private String totalCapacity;
	
	private String confirmedCapacity;
		
	private String percentageConfirmed;
	
	private String allocatedCapacity;
	
	private String percentageAllocated;
	
	private List<EarlyWarningCommand> timeslotDetails;

	
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
