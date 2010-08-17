package com.freshdirect.routing.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.routing.constants.EnumGeocodeConfidenceType;
import com.freshdirect.routing.model.IAreaModel;
import com.freshdirect.routing.proxy.stub.transportation.Notification;
import com.freshdirect.routing.proxy.stub.transportation.NotificationCriteria;
import com.freshdirect.routing.proxy.stub.transportation.NotificationRetrieveOptions;
import com.freshdirect.routing.proxy.stub.transportation.RecipientIdentity;
import com.freshdirect.routing.proxy.stub.transportation.SchedulerOrdersCanceledNotification;
import com.freshdirect.routing.proxy.stub.transportation.TransportationWebService;
import com.freshdirect.routing.service.RoutingServiceLocator;
import com.freshdirect.routing.service.exception.RoutingServiceException;

public class RoutingUtil {
	
	
	public static String standardizeStreetAddress(String s1, String s2) {
		String streetAddressResult = null;
		//String oldStreetAddress = address.getStreetAddress1();
		try {
			streetAddressResult = AddressScrubber.standardizeForGeocode(s1);
			//streetAddress = AddressScrubber.standardizeForGeocode(address.getAddress1());
		} catch (RoutingServiceException iae1) {
			try {
				streetAddressResult = AddressScrubber.standardizeForGeocode(s2);
			} catch (RoutingServiceException iae2) {
				iae2.printStackTrace();
			}
		}
		return streetAddressResult;
	}
	
	public static boolean isGeocodeAcceptable(String confidence, String quality) {
		return EnumGeocodeConfidenceType.HIGH.getName().equals(confidence);
	}
	
	public static List getZipCodes(String zipCode) {
		
		if(zipCode != null && zipCode.trim().length() != 0) {
			StringBuffer tmpBufZipCode = new StringBuffer();
			tmpBufZipCode.append(zipCode);
			String tmpZipCode = FDStoreProperties.getAlternateZipcodeForGeocode(zipCode);
	    	if(!StringUtil.isEmpty(tmpZipCode)) {
	    		tmpBufZipCode.append(",").append(tmpZipCode);
	    	}
	    	return Arrays.asList(StringUtil.decodeStrings(tmpBufZipCode.toString()));	    	
		} 
		return null;    	
	}
	
	public static String getQueryParam(List lstZipCode) {
		
		if(lstZipCode != null) {			
	    	if(lstZipCode.size() > 1) {
	    		Iterator iterator = lstZipCode.iterator();
	    		int intCount = 0;
	    		StringBuffer strBuf = new StringBuffer();	    		
	        	while(iterator.hasNext()) {
	        		intCount++;
	        		strBuf.append("'").append(iterator.next()).append("'");
	        		if(intCount != lstZipCode.size()) {
	        			strBuf.append(",");
	        		}
	        	}
	        	return "in ("+strBuf.toString()+")";
	    	} else if (lstZipCode.size() == 1){
	    		return "= '"+lstZipCode.get(0)+"'";
	    	}
		} 
		return null;
    	
	}
	
	public static String getRegion(IAreaModel model) {
		if(model != null && model.isDepot()) {
			return RoutingServicesProperties.getDefaultDepotRegion();
		} else {
			return RoutingServicesProperties.getDefaultTruckRegion();
		}
	}
	
	public static double getDouble(String input) {
		try {
			return Double.parseDouble(input);
		} catch (NumberFormatException e) {
			return 0.0;
		}
	}
	
	public static String[] toStringArray(List<String> source) {
		if(source != null) {
			String[] result = new String[source.size()];
			int intCount = 0;
			for(String unit : source) {
				result[intCount++] = unit;
			}
			return result;
		}
		return null;
	}
	
}
