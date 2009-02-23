package com.freshdirect.transadmin.web.json;

import com.freshdirect.transadmin.web.model.SpatialBoundary;

public interface IGeographyProvider {
	
	SpatialBoundary getGeoRestrictionBoundary(String code);
	SpatialBoundary getZoneBoundary(String code);
}
