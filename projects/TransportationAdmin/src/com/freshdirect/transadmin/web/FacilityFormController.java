package com.freshdirect.transadmin.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.ServletRequestDataBinder;

import com.freshdirect.transadmin.model.Region;
import com.freshdirect.transadmin.model.TrnArea;
import com.freshdirect.transadmin.model.TrnFacility;
import com.freshdirect.transadmin.model.TrnFacilityType;
import com.freshdirect.transadmin.model.TrnZoneType;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.LocationManagerI;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.editor.RegionPropertyEditor;
import com.freshdirect.transadmin.web.editor.TrnAreaPropertyEditor;
import com.freshdirect.transadmin.web.editor.TrnFacilityTypePropertyEditor;
import com.freshdirect.transadmin.web.editor.TrnZoneTypePropertyEditor;

public class FacilityFormController extends AbstractFormController {
	
	private LocationManagerI locationManagerService;
			
	public LocationManagerI getLocationManagerService() {
		return locationManagerService;
	}

	public void setLocationManagerService(LocationManagerI locationManagerService) {
		this.locationManagerService = locationManagerService;
	}

	protected Map referenceData(HttpServletRequest request) throws ServletException {
		Map refData = new HashMap();
		refData.put("trnFacilityTypes", getLocationManagerService().getTrnFacilityTypes());
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
		return "TrnFacility";
	}
	
	protected void onBind(HttpServletRequest request, Object command) {
		TrnFacility model = (TrnFacility)command;
		String trnFacilityType = request.getParameter("trnFacilityType");
		
		TrnFacilityType facilityType = getLocationManagerService().getTrnFacilityType(trnFacilityType);
		model.setTrnFacilityType(facilityType);
	}
	
	public List saveDomainObject(HttpServletRequest request, Object domainObject) {
		List errorList = new ArrayList();
		TrnFacility modelNew = (TrnFacility)domainObject;
	
		try{
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
    }

}
