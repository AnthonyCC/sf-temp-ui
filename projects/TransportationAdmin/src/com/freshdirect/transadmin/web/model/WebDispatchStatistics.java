package com.freshdirect.transadmin.web.model;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.model.Plan;
import com.freshdirect.transadmin.model.RouteInfo;
import com.freshdirect.transadmin.model.TrnAdHocRoute;
import com.freshdirect.transadmin.util.TransStringUtil;

public class WebDispatchStatistics  implements Serializable 
{
	private int paycodeEx;
	private int plannedRoute;
	
	private int dispatchRoute;
	private int unassigned;
	private int employeesWorkedSixdays;
	
	private int dispatchId;
	private int dispatchIdRegion;
	private int fireTruckorMOT;
	
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
	public int getFireTruckorMOT() {
		return fireTruckorMOT;
	}
	public void setFireTruckorMOT(int fireTruckorMOT) {
		this.fireTruckorMOT = fireTruckorMOT;
	}

	
	public int getEmployeesWorkedSixdays() {
		return employeesWorkedSixdays;
	}
	public void setEmployeesWorkedSixdays(int employeesWorkedSixdays) {
		this.employeesWorkedSixdays = employeesWorkedSixdays;
	}
	//private int unassignedRouteNumber;
	public int getDispatchRoute() {
		return dispatchRoute;
	}
	public void setDispatchRoute(int dispatchRoute) {
		this.dispatchRoute = dispatchRoute;
	}
	public int getPaycodeEx() {
		return paycodeEx;
	}
	public void setPaycodeEx(int paycodeEx) {
		this.paycodeEx = paycodeEx;
	}
	public int getPlannedRoute() {
		return plannedRoute;
		
	}
	public void setPlannedRoute(int plannedRoute) {
		this.plannedRoute = plannedRoute;
	}
	public int getUnassigned() {
		return unassigned;
	}
	public void setUnassigned(int unassigned) {
		this.unassigned = unassigned;
	}
	
	public void calculateDispatchRoute(Collection c)
	{
		int result=0;
		for(Iterator i=c.iterator();i.hasNext();)
		{
			DispatchCommand command =(DispatchCommand )i.next();
			
			//if(command.getPlanDay()){}
			if("N".equalsIgnoreCase(command.getIsBullpen())||"false".equalsIgnoreCase(command.getIsBullpen()))
			{
				result++;
			}
		}
		dispatchRoute=result;		
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
		
		
		int result=0;
		for(Iterator i=c.iterator();i.hasNext();)
		{
			Plan command =(Plan )i.next();
			
			int dayOfweek = TransStringUtil.getClientDayofWeek(TransStringUtil.getDatewithTime(command.getPlanDate()));
			
			double hourOfDay = Double.parseDouble(TransStringUtil.formatTimeFromDate(command.getFirstDeliveryTime()) );
			 
			if(!"Y".equalsIgnoreCase(command.getIsBullpen()))
			{
				result++;
				
			}
			if (hourOfDay < 12 && dayOfweek != 7) {
				plannedRoute=result;
			} else if (hourOfDay < 10 && dayOfweek == 7) {
				plannedRoute=result;
				} else
					plannedRoute=result;	
			}
					
	}
	// added new code AppDev 808
	public void calculateEmployeesWorkedSixdays(List planList) 
	
	{
	if(planList!=null) employeesWorkedSixdays=planList.size();
												
	}
		
	
  public void calculateDispatchId(Collection c)
	
	{
		
	}
	
  public void calculateDispatchIdRegion(Collection c)
	
	{
		
	}
  
  public void calculateFireTruckorMOT(Collection dataList)
	
	{
	 	  
	 int result=0;
		for(Iterator i=dataList.iterator();i.hasNext();)
		{
			TrnAdHocRoute routetype =(TrnAdHocRoute )i.next();
			if((routetype.getRouteNumber().startsWith("FIRE"))||(routetype.getRouteNumber().startsWith("MOT")))
			{
				result++;
			}
		}
		fireTruckorMOT =result;
	}
  
    
}
	


