package com.freshdirect.transadmin.model;

public class EmployeeStatus 
{
	private String personnum;
	private boolean status;
	
	public EmployeeStatus()
	{
		
	}
	public EmployeeStatus(String personnum,boolean status)
	{
		this.personnum=personnum;
		this.status=status;
	}
	public String getPersonnum() {
		return personnum;
	}
	public void setPersonnum(String personnum) {
		this.personnum = personnum;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	

}
