package com.freshdirect.transadmin.datamanager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.routing.util.IRoutingParamConstants;
import com.freshdirect.transadmin.datamanager.model.OrderRouteInfoModel;
import com.freshdirect.transadmin.model.TrnArea;
import com.freshdirect.transadmin.model.TrnZone;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.util.TransportationAdminProperties;

public class RouteMergeDataManager extends RouteDataManager {

	public RoutingResult process(byte[] inputInfo1,byte[] inputInfo2, byte[] inputInfo3,
			String userName, Map paramMap, DomainManagerI domainManagerService) throws IOException  {

		long time = System.currentTimeMillis();
		
		List zoneList = getZoneList(domainManagerService);
		List zoneNumbers = getZoneNumbers(zoneList);
		
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

		updateRouteInfo(orderDataList, truckDataList);

		List fullDataList = fileManager.parseRouteFile(TransportationAdminProperties.getRoutingOrderRouteOutputFormat()
				, new ByteArrayInputStream(inputInfo3), ROW_IDENTIFIER, ROW_BEAN_IDENTIFIER
				, null);
		
		RouteGenerationResult routeGenResult = generateRouteNumber(fullDataList, cutOff, domainManagerService);		
		fullDataList = routeGenResult.getRouteInfos();		
		result.setRouteNoSaveInfos(routeGenResult.getRouteNoSaveInfos());

		Map areaOrderMap = groupOrderRouteInfo(fullDataList);
		Set areaCodes = areaOrderMap.keySet();	
		String missingZones = getMissingZones(zoneList, areaCodes);

		if(missingZones.length() == 0) {

			List mergeDataList =  new ArrayList();
			String strArea = null;

			if(areaCodes != null) {	
				Iterator iterator = areaCodes.iterator();
				while(iterator.hasNext()) {
					strArea = (String)iterator.next();
					List dataList = (List)areaOrderMap.get(strArea);
					mergeDataList.addAll(dataList);
				}
			}
			
			if(orderDataList != null) {
				Iterator iterator = orderDataList.iterator();
				OrderRouteInfoModel tmpInfo = null;
				while(iterator.hasNext()) {
					tmpInfo = (OrderRouteInfoModel)iterator.next();
					if(!zoneNumbers.contains(tmpInfo.getDeliveryZone())) {
						mergeDataList.add(tmpInfo);
					}					
				}
			}

			fileManager.generateRouteFile(TransportationAdminProperties.getErpOrderInputFormat()
					, result.getOutputFile1(), ROW_IDENTIFIER, ROW_BEAN_IDENTIFIER, mergeDataList
					, null);
			fileManager.generateRouteFile(TransportationAdminProperties.getErpRouteInputFormat()
					, result.getOutputFile2(), ROW_IDENTIFIER, ROW_BEAN_IDENTIFIER, mergeDataList
					, null);
		} else {
			result.setOutputFile1(null);
			result.setOutputFile2(null);
			List errorList = new ArrayList();
			errorList.add(missingZones+ " are missing");
			result.setErrors(errorList);
		}
		return result;
	}
	
	private List getZoneList(DomainManagerI domainManagerService) {
		
		List result = new ArrayList();
		Collection dataList = domainManagerService.getZones();
		TrnZone tmpZone = null;
		TrnArea tmpArea = null;
		if(dataList != null) {
			Iterator iterator = dataList.iterator();
			tmpZone = (TrnZone)iterator.next();
			tmpArea = tmpZone.getTrnArea();
			if(tmpArea != null && "X".equalsIgnoreCase(tmpArea.getActive())) {
				result.add(tmpZone);
			}
		}
		return result;
	}
	
	private List getZoneNumbers(Collection dataList) {
		
		List result = new ArrayList();		
		TrnZone tmpZone = null;
		
		if(dataList != null) {
			Iterator iterator = dataList.iterator();
			while(iterator.hasNext()) {
				tmpZone = (TrnZone)iterator.next();
				result.add(tmpZone.getZoneNumber());
			}			
		}
		return result;
	}
	
	private String getMissingZones(Collection rootList, Collection dataList) {
		
		StringBuffer result = new StringBuffer();
		String strArea = null;
		TrnZone tmpZone = null;
		if(rootList != null && dataList != null) {
			Iterator iterator = rootList.iterator();
			while(iterator.hasNext()) {
				tmpZone = (TrnZone)iterator.next();
				strArea = tmpZone.getArea();
				if(!dataList.contains(strArea)) {
					result.append(tmpZone.getZoneNumber());
					result.append(" ");
				}
			}
		}
		return result.toString();
	}
	
	private Map groupOrderRouteInfo(List fullDataList) {
		
		Map dataMap = new HashMap();		
		OrderRouteInfoModel tmpInfo = null;
		List tmpList = null;
		
		String areaCode = null;
		if(fullDataList != null) {
			Iterator iterator = fullDataList.iterator();
			while(iterator.hasNext()) {
				tmpInfo = (OrderRouteInfoModel)iterator.next();
				areaCode = tmpInfo.getDeliveryArea();
				if(dataMap.containsKey(areaCode)) {
					tmpList = (List)dataMap.get(areaCode);
					tmpList.add(tmpInfo);				
				} else {
					tmpList = new ArrayList();
					tmpList.add(tmpInfo);
					dataMap.put(areaCode, tmpList);
				}
			}
		}		
		return dataMap;
	}

	private void updateRouteInfo(List orderDataList, List truckDataList) {
		
		Map routeMap = new HashMap();		
		OrderRouteInfoModel tmpRouteInfo = null;
		OrderRouteInfoModel tmpOrderInfo = null;
		String routeId = null;
		if(truckDataList != null) {
			Iterator iterator = truckDataList.iterator();
			tmpRouteInfo = (OrderRouteInfoModel)iterator.next();
			routeId = tmpRouteInfo.getRouteId();
			if(!routeMap.containsKey(routeId)) {
				routeMap.put(routeId, tmpRouteInfo);
			}
		}	
		
		if(orderDataList != null) {
			Iterator iterator = orderDataList.iterator();
			tmpOrderInfo = (OrderRouteInfoModel)iterator.next();
			routeId = tmpRouteInfo.getRouteId();
			if(routeMap.containsKey(routeId)) {
				tmpRouteInfo = (OrderRouteInfoModel)routeMap.get(routeId);
				tmpOrderInfo.setPlant(tmpRouteInfo.getPlant());
				tmpOrderInfo.setDeliveryDate(tmpRouteInfo.getDeliveryDate());
				tmpOrderInfo.setDeliveryModel(tmpRouteInfo.getDeliveryModel());
				tmpOrderInfo.setRouteStartTime(tmpRouteInfo.getRouteStartTime());
			}
		}	
	}
}
