/**
 * @author ekracoff
 * Created on Apr 27, 2005*/

package com.freshdirect.ocf.core;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Campaign extends Entity {
	private String name;
	private String description;
	private Set flights = new HashSet();
	private Date startDate;
	private Date endDate;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set getFlights() {
		return flights;
	}

	public void setFlights(Set flights) {
		this.flights = flights;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
}