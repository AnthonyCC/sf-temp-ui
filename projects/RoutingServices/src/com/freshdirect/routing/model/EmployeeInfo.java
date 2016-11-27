package com.freshdirect.routing.model;

import java.util.Date;

public class EmployeeInfo implements java.io.Serializable {

	private static final String SEPERATOR = " ";
	private String employeeId;
	private String firstName;
	private String lastName;
	private String middleInitial;
	private String shortName;
	private String jobType;
	private Date hireDate;
	private String status;
	private String supervisorId;
	private String supervisorFirstName;
	private String supervisorMiddleInitial;
	private String supervisorLastName;
	private String supervisorShortName;
	private Date terminationDate;
	private Date adjustmentTime;

	public EmployeeInfo() {
	}

	public EmployeeInfo(String employeeId, String firstName, String lastName,
			String middleInitial, String shortName, String jobType,
			Date hireDate, String status, String supervisorId,
			String supervisorFirstName, String supervisorMiddleInitial,
			String supervisorLastName, String supervisorShortName,
			Date terminationDate) {
		super();
		this.employeeId = employeeId;
		this.firstName = firstName;
		this.lastName = lastName;

		this.middleInitial = middleInitial;
		this.shortName = shortName;

		this.jobType = jobType;
		this.hireDate = hireDate;
		this.status = status;
		this.supervisorId = supervisorId;
		this.supervisorFirstName = supervisorFirstName;
		this.supervisorMiddleInitial = supervisorMiddleInitial;
		this.supervisorLastName = supervisorLastName;
		this.supervisorShortName = supervisorShortName;
		this.terminationDate = terminationDate;
	}

	public boolean isObsoleteEntity() {

		return false;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public String getFirstName() {
		return firstName;
	}

	public Date getHireDate() {
		return hireDate;
	}

	public String getJobType() {
		return jobType;
	}

	public String getLastName() {
		return lastName;
	}

	public String getMiddleInitial() {
		return middleInitial;
	}

	public String getShortName() {
		return shortName;
	}

	public String getStatus() {
		return status;
	}

	public String getSupervisorFirstName() {
		return supervisorFirstName;
	}

	public String getSupervisorId() {
		return supervisorId;
	}

	public String getSupervisorLastName() {
		return supervisorLastName;
	}

	public String getSupervisorMiddleInitial() {
		return supervisorMiddleInitial;
	}

	public String getSupervisorShortName() {
		return supervisorShortName;
	}

	public String getName() {

		if ((lastName == null || "".equals(lastName))
				&& (firstName == null || "".equals(firstName)))
			return "";
		if (lastName == null || "".equals(lastName))
			return firstName;
		if (firstName == null || "".equals(firstName))
			return lastName;
		return lastName + SEPERATOR + firstName;
	}

	public String getSupervisorInfo() {

		if ((lastName == null || "".equals(lastName))
				&& (firstName == null || "".equals(firstName))
				&& (employeeId == null || "".equals(employeeId)))
			return "";
		if (lastName == null || "".equals(lastName))
			return firstName;
		if (firstName == null || "".equals(firstName))
			return lastName;
		if (employeeId == null || "".equals(employeeId))
			return lastName;
		return lastName + SEPERATOR + firstName + " - " + employeeId;
	}

	public String getSupervisorName() {

		if ((lastName == null || "".equals(lastName))
				&& (firstName == null || "".equals(firstName)))
			return "";
		if (lastName == null || "".equals(lastName))
			return firstName;
		if (firstName == null || "".equals(firstName))
			return lastName;
		return lastName + ", " + firstName;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNextelNo() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setNextelNo(String nextelNo) {
		// TODO Auto-generated method stub

	}

	public Date getTerminationDate() {
		return terminationDate;
	}

	public void setTerminationDate(Date terminationDate) {
		this.terminationDate = terminationDate;
	}

	public Date getAdjustmentTime() {
		return adjustmentTime;
	}

	public void setAdjustmentTime(Date adjustmentTime) {
		this.adjustmentTime = adjustmentTime;
	}

	@Override
	public String toString() {
		return this.getName() + "[" + employeeId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((employeeId == null) ? 0 : employeeId.hashCode());
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
		EmployeeInfo other = (EmployeeInfo) obj;
		if (employeeId == null) {
			if (other.employeeId != null)
				return false;
		} else if (!employeeId.equals(other.employeeId))
			return false;
		return true;
	}

}
