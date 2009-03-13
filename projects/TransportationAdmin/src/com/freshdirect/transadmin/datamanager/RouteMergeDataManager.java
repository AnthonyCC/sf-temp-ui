package com.freshdirect.transadmin.datamanager;

import java.io.ByteArrayInputStream;
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
import com.freshdirect.transadmin.util.ModelUtil;
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

	protected void preProcessRoutingOutput(IRoutingOutputInfo routingInfo, RoutingResult result, IServiceProvider serviceProvider) {
		
		Collection areas = serviceProvider.getDomainManagerService().getAreas();
		
		Map hshRoutingArea = getRoutingAreaMapping(areas);
		
		Map hshDepotArea = getRoutingDepotAreaMapping(areas);
		
		OrderAreaGroup truckOrderGroup = groupOrderRouteInfo(result.getDepotOrders(), hshDepotArea, null);
		
		OrderAreaGroup regularOrderGroup = groupOrderRouteInfo(result.getRegularOrders(), hshRoutingArea, truckOrderGroup.getMissingAreas());
				
		OrderAreaGroup truckOrderProcessingGroup = groupOrderRouteInfo(ModelUtil.mapToList(truckOrderGroup.getOrderGroup()), hshDepotArea, null);
		
		if(regularOrderGroup.getMissingAreas() != null && regularOrderGroup.getMissingAreas().size() > 0) {
			result.addError("Areas "+ regularOrderGroup.getMissingAreas().toString()+ " are missing in roadnet regular file");
		}
		
		if(truckOrderGroup.getMissingAreas() != null && truckOrderGroup.getMissingAreas().size() > 0) {
			result.addError("Areas "+ truckOrderGroup.getMissingAreas().toString()+ " are missing in roadnet depot file");
		}
		
		if(truckOrderProcessingGroup.getMissingAreas() != null && truckOrderProcessingGroup.getMissingAreas().size() > 0) {
			result.addError("Areas "+ truckOrderProcessingGroup.getMissingAreas().toString()+ " are missing in roadnet depot file");
		}
		
		result.setRegularOrders(ModelUtil.mapToList(regularOrderGroup.getOrderGroup()));
		
		result.setDepotOrders(ModelUtil.mapToList(truckOrderGroup.getOrderGroup()));
		
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
