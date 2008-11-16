package com.freshdirect.routing.service.util;

import com.freshdirect.routing.model.IGeocodeResult;
import com.freshdirect.routing.service.exception.RoutingServiceException;

public interface IGeocodeEngine {
	IGeocodeResult getGeocode(String street, String zipCode, String country) throws RoutingServiceException;
}
