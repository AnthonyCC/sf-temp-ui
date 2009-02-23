package com.freshdirect.transadmin.web.json;

import com.freshdirect.transadmin.service.RestrictionManagerI;
import com.freshdirect.transadmin.web.model.SpatialBoundary;

public class GeographyProviderController extends JsonRpcController  implements IGeographyProvider {
	
	private RestrictionManagerI restrictionManagerService;
	
	public RestrictionManagerI getRestrictionManagerService() {
		return restrictionManagerService;
	}

	public void setRestrictionManagerService(
			RestrictionManagerI restrictionManagerService) {
		this.restrictionManagerService = restrictionManagerService;
	}

	public SpatialBoundary getGeoRestrictionBoundary(String code) {
		// TODO Auto-generated method stub
		return getRestrictionManagerService().getGeoRestrictionBoundary(code);
	}
	
	public SpatialBoundary getZoneBoundary(String code) {
		// TODO Auto-generated method stub
		return getRestrictionManagerService().getZoneBoundary(code);
	}

}
