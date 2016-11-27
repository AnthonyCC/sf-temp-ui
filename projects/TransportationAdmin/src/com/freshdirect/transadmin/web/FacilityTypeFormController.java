package com.freshdirect.transadmin.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataIntegrityViolationException;

import com.freshdirect.transadmin.model.TrnFacilityType;
import com.freshdirect.transadmin.service.LocationManagerI;

public class FacilityTypeFormController extends AbstractFormController {

	private LocationManagerI locationManagerService;
	
	public LocationManagerI getLocationManagerService() {
		return locationManagerService;
	}

	public void setLocationManagerService(LocationManagerI locationManagerService) {
		this.locationManagerService = locationManagerService;
	}
	
	public boolean isNew(Object command) {
		TrnFacilityType modelIn = (TrnFacilityType)command;
		return (modelIn.getName() == null);
	}
	
	protected Map referenceData(HttpServletRequest request)
			throws ServletException {
		Map refData = new HashMap();
		return refData;
	}

	public Object getBackingObject(String id) {
		return getLocationManagerService().getTrnFacilityType(id);
	}
	
	public Object getDefaultBackingObject() {
		TrnFacilityType zoneType = new TrnFacilityType();		
		return zoneType;
	}
	public String getDomainObjectName() {
		return "TrnFacilityType";
	}

	public List saveDomainObject(HttpServletRequest request, Object domainObject) {			

		TrnFacilityType model = (TrnFacilityType)domainObject;

		List errorList = null;
		try {			
			getLocationManagerService().saveEntity(model);			
		} catch (DataIntegrityViolationException objExp) {
			errorList = new ArrayList();
			errorList.add(this.getMessage("app.actionmessage.119", new Object[]{this.getDomainObjectName()}));
		}
		return errorList;				
		
	}	
	
	protected String getIdFromRequest(HttpServletRequest request){
		return request.getParameter("facilityTypeId");
	}

}
