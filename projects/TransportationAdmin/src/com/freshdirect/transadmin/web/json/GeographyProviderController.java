package com.freshdirect.transadmin.web.json;

import java.util.ArrayList;
import java.util.List;

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
	
	public List getBoundaries(String code) {
		List result = new ArrayList<SpatialBoundary>();
		SpatialBoundary boundary = null;
		if (code != null && code.trim().length() > 0) {
			String[] splitCodes = code.split(",");	
			for (String _tmpCode : splitCodes) {
		        if(_tmpCode.startsWith("$_")) {
		        	boundary = getRestrictionManagerService().getGeoRestrictionBoundary(_tmpCode.substring(2, _tmpCode.length()));		        	
		        } else{
		        	boundary = getRestrictionManagerService().getZoneBoundary(_tmpCode);
		        	boundary.setZone(true);
		        }
		        result.add(boundary);
			}

		}
		return result;
	}

}
