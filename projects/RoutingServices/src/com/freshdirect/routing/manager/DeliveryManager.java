package com.freshdirect.routing.manager;

import java.util.List;
import java.util.Map;

import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.model.IServiceTimeTypeModel;
import com.freshdirect.routing.model.IZoneModel;
import com.freshdirect.routing.service.exception.IIssue;
import com.freshdirect.routing.service.exception.RoutingProcessException;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.RoutingInfoServiceProxy;
import com.freshdirect.routing.util.RoutingDateUtil;
import com.freshdirect.routing.util.RoutingServicesProperties;

public class DeliveryManager extends BaseProcessManager {

	public void processRequest(ProcessContext request) throws RoutingProcessException  {
						
		RoutingInfoServiceProxy proxy = new RoutingInfoServiceProxy();
		
		IOrderModel orderModel = request.getOrderInfo();
		
		String zoneCode = orderModel.getDeliveryInfo().getDeliveryZone().getZoneNumber();
				
		IZoneModel zoneModel = (IZoneModel)((Map)request.getDeliveryTypeCache()).get(zoneCode);
				
		if(zoneModel == null) {
			throw new RoutingProcessException(zoneCode, null, IIssue.PROCESS_ZONEINFO_NOTFOUND);
		}
				
		IServiceTimeScenarioModel scenario = request.getRoutingScenario();
		try {
			scenario.setZoneConfiguration(proxy.getRoutingScenarioMapping(scenario.getCode()));
			if(zoneModel.getServiceTimeType() != null) {
				zoneModel.setServiceTimeType((IServiceTimeTypeModel)(((Map)request.getServiceTimeTypeCache())
													.get(zoneModel.getServiceTimeType().getCode())));
			} else {
				zoneModel.setServiceTimeType(null);
			}
			if(orderModel.getDeliveryInfo().getDeliveryLocation().getServiceTimeType() != null) {
				orderModel.getDeliveryInfo().getDeliveryLocation().setServiceTimeType(
									(IServiceTimeTypeModel)(((Map)request.getServiceTimeTypeCache())
												.get(orderModel.getDeliveryInfo().getDeliveryLocation().getServiceTimeType().getCode())));
			} else {
				orderModel.getDeliveryInfo().getDeliveryLocation().setServiceTimeType(null);
			}
			if(orderModel.getDeliveryInfo().getDeliveryLocation().getBuilding() != null
					&& orderModel.getDeliveryInfo().getDeliveryLocation().getBuilding().getServiceTimeType() != null) {
				orderModel.getDeliveryInfo().getDeliveryLocation().getBuilding().setServiceTimeType(
						(IServiceTimeTypeModel)(((Map)request.getServiceTimeTypeCache())
									.get(orderModel.getDeliveryInfo().getDeliveryLocation().getBuilding().getServiceTimeType().getCode())));
			} else {
				orderModel.getDeliveryInfo().getDeliveryLocation().getBuilding().setServiceTimeType(null);
			}
			
		} catch (RoutingServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		/*if(zoneType == null || zoneType.trim().length() == 0) {
			
			zoneType = scenario.getDefaultZoneType();
			ProcessInfo  processInfo = new ProcessInfo();
			processInfo.setProcessType(EnumProcessType.LOAD_ZONETYPE);
			processInfo.setProcessInfoType(EnumProcessInfoType.WARNING);
			processInfo.setOrderId(orderModel.getOrderNumber());
			processInfo.setZoneCode(zoneCode);
			request.addProcessInfo(processInfo);
			zoneModel.setZoneType(zoneType);
		} */			
		orderModel.getDeliveryInfo().setDeliveryZone(zoneModel);	
		
		if(orderModel.getDeliveryInfo().getDeliveryEndTime() != null
				&& RoutingServicesProperties.isIncrementWindowEndTime()) {
			//Fix for time increase window end time by a second
			orderModel.getDeliveryInfo().setDeliveryEndTime(RoutingDateUtil
										.addSeconds(orderModel.getDeliveryInfo().getDeliveryEndTime(), 1));
		}
		
		if( request.getLateDeliveryOrderList() != null 
				&& ((List)request.getLateDeliveryOrderList()).contains(orderModel.getCustomerNumber()) 
					&& orderModel.getDeliveryInfo().getDeliveryStartTime() != null
				&& orderModel.getDeliveryInfo().getDeliveryEndTime() != null) {
			//Late Delivery Delivery Window End Time Reduction
			orderModel.getDeliveryInfo().setDeliveryEndTime(RoutingDateUtil
										.reduceTimeByPercent(orderModel.getDeliveryInfo().getDeliveryStartTime(), 
												orderModel.getDeliveryInfo().getDeliveryEndTime(), scenario.getLateDeliveryFactor()));
		}

	}

}
