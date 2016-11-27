package com.freshdirect.transadmin.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.routing.constants.EnumTransportationFacilitySrc;
import com.freshdirect.transadmin.constants.EnumDispatchType;
import com.freshdirect.transadmin.model.Asset;
import com.freshdirect.transadmin.model.AssetActivity;
import com.freshdirect.transadmin.model.Dispatch;
import com.freshdirect.transadmin.model.DispatchReason;
import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.model.EmployeeRole;
import com.freshdirect.transadmin.model.EmployeeStatus;
import com.freshdirect.transadmin.model.EmployeeTeam;
import com.freshdirect.transadmin.model.EmployeeTruckPreference;
import com.freshdirect.transadmin.model.IWaveInstanceSource;
import com.freshdirect.transadmin.model.Plan;
import com.freshdirect.transadmin.model.Region;
import com.freshdirect.transadmin.model.UPSRouteInfo;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.model.ZoneSupervisor;
import com.freshdirect.transadmin.model.ZonetypeResource;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.service.ZoneManagerI;
import com.freshdirect.transadmin.web.model.DispatchCommand;
import com.freshdirect.transadmin.web.model.DispatchResourceInfo;
import com.freshdirect.transadmin.web.model.ResourceReq;
import com.freshdirect.transadmin.web.model.WebEmployeeInfo;
import com.freshdirect.transadmin.web.model.WebPlanInfo;

public class DispatchPlanUtil {

	private static EmployeeComparator EMPLOYEE_COMPARATOR = new EmployeeComparator();
	
	public static final String ASSETTYPE_GPS = "GPS";
	public static final String ASSETTYPE_EZPASS = "EZPASS";
	public static final String ASSETTYPE_MOTKIT = "MOTKIT";
	public static final String ASSETTYPE_TRUCK = "TRUCK";
	public static final String ASSETTYPE_TRAILER = "TRAILER";
	public static final String SCANNED_ASSETS = "SCANNEDASSETS";
	
	private static class EmployeeComparator implements Comparator {

		public int compare(Object o1, Object o2) {

			if (o1 instanceof EmployeeInfo && o2 instanceof EmployeeInfo) {
				EmployeeInfo p1 = (EmployeeInfo) o1;
				EmployeeInfo p2 = (EmployeeInfo) o2;
				return p1.getLastName().compareTo(p2.getLastName());
			}
			return 0;
		}
	}

	public static WebPlanInfo getWebPlanInfo(Plan plan, Zone zone,EmployeeManagerI employeeManagerService, boolean isPlan, Map empInfo, Map empRoleMap,Map empStatusMap,Map empTruckPrefMap,Map empTeams) {

		WebPlanInfo planInfo=new WebPlanInfo();
		planInfo.setPlanId(plan.getPlanId());
		planInfo.setPlan(plan.getZone()!=null && isPlan && plan.getZone().getArea()!=null && "X".equals(plan.getZone().getArea().getIsDepot()));
		
		planInfo.setPlanDate(plan.getPlanDate());
		if(plan.getZone()!=null) {
			planInfo.setZoneCode(plan.getZone().getZoneCode());
			planInfo.setZoneName(plan.getZone().getName());
			if(plan.getZone().getTrnZoneType()!=null)
				planInfo.setZoneType(plan.getZone().getTrnZoneType().getName());
			planInfo.setEquipmentTypes(plan.getEquipmentTypes());
			planInfo.setEquipmentTypeS(plan.getEquipmentTypeS());
		}
		planInfo.setRegionCode(plan.getRegion().getCode());
		planInfo.setRegionName(plan.getRegion().getName());
		try {
			planInfo.setDispatchGroup(plan.getDispatchGroup());
			planInfo.setStartTime(TransStringUtil.getServerTime(plan.getStartTime()));
			planInfo.setEndTime(TransStringUtil.getServerTime(plan.getEndTime()));
			planInfo.setMaxTime(plan.getMaxReturnTime() != null ? TransStringUtil.getServerTime(plan.getMaxReturnTime()) : null);
			planInfo.setCutOffTime(plan.getCutOffTime() != null ? TransStringUtil.getServerTime(plan.getCutOffTime()) : null);
		} catch (ParseException exp) {
			throw new RuntimeException("Unparseable date "+exp.getMessage());
		}
		planInfo.setIsBullpen(plan.getIsBullpen());
		planInfo.setSequence(plan.getSequence());
		planInfo.setSupervisorCode(plan.getSupervisorId());
		planInfo.setOriginFacility(plan.getOriginFacility());
		planInfo.setDestinationFacility(plan.getDestinationFacility());
		
		WebEmployeeInfo webEmp = null;
		if (empInfo != null && empInfo.containsKey(plan.getSupervisorId()))
		{
			Object obj = empInfo.get(plan.getSupervisorId());
			Object roles = (empRoleMap!=null)?empRoleMap.get(plan.getSupervisorId()):null;
			Object status = (empStatusMap!=null)?empStatusMap.get(plan.getSupervisorId()):null;
			Object truckPref = (empTruckPrefMap!=null)?empTruckPrefMap.get(plan.getSupervisorId()):null;
			Object teams = (empTeams!=null)?empTeams.get(plan.getSupervisorId()):null;
			
			webEmp = DispatchPlanUtil.buildEmpInfo((obj!=null)?(EmployeeInfo)obj:null,(roles!=null)?(List<EmployeeRole>)roles:null, 
					(status!=null)?(List<EmployeeStatus>)status:null, (truckPref!=null)?(List<EmployeeTruckPreference>)truckPref:null, 
							(teams!=null)?(List<EmployeeTeam>)teams:null, employeeManagerService);	
		}
		else
		    webEmp = employeeManagerService.getEmployee(plan.getSupervisorId());

		if(webEmp!=null && webEmp.getEmpInfo()!=null) {
			planInfo.setSupervisorName(webEmp.getEmpInfo().getName());
		}
		
		String val=DateUtil.formatDay(plan.getPlanDate());
		planInfo.setPlanDay(val);

		Map resourceReqs=getResourceRequirements(zone);
		Set resources=plan.getPlanResources();
		planInfo.setResourceRequirements(resourceReqs);
		planInfo.setResources(employeeManagerService,resources,resourceReqs, empInfo, empRoleMap, empStatusMap,
				empTruckPrefMap, empTeams);
		setResourceReq(planInfo,plan.getZone());
		
		planInfo.setIsTeamOverride(plan.getIsTeamOverride() != null &&  plan.getIsTeamOverride() ? true : false);
		return planInfo;
	}

	@SuppressWarnings("unchecked")
	public static DispatchCommand getDispatchCommand(Dispatch dispatch, Zone zone,EmployeeManagerI employeeManagerService, Collection punchInfos,
			Map htInData,Map htOutData, Map empInfo, Map empRoleMap,Map empStatusMap,Map empTruckPrefMap,Map empTeams, Map<String, List<AssetActivity>> scannedAssetMapping) {
		DispatchCommand command = new DispatchCommand();
		command.setDispatchId(dispatch.getDispatchId());
		try{
			command.setDispatchDate(TransStringUtil.getDate(dispatch.getDispatchDate()));
		}catch(ParseException ex){
			throw new IllegalArgumentException("Unparseable date "+ex.getMessage());
		}
		String zoneCode = "";
		String zoneName = "";
		String zoneType= "";
		if(dispatch.getZone() != null) {
			zoneCode = dispatch.getZone().getZoneCode();
			zoneName = dispatch.getZone().getName();
			if(dispatch.getZone().getTrnZoneType()!=null)
				zoneType = dispatch.getZone().getTrnZoneType().getName();

		}
		command.setZoneCode(zoneCode);
		command.setZoneName(zoneName);
		command.setZoneType(zoneType);
		command.setRegionCode(dispatch.getRegion().getCode());
		command.setRegionName(dispatch.getRegion().getName());
		command.setOriginFacility(dispatch.getOriginFacility());
		command.setDestinationFacility(dispatch.getDestinationFacility());
		if(dispatch.getBullPen()!=null)
			command.setIsBullpen(String.valueOf(dispatch.getBullPen().booleanValue()));
		else
			command.setIsBullpen("");
		if(dispatch.getDispositionType() != null){
			command.setStatus(dispatch.getDispositionType().getCode());
			command.setStatusName(dispatch.getDispositionType().getName());
		}
		//command.setStatus(dispatch.getDispositionType().getCode());
		WebEmployeeInfo supInfo = null;
		if(empInfo!=null && empInfo.containsKey(dispatch.getSupervisorId()))
		{
			Object obj = empInfo.get(dispatch.getSupervisorId());
			Object roles = (empRoleMap!=null)?empRoleMap.get(dispatch.getSupervisorId()):null;
			Object status = (empStatusMap!=null)?empStatusMap.get(dispatch.getSupervisorId()):null;
			Object truckPref = (empTruckPrefMap!=null)?empTruckPrefMap.get(dispatch.getSupervisorId()):null;
			Object teams = (empTeams!=null)?empTeams.get(dispatch.getSupervisorId()):null;
			
			supInfo = DispatchPlanUtil
										.buildEmpInfo(
														(obj != null) ? (EmployeeInfo) obj : null,
																(roles != null) ? (List<EmployeeRole>) roles : null,
																		(status != null) ? (List<EmployeeStatus>) status : null,
																				(truckPref != null) ? (List<EmployeeTruckPreference>) truckPref	: null,
																						(teams != null) ? (List<EmployeeTeam>) teams : null,
																								employeeManagerService
													 );
		} else {
		    supInfo = employeeManagerService.getEmployee(dispatch.getSupervisorId());
		}		
		
    	if(supInfo!=null && supInfo.getEmpInfo()!=null) {
	    	command.setSupervisorCode(supInfo.getEmpInfo().getEmployeeId());
	    	command.setSupervisorName(supInfo.getEmpInfo().getName());
    	}
		command.setRoute(dispatch.getRoute());
		if(dispatch.getTruck() != null){
			command.setTruck(dispatch.getTruck());
			command.setActualTruckAssigned(true);
		} else if(dispatch.getPhysicalTruck() != null){
			command.setTruck(dispatch.getPhysicalTruck());
			command.setActualTruckAssigned(false);
		} else {
			command.setTruck("");
		}

		try {
			command.setDispatchGroup(dispatch.getDispatchGroup());
			command.setStartTime(TransStringUtil.getServerTime(dispatch.getStartTime()));			
			if(dispatch.getDispatchTime()!=null)
				command.setDispatchTime(TransStringUtil.getServerTime(dispatch.getDispatchTime()));
			if(dispatch.getCheckedInTime()!=null)
				command.setCheckedInTime(TransStringUtil.getServerTime(dispatch.getCheckedInTime()));
		} catch(ParseException ex) {
			throw new IllegalArgumentException("Unparseable date "+ex.getMessage());
		}
		if(dispatch.getConfirmed() != null )
			command.setConfirmed(dispatch.getConfirmed().booleanValue());
		command.setPlanId(dispatch.getPlanId());
		command.setComments(dispatch.getComments());
		setResourceReq(command,dispatch.getZone());
		Map resourceReqs=getResourceRequirements(zone);
		Set resources=dispatch.getDispatchResources();
		command.setResourceRequirements(resourceReqs);
		//Comment code for black hole testing
		//if(punchInfos!=null && !punchInfos.isEmpty())
			command.setResources(employeeManagerService,resources,resourceReqs,punchInfos,  empInfo,  empRoleMap, empStatusMap, empTruckPrefMap, empTeams, scannedAssetMapping);
		/*else
			command.setResources(employeeManagerService,resources,resourceReqs);*/
		//command.setResources(employeeManagerService,resources,resourceReqs,resourceSchedule);
		command.setEzpassNumber(dispatch.getEzpassNumber());
		command.setGpsNumber(dispatch.getGpsNumber());		
		if(dispatch.getPhonesAssigned() != null )
			command.setPhoneAssigned(dispatch.getPhonesAssigned().booleanValue());
		
		if(dispatch.getKeysReady() != null )
			command.setKeysReady(dispatch.getKeysReady().booleanValue());
		
		if(dispatch.getKeysIn() != null )
			command.setKeysIn(dispatch.getKeysIn().booleanValue());
		
		if(htInData!=null)command.setHtinDate((Date)htInData.get(dispatch.getRoute()));
		if(htOutData!=null)command.setHtoutDate((Date)htOutData.get(dispatch.getRoute()));
		if(dispatch.getIsOverride()!=null)command.setIsOverride(dispatch.getIsOverride());
		if(dispatch.getOverrideReason()!=null)command.setOverrideReasonCode(dispatch.getOverrideReason().getCode());
		command.setOverrideUser(dispatch.getOverrideUser());
		command.setIsTeamOverride(dispatch.getIsTeamOverride() != null &&  dispatch.getIsTeamOverride() ? true : false);
		
		command.setAdditionalNextels(dispatch.getAdditionalNextels());
		command.setMotKitNumber(dispatch.getMotKitNumber());	
		command.setDispatchType(dispatch.getDispatchType());
		command.setMuniMeterValueAssigned(dispatch.getMuniMeterValueAssigned());
		command.setMuniMeterValueReturned(dispatch.getMuniMeterValueReturned());
		command.setMuniMeterCardNotAssigned(dispatch.getMuniMeterCardNotAssigned());
		command.setMuniMeterCardNotReturned(dispatch.getMuniMeterCardNotReturned());
		command.setMuniMeterEnabled(dispatch.getRegion().getMuniMeterEnabled());
		
		return command;
	}

	public static Plan getPlan(WebPlanInfo planInfo) {

		Plan plan=new Plan();

		plan.setPlanId(planInfo.getPlanId());
		plan.setPlanDate(planInfo.getPlanDate());

		if(!DispatchPlanUtil.isBullpen(planInfo.getIsBullpen())) {
			Zone zone = null;
			if (planInfo.getZoneCode() != null
					&& !"".equalsIgnoreCase(planInfo.getZoneCode())) {
				zone = new Zone();
				zone.setZoneCode(planInfo.getZoneCode());
				plan.setEquipmentTypeS(planInfo.getEquipmentTypeS());
			}
			plan.setZone(zone);
		}

		Region region = new Region();
		region.setCode(planInfo.getRegionCode());
		plan.setRegion(region);
		try {
			plan.setDispatchGroup(planInfo.getDispatchGroup());
			plan.setStartTime(TransStringUtil.getServerTime(planInfo.getStartTime()));
			plan.setEndTime(TransStringUtil.getServerTime(planInfo.getEndTime()));
			plan.setMaxReturnTime((planInfo.getMaxTime() != null && !planInfo.getMaxTime().isEmpty())? TransStringUtil.getServerTime(planInfo.getMaxTime()) : null);
			plan.setCutOffTime((planInfo.getCutOffTime() != null && !planInfo.getCutOffTime().isEmpty())? TransStringUtil.getServerTime(planInfo.getCutOffTime()) : null);
		} catch(ParseException exp) {
			throw new RuntimeException("Unparseable date "+exp.getMessage());
		}
		plan.setSequence(planInfo.getSequence());
		plan.setIsBullpen(planInfo.getIsBullpen());
		plan.setSupervisorId(planInfo.getSupervisorCode());
		plan.setPlanResources(planInfo.getResources());
		plan.setUserId(planInfo.getUserId());
		plan.setOpen(planInfo.getOpen());
		plan.setIsTeamOverride(planInfo.getIsTeamOverride());
		plan.setOriginFacility(planInfo.getOriginFacility());
		plan.setDestinationFacility(planInfo.getDestinationFacility());

		return plan;
	}

	public static Dispatch getDispatch(DispatchCommand command, DomainManagerI domainManagerService) {

		Dispatch dispatch = new Dispatch();
    	if(!"".equals(command.getDispatchId()))
    		dispatch.setDispatchId(command.getDispatchId());
    	try{
	    	if(!"".equals(command.getDispatchDate()))
	    		dispatch.setDispatchDate(TransStringUtil.getDate(command.getDispatchDate()));
    	}catch(ParseException exp){
			throw new RuntimeException("Unparseable date "+exp.getMessage());
		}
		if(!DispatchPlanUtil.isBullpen(command.getIsBullpen()) && !"".equals(command.getZoneCode())) {
			Zone zone=new Zone();
				zone.setZoneCode(command.getZoneCode());
			dispatch.setZone(zone);
			}
		Region region=new Region();
		region.setCode(command.getRegionCode());
		dispatch.setRegion(region);

		dispatch.setDispositionType(domainManagerService.getDispositionType(command.getStatus()));
		dispatch.setSupervisorId(command.getSupervisorCode());
		dispatch.setRoute(command.getRoute());
		dispatch.setTruck(command.getTruck());
		dispatch.setPhysicalTruck(command.getPhysicalTruck());
		dispatch.setBullPen(Boolean.valueOf(command.getIsBullpen()));
		try{
			dispatch.setStartTime(TransStringUtil.getServerTime(command.getStartTime()));
			dispatch.setDispatchTimeEx(TransStringUtil.getServerTime(command.getStartTime()));
			dispatch.setDispatchGroup(command.getDispatchGroup());
			if(command.getDispatchTime()!=null)
				dispatch.setDispatchTime(TransStringUtil.getServerTime(command.getDispatchTime()));
			if(command.getCheckedInTime()!=null)
				dispatch.setCheckedInTime(TransStringUtil.getServerTime(command.getCheckedInTime()));
		}catch(ParseException exp){
			throw new RuntimeException("Unparseable date "+exp.getMessage());
		}
		dispatch.setConfirmed(Boolean.valueOf(command.isConfirmed()));

		dispatch.setPlanId(command.getPlanId());
		dispatch.setComments(command.getComments());
		dispatch.setDispatchResources(command.getResources());
		dispatch.setUserId(command.getUserId());
		dispatch.setGpsNumber(command.getGpsNumber());
		dispatch.setEzpassNumber(command.getEzpassNumber());	
		dispatch.setPhonesAssigned(Boolean.valueOf(command.isPhoneAssigned()));
		dispatch.setKeysReady(Boolean.valueOf(command.isKeysReady()));
		dispatch.setKeysIn(Boolean.valueOf(command.isKeysIn()));
		dispatch.setIsOverride(command.getIsOverride());
		dispatch.setOriginFacility(command.getOriginFacility());
		dispatch.setDestinationFacility(command.getDestinationFacility());
		dispatch.setMuniMeterValueAssigned(command.getMuniMeterValueAssigned());
		dispatch.setMuniMeterValueReturned(command.getMuniMeterValueReturned());
		dispatch.setMuniMeterCardNotAssigned(command.getMuniMeterCardNotAssigned());
		dispatch.setMuniMeterCardNotReturned(command.getMuniMeterCardNotReturned());
		if(command.getOverrideReasonCode()!=null&&command.getOverrideReasonCode().length()>0)
		{
			DispatchReason reason=new DispatchReason();
			reason.setCode(command.getOverrideReasonCode());
			dispatch.setOverrideReason(reason);
		}
		dispatch.setOverrideUser(command.getOverrideUser());
		if((dispatch.getOverrideUser()==null||dispatch.getOverrideUser().length()==0)&&command.getIsOverride())
		{
			dispatch.setOverrideUser(dispatch.getUserId());
		}
		if(command.getIsOverride()==false)
		{
			dispatch.setOverrideUser(null);
			dispatch.setOverrideReason(null);
		}
		
		dispatch.setIsTeamOverride(command.getIsTeamOverride());
		
		dispatch.setAdditionalNextels(command.getAdditionalNextels());
		dispatch.setMotKitNumber(command.getMotKitNumber());
		dispatch.setDispatchType(command.getDispatchType());
		return dispatch;

	}
	public static List getSortedResources(Collection resources) {

		List _resources=(List)resources;
		Collections.sort(_resources, EMPLOYEE_COMPARATOR);
		return _resources;

	}

	public static WebPlanInfo reconstructWebPlanInfo(WebPlanInfo planInfo,Zone zone,String dispatchGroupModified,
			String dispatchDate,EmployeeManagerI employeeManagerService,ZoneManagerI zoneManagerService, boolean isPlan) {

		setResourceReq(planInfo,zone);
		boolean isZoneModified=false;
		planInfo.setPlan(zone!=null && zone.getArea()!=null && isPlan && "X".equals(zone.getArea().getIsDepot()));
		isZoneModified = isZoneModified(zone, planInfo);
		if(zone!=null && isZoneModified) {
			planInfo.setZoneName(zone.getName());
			planInfo.setRegionCode(zone.getRegion().getCode());
			planInfo.setRegionName(zone.getRegion().getName());	
		}
		if (zone != null
				&& planInfo.getDispatchGroupModified() != null
					&& ("true".equalsIgnoreCase(dispatchGroupModified) 
							|| "true".equalsIgnoreCase(planInfo.getZoneModified()))) {
			
			try {
				String shift = null;
				if(planInfo.getDispatchGroup() != null && planInfo.getDispatchGroup() != null) {
					shift = getShift(planInfo.getDispatchGroup());
				}
				planInfo.setSupervisorCode(null);
				
				Date _currentDate = null;
				if (dispatchDate != null && planInfo.getPlanDate() == null)
					_currentDate = TransStringUtil.getServerDateString1(dispatchDate);
				else
					_currentDate = planInfo.getPlanDate();

				Collection supervisorLst = new ArrayList();
				if("AM".equals(shift)){
					supervisorLst = zoneManagerService.getDefaultZoneSupervisor(planInfo.getZoneCode(), shift, TransStringUtil.getDate(_currentDate));
					for (Iterator<ZoneSupervisor> itr = supervisorLst.iterator(); itr.hasNext();) {
						ZoneSupervisor _supervisor = itr.next();					
							WebEmployeeInfo webEmp=employeeManagerService.getEmployee(_supervisor.getSupervisorId());
							if(webEmp!=null && webEmp.getEmpInfo()!=null) {
								planInfo.setSupervisorName(webEmp.getEmpInfo().getSupervisorInfo());
							}							
							planInfo.setSupervisorCode(_supervisor.getSupervisorId());
					}
				} else if ("PM".equals(shift)) {
					supervisorLst = zoneManagerService.getDefaultZoneSupervisor(planInfo.getZoneCode(), shift,TransStringUtil.getDate(_currentDate));
					for (Iterator<ZoneSupervisor> itr = supervisorLst.iterator(); itr.hasNext();) {
						ZoneSupervisor _supervisor = itr.next();						
							WebEmployeeInfo webEmp=employeeManagerService.getEmployee(_supervisor.getSupervisorId());
							if(webEmp!=null && webEmp.getEmpInfo()!=null) {
								planInfo.setSupervisorName(webEmp.getEmpInfo().getSupervisorInfo());
							}
							planInfo.setSupervisorCode(_supervisor.getSupervisorId());
					}
				}
			} catch (ParseException e) {				
				e.printStackTrace();
			}			
			
		}
		setResourceInfo(planInfo,isZoneModified,employeeManagerService);
		return planInfo;
	}
	
	public static boolean isBullpen(String bullpen) {

		if("Y".equalsIgnoreCase(bullpen) || "true".equalsIgnoreCase(bullpen)) {
			return true;
		}
		return false;
	}

	public  static Map getResourceRequirements(Zone zone) {

		Map resourceReqs=new HashMap();
		if(zone==null) {
			ResourceReq resourceReq=new ResourceReq();
			resourceReq.setRole(EnumResourceType.DRIVER);
			resourceReq.setMax(new Integer(TransportationAdminProperties.getDriverMaxForBullpen()));
			resourceReq.setReq(new Integer(TransportationAdminProperties.getDriverReqForBullpen()));
			resourceReqs.put(EnumResourceType.DRIVER,resourceReq);

			resourceReq=new ResourceReq();
			resourceReq.setRole(EnumResourceType.HELPER);
			resourceReq.setMax(new Integer(TransportationAdminProperties.getHelperMaxForBullpen()));
			resourceReq.setReq(new Integer(TransportationAdminProperties.getHelperReqForBullpen()));
			resourceReqs.put(EnumResourceType.HELPER,resourceReq);

			resourceReq=new ResourceReq();
			resourceReq.setRole(EnumResourceType.RUNNER);
			resourceReq.setMax(new Integer(TransportationAdminProperties.getRunnerMaxForBullpen()));
			resourceReq.setReq(new Integer(TransportationAdminProperties.getRunnerReqForBullpen()));
			resourceReqs.put(EnumResourceType.RUNNER,resourceReq);

		}else if(zone.getTrnZoneType()!=null && zone.getTrnZoneType().getZonetypeResources()!=null) {

			Iterator _it=zone.getTrnZoneType().getZonetypeResources().iterator();
			while(_it.hasNext()) {

				ZonetypeResource ztr=(ZonetypeResource)_it.next();
				ResourceReq resourceReq=new ResourceReq();
				resourceReq.setRole(EnumResourceType.getEnum(ztr.getId().getRole()));
				resourceReq.setMax( ztr.getMaximumNo());
				resourceReq.setReq(ztr.getRequiredNo());
				resourceReqs.put(resourceReq.getRole(), resourceReq);

			}
		}
		return resourceReqs;
	}

	private static void setResourceReq(WebPlanInfo model, Zone zone) {

		if(isBullpen(model.getIsBullpen()) || (EnumDispatchType.LIGHTDUTYDISPATCH.getName().equals(model.getDispatchType()))) {
			setDriverRequirements(model,TransportationAdminProperties.getDriverReqForBullpen(),TransportationAdminProperties.getDriverMaxForBullpen());
			setHelperRequirements(model,TransportationAdminProperties.getHelperReqForBullpen(),TransportationAdminProperties.getHelperMaxForBullpen());
			setRunnerRequirements(model,TransportationAdminProperties.getRunnerReqForBullpen(),TransportationAdminProperties.getRunnerMaxForBullpen());

		} else if(hasResources(zone)) {
				Iterator _it=zone.getTrnZoneType().getZonetypeResources().iterator();
				boolean hasDrivers=false;
				boolean hasHelpers=false;
				boolean hasRunners=false;
				while(_it.hasNext()) {
	
					ZonetypeResource ztr=(ZonetypeResource)_it.next();
					int max=ztr.getMaximumNo().intValue();
					int req=ztr.getRequiredNo().intValue();
					String role=ztr.getId().getRole();
					if(EnumResourceType.DRIVER.equals(EnumResourceType.getEnum(role))) {
						hasDrivers=true;
						setDriverRequirements(model,req,max);
					} else if(EnumResourceType.HELPER.equals(EnumResourceType.getEnum(role))) {
						hasHelpers=true;
						setHelperRequirements(model,req,max);
					} else if(EnumResourceType.RUNNER.equals(EnumResourceType.getEnum(role))) {
						hasRunners=true;
						setRunnerRequirements(model,req,max);
					}
				}
				if(!hasDrivers) {
					setDriverRequirements(model,0,0);
				}
				if(!hasHelpers) {
					setHelperRequirements(model,0,0);
				}
				if(!hasRunners) {
					setRunnerRequirements(model,0,0);
				}
		} else {
			setDriverRequirements(model,TransportationAdminProperties.getDriverReqForTrailer(),TransportationAdminProperties.getDriverMaxForTrailer());
			setHelperRequirements(model,TransportationAdminProperties.getHelperReqForTrailer(),TransportationAdminProperties.getHelperMaxForTrailer());
			setRunnerRequirements(model,TransportationAdminProperties.getRunnerReqForTrailer(),TransportationAdminProperties.getRunnerMaxForTrailer());
		}
	}

	private static boolean hasResources(Zone zone) {
		return (zone!=null && zone.getTrnZoneType()!=null && zone.getTrnZoneType().getZonetypeResources()!=null)?true:false;
	}

	private static void setDriverRequirements(WebPlanInfo planInfo, int req, int max) {
		//planInfo.getDrivers().setResourceReq(getResourceReq(req,max,EnumResourceType.DRIVER));
		planInfo.setDriverMax(max);
		planInfo.setDriverReq(req);
	}

	private static void setHelperRequirements(WebPlanInfo planInfo, int req, int max) {
		//planInfo.getHelpers().setResourceReq(getResourceReq(req,max,EnumResourceType.HELPER));
		planInfo.setHelperMax(max);
		planInfo.setHelperReq(req);
	}

	private static void setRunnerRequirements(WebPlanInfo planInfo, int req, int max) {
		//planInfo.getRunners().setResourceReq(getResourceReq(req,max,EnumResourceType.RUNNER));
		planInfo.setRunnerMax(max);
		planInfo.setRunnerReq(req);
	}

	private static ResourceReq getResourceReq(int req, int max, EnumResourceType role) {

		ResourceReq resourceReq=new ResourceReq();
		resourceReq.setMax(new Integer(max));
		resourceReq.setReq(new Integer(req));
		resourceReq.setRole(role);
		return resourceReq;
	}

	private static boolean isZoneModified(Zone zone, WebPlanInfo model) {

		if(isBullpen(model.getIsBullpen())) {
			return false;
		} else if(TransStringUtil.isEmpty(model.getZoneName())) {
		    return true;
		} else if(zone != null && model.getZoneName().equals(zone.getName())) {
			return false;
	    }
		return true;
	}

	public static WebPlanInfo setResourceInfo(WebPlanInfo model, boolean isZoneModified,EmployeeManagerI employeeManagerService) {

		model.setResourceInfo(model.getDrivers(),isZoneModified,EnumResourceType.DRIVER,employeeManagerService,model.getDriverMax());
		model.setResourceInfo(model.getHelpers(),isZoneModified,EnumResourceType.HELPER,employeeManagerService,model.getHelperMax());
		model.setResourceInfo(model.getRunners(),isZoneModified,EnumResourceType.RUNNER,employeeManagerService,model.getRunnerMax());
		return model;

	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Collection getsortedDispatch(Collection unsorted,int page)
	{
		Collection result = new ArrayList();
		List tempResult = (List) getsortedDispatch(unsorted);
		if (page == -1)
			return tempResult;
		int startingIndex = (page - 1) * 25;
		int endingIndex = page * 25;
		if (startingIndex >= tempResult.size())
			return result;
		if (endingIndex > tempResult.size())
			endingIndex = tempResult.size();
		for (int i = startingIndex; i < endingIndex; i++) {
			result.add(tempResult.get(i));
		}

		return result;
	}
	public static Collection getsortedDispatchView(Collection unsorted,int mode)
	{
		//1 ---ready view
		//2 ---waiting view
		//3 ---NR view		
		if (unsorted != null) {
			if(mode==1) {
				List ready = new ArrayList();
				Iterator unsortedIterator = unsorted.iterator();
				int dispatchCategory = 0;
				while (unsortedIterator.hasNext()) {
					DispatchCommand command = (DispatchCommand) unsortedIterator.next();
					dispatchCategory = categorizeDispatch(command);
					if (dispatchCategory == -1
							&& (command.getStartTime() != null && checkReady(command.getStartTime()))
							&& command.getDispatchStatus() == EnumStatus.EmpReady) {
						ready.add(command);
					}
				}
				DispatchTimeComparator compare = new DispatchTimeComparator();
				compare.setStatus(true);
				Collections.sort(ready, compare);
				int READY_MAX = TransportationAdminProperties.getMaxReadyView();
				int n = ready.size();
				if (n > READY_MAX)
					n = READY_MAX;
				List readyTotal = new ArrayList();
				for (int i = 0; i < n; i++) {
					DispatchCommand temp = (DispatchCommand) ((List) ready)
							.get(i);
					if (temp.getDispatchStatus() == EnumStatus.EmpReady)
						temp.setDispatchStatus(EnumStatus.Ready);
					readyTotal.add(temp);
				}
				Collections.sort(readyTotal, new DispatchReadyViewComparator());
				return readyTotal;
			}
			
			if (mode == 2) {
				List total = new ArrayList();
				Iterator unsortedIterator = unsorted.iterator();
				int dispatchCategory = 0;
				while (unsortedIterator.hasNext()) {
					DispatchCommand command = (DispatchCommand) unsortedIterator.next();
					dispatchCategory = categorizeDispatch(command);
					if (dispatchCategory == -1) {
						total.add(command);
					}
				}
				DispatchTimeComparator compare = new DispatchTimeComparator();
				compare.setStatus(true);
				Collections.sort(total, compare);
				int READY_MAX = TransportationAdminProperties.getMaxReadyView();
				int n = total.size();
				if (n > READY_MAX)
					n = READY_MAX;
				Iterator totalItr = total.iterator();
				int i = 0;
				while (totalItr.hasNext()) {
					if (i == n)
						break;
					i++;
					DispatchCommand temp = (DispatchCommand) totalItr.next();
					if ((temp.getStartTime() != null && checkReady(temp
							.getStartTime()))
							&& temp.getDispatchStatus() == EnumStatus.EmpReady) {
						totalItr.remove();
					}
				}
				Collections.sort(total, new DispatchWaitingViewComparator());
				return total;
			}
			
			if (mode == 3) {
				List dispatched = new ArrayList();
				Iterator unsortedIterator = unsorted.iterator();
				int dispatchCategory = 0;
				while (unsortedIterator.hasNext()) {
					DispatchCommand command = (DispatchCommand) unsortedIterator
							.next();
					dispatchCategory = categorizeDispatch(command);
					if (dispatchCategory == 1 && !command.isCheckedIn()) {
						dispatched.add(command);
					}
				}
				Collections.sort(dispatched, new DispatchNRViewComparator());
				return dispatched;
			}
			
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static Collection getsortedDispatch(Collection unsorted)
	{
		Collection total=new ArrayList();
		if (unsorted != null) {
			List ready=new ArrayList();
			List bullpen=new ArrayList();
			List dispatched=new ArrayList();
			Iterator unsortedIterator=unsorted.iterator();
			int dispatchCategory = 0;
			while(unsortedIterator.hasNext())	{
				DispatchCommand command = (DispatchCommand) unsortedIterator
						.next();
				dispatchCategory = categorizeDispatch(command);
				if(dispatchCategory == 1) {
					dispatched.add(command);
				} else if (dispatchCategory == 0) {
					bullpen.add(command);
				} else {
					ready.add(command);
				}					
			}
			DispatchTimeComparator compare=new DispatchTimeComparator();			
			Collections.sort(bullpen, compare);
			Collections.sort(dispatched, compare);
			compare.setStatus(true);
			Collections.sort(ready, compare);
			
			total.addAll(ready);
			total.addAll(bullpen);
			total.addAll(dispatched);
			int READY_MAX=TransportationAdminProperties.getMaxReady();
			int n = total.size();
			if (n > READY_MAX)
				n = READY_MAX;
			for (int i = 0; i < n; i++) {
				DispatchCommand temp=(DispatchCommand)((List)total).get(i);
				if (temp.getDispatchStatus() == EnumStatus.EmpReady)
					temp.setDispatchStatus(EnumStatus.Ready);
			}
		}
		return total;
	}
	
	public static int categorizeDispatch(DispatchCommand command) {
		// 1  - Dispatched
		// 0  - Bullpen
		// -1 - Ready
		if (command.getDispatchTime() != null
				&& command.getDispatchTime().trim().length() > 0) {
			return 1;
		} else if (TransStringUtil.isEmpty(command.getZoneName())) {
			return 0;
		} else {
			return -1;
		}
	}
	
	public static class DispatchTimeComparator implements Comparator{
        private boolean status=false;
        

		public boolean isStatus() {
			return status;
		}


		public void setStatus(boolean status) {
			this.status = status;
		}


		public int compare(Object o1, Object o2) {

			if(o1 instanceof DispatchCommand && o2 instanceof DispatchCommand)
			{
				DispatchCommand p1=(DispatchCommand)o1;
				DispatchCommand p2=(DispatchCommand)o2;

				try {
					if(status)
					{
						int result=p2.getDispatchStatus().compareTo(p1.getDispatchStatus());
						if(result==0)
						{
							Date d1=TransStringUtil.getServerTime(p1.getStartTime());
							Date d2=TransStringUtil.getServerTime(p2.getStartTime());
							return d1.compareTo(d2);
						}
						return result;
					}
					else
					{
						Date d1=TransStringUtil.getServerTime(p1.getStartTime());
						Date d2=TransStringUtil.getServerTime(p2.getStartTime());
						return d1.compareTo(d2);
					}
				} catch (Exception e) {
					
				}
				
			}
			return 0;
		}

	}
	public static class DispatchPunchTimeComparator implements Comparator{
       


		public int compare(Object o1, Object o2) {

			if(o1 instanceof Dispatch && o2 instanceof Dispatch)
			{
				Dispatch p1=(Dispatch)o1;
				Dispatch p2=(Dispatch)o2;

				try {
					
						Date d1=p1.getStartTime();
						Date d2=p2.getStartTime();
						return d1.compareTo(d2);
					
				} catch (Exception e) {
					
				}
				
			}
			return 0;
		}

	}
	private static class DispatchReadyViewComparator implements Comparator{       


		public int compare(Object o1, Object o2) {

			if(o1 instanceof DispatchCommand && o2 instanceof DispatchCommand)
			{
				DispatchCommand p1=(DispatchCommand)o1;
				DispatchCommand p2=(DispatchCommand)o2;

				try {
					
					Date d1=TransStringUtil.getServerTime(p1.getStartTime());
					Date d2=TransStringUtil.getServerTime(p2.getStartTime());
					int result= d1.compareTo(d2);
					if(result==0)
					{
						UPSRouteInfo u1=p1.getUpsRouteInfo();
						UPSRouteInfo u2=p2.getUpsRouteInfo();
						if(u1!=null&&u2!=null&&u1.getStartTime()!=null&&u2.getStartTime()!=null)
						{
							result= u1.getStartTime().compareTo(u2.getStartTime());
						}
					}
					return result;
						
				} catch (Exception e) {
					
				}
				
			}
			return 0;
		}

	}	
	
	private static class DispatchWaitingViewComparator implements Comparator{       


		public int compare(Object o1, Object o2) {

			if(o1 instanceof DispatchCommand && o2 instanceof DispatchCommand)
			{
				DispatchCommand p1=(DispatchCommand)o1;
				DispatchCommand p2=(DispatchCommand)o2;

				try {						
						UPSRouteInfo u1=p1.getUpsRouteInfo();
						UPSRouteInfo u2=p2.getUpsRouteInfo();
						if(u1!=null&&u2!=null&&u1.getStartTime()!=null&&u2.getStartTime()!=null)
						{
							int result= u1.getStartTime().compareTo(u2.getStartTime());
							if(result==0)
							{
								int r1=Integer.parseInt(p1.getRoute());
								int r2=Integer.parseInt(p2.getRoute());
								result=r1-r2;
							}
							return result;
						}						
				} catch (Exception e) {
					
				}
				
			}
			return 0;
		}

	}	
	private static class DispatchNRViewComparator implements Comparator{       


		public int compare(Object o1, Object o2) {

			if(o1 instanceof DispatchCommand && o2 instanceof DispatchCommand)
			{
				DispatchCommand p1=(DispatchCommand)o1;
				DispatchCommand p2=(DispatchCommand)o2;

				try {						
						UPSRouteInfo u1=p1.getUpsRouteInfo();
						UPSRouteInfo u2=p2.getUpsRouteInfo();
						if(u1!=null&&u2!=null&&u1.getEndTime()!=null&&u2.getEndTime()!=null)
						{
							int result= u1.getEndTime().compareTo(u2.getEndTime());							
							return result;
						}						
				} catch (Exception e) {
					
				}
				
			}
			return 0;
		}

	}			
	public static void setDispatchStatus(Collection c,boolean remove) 
	{
		if(remove)
		{
			long dispatchProcessingTime=TransportationAdminProperties.getDispatchPeriod();
			long dispatchProcessedTime=TransportationAdminProperties.getDispatchedPeriod()*-1;
			Calendar currentCalendar = Calendar.getInstance();
			Calendar startCalendar = Calendar.getInstance();		
			Iterator iterator=c.iterator();
			while (iterator.hasNext()) {
				try {
					DispatchCommand command=(DispatchCommand)iterator.next();
					Date startTime = TransStringUtil.getServerTime(command
							.getDispatchTime() != null ? command
							.getDispatchTime() : command.getStartTime());
					startCalendar.setTime(startTime);
					startCalendar.set(currentCalendar.get(Calendar.YEAR)
												,currentCalendar.get(Calendar.MONTH)
													,currentCalendar.get(Calendar.DATE));
					long timediff = startCalendar.getTimeInMillis()
										- currentCalendar.getTimeInMillis();
					if ((timediff < dispatchProcessedTime
							&& !TransStringUtil.isEmpty(command
									.getDispatchTime()) || timediff > dispatchProcessingTime)) {
						iterator.remove();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
		Iterator iterator=c.iterator();
		while (iterator.hasNext()) {
			setDispatchStatus((DispatchCommand)(iterator.next()) );
		}
	}
	
	public static void setDispatchStatus(DispatchCommand command) {
		//for all non bullpen dispatches or trailer routes
		if (!TransStringUtil.isEmpty(command.getZoneName())
				|| (command.getDestinationFacility() != null && EnumTransportationFacilitySrc.CROSSDOCK.getName().equals(command.getDestinationFacility().getTrnFacilityType().getName()))) {
			
			//decide the dispatch status after dispatch;
			if (!TransStringUtil.isEmpty(command.getDispatchTime())) {
				command.setDispatchStatus(EnumStatus.Dispatched);				
				
				//checkedIn status
				if (command.isKeysIn()) {
					command.setDispatchStatus(EnumStatus.keysIn);
				} else {
					return;
				}
				
				//checkedIn status
				if (!TransStringUtil.isEmpty(command.getCheckedInTime())) {
					command.setDispatchStatus(EnumStatus.CheckedIn);
				} else {
					return;
				}
				
				if (checkEmployeeStatus(command, command.getDrivers(), false)
									&& checkEmployeeStatus(command, command.getHelpers(), false)) {
					command.setDispatchStatus(EnumStatus.OffPremises);
				}
			} else {// decide the dispatch status before dispatch
			
				command.setDispatchStatus(EnumStatus.NoStatus);
				
				boolean empReady=false;
				if (checkEmployeeStatus(command, command.getDrivers(), true)
						&& checkEmployeeStatus(command, command.getHelpers(), true)) {
					empReady=true;
				}
				
				//route status
				if (!TransStringUtil.isEmpty(command.getRoute())) {
					command.setDispatchStatus(EnumStatus.Route);
				} else {
					return;
				}
				
				//truck status
				if (!TransStringUtil.isEmpty(command.getTruck())
							&& command.isActualTruckAssigned())
					command.setDispatchStatus(EnumStatus.ActualTruck);
				else if (!TransStringUtil.isEmpty(command.getTruck())
							&& !command.isActualTruckAssigned())
					command.setDispatchStatus(EnumStatus.PlannedTruck);
				else
					return;
				
				//do not do any status if no employees assigned
				if (!command.getIsOverride()
						&& "Y".equalsIgnoreCase(command.getOpen())) {
					//command.setDispatchStatus(EnumStatus.NoStatus);
					return ;
				}
				
				//Packet status
				if (command.isKeysReady() && command.isPhoneAssigned()) {
					command.setDispatchStatus(EnumStatus.Packet);
				} else {
					return;
				}
				if (empReady) {
					command.setDispatchStatus(EnumStatus.EmpReady);
				} else {
					return;
				}
//				if(checkReady(command.getStartTime()))
//				{
//					command.setDispatchStatus(EnumStatus.Ready);
//				}
				
			}
		} else {
			if (!TransStringUtil.isEmpty(command.getDispatchTime())) {
				command.setDispatchStatus(EnumStatus.Dispatched);				
				
				//checkedIn status
				if (!TransStringUtil.isEmpty(command.getCheckedInTime())) {
					command.setDispatchStatus(EnumStatus.CheckedIn);
				} else {
					return;
				}
				if (checkEmployeeStatus(command, command.getDrivers(), false)
						&& checkEmployeeStatus(command, command.getHelpers(),
								false)) {
					command.setDispatchStatus(EnumStatus.OffPremises);
				}
			}
		}
	}
	
	public static boolean checknonNullEmployees(DispatchCommand command)
	{
		boolean result=false;
		if(checknonNullEmployees(command.getDrivers())) result=true;
		if(checknonNullEmployees(command.getHelpers())) result=true;
		return result;
	}
	public static boolean checknonNullEmployees(List employees)
	{
		boolean result=false;
		if(employees!=null&&employees.size()>0)
		{
			for(int i=0,n=employees.size();i<n;i++)
			{
				DispatchResourceInfo employee=(DispatchResourceInfo)employees.get(i);
				if(employee!=null&&employee.getEmployeeId()!=null)
				{
					result=true;
				}
				
			}
		}
		return result;
	}
	public static boolean checkReady(String startTime)
	{
		boolean result=false; 
		try {
			Date d=TransStringUtil.getServerTime(startTime);
			Date c=TransStringUtil.getServerTime(TransStringUtil.getServerTime(new Date()));
			if(c.before(d)) return false;
			else return true;
		} catch (ParseException e) {
			
		}
		return result;
	}
	public static boolean checkEmployeeStatus(DispatchCommand command,List employees,boolean in )
	{
		boolean result=true;
		if(employees!=null&&employees.size()>0)
		{
			for(int i=0,n=employees.size();i<n;i++)
			{
				DispatchResourceInfo employee=(DispatchResourceInfo)employees.get(i);
				if(employee!=null&&employee.getEmployeeId()!=null)
				if(!checkEmployeeStatus(command,employee,in)) result=false;
			}
		}
		return result;
	}
	public static boolean checkEmployeeStatus(DispatchCommand command,DispatchResourceInfo employee,boolean in)
	{
		if(employee.getPunchInfo()==null) return false;		
		if(in)
			return employee.getPunchInfo().isPunchedIn();
		else
			return employee.getPunchInfo().isPunchedOut();

	}
	
	public static boolean isEligibleForPlan(String kronosStatus,String trnStatus)
	{
		if("Inactive".equalsIgnoreCase(kronosStatus))
		{
			if("true".equalsIgnoreCase(trnStatus)) return true;			
		}
		if("Active".equalsIgnoreCase(kronosStatus))
		{
			if("true".equalsIgnoreCase(trnStatus)) return true;			
			if(trnStatus==null) return true;
		}
		return false;
	}
	public static boolean isEligibleForKronosFileGeneration(Collection<EmployeeRole> c)
	{
		if(c!=null&&c.size()>0)
		{			
			if(EnumResourceSubType.isKronosFileGeneration((EnumResourceSubType.getEnum(((EmployeeRole)c.toArray()[0]).getEmployeeSubRoleType().getCode()))))
			{
				return true;
			}
		}
		return false;
	}
	public static boolean isEligibleForUnassignedEmployees(Collection c)
	{
		if(c!=null&&c.size()>0)
		{			
			if(EnumResourceSubType.isUnassignedEmployees((EnumResourceSubType.getEnum(((EmployeeRole)c.toArray()[0]).getEmployeeSubRoleType().getCode()))))
			{
				return true;
			}
		}
		return false;
	}
	public static boolean isEligibleForUnAvailable(Collection c)
	{
		if(c!=null&&c.size()>0)
		{			
			if(EnumResourceSubType.isUnAvailable((EnumResourceSubType.getEnum(((EmployeeRole)c.toArray()[0]).getEmployeeSubRoleType().getCode()))))
			{
				return true;
			}
		}
		return false;
	}
	
	public static Map<String, Asset> getAssetMapping(Collection assets) {
		
		Map<String, Asset> assetMapping = new HashMap<String, Asset>();
		if(assets != null) {
			Iterator itr = assets.iterator();
			Asset asset = null;
			while(itr.hasNext()) {
				asset = (Asset)itr.next();
				assetMapping.put(asset.getAssetId(), asset);
			}
		}
		return assetMapping;
	}
	
	/* 
	 * APPDEV-2994
	 * Shift is 'AM' if the hour part of the dispatch group time is less than 14. Shift is 'PM' otherwise.
	 * 
	 * */	
	public static String getShift(Date dispatchGroupTime) throws ParseException {		
		
		if(dispatchGroupTime != null) {
			double hourOfDay = Double.parseDouble(TransStringUtil.formatTimeFromDate(dispatchGroupTime));
			return hourOfDay < 14 ? "AM" : "PM";
		}
		return null;
	}
	
	 public static WebEmployeeInfo buildEmpInfo( EmployeeInfo empInfo, List<EmployeeRole> empRoles,List<EmployeeStatus> empStatus,
	    		List<EmployeeTruckPreference> empTruckPrefs,List<EmployeeTeam> empTeam, EmployeeManagerI employeeManagerService)
	    {
	    	Map empTruckPrefMap = new HashMap<String, String>();
	    	if(empTruckPrefs!=null)
	    	{
		    	Iterator truckIterator = empTruckPrefs.iterator();
		    	while(truckIterator.hasNext())
		    	{
		    		EmployeeTruckPreference pref  = (EmployeeTruckPreference) truckIterator.next();
		    		empTruckPrefMap.put(pref.getId().getPrefKey(), pref.getTruckNumber());
		    	}
	    	}
			WebEmployeeInfo webInfo = new WebEmployeeInfo(empInfo, empRoles, empTruckPrefMap);
			if (empStatus != null && empStatus.size() > 0) {
				webInfo.setTrnStatus(""
						+ ((EmployeeStatus) (empStatus.toArray()[0])).isStatus());
			}
			if (empTeam != null && empTeam.size() > 0) {
				EmployeeTeam _team = (EmployeeTeam)(empTeam.toArray()[0]);
				EmployeeInfo _leadInfo = TransAdminCacheManager.getInstance().getActiveInactiveEmployeeInfo(_team.getLeadKronosId(), employeeManagerService);
				webInfo.setLeadInfo(_leadInfo);
			}
			return webInfo;
	    }
	 
	 public static boolean isShuttlePlan(IWaveInstanceSource model){
		return (model.getDestinationFacility()!=null && 
				EnumTransportationFacilitySrc.DEPOTDELIVERY.getName().equals(model.getDestinationFacility().getTrnFacilityType().getName()));
				 
	 }
	 
}
