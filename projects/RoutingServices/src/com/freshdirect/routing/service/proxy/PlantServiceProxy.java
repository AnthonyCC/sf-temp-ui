package com.freshdirect.routing.service.proxy;

import java.util.List;
import java.util.Map;

import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IPackagingModel;
import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.service.IPlantService;
import com.freshdirect.routing.service.RoutingServiceLocator;
import com.freshdirect.routing.service.exception.RoutingServiceException;

public class PlantServiceProxy extends BaseServiceProxy  {
	
	public IPackagingModel getPackagingInfo(IOrderModel model, String orderSizeExpression, int defaultCartonCount
			, int defaultFreezerCount, int defaultCaseCount) throws RoutingServiceException {
		
		return getService().getPackagingInfo(model, orderSizeExpression, defaultCartonCount
				, defaultFreezerCount, defaultCaseCount);
	}
		
	
	public IPackagingModel getPackageModel(Map rowMap, String orderSizeExpression, int defaultCartonCount
			, int defaultFreezerCount, int defaultCaseCount) throws RoutingServiceException {
		
		return getService().getPackageModel(rowMap, orderSizeExpression, defaultCartonCount
				, defaultFreezerCount, defaultCaseCount);
	}
		
	
	public Map getPackagingInfoList(List orderIdLst) throws RoutingServiceException {
		
		return getService().getPackagingInfoList(orderIdLst);
	}
	
	public IPackagingModel estimateOrderSize(IOrderModel model, IServiceTimeScenarioModel scenario) throws RoutingServiceException {
		return getService().estimateOrderSize(model, scenario);
	}
	
	public Map getOrderSize(IOrderModel model) throws RoutingServiceException {
		return getService().getOrderSize(model);
	}
	
	public IPlantService getService() {
		return RoutingServiceLocator.getInstance().getPlantService();
	}
	
	
}
