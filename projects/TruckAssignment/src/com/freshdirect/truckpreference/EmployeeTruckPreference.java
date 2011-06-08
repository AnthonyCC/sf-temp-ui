package com.freshdirect.truckpreference;

public class EmployeeTruckPreference {
	private String employeeId;
	private String truckNumber;
	private int count;	
	private String prefKey;
	
	public EmployeeTruckPreference(String employeeId, String truckNumber,
			int count) {
		super();
		this.employeeId = employeeId;
		this.truckNumber = truckNumber;
		this.count = count;
	}
	
	public String getPrefKey() {
		return prefKey;
	}

	public void setPrefKey(String prefKey) {
		this.prefKey = prefKey;
	}

	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getTruckNumber() {
		return truckNumber;
	}
	public void setTruckNumber(String truckNumber) {
		this.truckNumber = truckNumber;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	
}
