package com.freshdirect.transadmin.web;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.RoutingEngineServiceProxy;
import com.freshdirect.transadmin.datamanager.model.WorkTableModel;
import com.freshdirect.transadmin.datamanager.model.ZoneWorktableModel;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.LocationManagerI;
import com.freshdirect.transadmin.service.LogManagerI;
import com.freshdirect.transadmin.service.ZoneManagerI;
import com.freshdirect.transadmin.util.TransStringUtil;

public class AdminController extends AbstractMultiActionController {
		
	private DomainManagerI domainManagerService;

	private LogManagerI logManager;
	
	private LocationManagerI locationManagerService;
	
	private ZoneManagerI zoneManagerService;
	
	public ZoneManagerI getZoneManagerService() {
		return zoneManagerService;
	}

	public void setZoneManagerService(ZoneManagerI zoneManagerService) {
		this.zoneManagerService = zoneManagerService;
	}	
	
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

	public LogManagerI getLogManager() {
		return logManager;
	}

	public void setLogManager(LogManagerI logManager) {
		this.logManager = logManager;
	}
	
	
	/**
	 * Custom handler for early warning
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView timeslotLogHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
		ModelAndView mav = new ModelAndView("timeslotLogView");
		
		try {
			String rDate = request.getParameter("rDate");
			String sTime = request.getParameter("sTime");
			String eTime = request.getParameter("eTime");
			//String orderId = request.getParameter("orderId");
			//String customerId = request.getParameter("custId");
			
			if(!TransStringUtil.isEmpty(rDate)
											&& !TransStringUtil.isEmpty(sTime)
											&& !TransStringUtil.isEmpty(eTime)) {
				Date fromDate = TransStringUtil.getDatewithTime(rDate+" "+sTime);
				Date toDate = TransStringUtil.getDatewithTime(rDate+" "+eTime);
				
				fromDate = new Timestamp(fromDate.getTime());
				toDate = new Timestamp(toDate.getTime());
				Collection list = logManager.getTimeSlotLogs(fromDate, toDate);
				mav.getModel().put("timeslotlogs", list );
			}
			
			if(TransStringUtil.isEmpty(rDate)) {
				rDate = TransStringUtil.getCurrentDate();
			}
			mav.getModel().put("rDate", rDate);
			mav.getModel().put("sTime", sTime);
			mav.getModel().put("eTime", eTime);
			//mav.getModel().put("orderId", orderId);
			//mav.getModel().put("customerId", customerId);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mav;
	}
	
	/**
	 * Custom handler for early warning
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView notificationHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
		ModelAndView mav = new ModelAndView("notificationView");
		
		try {
			RoutingEngineServiceProxy proxy = new RoutingEngineServiceProxy();
			mav.getModel().put("notifications", proxy.retrieveNotifications());
			
		} catch (RoutingServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mav;
	}
	
	/**
	 * Custom handler for early warning
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView geographyRestrictionsHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
		ModelAndView mav = new ModelAndView("geographyRestrictionsView");
		
		try {
			//Environment
			String url=request.getRequestURL().toString();
		    if(url.indexOf("dev")>-1 || url.indexOf("stg")>-1 || url.indexOf("trn")>-1){
		    	request.setAttribute("rightEnvironment", true);
		    	String envName="";
		    	if(url.indexOf("dev")>-1){
		    		envName = "DEV";
		    	}else if(url.indexOf("stg")>-1){
		    		envName = "STAGE";
		    	}else if(url.indexOf("trn")>-1){
		    		envName = "PROD";
		    	}
		    	request.setAttribute("environment", envName);
		    }
		    
		    
		    if(request.getAttribute("environment")!=null && ("DEV".equals((String)request.getAttribute("environment"))||"PROD".equals((String)request.getAttribute("environment")))){
				domainManagerService.refreshGeoRestrictionWorktable();
			}
		    // validate for Geo-restriction polygons.
			//if errors create a view and return
			List<WorkTableModel> dataList = new ArrayList();
			dataList = (List) domainManagerService.checkGeoRestrictionPolygons();
			if (dataList == null || dataList.isEmpty()) {
				saveMessage(request, "Drawn Geo-restriction Polygons are OK");
			}

			if (!dataList.isEmpty()) {
				StringBuffer str = new StringBuffer();
				int intCount = 0;
				for (WorkTableModel model : dataList) {
					if (model != null)
						str = str.append(model.getCode());
					intCount++;
					if(intCount != dataList.size()) {
						str.append(",");
					}
				}
				saveMessage(request,
						"Drawn Geo-restriction Polygons as validation errors. Please check geo-restriction(s) {"
								+ str + "} before proceeding.");
			}
		    
		    Collection zoneList= domainManagerService.getGeoRestrictions();
			mav.getModel().put("zones", zoneList);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mav;
	}
	
	public ModelAndView zipcodeHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
		ModelAndView mav = new ModelAndView("zipCodeView");
		Set zipCodes = null;
		String zipCode = request.getParameter("ec_f_zipcode");
		if(zipCode != null && !"".equalsIgnoreCase(zipCode)){
			zipCodes = zoneManagerService.getZipCodeInfo(zipCode);
		}else{
			zipCodes = zoneManagerService.getDeliverableZipCodes();
		}
		mav.getModel().put("zipcodes", zipCodes );
		return mav;
	}
}
