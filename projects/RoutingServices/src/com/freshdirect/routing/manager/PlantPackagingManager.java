package com.freshdirect.routing.manager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.freshdirect.routing.constants.EnumProcessInfoType;
import com.freshdirect.routing.constants.EnumProcessType;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IPackagingModel;
import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.service.exception.RoutingProcessException;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.PlantServiceProxy;

public class PlantPackagingManager extends BaseProcessManager {

	public void processRequest(ProcessContext request) throws RoutingProcessException   {		
		
		PlantServiceProxy proxy = new PlantServiceProxy();
		try {
			loadBatch(request, proxy);
		} catch (RoutingServiceException e) {			
			e.printStackTrace();
		}	
	}
	
	private void loadBatch(ProcessContext request, PlantServiceProxy proxy) throws RoutingServiceException {
		
		IServiceTimeScenarioModel scenario = request.getRoutingScenario();
		
		IOrderModel orderModel = request.getOrderInfo();
		List orderList = (List)request.getOrderIdLst();
		Map dataMap = new HashMap();
		if(orderList != null && request.getOrderPackageCache() == null) {			
			Iterator tmpIterator = orderList.iterator();
			List tmpLst = null;
			while(tmpIterator.hasNext()) {
				Object tmp = tmpIterator.next();				
				tmpLst = (List)tmp;
				try {
					Map tmpRowMap = proxy.getPackagingInfoList(tmpLst);					
					dataMap.putAll(tmpRowMap);		
					
				} catch (RoutingServiceException e) {			
					e.printStackTrace();
				}
			}
			request.setOrderPackageCache(dataMap);			
		}
		
		Map rowMap = (Map)((Map)request.getOrderPackageCache()).get(orderModel.getOrderNumber());
		IPackagingModel packageModel = proxy.getPackageModel(rowMap, scenario.getOrderSizeFormula(),
																(int)scenario.getDefaultCartonCount(),
																(int)scenario.getDefaultFreezerCount(),
																(int)scenario.getDefaultCaseCount());
		if(packageModel != null && packageModel.isDefault()) {
			ProcessInfo  processInfo = new ProcessInfo();
			processInfo.setProcessType(EnumProcessType.LOAD_CARTONCOUNT);
			processInfo.setProcessInfoType(EnumProcessInfoType.WARNING);
			processInfo.setOrderId(orderModel.getOrderNumber());
			request.addProcessInfo(processInfo);
			
		}
		orderModel.getDeliveryInfo().setPackagingInfo(proxy.getPackageModel(rowMap,scenario.getOrderSizeFormula(),
													(int)scenario.getDefaultCartonCount(),
													(int)scenario.getDefaultFreezerCount(),
													(int)scenario.getDefaultCaseCount()));
	}
	
	/*private void loadUnit(ProcessContext request, IPlantService service) throws RoutingServiceException {
				
		IOrderModel orderModel = request.getOrderInfo();
		
		try {			
			orderModel.getDeliveryInfo().setPackagingInfo(service.getPackagingInfo(orderModel));
		} catch (RoutingServiceException e) {			
			e.printStackTrace();
		}	
	}*/

}
