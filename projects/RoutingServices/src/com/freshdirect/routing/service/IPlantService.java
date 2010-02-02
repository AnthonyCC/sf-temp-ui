package com.freshdirect.routing.service;

import java.util.List;
import java.util.Map;

import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IPackagingModel;
import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.service.exception.RoutingServiceException;

public interface IPlantService {
			
	Map getPackagingInfoList(List orderIdLst) throws RoutingServiceException;
	
	IPackagingModel getPackageModel(Map rowMap, String orderSizeExpression, int defaultCartonCount
			, int defaultFreezerCount, int defaultCaseCount) throws RoutingServiceException;
	
	IPackagingModel estimateOrderSize(String orderNo, IServiceTimeScenarioModel scenario) throws RoutingServiceException;
	
	IPackagingModel estimateOrderSize(IOrderModel model, IServiceTimeScenarioModel scenario, IPackagingModel historyInfo) throws RoutingServiceException;
	
	Map getOrderSize(IOrderModel model) throws RoutingServiceException;
	
	IPackagingModel getHistoricOrderSize(IOrderModel model) throws RoutingServiceException;
		
}
