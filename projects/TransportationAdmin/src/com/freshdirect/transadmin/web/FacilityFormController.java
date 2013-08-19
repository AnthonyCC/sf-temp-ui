package com.freshdirect.transadmin.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.ServletRequestDataBinder;

import com.freshdirect.transadmin.model.TrnFacility;
import com.freshdirect.transadmin.model.TrnFacilityLocation;
import com.freshdirect.transadmin.model.TrnFacilityType;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.LocationManagerI;
import com.freshdirect.transadmin.service.ZoneManagerI;
import com.freshdirect.transadmin.web.editor.TrnFacilityLocationPropertyEditor;
import com.freshdirect.transadmin.web.editor.TrnFacilityTypePropertyEditor;

public class FacilityFormController extends AbstractFormController {
	
	private LocationManagerI locationManagerService;
	
	private DomainManagerI domainManagerService;

	private ZoneManagerI zoneManagerService;
				
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
	
	public ZoneManagerI getZoneManagerService() {
		return zoneManagerService;
	}

	public void setZoneManagerService(ZoneManagerI zoneManagerService) {
		this.zoneManagerService = zoneManagerService;
	}

	@SuppressWarnings("unchecked")
	protected Map referenceData(HttpServletRequest request) throws ServletException {
		Map refData = new HashMap();
		refData.put("facilityTypes", getLocationManagerService().getTrnFacilityTypes());
		refData.put("facilityLocations", getLocationManagerService().getTrnFacilityLocations());
		return refData;
	}
	
	public Object getBackingObject(String id) {
		return getLocationManagerService().getTrnFacility(id);
	}
	
	public Object getDefaultBackingObject() {
		return new TrnFacility();
	}
	
	public boolean isNew(Object command) {
		TrnFacility modelIn = (TrnFacility)command;
		return (modelIn.getFacilityId() == null);
	}
	
	public String getDomainObjectName() {
		return "Facility";
	}
	
	protected void onBind(HttpServletRequest request, Object command) {
		TrnFacility model = (TrnFacility)command;
		String trnFacilityType = request.getParameter("trnFacilityType");
		String trnFacilityLoc = request.getParameter("facilityLocation");
		
		TrnFacilityType facilityType = getLocationManagerService().getTrnFacilityType(trnFacilityType);
		model.setTrnFacilityType(facilityType);
		
		TrnFacilityLocation facilityLocation = getLocationManagerService().getTrnFacilityLocation(trnFacilityLoc);
		model.setFacilityLocation(facilityLocation);
	}
	
	@SuppressWarnings("unchecked")
	public List saveDomainObject(HttpServletRequest request, Object domainObject) {
		List errorList = new ArrayList();
		TrnFacility modelNew = (TrnFacility)domainObject;
		try{
			Collection activeZones = domainManagerService.getZones();
	        Collection activeZoneCodes = zoneManagerService.getActiveZoneCodes();
	        if(activeZones != null && activeZoneCodes != null) {
	        		Iterator _iterator = activeZones.iterator();
	        		Zone _tmpZone = null;
	        		while(_iterator.hasNext()) {
	        			_tmpZone = (Zone)_iterator.next();        			
	        			if(!activeZoneCodes.contains(_tmpZone.getZoneCode())) {
	        				_iterator.remove();
	        			}
	        		}
	        }
	        Iterator zoneItr = activeZones.iterator();
	        while(zoneItr.hasNext()){
	        	Zone _zone = (Zone)zoneItr.next();
	        	if(modelNew.getRoutingCode().equals(_zone.getZoneCode())){
	        		errorList.add(this.getMessage("app.error.149", new Object[]{}));
	        		break;
	        	}
	        }
			getLocationManagerService().saveEntity(modelNew);			
		}catch(DataIntegrityViolationException e){
			e.printStackTrace();
			errorList.add(this.getMessage("app.actionmessage.119", new Object[]{getDomainObjectName()}));
		}
		
		return errorList;
	}
	
	protected String getIdFromRequest(HttpServletRequest request){
		return request.getParameter("facilityId");
	}
	
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		super.initBinder(request,binder);
		binder.registerCustomEditor(TrnFacilityType.class, new TrnFacilityTypePropertyEditor());
		binder.registerCustomEditor(TrnFacilityLocation.class, new TrnFacilityLocationPropertyEditor());
    }
	
	

}
