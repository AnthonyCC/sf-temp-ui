package com.freshdirect.transadmin.model;

import java.util.HashSet;
import java.util.Set;

import com.freshdirect.framework.util.TimeOfDay;

public class BuildingOperationHours implements java.io.Serializable, TrnBaseEntityI  {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TimeOfDay bldgStartHour;
	private TimeOfDay bldgEndHour;
	private TimeOfDay serviceStartHour;
	private TimeOfDay serviceEndHour;

	private String bldgComments;
	private String serviceComments;
	
	private BuildingOperationHoursId id;
	
	public BuildingOperationHoursId getId() {
		return this.id;
	}

	public void setId(BuildingOperationHoursId id) {
		this.id = id;
	}

	
	public TimeOfDay getBldgStartHour() {
		return bldgStartHour;
	}


	public void setBldgStartHour(TimeOfDay bldgStartHour) {
		this.bldgStartHour = bldgStartHour;
	}


	public TimeOfDay getBldgEndHour() {
		return bldgEndHour;
	}


	public void setBldgEndHour(TimeOfDay bldgEndHour) {
		this.bldgEndHour = bldgEndHour;
	}


	public TimeOfDay getServiceStartHour() {
		return serviceStartHour;
	}


	public void setServiceStartHour(TimeOfDay serviceStartHour) {
		this.serviceStartHour = serviceStartHour;
	}


	public TimeOfDay getServiceEndHour() {
		return serviceEndHour;
	}


	public void setServiceEndHour(TimeOfDay serviceEndHour) {
		this.serviceEndHour = serviceEndHour;
	}


	public String getBldgComments() {
		return bldgComments;
	}


	public void setBldgComments(String bldgComments) {
		this.bldgComments = bldgComments;
	}


	public String getServiceComments() {
		return serviceComments;
	}


	public void setServiceComments(String serviceComments) {
		this.serviceComments = serviceComments;
	}


	@Override
	public boolean isObsoleteEntity() {
		// TODO Auto-generated method stub
		return false;
	}

}
