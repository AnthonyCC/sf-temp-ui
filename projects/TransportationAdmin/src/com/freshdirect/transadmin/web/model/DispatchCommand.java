package com.freshdirect.transadmin.web.model;

//Generated Dec 5, 2008 2:34:33 PM by Hibernate Tools 3.2.2.GA


import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.transadmin.model.DispatchResource;
import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.model.EmployeeRoleType;
import com.freshdirect.transadmin.model.PunchInfo;
import com.freshdirect.transadmin.model.PunchInfoI;
import com.freshdirect.transadmin.model.ResourceI;
import com.freshdirect.transadmin.model.ResourceId;
import com.freshdirect.transadmin.model.ResourceInfoI;

import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.util.EnumResourceType;
import com.freshdirect.transadmin.util.EnumStatus;
import com.freshdirect.transadmin.util.TransStringUtil;

/**
 * Dispatch generated by hbm2java
 */
public class DispatchCommand extends WebPlanInfo {

	private String dispatchId;
	private String status;
	private String dispatchDate;
	private String route;
	private String truck;
	private boolean confirmed;
	private String comments;
	private String statusName;
	private String noOfStops;
	private String gpsNumber;
    private String ezpassNumber;
    private String location;
    private String dispatchTime;
    private String checkedInTime;
    private EnumStatus dispatchStatus;
	private boolean dispatched;
	private boolean phoneAssigned;
	private boolean keysReady;
	private boolean checkedIn;
    private Date htinDate;
    private Date htoutDate;
    
    
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getDispatchId() {
		return dispatchId;
	}
	public void setDispatchId(String dispatchId) {
		this.dispatchId = dispatchId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDispatchDate() {
		return dispatchDate;
	}
	public void setDispatchDate(String dispatchDate) {
		this.dispatchDate = dispatchDate;
	}
	public String getRoute() {
		return route;
	}
	public void setRoute(String route) {
		this.route = route;
	}
	public String getTruck() {
		return truck;
	}
	public void setTruck(String truck) {
		this.truck = truck;
	}
	public boolean isConfirmed() {
		return confirmed;
	}
	
	public String getConfirmedValue() {
		return String.valueOf(confirmed);
	}
	
	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
		

	public Set getResources() {
		Set dispatchResources=new HashSet();
		dispatchResources.addAll(getResources(this.getDrivers(),EnumResourceType.DRIVER));
		dispatchResources.addAll(getResources(this.getHelpers(),EnumResourceType.HELPER));
		dispatchResources.addAll(getResources(this.getRunners(),EnumResourceType.RUNNER));
		return dispatchResources;
	}
	
	public ResourceInfoI constructResourceInfo(){
		return new DispatchResourceInfo();
	}

	public ResourceInfoI getResourceInfo(WebEmployeeInfo webEmpInfo, ResourceI resource){
		ResourceInfoI resourceInfo=new DispatchResourceInfo();
		if(webEmpInfo!=null && webEmpInfo.getEmpInfo()!=null ) {
        	EmployeeInfo empInfo= webEmpInfo.getEmpInfo();
        	resourceInfo.setEmployeeId(empInfo.getEmployeeId());
        	resourceInfo.setFirstName(empInfo.getFirstName());
        	resourceInfo.setLastName(empInfo.getLastName());
        	if(resource != null)
        		resourceInfo.setNextelNo(resource.getNextTelNo());
        }
		return resourceInfo;
		
	}
	
	protected ResourceI getResource(ResourceInfoI resourceInfo, EnumResourceType role) {
		
		if(TransStringUtil.isEmpty(resourceInfo.getEmployeeId())||TransStringUtil.isEmpty(role.getName()))
			return null;
		else {
			ResourceI resource= new DispatchResource();
			EmployeeRoleType empRole=new EmployeeRoleType();
			empRole.setCode(role.getName());
			resource.setId(new ResourceId(this.dispatchId,resourceInfo.getEmployeeId()));
			resource.setEmployeeRoleType(empRole);
			resource.setNextTelNo(resourceInfo.getNextelNo());
			return resource;
		}
	}
	public String getNoOfStops() {
		return noOfStops;
	}
	public void setNoOfStops(String noOfStops) {
		this.noOfStops = noOfStops;
	}
	
	public void setResources(EmployeeManagerI employeeManagerService,Set resources, Map resourceReqs, Collection punchInfos) {
		
		boolean hasPunchInfo=(punchInfos==null)?false:punchInfos.isEmpty()?false:true;
		if(resources == null || resources.isEmpty())
			return;
		Iterator _it=resources.iterator();
		int driverCount=0;
        int helperCount=0;
        int runnerCount=0;
        while(_it.hasNext()) {
        	
        	
            ResourceI resource=(ResourceI)_it.next();          
            EnumResourceType role=EnumResourceType.getEnum(resource.getEmployeeRoleType().getCode());   
            WebEmployeeInfo webEmpInfo=employeeManagerService.getEmployee(resource.getId().getResourceId());
            ResourceInfoI resourceInfo = getResourceInfo(webEmpInfo, resource);
            if(hasPunchInfo) 
            {
            	resourceInfo.setPunchInfo(getPunchInfo(resourceInfo.getEmployeeId(),punchInfos));
            	if(resourceInfo.getPunchInfo()==null)resourceInfo.setPunchInfo(new PunchInfo());
            	setStatus(resourceInfo.getPunchInfo());
            }
            if(resourceReqs.containsKey(role)){
                  ResourceReq req = (ResourceReq) resourceReqs.get(role);
                  if(EnumResourceType.DRIVER.equals(role) && driverCount < req.getMax().intValue()) {
                      this.getDrivers().remove(driverCount);
                      this.getDrivers().add(driverCount, resourceInfo);
                      driverCount++;

                  } else if (EnumResourceType.HELPER.equals(role) && helperCount < req.getMax().intValue()) {
                	  this.getHelpers().remove(helperCount);
                	  this.getHelpers().add(helperCount, resourceInfo);
                      helperCount++;
                  } else if (EnumResourceType.RUNNER.equals(role) && runnerCount < req.getMax().intValue()) {
                	  this.getRunners().remove(runnerCount);
                	  this.getRunners().add(runnerCount, resourceInfo);
                      runnerCount++;
                  }
                  
              }
        }
	}
	
	private void setStatus(PunchInfoI punchInfo)
	{
		try 
		{	
			if(getDispatchDate()!=null && getStartTime()!=null) {
				Date startTime=TransStringUtil.getDatewithTime(getDispatchDate()+" "+getStartTime());
				long startTimeLong=startTime.getTime();
				long currentTime=System.currentTimeMillis();	
				
				if(currentTime>startTimeLong&&punchInfo.getInPunchDTM()==null)
				{
					PunchInfo p=(PunchInfo)punchInfo;
					p.setLate(true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private PunchInfoI getPunchInfo(String employeeId, Collection punchInfos) {
		
		if ( TransStringUtil.isEmpty(employeeId)||punchInfos==null)
			return null;
		List punchedEmployees=new ArrayList();
		
		PunchInfoI punchInfo=null;
		Iterator it=punchInfos.iterator();
		while(it.hasNext()) 
		{
			punchInfo=(PunchInfoI)it.next();
			if(employeeId.equals(punchInfo.getEmployeeId()))
			{
				punchedEmployees.add(punchInfo);
			}
				
		}		
		if(punchedEmployees.size()==0)return null;
		if(punchedEmployees.size()==1) return (PunchInfoI)punchedEmployees.get(0);		
		long dispatchTimeLong=-1;
		try {
			Date startTime=TransStringUtil.getDatewithTime(getDispatchDate()+" "+getStartTime());
			dispatchTimeLong=startTime.getTime();
		} catch (Exception e) {	}
		
		for(int i=0,n=punchedEmployees.size();i<n;i++)
		{
			PunchInfoI tempInfo=(PunchInfoI)punchedEmployees.get(i);
			if(tempInfo.getStartTime()!=null&&tempInfo.getStartTime().getTime()<=dispatchTimeLong&&dispatchTimeLong<tempInfo.getEndTime().getTime())
			{
				return tempInfo;
			}
		}
		
		return punchInfo;
		
	}

	public String getEzpassNumber() {
		return ezpassNumber;
	}
	public void setEzpassNumber(String ezpassNumber) {
		this.ezpassNumber = ezpassNumber;
	}
	public String getGpsNumber() {
		return gpsNumber;
	}
	public void setGpsNumber(String gpsNumber) {
		this.gpsNumber = gpsNumber;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDispatchTime() {
		return dispatchTime;
	}
	public void setDispatchTime(String dispatchTime) 
	{
		this.dispatchTime = dispatchTime;
		if(dispatchTime!=null)this.dispatched =true;
		
	}
	public EnumStatus getDispatchStatus() {
		return dispatchStatus;
	}
	public void setDispatchStatus(EnumStatus dispatchStatus) {
		this.dispatchStatus = dispatchStatus;
	}
	public boolean isDispatched() {
		return dispatched;
	}
	public void setDispatched(boolean dispatched) 
	{
		this.dispatched = dispatched;
		this.confirmed=dispatched;
		if(this.dispatched)
		{			
			try {
				this.dispatchTime=TransStringUtil.getServerTime(new Date());
			} catch (ParseException e) {
				
			}
		}else
		{
			this.dispatchTime=null;
		}
	}
	public boolean isToday() 
	{
		try 
		{
			return TransStringUtil.isToday(dispatchDate);			
		} catch (ParseException e) {
			
		}
		return false;
	}
	
	public boolean isTodayTomorrow() 
	{
		try 
		{
			return TransStringUtil.isTodayTomorrow(dispatchDate);			
		} catch (ParseException e) {
			
		}
		return false;
	}
	
	public boolean isKeysReady() {
		return keysReady;
	}
	public void setKeysReady(boolean keysReady) {
		this.keysReady = keysReady;
	}
	public boolean isPhoneAssigned() {
		return phoneAssigned;
	}
	public void setPhoneAssigned(boolean phoneAssigned) {
		this.phoneAssigned = phoneAssigned;
	}
	public boolean isCheckedIn() {
		return checkedIn;
	}
	public void setCheckedIn(boolean checkedIn) 
	{
		this.checkedIn = checkedIn;
		if(this.checkedIn)
		{
			try {
				this.checkedInTime=TransStringUtil.getServerTime(new Date());
			} catch (ParseException e) {
				
			}
		}
		else
		{
			this.checkedInTime=null;
		}
	}
	public String getCheckedInTime() {
		return checkedInTime;
	}
	public void setCheckedInTime(String checkedInTime) 
	{
		this.checkedInTime = checkedInTime;
		if(checkedInTime!=null)this.checkedIn =true;
	}
	
	public String getRegionZone()
	{
		String zone=getZoneCode()==null?"":""+getZoneCode();
		if("true".equalsIgnoreCase(getIsBullpen())) zone="Bullpen";		
		if(getRegionName()==null) return zone;
		return (getRegionName()==null?"":getRegionName())+"-"+zone;
	}
	public String getExtras()
	{
		if(getGpsNumber()==null&&getEzpassNumber()==null) return "";
		if(getGpsNumber()==null) return getEzpassNumber();
		return (getGpsNumber()==null?"":getGpsNumber())+(getEzpassNumber()==null?",":","+getEzpassNumber());
	}
	public Date getHtinDate() {
		return htinDate;
	}
	public void setHtinDate(Date htinDate) {
		this.htinDate = htinDate;
	}
	public Date getHtoutDate() {
		return htoutDate;
	}
	public void setHtoutDate(Date htoutDate) {
		this.htoutDate = htoutDate;
	}
	
	public Date getDispatchTimeEx()
	{
		try {
			if(EnumStatus.Dispatched.compareTo(status)>=0)
			{
			if(dispatchTime==null&&htoutDate==null) return null;
			if(dispatchTime==null) return htoutDate;
			if(htoutDate==null) return TransStringUtil.getServerTime(dispatchTime);;
			Date dispatchDate=TransStringUtil.getServerTime(dispatchTime);
			Date htOutDateTemp=TransStringUtil.getServerTime(TransStringUtil.getServerTime(htoutDate));
			if(dispatchDate.getTime()<htOutDateTemp.getTime()) return htOutDateTemp;
			return dispatchDate;
			}
			else if(dispatchTime!=null) {
				return TransStringUtil.getServerTime(dispatchTime);
			}
		} catch (Exception e) 
		{
			
		}
		return null;
		
	}
	
}
