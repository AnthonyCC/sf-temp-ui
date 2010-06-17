package com.freshdirect.transadmin.web.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.model.EmployeeRole;

public class WebEmployeeInfo implements Serializable {


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

	private String region;
	private boolean bullpen;
	private EmployeeInfo empInfo;
	private Collection  empRole;
	private String trnStatus;
	private EmployeeInfo leadInfo;
	private boolean isLead;

	
	public WebEmployeeInfo(EmployeeInfo eInfo,Collection eRole){
		this.empInfo=eInfo;
		this.empRole=eRole;

	}
	
	public boolean isLead() {
		return isLead;
	}
	public void setLead(boolean isLead) {
		this.isLead = isLead;
	}
	
	public EmployeeInfo getLeadInfo() {		
		return leadInfo;
	}
	
	public EmployeeInfo getLeadInfoEx() {
		if(this.isLead()) {
			return this.getEmpInfo();
		} else {
			return leadInfo;
		}
	}
	
	public String getLeadId() {	
		if(leadInfo != null) {
			return leadInfo.getEmployeeId();
		} else {
			return null;
		}
	}
	
	public void setLeadId() {	
		//Dummy Method
	}

	public void setLeadInfo(EmployeeInfo leadInfo) {
		this.leadInfo = leadInfo;
	}


	public EmployeeInfo getEmpInfo() {
		return empInfo;
	}

	public void setEmpInfo(EmployeeInfo empInfo) {
		this.empInfo = empInfo;
	}

	public String getEmployeeId() 
	{
		if(empInfo==null)return null;
		return empInfo.getEmployeeId();
	}

	public String getEmployeeRoleType() {
		if(empRole==null) return null;

//		System.out.println("getEmployeeRoleType:"+empRole);

		StringBuffer buf=new StringBuffer();

		Iterator iterator=empRole.iterator();
		while(iterator.hasNext()){
			EmployeeRole role=(EmployeeRole)iterator.next();
			if(buf.length()>0)
				buf.append("/").append(role.getEmployeeSubRoleType().getName());
			else
				buf.append(role.getEmployeeSubRoleType().getName());
		}

		return buf.toString();
	}


	public Collection getEmployeeRoleTypes() {
		if(empRole==null) return null;

	//	System.out.println("getEmployeeRoleTypesssss:"+empRole);

		//StringBuffer buf=new StringBuffer();
		List empList=new ArrayList();
		Iterator iterator=empRole.iterator();
		while(iterator.hasNext()){
			EmployeeRole role=(EmployeeRole)iterator.next();
			empList.add(role.getEmployeeSubRoleType());
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

	public Date getTerminationDate()
	{
		if(empInfo.getTerminationDate()==null) return null;
		// try {
			//return TransStringUtil.getDate(empInfo.getTerminationDate());
			 return empInfo.getTerminationDate();
		//} catch (ParseException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
		//return null;
	}

	public boolean isBullpen() {
		return bullpen;
	}

	public void setBullpen(boolean bullpen) {
		this.bullpen = bullpen;
	}

	public String getRegion() {
		return region;
	}
	public String getRegionS() 
	{
		String result="";
		if(bullpen)result="Bullpen-";
		if(region!=null)result+=region;
		return result;
	}
	public void setRegion(String region) {
		this.region = region;
	}

	public void setTrnStatus(String status) {
		this.trnStatus = status;
	}

	public String getTrnStatus() {
		return this.trnStatus ;
	}
	public String getTrnStatus1()
	{
		String status=empInfo.getStatus();
		if("Inactive".equalsIgnoreCase(status))
		{
			if("true".equalsIgnoreCase(trnStatus)) return "FA";
			if("false".equalsIgnoreCase(trnStatus)) return "I";
			if(trnStatus==null) return "I";
		}
		if("Active".equalsIgnoreCase(status))
		{
			if("true".equalsIgnoreCase(trnStatus)) return "A";
			if("false".equalsIgnoreCase(trnStatus)) return "FI";
			if(trnStatus==null) return "A";
		}
		return null;
	}	
	public void toggleStatus()
	{
		String status=empInfo.getStatus();
		if("Inactive".equalsIgnoreCase(status))
		{
			if("true".equalsIgnoreCase(trnStatus)){ trnStatus=null; return; }			
			if(trnStatus==null){ trnStatus="true" ; return ; }
		}
		if("Active".equalsIgnoreCase(status))
		{
			if("false".equalsIgnoreCase(trnStatus)){ trnStatus=null; return; }			
			if(trnStatus==null) { trnStatus="false" ; return ;}
			
		}
	}
	
	public String toString() {
		return this.getEmpInfo() != null ? this.getEmpInfo().toString() : "";
	}
}
