package com.freshdirect.transadmin.web;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataAccessException;

import com.freshdirect.transadmin.model.TrnZoneType;
import com.freshdirect.transadmin.util.TransStringUtil;

public class ZoneTypeFormController extends AbstractDomainFormController {
		
	protected Map referenceData(HttpServletRequest request) throws ServletException {
		Map refData = new HashMap();
		refData.put("roles", getDomainManagerService().getEmployeeRoleTypes());
		//refData.put("supervisors", getDomainManagerService().getSupervisors());	
		//refData.put("zonetypes", getDomainManagerService().getZoneTypes());	
		//refData.put("areas", getDomainManagerService().getAreas());	
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
		return (modelIn.getId() == null);
	}
	
	public String getDomainObjectName() {
		return "ZoneType";
	}
	
	protected void preProcessDomainObject(Object domainObject) {
		TrnZoneType modelIn = (TrnZoneType)domainObject;
		if(TransStringUtil.isEmpty(modelIn.getId()) ) {
			modelIn.setId(modelIn.getId());
		}
	}

}
