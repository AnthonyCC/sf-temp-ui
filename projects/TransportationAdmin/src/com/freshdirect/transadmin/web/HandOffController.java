package com.freshdirect.transadmin.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.routing.model.IHandOffBatch;
import com.freshdirect.routing.model.IHandOffBatchRoute;
import com.freshdirect.routing.model.IHandOffBatchSession;
import com.freshdirect.routing.model.IHandOffBatchStop;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.HandOffServiceProxy;
import com.freshdirect.routing.util.RoutingServicesProperties;
import com.freshdirect.transadmin.datamanager.IRouteFileManager;
import com.freshdirect.transadmin.datamanager.RouteFileManager;
import com.freshdirect.transadmin.datamanager.model.OrderRouteInfoModel;
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
							_model = getOrderRouteInfoModel(_route, _stop);
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


				String reportFileName = TransportationAdminProperties.getRoutingCutOffRptFilename()
												+com.freshdirect.transadmin.security.SecurityManager.getUserName(request)
												+System.currentTimeMillis()+".xls";

				CutOffReportData result = new CutOffReportData();

				result.setCutOff(TransStringUtil.getServerTime(batch.getCutOffDateTime()));

				result.setReportData(new TreeMap());
				result.setTripReportData(new TreeMap());
				result.setSummaryData(new TreeMap());
				result.setDetailData(new TreeMap());
				
				for(Map.Entry<String, IHandOffBatchRoute> routeEntry : routeMapping.entrySet()) {

					IHandOffBatchRoute _route = routeEntry.getValue();
					IHandOffBatchStop _stop = null;
					OrderRouteInfoModel _model = null; 
					
					boolean isDepot = depotSessions.contains(_route.getSessionName());
					if(_route.getStops() != null) {
						Iterator _iterator = _route.getStops().iterator();
						while(_iterator.hasNext()) {
							_stop = (IHandOffBatchStop)_iterator.next();
							_model = getOrderRouteInfoModel(_route, _stop);
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

	private OrderRouteInfoModel getOrderRouteInfoModel(IHandOffBatchRoute _route, IHandOffBatchStop _stop)
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
		result.setTripId(_stop.getRoutingRouteId());
		return result;
	}
	
	private Map<String, IHandOffBatchRoute> getRouteInfo(IHandOffBatch batch) throws RoutingServiceException {
		Map<String, IHandOffBatchRoute> routeMapping = new HashMap<String, IHandOffBatchRoute>();
		
		HandOffServiceProxy proxy = new HandOffServiceProxy();
		
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
	
	
}
	
