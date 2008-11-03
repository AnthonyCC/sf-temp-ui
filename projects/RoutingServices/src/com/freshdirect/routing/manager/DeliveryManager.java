package com.freshdirect.routing.manager;

import java.util.Map;

import com.freshdirect.routing.constants.EnumProcessInfoType;
import com.freshdirect.routing.constants.EnumProcessType;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.model.IZoneModel;
import com.freshdirect.routing.service.exception.IIssue;
import com.freshdirect.routing.service.exception.RoutingProcessException;
import com.freshdirect.routing.util.RoutingDateUtil;
import com.freshdirect.routing.util.RoutingServicesProperties;

public class DeliveryManager extends BaseProcessManager {

	public void processRequest(ProcessContext request) throws RoutingProcessException  {
						
		IOrderModel orderModel = request.getOrderInfo();
		
		String zoneCode = orderModel.getDeliveryInfo().getDeliveryZone().getZoneNumber();
		
		IZoneModel zoneModel = (IZoneModel)((Map)request.getDeliveryTypeCache()).get(zoneCode);
				
		if(zoneModel == null) {
			throw new RoutingProcessException(zoneCode, null, IIssue.PROCESS_ZONEINFO_NOTFOUND);
		}
		String zoneType = zoneModel.getZoneType();
		
		IServiceTimeScenarioModel scenario = request.getRoutingScenario();
		
		if(zoneType == null || zoneType.trim().length() == 0) {
			
			zoneType = scenario.getDefaultZoneType();
			ProcessInfo  processInfo = new ProcessInfo();
			processInfo.setProcessType(EnumProcessType.LOAD_ZONETYPE);
			processInfo.setProcessInfoType(EnumProcessInfoType.WARNING);
			processInfo.setOrderId(orderModel.getOrderNumber());
			processInfo.setZoneCode(zoneCode);
			request.addProcessInfo(processInfo);
			zoneModel.setZoneType(zoneType);
		} 			
		orderModel.getDeliveryInfo().setDeliveryZone(zoneModel);	
		
		if(orderModel.getDeliveryInfo().getDeliveryEndTime() != null
				&& RoutingServicesProperties.isIncrementWindowEndTime()) {
			//Fix for time increase window end time by a second
			orderModel.getDeliveryInfo().setDeliveryEndTime(RoutingDateUtil
										.addSeconds(orderModel.getDeliveryInfo().getDeliveryEndTime(), 1));
		}

	}

}
