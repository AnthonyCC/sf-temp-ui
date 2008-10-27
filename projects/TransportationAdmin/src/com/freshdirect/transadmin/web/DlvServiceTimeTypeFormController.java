package com.freshdirect.transadmin.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataIntegrityViolationException;

import com.freshdirect.transadmin.model.DlvServiceTimeType;
import com.freshdirect.transadmin.service.LocationManagerI;
import com.freshdirect.transadmin.util.TransStringUtil;

public class DlvServiceTimeTypeFormController extends AbstractFormController {
		
	private LocationManagerI locationManagerService;
	
	protected Map referenceData(HttpServletRequest request) throws ServletException {
		Map refData = new HashMap();		
		//refData.put("servicetimetypes", getLocationManagerService().getServiceTimeTypes());				
		return refData;
	}
	
	public Object getBackingObject(String id) {
		return getLocationManagerService().getServiceTimeType(id);
	}
	
	public Object getDefaultBackingObject() {
		DlvServiceTimeType domainObj = new DlvServiceTimeType();
		domainObj.setIsNew("true");
		return domainObj;
	}
	
	public boolean isNew(Object command) {
		DlvServiceTimeType modelIn = (DlvServiceTimeType)command;
		return (modelIn.getCode() == null);
	}
	
	public String getDomainObjectName() {
		return "Service Time Type";
	}
	
	protected void preProcessDomainObject(Object domainObject) {
		DlvServiceTimeType modelIn = (DlvServiceTimeType)domainObject;
		if(TransStringUtil.isEmpty(modelIn.getCode()) ) {
			modelIn.setCode(modelIn.getCode());
		}
	}

	public LocationManagerI getLocationManagerService() {
		return locationManagerService;
	}

	public void setLocationManagerService(LocationManagerI locationManagerService) {
		this.locationManagerService = locationManagerService;
	}
	
	public List saveDomainObject(Object domainObject) {
		List errorList = new ArrayList();
		DlvServiceTimeType modelNew = (DlvServiceTimeType)domainObject;	
		if("true".equals(modelNew.getIsNew())) {
			DlvServiceTimeType refDomain = getLocationManagerService().getServiceTimeType(modelNew.getCode());
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
		return errorList;
	}

}
