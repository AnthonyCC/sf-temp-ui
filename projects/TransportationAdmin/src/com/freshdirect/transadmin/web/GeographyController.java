package com.freshdirect.transadmin.web;


import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.transadmin.model.NeighbourhoodZipcode;
import com.freshdirect.transadmin.model.TrnFacility;
import com.freshdirect.transadmin.model.TrnFacilityType;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.LocationManagerI;
import com.freshdirect.transadmin.service.RestrictionManagerI;
import com.freshdirect.transadmin.service.ZoneManagerI;
import com.freshdirect.transadmin.web.model.SpatialBoundary;
import com.freshdirect.transadmin.web.model.SpatialPoint;

public class GeographyController extends AbstractMultiActionController {
	
	private RestrictionManagerI restrictionManagerService;
	
	private DomainManagerI domainManagerService;
	
	private ZoneManagerI zoneManagerService;

	private LocationManagerI locationManagerService;

	public LocationManagerI getLocationManagerService() {
		return locationManagerService;
	}

	public void setLocationManagerService(LocationManagerI locationManagerService) {
		this.locationManagerService = locationManagerService;
	}

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

		ModelAndView mav = new ModelAndView("geographyView");
		
		String status = request.getParameter("status");
		
		Collection geoRestrictions = new ArrayList();
		Collection geoZones = new ArrayList();
		Collection geoNeighbourhoods = new ArrayList();
		
		geoRestrictions = restrictionManagerService.getGeoRestrictionBoundaries();
		geoNeighbourhoods = domainManagerService.getNeighbourhood();
		geoZones = domainManagerService.getZones();
		
		Collection activeZoneCodes = zoneManagerService.getActiveZoneCodes();
    	if(geoZones != null && activeZoneCodes != null) {
    		Iterator _iterator = geoZones.iterator();
    		Zone _tmpZone = null;
    		while(_iterator.hasNext()) {
    			_tmpZone = (Zone)_iterator.next();
    			if(!activeZoneCodes.contains(_tmpZone.getZoneCode())) {
    				_iterator.remove();
    			}
    		}
    	}	
    	mav.getModel().put("zoneboundaries", geoZones);
		mav.getModel().put("georestrictionboundaries", geoRestrictions);
		mav.getModel().put("geoNeighbourhoods", geoNeighbourhoods);
		return mav;
	}
	
	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView geographicBoundaryExportHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
		String code = request.getParameter("code");
		
		SpatialBoundary boundary = null;
		response.setContentType("application/csv");
		response.setHeader("Content-Disposition", "attachment; filename=exportmap.csv");
		try {
			OutputStream out = response.getOutputStream();
			StringBuffer strBuf = new StringBuffer();
		
			if (code != null && code.trim().length() > 0) {
				String[] splitCodes = code.split(",");	
				for (String _tmpCode : splitCodes) {
					boundary = null;
			        if(_tmpCode.startsWith("$_")) {
			        	boundary = getRestrictionManagerService().getGeoRestrictionBoundary(_tmpCode.substring(2, _tmpCode.length()));
			        } else if(_tmpCode.startsWith("NH_")) {
			        	Map<String, NeighbourhoodZipcode> zipInfo = zoneManagerService.getNeighbourhoodZipCodeInfo(_tmpCode.substring(3, _tmpCode.length()));
			        	if(zipInfo != null && zipInfo.size() > 0)
			        		boundary = getRestrictionManagerService().getNeighbourhoodBoundary(_tmpCode.substring(3, _tmpCode.length()));
			        } else {
			        	boundary = getRestrictionManagerService().getZoneBoundary(_tmpCode);
			        }
			        if(boundary != null) {					
						List points = boundary.getGeoloc();						
						
						if(points != null) {
							int intCount = 0;
							Iterator itr = points.iterator();
							SpatialPoint _point = null;					
							while(itr.hasNext()) {
								_point = (SpatialPoint)itr.next();
								strBuf.append(boundary.getCode()).append(",").append(boundary.getName())
													.append(",").append(_point.getX()).append(",").append(_point.getY())
													.append(",").append(++intCount).append("\n");
							}
							
						}									
					}
			        strBuf.append("").append(",").append("")
					.append(",").append("").append(",").append("")
					.append(",").append("").append("\n");
				}

			}
			out.write(strBuf.toString().getBytes());
			out.flush();			
			
		} catch (IOException e) {
			//Send Empty File
		} 
			
		return null;
	}
	
	/**
	 * Custom handler for facilityView
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView facilityHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		ModelAndView mav = new ModelAndView("facilityView");
		mav.getModel().put("trnFacilitys", getLocationManagerService().getTrnFacilitys());
		mav.getModel().put("trnFacilityTypes", getLocationManagerService().getTrnFacilityTypes());
	
		return mav;
}

	/**
	 * Custom handler for deleteFacilityView
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView deleteFacilityHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			Set<TrnFacility> faciltySet=new HashSet<TrnFacility>();
			String arrEntityList[] = getParamList(request);
			
			if (arrEntityList != null) {
				int arrLength = arrEntityList.length;
				for (int intCount = 0; intCount < arrLength; intCount++) {
					TrnFacility p = getLocationManagerService().getTrnFacility(arrEntityList[intCount]);					
					faciltySet.add(p);					
				}
			}
			if(faciltySet.size() > 0){
				getLocationManagerService().removeEntity(faciltySet);
				saveMessage(request, getMessage("app.actionmessage.103", null));
			}
			return facilityHandler(request,response);
		}catch(Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("Error in getiing Scrib List");
		}
	}

	/**
	 * Custom handler for deleteFacilityView
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView deleteFacilityTypeHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			Set<TrnFacilityType> faciltySet=new HashSet<TrnFacilityType>();
			String arrEntityList[] = getParamList(request);
			
			if (arrEntityList != null) {
				int arrLength = arrEntityList.length;
				for (int intCount = 0; intCount < arrLength; intCount++) {
					TrnFacilityType p = getLocationManagerService().getTrnFacilityType(arrEntityList[intCount]);					
					faciltySet.add(p);
				}
			}
			if(faciltySet.size() > 0){
				getLocationManagerService().removeEntity(faciltySet);
				saveMessage(request, getMessage("app.actionmessage.103", null));
			}
			return facilityHandler(request,response);
		}catch(Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("Error in getiing Scrib List");
		}
	}

	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView neighbourhoodHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
		String _neighbourhood = request.getParameter("nbhood");
				
		ModelAndView mav = new ModelAndView("neighbourhoodView");
		Map<String, NeighbourhoodZipcode> zipInfo = new HashMap<String, NeighbourhoodZipcode>();
		zipInfo = zoneManagerService.getNeighbourhoodZipCodeInfo(_neighbourhood);
		
		mav.getModel().put("neighbourhoodZipInfo", zipInfo.values());
		mav.getModel().put("neighbourhoods", domainManagerService.getNeighbourhood());
		return mav;
	}
}
