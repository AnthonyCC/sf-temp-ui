package com.freshdirect.transadmin.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.routing.model.IGeographicLocation;
import com.freshdirect.routing.model.ILocationModel;
import com.freshdirect.routing.model.LocationModel;
import com.freshdirect.transadmin.model.DlvBuilding;
import com.freshdirect.transadmin.model.DlvLocation;

public class ModelUtil {
	
	public static List getDeliveryLocations(List lstDlvLocation) {
		
		List result = new ArrayList();
		ILocationModel locationModel = null;
		DlvLocation dlvLocationModel = null;
		if(lstDlvLocation != null) {
			Iterator iterator = lstDlvLocation.iterator();
			while(iterator.hasNext()) {
				locationModel = new LocationModel();
				dlvLocationModel = (DlvLocation)iterator.next();
				locationModel.setApartmentNumber(dlvLocationModel.getApartment());
				locationModel.setCity(dlvLocationModel.getBuilding().getCity());		
				locationModel.setState(dlvLocationModel.getBuilding().getState());		
				locationModel.setStreetAddress1(dlvLocationModel.getBuilding().getSrubbedStreet());
				locationModel.setStreetAddress2(dlvLocationModel.getBuilding().getSrubbedStreet());
				locationModel.setZipCode(dlvLocationModel.getBuilding().getZip());
				locationModel.setCountry(dlvLocationModel.getBuilding().getCountry());
				result.add(locationModel);
			}
		}
		return result;
	}
	
	public static void updateGeographyLocation(List lstDlvLocation, List geographyLocation) {
				
		IGeographicLocation locationModel = null;
		DlvLocation dlvLocationModel = null;
		if(lstDlvLocation != null) {
			Iterator iterator = lstDlvLocation.iterator();
			int intCount = 0;
			while(iterator.hasNext()) {
				locationModel = (IGeographicLocation)geographyLocation.get(intCount++);
				dlvLocationModel = (DlvLocation)iterator.next();
				dlvLocationModel.setLatitude(new BigDecimal(locationModel.getLatitude()));
				dlvLocationModel.setLongitude(new BigDecimal(locationModel.getLongitude()));
				dlvLocationModel.setGeocodeConfidence(locationModel.getConfidence());
				dlvLocationModel.setGeocodeQuality(locationModel.getQuality());
			}
		}
	}
	
	public static List getDeliveryBuildings(List lstDlvLocation) {
		
		List result = new ArrayList();
		ILocationModel locationModel = null;
		DlvBuilding dlvLocationModel = null;
		if(lstDlvLocation != null) {
			Iterator iterator = lstDlvLocation.iterator();
			while(iterator.hasNext()) {
				locationModel = new LocationModel();
				dlvLocationModel = (DlvBuilding)iterator.next();
					
				locationModel.setStreetAddress1(dlvLocationModel.getSrubbedStreet());
				locationModel.setStreetAddress2(dlvLocationModel.getSrubbedStreet());
				locationModel.setZipCode(dlvLocationModel.getZip());
				locationModel.setCountry(dlvLocationModel.getCountry());
				result.add(locationModel);
			}
		}
		return result;
	}
	
	

}
