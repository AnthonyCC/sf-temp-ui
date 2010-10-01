package com.freshdirect.transadmin.web.model;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.transadmin.model.Dispatch;
import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.model.Plan;
import com.freshdirect.transadmin.model.RouteInfo;
import com.freshdirect.transadmin.model.TrnAdHocRoute;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.service.DispatchManagerI;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.util.DispatchPlanUtil;
import com.freshdirect.transadmin.util.TransStringUtil;

public class WebDispatchStatistics  implements Serializable 
{
	private int paycodeEx;
	private int plannedAmRoute;
	private int plannedPmRoute;

	private int dispatchAmRoute;
	private int dispatchPmRoute;
	private int unassigned;
	private int empAmWorkedSixdays;
	private int empPmWorkedSixdays;
	private int dispatchId;
	private int dispatchIdRegion;
	private int aMfireTruckorMOT;
	private int pMfireTruckorMOT;
	private int aMTeamChange;
	private int pMTeamChange;
	private int aMTeamChangeRegionOut;
	private int pMTeamChangeRegionOut;
	
	public int getAMTeamChangeRegionOut() {
		return aMTeamChangeRegionOut;
	}
	public void setAMTeamChangeRegionOut(int teamChangeRegionOut) {
		aMTeamChangeRegionOut = teamChangeRegionOut;
	}
	public int getPMTeamChangeRegionOut() {
		return pMTeamChangeRegionOut;
	}
	public void setPMTeamChangeRegionOut(int teamChangeRegionOut) {
		pMTeamChangeRegionOut = teamChangeRegionOut;
	}
	public int getAMTeamChange() {
		return aMTeamChange;
	}
	public void setAMTeamChange(int teamChange) {
		aMTeamChange = teamChange;
	}
	public int getPMTeamChange() {
		return pMTeamChange;
	}
	public void setPMTeamChange(int teamChange) {
		pMTeamChange = teamChange;
	}
	
	public int getEmpAmWorkedSixdays() {
		return empAmWorkedSixdays;
	}
	public void setEmpAmWorkedSixdays(int empAmWorkedSixdays) {
		this.empAmWorkedSixdays = empAmWorkedSixdays;
	}
	public int getEmpPmWorkedSixdays() {
		return empPmWorkedSixdays;
	}
	public void setEmpPmWorkedSixdays(int empPmWorkedSixdays) {
		this.empPmWorkedSixdays = empPmWorkedSixdays;
	}
	public int getAMfireTruckorMOT() {
		return aMfireTruckorMOT;
	}
	public void setAMfireTruckorMOT(int mfireTruckorMOT) {
		aMfireTruckorMOT = mfireTruckorMOT;
	}
	
	public int getPMfireTruckorMOT() {
		return pMfireTruckorMOT;
	}
	public void setPMfireTruckorMOT(int mfireTruckorMOT) {
		pMfireTruckorMOT = mfireTruckorMOT;
	}
	public int getDispatchId() {
		return dispatchId;
	}
	public void setDispatchId(int dispatchId) {
		this.dispatchId = dispatchId;
	}
	public int getDispatchIdRegion() {
		return dispatchIdRegion;
	}
	public void setDispatchIdRegion(int dispatchIdRegion) {
		this.dispatchIdRegion = dispatchIdRegion;
	}
		
	public int getDispatchAmRoute() {
		return dispatchAmRoute;
	}
	public void setDispatchAmRoute(int dispatchAmRoute) {
		this.dispatchAmRoute = dispatchAmRoute;
	}
	public int getDispatchPmRoute() {
		return dispatchPmRoute;
	}
	public void setDispatchPmRoute(int dispatchPmRoute) {
		this.dispatchPmRoute = dispatchPmRoute;
	}
	public int getPaycodeEx() {
		return paycodeEx;
	}
	public void setPaycodeEx(int paycodeEx) {
		this.paycodeEx = paycodeEx;
	}
	public int getPlannedAmRoute() {
		return plannedAmRoute;
	}
	public void setPlannedAmRoute(int plannedAmRoute) {
		this.plannedAmRoute = plannedAmRoute;
	}
	public int getPlannedPmRoute() {
		return plannedPmRoute;
	}
	public void setPlannedPmRoute(int plannedPmRoute) {
		this.plannedPmRoute = plannedPmRoute;
	}
	public int getUnassigned() {
		return unassigned;
	}
	public void setUnassigned(int unassigned) {
		this.unassigned = unassigned;
	}
	
	public void calculateDispatchRoute(Collection c)
	{
		int amCount=0;
		int pmCount=0;
		for(Iterator i=c.iterator();i.hasNext();)
		{
			DispatchCommand command =(DispatchCommand )i.next();	
			try{
				if(("N".equalsIgnoreCase(command.getIsBullpen())||"false".equalsIgnoreCase(command.getIsBullpen()))&& "AM".equalsIgnoreCase(getShiftForDispatch(command, command.getDispatchDate())))
					amCount++;
				else if(("N".equalsIgnoreCase(command.getIsBullpen())||"false".equalsIgnoreCase(command.getIsBullpen()))&& "PM".equalsIgnoreCase(getShiftForDispatch(command, command.getDispatchDate())))
					pmCount++;			
			}catch(ParseException px){
				px.printStackTrace();
			}
		}
		dispatchAmRoute = amCount;
		dispatchPmRoute = pmCount;
	}
	
	public void calculateUnassigned(Collection c)
	{
		if(c!=null) unassigned=c.size();
	}

	public void calculatePaycode(Collection c)
	{
		if(c!=null) paycodeEx=c.size();
	}
	public void calculatePlanRoute(Collection c) throws ParseException
	{		
		int amCount=0;
		int pmCount=0;
		for(Iterator i=c.iterator();i.hasNext();)
		{
			Plan command =(Plan)i.next();		 
			if(!"Y".equalsIgnoreCase(command.getIsBullpen())&& "AM".equalsIgnoreCase(getShiftForPlan(command)))
				amCount++;
			else if(!"Y".equalsIgnoreCase(command.getIsBullpen())&& "PM".equalsIgnoreCase(getShiftForPlan(command)))
				pmCount++;		
		}
		plannedAmRoute = amCount;
		plannedPmRoute = pmCount;
	}
	
	private String getShiftForPlan(Plan p) throws ParseException {		
		int day = TransStringUtil.getDayOfWeek(p.getPlanDate());
		double hourOfDay = Double.parseDouble(TransStringUtil.formatTimeFromDate(p.getFirstDeliveryTime()));
		if (hourOfDay < 12 && day != 7) {
			return "AM";
		} else if (hourOfDay < 10 && day == 7) {
			return "AM";
		} else
			return "PM";		
	}
	
	private static String getShiftForDispatch(WebPlanInfo planInfo, String dispatchDate) throws ParseException {		
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
	
	public void calculateFireTruckorMOT(Collection dataList)
	{
	  	int amCount=0;
		int pmCount=0;
		try{
			for (Iterator i = dataList.iterator(); i.hasNext();) {
				DispatchCommand command = (DispatchCommand) i.next();
				if(command.getRoute()!=null){
					if (((command.getRoute().startsWith("FIRE"))
							|| (command.getRoute().startsWith("MOT")))&& "AM".equalsIgnoreCase(getShiftForDispatch(command, command.getDispatchDate())))
						amCount++;
					else if (((command.getRoute().startsWith("FIRE"))
							|| (command.getRoute().startsWith("MOT")))&& "PM".equalsIgnoreCase(getShiftForDispatch(command, command.getDispatchDate()))) 
						pmCount++;
				}
			}
		}catch(ParseException ex){
			ex.printStackTrace();
		}
		aMfireTruckorMOT = amCount;
		pMfireTruckorMOT = pmCount;
	} 
	
	public void calculateResourceworkedSixdays(List resourceList,
			DispatchManagerI dispatchManagerService,
			DomainManagerI domainManagerService,
			EmployeeManagerI employeeManagerService, boolean isTeamChange, boolean isRegionOut) throws ParseException	
	{
		int amCount=0;
		int pmCount=0;
		Collection dispatchList= new ArrayList();
		if(resourceList!=null){
			for (Iterator iterator = resourceList.iterator(); iterator.hasNext();) {
				String resourceId = (String) iterator.next();
				dispatchList = dispatchManagerService.getDispatchForResource(TransStringUtil.getServerDate(TransStringUtil.getCurrentDate()), resourceId);
				for (Iterator itr = dispatchList.iterator(); itr.hasNext();) {
					Dispatch dispatch = (Dispatch) itr.next();
					Zone zone=null;
					if(dispatch.getZone()!=null) {
						zone = domainManagerService.getZone(dispatch.getZone().getZoneCode());
					}
					DispatchCommand command = DispatchPlanUtil.getDispatchCommand(dispatch, zone, employeeManagerService,null,null,null);
							
					if("AM".equalsIgnoreCase(getShiftForDispatch(command, command.getDispatchDate())))
						amCount++;
					else if("PM".equalsIgnoreCase(getShiftForDispatch(command, command.getDispatchDate())))
						pmCount++;					
				}
			}			
		}
		if(isTeamChange){
			if(isRegionOut){
				aMTeamChangeRegionOut = amCount;
				pMTeamChangeRegionOut = pmCount;
			}else{
				aMTeamChange = amCount;
				pMTeamChange= pmCount;
			}
		}else{
			empAmWorkedSixdays = amCount;
			empPmWorkedSixdays = pmCount;
		}
															
	}
	
	public void calculateDispatchTeamChange(List resourceList,
			DispatchManagerI dispatchManagerService,
			DomainManagerI domainManagerService,
			EmployeeManagerI employeeManagerService, boolean isTeamChange) throws ParseException	
	{
		calculateResourceworkedSixdays(resourceList,dispatchManagerService, domainManagerService, employeeManagerService,isTeamChange, false);
															
	}
	
	public void calculateDispatchTeamChangeOutOfRegion(List resourceList,
			DispatchManagerI dispatchManagerService,
			DomainManagerI domainManagerService,
			EmployeeManagerI employeeManagerService, boolean isTeamChange) throws ParseException	
	{
		calculateResourceworkedSixdays(resourceList,dispatchManagerService, domainManagerService, employeeManagerService,isTeamChange,true);
															
	}
	
	
    
}
	


