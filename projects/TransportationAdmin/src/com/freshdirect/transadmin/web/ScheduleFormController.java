package com.freshdirect.transadmin.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.ServletRequestDataBinder;

import com.freshdirect.transadmin.model.EmployeeRole;
import com.freshdirect.transadmin.model.EmployeeRoleType;
import com.freshdirect.transadmin.model.EmployeeroleId;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.service.ZoneManagerI;
import com.freshdirect.transadmin.web.model.WebEmployeeInfo;
import com.freshdirect.transadmin.web.model.WebSchedule;

public class ScheduleFormController extends AbstractFormController {

	private EmployeeManagerI employeeManagerService;
	private DomainManagerI domainManagerService;
	private ZoneManagerI zoneManagerService;
	
	protected Map referenceData(HttpServletRequest request) throws ServletException 
	{

		Collection zones=getDomainManagerService().getZones();
		Collection activeZoneCodes = getZoneManagerService().getActiveZoneCodes();
    	if(zones != null && activeZoneCodes != null) {
    		Iterator _iterator = zones.iterator();
    		Zone _tmpZone = null;
    		while(_iterator.hasNext()) {
    			_tmpZone = (Zone)_iterator.next();
    			if(!activeZoneCodes.contains(_tmpZone.getZoneCode())) {
    				_iterator.remove();
    			}
    		}
    	}
		Map refData = new HashMap();
		//refData.put("supervisors", getDomainManagerService().getSupervisors());
		refData.put("region", getDomainManagerService().getRegions());
		refData.put("zone", zones);	
		return refData;
	}

	public Object getBackingObject(String id) {
		//System.out.println("getBackingObject:"+id);
		return getEmployeeManagerService().getSchedule(id);
	}

	public Object getDefaultBackingObject() {
		return new WebSchedule();
	}

	public boolean isNew(Object command) {
		WebSchedule modelIn = (WebSchedule)command;
		return (modelIn.getEmpInfo() == null);
	}

	public String getDomainObjectName() {
		return "WebSchedule";
	}



	protected void onBind(HttpServletRequest request, Object command) {

		//System.out.println("On Bind");
		WebSchedule model = (WebSchedule) command;



	}

	protected void preProcessDomainObject(Object domainObject) {
	//	System.out.println("preProcessDomainObject");
//		Zone modelIn = (Zone)domainObject;
//		if(TransStringUtil.isEmpty(modelIn.getZoneCode()) ) {
//			modelIn.setZoneCode(modelIn.getZoneCode());
//		}
	}

	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		super.initBinder(request,binder);
		//binder.registerCustomEditor(TrnArea.class, new TrnAreaPropertyEditor());
		//binder.registerCustomEditor(TrnZoneType.class, new TrnZoneTypePropertyEditor());
		//binder.registerCustomEditor(Region.class, new RegionPropertyEditor());
    }

public List saveDomainObject(Object domainObject) {

		

		List errorList = null;
		try {
			WebSchedule model = (WebSchedule) domainObject;
			Collection c=model.getSchedules();
			if(c.size()>0)
			getDomainManagerService().saveEntityList(c);
			model.setSchdules(c);
			System.out.println("saving....");
		} catch (DataIntegrityViolationException objExp) {
			errorList = new ArrayList();
			errorList.add(this.getMessage("app.actionmessage.119", new Object[]{this.getDomainObjectName()}));
		}
		return errorList;
	}

public EmployeeManagerI getEmployeeManagerService() {
	return employeeManagerService;
}

public void setEmployeeManagerService(EmployeeManagerI employeeManagerService) {
	this.employeeManagerService = employeeManagerService;
}

public DomainManagerI getDomainManagerService() {
	return domainManagerService;
}

public void setDomainManagerService(DomainManagerI domainManagerService) {
	this.domainManagerService = domainManagerService;
}

public ZoneManagerI getZoneManagerService() {
	return zoneManagerService;
}

public void setZoneManagerService(ZoneManagerI zoneManagerService) {
	this.zoneManagerService = zoneManagerService;
}





}
