package com.freshdirect.routing.manager;

import java.util.Map;

import com.freshdirect.routing.constants.EnumProcessInfoType;
import com.freshdirect.routing.constants.EnumProcessType;
import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.model.IServiceTimeTypeModel;
import com.freshdirect.routing.service.exception.RoutingProcessException;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.DeliveryServiceProxy;


public class EstimationManager extends BaseProcessManager {

	public void processRequest(ProcessContext request) throws RoutingProcessException  {

		IServiceTimeScenarioModel scenario = request.getRoutingScenario();
		DeliveryServiceProxy proxy = new DeliveryServiceProxy();
								
		double serviceTime = 0.0;
		try {
			Map<String, IServiceTimeTypeModel> serviceTimeTypeMapping = (Map<String, IServiceTimeTypeModel>)request.getServiceTimeTypeCache();
			
			if(serviceTimeTypeMapping != null && request.getOrderInfo().getDeliveryInfo() != null) {
				if(request.getOrderInfo().getDeliveryInfo().getDeliveryZone() != null
						&& request.getOrderInfo().getDeliveryInfo().getDeliveryZone().getServiceTimeType().getCode() != null) {
					request.getOrderInfo().getDeliveryInfo()
							.getDeliveryZone().setServiceTimeType(serviceTimeTypeMapping.get(request.getOrderInfo().getDeliveryInfo()
																			.getDeliveryZone().getServiceTimeType().getCode()));
				} else {
					request.getOrderInfo().getDeliveryInfo().getDeliveryZone().setServiceTimeType(null);
				}
				if(request.getOrderInfo().getDeliveryInfo().getDeliveryLocation().getServiceTimeType() != null) {
					request.getOrderInfo().getDeliveryInfo().getDeliveryLocation().setServiceTimeType(serviceTimeTypeMapping
																.get(request.getOrderInfo().getDeliveryInfo().getDeliveryLocation()
																				.getServiceTimeType().getCode()));
				} else {
					request.getOrderInfo().getDeliveryInfo().getDeliveryLocation().setServiceTimeType(null);
				}
				if(request.getOrderInfo().getDeliveryInfo().getDeliveryLocation().getBuilding() != null 
						&& request.getOrderInfo().getDeliveryInfo().getDeliveryLocation().getBuilding().getServiceTimeType() != null) {
					request.getOrderInfo().getDeliveryInfo().getDeliveryLocation().getBuilding().setServiceTimeType(serviceTimeTypeMapping
							.get(request.getOrderInfo().getDeliveryInfo().getDeliveryLocation().getBuilding().getServiceTimeType().getCode()));
				} else {
					request.getOrderInfo().getDeliveryInfo().getDeliveryLocation().getBuilding().setServiceTimeType(null);
				}
			}
			serviceTime = proxy.getServiceTime(request.getOrderInfo(), scenario);
		} catch(Exception e) {
			ProcessInfo  processInfo = new ProcessInfo();
			processInfo.setProcessType(EnumProcessType.LOAD_SERVICETIME);
			processInfo.setProcessInfoType(EnumProcessInfoType.WARNING);
			processInfo.setOrderId(request.getOrderInfo().getOrderNumber());
			processInfo.setLocationId(request.getLocationInfo().getLocationId());
			request.addProcessInfo(processInfo);			
		}
		
		request.getDeliveryInfo().setServiceTime(serviceTime);

	}
}
