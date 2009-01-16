package com.freshdirect.transadmin.web.model;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.model.ResourceInfoI;

public class DispatchResourceInfo extends BaseCommand implements ResourceInfoI { 
	private String nextelNo;
	private static final String SEPERATOR = " ";
	private String lastName;
	private String firstName;
	private String employeeId;


	public String getName() {
		
		if((lastName==null ||"".equals(lastName))&&(firstName==null || "".equals(firstName)))
			return "";
		if(lastName==null || "".equals(lastName))
			return firstName;
		if(firstName==null || "".equals(firstName))
			return lastName;
		return lastName+SEPERATOR+firstName;
	}

	public String getNameWithFirstInitial() {
		if((lastName==null ||"".equals(lastName))&&(firstName==null || "".equals(firstName)))
			return "";
		if(lastName==null || "".equals(lastName))
			return firstName;
		if(firstName==null || "".equals(firstName))
			return lastName;
		return lastName+", "+firstName.substring(0,1);
	}
	
	public String toString() {
		
		return employeeId;
	}
	
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}


	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}


	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	/**
	 * @return the employeeId
	 */
	public String getEmployeeId() {
		return employeeId;
	}


	/**
	 * @param employeeId the employeeId to set
	 */
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	

	public String getNextelNo() {
		return this.nextelNo != null ? this.nextelNo : "";
	}

	public void setNextelNo(String nextelNo) {
		this.nextelNo = nextelNo;
	}	
}
