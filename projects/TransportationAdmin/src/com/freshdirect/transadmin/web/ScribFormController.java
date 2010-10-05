package com.freshdirect.transadmin.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.transadmin.model.Plan;
import com.freshdirect.transadmin.model.PlanResource;
import com.freshdirect.transadmin.model.Region;
import com.freshdirect.transadmin.model.ResourceId;
import com.freshdirect.transadmin.model.Scrib;
import com.freshdirect.transadmin.model.TrnArea;
import com.freshdirect.transadmin.model.TrnZoneType;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.model.ZoneSupervisor;
import com.freshdirect.transadmin.service.DispatchManagerI;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.service.ZoneManagerI;
import com.freshdirect.transadmin.util.DispatchPlanUtil;
import com.freshdirect.transadmin.util.ScribUtil;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.editor.RegionPropertyEditor;
import com.freshdirect.transadmin.web.editor.TrnAreaPropertyEditor;
import com.freshdirect.transadmin.web.editor.TrnZoneTypePropertyEditor;
import com.freshdirect.transadmin.web.model.CopyPlanCommand;
import com.freshdirect.transadmin.web.model.WebEmployeeInfo;
import com.freshdirect.transadmin.web.model.WebPlanInfo;

public class ScribFormController extends AbstractDomainFormController {
			
	private DispatchManagerI dispatchManagerService;
	private EmployeeManagerI employeeManagerService;
	private ZoneManagerI zoneManagerService;
	
	public EmployeeManagerI getEmployeeManagerService() {
		return employeeManagerService;
	}

	public void setEmployeeManagerService(EmployeeManagerI employeeManagerService) {
		this.employeeManagerService = employeeManagerService;
	}

	protected Map referenceData(HttpServletRequest request) throws ServletException {

		Collection zones=getDomainManagerService().getZones();
		Collection activeZoneCodes = zoneManagerService.getActiveZoneCodes();
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
		refData.put("zones",zones);	
		refData.put("supervisors", DispatchPlanUtil.getSortedResources(employeeManagerService.getSupervisors()));
		return refData;
	}

	public Object getBackingObject(String id) {
		return getDispatchManagerService().getScrib(id);
	}

	public Object getDefaultBackingObject() {
		return new Scrib();
	}

	public boolean isNew(Object command) {
		Scrib modelIn = (Scrib)command;
		return (modelIn.getScribId() == null);
	}

	public String getDomainObjectName() {
		return "Scrib";
	}



	protected void onBind(HttpServletRequest request, Object command) {

		Scrib model = (Scrib) command;
		String zoneId = request.getParameter("zoneS");
		Zone zone = getDomainManagerService().getZone(zoneId);
		if(zone!=null)
		{
			model.setZone(zone);
			model.setRegion(zone.getRegion());
		}
		model= ScribUtil.reconstructWebPlanInfo(model,zone,model.getFirstDeliveryTimeModified(),employeeManagerService);		
	}

	

	protected boolean isFormChangeRequest(HttpServletRequest request, Object command) {

		Scrib _command = (Scrib) command;
		if("true".equalsIgnoreCase(_command.getZoneModified())|| "true".equalsIgnoreCase(_command.getFirstDeliveryTimeModified())) {
			return true;
		}
		else
			return isFormChangeRequest(request);
	}

	protected void onFormChange(HttpServletRequest request, HttpServletResponse response, Object command)
																							throws Exception {
		Scrib _command=(Scrib)command;
		_command.setZoneModified("false");
		_command.setFirstDeliveryTimeModified("false");
	}

	protected void preProcessDomainObject(Object domainObject) {

		Scrib modelIn = (Scrib)domainObject;
		if(TransStringUtil.isEmpty(modelIn.getScribId()) ) {
			modelIn.setScribId(modelIn.getScribId());
		}
	}

	public void setDispatchManagerService(DispatchManagerI dispatchManagerService) {
		this.dispatchManagerService = dispatchManagerService;
	}

	public DispatchManagerI getDispatchManagerService() {
		return dispatchManagerService;
	}

	public List saveDomainObject(Object domainObject) 
	{
		List errorList = null;
		try {
			Scrib model = (Scrib) domainObject;
//			Zone zone=getDomainManagerService().getZone(model.getZoneS());
//			if(zone!=null)model.setRegion(zone.getRegion());
			getDispatchManagerService().saveEntity(domainObject);
		} catch (DataIntegrityViolationException objExp) {
			objExp.printStackTrace();
			errorList = new ArrayList();
			errorList.add(this.getMessage("app.actionmessage.119", new Object[]{this.getDomainObjectName()}));
		}
		return errorList;
	}

	protected String getIdFromRequest(HttpServletRequest request){
		return request.getParameter("scribId");
	}

	public void setZoneManagerService(ZoneManagerI zoneManagerService) {
		this.zoneManagerService = zoneManagerService;
	}
	
}