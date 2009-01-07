package com.freshdirect.transadmin.web;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.transadmin.model.GeoRestriction;
import com.freshdirect.transadmin.service.RestrictionManagerI;

public class GeoRestrictionFormController extends AbstractFormController {
	private RestrictionManagerI restrictionManagerService;
	

	public RestrictionManagerI getRestrictionManagerService() {
		return restrictionManagerService;
	}

	public void setRestrictionManagerService(
			RestrictionManagerI restrictionManagerService) {
		this.restrictionManagerService = restrictionManagerService;
	}

	public String getDomainObjectName() {
		return "Geo Restriction";
	}
	
	protected void preProcessDomainObject(Object domainObject) {
		System.out.println("$$$$$$entering preProcess");	

		GeoRestriction modelIn = (GeoRestriction)domainObject;
		//if(TransStringUtil.isEmpty(modelIn.getBuilding().getBuildingId()) ) {
			//modelIn.getBuilding().setBuildingId(modelIn.getBuilding().getBuildingId());
		//}
	}

	public Object getDefaultBackingObject() {
		GeoRestriction domainObj = new GeoRestriction();
		//domainObj.setIsNew("true");
		return domainObj;
	}
	
	public Object getBackingObject(String id) {
		System.out.println("#########entering getBackingObject");

		//return getLocationManagerService().getDlvBuildingDtl(id);
		GeoRestriction result = getRestrictionManagerService().getGeoRestriction(id);
		if(null == result){
			//DlvBuilding building = getLocationManagerService().getDlvBuilding(id);
			result = new GeoRestriction();
			//result.setBuilding(building);
			result.setName(new String("GeoRestrictionName"));
			//result.setIsNew("true");
			return result;
		}

		//result.setIsNew("false");
		
		System.out.println("#########exiting getBackingObject");
		
		return result;		
	}
	
	
	public boolean isNew(Object command) {
		GeoRestriction modelIn = (GeoRestriction)command;
		return (modelIn.getName() == null);  //tbr
	}
	
	
	public List saveDomainObject(Object domainObject) {
		
		System.out.println("entering to save");	
		List errorList = new ArrayList();
		GeoRestriction modelIn = (GeoRestriction)domainObject;
		//if("true".equalsIgnoreCase(modelIn.getSvcValidate())){
			//modelIn.setSvcScrubbedStreet(RoutingUtil.standardizeStreetAddress(modelIn.getSvcScrubbedStreet(), null));
		//}
		
		//if(TransStringUtil.isEmpty(modelIn.getDifficultReason()) ) {
			//modelIn.setDifficultReason(null);
		//}		
		//lvBuilding building = getLocationManagerService().getDlvBuilding(modelIn.getBuilding().getBuildingId());
		//modelIn.setBuilding(building);		
		try {
			getRestrictionManagerService().saveEntity(domainObject);
			//modelIn.setIsNew("false");
		} catch(Exception e) {
			e.printStackTrace();
			errorList.add(this.getMessage("app.actionmessage.119", new Object[]{getDomainObjectName()}));
		}
		return errorList;
	}
	
	
	

}
