package com.freshdirect.transadmin.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.freshdirect.transadmin.model.TrnAdHocRoute;
import com.freshdirect.transadmin.util.TransStringUtil;

public class RouteFormController extends AbstractDomainFormController {
		
	protected Map referenceData(HttpServletRequest request) throws ServletException {
		Map refData = new HashMap();		
		//refData.put("supervisors", getDomainManagerService().getSupervisors());
		//refData.put("zones", getDomainManagerService().getZones());
		refData.put("timings", getDomainManagerService().getTimings());
		return refData;
	}
	
	public Object getBackingObject(String id) {
		return getDomainManagerService().getAdHocRoute(id);
	}
	
	public Object getDefaultBackingObject() {
		return new TrnAdHocRoute();
	}
	
	public boolean isNew(Object command) {
		TrnAdHocRoute modelIn = (TrnAdHocRoute)command;
		return (modelIn.getRouteId() == null);
	}
	
	public String getDomainObjectName() {
		return "Route";
	}
	
	protected void preProcessDomainObject(Object domainObject) {
		TrnAdHocRoute modelIn = (TrnAdHocRoute)domainObject;
		//if(TransStringUtil.isEmpty(modelIn.getRouteId()) ) {
///			modelIn.setRouteId(modelIn.getRouteId());
	//	}
	}

}
