package com.freshdirect.routing.manager;

import com.freshdirect.routing.constants.EnumProcessInfoType;
import com.freshdirect.routing.constants.EnumProcessType;
import com.freshdirect.routing.model.IDeliveryModel;
import com.freshdirect.routing.model.ILocationModel;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.service.exception.RoutingProcessException;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.DeliveryServiceProxy;
import com.freshdirect.routing.util.RoutingServicesProperties;


public class EstimationManager extends BaseProcessManager {

	public void processRequest(ProcessContext request) throws RoutingProcessException  {

		IServiceTimeScenarioModel scenario = request.getRoutingScenario();
		DeliveryServiceProxy proxy = new DeliveryServiceProxy();
		IDeliveryModel model = request.getDeliveryInfo();
		ILocationModel locModel = request.getLocationInfo();
		IOrderModel orderModel = request.getOrderInfo();
				
		String serviceTimeType = model.getDeliveryLocation().getServiceTimeType();
		if(serviceTimeType == null || serviceTimeType.trim().length() == 0) {
			serviceTimeType = scenario.getDefaultServiceTimeType();
			model.getDeliveryLocation().setServiceTimeType(serviceTimeType);
			ProcessInfo  processInfo = new ProcessInfo();
			processInfo.setProcessType(EnumProcessType.LOAD_SERVICETIMETYPE);
			processInfo.setProcessInfoType(EnumProcessInfoType.WARNING);
			processInfo.setOrderId(orderModel.getOrderNumber());
			processInfo.setLocationId(locModel.getLocationId());
			request.addProcessInfo(processInfo);

		}
		double serviceTime = 0.0;
		try {
			serviceTime = proxy.getServiceTime(model, scenario.getServiceTimeFactorFormula(), scenario.getServiceTimeFormula());
		} catch(RoutingServiceException e) {
			ProcessInfo  processInfo = new ProcessInfo();
			processInfo.setProcessType(EnumProcessType.LOAD_SERVICETIME);
			processInfo.setProcessInfoType(EnumProcessInfoType.WARNING);
			processInfo.setOrderId(orderModel.getOrderNumber());
			processInfo.setLocationId(locModel.getLocationId());
			request.addProcessInfo(processInfo);
			try {
				serviceTime = proxy.getServiceTime(model,scenario.getServiceTimeFactorFormula()
													, scenario.getServiceTimeFormula(), RoutingServicesProperties.getDefaultFixedServiceTime()
													, RoutingServicesProperties.getDefaultVariableServiceTime());
			} catch(RoutingServiceException ex) {
				ex.printStackTrace();
			}
		}
		
		model.setServiceTime(serviceTime);

	}
}
