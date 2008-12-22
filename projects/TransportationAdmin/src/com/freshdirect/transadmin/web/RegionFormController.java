package com.freshdirect.transadmin.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.freshdirect.transadmin.model.Region;
import com.freshdirect.transadmin.model.TrnAdHocRoute;
import com.freshdirect.transadmin.model.TrnRoute;
import com.freshdirect.transadmin.util.TransStringUtil;

public class RegionFormController extends AbstractDomainFormController {

	
	public RegionFormController() {
		// OK to start with a blank command object
		System.out.println("***************** RegionFormController*****************");
		setCommandClass(Region.class);
		// activate session form mode to allow for detection of duplicate submissions
		setSessionForm(true);
	}
	
	protected Map referenceData(HttpServletRequest request) throws ServletException {
		Map refData = new HashMap();		
		//refData.put("supervisors", getDomainManagerService().getSupervisors());
		//refData.put("zones", getDomainManagerService().getZones());
		//refData.put("timings", getDomainManagerService().getTimings());
		return refData;
	}
	
	public Object getBackingObject(String id) {
		return getDomainManagerService().getRegion(id);
	}
	
	public Object getDefaultBackingObject() {
		return new Region();
	}
	
	public boolean isNew(Object command) {
		Region modelIn = (Region)command;
		return (modelIn.getCode() == null);
	}
	
	public String getDomainObjectName() {
		return "Region";
	}
	
	protected void preProcessDomainObject(Object domainObject) {
		Region modelIn = (Region)domainObject;
		//if(TransStringUtil.isEmpty(modelIn.getRouteId()) ) {
///			modelIn.setRouteId(modelIn.getRouteId());
	//	}
	}

}
