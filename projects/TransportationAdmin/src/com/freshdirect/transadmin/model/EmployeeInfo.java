package com.freshdirect.transadmin.model;

import java.text.ParseException;
import java.util.Date;

import com.freshdirect.transadmin.util.TransStringUtil;

public class EmployeeInfo implements java.io.Serializable, TrnBaseEntityI, ResourceInfoI {

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
	
	private PunchInfoI punchInfo=null;
	
	private Date adjustmentTime;
	
	public EmployeeInfo() {
	}
	public EmployeeInfo(String employeeId, String firstName, String lastName, String middleInitial, String shortName, String jobType, Date hireDate, String status, String supervisorId, String supervisorFirstName, String supervisorMiddleInitial, String supervisorLastName, String supervisorShortName,Date terminationDate) {
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
		
		if((lastName==null ||"".equals(lastName))&&(firstName==null || "".equals(firstName)))
			return "";
		if(lastName==null || "".equals(lastName))
			return firstName;
		if(firstName==null || "".equals(firstName))
			return lastName;
		return lastName+SEPERATOR+firstName;
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
	
	public PunchInfoI getPunchInfo() {
		return punchInfo;
	}
	
	public void setPunchInfo(PunchInfoI punchInfo) {
		this.punchInfo = punchInfo;
	}
	public Date getAdjustmentTime() {
		return adjustmentTime;
	}
	public void setAdjustmentTime(Date adjustmentTime) {
		this.adjustmentTime = adjustmentTime;
	}
	
	public String getAdjustmentTimeS() {
		try 
		{
			if(adjustmentTime!=null)return TransStringUtil.getServerTime(adjustmentTime);
		} catch (ParseException e) 
		{
			
		}
		return null;
	}
	public void setAdjustmentTimeS(String adjustmentTimeS) {
		try 
		{
			if(adjustmentTimeS!=null&&adjustmentTimeS.length()>0)adjustmentTime=TransStringUtil.getServerTime(adjustmentTimeS);
			else adjustmentTime=null;
		} catch (ParseException e) 
		{
			
		}
	}
	@Override
	public String toString() {
		return "EmployeeInfo [employeeId=" + employeeId + "]";
	}
	

}
