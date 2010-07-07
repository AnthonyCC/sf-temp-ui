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
								
		double serviceTime = 0.0;
		try {
			serviceTime = proxy.getServiceTime(request.getOrderInfo(), scenario);
		} catch(RoutingServiceException e) {
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
