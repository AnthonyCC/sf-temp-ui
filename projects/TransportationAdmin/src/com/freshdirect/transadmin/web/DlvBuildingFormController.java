package com.freshdirect.transadmin.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;


import com.freshdirect.routing.constants.EnumGeocodeConfidenceType;
import com.freshdirect.routing.constants.EnumGeocodeQualityType;
import com.freshdirect.routing.model.IBuildingModel;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.GeographyServiceProxy;
import com.freshdirect.routing.util.RoutingUtil;
import com.freshdirect.transadmin.model.DeliveryGroup;
import com.freshdirect.transadmin.model.DlvBuilding;
import com.freshdirect.transadmin.service.LocationManagerI;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.util.TransportationAdminProperties;

public class DlvBuildingFormController extends AbstractFormController {
		
	private LocationManagerI locationManagerService;
	
	protected Map referenceData(HttpServletRequest request) throws ServletException {
		Map refData = new HashMap();		
		refData.put("servicetimetypes", getLocationManagerService().getServiceTimeTypes());	
		refData.put("confidencetypes", getLocationManagerService().getConfidenceTypes());	
		refData.put("qualitytypes", getLocationManagerService().getQualityTypes());
		refData.put("deliveryGroups", getLocationManagerService().getDeliveryGroups());
		
		return refData;
	}
	
	
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
	       binder.registerCustomEditor(Set.class, "buildingGroups", new CustomCollectionEditor(Set.class) {
	           protected Object convertElement(Object element) {
	               if (element != null) {
	                   String id = (String) element;
	                   DeliveryGroup group = getLocationManagerService().getDeliveryGroupById(id);
	                   return group;
	               }
	               return null;
	           }
	       });
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {
	       String buildingId = getIdFromRequest(request);

	       if ((buildingId != null) && request.getMethod().equalsIgnoreCase("get")) {
	           return getLocationManagerService().getDlvBuilding(buildingId);
	       } else {
	           return new DlvBuilding();
	       }
	}
	
	public Object getBackingObject(String id) {
		return getLocationManagerService().getDlvBuilding(id);
	}	
	
	public Object getDefaultBackingObject() {
		return new DlvBuilding();
	}
	
	public boolean isNew(Object command) {
		DlvBuilding modelIn = (DlvBuilding)command;
		return (modelIn.getBuildingId() == null);
	}
	
	public String getDomainObjectName() {
		return "Delivery Building";
	}
	
	protected void preProcessDomainObject(Object domainObject) {
		DlvBuilding modelIn = (DlvBuilding)domainObject;
		if(TransStringUtil.isEmpty(modelIn.getBuildingId()) ) {
			modelIn.setBuildingId(modelIn.getBuildingId());
		}
	}

	public LocationManagerI getLocationManagerService() {
		return locationManagerService;
	}

	public void setLocationManagerService(LocationManagerI locationManagerService) {
		this.locationManagerService = locationManagerService;
	}
	
	public List saveDomainObject(HttpServletRequest request, Object domainObject) {
		
		List errorList = new ArrayList();
		DlvBuilding modelIn = (DlvBuilding)domainObject;
		GeographyServiceProxy proxy = new GeographyServiceProxy();
				
		modelIn.setCountry(TransportationAdminProperties.getDefaultCountry());
		String street = RoutingUtil.standardizeStreetAddress(modelIn.getSrubbedStreet(), null);
		modelIn.setSrubbedStreet(street);
		try {
			IBuildingModel model = proxy.getBuildingLocation(modelIn.getSrubbedStreet(), modelIn.getZip());
			//Verify if building exists with the same scrubbed address and zipcode
			if(model != null && model.getBuildingId() != null &&
					!model.getBuildingId().equalsIgnoreCase(modelIn.getBuildingId())) {
				errorList.add(this.getMessage("app.actionmessage.119", new Object[]{"Building"}));
			}  else {
				
				if((modelIn.getLatitude() != null 
						&& model.getGeographicLocation() != null
						&& model.getGeographicLocation().getLatitude() != null &&
						!model.getGeographicLocation().getLatitude().equalsIgnoreCase(modelIn.getLatitude().toString()))
						|| (modelIn.getLongitude() != null && model.getGeographicLocation() != null
								&& model.getGeographicLocation().getLongitude() != null && 
								!model.getGeographicLocation().getLongitude().equalsIgnoreCase(modelIn.getLongitude().toString()))) {
					modelIn.setGeocodeConfidence(EnumGeocodeConfidenceType.LOW.getName());
					modelIn.setGeocodeQuality(EnumGeocodeQualityType.USERMANUAL.getName());
				}
				getLocationManagerService().saveEntity(modelIn);
			}
			
		} catch (RoutingServiceException e) {
			errorList.add(this.getMessage("app.actionmessage.120", new Object[]{}));
		}
		return errorList;
	}
	
	private boolean isSameGeocode(DlvBuilding newBuilding, DlvBuilding oldBuilding) {
				
		return (newBuilding != null && oldBuilding != null && newBuilding.getLatitude() != null
				&& newBuilding.getLongitude() != null && oldBuilding.getLatitude() != null && oldBuilding.getLongitude() != null
				&& newBuilding.getLatitude().equals(oldBuilding.getLatitude()) && newBuilding.getLongitude().equals(oldBuilding.getLongitude()));
		
	}
	
	
}
