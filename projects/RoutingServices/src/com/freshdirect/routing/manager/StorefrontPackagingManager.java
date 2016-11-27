package com.freshdirect.routing.manager;

import com.freshdirect.routing.constants.EnumOrderMetricsSource;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.service.exception.RoutingProcessException;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.util.ServiceTimeUtil;

public class StorefrontPackagingManager extends BaseProcessManager {

	public void processRequest(ProcessContext request) throws RoutingProcessException   {		
		
		IServiceTimeScenarioModel scenario = request.getRoutingScenario();
		IOrderModel orderModel = request.getOrderInfo();
		
		try {
			if(orderModel.getDeliveryInfo() != null && orderModel.getDeliveryInfo().getPackagingDetail() != null) {
				if(orderModel.getDeliveryInfo().getPackagingDetail().getNoOfCartons() == 0 
						&& orderModel.getDeliveryInfo().getPackagingDetail().getNoOfCases() == 0 
							&& orderModel.getDeliveryInfo().getPackagingDetail().getNoOfFreezers() == 0) {
					orderModel.getDeliveryInfo().getPackagingDetail().setNoOfCartons((int)scenario.getDefaultCartonCount());
					orderModel.getDeliveryInfo().getPackagingDetail().setNoOfCases((int)scenario.getDefaultCaseCount());
					orderModel.getDeliveryInfo().getPackagingDetail().setNoOfFreezers((int)scenario.getDefaultFreezerCount());
					orderModel.getDeliveryInfo().getPackagingDetail().setSource(EnumOrderMetricsSource.DEFAULT);
				
				} else {
					orderModel.getDeliveryInfo().getPackagingDetail().setSource(EnumOrderMetricsSource.ACTUAL);
				}
			}
			orderModel.getDeliveryInfo().setCalculatedOrderSize(ServiceTimeUtil.evaluateExpression(scenario.getOrderSizeFormula()
											, ServiceTimeUtil.getServiceTimeFactorParams(orderModel.getDeliveryInfo().getPackagingDetail())));
		} catch (RoutingServiceException e) {			
			e.printStackTrace();
		}	
	}	
}
