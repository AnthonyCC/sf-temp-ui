package com.freshdirect.transadmin.web.model;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.model.EmployeeRole;
import com.freshdirect.transadmin.util.TransStringUtil;

public class WebEmployeeInfo {
	
	
private String employeeId;
	
//	private String firstName;
//	
//	private String lastName;
//	
//	private String middleInitial;
//	
//	private String shortName;
//	
//	private String jobType;
//	
//	private Date hireDate;
//	
//	private String status;
//	
//	private String supervisorId;
//	
//	private String supervisorFirstName;
//	
//	private String supervisorMiddleInitial;
//	
//	private String supervisorLastName;
//	
//	private String supervisorShortName;
//	
	
	// private EmployeeroleId id;
	//private EmployeeRoleType employeeRoleType;
	
	private EmployeeInfo empInfo;
	private Collection  empRole;
	
	public WebEmployeeInfo(EmployeeInfo eInfo,Collection eRole){
		this.empInfo=eInfo;
		this.empRole=eRole;
		
	}

	public EmployeeInfo getEmpInfo() {
		return empInfo;
	}

	public void setEmpInfo(EmployeeInfo empInfo) {
		this.empInfo = empInfo;
	}

	public String getEmployeeId() {
		return empInfo.getEmployeeId();
	}

	public String getEmployeeRoleType() {		
		if(empRole==null) return null;
		
		System.out.println("getEmployeeRoleType:"+empRole);
		
		StringBuffer buf=new StringBuffer();
		
		Iterator iterator=empRole.iterator();
		while(iterator.hasNext()){
			EmployeeRole role=(EmployeeRole)iterator.next();
			buf.append(role.getEmployeeRoleType().getName()).append("/");
		}
		
		return buf.toString();
	}
	
	
	public Collection getEmployeeRoleTypes() {		
		if(empRole==null) return null;
		
		System.out.println("getEmployeeRoleTypesssss:"+empRole);
		
		//StringBuffer buf=new StringBuffer();
		List empList=new ArrayList();
		Iterator iterator=empRole.iterator();
		while(iterator.hasNext()){
			EmployeeRole role=(EmployeeRole)iterator.next();
			empList.add(role.getEmployeeRoleType());
		}
		
		return empList;
	}

	public void setEmployeeRoleType(Collection employeeRoles) {	
		      this.empRole=employeeRoles;
	}

	public Collection getEmpRole() {
		return empRole;
	}

	public void setEmpRole(Collection empRole) {
		this.empRole = empRole;
	}

	public String getFirstName() {
		return empInfo.getFirstName();
	}


	public Date getHireDate() {
		return empInfo.getHireDate();
	}


	public String getJobType() {
		return empInfo.getJobType();
	}


	public String getLastName() {
		return empInfo.getLastName();
	}


	public String getMiddleInitial() {
		return empInfo.getMiddleInitial();
	}


	public String getShortName() {
		return empInfo.getShortName();
	}


	public String getStatus() {
		return empInfo.getStatus();
	}


	public String getSupervisorFirstName() {
		return empInfo.getSupervisorFirstName();
	}


	public String getSupervisorId() {
		return empInfo.getSupervisorId();
	}


	public String getSupervisorLastName() {
		return empInfo.getSupervisorLastName();
	}


	public String getSupervisorMiddleInitial() {
		return empInfo.getSupervisorMiddleInitial();
	}


	public String getSupervisorShortName() {
		return empInfo.getSupervisorShortName();
	}
	
	public String getTerminationDate()
	{ 
		if(empInfo.getTerminationDate()==null) return "";				
		 try {
			return TransStringUtil.getDate(empInfo.getTerminationDate());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
}
