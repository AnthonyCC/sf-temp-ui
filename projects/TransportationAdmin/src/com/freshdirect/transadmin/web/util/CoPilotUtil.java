package com.freshdirect.transadmin.web.util;

import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.routing.model.GeoPoint;
import com.freshdirect.routing.model.IGeoPoint;
import com.freshdirect.routing.model.IRouteModel;
import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.model.IRoutingStopModel;
import com.freshdirect.routing.model.RoutingSchedulerIdentity;
import com.freshdirect.routing.service.proxy.DeliveryServiceProxy;
import com.freshdirect.routing.service.proxy.HandOffServiceProxy;
import com.freshdirect.routing.service.proxy.RoutingEngineServiceProxy;
import com.freshdirect.routing.util.RoutingServicesProperties;
import com.freshdirect.transadmin.model.RouteMapping;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.util.ModelUtil;
import com.freshdirect.transadmin.util.TransStringUtil;

public class CoPilotUtil {
	

	public static ModelAndView gpsDrivingDirectionsHandlerEx(HttpServletRequest request, HttpServletResponse response
																, DomainManagerI domainManagerService) throws ServletException {

		List routingRouteIds = Arrays.asList(StringUtil.decodeStrings(request.getParameter("routeId")));
		String routeDate = request.getParameter("rdate");
		StringBuffer strBuf = new StringBuffer();
		String carriageReturn = "\r\n";
		
		try {
			if(routingRouteIds != null && routingRouteIds.size() > 0) {

				DeliveryServiceProxy proxy = new DeliveryServiceProxy();
				RoutingEngineServiceProxy engineProxy = new RoutingEngineServiceProxy();
				HandOffServiceProxy handOffProxy = new HandOffServiceProxy();

				Iterator _itr = routingRouteIds.iterator();
				String routingRouteId = null;
				Map directionRoutes = new TreeMap();
				IRouteModel route = null;

				routingRouteId = (String)_itr.next();

				route = handOffProxy.getHandOffBatchStopsByRoute(TransStringUtil.getDate(routeDate), routingRouteId, true);
				if(route == null) {
					Collection routes = domainManagerService.getRouteMapping(TransStringUtil.getServerDate(routeDate), routingRouteId);

					if(routes != null && routes.size() == 1) {

						RouteMapping routeMapping = (RouteMapping)routes.toArray()[0];	

						IRoutingSchedulerIdentity schedulerId = new RoutingSchedulerIdentity();
						schedulerId.setRegionId(RoutingServicesProperties.getDefaultTruckRegion());

						String sessionId = engineProxy.retrieveRoutingSession(schedulerId, routeMapping.getRoutingSessionID());

						List routingRoutes = proxy.getRoutes(TransStringUtil.getDate(routeDate), sessionId
								, routeMapping.getRouteMappingId().getRoutingRouteID());
						route = (IRouteModel)routingRoutes.get(0);
					} 
				}
					if(route != null) {

						if(route.getStops() != null && route.getStops().size() > 0) {
							if(route != null) {
								route.getStops().add(ModelUtil.getStop(Integer.MIN_VALUE, "DPT-FD", "", "",
										"", "40740250", "-73951989", true));
								route.getStops().add(ModelUtil.getStop(Integer.MAX_VALUE, "DPT-FD", "", "",
										"", "40740250", "-73951989", true));


								List points = new ArrayList();

								Iterator stopIterator = route.getStops().iterator();
								IRoutingStopModel _stop = null;
								IGeoPoint _geoPoint = null;

								while (stopIterator.hasNext()) {

									_stop = (IRoutingStopModel) stopIterator.next();
									_geoPoint = new GeoPoint();
									if(TransStringUtil.isValidInteger(_stop.getDeliveryInfo().getDeliveryLocation()
											.getBuilding().getGeographicLocation().getLatitude())) {
										_geoPoint.setLatitude(Integer.parseInt(_stop.getDeliveryInfo().getDeliveryLocation()
												.getBuilding().getGeographicLocation().getLatitude()));
									} else {
										_geoPoint.setLatitude((int)(Double.parseDouble(_stop.getDeliveryInfo().getDeliveryLocation()
												.getBuilding().getGeographicLocation().getLatitude()) * 1000000.0));

									}

									if(TransStringUtil.isValidInteger(_stop.getDeliveryInfo().getDeliveryLocation()
											.getBuilding().getGeographicLocation().getLongitude())) {
										_geoPoint.setLongitude(Integer.parseInt(_stop.getDeliveryInfo().getDeliveryLocation()
												.getBuilding().getGeographicLocation().getLongitude()));
									} else {
										_geoPoint.setLongitude((int)(Double.parseDouble(_stop.getDeliveryInfo().getDeliveryLocation()
												.getBuilding().getGeographicLocation().getLongitude()) * 1000000.0));

									}
									
									points.add(_geoPoint);
								}
								route.setDrivingDirection(proxy.buildDriverDirections(points));

								Object[] _stops = route.getStops().toArray();								
								IRoutingStopModel _nextStop = null;
								
								strBuf.append(getGPSMetaData());
								
								String strStopNo = "";
								String zipCode = "";
								String addLocation = "";
								if(_stops != null) {
									for(int intCount=0; intCount<_stops.length; intCount++) {
										_nextStop = (IRoutingStopModel)_stops[intCount];
										
										strBuf.append(carriageReturn).append("Start Stop=Stop ").append(intCount).append(carriageReturn);
										strBuf.append("Name=Stop ").append(intCount).append(carriageReturn);
										strBuf.append("Address=").append(carriageReturn);
										strBuf.append("City=").append(carriageReturn);
										strBuf.append("State=").append(carriageReturn);
										strBuf.append("County=").append(carriageReturn);
										strBuf.append("Zip=").append(carriageReturn);
										strBuf.append("Longitude=");
										if(TransStringUtil.isValidInteger(_nextStop.getDeliveryInfo().getDeliveryLocation()
												.getBuilding().getGeographicLocation().getLongitude())) {
											strBuf.append(Integer.parseInt(_nextStop.getDeliveryInfo().getDeliveryLocation()
													.getBuilding().getGeographicLocation().getLongitude())).append(carriageReturn);
										} else {
											strBuf.append((int)(Double.parseDouble(_nextStop.getDeliveryInfo().getDeliveryLocation()
													.getBuilding().getGeographicLocation().getLongitude()) * 1000000.0)).append(carriageReturn);

										}
										
										
										strBuf.append("Latitude=");
										if(TransStringUtil.isValidInteger(_nextStop.getDeliveryInfo().getDeliveryLocation()
												.getBuilding().getGeographicLocation().getLatitude())) {
											

											strBuf.append(Integer.parseInt(_nextStop.getDeliveryInfo().getDeliveryLocation()
													.getBuilding().getGeographicLocation().getLatitude())).append(carriageReturn);
										} else {
											strBuf.append((int)(Double.parseDouble(_nextStop.getDeliveryInfo().getDeliveryLocation()
													.getBuilding().getGeographicLocation().getLatitude()) * 1000000.0)).append(carriageReturn);

										}										
										
										strBuf.append("Version=2").append(carriageReturn);
										strBuf.append("Grid=-1").append(carriageReturn);
										strBuf.append("Link=-1").append(carriageReturn);
										strBuf.append("Distance=0").append(carriageReturn);
										strBuf.append("Percent=1200").append(carriageReturn);
										strBuf.append("Grid1=-1").append(carriageReturn);
										strBuf.append("Link1=-1").append(carriageReturn);
										strBuf.append("Distance1=0").append(carriageReturn);
										strBuf.append("Percent1=5000").append(carriageReturn);
										strBuf.append("Grid2=-1").append(carriageReturn);
										strBuf.append("Link2=-1").append(carriageReturn);
										strBuf.append("Distance2=0").append(carriageReturn);
										strBuf.append("Percent2=5000").append(carriageReturn);
										strBuf.append("Grid3=-1").append(carriageReturn);
										strBuf.append("Link3=-1").append(carriageReturn);
										strBuf.append("Distance3=0").append(carriageReturn);
										strBuf.append("Percent3=5000").append(carriageReturn);
										strBuf.append("Region=").append(carriageReturn);
										strBuf.append("Direction=3").append(carriageReturn);
										strBuf.append("Code=1").append(carriageReturn);
										strBuf.append("Glyph=0").append(carriageReturn);
										strBuf.append("Type=4").append(carriageReturn);
										strBuf.append("Size=0").append(carriageReturn);
										strBuf.append("Show=1").append(carriageReturn);
										strBuf.append("Sequence=0").append(carriageReturn);
										strBuf.append("End Stop").append(carriageReturn);
										strBuf.append(getStopOpt(intCount));
									}
								}								
							}
						} 
					}
				}
			
			//response.setBufferSize((int)strBuf.length());
			String encoding = "UTF-16LE";
			response.setHeader("Content-Disposition", "attachment; filename=\""+"fdcopilot.trp"+"\"");
			//response.setContentType("application/x-download; charset=UTF-8");
			response.setContentType("application/octet-stream; charset="+encoding);
			
			response.setHeader("Pragma", "public");
			response.setHeader("Cache-Control", "max-age=0");
			//response.setContentLength((int)strBuf.length());
			response.setCharacterEncoding(encoding);
			
			OutputStreamWriter out = new OutputStreamWriter(response.getOutputStream(), Charset.forName(encoding));
			
			out.write(strBuf.toString());
			out.close();

			//FileCopyUtils.copy(strBuf.toString().getBytes(Charset.forName("UTF-8")), response.getOutputStream());
			
		} catch (Exception e) {
			e.printStackTrace();
			//saveMessage(request, getMessage("app.actionmessage.123", null));
		}
		
		
		return null;
	}
	
	private static String getStopOpt(int stopNo) {
		String carriageReturn = "\r\n";
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(carriageReturn).append("Start StopOpt=Stop ").append(stopNo).append(carriageReturn);
		strBuf.append("Cost=0").append(carriageReturn);
		strBuf.append("Time=0").append(carriageReturn);
		strBuf.append("Loaded=1").append(carriageReturn);
		strBuf.append("OnDuty=1").append(carriageReturn);
		strBuf.append("End StopOpt").append(carriageReturn);
		return strBuf.toString();
	}
	
	private static String getGPSMetaData() {
		String carriageReturn = "\r\n";
		StringBuffer strBuf = new StringBuffer();
		
		strBuf.append("Data Version:4.5.2.1").append(carriageReturn);
		strBuf.append("Start Trip=\\Storage Card\\copilot\\NA\\North America Truck\\save\\Testtrip.trp").append(carriageReturn);
		strBuf.append("Memo=").append(carriageReturn);
		strBuf.append("DefaultTrip=").append(carriageReturn);
		strBuf.append("HasRoadSpeeds=0").append(carriageReturn);
		strBuf.append("HasStopOptions=1").append(carriageReturn);
		strBuf.append("RouteType=Quickest").append(carriageReturn);
		strBuf.append("VehicleType=Truck").append(carriageReturn);
		strBuf.append("RoundTrip=0").append(carriageReturn);
		strBuf.append("HubRouting=0").append(carriageReturn);
		strBuf.append("SeqType=0").append(carriageReturn);
		strBuf.append("FavorAvoid=1").append(carriageReturn);
		strBuf.append("DistUnits=0").append(carriageReturn);
		strBuf.append("TakeABreak=0").append(carriageReturn);
		strBuf.append("BreakTime=0").append(carriageReturn);
		strBuf.append("BreakWaitTime=0").append(carriageReturn);
		strBuf.append("BorderWaitTime=0").append(carriageReturn);
		strBuf.append("RestrTrucks=1").append(carriageReturn);
		strBuf.append("RestrTrucksOnly=0").append(carriageReturn);
		strBuf.append("TranslateAlias=0").append(carriageReturn);
		strBuf.append("StateOrder=1").append(carriageReturn);
		strBuf.append("FerryMiles=1").append(carriageReturn);
		strBuf.append("LoadedCost=13600").append(carriageReturn);
		strBuf.append("EmptyCost=13600").append(carriageReturn);
		strBuf.append("FirstStop=1").append(carriageReturn);
		strBuf.append("HazType=0").append(carriageReturn);
		strBuf.append("BordersOpen=1").append(carriageReturn);
		strBuf.append("CongestionZonesOpen=2").append(carriageReturn);
		strBuf.append("TollAvoid=0").append(carriageReturn);
		strBuf.append("NationalNetwork=0").append(carriageReturn);
		strBuf.append("53FootRouting=0").append(carriageReturn);
		strBuf.append("AccessRule=0").append(carriageReturn);
		strBuf.append("CountriesAsStates=0").append(carriageReturn);
		strBuf.append("ScenicRoute=0").append(carriageReturn);
		strBuf.append("PropaneRestricted=0").append(carriageReturn);
		strBuf.append("TollAdjust=175").append(carriageReturn);
		strBuf.append("TollType=Tolls Off").append(carriageReturn);
		strBuf.append("TollDiscounts=0").append(carriageReturn);
		strBuf.append("M2MRouting=0").append(carriageReturn);
		strBuf.append("M2MTime=Nominal").append(carriageReturn);
		strBuf.append("Length=-1").append(carriageReturn);
		strBuf.append("Width=-1").append(carriageReturn);
		strBuf.append("Height=-1").append(carriageReturn);
		strBuf.append("Weight=-1").append(carriageReturn);
		strBuf.append("WeightPerAxle=-1").append(carriageReturn);
		strBuf.append("UseHighwayOnly=0").append(carriageReturn);
		strBuf.append("TollClosed=0").append(carriageReturn);
		strBuf.append("FerryClosed=0").append(carriageReturn);
		strBuf.append("RVHeightRestr=150").append(carriageReturn);
		strBuf.append("RoadWeightAdjust=646464646464646464").append(carriageReturn);
		strBuf.append("DefAutoRoadSpeeds=0141412D1E05191E120137371E14050F190A").append(carriageReturn);
		strBuf.append("DefVanRoadSpeeds=0141412D1E05191E120137371E14050F190A").append(carriageReturn);
		strBuf.append("DefRVRoadSpeeds=013B3B291B05171B100137371E14050F190A").append(carriageReturn);
		strBuf.append("DefTruckRoadSpeeds=01323B190F050A1B05013232190F050A1705").append(carriageReturn);
		strBuf.append("DefBusRoadSpeeds=013B3B291B05171B100132321B12050E1709").append(carriageReturn);
		strBuf.append("DefTruckEditRoadSpeeds=013B3B291B05171B100132321B12050E1709").append(carriageReturn);
		strBuf.append("DefMotorcycleRoadSpeeds=0141412D1E05191E120137371E14050F190A").append(carriageReturn);
		strBuf.append("DefBicycleRoadSpeeds=0A0A0A0A0A0A0A0A0A0A0A0A0A0A0A0A0A0A").append(carriageReturn);
		strBuf.append("DefWalkingRoadSpeeds=030303030303030303030303030303030303").append(carriageReturn);
		strBuf.append("DefCrowRoadSpeeds=030303030303030303030303030303030303").append(carriageReturn);
		strBuf.append("RptCurrency=US").append(carriageReturn);
		strBuf.append("RptConvRate=10000").append(carriageReturn);
		strBuf.append("Units=0").append(carriageReturn);
		strBuf.append("OversizePermit=0").append(carriageReturn);
		strBuf.append("NumAxles=5").append(carriageReturn);
		strBuf.append("LCV=0").append(carriageReturn);
		strBuf.append("CountryAbrev=4").append(carriageReturn);
		strBuf.append("FuelUnit=0").append(carriageReturn);
		strBuf.append("FuelCost=27100").append(carriageReturn);
		strBuf.append("MaintenenceCostLoaded=1200").append(carriageReturn);
		strBuf.append("MaintenenceCostEmpty=1200").append(carriageReturn);
		strBuf.append("CostTimeLoaded=401100").append(carriageReturn);
		strBuf.append("CostTimeEmpty=401100").append(carriageReturn);
		strBuf.append("GreenHouseGas=222000").append(carriageReturn);
		strBuf.append("MPGCostLoaded=60000").append(carriageReturn);
		strBuf.append("MPGCostEmpty=60000").append(carriageReturn);
		strBuf.append("End Trip").append(carriageReturn);
		
		return strBuf.toString();
	}
}
