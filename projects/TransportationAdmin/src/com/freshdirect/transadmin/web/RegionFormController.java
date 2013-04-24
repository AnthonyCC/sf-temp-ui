package com.freshdirect.transadmin.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestDataBinder;

import com.freshdirect.transadmin.model.Region;
import com.freshdirect.transadmin.model.TrnFacility;
import com.freshdirect.transadmin.web.editor.TrnFacilityPropertyEditor;

public class RegionFormController extends AbstractDomainFormController {


	public RegionFormController() {
		// OK to start with a blank command object
//		System.out.println("***************** RegionFormController*****************");
		setCommandClass(Region.class);
		// activate session form mode to allow for detection of duplicate submissions
		setSessionForm(true);
	}

	@SuppressWarnings("unchecked")
	protected Map referenceData(HttpServletRequest request) throws ServletException {
		Map refData = new HashMap();
		refData.put("trnFacilitys", getLocationManagerService().getTrnFacilitys());
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
	}
	
	protected void onBind(HttpServletRequest request, Object command) {
		Region model = (Region) command;	
		String originFacility = request.getParameter("originFacility");
		TrnFacility deliveryFacility = getLocationManagerService().getTrnFacility(originFacility);
		model.setOriginFacility(deliveryFacility);				
	}

	
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		super.initBinder(request,binder);		
		binder.registerCustomEditor(TrnFacility.class, new TrnFacilityPropertyEditor());
    }

}
