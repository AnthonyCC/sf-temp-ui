package com.freshdirect.transadmin.model;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.freshdirect.transadmin.util.TransStringUtil;

public class ScheduleEmployeeInfo 
{
	public static final String DRIVER="001";
	public static final String HELPER="002";
	public static final String RUNNER="003";
	public static final String DEPOT="Depot";
	public static final String OFF="OFF";
	public static final String NO_SHIFT="No Shift";
	public static final String DAY[]=new String[]{"MON","TUE","WED","THU","FRI","SAT","SUN"};
	public static final Date OFFDATE=new Timestamp(-1);
	private EmployeeInfo empInfo;
	private String role;
	private Collection schedule;
	private Collection  empRole;
	private String trnStatus;
	private int scheduledDays;
	
	private EmployeeInfo leadInfo;
	private boolean isLead;
	
	public EmployeeInfo getLeadInfo() {
		return leadInfo;
	}
	public void setLeadInfo(EmployeeInfo leadInfo) {
		this.leadInfo = leadInfo;
	}
	public boolean isLead() {
		return isLead;
	}
	public void setLead(boolean isLead) {
		this.isLead = isLead;
	}
	public String getEmployeeId()
	{
		return empInfo!=null?empInfo.getEmployeeId():"";
	}
	public String getFirstName()
	{
		return empInfo!=null?empInfo.getFirstName():"";
	}
	public String getLastName()
	{
		return empInfo!=null?empInfo.getLastName():"";
	}
	public Date getHireDate()
	{
		return empInfo!=null?empInfo.getHireDate():null;
	}
	public EmployeeInfo getEmpInfo() {
		return empInfo;
	}
	public void setEmpInfo(EmployeeInfo empInfo) {
		this.empInfo = empInfo;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public Collection getSchedule() {
		return schedule;
	}
	public void setSchedule(Collection schedule) {
		this.schedule = schedule;
	}
	
	public String getRegionTime(String day)
	{
		String result=null;
		if(schedule!=null)
		{
			for(Iterator it=schedule.iterator();it.hasNext();)
			{
				ScheduleEmployee se=(ScheduleEmployee)it.next();
				if(day.equalsIgnoreCase(se.getDay()))
				{
					if(se.getRegion()==null&&se.getTime()==null)
					{
						result=OFF;
					}
					else
					{
						String time="";
						try {
							time=TransStringUtil.getServerTime(se.getTime());
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						result=(se.getDepotZone()!=null?se.getDepotZone().getZoneCode()+"-":"")+se.getRegion().getName()+"-"+time;
						
					}
				}
			}
		}
		if(result==null)result=NO_SHIFT;
		return result;
	}
	
	
	public int getScheduledDays()
	{
		scheduledDays = 0;
		if(schedule!=null)
		{
			for(Iterator it=schedule.iterator();it.hasNext();)
			{
				ScheduleEmployee se=(ScheduleEmployee)it.next();
				if(!(se.getRegion()==null&&se.getTime()==null))
					scheduledDays++;
			}
		}
		return scheduledDays;
	}
	
	public String getMon()
	{
		return getRegionTime(DAY[0]);
	}
	public String getTue()
	{
		return getRegionTime(DAY[1]);
	}
	public String getWed()
	{
		return getRegionTime(DAY[2]);
	}
	public String getThu()
	{
		return getRegionTime(DAY[3]);
	}
	public String getFri()
	{
		return getRegionTime(DAY[4]);
	}
	public String getSat()
	{
		return getRegionTime(DAY[5]);
	}
	public String getSun()
	{
		return getRegionTime(DAY[6]);
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
	public Collection getEmpRole() {
		return empRole;
	}
	public void setEmpRole(Collection empRole) {
		this.empRole = empRole;
	}
	public String getTrnStatus() {
		return trnStatus;
	}
	public void setTrnStatus(String trnStatus) {
		this.trnStatus = trnStatus;
	}
	
	public String getStatus()
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
	
	public boolean isSameSchedule(ScheduleEmployeeInfo inSchedule) {
		if(inSchedule != null) {
			if(this.getMon().equalsIgnoreCase(inSchedule.getMon())
					&& this.getTue().equalsIgnoreCase(inSchedule.getTue())
						&& this.getWed().equalsIgnoreCase(inSchedule.getWed())
							&& this.getThu().equalsIgnoreCase(inSchedule.getThu())
								&& this.getFri().equalsIgnoreCase(inSchedule.getFri())
									&& this.getSat().equalsIgnoreCase(inSchedule.getSat())
										&& this.getSun().equalsIgnoreCase(inSchedule.getSun())) {
				return true;
			}
		}
		return false;
		
	}
	
	public EmployeeInfo getLeadInfoEx() {
		if(this.isLead()) {
			return this.getEmpInfo();
		} else {
			return leadInfo;
		}
	}
}
