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
import com.freshdirect.transadmin.model.DlvLocation;
import com.freshdirect.transadmin.model.DlvScenarioDay;
import com.freshdirect.transadmin.model.DlvServiceTimeScenario;
import com.freshdirect.transadmin.model.DlvServiceTimeType;
import com.freshdirect.transadmin.model.TrnZoneType;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.LocationManagerI;
import com.freshdirect.transadmin.util.ModelUtil;
import com.freshdirect.transadmin.util.TransStringUtil;

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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ModelAndView dlvLocationHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {	
				
		String srubbedAddress = scrubStreet(request.getParameter("srubbedAddress"));
		String zipCode = request.getParameter("zipCode");
		String confidenceLevel = request.getParameter("confidence");
		String qualityLevel = request.getParameter("quality");
		ModelAndView mav = new ModelAndView("deliveryLocationView");
		
		mav.getModel().put("srubbedAddress", srubbedAddress);
		mav.getModel().put("zipCode", zipCode);
		mav.getModel().put("confidence", confidenceLevel);
		mav.getModel().put("quality", qualityLevel);
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ModelAndView dlvBuildingHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {	
		
		String srubbedAddress = scrubStreet(request.getParameter("srubbedAddress"));
		String zipCode = request.getParameter("zipCode");
		String confidenceLevel = request.getParameter("confidence");
		String qualityLevel = request.getParameter("quality");
		String group = request.getParameter("group");
		
		ModelAndView mav = new ModelAndView("deliveryBuildingView");
		
		mav.getModel().put("srubbedAddress", srubbedAddress);
		mav.getModel().put("zipCode", zipCode);
		mav.getModel().put("confidence", confidenceLevel);
		mav.getModel().put("quality", qualityLevel);
		mav.getModel().put("group", group);
		mav.getModel().put("confidencetypes",locationManagerService.getConfidenceTypes());
		mav.getModel().put("qualitytypes",locationManagerService.getQualityTypes());
		mav.getModel().put("deliveryGroups",domainManagerService.getDeliveryGroups());
		
		if(!TransStringUtil.isEmpty(srubbedAddress) || !TransStringUtil.isEmpty(qualityLevel)
				|| !TransStringUtil.isEmpty(confidenceLevel) || !TransStringUtil.isEmpty(zipCode)|| !TransStringUtil.isEmpty(group)) {
			Collection dataList = locationManagerService.getDeliveryBuildings(srubbedAddress, zipCode, confidenceLevel, qualityLevel, group);
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

			Set<DlvServiceTimeType> dispatchSet=new HashSet();
			String arrEntityList[] = getParamList(request);
			Map constriantMap=new HashMap();
			Collection zoneLst = locationManagerService.getZonesForServiceTimeTypes(Arrays.asList(arrEntityList));
			
			DlvServiceTimeType tmpEntity = null;
			boolean hasError = false;
			if (arrEntityList != null && (zoneLst==null || zoneLst.size()==0)) {
				int arrLength = arrEntityList.length;
				for (int intCount = 0; intCount < arrLength; intCount++) {
					tmpEntity = locationManagerService.getServiceTimeType(arrEntityList[intCount]);
					dispatchSet.add(tmpEntity);
				}
				try {
					domainManagerService.removeEntity(dispatchSet);
					saveMessage(request, getMessage("app.actionmessage.103", null));
				} catch (DataIntegrityViolationException ex) {			
					
					String expmsg=ex.getCause().getCause().toString();
					if(expmsg.indexOf("DLV.ACT_BUILDING_STTYPE_FK")>-1)
						constriantMap.put("BUILDING", "DLV.ACT_BUILDING_STTYPE_FK");
					if(expmsg.indexOf("DLV.ACT_DELIVERY_LOCATIONST_FK")>-1)
						constriantMap.put("LOCATION", "ACT_DELIVERY_LOCATIONST_FK");
					if(expmsg.indexOf("ACT_SERVICETIMETYPE_ZONEST_FK")>-1)
						constriantMap.put("SCENARIO ZONES", "ACT_SERVICETIMETYPE_ZONEST_FK");
					hasError = true;
				}
			} else {
				hasError = true;
			}
			if(hasError) {
				saveMessage(request, getMessage("app.actionmessage.154", new Object[]{constriantMap.keySet()}));
			}
		return dlvServiceTimeTypeHandler(request, response);			
	}
	
	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ModelAndView dlvServiceTimeScenarioHandler(HttpServletRequest request, HttpServletResponse response) 
									throws ServletException {
		
		String scenarioView = request.getParameter("displayView");
		String startDate = request.getParameter("fromdaterange");
		String endDate = request.getParameter("todaterange");
		
		Set<DlvScenarioDay> scenarios = new HashSet<DlvScenarioDay>();
		try {
			if(scenarioView == null || "".equalsIgnoreCase(scenarioView)) {
					
				for (int i = 1; i <= 7; i++) {
					Calendar baseDate = DateUtil.truncate(Calendar.getInstance());
					baseDate.add(Calendar.DATE, i);
					String date = TransStringUtil.dateFormat.format(baseDate.getTime());

					Date tmpDate = null;
					try {
						tmpDate = TransStringUtil.getDate(date);
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(tmpDate);
						int dayOfweek = calendar.get(Calendar.DAY_OF_WEEK);
						Set<DlvScenarioDay> dataList = new HashSet<DlvScenarioDay>();
						if (date != null) {
							Collection scenariosForDate = locationManagerService.getServiceTimeScenarios(date);
							for (Iterator itr = scenariosForDate.iterator(); itr.hasNext();) {
								Object[] object = (Object[]) itr.next();
								if (null != object && object.length > 0) {
									DlvScenarioDay scenario = (DlvScenarioDay) object[1];
									if (scenario != null)
										dataList.add(scenario);
								}
							}
							if (dataList.isEmpty()) {
								Collection scenariosForDayOfWeek = locationManagerService.getServiceTimeScenariosForDayofWeek(dayOfweek);
								for (Iterator itr = scenariosForDayOfWeek.iterator(); itr.hasNext();) {
									Object[] object = (Object[]) itr.next();
									if (null != object && object.length > 0) {
										DlvScenarioDay scenario = (DlvScenarioDay) object[1];
										if (scenario != null)
											dataList.add(scenario);
									}
								}
							}
							if (dataList.isEmpty()) {
								Collection defaultScenario = locationManagerService.getDefaultServiceTimeScenarioDay();
								for (Iterator itr = defaultScenario.iterator(); itr.hasNext();) {
									DlvScenarioDay scenario = (DlvScenarioDay) itr.next();
									if (scenario != null)
										dataList.add(scenario);
								}
							}
						}
						scenarios.addAll(dataList);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			} else {
				List finalScenarioList = new ArrayList();
				Collection tempScenarios = new ArrayList();
				Collection scenariosWithNoDays = new ArrayList();
				if (("".equals(startDate) || startDate == null)
						|| (endDate == null || "".equals(endDate))) {
					tempScenarios  = locationManagerService.getDlvServiceTimeScenarioDays();
				    scenariosWithNoDays = locationManagerService.getScenariosWithNoDay();		
						
					for (Iterator itr = scenariosWithNoDays.iterator(); itr
							.hasNext();) {
						DlvServiceTimeScenario scenario=(DlvServiceTimeScenario)itr.next();
						DlvScenarioDay sd=new DlvScenarioDay();
						sd.setScenario(scenario);
						tempScenarios.add(sd);
					}
					finalScenarioList.addAll(tempScenarios);					
					return new ModelAndView("dlvServiceTimeScenarioView","dlvservicetimescenariolist",finalScenarioList);
				} else {
					Date d1 = TransStringUtil.getDate(startDate);
					Date d2 = TransStringUtil.getDate(endDate);
					Calendar cal1 = Calendar.getInstance();
					Calendar cal2 = Calendar.getInstance();
					cal1.setTime(d1);
					cal2.setTime(d2);
					cal2.add(Calendar.DATE, 1);
					String date = "";
					Date tempDate = null;
		            
					while (cal1.before(cal2)) {
						try {					
							date = TransStringUtil.dateFormat.format(cal1.getTime());
							
							tempDate=TransStringUtil.getDate(date);
							Calendar calendar=Calendar.getInstance();
							calendar.setTime(tempDate);
							int dayOfweek = calendar.get(Calendar.DAY_OF_WEEK);
							
							Set<DlvScenarioDay> dataList = new HashSet<DlvScenarioDay>();
							if (date != null) {
								Collection scenariosForDate = locationManagerService.getServiceTimeScenarios(date);
								for (Iterator itr = scenariosForDate.iterator(); itr.hasNext();) {
									Object[] object = (Object[])itr.next();
									if(null != object && object.length > 0){
										DlvScenarioDay scenario=(DlvScenarioDay)object[1];
										if(scenario!=null) 
											dataList.add(scenario);
									}
								}
								if (dataList.isEmpty()) {
									Collection scenariosForDayOfWeek = locationManagerService.getServiceTimeScenariosForDayofWeek(dayOfweek);
										for(Iterator itr=scenariosForDayOfWeek.iterator();itr.hasNext();){
											Object[] object = (Object[])itr.next();
											if(null != object && object.length > 0){
												DlvScenarioDay scenario=(DlvScenarioDay)object[1];
												if (scenario != null)
													dataList.add(scenario);
											}
										}							
								}
								if (dataList.isEmpty()) {
									Collection defaultScenario = locationManagerService.getDefaultServiceTimeScenarioDay();
									for(Iterator itr=defaultScenario.iterator();itr.hasNext();){
										DlvScenarioDay scenario=(DlvScenarioDay)itr.next();
										if (scenario != null)
											dataList.add(scenario);									
									}				
								}						
							}							
							tempScenarios.addAll(dataList);						
						} catch (ParseException e) {
							e.printStackTrace();
						} catch (Exception ex) {
							ex.printStackTrace();
						}				
						//TODO: Look for next day if any.
						cal1.add(Calendar.DATE, 1);				
					}
					scenarios.addAll(tempScenarios);	
				}
			}
			return new ModelAndView("dlvServiceTimeScenarioView", "dlvservicetimescenariolist", scenarios);
		}catch (Exception e) {
			e.printStackTrace();
			saveMessage(request, getMessage("app.actionmessage.155", null));
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
	
	public ModelAndView customerInfoHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {		
		
		String id = request.getParameter("id");
		String context = request.getParameter("context");
		Collection dataList = new ArrayList();
		if(!StringUtil.isEmpty(id)) {
			dataList = locationManagerService.getCustomerInfo(context, id);
		}
		return new ModelAndView("customerInfoView","customerlist",dataList);
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
			removeEntityList(scenarioSet);
			saveMessage(request, getMessage("app.actionmessage.103", null));
		}else{
			saveMessage(request, getMessage("app.actionmessage.156", null));
		}
		return dlvServiceTimeScenarioHandler(request, response);
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
