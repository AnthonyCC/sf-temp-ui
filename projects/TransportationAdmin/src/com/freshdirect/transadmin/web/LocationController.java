package com.freshdirect.transadmin.web;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lcc.japi.trans;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.routing.constants.EnumGeocodeConfidenceType;
import com.freshdirect.routing.constants.EnumGeocodeQualityType;
import com.freshdirect.routing.model.IGeographicLocation;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.GeographyServiceProxy;
import com.freshdirect.routing.util.RoutingUtil;
import com.freshdirect.transadmin.model.DlvBuilding;
import com.freshdirect.transadmin.model.DlvBuildingDetail;
import com.freshdirect.transadmin.model.DlvBuildingDtl;
import com.freshdirect.transadmin.model.DlvLocation;
import com.freshdirect.transadmin.model.DlvScenarioDay;
import com.freshdirect.transadmin.model.DlvServiceTime;
import com.freshdirect.transadmin.model.DlvServiceTimeScenario;
import com.freshdirect.transadmin.model.TrnZoneType;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.LocationManagerI;
import com.freshdirect.transadmin.util.ModelUtil;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.model.ServiceTimeCommand;

public class LocationController extends AbstractMultiActionController  {
	
	private LocationManagerI locationManagerService;
	
	private DomainManagerI domainManagerService;
	
	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView welcomeDlvLocationHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
		return new ModelAndView("welcomeLocationView");
	}
	
	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView dlvLocationHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {	
				
		String srubbedAddress = scrubStreet(request.getParameter("srubbedAddress"));
		String zipCode = request.getParameter("zipCode");
		String confidenceLevel = request.getParameter("confidence");
		String qualityLevel = request.getParameter("quality");
		ModelAndView mav = new ModelAndView("deliveryLocationView");
		mav.getModel().put("confidencetypes",locationManagerService.getConfidenceTypes());
		mav.getModel().put("qualitytypes",locationManagerService.getQualityTypes());
		
		if(!TransStringUtil.isEmpty(srubbedAddress) || !TransStringUtil.isEmpty(qualityLevel)
				|| !TransStringUtil.isEmpty(confidenceLevel) || !TransStringUtil.isEmpty(zipCode)) {
			Collection dataList = locationManagerService.getDeliveryLocations(srubbedAddress, null, zipCode, confidenceLevel, qualityLevel);
			mav.getModel().put("dlvlocations",dataList);			
		}
		return mav;
	}
	
	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView dlvLocationDeleteHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
		Set dispatchSet=new HashSet();
		String arrEntityList[] = getParamList(request);
		Object tmpBean = null;
		if (arrEntityList != null) {			
			int arrLength = arrEntityList.length;
			for (int intCount = 0; intCount < arrLength; intCount++) {
				tmpBean = locationManagerService.getDlvLocation(arrEntityList[intCount]);
				if(tmpBean != null) {
					dispatchSet.add(tmpBean);
				}
			}
		}			
		removeEntityList(dispatchSet);
		saveMessage(request, getMessage("app.actionmessage.103", null));

		return dlvLocationHandler(request, response);
	}
	
		
	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView dlvLocationUpdateHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
		Set dispatchSet=new HashSet();
		String arrEntityList[] = getParamList(request);
		DlvLocation tmpBean = null;
		DlvBuilding tmpBuildingBean = null;
		boolean hasError = false;
		String errorKey = "app.actionmessage.121";
		if (arrEntityList != null) {			
			int arrLength = arrEntityList.length;
			GeographyServiceProxy proxy = new GeographyServiceProxy();
			for (int intCount = 0; intCount < arrLength; intCount++) {
				try {
					tmpBean = locationManagerService.getDlvLocation(arrEntityList[intCount]);
					if(tmpBean != null) {
						tmpBuildingBean = tmpBean.getBuilding();
						IGeographicLocation geoLoc = proxy.getRoutingLocation(arrEntityList[intCount]);	
						if(geoLoc == null || tmpBuildingBean == null) {
							hasError = true;
							errorKey = "app.actionmessage.122";
							break;
						} 
						tmpBuildingBean.setLatitude(new BigDecimal(geoLoc.getLatitude()));
						tmpBuildingBean.setLongitude(new BigDecimal(geoLoc.getLongitude()));
						tmpBuildingBean.setGeocodeConfidence(EnumGeocodeConfidenceType.HIGH.getName());
						tmpBuildingBean.setGeocodeQuality(EnumGeocodeQualityType.MANUAL.getName());
						dispatchSet.add(tmpBuildingBean);
					}
				} catch(RoutingServiceException exp) {
					exp.printStackTrace();
					hasError = true;
				}
			}
		}		
		if(hasError) {
			saveMessage(request, getMessage(errorKey, null));
		} else {
			locationManagerService.saveEntityList(dispatchSet);
			saveMessage(request, getMessage("app.actionmessage.117", null));
		}

		return dlvLocationHandler(request, response);
	}
	
	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView dlvLocationSendHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
		Set dispatchSet=new HashSet();
		String arrEntityList[] = getParamList(request);		
		boolean hasError = false;
		String errorKey = "app.actionmessage.121";
		if (arrEntityList != null) {
			GeographyServiceProxy proxy = new GeographyServiceProxy();
			try {
				proxy.sendLocationByIds(Arrays.asList(arrEntityList));
			} catch(RoutingServiceException exp) {
				exp.printStackTrace();
				hasError = true;
				errorKey = "app.actionmessage.128";
			}
		}		
		if(hasError) {
			saveMessage(request, getMessage(errorKey, null));
		} else {
			locationManagerService.saveEntityList(dispatchSet);
			saveMessage(request, getMessage("app.actionmessage.117", null));
		}

		return dlvLocationHandler(request, response);
	}
	
	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView dlvBuildingHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {	
		String srubbedAddress = scrubStreet(request.getParameter("srubbedAddress"));
		String zipCode = request.getParameter("zipCode");
		String confidenceLevel = request.getParameter("confidence");
		String qualityLevel = request.getParameter("quality");
		ModelAndView mav = new ModelAndView("deliveryBuildingView");
		mav.getModel().put("confidencetypes",locationManagerService.getConfidenceTypes());
		mav.getModel().put("qualitytypes",locationManagerService.getQualityTypes());
		
		if(!TransStringUtil.isEmpty(srubbedAddress) || !TransStringUtil.isEmpty(qualityLevel)
				|| !TransStringUtil.isEmpty(confidenceLevel) || !TransStringUtil.isEmpty(zipCode)) {
			Collection dataList = locationManagerService.getDeliveryBuildings(srubbedAddress, zipCode, confidenceLevel, qualityLevel);
			mav.getModel().put("dlvbuildings",dataList);			
		}
		return mav;
	}
	
	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView dlvBuildingDeleteHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
		Set dispatchSet=new HashSet();
		String arrEntityList[] = getParamList(request);
		Object tmpBean = null;
		if (arrEntityList != null) {			
			int arrLength = arrEntityList.length;
			
			for (int intCount = 0; intCount < arrLength; intCount++) {
				tmpBean = locationManagerService.getDlvBuilding(arrEntityList[intCount]);
				if(tmpBean != null) {
					dispatchSet.add(tmpBean);
				}
			}
		}
		try {
			locationManagerService.removeEntity(dispatchSet);
			saveMessage(request, getMessage("app.actionmessage.103", null));
		} catch (DataIntegrityViolationException e) {
			saveMessage(request, getMessage("app.actionmessage.127", null));
		}
		
		return dlvBuildingHandler(request, response);
	}
	
	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView dlvBuildingGeocodeHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
		String arrEntityList[] = getParamList(request);
		Object tmpBean = null;
		List lstLocation = new ArrayList();
		if (arrEntityList != null) {			
			int arrLength = arrEntityList.length;
			for (int intCount = 0; intCount < arrLength; intCount++) {
				tmpBean = locationManagerService.getDlvBuilding(arrEntityList[intCount]);
				if(tmpBean != null) {
					lstLocation.add(tmpBean);
				}
			}
			try {
				GeographyServiceProxy geoProxy = new GeographyServiceProxy();
				List geoResult = geoProxy.batchGeocode(ModelUtil.getDeliveryBuildings(lstLocation));
				updateGeographyBuilding(lstLocation, geoResult);
				locationManagerService.saveEntityList(lstLocation);
				saveMessage(request, getMessage("app.actionmessage.116", null));
			} catch (RoutingServiceException routingServiceException) {
				routingServiceException.printStackTrace();
				saveMessage(request, getMessage("app.actionmessage.118", null));
			}
		}		
			

		return dlvBuildingHandler(request, response);
	}
	
	public static void updateGeographyBuilding(List lstDlvLocation, List geographyLocation) throws RoutingServiceException {
		
		IGeographicLocation locationModel = null;
		DlvBuilding dlvLocationModel = null;
		GeographyServiceProxy proxy = new GeographyServiceProxy();
		if(lstDlvLocation != null) {
			Iterator iterator = lstDlvLocation.iterator();
			int intCount = 0;
			while(iterator.hasNext()) {
				locationModel = (IGeographicLocation)geographyLocation.get(intCount++);
				dlvLocationModel = (DlvBuilding)iterator.next();
				if(RoutingUtil.isGeocodeAcceptable(locationModel.getConfidence(), locationModel.getQuality())) {
					dlvLocationModel.setLatitude(new BigDecimal(locationModel.getLatitude()));
					dlvLocationModel.setLongitude(new BigDecimal(locationModel.getLongitude()));
					dlvLocationModel.setGeocodeConfidence(locationModel.getConfidence());
					dlvLocationModel.setGeocodeQuality(locationModel.getQuality());
				} else {
					IGeographicLocation storeFrontLocationModel = proxy.getLocalGeocode
										(dlvLocationModel.getSrubbedStreet(), null, dlvLocationModel.getZip());
					if(storeFrontLocationModel == null) {
						dlvLocationModel.setLatitude(new BigDecimal(locationModel.getLatitude()));
						dlvLocationModel.setLongitude(new BigDecimal(locationModel.getLongitude()));
						dlvLocationModel.setGeocodeConfidence(EnumGeocodeConfidenceType.LOW.getName());
						dlvLocationModel.setGeocodeQuality(EnumGeocodeQualityType.STOREFRONTUNSUCCESSFULGEOCODE.getName());
					} else {
						dlvLocationModel.setLatitude(new BigDecimal(storeFrontLocationModel.getLatitude()));
						dlvLocationModel.setLongitude(new BigDecimal(storeFrontLocationModel.getLongitude()));
						dlvLocationModel.setGeocodeConfidence(storeFrontLocationModel.getConfidence());
						dlvLocationModel.setGeocodeQuality(storeFrontLocationModel.getQuality());
					}
				}
			}
		}
	}
	
		
	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView dlvServiceTimeTypeHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {		
		
		Collection dataList = locationManagerService.getServiceTimeTypes();
		ModelAndView modelView = new ModelAndView("dlvServiceTimeTypeView","dlvservicetimetypelist",dataList);		
		return modelView;
	}
		
	
	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView dlvServiceTimeTypeDeleteHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
		Set dispatchSet=new HashSet();
		String arrEntityList[] = getParamList(request);
		Object tmpBean = null;
		if (arrEntityList != null) {			
			int arrLength = arrEntityList.length;
			for (int intCount = 0; intCount < arrLength; intCount++) {
				tmpBean = locationManagerService.getServiceTimeType(arrEntityList[intCount]);
				if(tmpBean != null) {
					dispatchSet.add(tmpBean);
				}
			}
		}		
		try {
			removeEntityList(dispatchSet);
			saveMessage(request, getMessage("app.actionmessage.103", null));
		} catch (DataIntegrityViolationException e) {
			saveMessage(request, getMessage("app.actionmessage.127", null));
		}
		
		return dlvServiceTimeTypeHandler(request, response);
	}
	
	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView dlvServiceTimeScenarioHandler(HttpServletRequest request, HttpServletResponse response) 
									throws ServletException {		
		
		Set scenarios=new HashSet(0);
		try{	
			for(int i=1;i<=7;i++){
				Calendar baseDate = DateUtil.truncate(Calendar.getInstance());					
				baseDate.add(Calendar.DATE, i);
				String date = TransStringUtil.dateFormat.format(baseDate.getTime());
				
				Date tmpDate=null;
				try {
					tmpDate = TransStringUtil.getDate(date);
					Calendar calendar=Calendar.getInstance();
					calendar.setTime(tmpDate);
					int dayOfweek = calendar.get(Calendar.DAY_OF_WEEK);
					Set dataList=new HashSet(0);
					if(date!=null){
						Collection scenariosForDate = locationManagerService.getServiceTimeScenarios(date);
						for(Iterator itr=scenariosForDate.iterator();itr.hasNext();){
							Object[] object = (Object[])itr.next();
							if(null != object && object.length > 0){
								DlvScenarioDay scenario=(DlvScenarioDay)object[1];
								if(scenario!=null)
									dataList.add(scenario);
							}
						}
						if(dataList.isEmpty()){
							Collection scenariosForDayOfWeek = locationManagerService.getServiceTimeScenariosForDayofWeek(dayOfweek);
								for(Iterator itr=scenariosForDayOfWeek.iterator();itr.hasNext();){
									Object[] object = (Object[])itr.next();
									if(null != object && object.length > 0){
										DlvScenarioDay scenario=(DlvScenarioDay)object[1];
										if(scenario!=null) dataList.add(scenario);
									}
								}							
						}									
					}					
					scenarios.addAll(dataList);
					
					if(scenarios.isEmpty()){
						Collection defaultScenario = locationManagerService.getDefaultServiceTimeScenarioDay();
							DlvScenarioDay sd=null;
							for(Iterator itr=defaultScenario.iterator();itr.hasNext();){
								DlvServiceTimeScenario scenario=(DlvServiceTimeScenario)itr.next();
								if(scenario!=null){
									sd=new DlvScenarioDay();
									sd.setScenario(scenario);
									scenarios.add(sd);
								}
							}				
					}	
				} catch (ParseException e) {
					e.printStackTrace();
				}		
			}			
			return new ModelAndView("dlvServiceTimeScenarioView","dlvservicetimescenariolist",scenarios);
		}catch (Exception e) {
			e.printStackTrace();
			saveMessage(request, getMessage("app.actionmessage.154", null));
			return new ModelAndView("dlvServiceTimeScenarioView");	
		}	
	}


	public ModelAndView dlvServiceTimeScenarioDisplayHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {		
		try{
			String startDate = request.getParameter("fromdaterange");
			String endDate = request.getParameter("todaterange");
			Collection scenarios=new ArrayList();
			Collection scenariosWithNoDays= new ArrayList();
			if(("".equals(startDate)||startDate==null)&&(endDate==null||"".equals(endDate))){
				scenarios  = locationManagerService.getDlvServiceTimeScenarioDays();
			    scenariosWithNoDays = locationManagerService.getScenariosWithNoDay();		
					
				DlvScenarioDay sd=null;
				for(Iterator itr=scenariosWithNoDays.iterator();itr.hasNext();){
					DlvServiceTimeScenario scenario=(DlvServiceTimeScenario)itr.next();
					sd=new DlvScenarioDay();
					sd.setScenario(scenario);
					scenarios.add(sd);
				}
			}else{
				
				Date d1=TransStringUtil.getDate(startDate);				
				Date d2=TransStringUtil.getDate(endDate);
				Calendar cal1 = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				cal1.setTime(d1);
				cal2.setTime(d2);
	            String date="";
	            Date tempDate=null;
				while(cal1.before(cal2)){
					try {
					
						date = TransStringUtil.dateFormat.format(cal1.getTime());
						
						tempDate=TransStringUtil.getDate(date);
						Calendar calendar=Calendar.getInstance();
						calendar.setTime(tempDate);
						int dayOfweek = calendar.get(Calendar.DAY_OF_WEEK);
						
						Set dataList=new HashSet(0);
						if(date!=null){
							Collection scenariosForDate = locationManagerService.getServiceTimeScenarios(date);
							for(Iterator itr=scenariosForDate.iterator();itr.hasNext();){
								Object[] object = (Object[])itr.next();
								if(null != object && object.length > 0){
									DlvScenarioDay scenario=(DlvScenarioDay)object[1];
									if(scenario!=null)
										dataList.add(scenario);
								}
							}
							if(dataList.isEmpty()){
								Collection scenariosForDayOfWeek = locationManagerService.getServiceTimeScenariosForDayofWeek(dayOfweek);
									for(Iterator itr=scenariosForDayOfWeek.iterator();itr.hasNext();){
										Object[] object = (Object[])itr.next();
										if(null != object && object.length > 0){
											DlvScenarioDay scenario=(DlvScenarioDay)object[1];
											if(scenario!=null) dataList.add(scenario);
										}
									}							
							}									
						}							
						scenarios.addAll(dataList);						
					} catch (ParseException e) {
						e.printStackTrace();
					} catch (Exception ex) {
						ex.printStackTrace();
					}				
					//TODO: Look for next day if any.
					cal1.add(Calendar.DATE, 1);				
				}
				if(scenarios.isEmpty()){
					Collection defaultScenario = locationManagerService.getDefaultServiceTimeScenarioDay();
						DlvScenarioDay sd=null;
						for(Iterator itr=defaultScenario.iterator();itr.hasNext();){
							DlvServiceTimeScenario scenario=(DlvServiceTimeScenario)itr.next();
							if(scenario!=null){
								sd=new DlvScenarioDay();
								sd.setScenario(scenario);
								scenarios.add(sd);
							}
						}				
				}			
			}						
			return new ModelAndView("dlvServiceTimeScenarioView","dlvservicetimescenariolist",scenarios);
		}catch (Exception e) {
			e.printStackTrace();
			saveMessage(request, getMessage("app.actionmessage.154", null));
			return new ModelAndView("dlvServiceTimeScenarioView");	
		}		
	}
		
	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView dlvBuildingLocationHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {		
		
		String id = request.getParameter("id");
		Collection dataList = new ArrayList();
		if(!StringUtil.isEmpty(id)) {
			dataList = locationManagerService.getDeliveryLocations(id);
		}
		return new ModelAndView("deliveryBuildingLocationView","dlvlocationlist",dataList);
	}
	
	
	
	
	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView dlvServiceTimeScenarioDeleteHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
		Set scenarioSet=new HashSet();
		String arrEntityList[] = getParamList(request);
		/*DlvServiceTimeScenario tmpBean = null;
		if (arrEntityList != null) {			
			int arrLength = arrEntityList.length;
			for (int intCount = 0; intCount < arrLength; intCount++) {
				tmpBean = locationManagerService.getServiceTimeScenario(arrEntityList[intCount]);
				if(tmpBean != null ) {
					if("X".equalsIgnoreCase(tmpBean.getIsDefault())) {
						saveMessage(request, getMessage("app.actionmessage.132", null));
						return dlvServiceTimeScenarioHandler(request, response);
					}
					scenarioSet.add(tmpBean);
				}
			}
		}*/
		DlvScenarioDay tmpBean=null;
		if(arrEntityList!=null){
			int arrLength=arrEntityList.length;
			for (int intCount = 0; intCount < arrLength; intCount++) {
				tmpBean= locationManagerService.getServiceTimeScenarioDay(arrEntityList[intCount]);
				if(tmpBean != null ) {
					if(tmpBean.getNormalDate()!=null || tmpBean.getDayOfWeek()!=null){
						scenarioSet.add(tmpBean);
					}else{
						saveMessage(request, getMessage("app.actionmessage.132", null));
						return dlvServiceTimeScenarioHandler(request, response);
					}
				}
			}
		}
		removeEntityList(scenarioSet);
		saveMessage(request, getMessage("app.actionmessage.103", null));

		return dlvServiceTimeScenarioHandler(request, response);
	}
	
	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView dlvServiceTimeHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {		
		
		Collection dataList = locationManagerService.getServiceTimes();
		List commandList = new ArrayList();
				
		Map zoneTypeMap = new HashMap();
		Collection zoneLst = domainManagerService.getZoneTypes();
		if(zoneLst != null) {
			Iterator iterator = zoneLst.iterator();
			TrnZoneType type = null;
			while(iterator.hasNext()) {
				type = (TrnZoneType)iterator.next();
				zoneTypeMap.put(type.getZoneTypeId(), type.getName());
			}
		}
		if(dataList != null) {
			Iterator iterator = dataList.iterator();
			DlvServiceTime serviceTime = null;
			ServiceTimeCommand tmpCommand = null;
			while(iterator.hasNext()) {
				serviceTime = (DlvServiceTime)iterator.next();
				tmpCommand = new ServiceTimeCommand(serviceTime);
				tmpCommand.setZoneTypeName((String)zoneTypeMap.get(tmpCommand.getZoneType()));
				commandList.add(tmpCommand);
			}
		}		
		ModelAndView modelView = new ModelAndView("dlvServiceTimeView","dlvservicetimelist",commandList);
		//modelView.addObject("referencemapping", zoneTypeMap);
		return modelView;
	}
	
	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView dlvServiceTimeDeleteHandler(HttpServletRequest request, HttpServletResponse response) 
								throws ServletException, ParseException {
		
		Set dispatchSet=new HashSet();
		String arrEntityList[] = getParamList(request);
		StringTokenizer splitter = null;
		Object tmpBean = null;
		if (arrEntityList != null) {			
			int arrLength = arrEntityList.length;
			for (int intCount = 0; intCount < arrLength; intCount++) {
				splitter = new StringTokenizer(arrEntityList[intCount], "$p$g");
				tmpBean = locationManagerService.getServiceTime(splitter.nextToken(), splitter.nextToken());
				if(tmpBean != null) {
					dispatchSet.add(tmpBean);
				}
			}
		}		
		//locationManagerService.removeEntity(dispatchSet);
		removeEntityList(dispatchSet);
		saveMessage(request, getMessage("app.actionmessage.103", null));

		return dlvServiceTimeHandler(request, response);
	}
		

	public LocationManagerI getLocationManagerService() {
		return locationManagerService;
	}

	public void setLocationManagerService(LocationManagerI locationManagerService) {
		this.locationManagerService = locationManagerService;
	}
	
	private void removeEntityList(Set dataLst) {
		if(dataLst != null && dataLst.size() > 0) {
			locationManagerService.removeEntity(dataLst);
		}
	}
	
	private String scrubStreet(String strStreet) {
		
		GeographyServiceProxy proxy = new GeographyServiceProxy();
		if(!TransStringUtil.isEmpty(strStreet)) {
			try {				
				return proxy.standardizeStreetAddress(strStreet, null);
			} catch(RoutingServiceException exp) {
				//Dont have to handle the exceptio its just used for filter
			}
		}
		return strStreet;
	}

	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}
    
    	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView dlvBuildingDetailHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {	
		String srubbedAddress = scrubStreet(request.getParameter("srubbedAddress"));
		String zipCode = request.getParameter("zipCode");		
		ModelAndView mav = new ModelAndView("deliveryBuildingDtlView");
		
		
		if(!TransStringUtil.isEmpty(srubbedAddress) || !TransStringUtil.isEmpty(zipCode)) {
			Collection dataList = locationManagerService.getDeliveryBuildingDetails(srubbedAddress, zipCode);
			
/*
 * 			Iterator iterator = zoneLst.iterator();
			TrnZoneType type = null;
			while(iterator.hasNext()) {
				type = (TrnZoneType)iterator.next();
				zoneTypeMap.put(type.getId(), type.getName());
			}

 */			
			
			Iterator iterator = dataList.iterator();
			DlvBuildingDetail type = null;
			while(iterator.hasNext()) {
				type = (DlvBuildingDetail)iterator.next();
				type.setDoorman( type.getDoorman() != null && type.getDoorman().equals("1")  ? "Yes" : "No");
				type.setWalkup( type.getWalkup() != null && type.getWalkup().equals("1")  ? "Yes" : "No");
				type.setElevator( type.getElevator() != null && type.getElevator().equals("1")  ? "Yes" : "No");
				type.setSvcEnt( type.getSvcEnt() != null && type.getSvcEnt().equals("1")  ? "Yes" : "No");
				type.setHouse( type.getHouse() != null && type.getHouse().equals("1")  ? "Yes" : "No");
				type.setFreightElevator( type.getFreightElevator() != null && type.getFreightElevator().equals("1")  ? "Yes" : "No");
				type.setDifficultToDeliver( type.getDifficultToDeliver() != null && type.getDifficultToDeliver().equals("1")  ? "Yes" : "No");
			}
		
			mav.getModel().put("dlvbuildingdtl",dataList);			
		}
		return mav;
	}
	
	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView dlvBuildingDtlDeleteHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
		Set dispatchSet=new HashSet();
		String arrEntityList[] = getParamList(request);
		Object tmpBean = null;
		if (arrEntityList != null) {			
			int arrLength = arrEntityList.length;
			
			for (int intCount = 0; intCount < arrLength; intCount++) {
				tmpBean = locationManagerService.getDlvBuildingDtl(arrEntityList[intCount]);
				if(tmpBean != null) {
					dispatchSet.add(tmpBean);
				}
			}
		}
		try {
			locationManagerService.removeEntity(dispatchSet);
			saveMessage(request, getMessage("app.actionmessage.103", null));
		} catch (DataIntegrityViolationException e) {
			saveMessage(request, getMessage("app.actionmessage.127", null));
		}
		
		return dlvBuildingDetailHandler(request, response);
	}
}
