package com.freshdirect.transadmin.web.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.freshdirect.transadmin.constants.EnumTruckPreference;
import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.model.EmployeeRole;

public class WebEmployeeInfo implements Serializable,
		Comparable<WebEmployeeInfo> {

	private String employeeId;
	private String firstName;
	private String lastName;
	private String shiftType;
	private String region;
	private boolean bullpen;
	private EmployeeInfo empInfo;
	private Collection empRole;
	private String trnStatus;
	private EmployeeInfo leadInfo;
	private boolean isLead;
	private String truckPref01;
	private String truckPref02;
	private String truckPref03;
	private String truckPref04;
	private String truckPref05;
	private Map<String, String> empTruckPreferences = new HashMap<String, String>();
	
	public String getTruckPref01() {
		if(this.empTruckPreferences != null && empTruckPreferences.size() > 0)
			truckPref01 = empTruckPreferences.get(EnumTruckPreference.TRUCK_PREF_01.getName());
		return truckPref01;
	}

	public void setTruckPref01(String truckPref01) {
		if(truckPref01 != null)
			empTruckPreferences.put(EnumTruckPreference.TRUCK_PREF_01.getName(), truckPref01);
	}

	public String getTruckPref02() {
		if(this.empTruckPreferences != null && empTruckPreferences.size() > 0)
			truckPref02 = empTruckPreferences.get(EnumTruckPreference.TRUCK_PREF_02.getName());
		return truckPref02;
	}

	public void setTruckPref02(String truckPref02) {
		if(truckPref02 != null)
			empTruckPreferences.put(EnumTruckPreference.TRUCK_PREF_02.getName(), truckPref02);		
	}

	public String getTruckPref03() {
		if(this.empTruckPreferences != null && empTruckPreferences.size() > 0)
			truckPref03 = empTruckPreferences.get(EnumTruckPreference.TRUCK_PREF_03.getName());
		return truckPref03;
	}

	public void setTruckPref03(String truckPref03) {		
		if(truckPref03 != null)
			empTruckPreferences.put(EnumTruckPreference.TRUCK_PREF_03.getName(), truckPref03);
	}

	public String getTruckPref04() {
		if(this.empTruckPreferences != null && empTruckPreferences.size() > 0)
			truckPref04 = empTruckPreferences.get(EnumTruckPreference.TRUCK_PREF_04.getName());
		return truckPref04;
	}

	public void setTruckPref04(String truckPref04) {
		if(truckPref04 != null)
			empTruckPreferences.put(EnumTruckPreference.TRUCK_PREF_04.getName(), truckPref04);
	}

	public String getTruckPref05() {
		if(this.empTruckPreferences != null && empTruckPreferences.size() > 0)
			truckPref05 = empTruckPreferences.get(EnumTruckPreference.TRUCK_PREF_05.getName());
		return truckPref05;
	}

	public void setTruckPref05(String truckPref05) {
		if(truckPref05 != null)
			empTruckPreferences.put(EnumTruckPreference.TRUCK_PREF_05.getName(), truckPref05);
	}

	public Map<String, String> getEmpTruckPreferences() {
		return empTruckPreferences;
	}

	public void setEmpTruckPreferences(Map<String, String> empTruckPreferences) {
		this.empTruckPreferences = empTruckPreferences;
	}

	public WebEmployeeInfo(EmployeeInfo eInfo, Collection eRole) {
		this.empInfo = eInfo;
		this.empRole = eRole;
	}

	public WebEmployeeInfo(EmployeeInfo eInfo, Collection eRole,
			Map empTruckPreferences) {
		this.empInfo = eInfo;
		this.empRole = eRole;
		this.empTruckPreferences = empTruckPreferences;
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
		if (this.isLead()) {
			return this.getEmpInfo();
		} else {
			return leadInfo;
		}
	}

	public String getLeadId() {
		if (leadInfo != null) {
			return leadInfo.getEmployeeId();
		} else {
			return null;
		}
	}

	public String getShiftType() {
		return shiftType;
	}

	public void setShiftType(String shiftType) {
		this.shiftType = shiftType;
	}

	public void setLeadId() {
		// Dummy Method
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

	public String getEmployeeId() {
		if (empInfo == null)
			return null;
		return empInfo.getEmployeeId();
	}

	public String getEmployeeRoleType() {
		if (empRole == null)
			return null;

		StringBuffer buf = new StringBuffer();

		Iterator iterator = empRole.iterator();
		while (iterator.hasNext()) {
			EmployeeRole role = (EmployeeRole) iterator.next();
			if (buf.length() > 0)
				buf.append("/").append(role.getEmployeeSubRoleType().getName());
			else
				buf.append(role.getEmployeeSubRoleType().getName());
		}

		return buf.toString();
	}

	public Collection getEmployeeRoleTypes() {
		if (empRole == null)
			return null;

		List empList = new ArrayList();
		Iterator iterator = empRole.iterator();
		while (iterator.hasNext()) {
			EmployeeRole role = (EmployeeRole) iterator.next();
			empList.add(role.getEmployeeSubRoleType());
		}

		return empList;
	}

	// added new code Appdev808
	public String getNameWithFirstInitial() {

		StringBuffer buf = new StringBuffer();

		buf.append((getFirstName()).substring(0, 1));

		return buf.toString();
	}

	public String getJobTypeWithFirstInitial() {

		StringBuffer buf = new StringBuffer();

		buf.append((getJobType()).substring(0, 1));

		return buf.toString();
	}

	public void setEmployeeRoleType(Collection employeeRoles) {
		this.empRole = employeeRoles;
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

	public String getFirstLetterJobType() {
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

	public Date getTerminationDate() {
		if (empInfo.getTerminationDate() == null)
			return null;
		return empInfo.getTerminationDate();		
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

	public String getRegionS() {
		String result = "";
		if (bullpen)
			result = "Bullpen-";
		if (region != null)
			result += region;
		return result;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public void setTrnStatus(String status) {
		this.trnStatus = status;
	}

	public String getTrnStatus() {
		return this.trnStatus;
	}

	public String getTrnStatus1() {
		String status = empInfo.getStatus();
		if ("Inactive".equalsIgnoreCase(status)) {
			if ("true".equalsIgnoreCase(trnStatus))
				return "FA";
			if ("false".equalsIgnoreCase(trnStatus))
				return "I";
			if (trnStatus == null)
				return "I";
		}
		if ("Active".equalsIgnoreCase(status)) {
			if ("true".equalsIgnoreCase(trnStatus))
				return "A";
			if ("false".equalsIgnoreCase(trnStatus))
				return "FI";
			if (trnStatus == null)
				return "A";
		}
		return null;
	}

	public void toggleStatus() {
		String status = empInfo.getStatus();
		if ("Inactive".equalsIgnoreCase(status)) {
			if ("true".equalsIgnoreCase(trnStatus)) {
				trnStatus = null;
				return;
			}
			if (trnStatus == null || "false".equalsIgnoreCase(trnStatus)) {
				trnStatus = "true";
				return;
			}
		}
		if ("Active".equalsIgnoreCase(status)) {
			if ("false".equalsIgnoreCase(trnStatus)) {
				trnStatus = null;
				return;
			}
			if (trnStatus == null || "true".equalsIgnoreCase(trnStatus)) {
				trnStatus = "false";
				return;
			}
		}
	}

	public String toString() {
		return this.getEmpInfo() != null ? this.getEmpInfo().toString() : "";
	}

	@Override
	public int compareTo(WebEmployeeInfo o) {
		// TODO Auto-generated method stub
		return this.toString().compareTo(o.toString());
	}

}
