package com.freshdirect.transadmin.web.model;

//Generated Dec 5, 2008 2:34:33 PM by Hibernate Tools 3.2.2.GA


import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.transadmin.model.AssetActivity;
import com.freshdirect.transadmin.model.DispatchResource;
import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.model.EmployeeRole;
import com.freshdirect.transadmin.model.EmployeeRoleType;
import com.freshdirect.transadmin.model.EmployeeStatus;
import com.freshdirect.transadmin.model.EmployeeTeam;
import com.freshdirect.transadmin.model.EmployeeTruckPreference;
import com.freshdirect.transadmin.model.PunchInfo;
import com.freshdirect.transadmin.model.PunchInfoI;
import com.freshdirect.transadmin.model.ResourceI;
import com.freshdirect.transadmin.model.ResourceId;
import com.freshdirect.transadmin.model.ResourceInfoI;
import com.freshdirect.transadmin.model.ScheduleEmployeeInfo;
import com.freshdirect.transadmin.model.UPSRouteInfo;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.util.DispatchPlanUtil;
import com.freshdirect.transadmin.util.EnumResourceSubType;
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
	private String physicalTruck;
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
	private boolean keysIn;
	private boolean checkedIn;
    private Date htinDate;
    private Date htoutDate;
    private UPSRouteInfo upsRouteInfo;
    
    private String motKitNumber;
	private String additionalNextels;
	
	private String extras;
	private boolean isActualTruckAssigned;
	private String dispatchTypeModified;
	private String assetStatus;
	private Double muniMeterValueAssigned;
	private Double muniMeterValueReturned;
	private String muniMeterCardNotAssigned;
	private String muniMeterCardNotReturned;
	
	
	
    public Double getMuniMeterValueAssigned() {
		return muniMeterValueAssigned;
	}

	public void setMuniMeterValueAssigned(Double muniMeterValueAssigned) {
		this.muniMeterValueAssigned = muniMeterValueAssigned;
	}

	public Double getMuniMeterValueReturned() {
		return muniMeterValueReturned;
	}

	public void setMuniMeterValueReturned(Double muniMeterValueReturned) {
		this.muniMeterValueReturned = muniMeterValueReturned;
	}

	public String getMuniMeterCardNotAssigned() {
		return muniMeterCardNotAssigned;
	}

	public void setMuniMeterCardNotAssigned(String muniMeterCardNotAssigned) {
		this.muniMeterCardNotAssigned = muniMeterCardNotAssigned;
	}

	public String getMuniMeterCardNotReturned() {
		return muniMeterCardNotReturned;
	}

	public void setMuniMeterCardNotReturned(String muniMeterCardNotReturned) {
		this.muniMeterCardNotReturned = muniMeterCardNotReturned;
	}

	public int getResourceSize(List resources)
	{
		int result=0;
		if(resources!=null)
		for(int i=0,n=resources.size();i<n;i++)
		{
			DispatchResourceInfo e=(DispatchResourceInfo)resources.get(i);
			if(e!=null&&e.getEmployeeId()!=null)result++;
		}
		return result;
	}
    
	public boolean isActualTruckAssigned() {
		return isActualTruckAssigned;
	}

	public void setActualTruckAssigned(boolean isActualTruckAssigned) {
		this.isActualTruckAssigned = isActualTruckAssigned;
	}

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
	
	@SuppressWarnings("unchecked")
	public Set getResources() {
		Set dispatchResources = new HashSet();
		dispatchResources.addAll(getResources(this.getDrivers(),EnumResourceType.DRIVER));
		dispatchResources.addAll(getResources(this.getHelpers(),EnumResourceType.HELPER));
		dispatchResources.addAll(getResources(this.getRunners(),EnumResourceType.RUNNER));
		return dispatchResources;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List getDispatchResources() {
		List dispatchResources = new ArrayList();
		dispatchResources.addAll(this.getDrivers());
		dispatchResources.addAll(this.getHelpers());
		dispatchResources.addAll(this.getRunners());
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setResources(EmployeeManagerI employeeManagerService,Set resources, Map resourceReqs, Collection punchInfos , Map empInfo,Map empRoleMap,
			Map empStatusMap,Map empTruckPrefMap,Map empTeams, Map<String, List<AssetActivity>> scannedAssetMapping) {
		
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
            WebEmployeeInfo webEmpInfo = null;
            
			if (empInfo != null	&& empInfo.containsKey(resource.getId().getResourceId())) {

            	Object obj = empInfo.get(resource.getId().getResourceId());
    			Object roles = (empRoleMap!=null)?empRoleMap.get(resource.getId().getResourceId()):null;
    			Object status = (empStatusMap!=null)?empStatusMap.get(resource.getId().getResourceId()):null;
    			Object truckPref = (empTruckPrefMap!=null)?empTruckPrefMap.get(resource.getId().getResourceId()):null;
    			Object teams = (empTeams!=null)?empTeams.get(resource.getId().getResourceId()):null;
    			
				webEmpInfo = DispatchPlanUtil.buildEmpInfo(
						(obj != null) ? (EmployeeInfo) obj : null,
						(roles != null) ? (List<EmployeeRole>) roles : null,
						(status != null) ? (List<EmployeeStatus>) status : null,
						(truckPref != null) ? (List<EmployeeTruckPreference>) truckPref : null,
						(teams != null) ? (List<EmployeeTeam>) teams : null, employeeManagerService);
            }
            else
            {
            	webEmpInfo = employeeManagerService.getEmployee(resource.getId().getResourceId());
            }

			Collection empRoles = null;
			if(empRoleMap!=null && empRoleMap.containsKey(webEmpInfo.getEmployeeId()))
				empRoles = (List<EmployeeRole>) empRoleMap.get(webEmpInfo.getEmployeeId());
			else
            	empRoles = employeeManagerService.getEmployeeRole(webEmpInfo.getEmployeeId());
            
            ResourceInfoI resourceInfo = getResourceInfo(webEmpInfo, resource);
			if (empRoles != null && empRoles.size() > 0)
			{
				if(EnumResourceSubType.ignorePunch((EnumResourceSubType.getEnum(((EmployeeRole)empRoles.toArray()[0]).getEmployeeSubRoleType().getCode()))))
				{
					resourceInfo.setPunchInfo(new FulltimePunchInfo(this));
				}
				else
				{
		            if(hasPunchInfo) 
		            {
		            	PunchInfoI tempPunchInfo=getPunchInfo(resourceInfo.getEmployeeId(),punchInfos);
		            	Collection _clonedPunchInfos = clonePunchInfo(punchInfos);
		            	if(tempPunchInfo!=null&&!ScheduleEmployeeInfo.DEPOT.equalsIgnoreCase(this.getRegionCode()))
		            	{
		            		_clonedPunchInfos.remove(tempPunchInfo);
		            	}
		            	resourceInfo.setPunchInfo(tempPunchInfo);
		            	if(resourceInfo.getPunchInfo()==null)resourceInfo.setPunchInfo(new PunchInfo());
		            	setStatus(resourceInfo.getPunchInfo());
		            }// added new  code for black hole testing 
		            else {
		            	resourceInfo.setPunchInfo(new FulltimePunchInfo(this));
		            }
				}
			}
            if(resourceReqs.containsKey(role)){
                  ResourceReq req = (ResourceReq) resourceReqs.get(role);
                  if((EnumResourceType.DRIVER.equals(role)||EnumResourceType.MANAGER.equals(role)) && driverCount < req.getMax().intValue()) {
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
            if(scannedAssetMapping != null && scannedAssetMapping.size() > 0) {
            	setResourceAssetScanInfo(resourceInfo, scannedAssetMapping.get(resourceInfo.getEmployeeId()));
            }
        }
	}

	private Collection clonePunchInfo(Collection punchInfos) {
		Collection<PunchInfo> _clonedPunchInfo = new ArrayList<PunchInfo>(); 
		Iterator _itr = punchInfos.iterator();
		while(_itr.hasNext()) {
			PunchInfo _punchInfo = (PunchInfo)_itr.next();
			try {
				_clonedPunchInfo.add((PunchInfo)_punchInfo.clone());
			} catch (CloneNotSupportedException notSupported) {
				// No Supposed to Happen
				notSupported.printStackTrace();
			}
		}
		return _clonedPunchInfo;
	}

	private void setResourceAssetScanInfo(ResourceInfoI resourceInfo,
			List<AssetActivity> resourceScannedAssets) {
		
		List<AssetScanInfo> result = new ArrayList<AssetScanInfo>();
		resourceInfo.setScannedAssets(result);
		if(resourceScannedAssets != null) {
			for(AssetActivity asseyEntry: resourceScannedAssets) {				
				AssetScanInfo _asset = new AssetScanInfo();
				result.add(_asset);
				_asset.setAssetNo(asseyEntry.getAssetNo());
				_asset.setEmployeeId(resourceInfo.getEmployeeId());
				_asset.setStatus(asseyEntry.getStatus());
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
		
		if(punchedEmployees.size()==2)
		{
			PunchInfoI tempInfo1=(PunchInfoI)punchedEmployees.get(0);
			PunchInfoI tempInfo2=(PunchInfoI)punchedEmployees.get(1);
			long tempStartTime1=tempInfo1.getStartTime().getTime()-dispatchTimeLong;
			long tempStartTime2=tempInfo2.getStartTime().getTime()-dispatchTimeLong;
			if(tempStartTime1<0&&tempStartTime2<0)
			{
				tempStartTime1*=-1;
				tempStartTime2*=-1;
			}			
			if(tempStartTime1>tempStartTime2)return tempInfo2;
			else return tempInfo1;
		}
		
		for(int i=0,n=punchedEmployees.size();i<n;i++)
		{
			PunchInfoI tempInfo=(PunchInfoI)punchedEmployees.get(i);
			if(tempInfo.getStartTime()!=null&& tempInfo.getEndTime()!=null&&tempInfo.getStartTime().getTime()<=dispatchTimeLong
			//		&&dispatchTimeLong<tempInfo.getEndTime().getTime()
			)
			{
				return tempInfo;
			}
		}
		
		return null;
		
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

	public void setDispatched(boolean dispatched) {
		this.dispatched = dispatched;
		this.confirmed = dispatched;
		if (this.dispatched) {
			try {
				this.dispatchTime = TransStringUtil.getServerTime(new Date());
			} catch (ParseException e) {

			}
		} else {
			this.dispatchTime = null;
		}
	}

	public boolean isToday() {
		try {
			return TransStringUtil.isToday(dispatchDate);
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
	public boolean isKeysIn() {
		return keysIn;
	}
	public void setKeysIn(boolean keysIn) {
		this.keysIn = keysIn;
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

	public void setCheckedIn(boolean checkedIn) {
		this.checkedIn = checkedIn;
		if (this.checkedIn) {
			try {
				this.checkedInTime = TransStringUtil.getServerTime(new Date());
			} catch (ParseException e) {

			}
		} else {
			this.checkedInTime = null;
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
		String zone = getZoneCode() == null ? "" : "" + getZoneCode();
		if ("true".equalsIgnoreCase(getIsBullpen()))
			zone = "Bullpen";
		if (getRegionName() == null)
			return zone;
		return (getRegionName() == null ? "" : getRegionName()) + "-" + zone;
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
	public String getAssetStatus()
	{
		if(htinDate!=null) return "In";
		if(htoutDate!=null) return "Out";
		return null;
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
	public void setUPSRouteInfo(List<UPSRouteInfo> rInfo)
	{
		Iterator<UPSRouteInfo> iterator = rInfo.iterator();
		while (iterator.hasNext()) {
			UPSRouteInfo upsRouteInfo = iterator.next();
			if (route != null
					&& route.equalsIgnoreCase(upsRouteInfo.getRouteNumber())) {
				this.upsRouteInfo = upsRouteInfo;
				break;
			}
		}
	}
	public UPSRouteInfo getUpsRouteInfo() {
		return upsRouteInfo;
	}
	public void setUpsRouteInfo(UPSRouteInfo upsRouteInfo) {
		this.upsRouteInfo = upsRouteInfo;
	}
	
	public String getRouteLastStopTime()
	{
		try {
			if(upsRouteInfo!=null)
			return TransStringUtil.getServerTime(upsRouteInfo.getLastStop());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String getRouteEndTime()
	{
		try {
			if(upsRouteInfo!=null)
			return TransStringUtil.getServerTime(upsRouteInfo.getEndTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	public String getMotKitNumber() {
		return motKitNumber;
	}
	public String getAdditionalNextels() {
		return additionalNextels;
	}
	public void setMotKitNumber(String motKitNumber) {
		this.motKitNumber = motKitNumber;
	}
	public void setAdditionalNextels(String additionalNextels) {
		this.additionalNextels = additionalNextels;
	}
	public String getExtras() {
		return extras;
	}
	public void setExtras(String extras) {
		this.extras = extras;
	}
	
	public String getPhysicalTruck() {
		return physicalTruck;
	}

	public void setPhysicalTruck(String physicalTruck) {
		this.physicalTruck = physicalTruck;
	}
	public String getDispatchTypeModified() {
		return dispatchTypeModified;
	}

	public void setDispatchTypeModified(String dispatchTypeModified) {
		this.dispatchTypeModified = dispatchTypeModified;
	}

	public void setAssetStatus(String assetStatus) {
		this.assetStatus = assetStatus;
	}
	
}
