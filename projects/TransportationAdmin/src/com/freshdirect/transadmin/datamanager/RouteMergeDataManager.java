package com.freshdirect.transadmin.datamanager;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.freshdirect.transadmin.datamanager.model.IRoutingOutputInfo;
import com.freshdirect.transadmin.datamanager.model.OrderRouteInfoModel;
import com.freshdirect.transadmin.model.TrnArea;
import com.freshdirect.transadmin.util.TransportationAdminProperties;

public class RouteMergeDataManager extends RouteOutputDataManager {
	
	private final String INVALID_RSORDERFILE = "Invalid Route Smart Order File";
	private final String INVALID_RSROUTEFILE = "Invalid Route Smart Truck File";
	private final String INVALID_RSORDERROUTEFILE = "Invalid Route Smart Order/Truck File";
		
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
		} else if (!updateRouteInfo(result.getOrders(), result.getTrucks())){
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
			while(iterator.hasNext()) {
				tmpInfo = (OrderRouteInfoModel)iterator.next();					
				if(!regularOrderIds.contains(tmpInfo.getOrderNumber())) {
					result.getRegularOrders().add(tmpInfo);							
				}					
			}
		}
	}
	
	private boolean updateRouteInfo(List orderDataList, List truckDataList) {
		
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
	
}
