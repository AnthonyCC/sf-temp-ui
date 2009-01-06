package com.freshdirect.transadmin.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Date;

import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.transadmin.model.Dispatch;
import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.model.Plan;
import com.freshdirect.transadmin.model.Region;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.model.ZonetypeResource;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.web.model.DispatchCommand;
import com.freshdirect.transadmin.web.model.ResourceList;
import com.freshdirect.transadmin.web.model.ResourceReq;
import com.freshdirect.transadmin.web.model.WebEmployeeInfo;
import com.freshdirect.transadmin.web.model.WebPlanInfo;

public class DispatchPlanUtil {
	
	private static EmployeeComparator EMPLOYEE_COMPARATOR=new EmployeeComparator();
	
	private static class EmployeeComparator implements Comparator{

	
		public int compare(Object o1, Object o2) {
			
			if(o1 instanceof EmployeeInfo && o2 instanceof EmployeeInfo)
			{
				EmployeeInfo p1=(EmployeeInfo)o1;
				EmployeeInfo p2=(EmployeeInfo)o2;
				
				int value=p1.getHireDate().compareTo(p2.getHireDate());
				if(value==0) {
					return p1.getLastName().compareTo(p2.getLastName());
				}
				return value;
			}
			return 0;
		}
				
	}
	
	public static WebPlanInfo getWebPlanInfo(Plan plan, Zone zone,EmployeeManagerI employeeManagerService) {
		
		WebPlanInfo planInfo=new WebPlanInfo();
		planInfo.setPlanId(plan.getPlanId());
		planInfo.setPlanDate(plan.getPlanDate());
		if(plan.getZone()!=null) {
			planInfo.setZoneCode(plan.getZone().getZoneCode());
			planInfo.setZoneName(plan.getZone().getName());
		}
		planInfo.setRegionCode(plan.getRegion().getCode());
		planInfo.setRegionName(plan.getRegion().getName());
		try{
			planInfo.setFirstDeliveryTime(TransStringUtil.getServerTime(plan.getFirstDeliveryTime()));
			planInfo.setStartTime(TransStringUtil.getServerTime(plan.getStartTime()));
		}catch(ParseException exp){
			throw new RuntimeException("Unparseable date "+exp.getMessage());
		}
		planInfo.setIsBullpen(plan.getIsBullpen());
		planInfo.setSequence(plan.getSequence());
		try {
			String val=DateUtil.formatDay(plan.getPlanDate());
			planInfo.setPlanDay(val);
		} catch (ParseException e) {}
		
		Map resourceReqs=getResourceRequirements(zone);
		Set resources=plan.getPlanResources();
		planInfo.setResourceRequirements(resourceReqs);
		planInfo.setResources(employeeManagerService,resources,resourceReqs);
		return planInfo;
	}
	
	public static DispatchCommand getDispatchCommand(Dispatch dispatch, Zone zone,EmployeeManagerI employeeManagerService) {
		DispatchCommand command = new DispatchCommand();
		command.setDispatchId(dispatch.getDispatchId());
		try{
			command.setDispatchDate(TransStringUtil.getDate(dispatch.getDispatchDate()));
		}catch(ParseException ex){
			throw new IllegalArgumentException("Unparseable date "+ex.getMessage());
		}
		String zoneCode = "";
		if(dispatch.getZone() != null) {
			zoneCode = dispatch.getZone().getZoneCode();
		}
		command.setZoneCode(zoneCode);
		command.setRegionCode(dispatch.getRegion().getCode());
		command.setRegionName(dispatch.getRegion().getName());

		if(dispatch.getDispositionType() != null){
			command.setStatus(dispatch.getDispositionType().getCode());
			command.setStatusName(dispatch.getDispositionType().getName());
		}
		//command.setStatus(dispatch.getDispositionType().getCode());
		WebEmployeeInfo supInfo = employeeManagerService.getEmployee(dispatch.getSupervisorId());
		
    	if(supInfo!=null && supInfo.getEmpInfo()!=null) {
	    	command.setSupervisorCode(supInfo.getEmpInfo().getEmployeeId());
	    	command.setSupervisorName(supInfo.getEmpInfo().getName());
    		
    	}
		command.setRoute(dispatch.getRoute());
		if(dispatch.getTruck() != null){
			command.setTruck(dispatch.getTruck());
		} else {
			command.setTruck("");
		}
		
		try{
			command.setStartTime(TransStringUtil.getServerTime(dispatch.getStartTime()));
			command.setFirstDeliveryTime(TransStringUtil.getServerTime(dispatch.getFirstDlvTime()));
		}catch(ParseException ex){
			throw new IllegalArgumentException("Unparseable date "+ex.getMessage());
		}
		if(dispatch.getConfirmed() != null )
			command.setConfirmed(dispatch.getConfirmed().booleanValue());
		command.setPlanId(dispatch.getPlanId());
		command.setComments(dispatch.getComments());
		
		Map resourceReqs=getResourceRequirements(zone);
		Set resources=dispatch.getDispatchResources();
		command.setResourceRequirements(resourceReqs);
		command.setResources(employeeManagerService,resources,resourceReqs);
		return command;
	}
	
	
	public static Plan getPlan(WebPlanInfo planInfo) {
		
		Plan plan=new Plan();
		
		plan.setPlanId(planInfo.getPlanId());
		plan.setPlanDate(planInfo.getPlanDate());
		
		Zone zone=new Zone();
		zone.setZoneCode(planInfo.getZoneCode());
		plan.setZone(zone);
		
		Region region=new Region();
		region.setCode(planInfo.getRegionCode());
		plan.setRegion(region);
		try{
			//plan.setFirstDeliveryTime(new Date());
			//plan.setStartTime(new Date());
			//plan.setFirstDeliveryTime(planInfo.getFirstDeliveryTime());
			//plan.setStartTime(planInfo.getStartTime());			
			plan.setFirstDeliveryTime(TransStringUtil.getServerTime(planInfo.getFirstDeliveryTime()));
			plan.setStartTime(TransStringUtil.getServerTime(planInfo.getStartTime()));
		}catch(ParseException exp){
			throw new RuntimeException("Unparseable date "+exp.getMessage());
		}
		plan.setSequence(planInfo.getSequence());
		plan.setIsBullpen(planInfo.getIsBullpen());
		plan.setPlanResources(planInfo.getResources());
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
		Zone zone=new Zone();
		zone.setZoneCode(command.getZoneCode());
		dispatch.setZone(zone);
		
		Region region=new Region();
		region.setCode(command.getRegionCode());
		dispatch.setRegion(region);
		
		dispatch.setDispositionType(domainManagerService.getDispositionType(command.getStatus()));
		dispatch.setSupervisorId(command.getSupervisorCode());
		dispatch.setRoute(command.getRoute());
		dispatch.setTruck(command.getTruck());
		try{
			dispatch.setStartTime(TransStringUtil.getServerTime(command.getStartTime()));
			dispatch.setFirstDlvTime(TransStringUtil.getServerTime(command.getFirstDeliveryTime()));
		}catch(ParseException exp){
			throw new RuntimeException("Unparseable date "+exp.getMessage());
		}
		dispatch.setConfirmed(new Boolean(command.isConfirmed()));
		
		dispatch.setPlanId(command.getPlanId());
		dispatch.setComments(command.getComments());
		dispatch.setDispatchResources(command.getResources());
		return dispatch;
		
	}
	public static List getSortedResources(Collection resources) {
		
		List _resources=(List)resources;
		/*List sortedResources=new ArrayList(_resources.size());
		for(int i=0;i<_resources.size();i++) {
			sortedResources.add(_resources.get(i));
		}*/
		
		Collections.sort(_resources, EMPLOYEE_COMPARATOR);
		return _resources;
		
	}
	
	public static WebPlanInfo reconstructWebPlanInfo(WebPlanInfo planInfo,Zone zone,EmployeeManagerI employeeManagerService) {
		
		setResourceReq(planInfo,zone);
		boolean isZoneModified=false;
		
		isZoneModified=isZoneModified(zone,planInfo);
		if(zone!=null && isZoneModified) {
			planInfo.setZoneName(zone.getName());
			planInfo.setRegionCode(zone.getRegion().getCode());
			planInfo.setRegionName(zone.getRegion().getName());
		} 
		setResourceInfo(planInfo,isZoneModified,employeeManagerService);
		return planInfo;
	}

	public static boolean isBullpen(String bullpen) {
		
		if("Y".equalsIgnoreCase(bullpen)) {
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
		
		if(isBullpen(model.getIsBullpen())) {
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
			setDriverRequirements(model,0,0);
			setHelperRequirements(model,0,0);
			setRunnerRequirements(model,0,0);
		}
	}
	
	private static boolean hasResources(Zone zone) {
		
		return (zone!=null && zone.getTrnZoneType()!=null && zone.getTrnZoneType().getZonetypeResources()!=null)?true:false;
	}
	
	private static void setDriverRequirements(WebPlanInfo planInfo, int req, int max) {

		planInfo.setDriverMax(max);
		planInfo.setDriverReq(req);
		planInfo.getDrivers().setResourceReq(getResourceReq(req,max,EnumResourceType.DRIVER));
		/*if(req==0&&max==0) {
			planInfo.getDrivers().clear();
		} else {
			planInfo.getDrivers().setResourceReq(getResourceReq(req,max,EnumResourceType.DRIVER.getName()));
		}*/
	}

	private static void setHelperRequirements(WebPlanInfo planInfo, int req, int max) {

		planInfo.setHelperMax(max);
		planInfo.setHelperReq(req);
		planInfo.getHelpers().setResourceReq(getResourceReq(req,max,EnumResourceType.HELPER));
		/*if(req==0&&max==0) {
			planInfo.getHelpers().clear();
		} else {
			planInfo.getHelpers().setResourceReq(getResourceReq(req,max,EnumResourceType.HELPER.getName()));
		}*/
	}
	
	private static void setRunnerRequirements(WebPlanInfo planInfo, int req, int max) {

		planInfo.setRunnerMax(max);
		planInfo.setRunnerReq(req);
		planInfo.getRunners().setResourceReq(getResourceReq(req,max,EnumResourceType.RUNNER));
		/*if(req==0&&max==0) {
			planInfo.getRunners().clear();
		} else {
			planInfo.getRunners().setResourceReq(getResourceReq(req,max,EnumResourceType.RUNNER.getName()));
		}*/
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
		} else if(model.getZoneName().equals(zone.getName())) {
			return false;
	    }
		return true;
	}
	

	private static WebPlanInfo setResourceInfo(WebPlanInfo model, boolean isZoneModified,EmployeeManagerI employeeManagerService) {

		model.setResourceInfo(model.getDrivers(),isZoneModified,EnumResourceType.DRIVER,employeeManagerService);
		model.setResourceInfo(model.getHelpers(),isZoneModified,EnumResourceType.HELPER,employeeManagerService);
		model.setResourceInfo(model.getRunners(),isZoneModified,EnumResourceType.RUNNER,employeeManagerService);
		return model;
		
	}

}
