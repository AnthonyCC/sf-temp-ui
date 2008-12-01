package com.freshdirect.transadmin.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.freshdirect.transadmin.model.TrnZoneType;
import com.freshdirect.transadmin.util.TransStringUtil;

public class ZoneTypeFormController extends AbstractDomainFormController {
		
	protected Map referenceData(HttpServletRequest request) throws ServletException {
		Map refData = new HashMap();						
		return refData;
	}
	
	public Object getBackingObject(String id) {
		return getDomainManagerService().getZoneType(id);
	}
	
	public Object getDefaultBackingObject() {
		return new TrnZoneType();
	}
	
	public boolean isNew(Object command) {
		TrnZoneType modelIn = (TrnZoneType)command;
		return (modelIn.getZoneTypeId() == null);
	}
	
	public String getDomainObjectName() {
		return "ZoneType";
	}
	
	protected void preProcessDomainObject(Object domainObject) {
		TrnZoneType modelIn = (TrnZoneType)domainObject;
		if(TransStringUtil.isEmpty(modelIn.getZoneTypeId()) ) {
			modelIn.setZoneTypeId(modelIn.getZoneTypeId());
		}
	}

}
