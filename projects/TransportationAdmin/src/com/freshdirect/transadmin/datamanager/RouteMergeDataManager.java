package com.freshdirect.transadmin.datamanager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.freshdirect.transadmin.datamanager.model.IRoutingOutputInfo;
import com.freshdirect.transadmin.datamanager.model.OrderRouteInfoModel;
import com.freshdirect.transadmin.model.TrnArea;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.util.TransportationAdminProperties;
import com.freshdirect.transadmin.web.model.RSFileMergeCommand;

public class RouteMergeDataManager extends RouteOutputDataManager {
	
	private final String INVALID_RSORDERFILE = "Invalid Route Smart Order File";
	private final String INVALID_RSROUTEFILE = "Invalid Route Smart Truck File";
	private final String INVALID_RSORDERROUTEFILE = "Invalid Route Smart Order/Truck File";
	private final String NODATA_RSORDERROUTEFILE = "No Data in Route Smart Order/Truck File";
		
	protected void collectOrders(IRoutingOutputInfo routingInfo, RoutingResult result) {
		
		result.setOrders(fileManager.parseRouteFile(TransportationAdminProperties.getErpOrderInputFormat()
				, new ByteArrayInputStream(routingInfo.getOrderFile()), ROW_IDENTIFIER, ROW_BEAN_IDENTIFIER
				, null));
		
		result.setTrucks(fileManager.parseRouteFile(TransportationAdminProperties.getErpRouteInputFormat()
				, new ByteArrayInputStream(routingInfo.getTruckFile()), ROW_IDENTIFIER, ROW_BEAN_IDENTIFIER
				, null));		
		
		super.collectOrders(routingInfo, result);
	}
	
	protected void validateData(IRoutingOutputInfo routingInfo, RoutingResult result, IServiceProvider serviceProvider) {
		
		StringBuffer strBuf = new StringBuffer();
		if(result.getOrders() == null || result.getOrders().size() == 0) {
			strBuf.append(INVALID_RSORDERFILE);
		}
		
		if(result.getTrucks() == null || result.getTrucks().size() == 0) {
			if(strBuf.length() > 0) {
				strBuf.append(", ");
			}
			strBuf.append(INVALID_RSROUTEFILE);
		}			
		
		if(strBuf.length() > 0) {
			result.addError(strBuf.toString());
		} else if (!updateRouteInfo(result.getOrders(), result.getTrucks(), null)){
			result.addError(INVALID_RSORDERROUTEFILE);
		}
		
		super.validateData(routingInfo, result, serviceProvider);
	}

	protected Object preProcessRoutingOutput(IRoutingOutputInfo routingInfo, RoutingResult result, IServiceProvider serviceProvider) {
		
		OrderAreaGroup orderGrp = (OrderAreaGroup) super.preProcessRoutingOutput(routingInfo, result, serviceProvider);
		
		Set routingAreaCodes = orderGrp.getRoutingAreaCodes();
		List missingAreas = new ArrayList();
		
		if(routingAreaCodes != null) {
			
			result.setRegularOrders(new ArrayList());
			result.setDepotOrders(new ArrayList());
			
			Iterator _iterator = routingAreaCodes.iterator();
			String _areaCode = null;
			TrnArea _tmpArea = new TrnArea();
			List orders = null;
			boolean foundInDepot = false;
			
			while(_iterator.hasNext()) {
				
				_areaCode = (String)_iterator.next();
				_tmpArea.setCode(_areaCode);
				orders = null;
				foundInDepot = false;
				
				if(orderGrp.getTruckOrderGroup() != null && orderGrp.getTruckOrderGroup().containsKey(_tmpArea)) {
					orders = (List)orderGrp.getTruckOrderGroup().get(_tmpArea);
				} else if (orderGrp.getDepotOrderGroup() != null && orderGrp.getDepotOrderGroup().containsKey(_tmpArea)) {
					foundInDepot = true;
					orders = (List)orderGrp.getDepotOrderGroup().get(_tmpArea);
				} else {
					missingAreas.add(_areaCode);
				}
				
				if(orders != null) {
					if(foundInDepot) {
						result.getDepotOrders().addAll(orders);
					} else {
						result.getRegularOrders().addAll(orders);
					}
				}
			}
		}
		
		if(missingAreas.size() > 0) {
			result.addError("Areas "+ missingAreas.toString()+ " are missing in roadnet file");
		}
		
		return orderGrp;
	}
	
	protected void postProcessRoutingOutput(IRoutingOutputInfo routingInfo, RoutingResult result, IServiceProvider serviceProvider) {
		
		Set regularOrderIds = new TreeSet();
		Set rnZones = getRoutingZoneMapping(serviceProvider.getDomainManagerService().getZones());
		
		Set rnMissingAreas = new TreeSet();
		
		if(result.getRegularOrders() != null) {
			Iterator iterator = result.getRegularOrders().iterator();
			OrderRouteInfoModel tmpInfo = null;
			while(iterator.hasNext()) {
				tmpInfo = (OrderRouteInfoModel)iterator.next();					
				regularOrderIds.add(tmpInfo.getOrderNumber());
			}
		}
		
		if(result.getOrders() != null) {
			Iterator iterator = result.getOrders().iterator();
			OrderRouteInfoModel tmpInfo = null;
			String _tmpZoneId = null;
			
			while(iterator.hasNext()) {
				tmpInfo = (OrderRouteInfoModel)iterator.next();					
				if(!regularOrderIds.contains(tmpInfo.getOrderNumber())) {
					_tmpZoneId = TransStringUtil.getZoneNumber(tmpInfo.getRouteId());
					if(rnZones.contains(_tmpZoneId)) {
						rnMissingAreas.add(_tmpZoneId);
					} else {
						result.getRegularOrders().add(tmpInfo);
					}
				}					
			}
			iterator = rnMissingAreas.iterator();
			while(iterator.hasNext()) {
				result.addError("Orders missing in roadnet file for Route /w UPS zone "+iterator.next());
			}
		}
	}
	
	private boolean updateRouteInfo(List orderDataList, List truckDataList, List orderIds) {
		
		Map routeMap = new HashMap();		
				
		String routeId = null;
		if(truckDataList != null) {
			OrderRouteInfoModel tmpRouteInfo = null;
			Iterator iterator = truckDataList.iterator();
			while(iterator.hasNext()) {
				tmpRouteInfo = (OrderRouteInfoModel)iterator.next();
				routeId = tmpRouteInfo.getRouteId();
				if(!routeMap.containsKey(routeId)) {
					routeMap.put(routeId, tmpRouteInfo);
				}
			}
		}	
		
		if(orderDataList != null) {
			OrderRouteInfoModel tmpRouteInfo = null;
			OrderRouteInfoModel tmpOrderInfo = null;
			Iterator iterator = orderDataList.iterator();
			while(iterator.hasNext()) {
				tmpOrderInfo = (OrderRouteInfoModel)iterator.next();
				routeId = tmpOrderInfo.getRouteId();
				if(orderIds != null) {
					if(orderIds.contains(tmpOrderInfo.getOrderNumber())) {
						return false;
					} else {
						orderIds.add(tmpOrderInfo.getOrderNumber());
					}
				}
				if(routeMap.containsKey(routeId)) {
					tmpRouteInfo = (OrderRouteInfoModel)routeMap.get(routeId);
					tmpOrderInfo.setPlant(tmpRouteInfo.getPlant());
					tmpOrderInfo.setDeliveryDate(tmpRouteInfo.getDeliveryDate());
					tmpOrderInfo.setDeliveryModel(tmpRouteInfo.getDeliveryModel());
					tmpOrderInfo.setRouteStartTime(tmpRouteInfo.getRouteStartTime());
				} else {
					return false;
				}
			}
		}
		return true;
	}
	
	protected Map getRoutingDepotAreaMapping(Collection areas) {
		
		Map result = new HashMap();
		
		TrnArea tmpArea = null;
		if(areas != null) {
			Iterator iterator = areas.iterator();
			while(iterator.hasNext()) {
				tmpArea = (TrnArea)iterator.next();				
				if(tmpArea != null && "X".equalsIgnoreCase(tmpArea.getIsDepot()) && "X".equalsIgnoreCase(tmpArea.getActive())) {
					result.put(tmpArea.getCode(), tmpArea);
				}
			}
		}
		return result;
	}
	
	public RoutingResult process(RSFileMergeCommand routingInfo ,String userName) throws IOException {
		
		long time = System.currentTimeMillis();
		String outputFileName1 = TransportationAdminProperties.getRoutingOutputOrderFilename()+userName+time;
		String outputFileName2 = TransportationAdminProperties.getRoutingOutputTruckFilename()+userName+time;
		
		List orders = new ArrayList();
		List trucks = new ArrayList();
		
		RoutingResult result = new RoutingResult();
		
		if((routingInfo.getOrderFile1() == null || routingInfo.getOrderFile1().length == 0 
					|| routingInfo.getTruckFile1() == null || routingInfo.getTruckFile1().length == 0) 
				&& (routingInfo.getOrderFile2() == null || routingInfo.getOrderFile2().length == 0 
						|| routingInfo.getTruckFile2() == null || routingInfo.getTruckFile2().length == 0)
				&& (routingInfo.getOrderFile3() == null || routingInfo.getOrderFile3().length == 0 
						|| routingInfo.getTruckFile3() == null || routingInfo.getTruckFile3().length == 0)) {
			result.addError(NODATA_RSORDERROUTEFILE);
		} else {
			List orderIds = new ArrayList();
			collectMergeData(result, orderIds, orders, trucks, routingInfo.getOrderFile1(), routingInfo.getTruckFile1(), 1);
			collectMergeData(result, orderIds, orders, trucks, routingInfo.getOrderFile2(), routingInfo.getTruckFile2(), 2);
			collectMergeData(result, orderIds, orders, trucks, routingInfo.getOrderFile3(), routingInfo.getTruckFile3(), 3);
			
			result.setOutputFile1(createFile(outputFileName1, "."+TransportationAdminProperties.getFilenameSuffix()).getAbsolutePath());
			result.setOutputFile2(createFile(outputFileName2, "."+TransportationAdminProperties.getFilenameSuffix()).getAbsolutePath());
		    
			fileManager.generateRouteFile(TransportationAdminProperties.getErpOrderInputFormat()
					, result.getOutputFile1(), ROW_IDENTIFIER, ROW_BEAN_IDENTIFIER, orders
					, null);
			fileManager.generateRouteFile(TransportationAdminProperties.getErpRouteInputFormat()
					, result.getOutputFile2(), ROW_IDENTIFIER, ROW_BEAN_IDENTIFIER, trucks
					, null);
		}
		
		return result;
	}
	
	private void collectMergeData(RoutingResult result, List orderIds, List orderLst
									, List truckLst, byte[] orders, byte[] trucks
			, int index) {
		
		List tmpOrders = null;
		List tmpTrucks = null;
		if(orders != null && orders.length > 0) {
			tmpOrders = fileManager.parseRouteFile(TransportationAdminProperties.getErpOrderInputFormat()
					, new ByteArrayInputStream(orders), ROW_IDENTIFIER, ROW_BEAN_IDENTIFIER
					, null);
			if(tmpOrders == null || tmpOrders.size() == 0) {
				result.addError(INVALID_RSORDERFILE+" "+index);
			} else {
				orderLst.addAll(tmpOrders);
			}
		}
		
		if(trucks != null && trucks.length > 0) {
			tmpTrucks = fileManager.parseRouteFile(TransportationAdminProperties.getErpRouteInputFormat()
					, new ByteArrayInputStream(trucks), ROW_IDENTIFIER, ROW_BEAN_IDENTIFIER
					, null);
			if(tmpTrucks == null || tmpTrucks.size() == 0) {
				result.addError(INVALID_RSROUTEFILE+" "+index);
			} else {
				if (!updateRouteInfo(tmpOrders, tmpTrucks, orderIds)){
					result.addError(INVALID_RSORDERROUTEFILE+" "+index);
				}
				truckLst.addAll(tmpTrucks);
			}
		}
	}
	
}
