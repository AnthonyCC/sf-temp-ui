package com.freshdirect.routing.truckassignment;

import java.util.Date;
import java.util.LinkedHashMap;

public class Employee extends Id {
	private Date hireDate;
	private LinkedHashMap<Truck, Integer> preferences;

	public Employee(String id) {
		super(id);
		preferences = new LinkedHashMap<Truck, Integer>();
	}

	public Date getHireDate() {
		return hireDate;
	}

	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}

	public LinkedHashMap<Truck, Integer> getPreferences() {
		return preferences;
	}

	public void setPreferences(LinkedHashMap<Truck, Integer> preferences) {
		this.preferences = preferences;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Employee [id=");
		builder.append(getId());
		builder.append(", hireDate=");
		builder.append(hireDate != null ? TruckAssignmentProblem.DATE_FORMAT.format(hireDate) : null);
		builder.append(", preferences=");
		builder.append(preferences.keySet().toString());
		builder.append("]");
		return builder.toString();
	}
}