package com.freshdirect.transadmin.web;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.routing.model.GeoPoint;
import com.freshdirect.routing.model.GeographicLocation;
import com.freshdirect.routing.model.IGeoPoint;
import com.freshdirect.routing.model.IGeographicLocation;
import com.freshdirect.routing.model.ILocationModel;
import com.freshdirect.routing.model.IRouteModel;
import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.model.IRoutingStopModel;
import com.freshdirect.routing.model.LocationModel;
import com.freshdirect.routing.model.RoutingSchedulerIdentity;
import com.freshdirect.routing.model.RoutingStopModel;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.DeliveryServiceProxy;
import com.freshdirect.routing.service.proxy.RoutingEngineServiceProxy;
import com.freshdirect.routing.util.RoutingServicesProperties;
import com.freshdirect.transadmin.datamanager.report.DrivingDirectionsReport;
import com.freshdirect.transadmin.datamanager.report.ReportGenerationException;
import com.freshdirect.transadmin.model.Dispatch;
import com.freshdirect.transadmin.model.FDRouteMasterInfo;
import com.freshdirect.transadmin.model.Plan;
import com.freshdirect.transadmin.model.RouteMapping;
import com.freshdirect.transadmin.model.RouteMappingId;
import com.freshdirect.transadmin.model.TrnRouteNumber;
import com.freshdirect.transadmin.model.TrnRouteNumberId;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.security.SecurityManager;
import com.freshdirect.transadmin.service.DispatchManagerI;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.util.DispatchPlanUtil;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.model.DispatchCommand;
import com.freshdirect.transadmin.web.model.WebEmployeeInfo;
import com.freshdirect.transadmin.web.model.WebPlanInfo;

public class DispatchController extends AbstractMultiActionController {

	private DispatchManagerI dispatchManagerService;
	private EmployeeManagerI employeeManagerService;
	private DomainManagerI domainManagerService;


	public DispatchManagerI getDispatchManagerService() {
		return dispatchManagerService;
	}

	public void setDispatchManagerService(DispatchManagerI dispatchManagerService) {
		this.dispatchManagerService = dispatchManagerService;
	}
	public EmployeeManagerI getEmployeeManagerService() {
		return employeeManagerService;
	}

	public void setEmployeeManagerService(EmployeeManagerI employeeManagerService) {
		this.employeeManagerService = employeeManagerService;
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
	public ModelAndView planHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		String daterange = request.getParameter("daterange");
		String zoneLst = request.getParameter("zone");
		ModelAndView mav = new ModelAndView("planView");
		if(!TransStringUtil.isEmpty(daterange) || !TransStringUtil.isEmpty(zoneLst)) {

			try {
				String dateQryStr = TransStringUtil.formatDateSearch(daterange);
				String zoneQryStr = StringUtil.formQueryString(Arrays.asList(StringUtil.decodeStrings(zoneLst)));
					//TransStringUtil.formatStringSearch(zoneLst);
				if(dateQryStr != null || zoneQryStr != null) {
					Collection dataList = getPlanInfo(dateQryStr,zoneQryStr);//dispatchManagerService.getPlan(dateQryStr, zoneQryStr);
					mav.getModel().put("planlist",dataList);
				}
			} catch (Exception e) {
				e.printStackTrace();
				saveMessage(request, getMessage("app.actionmessage.123", null));
			}
		}

		return mav;
	}

	private Collection getPlanInfo(String dateQryStr, String zoneQryStr) {

		Collection plans=dispatchManagerService.getPlan(dateQryStr, zoneQryStr);
		List termintedEmployees = getTermintedEmployeeIds();

		Collection planInfos=new ArrayList();
		Iterator it=plans.iterator();
		while(it.hasNext()) {

			Plan plan=(Plan)it.next();
			Zone zone=null;
			if(plan.getZone()!=null) {
				zone=domainManagerService.getZone(plan.getZone().getZoneCode());
			}
			WebPlanInfo planInfo=DispatchPlanUtil.getWebPlanInfo(plan, zone, employeeManagerService);
			planInfo.setTermintedEmployees(termintedEmployees);
			planInfos.add(planInfo);
		}


		return planInfos;
	}



	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView planDeleteHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		Set employeeSet=new HashSet();
		String arrEntityList[] = getParamList(request);
		if (arrEntityList != null) {
			int arrLength = arrEntityList.length;
			for (int intCount = 0; intCount < arrLength; intCount++) {
				employeeSet.add(dispatchManagerService.getPlan(arrEntityList[intCount]));
			}
		}
		dispatchManagerService.removeEntity(employeeSet);
		saveMessage(request, getMessage("app.actionmessage.103", null));

		return planHandler(request, response);
	}

	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView dispatchHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		String dispDate = request.getParameter("dispDate");
		String zone = request.getParameter("zone");
		String region = request.getParameter("region");
		ModelAndView mav = new ModelAndView("dispatchView");
		if(!TransStringUtil.isEmpty(dispDate)) {
			mav.getModel().put("dispatchInfos", getDispatchInfos(getServerDate(dispDate), zone, region, false));
			mav.getModel().put("dispDate", dispDate);
		} else {
			//By default get the today's dispatches.
			mav.getModel().put("dispatchInfos",getDispatchInfos(TransStringUtil.getCurrentServerDate(), zone, region, false));
			mav.getModel().put("dispDate", TransStringUtil.getCurrentDate());
		}
		mav.getModel().put("zones", domainManagerService.getZones());
		mav.getModel().put("regions", domainManagerService.getRegions());
		return mav;
	}

	public ModelAndView dispatchSummaryHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		ModelAndView mav = new ModelAndView("dispatchSummaryView");
		//By default get the today's dispatches.
		mav.getModel().put("dispatchInfos",getDispatchInfos(TransStringUtil.getCurrentServerDate(), null, null, true));
		mav.getModel().put("dispDate", TransStringUtil.getCurrentDate());
		return mav;
	}

	private Collection getDispatchInfos(String dispDate, String zoneStr, String region, boolean isSummary){
		Collection dispatchInfos = new ArrayList();
		List termintedEmployees = getTermintedEmployeeIds();
		try {
		Collection dispatchList = dispatchManagerService.getDispatchList(dispDate, zoneStr, region);
		Iterator iter = dispatchList.iterator();
			while(iter.hasNext()){
				Dispatch dispatch = (Dispatch) iter.next();
				Zone zone=null;
				if(dispatch.getZone() != null) {
					zone=domainManagerService.getZone(dispatch.getZone().getZoneCode());
				}
				DispatchCommand command = DispatchPlanUtil.getDispatchCommand(dispatch, zone, employeeManagerService);
				command.setTermintedEmployees(termintedEmployees);
				if(isSummary){
					FDRouteMasterInfo routeInfo = domainManagerService.getRouteMasterInfo(command.getRoute(), new Date());
					if(routeInfo != null){
						command.setNoOfStops(routeInfo.getNumberOfStops());
					}

				}
				dispatchInfos.add(command);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			throw new RuntimeException("Exception ocuurred while processing the dispatch list for requested date "+dispDate);
		}

		return dispatchInfos;
	}

	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView dispatchDeleteHandler(HttpServletRequest request, HttpServletResponse response)
								throws ServletException, ParseException {

		Set dispatchSet=new HashSet();
		String arrEntityList[] = getParamList(request);
		//StringTokenizer splitter = null;
		if (arrEntityList != null) {
			int arrLength = arrEntityList.length;
			for (int intCount = 0; intCount < arrLength; intCount++) {
				//splitter = new StringTokenizer(arrEntityList[intCount], "$");
				Dispatch dispatch =dispatchManagerService.getDispatch(arrEntityList[intCount]);
				if(dispatch.getConfirmed()== null || dispatch.getConfirmed() == Boolean.FALSE){
					dispatchSet.add(dispatch);
				}
			}
			if(dispatchSet.size() == arrLength) {
				dispatchManagerService.removeEntity(dispatchSet);
				saveMessage(request, getMessage("app.actionmessage.103", null));
			} else {
				saveMessage(request, getMessage("app.actionmessage.136", null));
			}
		}
		return dispatchHandler(request, response);
	}

	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView dispatchConfirmHandler(HttpServletRequest request, HttpServletResponse response)
								throws ServletException, ParseException {

		Set dispatchSet=new HashSet();
		String arrEntityList[] = getParamList(request);
		StringTokenizer splitter = null;
		Dispatch tmpDispatch = null;
		int routeBlankCount = 0;
		if (arrEntityList != null) {
			int arrLength = arrEntityList.length;
			for (int intCount = 0; intCount < arrLength; intCount++) {
				//splitter = new StringTokenizer(arrEntityList[intCount], "$");
				tmpDispatch =dispatchManagerService.getDispatch(arrEntityList[intCount]);
				if(TransStringUtil.isEmpty(tmpDispatch.getRoute())){
					routeBlankCount++;
					break;
				}
				if(tmpDispatch != null) {
					Boolean confirm = tmpDispatch.getConfirmed();
					if( confirm == null || ! confirm.booleanValue() ) {
						tmpDispatch.setConfirmed(Boolean.TRUE);
					} else {
						tmpDispatch.setConfirmed(Boolean.FALSE);
					}
					dispatchSet.add(tmpDispatch);
				}
			}
			if(routeBlankCount > 0){
				saveMessage(request, getMessage("app.actionmessage.138", null));
			}else {
				dispatchManagerService.saveEntityList(dispatchSet);
				saveMessage(request, getMessage("app.actionmessage.104", null));
			}
		}

		return dispatchHandler(request, response);
	}

	public ModelAndView routeRefreshHandler(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, ParseException {
		String dispDate = request.getParameter("dispDate");
		String isSummary = request.getParameter("summary");
		boolean changed = false;
		if(!TransStringUtil.isEmpty(dispDate)) {
			changed = dispatchManagerService.refreshRoute(TransStringUtil.getDate(dispDate));
		} else {
			//By default get the today's dispatches.
			changed = dispatchManagerService.refreshRoute(new Date());
		}
		if(changed){
			saveMessage(request, getMessage("app.actionmessage.134", null));
		}
		if(TransStringUtil.isEmpty(isSummary)){
			return dispatchHandler(request, response);
		}else{
//			System.out.println("Inside Summary");
			return dispatchSummaryHandler(request, response);
		}

	}

	public ModelAndView autoDispatchHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException, ParseException {

				// if there is a dispatch record for the same date then check what user role
				// if not admin send an error message
				// if admin delete the existing record and run the autodispatch crap


				String dispatchDate = request.getParameter("daterange");
				try {
					if(!TransStringUtil.isEmpty(dispatchDate)) {
						dispatchDate=TransStringUtil.getServerDate(dispatchDate);
					}else
					{
						dispatchDate=TransStringUtil.getServerDate(new Date());
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

					saveMessage(request, getMessage("app.error.115", new String[]{"Invalid Date"}));
					return planHandler(request,response);
			    }

				    Collection planList=dispatchManagerService.getPlanList(dispatchDate);

				    if(planList == null || planList.size() == 0){
				    	saveMessage(request, getMessage("app.actionmessage.142", null));
				    	return planHandler(request,response);
				    }

					Collection dispList = dispatchManagerService.getDispatchList(dispatchDate,null,null);
					//System.out.println("dispList >>"+dispList);
					if(!SecurityManager.isUserAdmin(request)){
						  saveMessage(request, getMessage("app.actionmessage.140", null));
						  return planHandler(request,response);
					}
				   dispatchManagerService.autoDisptch(dispatchDate);
				   saveMessage(request, getMessage("app.actionmessage.143", null));
				   return planHandler(request,response);
	}

	public ModelAndView unassignedRouteHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException  {

		ModelAndView mav = new ModelAndView("unassignedRouteView");
		String routeDate = request.getParameter("routeDate");
		try {
			mav.getModel().put("routes", dispatchManagerService.getUnusedDispatchRoutes(TransStringUtil.getServerDate(routeDate)));
		}  catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mav.getModel().put("routes",dispatchManagerService.getUnusedDispatchRoutes(TransStringUtil.getCurrentServerDate()));
		}
		return mav;
	}
	
	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView routeNumberHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		String routeDate = request.getParameter("routeDate");
		ModelAndView mav = new ModelAndView("routeNumberView");
		mav.getModel().put("routeDate", routeDate);
		List dataList = new ArrayList();
		
		if(!TransStringUtil.isEmpty(routeDate)) {
			Map routeInfoGrp = this.getDispatchManagerService().getRouteNumberGroup(getServerDate(routeDate), null, null);
			Iterator _iterator = routeInfoGrp.keySet().iterator();
			
			while(_iterator.hasNext()) {
				
				RouteMappingId key = (RouteMappingId)_iterator.next();
				TrnRouteNumber _routeNo = new TrnRouteNumber();
				_routeNo.setRouteNumberId(new TrnRouteNumberId(key.getRouteDate(), key.getCutOffId(), key.getGroupCode()));
				_routeNo.setCurrentVal(new BigDecimal(routeInfoGrp.get(key).toString()));
				dataList.add(_routeNo);
			}
			mav.getModel().put("routenumberlist",dataList);
		}

		return mav;
	}
	
	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView drivingDirectionsHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		List routingRouteIds = Arrays.asList(StringUtil.decodeStrings(request.getParameter("routeId")));
		String routeDate = request.getParameter("rdate");
		
		try {
			if(routingRouteIds != null) {
				
				DeliveryServiceProxy proxy = new DeliveryServiceProxy();
				RoutingEngineServiceProxy engineProxy = new RoutingEngineServiceProxy();
				
				Iterator _itr = routingRouteIds.iterator();
				String routingRouteId = null;
				Map directionRoutes = new TreeMap();
				IRouteModel _tmpRoute = null;
				
				while(_itr.hasNext()) {
					routingRouteId = (String)_itr.next();
					Collection routes = domainManagerService.getRouteMapping(TransStringUtil.getServerDate(routeDate), routingRouteId);
					
					if(routes != null && routes.size() == 1) {
						
						RouteMapping routeMapping = (RouteMapping)routes.toArray()[0];		
						
						IRoutingSchedulerIdentity schedulerId = new RoutingSchedulerIdentity();
						schedulerId.setRegionId(RoutingServicesProperties.getDefaultRegion());
								
						String sessionId = engineProxy.retrieveRoutingSession(schedulerId, routeMapping.getRoutingSessionID());
						
						List routingRoutes = proxy.getRoutes(TransStringUtil.getDate(routeDate), sessionId
																, routeMapping.getRouteMappingId().getRoutingRouteID());
						
						if(routingRoutes != null && routingRoutes.size() > 0) {
							_tmpRoute = (IRouteModel)routingRoutes.get(0);
							System.out.println(">>"+_tmpRoute+" >> "+_tmpRoute.getStops());
							if(_tmpRoute.getStops() != null && _tmpRoute.getStops().size() > 0) {
								directionRoutes.put(routingRouteId, _tmpRoute);
							} else {
								directionRoutes.put(routingRouteId, null);
							}
						}
					}
				}
				DrivingDirectionsReport reportEngine = new DrivingDirectionsReport();
				if(directionRoutes != null && directionRoutes.size() > 0) {
					Map directionsReportData = this.getRouteDirections(directionRoutes);
					reportEngine.generateDrivingDirectionsReport(response.getOutputStream(), directionsReportData);
					
				} else {
					reportEngine.generateError(response.getOutputStream(), "Driving Direction generation error!");
				}
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			saveMessage(request, getMessage("app.actionmessage.123", null));
		}
		response.setContentType("application/pdf");
		return null;
	}
	
	private Map getRouteDirections(Map routes) throws ReportGenerationException {
		Map result = new TreeMap();
		DeliveryServiceProxy proxy = new DeliveryServiceProxy();
		try {
			Iterator _itr = routes.keySet().iterator();
			String _routeId = null;
			IRouteModel route = null;
			
			while(_itr.hasNext()) {
				_routeId = (String)_itr.next();
				route = (IRouteModel)routes.get(_routeId);
				if(route != null) {
					route.getStops().add(getStop(Integer.MIN_VALUE, "DPT/FD", null, null,
							null, "40740250", "-73951989"));
					route.getStops().add(getStop(Integer.MAX_VALUE, "DPT/FD", null, null,
							null, "40740250", "-73951989"));
		
					
					List points = new ArrayList();
					
					Iterator stopIterator = route.getStops().iterator();
					IRoutingStopModel _stop = null;
					IGeoPoint _geoPoint = null;
					
					while (stopIterator.hasNext()) {
						
						_stop = (IRoutingStopModel) stopIterator.next();
						_geoPoint = new GeoPoint();
						_geoPoint.setLatitude(Integer.parseInt(_stop.getLocation().getGeographicLocation().getLatitude()));
						_geoPoint.setLongitude(Integer.parseInt(_stop.getLocation().getGeographicLocation().getLongitude()));
						points.add(_geoPoint);
					}
					route.setDrivingDirection(proxy.buildDriverDirections(points));
					result.put(_routeId, route);
				} else {
					result.put(_routeId, null);
				}
			}
		} catch (RoutingServiceException exp) {
			exp.printStackTrace();
			throw new ReportGenerationException(
					"Unable to generate driver directions");
		}
		
		return result;
	}
	
	private IRoutingStopModel getStop(int id, String line1, String city, String state, String zipCode,
											String latitude, String longitude) {
	
		IRoutingStopModel _stop = new RoutingStopModel(id);
		
		ILocationModel _locModel = new LocationModel();
		
		_locModel.setStreetAddress1(line1);
		_locModel.setCity(city); 
		_locModel.setState(state);
		_locModel.setZipCode(zipCode);
		
		_stop.setLocation(_locModel);
		
		IGeographicLocation _geoLocModel = new GeographicLocation();
		_geoLocModel.setLatitude(latitude);
		_geoLocModel.setLongitude(longitude);
		
		_locModel.setGeographicLocation(_geoLocModel);
		
		return _stop;
	}

	private List getTermintedEmployeeIds() {
		Collection termintedList = employeeManagerService.getTerminatedEmployees();
		List result = new ArrayList();
		if(termintedList != null) {
			Iterator iterator = termintedList.iterator();
			WebEmployeeInfo info = null;
			while(iterator.hasNext()) {
				//System.out.println(iterator.next());
				info = (WebEmployeeInfo)iterator.next();
				result.add(info.getEmployeeId());
			}
		}
		return result;
	}
	
	

}
