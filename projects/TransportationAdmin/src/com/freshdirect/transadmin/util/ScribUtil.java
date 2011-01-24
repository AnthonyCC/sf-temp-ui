package com.freshdirect.transadmin.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.model.Scrib;
import com.freshdirect.transadmin.model.ScribEmployee;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.model.ZoneSupervisor;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.service.ZoneManagerI;
import com.freshdirect.transadmin.web.model.WebEmployeeInfo;

public class ScribUtil 
{
	public static Collection getPlans(Scrib scrib, Collection allEmployees)
	{
		return null;
	}
	
	public static Collection getEmployees(Scrib scrib, Collection employees)
	{
		Collection result=new ArrayList();
		if(employees!=null)
		{
			Iterator employeeIterator=employees.iterator();
			while(employeeIterator.hasNext())
			{
				ScribEmployee employee=(ScribEmployee)employeeIterator.next();
				if(employee.getScheduleDate().equals(scrib.getScribDate())&&employee.getRegion().equals(scrib.getRegion().getCode())&&employee.getStartTime().equals(scrib.getStartTime()))
				{
					result.add(employee);
				}
			}
		}
		return result;
	}
	
	public static Scrib reconstructWebPlanInfo(Scrib scribInfo,Zone zone,String isfirstDlvTimeModified,
			EmployeeManagerI employeeManagerService, ZoneManagerI zoneManagerService) {		
		
		if(zone!=null && scribInfo.getFirstDeliveryTime()!=null 
				&& ("true".equalsIgnoreCase(isfirstDlvTimeModified)|| "true".equalsIgnoreCase(scribInfo.getZoneModified()))){
			try {
				String shift = getShiftForPlan(scribInfo);
				if("true".equalsIgnoreCase(isfirstDlvTimeModified)|| "true".equalsIgnoreCase(scribInfo.getZoneModified()))
					scribInfo.setSupervisorCode(null);
				Collection supervisorLst = new ArrayList();
				if("AM".equals(shift)){
					supervisorLst = zoneManagerService.getDefaultZoneSupervisor(scribInfo.getZoneS(), shift, TransStringUtil.getDate(scribInfo.getScribDate()));
					for (Iterator<ZoneSupervisor> itr = supervisorLst.iterator(); itr.hasNext();) {
						ZoneSupervisor _supervisor = itr.next();					
							WebEmployeeInfo webEmp=employeeManagerService.getEmployee(_supervisor.getSupervisorId());
							if(webEmp!=null && webEmp.getEmpInfo()!=null) {
								scribInfo.setSupervisorName(webEmp.getEmpInfo().getSupervisorInfo());
							}							
							scribInfo.setSupervisorCode(_supervisor.getSupervisorId());						
					}
				}else if("PM".equals(shift)){
					supervisorLst = zoneManagerService.getDefaultZoneSupervisor(scribInfo.getZoneS(), shift, TransStringUtil.getDate(scribInfo.getScribDate()));
					for (Iterator<ZoneSupervisor> itr = supervisorLst.iterator(); itr.hasNext();) {
						ZoneSupervisor _supervisor = itr.next();						
							WebEmployeeInfo webEmp=employeeManagerService.getEmployee(_supervisor.getSupervisorId());
							if(webEmp!=null && webEmp.getEmpInfo()!=null) {
								scribInfo.setSupervisorName(webEmp.getEmpInfo().getSupervisorInfo());
							}
							scribInfo.setSupervisorCode(_supervisor.getSupervisorId());
						
					}
				}
			} catch (ParseException e) {				
				e.printStackTrace();
			}			
		}
		
		return scribInfo;
	}
	
	private static String getShiftForPlan(Scrib model) throws ParseException {		
		int day = TransStringUtil.getDayOfWeek(model.getScribDate());
		double hourOfDay = Double.parseDouble(TransStringUtil.formatTimeFromDate(model.getFirstDeliveryTime()));
		if (hourOfDay < 12 && day != 7) {
			return "AM";
		} else if (hourOfDay < 10 && day == 7) {
			return "AM";
		} else
			return "PM";		
	}

}
