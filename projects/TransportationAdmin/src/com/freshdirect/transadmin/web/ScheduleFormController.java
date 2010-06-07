package com.freshdirect.transadmin.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;

import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.service.ZoneManagerI;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.model.WebSchedule;

public class ScheduleFormController extends AbstractFormController {

	private EmployeeManagerI employeeManagerService;
	private DomainManagerI domainManagerService;
	private ZoneManagerI zoneManagerService;

	protected Map referenceData(HttpServletRequest request)
			throws ServletException {

		Collection zones = getDomainManagerService().getZones();
		Collection activeZoneCodes = getZoneManagerService()
				.getActiveZoneCodes();
		if (zones != null && activeZoneCodes != null) {
			Iterator _iterator = zones.iterator();
			Zone _tmpZone = null;
			while (_iterator.hasNext()) {
				_tmpZone = (Zone) _iterator.next();
				if (!activeZoneCodes.contains(_tmpZone.getZoneCode())) {
					_iterator.remove();
				}
			}
		}
		Map refData = new HashMap();
		// refData.put("supervisors",
		// getDomainManagerService().getSupervisors());
		refData.put("region", getDomainManagerService().getRegions());
		refData.put("zone", zones);
		return refData;
	}

	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		String id = getIdFromRequest(request);
		String[] employeeIds = StringUtil.decodeStrings(id.toString());
		Date scheduleDate = getFromClientDate(request.getParameter("scheduleDate"));
		
		if (StringUtils.hasText(id) && scheduleDate != null && employeeIds.length > 0) {
			WebSchedule tmp = (WebSchedule)getBackingObject(employeeIds[0], scheduleDate);
			tmp.setEmployeeIds(id);			
			return tmp;
		} else {
			return getDefaultBackingObject(id, scheduleDate);
		}
	}

	public Object getBackingObject(String id, Date scheduleDate) {
		
		return getEmployeeManagerService().getSchedule(id, scheduleDate);
	}

	public Object getDefaultBackingObject() {
		return new WebSchedule(null, null);
	}
	
	public Object getDefaultBackingObject(String employeeIds, Date scheduleDate) {
		return new WebSchedule(employeeIds, scheduleDate);
	}

	public boolean isNew(Object command) {
		WebSchedule modelIn = (WebSchedule) command;
		return (modelIn.getEmpInfo() == null);
	}

	public String getDomainObjectName() {
		return "WebSchedule";
	}

	protected void onBind(HttpServletRequest request, Object command) {

		// System.out.println("On Bind");
		WebSchedule model = (WebSchedule) command;

	}

	protected void preProcessDomainObject(Object domainObject) {
		// System.out.println("preProcessDomainObject");
		// Zone modelIn = (Zone)domainObject;
		// if(TransStringUtil.isEmpty(modelIn.getZoneCode()) ) {
		// modelIn.setZoneCode(modelIn.getZoneCode());
		// }
	}

	public void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
		super.initBinder(request, binder);
		// binder.registerCustomEditor(TrnArea.class, new
		// TrnAreaPropertyEditor());
		// binder.registerCustomEditor(TrnZoneType.class, new
		// TrnZoneTypePropertyEditor());
		// binder.registerCustomEditor(Region.class, new
		// RegionPropertyEditor());
	}

	public List saveDomainObject(Object domainObject) {

		List errorList = null;
		try {
			WebSchedule model = (WebSchedule) domainObject;
			String[] employeeIds = StringUtil.decodeStrings(model.getEmployeeIds());
			Collection c = model.getSchedules();
			
			if(employeeIds != null && employeeIds.length == 1) {				
				if (c.size() > 0) {
					getDomainManagerService().saveEntityList(c);
				}
				model.setSchdules(c);
			} else {
				getDomainManagerService().saveScheduleGroup(model, employeeIds, model.getWeekOf());
			}
						
		} catch (DataIntegrityViolationException objExp) {
			objExp.printStackTrace();
			errorList = new ArrayList();
			errorList.add(this.getMessage("app.actionmessage.119",	new Object[] { this.getDomainObjectName() }));
		} 
		return errorList;
	}

	public EmployeeManagerI getEmployeeManagerService() {
		return employeeManagerService;
	}

	public void setEmployeeManagerService(
			EmployeeManagerI employeeManagerService) {
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

	@Override
	public Object getBackingObject(String id) {
		// Replaced BY getBackingObject(String id, Date scheduleDate)
		return null;
	}

}
