package com.freshdirect.transadmin.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.routing.constants.EnumOrderMetricsSource;
import com.freshdirect.routing.model.IHandOffBatch;
import com.freshdirect.routing.model.IHandOffBatchRoute;
import com.freshdirect.routing.model.IHandOffBatchSession;
import com.freshdirect.routing.model.IHandOffBatchStop;
import com.freshdirect.routing.model.IHandOffBatchTrailer;
import com.freshdirect.routing.model.IPackagingModel;
import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.model.OrderEstimationResult;
import com.freshdirect.routing.service.exception.IIssue;
import com.freshdirect.routing.service.exception.RoutingProcessException;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.HandOffServiceProxy;
import com.freshdirect.routing.service.proxy.RoutingInfoServiceProxy;
import com.freshdirect.routing.util.RoutingServicesProperties;
import com.freshdirect.routing.util.ServiceTimeUtil;
import com.freshdirect.transadmin.datamanager.IRouteFileManager;
import com.freshdirect.transadmin.datamanager.RouteFileManager;
import com.freshdirect.transadmin.datamanager.model.OrderRouteInfoModel;
import com.freshdirect.transadmin.datamanager.model.TrailerRouteInfoModel;
import com.freshdirect.transadmin.datamanager.report.ICommunityReport;
import com.freshdirect.transadmin.datamanager.report.XlsCommunityReport;
import com.freshdirect.transadmin.datamanager.report.XlsCutOffReport;
import com.freshdirect.transadmin.datamanager.report.model.CutOffReportData;
import com.freshdirect.transadmin.datamanager.report.model.CutOffReportKey;
import com.freshdirect.transadmin.model.TrnArea;
import com.freshdirect.transadmin.service.DispatchManagerI;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.util.TransportationAdminProperties;
import com.freshdirect.transadmin.web.model.SpatialBoundary;

public class HandOffController extends AbstractMultiActionController  {
	
	private static final String ROW_IDENTIFIER = "row";
	
	private static final String ROW_BEAN_IDENTIFIER = "rowBean";
	
	private DomainManagerI domainManagerService;
	
	private DispatchManagerI dispatchManagerService;
	
	
	public DispatchManagerI getDispatchManagerService() {
		return dispatchManagerService;
	}

	public void setDispatchManagerService(DispatchManagerI dispatchManagerService) {
		this.dispatchManagerService = dispatchManagerService;
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
	public ModelAndView communityReportHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {	
		String handOffBatchId = request.getParameter("handOffBatchId");
		if(handOffBatchId != null && handOffBatchId.trim().length() > 0) {

			try {
				Map areaMapping = getAreaMapping(domainManagerService.getAreas());
				HandOffServiceProxy proxy = new HandOffServiceProxy();
				IHandOffBatch batch = proxy.getHandOffBatchById(handOffBatchId);
				List<String> depotSessions = new ArrayList<String>();
				if(batch.getSession() != null) { 
					for(IHandOffBatchSession session : batch.getSession()) {
						if(session.isDepot()) {
							depotSessions.add(session.getSessionName());
						}
					}				
				}
				Map<String, IHandOffBatchRoute> routeMapping = getRouteInfo(batch);


				String reportFileName = TransportationAdminProperties.getCommunityRptFileName()+System.currentTimeMillis()+".xls";
				Map reportData = new TreeMap();
				Map stopCounts = new HashMap();
				
				for(Map.Entry<String, IHandOffBatchRoute> routeEntry : routeMapping.entrySet()) {

					IHandOffBatchRoute _route = routeEntry.getValue();
					IHandOffBatchStop _stop = null;
					String routingRouteId = routeEntry.getKey();
					if(_route.getStops() != null && _route.getStops().size() > 0) {
						
						stopCounts.put(routingRouteId, new Integer(_route.getStops().size()));
						if(_route.getStops() != null) {
							Iterator _iterator = _route.getStops().iterator();
							while(_iterator.hasNext()) {
								_stop = (IHandOffBatchStop)_iterator.next();
								String deliveryModel = ((TrnArea)areaMapping.get(_route.getArea())).getDeliveryModel();
									
								List communities = dispatchManagerService.matchCommunity
														(Double.parseDouble(_stop.getDeliveryInfo().getDeliveryLocation().getBuilding().getGeographicLocation().getLatitude())
																	, Double.parseDouble(_stop.getDeliveryInfo().getDeliveryLocation().getBuilding().getGeographicLocation().getLongitude())
																	, deliveryModel);
								if(communities != null && communities.size() > 0) {
									if(!reportData.containsKey(routingRouteId)) {
										reportData.put(routingRouteId, new HashMap());
									}
									Iterator _spaItr = communities.iterator();
									SpatialBoundary _boundary = null;
									while(_spaItr.hasNext()) {
										_boundary = (SpatialBoundary)_spaItr.next();
										if(!((Map)reportData.get(routingRouteId)).containsKey(_boundary)) {
											((Map)reportData.get(routingRouteId)).put(_boundary, new ArrayList());
										}
										((List)((Map)reportData.get(routingRouteId)).get(_boundary)).add(_stop);
									}
								}
							}
						}
					}
				}

				ICommunityReport report = new XlsCommunityReport();
				report.generateCommunityReport(reportFileName, reportData, stopCounts, TransStringUtil.getDate(batch.getDeliveryDate())
													, TransStringUtil.getTime(batch.getCutOffDateTime()));
				

				File outputFile = new File(reportFileName);
				response.setBufferSize((int)outputFile.length());

				response.setHeader("Content-Disposition", "attachment; filename=\""+outputFile.getName()+"\"");
				response.setContentType("application/x-download");
				response.setHeader("Pragma", "public");
				response.setHeader("Cache-Control", "max-age=0");
				response.setContentLength((int)outputFile.length());
				FileCopyUtils.copy(new FileInputStream(outputFile), response.getOutputStream());

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView sapUploadHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {	

		String handOffBatchId = request.getParameter("handOffBatchId");
		if(handOffBatchId != null && handOffBatchId.trim().length() > 0) {


			try {
				HandOffServiceProxy proxy = new HandOffServiceProxy();
				IHandOffBatch batch = proxy.getHandOffBatchById(handOffBatchId);
				List<String> depotSessions = new ArrayList<String>();
				if(batch.getSession() != null) { 
					for(IHandOffBatchSession session : batch.getSession()) {
						if(session.isDepot()) {
							depotSessions.add(session.getSessionName());
						}
					}				
				}
				Map<String, IHandOffBatchRoute> routeMapping = getRouteInfo(batch);
				long time = System.currentTimeMillis(); 
				String userName = com.freshdirect.transadmin.security.SecurityManager.getUserName(request);
				String outputFileName1 = TransportationAdminProperties.getRoutingOutputOrderFilename()+userName+time+".txt";
				String outputFileName2 = TransportationAdminProperties.getRoutingOutputTruckFilename()+userName+time+".txt";
				String outputFileName3 = TransportationAdminProperties.getRoutingOutputRouteStopFilename()+userName+time+".zip";
				
				
				List<OrderRouteInfoModel> orders = new ArrayList<OrderRouteInfoModel>();
				for(Map.Entry<String, IHandOffBatchRoute> routeEntry : routeMapping.entrySet()) {

					IHandOffBatchRoute _route = routeEntry.getValue();
					IHandOffBatchStop _stop = null;
					OrderRouteInfoModel _model = null; 
					boolean isDepot = depotSessions.contains(_route.getSessionName());
					if(_route.getStops() != null) {
						Iterator _iterator = _route.getStops().iterator();
						while(_iterator.hasNext()) {
							_stop = (IHandOffBatchStop)_iterator.next();
							_model = getOrderRouteInfoModel(_route, _stop, new HashMap<String, Map<Date, Integer>>());
							orders.add(_model);
						}
					}
				}
				IRouteFileManager fileManager = new RouteFileManager();
				
				fileManager.generateRouteFile(TransportationAdminProperties.getErpOrderInputFormat()
						, outputFileName1, ROW_IDENTIFIER, ROW_BEAN_IDENTIFIER, orders
						, null);
				fileManager.generateRouteFile(TransportationAdminProperties.getErpRouteInputFormat()
						, outputFileName2, ROW_IDENTIFIER, ROW_BEAN_IDENTIFIER, filterRoutesFromOrders(orders)
						, null);
				
				response.setHeader("Content-Disposition", "attachment; filename=\""+outputFileName3+"\"");
				response.setContentType("application/x-zip-compressed");
				response.setHeader("Pragma", "public");
				response.setHeader("Cache-Control", "max-age=0");
				//response.setContentLength((int)outputFile.length());
				
				ZipOutputStream zipout = new ZipOutputStream(response.getOutputStream());
				 
				String[] filenames = new String[] {outputFileName1, outputFileName2};
				byte[] buf = new byte[1024];
		        for (int i=0; i<filenames.length; i++) {    
		        	InputStream in;
		        	in = new FileInputStream (filenames[i]);
		           	zipout.putNextEntry(new ZipEntry(filenames[i]));
		            int len;
		            while ((len = in.read(buf)) > 0) {
		            	zipout.write(buf, 0, len);
		            }       
		            zipout.closeEntry();
		            in.close();
		        }
		        zipout.flush();
		        response.getOutputStream().flush();
				zipout.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView cutOffReportHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {	

		String handOffBatchId = request.getParameter("handOffBatchId");
		if(handOffBatchId != null && handOffBatchId.trim().length() > 0) {


			try {
				HandOffServiceProxy proxy = new HandOffServiceProxy();
				RoutingInfoServiceProxy routingInfoProxy = new RoutingInfoServiceProxy();

				IHandOffBatch batch = proxy.getHandOffBatchById(handOffBatchId);
				List<String> depotSessions = new ArrayList<String>();
				if(batch.getSession() != null) { 
					for(IHandOffBatchSession session : batch.getSession()) {
						if(session.isDepot()) {
							depotSessions.add(session.getSessionName());
						}
					}				
				}
				Map<String, IHandOffBatchRoute> routeMapping = getRouteInfo(batch);

				Map<String, IHandOffBatchTrailer> trailerMapping = getTrailerInfo(batch);

				IServiceTimeScenarioModel scenarioModel = routingInfoProxy.getRoutingScenarioByCode(batch.getServiceTimeScenario());
				if(scenarioModel == null) {
					throw new RoutingProcessException(null, null, IIssue.PROCESS_SCENARIO_NOTFOUND);
				}

				String reportFileName = TransportationAdminProperties.getRoutingCutOffRptFilename()
												+com.freshdirect.transadmin.security.SecurityManager.getUserName(request)
												+System.currentTimeMillis()+".xls";

				CutOffReportData result = new CutOffReportData();
				
				result.setBatch(batch);
				result.setPlannedDispatchTree(new RoutingInfoServiceProxy().getPlannedDispatchTree(batch.getDeliveryDate()));
				result.setCutOff(TransStringUtil.getServerTime(batch.getCutOffDateTime()));
				result.setScenarioModel(scenarioModel);
				result.setRouteMapping(routeMapping);
				result.setTrailerMapping(trailerMapping);
				result.setReportData(new TreeMap());
				result.setTripReportData(new TreeMap());
				result.setSummaryData(new TreeMap());
				result.setDetailData(new TreeMap());
				result.setTrailerDetailData(new TreeMap());
				
				for(Map.Entry<String, IHandOffBatchRoute> routeEntry : routeMapping.entrySet()) {

					IHandOffBatchRoute _route = routeEntry.getValue();
					IHandOffBatchStop _stop = null;
					OrderRouteInfoModel _model = null; 
					Map<String, Map<Date, Integer>> tripMapping = null;
					boolean isDepot = depotSessions.contains(_route.getSessionName());
					if(_route.getStops() != null) {
						if(isDepot) {
							tripMapping = getRouteTripDetails(_route);
						}
						Iterator _iterator = _route.getStops().iterator();
						while(_iterator.hasNext()) {
							_stop = (IHandOffBatchStop)_iterator.next();
							_model = getOrderRouteInfoModel(_route, _stop, tripMapping);
							if(_model.getDeliveryDate() != null &&
									_model.getTimeWindowStart() != null &&  
									_model.getTimeWindowStop() != null &&
									_model.getRouteId() != null) {
								result.putReportData(new CutOffReportKey(_model.getDeliveryDate(),
										_model.getTimeWindowStart(),
										_model.getTimeWindowStop(),
										_model.getRouteId()), _model);
								result.putSummaryData(_model.getRouteId(), _model);	
								if(isDepot) {
									result.putTripReportData(_model.getRouteId(), _model);
								} else {
									result.putDetailData(_model.getRouteId(), _model);
								}
							}
						}
					}
				}
				
				for(Map.Entry<String, IHandOffBatchTrailer> trailerEntry : trailerMapping.entrySet()) {
					IHandOffBatchTrailer _trailer = trailerEntry.getValue();
					IHandOffBatchRoute _route = null;
					TrailerRouteInfoModel _trailerRouteModel = null;
					
					if(_trailer.getRoutes() != null){
						Iterator<IHandOffBatchRoute> _iterator = _trailer.getRoutes().iterator();
						while(_iterator.hasNext()){
							_route = _iterator.next();
							_trailerRouteModel = getTrailerRouteInfoModel(_trailer, _route, scenarioModel);
							if(_trailerRouteModel.getTrailerNo() != null){
								result.putTrailerDetailData(_trailerRouteModel.getTrailerNo(), _trailerRouteModel);
							}
						}
					}
				}

				XlsCutOffReport report = new XlsCutOffReport();
				report.setNeedsDistanceFactor(false);
				
				report.generateCutOffReport(reportFileName, result);

				File outputFile = new File(reportFileName);
				response.setBufferSize((int)outputFile.length());

				response.setHeader("Content-Disposition", "attachment; filename=\""+outputFile.getName()+"\"");
				response.setContentType("application/x-download");
				response.setHeader("Pragma", "public");
				response.setHeader("Cache-Control", "max-age=0");
				response.setContentLength((int)outputFile.length());
				FileCopyUtils.copy(new FileInputStream(outputFile), response.getOutputStream());

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private TrailerRouteInfoModel getTrailerRouteInfoModel(IHandOffBatchTrailer _trailer, IHandOffBatchRoute _route, IServiceTimeScenarioModel scenario) throws ParseException{
		TrailerRouteInfoModel result = new TrailerRouteInfoModel();
		
		result.setRouteId(_route.getRouteId());		
		result.setTrailerNo(_trailer.getTrailerId());		
		result.setDispatchTime(_route.getDispatchTime() != null ? _route.getDispatchTime().getAsDate() : null);
		result.setDispatchSequence(_route.getDispatchSequence());		
		result.setNoOfStops(_route.getStops() != null ? _route.getStops().size() : 0);
		
		double routeCartonCnt = 0;
		if(_route.getStops() != null){
			Iterator<IHandOffBatchStop> _iterator = _route.getStops().iterator();
			IHandOffBatchStop _stop = null;	
			while(_iterator.hasNext()) {
				_stop = _iterator.next();
				if(_stop.getDeliveryInfo() != null && _stop.getDeliveryInfo().getPackagingDetail() != null){
					OrderEstimationResult orderSizeResult = getEstimateOrderSize(scenario, _stop.getDeliveryInfo().getPackagingDetail());					
					routeCartonCnt += orderSizeResult.getCalculatedOrderSize();
				}
			}	
		}
			
		result.setNoOfCartons(routeCartonCnt);
		return result;
	}
	
	private OrderRouteInfoModel getOrderRouteInfoModel(IHandOffBatchRoute _route, IHandOffBatchStop _stop, Map<String, Map<Date, Integer>> tripMapping)
			throws ParseException {
		
		OrderRouteInfoModel result = new OrderRouteInfoModel();
		result.setTotalDistance(""+_route.getDistance());
		
		result.setTotalTravelTime(TransStringUtil.getTime(TransStringUtil.calcHMS((int)_route.getTravelTime())));
		result.setTotalServiceTime(TransStringUtil.getTime(TransStringUtil.calcHMS((int)_route.getServiceTime())));
		result.setRouteCompleteTime(_route.getCompletionTime());
		result.setDeliveryModel(_stop.getDeliveryInfo() != null ? _stop.getDeliveryInfo().getDeliveryModel() : null);
		result.setFixedServiceTime(null);
		result.setLocationId(_stop.getDeliveryInfo() != null 
								&& _stop.getDeliveryInfo().getDeliveryLocation() != null 
										? _stop.getDeliveryInfo().getDeliveryLocation().getLocationId() : null);
		result.setOrderNumber(_stop.getErpOrderNumber());
		result.setPlant(RoutingServicesProperties.getDefaultPlantCode());
		result.setRouteId(_route.getRouteId());
		result.setStopNumber(""+_stop.getStopNo());
		result.setTotalSize1(null);
		result.setTotalSize2(null);
		result.setDeliveryDate(_stop.getDeliveryInfo() != null ? _stop.getDeliveryInfo().getDeliveryDate() : null);
		result.setOrderBeginDate(_stop.getDeliveryInfo() != null ? _stop.getDeliveryInfo().getDeliveryDate() : null);
		result.setOrderEndDate(_stop.getDeliveryInfo() != null ? _stop.getDeliveryInfo().getDeliveryDate() : null);
		result.setRouteStartTime(_route.getStartTime());
		result.setStopArrivalTime(_stop.getStopArrivalTime());
		result.setTimeWindowStart(_stop.getDeliveryInfo() != null ? _stop.getDeliveryInfo().getDeliveryStartTime() : null);
		result.setTimeWindowStop(_stop.getDeliveryInfo() != null ? _stop.getDeliveryInfo().getDeliveryEndTime() : null);
		result.setDlvETAWindowStart(_stop.getDeliveryInfo() != null ? _stop.getDeliveryInfo().getDeliveryETAStartTime() : null);
		result.setDlvETAWindowStop(_stop.getDeliveryInfo() != null ? _stop.getDeliveryInfo().getDeliveryETAEndTime() : null);
		result.setDeliveryZone(_stop.getDeliveryInfo() != null && _stop.getDeliveryInfo().getDeliveryZone() != null
							? _stop.getDeliveryInfo().getDeliveryZone().getZoneNumber() : null);
		result.setDeliveryArea(_stop.getDeliveryInfo() != null && _stop.getDeliveryInfo().getDeliveryZone() != null
				? _stop.getDeliveryInfo().getDeliveryZone().getZoneNumber() : null);
		result.setAddress(_stop.getDeliveryInfo() != null 
								&& _stop.getDeliveryInfo().getDeliveryLocation() != null
									&& _stop.getDeliveryInfo().getDeliveryLocation().getBuilding() != null 
									? _stop.getDeliveryInfo().getDeliveryLocation().getBuilding().getSrubbedStreet() : null);	
		result.setLatitude(_stop.getDeliveryInfo() != null 
								&& _stop.getDeliveryInfo().getDeliveryLocation() != null
								&& _stop.getDeliveryInfo().getDeliveryLocation().getBuilding() != null
								&& _stop.getDeliveryInfo().getDeliveryLocation().getBuilding().getGeographicLocation() != null 
								? _stop.getDeliveryInfo().getDeliveryLocation().getBuilding().getGeographicLocation().getLatitude() : null);
		result.setLongitude(_stop.getDeliveryInfo() != null 
				&& _stop.getDeliveryInfo().getDeliveryLocation() != null
				&& _stop.getDeliveryInfo().getDeliveryLocation().getBuilding() != null
				&& _stop.getDeliveryInfo().getDeliveryLocation().getBuilding().getGeographicLocation() != null 
				? _stop.getDeliveryInfo().getDeliveryLocation().getBuilding().getGeographicLocation().getLongitude() : null);
		result.setState(_stop.getDeliveryInfo() != null 
				&& _stop.getDeliveryInfo().getDeliveryLocation() != null
				&& _stop.getDeliveryInfo().getDeliveryLocation().getBuilding() != null 
				? _stop.getDeliveryInfo().getDeliveryLocation().getBuilding().getState() : null);
		result.setZipcode(_stop.getDeliveryInfo() != null 
				&& _stop.getDeliveryInfo().getDeliveryLocation() != null
				&& _stop.getDeliveryInfo().getDeliveryLocation().getBuilding() != null 
				? _stop.getDeliveryInfo().getDeliveryLocation().getBuilding().getZipCode() : null);
		result.setStopDepartureTime(_stop.getStopDepartureTime());
		result.setTripId(getTripId(_route, _stop.getStopDepartureTime(), _stop.getRoutingRouteId()));
		result.setTripNo(tripMapping != null ? tripMapping.get(_stop.getRoutingRouteId()).get(_stop.getStopDepartureTime()) : 0);
		result.setOrderSize(_stop.getDeliveryInfo() != null ? (int) _stop.getDeliveryInfo().getCalculatedOrderSize() : 0);
		
		result.setDispatchTime(_route.getDispatchTime() != null ? _route.getDispatchTime().getAsDate() : null);
		result.setDispatchSequence(_route.getDispatchSequence());
		return result;
	}
	
	private Map<String, Map<Date, Integer>> getRouteTripDetails(IHandOffBatchRoute _route) {
				
		List<Date> stopDepartureTimeLst = new ArrayList<Date>();			
		/*RoutingRouteId -> Stop DepartureTime -> TripNo*/
		Map<String, Map<Date, Integer>> tripMapping = new HashMap<String, Map<Date,Integer>>();
		/*RoutingRouteId -> Stop DepartureTime*/
		Map<String, List<Date>> routeMapping = new HashMap<String, List<Date>>();
		IHandOffBatchStop _stop = null;
		if(_route != null) {
			if(_route.getStops() != null) {
				Iterator _iterator = _route.getStops().iterator();
				while(_iterator.hasNext()) {
					_stop = (IHandOffBatchStop)_iterator.next();
					if(!stopDepartureTimeLst.contains(_stop.getStopDepartureTime())){
						stopDepartureTimeLst.add(_stop.getStopDepartureTime());
					}
					if(!tripMapping.containsKey(_stop.getRoutingRouteId())){
						tripMapping.put(_stop.getRoutingRouteId(), new HashMap<Date, Integer>());
					}
					if(!routeMapping.containsKey(_stop.getRoutingRouteId())){
						routeMapping.put(_stop.getRoutingRouteId(), new ArrayList<Date>());
					}
					if(!routeMapping.get(_stop.getRoutingRouteId()).contains(_stop.getStopDepartureTime())) {
						routeMapping.get(_stop.getRoutingRouteId()).add(_stop.getStopDepartureTime());
					}
				}
			}
		}
		
		Collections.sort(stopDepartureTimeLst);
		Iterator<Date> stopDeptItr = stopDepartureTimeLst.iterator();
		Set<String> routingRouteIds = routeMapping.keySet();
		List<String> sortedKeys = new ArrayList<String>();
		sortedKeys.addAll(routingRouteIds);
		Collections.sort(sortedKeys, new Comparator<String>() {
			    @Override
			    public int compare(String s1, String s2) {
			        String str1 = s1.replace("-", ""); 
			        String str2 = s2.replace("-", "");
			        return Integer.parseInt(str1) < (Integer.parseInt(str2)) ? -1 : Integer.parseInt(str1) == (Integer.parseInt(str2)) ? 0 : 1;
			    }
		});
		int tripCnt = 0;
		while(stopDeptItr.hasNext()){
			Date _stopDepartureTime = stopDeptItr.next();
			for(String _routingRouteId : sortedKeys) {
				if(routeMapping.get(_routingRouteId).contains(_stopDepartureTime)) {
					tripMapping.get(_routingRouteId).put(_stopDepartureTime, ++tripCnt);
				}
			}
		}
		
		return tripMapping;
	}

	@SuppressWarnings("rawtypes")
	private String getTripId(IHandOffBatchRoute _route, Date stopDepartureTime, String stopRoutingRouteId) {
		
		String stopTripId = null;
		Map<Date, Integer> routeTripMapping = new HashMap<Date, Integer>();
		List<Date> stopDepartureTimeLst = new ArrayList<Date>();
		if(_route.getStops() != null) {
			IHandOffBatchStop _stop = null;
			int tripCnt = 0;
			Iterator _iterator = _route.getStops().iterator();
			while(_iterator.hasNext()) {
				_stop = (IHandOffBatchStop)_iterator.next();
				if(_stop.getRoutingRouteId() != null && _stop.getRoutingRouteId().equals(stopRoutingRouteId)) {
					if(!stopDepartureTimeLst.contains(_stop.getStopDepartureTime())) {
						stopDepartureTimeLst.add(_stop.getStopDepartureTime());
					}
				}
			}
			Collections.sort(stopDepartureTimeLst);
			for(Date _stopDepartureTime : stopDepartureTimeLst) {
				routeTripMapping.put(_stopDepartureTime, ++tripCnt);
			}
			stopTripId = stopRoutingRouteId + "_" + Integer.toString(routeTripMapping.get(stopDepartureTime));
		}		
		return stopTripId;
	}

	private Map<String, IHandOffBatchRoute> getRouteInfo(IHandOffBatch batch) throws RoutingServiceException {
		Map<String, IHandOffBatchRoute> routeMapping = new HashMap<String, IHandOffBatchRoute>();
		
		HandOffServiceProxy proxy = new HandOffServiceProxy();
		
		List<IHandOffBatchStop> handOffStops = proxy.getOrderByCutoff(batch.getDeliveryDate(), batch.getCutOffDateTime());
		Map<String, IPackagingModel> orderNoToPackaging = new HashMap<String, IPackagingModel>();

		if(handOffStops != null) {
			for(IHandOffBatchStop stop : handOffStops) {
				if(stop.getDeliveryInfo() != null && stop.getDeliveryInfo().getPackagingDetail() != null){
					orderNoToPackaging.put(stop.getOrderNumber(), stop.getDeliveryInfo().getPackagingDetail());
				}					
			}
		}

		if(batch != null) {
			List routes = proxy.getHandOffBatchRoutes(batch.getBatchId());
			List stops = proxy.getHandOffBatchStops(batch.getBatchId(), true);
						
			Iterator<IHandOffBatchRoute> itr = routes.iterator();
			while(itr.hasNext()) {
				IHandOffBatchRoute route = itr.next();
				routeMapping.put(route.getRouteId(), route);
			}
			
			Iterator<IHandOffBatchStop> itrStop = stops.iterator();
			while( itrStop.hasNext()) {
				IHandOffBatchStop stop = itrStop.next();
				if(stop.getDeliveryInfo() != null && stop.getDeliveryInfo().getPackagingDetail() == null){
					stop.getDeliveryInfo().setPackagingDetail(orderNoToPackaging.get(stop.getOrderNumber()));
				} 
				IHandOffBatchRoute route = routeMapping.get(stop.getRouteId());
				if(route != null) {
					if(route.getStops() == null) {
						route.setStops(new TreeSet());
					}
					route.getStops().add(stop);
				}
			}
		}
		return routeMapping;
			
	}
	
	private Map<String, IHandOffBatchTrailer> getTrailerInfo(IHandOffBatch batch) throws RoutingServiceException {
		Map<String, IHandOffBatchTrailer> trailerMapping = new HashMap<String, IHandOffBatchTrailer>();
		
		HandOffServiceProxy proxy = new HandOffServiceProxy();
		
		if(batch != null) {
			List<IHandOffBatchTrailer> trailers = proxy.getHandOffBatchTrailers(batch.getBatchId());
			Map<String, IHandOffBatchRoute> routeMapping = getRouteInfo(batch); 

			Iterator<IHandOffBatchTrailer> itr = trailers.iterator();
			while(itr.hasNext()) {
				IHandOffBatchTrailer trailer = itr.next();
				trailerMapping.put(trailer.getTrailerId(), trailer);
			}

			for(Map.Entry<String, IHandOffBatchRoute> routeEntry : routeMapping.entrySet()){
				IHandOffBatchRoute route = routeEntry.getValue();
				if(route.getTrailerId() != null){
					IHandOffBatchTrailer trailer = trailerMapping.get(route.getTrailerId());
					if(trailer != null){
						if(trailer.getRoutes() == null) {
							trailer.setRoutes(new TreeSet(new RouteComparator()));
						}
						trailer.getRoutes().add(route);
					}
				}
			}
		}
		return trailerMapping;
	}

	protected List filterRoutesFromOrders(List orderDataList) {
		List routeIds = new ArrayList();
		List routes = new ArrayList();

		String routeId = null;
		if(orderDataList != null) {
			OrderRouteInfoModel tmpRouteInfo = null;
			Iterator iterator = orderDataList.iterator();
			while(iterator.hasNext()) {
				tmpRouteInfo = (OrderRouteInfoModel)iterator.next();
				routeId = tmpRouteInfo.getRouteId();
				if(!routeIds.contains(routeId)) {
					routes.add(tmpRouteInfo);
					routeIds.add(routeId);
				}
			}
		}
		return routes;
	}
	
	private Map getAreaMapping(Collection areaLst) {
		Map areaMapping = new HashMap();
		if(areaLst != null) {
			Iterator iterator = areaLst.iterator();
			TrnArea tmpModel = null;
			
			while(iterator.hasNext()) {
				tmpModel = (TrnArea)iterator.next();
				areaMapping.put(tmpModel.getCode(), tmpModel);
			}
		}
		return areaMapping;
	}
	
	private class RouteComparator implements Comparator<IHandOffBatchRoute> {		
	
		public int compare(IHandOffBatchRoute obj1, IHandOffBatchRoute obj2){
			String routeId1 = ((IHandOffBatchRoute) obj1).getRouteId();
			String routeId2 = ((IHandOffBatchRoute) obj2).getRouteId();			
			return routeId1.compareTo(routeId2);
		}
	}
	
	protected OrderEstimationResult getEstimateOrderSize(IServiceTimeScenarioModel scenario, IPackagingModel tmpPackageModel) {
		OrderEstimationResult result = new OrderEstimationResult();
		if(scenario != null) {
			double cartonCount = scenario.getDefaultCartonCount(); 
			double freezerCount = scenario.getDefaultFreezerCount();
			double caseCount = scenario.getDefaultCaseCount();
			boolean isDefault = true;
			
			if(tmpPackageModel != null) {
				cartonCount = tmpPackageModel.getNoOfCartons(); 
				freezerCount = tmpPackageModel.getNoOfFreezers();
				caseCount = tmpPackageModel.getNoOfCases();
			
				isDefault = (cartonCount == 0 && freezerCount == 0 && caseCount == 0);								
				tmpPackageModel.setSource(isDefault ? EnumOrderMetricsSource.DEFAULT : EnumOrderMetricsSource.ACTUAL);
			}
			result.setPackagingModel(tmpPackageModel);
			result.setCalculatedOrderSize(ServiceTimeUtil.evaluateExpression(scenario.getOrderSizeFormula()
																					, ServiceTimeUtil.getServiceTimeFactorParams(tmpPackageModel)));
		}
		return result;
	}
	
	
}
