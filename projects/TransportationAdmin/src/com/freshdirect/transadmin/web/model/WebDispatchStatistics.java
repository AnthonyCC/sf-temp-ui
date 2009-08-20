package com.freshdirect.transadmin.web.model;

import java.util.Collection;
import java.util.Iterator;

import com.freshdirect.transadmin.model.Plan;

public class WebDispatchStatistics 
{
	private int paycodeEx;
	private int plannedRoute;
	private int dispatchRoute;
	private int unassigned;
	
	
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
	public void calculatePlanRoute(Collection c)
	{
		int result=0;
		for(Iterator i=c.iterator();i.hasNext();)
		{
			Plan command =(Plan )i.next();
			if(!"Y".equalsIgnoreCase(command.getIsBullpen()))
			{
				result++;
			}
		}
		plannedRoute=result;		
	}
}
