package com.freshdirect.transadmin.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;

import com.freshdirect.transadmin.model.DlvServiceTime;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.LocationManagerI;

public class DlvServiceTimeFormController extends AbstractFormController {
	
	private LocationManagerI locationManagerService;
	
	private DomainManagerI domainManagerService;
	
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
			super.initBinder(request, binder);
	}

	protected Map referenceData(HttpServletRequest request) throws ServletException {
		Map refData = new HashMap();		
		
		refData.put("servicetimetypes", locationManagerService.getServiceTimeTypes());
		refData.put("zonetypes", domainManagerService.getZoneTypes());	
		return refData;
	}
	
		
	protected Object formBackingObject(HttpServletRequest request)
	throws Exception {
		String serviceTimeType = request.getParameter("servicetimetype");
		String serviceType = request.getParameter("zonetype");
		
		if (StringUtils.hasText(serviceTimeType) && StringUtils.hasText(serviceType)) {
			return getLocationManagerService().getServiceTime(serviceTimeType, serviceType);
		} else {
			return getDefaultBackingObject();
		}
	}
		
	public Object getBackingObject(String id) {
		return null;
	}
	
	public Object getDefaultBackingObject() {
		return new DlvServiceTime();
	}
	
	public boolean isNew(Object command) {
		DlvServiceTime modelIn = (DlvServiceTime)command;
		return (modelIn.getId() == null);
	}
	
	public String getDomainObjectName() {
		return "Service Time";
	}
	
	public List saveDomainObject(Object domainObject) {
		getLocationManagerService().saveEntity(domainObject);
		return null;
	}
		

	public LocationManagerI getLocationManagerService() {
		return locationManagerService;
	}

	public void setLocationManagerService(LocationManagerI locationManagerService) {
		this.locationManagerService = locationManagerService;
	}

	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}	

}
