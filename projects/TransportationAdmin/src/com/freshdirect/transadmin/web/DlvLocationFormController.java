package com.freshdirect.transadmin.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.freshdirect.routing.constants.EnumArithmeticOperator;
import com.freshdirect.routing.service.proxy.GeographyServiceProxy;
import com.freshdirect.transadmin.model.DlvBuilding;
import com.freshdirect.transadmin.model.DlvLocation;
import com.freshdirect.transadmin.service.LocationManagerI;
import com.freshdirect.transadmin.util.TransStringUtil;

public class DlvLocationFormController extends AbstractFormController {
		
	private LocationManagerI locationManagerService;
	
	protected Map referenceData(HttpServletRequest request) throws ServletException {
		Map refData = new HashMap();		
		refData.put("servicetimetypes", getLocationManagerService().getServiceTimeTypes());	
		refData.put("confidencetypes", getLocationManagerService().getConfidenceTypes());	
		refData.put("qualitytypes", getLocationManagerService().getQualityTypes());
		refData.put("serviceTimeOperators", (List)EnumArithmeticOperator.getEnumList());
		try {
			refData.put("states", new GeographyServiceProxy().getStateList());
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return refData;
	}
	
	public Object getBackingObject(String id) {
		return getLocationManagerService().getDlvLocation(id);
	}
	
	public Object getDefaultBackingObject() {
		return new DlvLocation();
	}
	
	public boolean isNew(Object command) {
		DlvLocation modelIn = (DlvLocation)command;
		return (modelIn.getLocationId() == null);
	}
	
	public String getDomainObjectName() {
		return "Delivery Location";
	}
	
	protected void preProcessDomainObject(Object domainObject) {
		DlvLocation modelIn = (DlvLocation)domainObject;
		if(TransStringUtil.isEmpty(modelIn.getLocationId()) ) {
			modelIn.setLocationId(modelIn.getLocationId());
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
		DlvLocation modelIn = (DlvLocation)domainObject;
		if(TransStringUtil.isEmpty(modelIn.getApartment()) ) {
			modelIn.setApartment(null);
		}		
		DlvBuilding building = getLocationManagerService().getDlvBuilding(modelIn.getBuildingId());
		modelIn.setBuilding(building);
		try {
			getLocationManagerService().saveEntity(domainObject);
		} catch(Exception e) {
			e.printStackTrace();
			errorList.add(this.getMessage("app.actionmessage.119", new Object[]{"Location"}));
		}
		return errorList;
	}
	
	
}
