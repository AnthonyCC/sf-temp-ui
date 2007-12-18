package com.freshdirect.transadmin.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.freshdirect.transadmin.model.TrnZone;
import com.freshdirect.transadmin.util.TransStringUtil;

public class ZoneFormController extends AbstractDomainFormController {
		
	protected Map referenceData(HttpServletRequest request) throws ServletException {
		Map refData = new HashMap();		
		refData.put("supervisors", getDomainManagerService().getSupervisors());				
		return refData;
	}
	
	public Object getBackingObject(String id) {
		return getDomainManagerService().getZone(id);
	}
	
	public Object getDefaultBackingObject() {
		return new TrnZone();
	}
	
	public boolean isNew(Object command) {
		TrnZone modelIn = (TrnZone)command;
		return (modelIn.getZoneId() == null);
	}
	
	public String getDomainObjectName() {
		return "Zone";
	}
	
	protected void preProcessDomainObject(Object domainObject) {
		TrnZone modelIn = (TrnZone)domainObject;
		if(TransStringUtil.isEmpty(modelIn.getZoneId()) ) {
			modelIn.setZoneId(modelIn.getZoneNumber());
		}
	}

}
