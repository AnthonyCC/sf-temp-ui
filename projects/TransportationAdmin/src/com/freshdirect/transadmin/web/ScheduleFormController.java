package com.freshdirect.transadmin.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;

import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.transadmin.model.DispatchGroup;
import com.freshdirect.transadmin.model.TrnFacility;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.service.LocationManagerI;
import com.freshdirect.transadmin.web.editor.DispatchGroupPropertyEditor;
import com.freshdirect.transadmin.web.editor.TrnFacilityPropertyEditor;
import com.freshdirect.transadmin.web.model.WebSchedule;

public class ScheduleFormController extends AbstractFormController {

	private EmployeeManagerI employeeManagerService;
	private DomainManagerI domainManagerService;
	private LocationManagerI locationManagerService;
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Map referenceData(HttpServletRequest request)
			throws ServletException {

		Map refData = new HashMap();
		refData.put("region", getDomainManagerService().getRegions());
		refData.put("depotFacility", locationManagerService.getTrnFacilityByType("DPT"));
		refData.put("dispatchGroups", domainManagerService.getDispatchGroups());

		return refData;
	}

	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		String id = getIdFromRequest(request);
		String[] employeeIds = StringUtil.decodeStrings(id.toString());
		Date scheduleDate = getFromClientDate(request.getParameter("scheduleDate"));
		
		if (StringUtils.hasText(id) && scheduleDate != null && employeeIds.length > 0) {
			WebSchedule tmp = (WebSchedule) getBackingObject(employeeIds[0], scheduleDate);
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
		WebSchedule model = (WebSchedule) command;

	}

	protected void preProcessDomainObject(Object domainObject) {
	}

	public void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
		super.initBinder(request, binder);
		binder.registerCustomEditor(TrnFacility.class, new TrnFacilityPropertyEditor());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List saveDomainObject(HttpServletRequest request, Object domainObject) {

		List errorList = null;
		try {
			WebSchedule model = (WebSchedule) domainObject;
			String[] employeeIds = StringUtil.decodeStrings(model.getEmployeeIds());
			Collection c = model.getSchedules();

			if (employeeIds != null && employeeIds.length == 1) {
				if (c.size() > 0) {
					getDomainManagerService().saveEntityList(c);
				}
			} else {
				getDomainManagerService().saveScheduleGroup(c, employeeIds,	model.getWeekOf());
			}
			model.setSchdules(c);
		} catch (DataIntegrityViolationException objExp) {
			objExp.printStackTrace();
			errorList = new ArrayList();
			errorList.add(this.getMessage("app.actionmessage.119",
					new Object[] { this.getDomainObjectName() }));
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

	public LocationManagerI getLocationManagerService() {
		return locationManagerService;
	}

	public void setLocationManagerService(LocationManagerI locationManagerService) {
		this.locationManagerService = locationManagerService;
	}

	@Override
	public Object getBackingObject(String id) {
		return null;
	}

}
