package com.freshdirect.routing.model;

public class TruckPreferenceStat {
	private String truckId;
	private String employeeId;
	private String prefKey;
	
	public TruckPreferenceStat(String truckId, String employeeId, String prefKey) {
		super();
		this.truckId = truckId;
		this.employeeId = employeeId;
		this.prefKey = prefKey;
	}
	
	public TruckPreferenceStat() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;	
		result = prime * result + ((employeeId == null) ? 0 : employeeId.hashCode());	
		result = prime * result + ((truckId == null) ? 0 : truckId.hashCode());
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
		TruckPreferenceStat other = (TruckPreferenceStat) obj;
		
		if (employeeId == null) {
			if (other.employeeId != null)
				return false;
		} else if (!employeeId.equals(other.employeeId))
			return false;		
		if (truckId == null) {
			if (other.truckId != null)
				return false;
		} else if (!truckId.equals(other.truckId))
			return false;
		return true;
	}
	
	public String getTruckId() {
		return truckId;
	}
	public void setTruckId(String truckId) {
		this.truckId = truckId;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getPrefKey() {
		return prefKey;
	}
	public void setPrefKey(String prefKey) {
		this.prefKey = prefKey;
	}
	
	
}
