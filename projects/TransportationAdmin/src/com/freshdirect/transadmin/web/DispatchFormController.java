package com.freshdirect.transadmin.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.propertyeditors.ClassEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;

import com.freshdirect.transadmin.model.TrnDispatch;
import com.freshdirect.transadmin.model.TrnDispatchId;
import com.freshdirect.transadmin.service.DispatchManagerI;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.util.TransStringUtil;

public class DispatchFormController extends AbstractFormController {
	
	private DomainManagerI domainManagerService;
	
	private DispatchManagerI dispatchManagerService;
	
	public DispatchManagerI getDispatchManagerService() {
		return dispatchManagerService;
	}

	public void setDispatchManagerService(DispatchManagerI dispatchManagerService) {
		this.dispatchManagerService = dispatchManagerService;
	}
	
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
			DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
			CustomDateEditor editor = new CustomDateEditor(df, false);
			binder.registerCustomEditor(Date.class, editor);
			binder.registerCustomEditor(TrnDispatchId.class, new ClassEditor(){
				public void setAsText(String text) {
					  setValue(new TrnDispatchId());
				}				
			});
	}

	protected Map referenceData(HttpServletRequest request) throws ServletException {
		Map refData = new HashMap();		
		//refData.put("zones", domainManagerService.getZones());
		refData.put("drivers", dispatchManagerService.getDrivers());
		refData.put("helpers", dispatchManagerService.getHelpers());
		refData.put("timeslots", domainManagerService.getTimeSlots());
		refData.put("supervisors", getDomainManagerService().getSupervisors());
		refData.put("trucks", getDomainManagerService().getTrucks());
		refData.put("routes", getDomainManagerService().getRoutes());
		return refData;
	}
	
		
	protected Object formBackingObject(HttpServletRequest request)
	throws Exception {
		String id = request.getParameter("id");
		String dispDate = request.getParameter("dispDate");
		
		if (StringUtils.hasText(id) && StringUtils.hasText(dispDate)) {
			return getDispatchManagerService().getDispatch(id, TransStringUtil.getServerDate(dispDate));
		} else {
			return getDefaultBackingObject();
		}
	}
		
	public Object getBackingObject(String id) {
		return null;
	}
	
	public Object getDefaultBackingObject() {
		return new TrnDispatch();
	}
	
	public boolean isNew(Object command) {
		TrnDispatch modelIn = (TrnDispatch)command;
		return (modelIn.getId() == null);
	}
	
	public String getDomainObjectName() {
		return "Dispatch";
	}
	
	public List saveDomainObject(Object domainObject) {
		getDomainManagerService().saveEntity(domainObject);
		return null;
	}
	
	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}	

}
