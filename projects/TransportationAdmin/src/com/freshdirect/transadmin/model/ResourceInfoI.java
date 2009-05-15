package com.freshdirect.transadmin.model;

public interface ResourceInfoI {

	public String getName();
	
	/**
	 * @return the firstName
	 */
	public String getFirstName();


	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName);


	/**
	 * @return the lastName
	 */
	public String getLastName();


	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName);


	/**
	 * @return the employeeId
	 */
	public String getEmployeeId() ;


	/**
	 * @param employeeId the employeeId to set
	 */
	public void setEmployeeId(String employeeId);
	

	public String getNextelNo() ;

	public void setNextelNo(String nextelNo) ;
	
	public PunchInfoI getPunchInfo();
	
	public void setPunchInfo(PunchInfoI punchInfo);
	
}
