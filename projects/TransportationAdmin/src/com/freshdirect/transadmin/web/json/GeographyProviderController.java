package com.freshdirect.transadmin.web.json;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.RestrictionManagerI;
import com.freshdirect.transadmin.web.model.SpatialBoundary;
import com.freshdirect.transadmin.web.util.ZoneWorkTableUtil;

public class GeographyProviderController extends JsonRpcController  implements IGeographyProvider {
	
	private RestrictionManagerI restrictionManagerService;
	
	private DomainManagerI domainManagerService;
	
	public RestrictionManagerI getRestrictionManagerService() {
		return restrictionManagerService;
	}

	public void setRestrictionManagerService(
			RestrictionManagerI restrictionManagerService) {
		this.restrictionManagerService = restrictionManagerService;
	}
	
	
	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
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
	
	public boolean doZoneExpansion(String worktable, String zone[][], String deliveryFee, String expansionType) {
		
		String regionId=ZoneWorkTableUtil.getRegionId(worktable);
		try{
			domainManagerService.doZoneExpansion(worktable, zone, regionId, deliveryFee, expansionType);
			//saveMessage(request, getMessage("app.actionmessage.153", new Object[]{}));
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean generateTimeslots(String zone[][]) {
		try{
			domainManagerService.rollbackTimeslots(zone);
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
		return true;
	}

}
