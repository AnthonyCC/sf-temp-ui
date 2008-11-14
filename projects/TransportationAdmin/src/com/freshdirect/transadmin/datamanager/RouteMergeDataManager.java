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

import com.freshdirect.routing.util.IRoutingParamConstants;
import com.freshdirect.transadmin.datamanager.model.OrderRouteInfoModel;
import com.freshdirect.transadmin.model.TrnArea;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.util.TransportationAdminProperties;

public class RouteMergeDataManager extends RouteDataManager {
	
	private final String INVALID_RSORDERFILE = "Invalid Route Smart Order File";
	private final String INVALID_RSROUTEFILE = "Invalid Route Smart Truck File";
	private final String INVALID_RSORDERROUTEFILE = "Invalid Route Smart Order/Truck File";
	private final String INVALID_ORDERROUTEFILE = "Invalid RoadNet Order/Truck File";
	
	public RoutingResult process(byte[] inputInfo1, byte[] inputInfo2, byte[] inputInfo3,
			String userName, Map paramMap, DomainManagerI domainManagerService) throws IOException  {

		long time = System.currentTimeMillis();
		
		String cutOff = (String)paramMap.get(IRoutingParamConstants.ROUTING_CUTOFF);
		String outputFileName1 = TransportationAdminProperties.getRoutingOutputOrderFilename()+userName+time;
		String outputFileName2 = TransportationAdminProperties.getRoutingOutputTruckFilename()+userName+time;
		RoutingResult result = initResult(outputFileName1, outputFileName2, null);
						
		List orderDataList = fileManager.parseRouteFile(TransportationAdminProperties.getErpOrderInputFormat()
				, new ByteArrayInputStream(inputInfo2), ROW_IDENTIFIER, ROW_BEAN_IDENTIFIER
				, null);

		List truckDataList = fileManager.parseRouteFile(TransportationAdminProperties.getErpRouteInputFormat()
				, new ByteArrayInputStream(inputInfo1), ROW_IDENTIFIER, ROW_BEAN_IDENTIFIER
				, null);
						
		List fullDataList = fileManager.parseRouteFile(TransportationAdminProperties.getRoutingOrderRouteOutputFormat()
				, new ByteArrayInputStream(inputInfo3), ROW_IDENTIFIER, ROW_BEAN_IDENTIFIER
				, null);
		
				
		StringBuffer strBuf = new StringBuffer();
		if(orderDataList == null || orderDataList.size() == 0) {
			strBuf.append(INVALID_RSORDERFILE);
		}
		
		if(truckDataList == null || truckDataList.size() == 0) {
			if(strBuf.length() > 0) {
				strBuf.append(", ");
			}
			strBuf.append(INVALID_RSROUTEFILE);
		}
		
		if(fullDataList == null || fullDataList.size() == 0) {
			if(strBuf.length() > 0) {
				strBuf.append(", ");
			}
			strBuf.append(INVALID_ORDERROUTEFILE);
		}
		
		if(strBuf.length() > 0) {
			result.addError(strBuf.toString());
		}
				
		if(result.getErrors() == null || result.getErrors().size() == 0) {
			boolean updateResult = updateRouteInfo(orderDataList, truckDataList);
			if(updateResult) {
				processRoutingMerge(result, cutOff, orderDataList, fullDataList, domainManagerService);
			} else {
				result.addError(INVALID_RSORDERROUTEFILE);
			}
		}
				
		return result;
	}
	
	private void processRoutingMerge(RoutingResult result, String cutOff, List orderDataList, 
													List fullDataList, DomainManagerI domainManagerService) {
		
		Map areaMap = getAreas(domainManagerService);
		
		OrderAreaGroup orderGroup = groupOrderRouteInfo(fullDataList, areaMap);
		try {
			RouteGenerationResult routeGenResult = generateRouteNumber(collectOrdersFromMap(orderGroup.getOrderGroup()), cutOff, domainManagerService);	
			
			fullDataList = routeGenResult.getRouteInfos();		
			result.setRouteNoSaveInfos(routeGenResult.getRouteNoSaveInfos());
			
			Set areaCodes = orderGroup.getOrderGroup().keySet();	
			
			List missingAreas = checkMissingAreas(areaMap, orderGroup.getOrderGroup());
			if(missingAreas.size() == 0) {
	
				List mergeDataList =  new ArrayList();
				String strArea = null;
	
				if(areaCodes != null) {	
					Iterator iterator = areaCodes.iterator();
					while(iterator.hasNext()) {
						strArea = (String)iterator.next();
						List dataList = (List)orderGroup.getOrderGroup().get(strArea);
						mergeDataList.addAll(dataList);
					}
				}
				
				if(orderDataList != null) {
					Iterator iterator = orderDataList.iterator();
					OrderRouteInfoModel tmpInfo = null;
					while(iterator.hasNext()) {
						tmpInfo = (OrderRouteInfoModel)iterator.next();					
						if(!orderGroup.getOrderIds().contains(tmpInfo.getOrderNumber())) {
							mergeDataList.add(tmpInfo);
						}					
					}
				}
	
				fileManager.generateRouteFile(TransportationAdminProperties.getErpOrderInputFormat()
						, result.getOutputFile1(), ROW_IDENTIFIER, ROW_BEAN_IDENTIFIER, mergeDataList
						, null);
				fileManager.generateRouteFile(TransportationAdminProperties.getErpRouteInputFormat()
						, result.getOutputFile2(), ROW_IDENTIFIER, ROW_BEAN_IDENTIFIER, filterRoutesFromOrders(mergeDataList)
						, null);
			} else {			
				postError(result, "Areas "+ missingAreas.toString()+ " are missing in roadnet file");			
			}
		}  catch (RouteNoGenException routeNoGen) {
			result.addError(routeNoGen.getMessage());
		}
	}
		
	private List checkMissingAreas(Map markedAreas, Map availableAreas) {
		
		List missingAreas = new ArrayList();
		String strArea = null;
		
		if(markedAreas != null && availableAreas != null && markedAreas.keySet() != null) {
			Iterator iterator = markedAreas.keySet().iterator();
			while(iterator.hasNext()) {
				strArea = (String)iterator.next();
				if(!availableAreas.containsKey(strArea)) {
					missingAreas.add(strArea);
				}
			}
		}
		return missingAreas;
	}
	
	private List collectOrdersFromMap(Map ordersByArea) {
		
		List orders = new ArrayList();
		List tmpOrders = null;
		
		if(ordersByArea != null) {
			Iterator iterator = ordersByArea.values().iterator();
			while(iterator.hasNext()) {
				tmpOrders = (List)iterator.next();
				orders.addAll(tmpOrders);
			}
		}
		return orders;
	}
	
	private Map getAreas(DomainManagerI domainManagerService) {
		
		Map result = new HashMap();
		Collection dataList = domainManagerService.getAreas();
		TrnArea tmpArea = null;
		if(dataList != null) {
			Iterator iterator = dataList.iterator();
			while(iterator.hasNext()) {
				tmpArea = (TrnArea)iterator.next();				
				if(tmpArea != null && "X".equalsIgnoreCase(tmpArea.getActive())) {
					result.put(tmpArea.getCode(), tmpArea);
				}
			}
		}
		return result;
	}
		
	private OrderAreaGroup groupOrderRouteInfo(List fullDataList, Map areaMap) {
				
		OrderAreaGroup orderGroupResult = new OrderAreaGroup();
		Map orderGroup = new HashMap();
		Set orderIds = new HashSet();
		orderGroupResult.setOrderGroup(orderGroup);
		orderGroupResult.setOrderIds(orderIds);
		
		OrderRouteInfoModel tmpInfo = null;
		List tmpList = null;
		
		String areaCode = null;
		if(fullDataList != null) {
			Iterator iterator = fullDataList.iterator();
			while(iterator.hasNext()) {
				tmpInfo = (OrderRouteInfoModel)iterator.next();				
				areaCode = TransStringUtil.splitStringForCode(tmpInfo.getRouteId());
				if(areaMap.containsKey(areaCode)) {
					orderIds.add(tmpInfo.getOrderNumber());
					if(orderGroup.containsKey(areaCode)) {
						tmpList = (List)orderGroup.get(areaCode);
						tmpList.add(tmpInfo);				
					} else {
						tmpList = new ArrayList();
						tmpList.add(tmpInfo);
						orderGroup.put(areaCode, tmpList);
					}
				} 
			}
		}		
		return orderGroupResult;
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
	
	private void postError(RoutingResult result, String message) {
		result.setOutputFile1(null);
		result.setOutputFile2(null);
		List errorList = new ArrayList();
		errorList.add(message);
		result.setErrors(errorList);
	}
	
	private class OrderAreaGroup {
		
		private Map orderGroup = null;
		
		private Set orderIds = null;

		public Map getOrderGroup() {
			return orderGroup;
		}

		public void setOrderGroup(Map orderGroup) {
			this.orderGroup = orderGroup;
		}

		public Set getOrderIds() {
			return orderIds;
		}

		public void setOrderIds(Set orderIds) {
			this.orderIds = orderIds;
		}

	}
	
}
