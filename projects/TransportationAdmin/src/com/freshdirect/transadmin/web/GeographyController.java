package com.freshdirect.transadmin.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.RestrictionManagerI;
import com.freshdirect.transadmin.service.ZoneManagerI;

public class GeographyController extends AbstractMultiActionController {
	
	private RestrictionManagerI restrictionManagerService;
	
	private DomainManagerI domainManagerService;
	
	private ZoneManagerI zoneManagerService;

	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}

	public RestrictionManagerI getRestrictionManagerService() {
		return restrictionManagerService;
	}

	public void setRestrictionManagerService(
			RestrictionManagerI restrictionManagerService) {
		this.restrictionManagerService = restrictionManagerService;
	}

	public ZoneManagerI getZoneManagerService() {
		return zoneManagerService;
	}

	public void setZoneManagerService(ZoneManagerI zoneManagerService) {
		this.zoneManagerService = zoneManagerService;
	}
	
	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView geographicBoundaryHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		String type = request.getParameter("type");
		Collection dataList = new ArrayList();
		if("georestriction".equalsIgnoreCase(type)) {
			dataList = restrictionManagerService.getGeoRestrictionBoundaries();
		} else {
			dataList = domainManagerService.getZones();
			Collection activeZoneCodes = zoneManagerService.getActiveZoneCodes();
        	if(dataList != null && activeZoneCodes != null) {
        		Iterator _iterator = dataList.iterator();
        		Zone _tmpZone = null;
        		while(_iterator.hasNext()) {
        			_tmpZone = (Zone)_iterator.next();
        			if(!activeZoneCodes.contains(_tmpZone.getZoneCode())) {
        				_iterator.remove();
        			}
        		}
        	}			
		}
		
		return new ModelAndView("geographyView","boundaries",dataList);
	}
}
