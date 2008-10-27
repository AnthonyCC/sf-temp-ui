package com.freshdirect.transadmin.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataIntegrityViolationException;
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
		
		String serviceTimeType = null;
		String zoneType = null;
		String id = request.getParameter("id");
		if(id != null && !"null".equalsIgnoreCase(id) && id.trim().length() != 0) {
			StringTokenizer splitter = new StringTokenizer(id, "$p$g");
			if(splitter.hasMoreTokens()) {
				serviceTimeType = splitter.nextToken();
			}
			if(splitter.hasMoreTokens()) {
				zoneType = splitter.nextToken(); 
			}
		}
		//String serviceTimeType = request.getParameter("servicetimetype");
		//String zoneType = request.getParameter("zonetype");
		
		if (StringUtils.hasText(serviceTimeType) && StringUtils.hasText(zoneType)) {
			return getLocationManagerService().getServiceTime(serviceTimeType, zoneType);
		} else {
			return getDefaultBackingObject();
		}
	}
		
	public Object getBackingObject(String id) {
		return null;
	}
	
	public Object getDefaultBackingObject() {
		DlvServiceTime domainObj = new DlvServiceTime();
		domainObj.setIsNew("true");
		return domainObj;
	}
	
	public boolean isNew(Object command) {
		DlvServiceTime modelIn = (DlvServiceTime)command;
		return ("true".equals(modelIn.getIsNew()));
	}
	
	public String getDomainObjectName() {
		return "Service Time";
	}
	
	public List saveDomainObject(Object domainObject) {
		
		List errorList = new ArrayList();
		DlvServiceTime modelNew = (DlvServiceTime)domainObject;
		if("true".equals(modelNew.getIsNew())) {
			DlvServiceTime refDomain = getLocationManagerService().getServiceTime(modelNew.getServiceTimeId().getServiceTimeType(),
																		modelNew.getServiceTimeId().getZoneType());
			if(refDomain != null) {
				errorList.add(this.getMessage("app.actionmessage.119", new Object[]{getDomainObjectName()}));
			} 
		}
		if(errorList.size() == 0) {
			try {
				getLocationManagerService().saveEntity(modelNew);
				modelNew.setIsNew(null);
			} catch (DataIntegrityViolationException e) {
				errorList.add(getMessage("app.actionmessage.119", new Object[]{getDomainObjectName()}));
			}
		}
		//getLocationManagerService().saveEntity(domainObject);
		return errorList;
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
