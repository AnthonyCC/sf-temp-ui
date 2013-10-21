package com.freshdirect.transadmin.util;

import java.text.ParseException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.freshdirect.transadmin.model.Scrib;
import com.freshdirect.transadmin.model.ScribEmployee;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.model.ZoneSupervisor;
import com.freshdirect.transadmin.service.AssetManagerI;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.service.ZoneManagerI;
import com.freshdirect.transadmin.web.model.WebEmployeeInfo;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ScribUtil {
	
	public static Collection getPlans(Scrib scrib, Collection allEmployees) {
		return null;
	}
	
	public static Collection getEmployees(Scrib scrib, Collection employees) {
		Collection result = new ArrayList();
		if (employees != null) {
			Iterator employeeIterator = employees.iterator();
			while (employeeIterator.hasNext()) {
				ScribEmployee employee = (ScribEmployee) employeeIterator
						.next();
				if (employee.getScheduleDate().equals(scrib.getScribDate())
						&& employee.getRegion().equals(
								scrib.getRegion().getCode())
						&& employee.getStartTime().equals(scrib.getStartTime())) {
					result.add(employee);
				}
			}
		}
		return result;
	}
	

	public static Scrib reconstructScribInfo(Scrib scribInfo, Zone zone,
			String isDispatchGroupTimeModified, EmployeeManagerI employeeManagerService, 
			ZoneManagerI zoneManagerService, AssetManagerI assetManagerService) {		
		
		if (zone != null) {
			try {
				if(scribInfo.getDispatchGroup () != null 
						&& ("true".equalsIgnoreCase(isDispatchGroupTimeModified) || "true".equalsIgnoreCase(scribInfo.getZoneModified()))){
				
					String shift = getShift(scribInfo);
					if ("true".equalsIgnoreCase(isDispatchGroupTimeModified)
							|| "true".equalsIgnoreCase(scribInfo.getZoneModified()))
						scribInfo.setSupervisorCode(null);
					Collection supervisorLst = new ArrayList();
					if("AM".equals(shift)){
						supervisorLst = zoneManagerService.getDefaultZoneSupervisor(scribInfo.getZoneS(), shift, TransStringUtil.getDate(scribInfo.getScribDate()));
						for (Iterator<ZoneSupervisor> itr = supervisorLst.iterator(); itr.hasNext();) {
							ZoneSupervisor _supervisor = itr.next();					
								WebEmployeeInfo webEmp=employeeManagerService.getEmployee(_supervisor.getSupervisorId());
								if (webEmp != null && webEmp.getEmpInfo() != null) {
									scribInfo.setSupervisorName(webEmp.getEmpInfo().getSupervisorInfo());
								}							
								scribInfo.setSupervisorCode(_supervisor.getSupervisorId());						
						}
					} else if("PM".equals(shift)){
						supervisorLst = zoneManagerService.getDefaultZoneSupervisor(scribInfo.getZoneS(), shift, TransStringUtil.getDate(scribInfo.getScribDate()));
						for (Iterator<ZoneSupervisor> itr = supervisorLst.iterator(); itr.hasNext();) {
							ZoneSupervisor _supervisor = itr.next();						
								WebEmployeeInfo webEmp=employeeManagerService.getEmployee(_supervisor.getSupervisorId());
								if (webEmp != null && webEmp.getEmpInfo() != null) {
									scribInfo.setSupervisorName(webEmp.getEmpInfo().getSupervisorInfo());
								}
								scribInfo.setSupervisorCode(_supervisor.getSupervisorId());							
						}
					}
				}
				if("true".equalsIgnoreCase(scribInfo.getZoneModified())
						&& zone.getArea() != null){
					scribInfo.setEquipmentTypes(assetManagerService.getEquipmentTypes(zone.getArea().getRegion()));
				}
			} catch (ParseException e) {				
				e.printStackTrace();
			}	
		}
		
		return scribInfo;
	}
	
	private static String getShift(Scrib model) throws ParseException {		
		if(model.getDispatchGroup() != null) {
			double hourOfDay = Double.parseDouble(TransStringUtil.formatTimeFromDate(model.getDispatchGroup()));
			return hourOfDay < 14 ? "AM" : "PM";
		}
		return null;
	}

}
