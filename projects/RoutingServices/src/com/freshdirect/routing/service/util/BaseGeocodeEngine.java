package com.freshdirect.routing.service.util;

import java.util.Iterator;
import java.util.List;

import com.freshdirect.routing.model.GeocodeResult;
import com.freshdirect.routing.model.GeographicLocation;
import com.freshdirect.routing.model.IGeocodeResult;
import com.freshdirect.routing.model.IGeographicLocation;
import com.freshdirect.routing.proxy.stub.roadnet.Address;
import com.freshdirect.routing.proxy.stub.roadnet.GeocodeData;
import com.freshdirect.routing.proxy.stub.roadnet.MapArc;
import com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebService;
import com.freshdirect.routing.service.exception.IIssue;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.impl.BaseService;
import com.freshdirect.routing.util.RoutingDataEncoder;
import com.freshdirect.routing.util.RoutingUtil;

public class BaseGeocodeEngine extends BaseService implements IGeocodeEngine {
	
	public IGeocodeResult getGeocode(String street, String zipCode, String country) throws RoutingServiceException {
		
		IGeocodeResult geocodeResult = new GeocodeResult();
		IGeographicLocation result = new GeographicLocation();
		geocodeResult.setGeographicLocation(result);
		try {

			RouteNetWebService port = getRouteNetBatchService();
			
			Address address = RoutingDataEncoder.encodeAddress(street, zipCode, country); 
						
			//GeocodeOptions options = new GeocodeOptions();
			//options.setReturnCandidates(true);
			//options.setReturnMatchingArc(false);
			
			List zipCodes = RoutingUtil.getZipCodes(zipCode);
			if(zipCodes != null) {
				Iterator iterator = zipCodes.iterator();
				String tmpZipCode = null;
				
				while(iterator.hasNext()) {
					tmpZipCode = (String)iterator.next();
 					address.setPostalCode(tmpZipCode);
					GeocodeData geographicData = port.geocode(address);
					
					if(geographicData != null) {
						result.setLatitude(""+(double)(geographicData.getCoordinate().getLatitude()/1000000.0));
						result.setLongitude(""+(double)(geographicData.getCoordinate().getLongitude()/1000000.0));
						result.setConfidence(geographicData.getConfidence().getValue());
						result.setQuality(geographicData.getQuality().getValue());
						if(RoutingUtil.isGeocodeAcceptable(geographicData.getConfidence().getValue()
							, geographicData.getQuality().getValue())) {
							geocodeResult.setAlternateZipcode(tmpZipCode);
							break;
						}
					}
				}
			}
			/*GeocodeData geographicData = port.geocodeEx(address, options);
			if(geographicData != null && !RoutingUtil.isGeocodeAcceptable(geographicData.getConfidence().getValue()
												, geographicData.getQuality().getValue())) {
				String alternateZipCode = hasMatchingZipCode(geographicData, zipCode);
				if(alternateZipCode != null) {
					address.setPostalCode(alternateZipCode);
					GeocodeData altGeographicData = port.geocode(address);
					if(altGeographicData != null && !RoutingUtil.isGeocodeAcceptable(altGeographicData.getConfidence().getValue()
							, altGeographicData.getQuality().getValue())) {
						geographicData = altGeographicData;	
						geocodeResult.setAlternateZipcode(alternateZipCode);
					}
				}
			}
			result.setLatitude(""+(double)(geographicData.getCoordinate().getLatitude()/1000000.0));
			result.setLongitude(""+(double)(geographicData.getCoordinate().getLongitude()/1000000.0));
			result.setConfidence(geographicData.getConfidence().getValue());
			result.setQuality(geographicData.getQuality().getValue());*/

		} catch (Exception exp) {
			exp.printStackTrace();
			throw new RoutingServiceException(exp, IIssue.PROCESS_GEOCODE_UNSUCCESSFUL);
		}
		return geocodeResult;
		
		
	}
	
	private String hasMatchingZipCode(GeocodeData geographicData, String baseZipCode) {
		String result = null;
		List zipCodes = RoutingUtil.getZipCodes(baseZipCode);
		MapArc[] candidates = geographicData.getCandidates();
		if(zipCodes != null && candidates != null) {
			for(int intCount=0; intCount<candidates.length;intCount++) {
				if(zipCodes.contains(candidates[intCount].getPostalCode())) {
					result = candidates[intCount].getPostalCode();
				}				
			}
		}
		return result;
	}
}
