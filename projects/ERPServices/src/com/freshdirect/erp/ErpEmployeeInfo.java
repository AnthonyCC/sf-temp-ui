package com.freshdirect.erp;

import java.io.Serializable;
import java.util.Date;

public class ErpEmployeeInfo implements Serializable {
		
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
	
	public ErpEmployeeInfo(String employeeId, String firstName, String lastName
							, String middleInitial, String shortName, String jobType, Date hireDate
									, String status, String supervisorId, String supervisorFirstName
											, String supervisorMiddleInitial, String supervisorLastName, String supervisorShortName
													,Date terminationDate) {
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
		this.terminationDate=terminationDate;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Date getHireDate() {
		return hireDate;
	}

	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleInitial() {
		return middleInitial;
	}

	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSupervisorFirstName() {
		return supervisorFirstName;
	}

	public void setSupervisorFirstName(String supervisorFirstName) {
		this.supervisorFirstName = supervisorFirstName;
	}

	public String getSupervisorId() {
		return supervisorId;
	}

	public void setSupervisorId(String supervisorId) {
		this.supervisorId = supervisorId;
	}

	public String getSupervisorLastName() {
		return supervisorLastName;
	}

	public void setSupervisorLastName(String supervisorLastName) {
		this.supervisorLastName = supervisorLastName;
	}

	public String getSupervisorMiddleInitial() {
		return supervisorMiddleInitial;
	}

	public void setSupervisorMiddleInitial(String supervisorMiddleInitial) {
		this.supervisorMiddleInitial = supervisorMiddleInitial;
	}

	public String getSupervisorShortName() {
		return supervisorShortName;
	}

	public void setSupervisorShortName(String supervisorShortName) {
		this.supervisorShortName = supervisorShortName;
	}

	public Date getTerminationDate() {
		return terminationDate;
	}

	public void setTerminationDate(Date terminationDate) {
		this.terminationDate = terminationDate;
	}

}
