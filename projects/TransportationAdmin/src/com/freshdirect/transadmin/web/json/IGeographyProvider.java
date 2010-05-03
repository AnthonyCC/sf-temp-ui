package com.freshdirect.transadmin.web.json;

import java.util.List;

import com.freshdirect.transadmin.web.model.SpatialBoundary;

public interface IGeographyProvider {
	
	SpatialBoundary getGeoRestrictionBoundary(String code);
	SpatialBoundary getZoneBoundary(String code);
	List getBoundaries(String code);
	boolean doZoneExpansion(String worktable, String zone[][], String deliveryFee, String expansionType);
	boolean generateTimeslots(String zone[][], String worktable);
}
