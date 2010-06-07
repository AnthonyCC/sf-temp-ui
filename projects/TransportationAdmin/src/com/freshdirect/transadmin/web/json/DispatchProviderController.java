package com.freshdirect.transadmin.web.json;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.freshdirect.customer.ErpRouteMasterInfo;
import com.freshdirect.routing.model.IRouteModel;
import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.model.IRoutingStopModel;
import com.freshdirect.routing.model.RoutingSchedulerIdentity;
import com.freshdirect.routing.service.proxy.DeliveryServiceProxy;
import com.freshdirect.routing.service.proxy.RoutingEngineServiceProxy;
import com.freshdirect.routing.util.RoutingServicesProperties;
import com.freshdirect.transadmin.datamanager.report.ICommunityReport;
import com.freshdirect.transadmin.datamanager.report.XlsCommunityReport;
import com.freshdirect.transadmin.model.Dispatch;
import com.freshdirect.transadmin.model.DispatchReason;
import com.freshdirect.transadmin.model.Plan;
import com.freshdirect.transadmin.model.RouteMapping;
import com.freshdirect.transadmin.model.TrnAdHocRoute;
import com.freshdirect.transadmin.model.TrnArea;
import com.freshdirect.transadmin.model.TrnCutOff;
import com.freshdirect.transadmin.model.UserPref;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.service.DispatchManagerI;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.service.LogManagerI;
import com.freshdirect.transadmin.service.ZoneManagerI;
import com.freshdirect.transadmin.util.DispatchPlanUtil;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.util.TransportationAdminProperties;
import com.freshdirect.transadmin.web.model.DispatchCommand;
import com.freshdirect.transadmin.web.model.SpatialBoundary;
import com.freshdirect.transadmin.web.model.WebPlanInfo;
import com.freshdirect.transadmin.web.util.TransWebUtil;

public class DispatchProviderController extends JsonRpcController implements
		IDispatchProvider 
{	

	private DispatchManagerI dispatchManagerService;

	private DomainManagerI domainManagerService;

	private LogManagerI logManager;

	private ZoneManagerI zoneManagerService;
	
	private EmployeeManagerI employeeManagerService;
		
	public EmployeeManagerI getEmployeeManagerService() {
		return employeeManagerService;
	}
	public void setEmployeeManagerService(EmployeeManagerI employeeManagerService) {
		this.employeeManagerService = employeeManagerService;
	}
	public void setZoneManagerService(ZoneManagerI zoneManagerService) {
		this.zoneManagerService = zoneManagerService;
	}
	public void setLogManager(LogManagerI logManager) {
		this.logManager = logManager;
	}

	public DispatchManagerI getDispatchManagerService() {
		return dispatchManagerService;
	}

	public void setDispatchManagerService(
			DispatchManagerI dispatchManagerService) {
		this.dispatchManagerService = dispatchManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}

	public int updateRouteMapping(String routeDate, String cutOffId,
			String sessionId, boolean isDepot) {
		int result = 0;
		
		try {
			result = getDispatchManagerService().updateRouteMapping(
					TransStringUtil.getDate(routeDate), cutOffId, sessionId,
					isDepot);
		} catch (ParseException parseExp) {
			parseExp.printStackTrace();
			// Do Nothing Return 0;
		}
		return result;
	}

	public Collection getActiveRoute(String date, String zoneCode) {
		Collection results = null;

		try {
			if (zoneCode == null || zoneCode.trim().length() == 0) {
				Collection c = domainManagerService.getAdHocRoutes();
				if (c != null) {
					results = new ArrayList();
					Iterator iterator = c.iterator();
					while (iterator.hasNext()) {
						TrnAdHocRoute info = (TrnAdHocRoute) iterator.next();
						results.add(info.getRouteNumber());

					}
				}
			} else {
				Collection c = domainManagerService.getRoutes(TransStringUtil
						.getServerDate(date));
				if (c != null) {
					results = new ArrayList();
					Iterator iterator = c.iterator();
					while (iterator.hasNext()) {
						ErpRouteMasterInfo info = (ErpRouteMasterInfo) iterator
								.next();
						if (zoneCode.equals(info.getZoneNumber())) {
							results.add(info.getRouteNumber());
						}
					}
				}
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return results;
	}

	public Collection getActivityLog(String date) {
		try {
			Date fromDate = TransStringUtil.getDate(date);
			Date toDate = TransStringUtil.getDate(date);
			Calendar c = Calendar.getInstance();
			c.setTime(toDate);
			c.add(Calendar.DATE, 1);
			c.add(Calendar.SECOND, -1);
			toDate = c.getTime();

			fromDate = new Timestamp(fromDate.getTime());
			toDate = new Timestamp(toDate.getTime());
			Collection list = logManager.getLogs(fromDate, toDate);
			
			return list;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String generateCommunityReport(String routeDate, String cutOff) {
			
		Map reportData = new TreeMap();
		Map stopCounts = new HashMap();
		Set processedRoutes = new HashSet();
		String resultFile = null;
		
		try {
			Collection routingRouteIds = domainManagerService.getRouteMappingByCutOff(TransStringUtil.getServerDate(routeDate), cutOff);
			Map areaMapping = getAreaMapping(domainManagerService.getAreas());
			TrnCutOff cutOffName = domainManagerService.getCutOff(cutOff);
			
			if(routingRouteIds != null) {
				
				DeliveryServiceProxy proxy = new DeliveryServiceProxy();
				RoutingEngineServiceProxy engineProxy = new RoutingEngineServiceProxy();
				
				Iterator _itr = routingRouteIds.iterator();
				String routingRouteId = null;
				
				IRouteModel _tmpRoute = null;
				
					while(_itr.hasNext()) {
						routingRouteId = ((RouteMapping)_itr.next()).getRouteMappingId().getRouteID();
						
						if(!processedRoutes.contains(routingRouteId)) {
							processedRoutes.add(routingRouteId);
							Collection routes = domainManagerService.getRouteMapping(TransStringUtil.getServerDate(routeDate)
																						, routingRouteId);
											
							if(routes != null && routes.size() == 1) {
								
								RouteMapping routeMapping = (RouteMapping)routes.toArray()[0];	
								
								
								
								IRoutingSchedulerIdentity schedulerId = new RoutingSchedulerIdentity();
								schedulerId.setRegionId(RoutingServicesProperties.getDefaultTruckRegion());
										
								String sessionId = engineProxy.retrieveRoutingSession(schedulerId, routeMapping.getRoutingSessionID());
								
								List routingRoutes = proxy.getRoutes(TransStringUtil.getDate(routeDate), sessionId
																		, routeMapping.getRouteMappingId().getRoutingRouteID());
								
								if(routingRoutes != null && routingRoutes.size() > 0) {
									
									_tmpRoute = (IRouteModel)routingRoutes.get(0);
									if(_tmpRoute.getStops() != null && _tmpRoute.getStops().size() > 0) {
										
										stopCounts.put(routingRouteId, new Integer(_tmpRoute.getStops().size()));
										Iterator _stopsItr = _tmpRoute.getStops().iterator();
										IRoutingStopModel _stop = null;
										while(_stopsItr.hasNext()) {
																						
											_stop = (IRoutingStopModel)_stopsItr.next();
											
											String deliveryModel = ((TrnArea)areaMapping.get(routeMapping.getRouteMappingId().getGroupCode())).getDeliveryModel();
											List comunities = dispatchManagerService.matchCommunity
																	(Double.parseDouble(_stop.getLocation().getGeographicLocation().getLatitude())/1000000.0
																				, Double.parseDouble(_stop.getLocation().getGeographicLocation().getLongitude())/1000000.0
																				, deliveryModel);
											if(comunities != null && comunities.size() > 0) {
												if(!reportData.containsKey(routingRouteId)) {
													reportData.put(routingRouteId, new HashMap());
												}
												Iterator _spaItr = comunities.iterator();
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
							
						}	
					}
					resultFile = TransportationAdminProperties.getCommunityRptFileName()+System.currentTimeMillis()+".xls";
					ICommunityReport report = new XlsCommunityReport();
					report.generateCommunityReport(resultFile, reportData, stopCounts, routeDate, cutOffName.getName());
					resultFile = TransportationAdminProperties.getDownloadProviderUrl()+"?filePath="+resultFile;
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultFile;
	}
	
	

	protected Map getAreaMapping(Collection areaLst) {
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
	
	public int updateUserPref(String key,String value)
	{
		try {
			String userName=com.freshdirect.transadmin.security.SecurityManager.getUserName(getHttpServletRequest());
			UserPref pref=new UserPref();
			pref.setUserId(userName);
			pref.setKey(key);
			pref.setValue(value);
			Collection list=new ArrayList();
			list.add(pref);
			dispatchManagerService.removeEntity(list);
			dispatchManagerService.saveEntityEx(pref);	
			TransWebUtil.updatePref(getHttpServletRequest(), pref);
			return 1;
		} catch (Exception e) 
		{
			e.printStackTrace();
			return 0;
		}
	}

	public String getUserPref(String key) 
	{
		String value=TransWebUtil.getUserPref(getHttpServletRequest(), key,dispatchManagerService);
		if(value==null) value="";
		return value;
	}
	
	public Collection getActiveZones(String date)
	{
		Collection zones=domainManagerService.getZones();
		try {
			date=TransStringUtil.getServerDate(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Collection activeZoneCodes = zoneManagerService.getActiveZoneCodes(date);
    	if(zones != null && activeZoneCodes != null) {
    		Iterator _iterator = zones.iterator();
    		Zone _tmpZone = null;
    		while(_iterator.hasNext()) {
    			_tmpZone = (Zone)_iterator.next();
    			if(!activeZoneCodes.contains(_tmpZone.getZoneCode())) {
    				_iterator.remove();
    			}
    		}
    	}
    	List result=new ArrayList();
    	for(Iterator i=zones.iterator();i.hasNext();)
    	{
    		Zone tmpZone =(Zone)i.next();
    		Zone z=new Zone();
    		z.setZoneCode(tmpZone.getZoneCode());
    		z.setName(tmpZone.getDisplayName());  
    		result.add(z);
    	}
    	return result;
	}
	public int addReasonCode(String reason) 
	{
		try {
			DispatchReason r=new DispatchReason();
			r.setReason(reason);
			dispatchManagerService.saveEntity(r);
			return 0;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	public Collection getReasonCode(boolean active) 
	{
		try {
			return dispatchManagerService.getDispatchReasons(active);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public int setReasonCode(String code,String reason, boolean enable) {
		try {
			DispatchReason r=new DispatchReason();
			r.setCode(code);
			r.setReason(reason);
			r.setActive(enable);
			dispatchManagerService.saveEntity(r);
			return 0;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	
	public WebPlanInfo getPlanForResource(String date, String resourceId, String planId) {
		List planInfos = new ArrayList();
		try {
			Collection plans = dispatchManagerService.getPlanForResource(TransStringUtil.getServerDate(date), resourceId);
						
			Iterator it=plans.iterator();
			while(it.hasNext()) {

				Plan plan=(Plan)it.next();
				Zone zone=null;
				if(plan.getZone()!=null) {
					zone = domainManagerService.getZone(plan.getZone().getZoneCode());
				}
				WebPlanInfo planInfo = DispatchPlanUtil.getWebPlanInfo(plan, zone, employeeManagerService);
				planInfos.add(planInfo);
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WebPlanInfo resultPlan = null;
		Iterator _itr = planInfos.iterator();
		while(_itr.hasNext()) {
			WebPlanInfo _tmpPlan = (WebPlanInfo)_itr.next();
			if(!_tmpPlan.getPlanId().equalsIgnoreCase(planId) || planId == null) {
				resultPlan = _tmpPlan;
				break;
			}
		}
		return resultPlan;
	}
	
	public DispatchCommand getDispatchForResource(String date, String resourceId, String dispatchId) {
		List dispatchInfos = new ArrayList();
		try {
			Collection dispatches = dispatchManagerService.getDispatchForResource(TransStringUtil.getServerDate(date), resourceId);
						
			Iterator it = dispatches.iterator();
			while(it.hasNext()) {

				Dispatch dispatch = (Dispatch) it.next();
				Zone zone=null;
				if(dispatch.getZone()!=null) {
					zone = domainManagerService.getZone(dispatch.getZone().getZoneCode());
				}
				DispatchCommand command = DispatchPlanUtil.getDispatchCommand(dispatch, zone, employeeManagerService,null,null,null);
				dispatchInfos.add(command);
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DispatchCommand resultDispatch = null;
		Iterator _itr = dispatchInfos.iterator();
		while(_itr.hasNext()) {
			DispatchCommand _tmpDispatch = (DispatchCommand)_itr.next();
			if(!_tmpDispatch.getDispatchId().equalsIgnoreCase(dispatchId) || dispatchId == null) {
				resultDispatch = _tmpDispatch;
				break;
			}
		}
		return resultDispatch;
	}
	
}
