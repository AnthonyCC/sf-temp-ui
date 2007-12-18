package com.freshdirect.transadmin.web.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.FactoryUtils;
import org.apache.commons.collections.list.LazyList;


public class AssignmentCommand extends BaseCommand {

	private String planDate;
	
	private String zoneId;
	
	private String submitAssignment;
	
	private List planDataList = LazyList.decorate(
		      new ArrayList(),
		      FactoryUtils.instantiateFactory(DispatchSheetCommand.class));
	
	
	public List getPlanDataList() {
		return planDataList;
	}

	public void setPlanDataList(List planDataList) {
		this.planDataList = LazyList.decorate(
					planDataList,
			      FactoryUtils.instantiateFactory(DispatchSheetCommand.class));
	}

	public String getPlanDate() {
		return planDate;
	}

	public void setPlanDate(String planDate) {
		this.planDate = planDate;
	}

	public String getZoneId() {
		return zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	public String getSubmitAssignment() {
		return submitAssignment;
	}

	public void setSubmitAssignment(String submitAssignment) {
		this.submitAssignment = submitAssignment;
	}

	
	

}

