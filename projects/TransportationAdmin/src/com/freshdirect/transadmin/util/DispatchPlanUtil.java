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
import com.freshdirect.transadmin.model.Dispatch;
import com.freshdirect.transadmin.model.DispatchReason;
import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.model.EmployeeRole;
import com.freshdirect.transadmin.model.Plan;
import com.freshdirect.transadmin.model.Region;
import com.freshdirect.transadmin.model.UPSRouteInfo;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.model.ZoneSupervisor;
import com.freshdirect.transadmin.model.ZonetypeResource;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.web.model.DispatchCommand;
import com.freshdirect.transadmin.web.model.DispatchResourceInfo;
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

				/*int value=p1.getHireDate().compareTo(p2.getHireDate());
				if(value==0) {
					return p1.getLastName().compareTo(p2.getLastName());
				}
				return value;*/
				return p1.getLastName().compareTo(p2.getLastName());
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
			if(plan.getZone().getTrnZoneType()!=null)
				planInfo.setZoneType(plan.getZone().getTrnZoneType().getName());
		}
		planInfo.setRegionCode(plan.getRegion().getCode());
		planInfo.setRegionName(plan.getRegion().getName());
		try{
			planInfo.setFirstDeliveryTime(TransStringUtil.getServerTime(plan.getFirstDeliveryTime()));
			planInfo.setStartTime(TransStringUtil.getServerTime(plan.getStartTime()));
			planInfo.setMaxTime(TransStringUtil.formatTimeFromDate(plan.getMaxTime()));
		}catch(ParseException exp){
			throw new RuntimeException("Unparseable date "+exp.getMessage());
		}
		planInfo.setIsBullpen(plan.getIsBullpen());
		planInfo.setSequence(plan.getSequence());
		planInfo.setSupervisorCode(plan.getSupervisorId());
		WebEmployeeInfo webEmp=employeeManagerService.getEmployee(plan.getSupervisorId());
		if(webEmp!=null && webEmp.getEmpInfo()!=null) {
			planInfo.setSupervisorName(webEmp.getEmpInfo().getName());
		}
		
		String val=DateUtil.formatDay(plan.getPlanDate());
		planInfo.setPlanDay(val);

		Map resourceReqs=getResourceRequirements(zone);
		Set resources=plan.getPlanResources();
		planInfo.setResourceRequirements(resourceReqs);
		planInfo.setResources(employeeManagerService,resources,resourceReqs);
		setResourceReq(planInfo,plan.getZone());
		
		planInfo.setIsTeamOverride(plan.getIsTeamOverride() != null &&  plan.getIsTeamOverride() ? true : false);
		return planInfo;
	}

	public static DispatchCommand getDispatchCommand(Dispatch dispatch, Zone zone,EmployeeManagerI employeeManagerService, Collection punchInfos,Map htInData,Map htOutData) {
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
		if(dispatch.getBullPen()!=null)
			command.setIsBullpen(String.valueOf(dispatch.getBullPen().booleanValue()));
		else
			command.setIsBullpen("");
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
			if(dispatch.getDispatchTime()!=null)
			command.setDispatchTime(TransStringUtil.getServerTime(dispatch.getDispatchTime()));
			if(dispatch.getCheckedInTime()!=null)
				command.setCheckedInTime(TransStringUtil.getServerTime(dispatch.getCheckedInTime()));
		}catch(ParseException ex){
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
			command.setResources(employeeManagerService,resources,resourceReqs,punchInfos);
		/*else
			command.setResources(employeeManagerService,resources,resourceReqs);*/
		//command.setResources(employeeManagerService,resources,resourceReqs,resourceSchedule);
		command.setEzpassNumber(dispatch.getEzpassNumber());
		command.setGpsNumber(dispatch.getGpsNumber());		
		if(dispatch.getPhonesAssigned() != null )
			command.setPhoneAssigned(dispatch.getPhonesAssigned().booleanValue());
		
		if(dispatch.getKeysReady() != null )
			command.setKeysReady(dispatch.getKeysReady().booleanValue());
		
		if(htInData!=null)command.setHtinDate((Date)htInData.get(dispatch.getRoute()));
		if(htOutData!=null)command.setHtoutDate((Date)htOutData.get(dispatch.getRoute()));
		if(dispatch.getIsOverride()!=null)command.setIsOverride(dispatch.getIsOverride());
		if(dispatch.getOverrideReason()!=null)command.setOverrideReasonCode(dispatch.getOverrideReason().getCode());
		command.setOverrideUser(dispatch.getOverrideUser());
		command.setIsTeamOverride(dispatch.getIsTeamOverride() != null &&  dispatch.getIsTeamOverride() ? true : false);
		return command;
	}

    
	public static Plan getPlan(WebPlanInfo planInfo) {

		Plan plan=new Plan();

		plan.setPlanId(planInfo.getPlanId());
		plan.setPlanDate(planInfo.getPlanDate());

		if(!DispatchPlanUtil.isBullpen(planInfo.getIsBullpen())) {
			Zone zone=new Zone();
			zone.setZoneCode(planInfo.getZoneCode());
			plan.setZone(zone);
		}

		Region region=new Region();
		region.setCode(planInfo.getRegionCode());
		plan.setRegion(region);
		try{

			plan.setFirstDeliveryTime(TransStringUtil.getServerTime(planInfo.getFirstDeliveryTime()));
			plan.setStartTime(TransStringUtil.getServerTime(planInfo.getStartTime()));
			plan.setMaxTime(TransStringUtil.formatTimeFromString(planInfo.getMaxTime()));
		}catch(ParseException exp){
			throw new RuntimeException("Unparseable date "+exp.getMessage());
		}
		plan.setSequence(planInfo.getSequence());
		plan.setIsBullpen(planInfo.getIsBullpen());
		plan.setSupervisorId(planInfo.getSupervisorCode());
		plan.setPlanResources(planInfo.getResources());
		plan.setUserId(planInfo.getUserId());	
		plan.setOpen(planInfo.getOpen());
		plan.setIsTeamOverride(planInfo.getIsTeamOverride());
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
		if(!DispatchPlanUtil.isBullpen(command.getIsBullpen())) {
			Zone zone=new Zone();
			zone.setZoneCode(command.getZoneCode());
			dispatch.setZone(zone);
		}
//		System.out.println("Region code $$$$$$$$$$$ "+command.getRegionCode());
		Region region=new Region();
		region.setCode(command.getRegionCode());
		dispatch.setRegion(region);

		dispatch.setDispositionType(domainManagerService.getDispositionType(command.getStatus()));
		dispatch.setSupervisorId(command.getSupervisorCode());
		dispatch.setRoute(command.getRoute());
		dispatch.setTruck(command.getTruck());
		dispatch.setBullPen(new Boolean(command.getIsBullpen()));
		try{
			dispatch.setStartTime(TransStringUtil.getServerTime(command.getStartTime()));
			dispatch.setFirstDlvTime(TransStringUtil.getServerTime(command.getFirstDeliveryTime()));
			if(command.getDispatchTime()!=null)
				dispatch.setDispatchTime(TransStringUtil.getServerTime(command.getDispatchTime()));
			if(command.getCheckedInTime()!=null)
				dispatch.setCheckedInTime(TransStringUtil.getServerTime(command.getCheckedInTime()));
		}catch(ParseException exp){
			throw new RuntimeException("Unparseable date "+exp.getMessage());
		}
		dispatch.setConfirmed(new Boolean(command.isConfirmed()));

		dispatch.setPlanId(command.getPlanId());
		dispatch.setComments(command.getComments());
		dispatch.setDispatchResources(command.getResources());
		dispatch.setUserId(command.getUserId());
		dispatch.setGpsNumber(command.getGpsNumber());
		dispatch.setEzpassNumber(command.getEzpassNumber());	
		dispatch.setPhonesAssigned(new Boolean(command.isPhoneAssigned()));
		dispatch.setKeysReady(new Boolean(command.isKeysReady()));
		dispatch.setIsOverride(command.getIsOverride());
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
		return dispatch;

	}
	public static List getSortedResources(Collection resources) {

		List _resources=(List)resources;
		Collections.sort(_resources, EMPLOYEE_COMPARATOR);
		return _resources;

	}

	public static WebPlanInfo reconstructWebPlanInfo(WebPlanInfo planInfo,Zone zone,String isfirstDlvTimeModified,String dispatchDate,EmployeeManagerI employeeManagerService) {

		setResourceReq(planInfo,zone);
		boolean isZoneModified=false;

		isZoneModified=isZoneModified(zone,planInfo);
		if(zone!=null && isZoneModified) {
			planInfo.setZoneName(zone.getName());
			planInfo.setRegionCode(zone.getRegion().getCode());
			planInfo.setRegionName(zone.getRegion().getName());						
		}
		if(zone!=null && planInfo.getFirstDeliveryTime()!=null 
				&& ("true".equalsIgnoreCase(isfirstDlvTimeModified)|| "true".equalsIgnoreCase(planInfo.getZoneModified()))) {
			
			try {
				String shift = getShiftForPlan(planInfo,dispatchDate);
				if("true".equalsIgnoreCase(isfirstDlvTimeModified)|| "true".equalsIgnoreCase(planInfo.getZoneModified()))
					planInfo.setSupervisorCode(null);
				Date _currentDate = null;
				if(dispatchDate!=null && planInfo.getPlanDate()==null)
					_currentDate = TransStringUtil.getServerDateString1(dispatchDate);
				else						
					_currentDate = planInfo.getPlanDate();
				if("AM".equals(shift)){
					for (Iterator<ZoneSupervisor> itr = zone.getAmZoneSupervisors().iterator(); itr.hasNext();) {
						ZoneSupervisor _supervisor = itr.next();					
						if(_supervisor.getEffectiveDate().equals(_currentDate)){
							WebEmployeeInfo webEmp=employeeManagerService.getEmployee(_supervisor.getSupervisorId());
							if(webEmp!=null && webEmp.getEmpInfo()!=null) {
								planInfo.setSupervisorName(webEmp.getEmpInfo().getSupervisorInfo());
							}							
							planInfo.setSupervisorCode(_supervisor.getSupervisorId());
						}
					}
				}else if("PM".equals(shift)){
					for (Iterator<ZoneSupervisor> itr = zone.getPmZoneSupervisors().iterator(); itr.hasNext();) {
						ZoneSupervisor _supervisor = itr.next();						
						if(_supervisor.getEffectiveDate().equals(_currentDate)){
							WebEmployeeInfo webEmp=employeeManagerService.getEmployee(_supervisor.getSupervisorId());
							if(webEmp!=null && webEmp.getEmpInfo()!=null) {
								planInfo.setSupervisorName(webEmp.getEmpInfo().getSupervisorInfo());
							}
							planInfo.setSupervisorCode(_supervisor.getSupervisorId());
						}
					}
				}
			} catch (ParseException e) {				
				e.printStackTrace();
			}			
			
		}
		setResourceInfo(planInfo,isZoneModified,employeeManagerService);
		return planInfo;
	}
	
	private static String getShiftForPlan(WebPlanInfo planInfo, String dispatchDate) throws ParseException {		
		int day;
		if(dispatchDate!=null && planInfo.getPlanDate()==null)
			day = TransStringUtil.getDayOfWeek(TransStringUtil.getDate(dispatchDate));
		else
			day = TransStringUtil.getDayOfWeek(planInfo.getPlanDate());
		double hourOfDay = Double.parseDouble(TransStringUtil.formatTimeFromDate(TransStringUtil.getServerTime(planInfo.getFirstDeliveryTime())));
		if (hourOfDay < 12 && day != 7) {
			return "AM";
		} else if (hourOfDay < 10 && day == 7) {
			return "AM";
		} else
			return "PM";		
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
		} else if(model.getZoneName().equals(zone.getName())) {
			return false;
	    }
		return true;
	}

	private static WebPlanInfo setResourceInfo(WebPlanInfo model, boolean isZoneModified,EmployeeManagerI employeeManagerService) {

		model.setResourceInfo(model.getDrivers(),isZoneModified,EnumResourceType.DRIVER,employeeManagerService,model.getDriverMax());
		model.setResourceInfo(model.getHelpers(),isZoneModified,EnumResourceType.HELPER,employeeManagerService,model.getHelperMax());
		model.setResourceInfo(model.getRunners(),isZoneModified,EnumResourceType.RUNNER,employeeManagerService,model.getRunnerMax());
		return model;

	}
	public static Collection getsortedDispatch(Collection unsorted,int page)
	{
		Collection result=new ArrayList();
		List tempResult=(List)getsortedDispatch(unsorted);
		if(page==-1)return tempResult;
		int startingIndex=(page-1)*25;
		int endingIndex=page*25;
		if(startingIndex>=tempResult.size()) return result;
		if(endingIndex>tempResult.size())endingIndex=tempResult.size();
		for(int i=startingIndex;i<endingIndex;i++)
		{
			result.add(tempResult.get(i));
		}
		
		return result;
	}
	public static Collection getsortedDispatchView(Collection unsorted,int mode)
	{
		//1 ---ready view
		//2 ---waiting view
		//3 ---NR view		
		if(unsorted!=null)
		{
			if(mode==1)
			{
				List ready=new ArrayList();
				Iterator unsortedIterator=unsorted.iterator();
				int dispatchCategory = 0;
				while(unsortedIterator.hasNext())	{
					DispatchCommand command = (DispatchCommand)unsortedIterator.next();
					dispatchCategory = categorizeDispatch(command);					
					if(dispatchCategory == -1&&(command.getStartTime()!=null&&checkReady(command.getStartTime())))
						// added comment issue 808	&&command.getDispatchStatus()==EnumStatus.EmpReady	) 
					{
						ready.add(command);
					}									
				}
				DispatchTimeComparator compare=new DispatchTimeComparator();				
				compare.setStatus(true);
				Collections.sort(ready, compare);				
				int READY_MAX=TransportationAdminProperties.getMaxReadyView();
				int n=ready.size();if(n>READY_MAX) n=READY_MAX;
				List readyTotal=new ArrayList();
				for(int i=0;i<n;i++ )
				{
					DispatchCommand temp=(DispatchCommand)((List)ready).get(i);
					if(temp.getDispatchStatus()==EnumStatus.EmpReady) temp.setDispatchStatus(EnumStatus.Ready);
					readyTotal.add(temp);
				}
				Collections.sort(readyTotal,new DispatchReadyViewComparator());
				return readyTotal;
			}
			
			if(mode==2)
			{
				List total=new ArrayList();
				Iterator unsortedIterator=unsorted.iterator();
				int dispatchCategory = 0;
				while(unsortedIterator.hasNext())	{
					DispatchCommand command = (DispatchCommand)unsortedIterator.next();
					dispatchCategory = categorizeDispatch(command);
					if(dispatchCategory == -1)
					{
						total.add(command);
					}									
				}
				DispatchTimeComparator compare=new DispatchTimeComparator();				
				compare.setStatus(true);
				Collections.sort(total, compare);				
				int READY_MAX=TransportationAdminProperties.getMaxReadyView();
				int n=total.size();if(n>READY_MAX) n=READY_MAX;				
				Iterator totalItr=total.iterator();
				int i=0;
				while(totalItr.hasNext())
				{				
					if(i==n) break;
					i++;
					DispatchCommand temp=(DispatchCommand)totalItr.next();
					if((temp.getStartTime()!=null&&checkReady(temp.getStartTime()))
						&&temp.getDispatchStatus()==EnumStatus.EmpReady	)
					{
						totalItr.remove();
					}
				}
				Collections.sort(total,new DispatchWaitingViewComparator());
				return total;
			}
			
			if(mode==3)
			{
				List dispatched=new ArrayList();
				Iterator unsortedIterator=unsorted.iterator();
				int dispatchCategory = 0;
				while(unsortedIterator.hasNext())	{
					DispatchCommand command = (DispatchCommand)unsortedIterator.next();
					dispatchCategory = categorizeDispatch(command);
					if(dispatchCategory ==1&&!command.isCheckedIn())
					{
						dispatched.add(command);
					}									
				}								
				Collections.sort(dispatched, new DispatchNRViewComparator());					
				return dispatched;
			}
			
		}
		return null;
	}
	
	public static Collection getsortedDispatch(Collection unsorted)
	{
		Collection total=new ArrayList();
		if(unsorted!=null)
		{
			List ready=new ArrayList();
			List bullpen=new ArrayList();
			List dispatched=new ArrayList();
			Iterator unsortedIterator=unsorted.iterator();
			int dispatchCategory = 0;
			while(unsortedIterator.hasNext())	{
				DispatchCommand command = (DispatchCommand)unsortedIterator.next();
				dispatchCategory = categorizeDispatch(command);
				if(dispatchCategory == 1) {
					dispatched.add(command);
				}
				else if(dispatchCategory == 0)	{
					bullpen.add(command);
				}
				else {
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
			int n=total.size();if(n>READY_MAX) n=READY_MAX;
			for(int i=0;i<n;i++ )
			{
				DispatchCommand temp=(DispatchCommand)((List)total).get(i);
				if(temp.getDispatchStatus()==EnumStatus.EmpReady) temp.setDispatchStatus(EnumStatus.Ready);
			}
		}
		return total;
	}
	
	public static int categorizeDispatch(DispatchCommand command) {
		// 1  - Dispatched
		// 0  - Bullpen
		// -1 - Ready
		if(command.getDispatchTime()!=null&&command.getDispatchTime().trim().length()>0) {
			return 1;
		}
		else if(TransStringUtil.isEmpty(command.getZoneName()))	{
			return 0;
		}
		else {
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
			while(iterator.hasNext())
			{
				try {
					DispatchCommand command=(DispatchCommand)iterator.next();
					Date startTime=TransStringUtil.getServerTime(command.getDispatchTime()!=null?command.getDispatchTime():command.getStartTime());
					startCalendar.setTime(startTime);
					startCalendar.set(currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH), currentCalendar.get(Calendar.DATE));
					long timediff=startCalendar.getTimeInMillis()-currentCalendar.getTimeInMillis();					
					if((timediff<dispatchProcessedTime&&!TransStringUtil.isEmpty(command.getDispatchTime())||timediff>dispatchProcessingTime))
					{
						iterator.remove();
					}
				} catch (Exception e) {	}
				
			}
		}
		Iterator iterator=c.iterator();
		while(iterator.hasNext())
		{
			setDispatchStatus((DispatchCommand)(iterator.next()) );
		}
	}
	
	public static void setDispatchStatus(DispatchCommand command)
	{
		//for all non bullpen dispatches
		if(!TransStringUtil.isEmpty(command.getZoneName()))	
	    {		
			
			//decide the dispatch status after dispatch;
			if(!TransStringUtil.isEmpty(command.getDispatchTime()))
			{
				command.setDispatchStatus(EnumStatus.Dispatched);				
				
				//checkedIn status
				if(!TransStringUtil.isEmpty(command.getCheckedInTime()))
				{					
					command.setDispatchStatus(EnumStatus.CheckedIn);
				}
				else
				{
					return;
				}
				
				if(checkEmployeeStatus(command,command.getDrivers(),false)&&checkEmployeeStatus(command,command.getHelpers(),false))
				{
					command.setDispatchStatus(EnumStatus.OffPremises);
				}
			}
			else//decide the dispatch status before dispatch
			{				
				command.setDispatchStatus(EnumStatus.NoStatus);
				
				boolean empReady=false;
				if(checkEmployeeStatus(command,command.getDrivers(),true)&&checkEmployeeStatus(command,command.getHelpers(),true))
				{
					empReady=true;
				}
				
				//route status
				if(!TransStringUtil.isEmpty(command.getRoute()))
				{					
					command.setDispatchStatus(EnumStatus.Route);
				}
				else
				{
					return;
				}
				
				//truck status
				if(!TransStringUtil.isEmpty(command.getTruck()))
				{					
					command.setDispatchStatus(EnumStatus.Truck);
				}
				else
				{
					return;
				}
				
				//do not do any status if no employees assigned
				if(!command.getIsOverride()&&"Y".equalsIgnoreCase(command.getOpen())) 
				{
					//command.setDispatchStatus(EnumStatus.NoStatus);
					return ;
				}
				
				//Packet status
				if(command.isKeysReady()&& command.isPhoneAssigned())
				{					
					command.setDispatchStatus(EnumStatus.Packet);
				}
				else
				{
					return;
				}
				if(empReady)
				{
					command.setDispatchStatus(EnumStatus.EmpReady);
				}
				else
				{
					return;
				}
//				if(checkReady(command.getStartTime()))
//				{
//					command.setDispatchStatus(EnumStatus.Ready);
//				}
				
			}
	     }
		else
		{
			if(!TransStringUtil.isEmpty(command.getDispatchTime()))
			{
				command.setDispatchStatus(EnumStatus.Dispatched);				
				
				//checkedIn status
				if(!TransStringUtil.isEmpty(command.getCheckedInTime()))
				{					
					command.setDispatchStatus(EnumStatus.CheckedIn);
				}
				else
				{
					return;
				}
				
				if(checkEmployeeStatus(command,command.getDrivers(),false)&&checkEmployeeStatus(command,command.getHelpers(),false))
				{
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
}
