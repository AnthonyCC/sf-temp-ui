package com.freshdirect.transadmin.web.model;


import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.model.EmployeeRoleType;
import com.freshdirect.transadmin.model.PlanResource;
import com.freshdirect.transadmin.model.ResourceI;
import com.freshdirect.transadmin.model.ResourceId;
import com.freshdirect.transadmin.model.ResourceInfoI;
import com.freshdirect.transadmin.model.TrnBaseEntityI;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.util.EnumResourceType;
import com.freshdirect.transadmin.util.TransStringUtil;

public class WebPlanInfo extends BaseCommand implements TrnBaseEntityI  {
	
	private String planId;
	private String planDay;
	private String zoneCode;
	private String zoneName;
	private String regionCode;
	private String regionName;	
	private Date planDate;
	private String firstDeliveryTime;
	private String startTime;
	private int sequence;
	private String isBullpen;
	private String ignoreErrors;
	private String zoneModified;
	private Date errorDate;
	private String  supervisorCode;
    private String maxTime;
    
	private String supervisorName;
	private String zoneType;
	/*private ResourceList drivers= new ResourceList();
	private ResourceList helpers=new ResourceList();		      
	private ResourceList runners=new ResourceList();*/
		      
	private List drivers= new ResourceList();
	private List helpers=new ResourceList();		      
	private List runners=new ResourceList();
	
	private int driverReq;
	private int driverMax;
	
	private int helperReq;
	private int helperMax;

	private int runnerReq;
	private int runnerMax;

	private List termintedEmployees = null;
	
	private boolean isOverride;
	private String overrideReasonCode;
	private String overrideUser;
	
	private String referenceContextId;
	private boolean isTeamOverride;
	
	
	public boolean getIsTeamOverride() {
		return isTeamOverride;
	}
	public void setIsTeamOverride(boolean isTeamOverride) {
		this.isTeamOverride = isTeamOverride;
	}
	public String getOpen()
	{		
		if(getResourceSize(drivers)<driverReq||getResourceSize(helpers)<helperReq||getResourceSize(runners)<runnerReq)
		{
			return "Y";
		}
		return null;
	}
	public String getOverride()
	{		
		if(getIsOverride())
		{
			return "Y";
		}
		return null;
	}
	
	public int getResourceSize(List resources)
	{
		int result=0;
			if(resources!=null)
		for(int i=0,n=resources.size();i<n;i++)
		{
			EmployeeInfo e=(EmployeeInfo)resources.get(i);
			if(e!=null&&e.getEmployeeId()!=null&&e.getEmployeeId().length()>0)result++;
		}
		return result;
	}
	
	public List getTermintedEmployees() {
		return termintedEmployees;
	}

	public void setTermintedEmployees(List termintedEmployees) {
		this.termintedEmployees = termintedEmployees;
	}

	public Tooltip getSupervisorEx() {
		String value=new StringBuffer(100).append(supervisorName).append("\n").append("ID : ").append(supervisorCode).toString();
		return new Tooltip(this.getSupervisorName(), value);
	}
	
	public Tooltip getZoneNameEx() {
		if(!TransStringUtil.isEmpty(zoneName)) {
			String value=new StringBuffer(100).append(zoneName).append("\n").append(zoneType).toString();
			return new Tooltip(this.getZoneCode(), value);
		}
		return new Tooltip("Bullpen","");

	}
	
		
   /* public static class MyList {
    	
    	public static ResourceList decorate(ResourceList list,org.apache.commons.collections.Factory factory) {
    		
    		ResourceList responseList=null;
    		List  response=LazyList.decorate(list,factory);
    		responseList=new ResourceList((Collection)response);
    		
    		responseList.setResourceReq(list.getResourceReq());
    		return responseList;
    	}
    	
    }*/
	
	public int getDriverMax() {
		
		/*if(drivers!=null && drivers.getResourceReq()!=null )
			return drivers.getResourceReq().getMax().intValue();
		return 0;*/
		return driverMax;
	}
	
	
	
	public int getDriverReq() {
		/*if(drivers!=null && drivers.getResourceReq()!=null )
			return drivers.getResourceReq().getReq().intValue();
		return 0;*/
		
		return driverReq;

	}
	
	
	
	public int getHelperMax() {
		
		/*if(helpers!=null && helpers.getResourceReq()!=null )
			return helpers.getResourceReq().getMax().intValue();
		return 0;*/
		return helperMax;
	}
	
	
	
	public int getHelperReq() {
		/*if(helpers!=null && helpers.getResourceReq()!=null )
			return helpers.getResourceReq().getReq().intValue();
		return 0;*/
		return helperReq;
	}
	
	
	
	public int getRunnerMax() {
		
		/*if(runners!=null && runners.getResourceReq()!=null )
			return runners.getResourceReq().getMax().intValue();
		return 0;*/
		return runnerMax;
	}
	
	
	
	public int getRunnerReq() {
		/*if(runners!=null && runners.getResourceReq()!=null )
			return runners.getResourceReq().getReq().intValue();
		return 0;*/
		return runnerReq;

	}
	
	
	/**
	 * @return the errorDate
	 */
	public Date getErrorDate() {
		return errorDate;
	}
	/**
	 * @param errorDate the errorDate to set
	 */
	public void setErrorDate(Date errorDate) {
		this.errorDate = errorDate;
	}
	/**
	 * @return the ignoreErrors
	 */
	public String getIgnoreErrors() {
		return ignoreErrors;
	}
	/**
	 * @param ignoreErrors the ignoreErrors to set
	 */
	public void setIgnoreErrors(String ignoreErrors) {
		this.ignoreErrors = ignoreErrors;
	}
	/**
	 * @return the isBullpen
	 */
	public String getIsBullpen() {
		return isBullpen==null?"N":isBullpen;
	}
	/**
	 * @param isBullpen the isBullpen to set
	 */
	public void setIsBullpen(String isBullpen) {
		this.isBullpen = isBullpen;
	}
	/**
	 * @return the planDate
	 */
	public Date getPlanDate() {
		return planDate;
	}
	/**
	 * @param planDate the planDate to set
	 */
	public void setPlanDate(Date planDate) {
		this.planDate = planDate;
	}
	/**
	 * @return the planDay
	 */
	public String getPlanDay() {
		return planDay;
	}
	/**
	 * @param planDay the planDay to set
	 */
	public void setPlanDay(String planDay) {
		this.planDay = planDay;
	}
	/**
	 * @return the planId
	 */
	public String getPlanId() {
		return planId;
	}
	/**
	 * @param planId the planId to set
	 */
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	/**
	 * @return the regionCode
	 */
	public String getRegionCode() {
		return regionCode;
	}
	/**
	 * @param regionCode the regionCode to set
	 */
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}
	/**
	 * @return the regionName
	 */
	public String getRegionName() {
		return regionName;
	}
	/**
	 * @param regionName the regionName to set
	 */
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	/**
	 * @return the reportTime
	 */
	public String getFirstDeliveryTime() {
		return firstDeliveryTime;
	}
	/**
	 * @param reportTime the reportTime to set
	 */
	public void setFirstDeliveryTime(String firstDeliveryTime) {
		this.firstDeliveryTime = firstDeliveryTime;
	}
	/**
	 * @return the sequence
	 */
	public int getSequence() {
		return sequence;
	}
	/**
	 * @param sequence the sequence to set
	 */
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		return startTime;
	}
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	/**
	 * @return the zoneCode
	 */
	public String getZoneCode() {
		return zoneCode;
	}
	/**
	 * @param zoneCode the zoneCode to set
	 */
	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}
	/**
	 * @return the zoneName
	 */
	public String getZoneName() {
		return zoneName;
	}
	/**
	 * @param zoneName the zoneName to set
	 */
	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}
	
	
	public void setDrivers(ResourceList drivers) {
		this.drivers =drivers;
	}
	
	public void setHelpers(ResourceList helpers) {
		this.helpers =helpers;
		
	}
	
	public void setRunners(ResourceList runners) {
		this.runners =runners;
		
	}
	/**
	 * @param supervisor the supervisor to set
	 */
	public void setSupervisorCode(String supervisor) {
		this.supervisorCode = supervisor;
	}
	public String getSupervisorCode() {
		return supervisorCode;
	}
	public void setSupervisorName(String name) {
		this.supervisorName=name;
	}
	public String getSupervisorName() {
		return supervisorName;
	}
	public void addDriver(ResourceInfoI driver) {
		if(driver!=null)
			drivers.add(driver);
	}

	public void addHelper(ResourceInfoI helper) {
		if(helper!=null)
			helpers.add(helper);
	}
	
	public void addRunner(ResourceInfoI runner) {
		if(runner!=null)
			runners.add(runner);
	}	
	
	public List getDrivers() {
		return drivers;
	}
	
	public List getRunners() {
		return runners;
	}
	
	public List getHelpers() {
		return helpers;
	}
	
	public ResourceList getDummyResources(ResourceReq resourceReq) {
		
		int size=resourceReq.getMax().intValue();
		ResourceList resourceList=new ResourceList(size);
		//resourceList.setResourceReq(resourceReq);
		for(int i=0;i<size;i++) {
			resourceList.add(constructResourceInfo());
		}
		return resourceList;

	}
	
	public Set getResources() {
		Set planResources=new HashSet();
		ResourceInfoI supervisorInfo = constructResourceInfo();
		supervisorInfo.setEmployeeId(this.supervisorCode);
		planResources.addAll(getResources(this.getDrivers(),EnumResourceType.DRIVER));
		planResources.addAll(getResources(this.getHelpers(),EnumResourceType.HELPER));
		planResources.addAll(getResources(this.getRunners(),EnumResourceType.RUNNER));
		return planResources;
	}
	
	protected Set getResources(List resources, EnumResourceType role) {
		Set planResources=new HashSet();
		if(resources==null || resources.size()==0)
			return planResources;
		for(int i=0;i<resources.size();i++) {
			ResourceInfoI resourceInfo=(ResourceInfoI)resources.get(i);
			ResourceI resource=getResource(resourceInfo,role);
			if(resource!=null)
				planResources.add(resource);
		}
		return planResources;
	}
	
	protected ResourceI getResource(ResourceInfoI resourceInfo, EnumResourceType role) {
		
		
		if(TransStringUtil.isEmpty(resourceInfo.getEmployeeId())) {
			return null;
		}
		ResourceI resource= new PlanResource();
		EmployeeRoleType empRole=new EmployeeRoleType();
		empRole.setCode(role.getName());
		ResourceId r=new ResourceId(this.planId,resourceInfo.getEmployeeId());
		r.setAdjustmentTime(resourceInfo.getAdjustmentTime());
		resource.setId(r);
		resource.setEmployeeRoleType(empRole);
		return resource;
		
	}
	
	public void setResourceRequirements(Map resourceReqs) {
		Iterator it=resourceReqs.keySet().iterator();
		while(it.hasNext()) {
			Object key=it.next();
			ResourceReq resourceReq=(ResourceReq)resourceReqs.get(key);
			if(EnumResourceType.DRIVER.equals(key)) {
				this.setDrivers(getDummyResources(resourceReq));
			}else if(EnumResourceType.HELPER.equals(key)) {
				this.setHelpers(getDummyResources(resourceReq));
			}else if(EnumResourceType.RUNNER.equals(key)) {
				this.setRunners(getDummyResources(resourceReq));
			}
		}
	}
	
	public void setResources(EmployeeManagerI employeeManagerService,Set resources, Map resourceReqs) {
		
		
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
            resourceInfo.setAdjustmentTime(resource.getId().getAdjustmentTime());
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
	
	

	public void setResourceInfo(List resources, boolean isZoneModified, EnumResourceType role,EmployeeManagerI employeeManagerService, int max) {
		
		
		//int max=resources.getResourceReq().getMax().intValue();
		if(max==0) {
			resources.clear();
		}else if (resources.size()>max) {
			trimResources(resources,max);
		}else if (resources.size()<max) {
			int difference=max-resources.size();
			for(int i=0;i<difference;i++) {
				resources.add(constructResourceInfo());
			}
		}
		if(isZoneModified) {
			return;
		}
		int size=resources.size();
		for(int i=0;i<size;i++) {
			ResourceInfoI resourceInfo=(ResourceInfoI)resources.get(i);
			if(!TransStringUtil.isEmpty(resourceInfo.getEmployeeId())) {
				WebEmployeeInfo webEmplInfo=employeeManagerService.getEmployee(resourceInfo.getEmployeeId());
				webEmplInfo.getEmpInfo().setAdjustmentTime(resourceInfo.getAdjustmentTime());
				String nexttel=resourceInfo.getNextelNo();
				if(webEmplInfo!=null && webEmplInfo.getEmpInfo()!=null) {
					resources.remove(i);
					ResourceInfoI resourceInfoI=getResourceInfo(webEmplInfo, null);	
					resourceInfoI.setAdjustmentTime(resourceInfo.getAdjustmentTime());
					resourceInfoI.setNextelNo(nexttel);
					resources.add(i,resourceInfoI );
				}
			}
		}
	}
	
	private static void trimResources(List resources, int max) {
		
		if(resources==null || resources.size()==0)
			return;
		int trimCount=resources.size()-max;
		for(int i=0;i<trimCount;i++) {
			resources.remove(resources.size()-1);
		}
	}
	
	public ResourceInfoI constructResourceInfo(){
		return new EmployeeInfo();
	}
	
	public ResourceInfoI getResourceInfo(WebEmployeeInfo webEmpInfo, ResourceI resource){
		if(webEmpInfo!=null && webEmpInfo.getEmpInfo()!=null ) {
			EmployeeInfo empInfo= new EmployeeInfo();
			empInfo.setEmployeeId(webEmpInfo.getEmpInfo().getEmployeeId());
			empInfo.setFirstName(webEmpInfo.getEmpInfo().getFirstName());
			empInfo.setLastName(webEmpInfo.getEmpInfo().getLastName());
			if(resource!=null&&resource.getId()!=null)
			empInfo.setAdjustmentTime(resource.getId().getAdjustmentTime());
			return empInfo;
			
		}else {
			return new EmployeeInfo();
		}
	}

	



	public String getZoneModified() {
		return zoneModified;
	}



	public void setZoneModified(String zoneModified) {
		this.zoneModified = zoneModified;
	}

	public String getZoneType() {
		return zoneType;
	}

	public void setZoneType(String zoneType) {
		this.zoneType = zoneType;
	}
	
	public boolean  isObsoleteEntity(){
		return hasTerminatedResources(this.getDrivers(), termintedEmployees) 
					|| hasTerminatedResources(this.getRunners(), termintedEmployees) 
						|| hasTerminatedResources(this.getHelpers(), termintedEmployees);
	}
	
	public boolean hasTerminatedResources(List resources, List termintedEmployees) {
		boolean result = false;
		if(resources != null && termintedEmployees != null) {
			for(int i=0;i<resources.size();i++) {
				ResourceInfoI resourceInfo=(ResourceInfoI)resources.get(i);
				if(termintedEmployees.contains(resourceInfo.getEmployeeId()) ) {
					result = true;
					break;
				}
				
			}
		}
		return result;
	}
	
   public Date getStartTimeEx() {
		
		try {
			return TransStringUtil.getServerTime(getStartTime());
		} catch (ParseException e) {
			return null;
		}
		
	}
		
	public Date getFirstDeliveryTimeEx() {
		try {
			return TransStringUtil.getServerTime(getFirstDeliveryTime());
		} catch (ParseException e) {
			return null;
		}
	}

	public void setDriverMax(int driverMax) {
		this.driverMax = driverMax;
	}

	public void setDriverReq(int driverReq) {
		this.driverReq = driverReq;
	}

	public void setHelperMax(int helperMax) {
		this.helperMax = helperMax;
	}

	public void setHelperReq(int helperReq) {
		this.helperReq = helperReq;
	}

	public void setRunnerMax(int runnerMax) {
		this.runnerMax = runnerMax;
	}

	public void setRunnerReq(int runnerReq) {
		this.runnerReq = runnerReq;
	}

	public String getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(String maxTime) {
		this.maxTime = maxTime;
	}

	public boolean getIsOverride() {
		return isOverride;
	}

	public void setIsOverride(boolean isOverride) {
		this.isOverride = isOverride;
	}

	public String getOverrideReasonCode() {
		return overrideReasonCode;
	}

	public void setOverrideReasonCode(String overrideReasonCode) {
		this.overrideReasonCode = overrideReasonCode;
	}

	public String getOverrideUser() {
		return overrideUser;
	}

	public void setOverrideUser(String overrideUser) {
		this.overrideUser = overrideUser;
	}
	public String getReferenceContextId() {
		return referenceContextId;
	}
	public void setReferenceContextId(String referenceContextId) {
		this.referenceContextId = referenceContextId;
	}
	
	
}
