package com.freshdirect.routing.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.routing.constants.EnumOrderMetricsSource;
import com.freshdirect.routing.dao.IDeliveryDetailsDAO;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IPackagingModel;
import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.model.OrderEstimationResult;
import com.freshdirect.routing.model.PackagingModel;
import com.freshdirect.routing.service.IPlantService;
import com.freshdirect.routing.service.exception.IIssue;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.util.RoutingServicesProperties;
import com.freshdirect.routing.util.ServiceTimeUtil;
import com.freshdirect.sap.command.SapCartonInfo;
import com.freshdirect.sap.ejb.SapException;

public class PlantService extends BaseService implements IPlantService {
	
	private IDeliveryDetailsDAO deliveryDAOImpl;
	
	public IDeliveryDetailsDAO getDeliveryDAOImpl() {
		return deliveryDAOImpl;
	}

	public void setDeliveryDAOImpl(IDeliveryDetailsDAO deliveryDAOImpl) {
		this.deliveryDAOImpl = deliveryDAOImpl;
	}

	public OrderEstimationResult estimateOrderSize(IOrderModel model, IServiceTimeScenarioModel scenario
														, IPackagingModel _historyInfo) throws RoutingServiceException {

		int cartonCount = (int)scenario.getDefaultCartonCount(); 
		int freezerCount = (int)scenario.getDefaultFreezerCount();
		int caseCount = (int)scenario.getDefaultCaseCount();
		
		boolean isHistoryInfo = false;
		if(_historyInfo != null && !(_historyInfo.getNoOfCartons() == 0
				&& _historyInfo.getNoOfCases() == 0
				&& _historyInfo.getNoOfFreezers() == 0)) {
			cartonCount = (int)_historyInfo.getNoOfCartons(); 
			freezerCount = (int)_historyInfo.getNoOfFreezers();
			caseCount = (int)_historyInfo.getNoOfCases();
			isHistoryInfo = true;
		}
		OrderEstimationResult result = getPackageModel(new HashMap(), scenario.getOrderSizeFormula(),
															cartonCount, freezerCount, caseCount);
		
		if(model!=null && model.getDeliveryInfo()!=null && model.getDeliveryInfo().getDeliveryZone()!=null
					&& model.getDeliveryInfo().getDeliveryZone().getArea()!=null && model.getDeliveryInfo().getDeliveryZone().getArea().isDepot())
		result.setCalculatedOrderSize(Math.min(RoutingServicesProperties.getMaxOrderSize(), result.getCalculatedOrderSize()));
		
		if(isHistoryInfo) {
			result.getPackagingModel().setSource(EnumOrderMetricsSource.HISTORY);
		}
		return result;
	}

	public OrderEstimationResult estimateOrderSize(String orderNo, IServiceTimeScenarioModel scenario) throws RoutingServiceException {
		
		IPackagingModel _historyInfo = null;
		int cartonCount = (int)scenario.getDefaultCartonCount(); 
		int freezerCount = (int)scenario.getDefaultFreezerCount();
		int caseCount = (int)scenario.getDefaultCaseCount();
		
		if(_historyInfo != null && !(_historyInfo.getNoOfCartons() == 0
				&& _historyInfo.getNoOfCases() == 0
				&& _historyInfo.getNoOfFreezers() == 0)) {
			cartonCount = (int)_historyInfo.getNoOfCartons(); 
			freezerCount = (int)_historyInfo.getNoOfFreezers();
			caseCount = (int)_historyInfo.getNoOfCases();
		}
		return getPackagingInfo(orderNo, scenario.getOrderSizeFormula(),
										cartonCount, freezerCount, caseCount);
	}
	
		
	public IPackagingModel getHistoricOrderSize(IOrderModel model) throws RoutingServiceException {
		
		IPackagingModel _historyInfo = null;		
		try {			
			
			_historyInfo = deliveryDAOImpl.getHistoricOrderSize(model.getCustomerNumber()
																, RoutingServicesProperties.getDefaultOrderEstimationRange());
			_historyInfo.setSource(EnumOrderMetricsSource.HISTORY);
		} catch (SQLException e) {
			e.printStackTrace();			
		}	
		return _historyInfo;
	}
	
	public Map getOrderSize(IOrderModel model) throws RoutingServiceException {
		
		List orderIdLst = new ArrayList();
		orderIdLst.add(model.getOrderNumber());
		
		Map resultMap = new HashMap();
		
		try {
			
			Map result = loadPackingInfo(orderIdLst);
			if(result != null) {
				resultMap = (Map)resultMap.get(model.getOrderNumber());				
			}
		} catch(SapException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_DELIVERYINFO_NOTFOUND);
		}
		
		return resultMap;
	}

	private OrderEstimationResult getPackagingInfo(String orderNo, String orderSizeExpression, int defaultCartonCount
			, int defaultFreezerCount, int defaultCaseCount) throws RoutingServiceException {
		
		List orderIdLst = new ArrayList();
		orderIdLst.add(orderNo);
		Map rowMap = null;
		
		try {
			
			Map resultMap = loadPackingInfo(orderIdLst);
			if(resultMap != null) {
				rowMap = (Map)resultMap.get(orderNo);				
			}
		} catch(Exception exp) {
			exp.printStackTrace();
			// Do Nothing returned the configured default values if ERP is down
		}
		
		return getPackageModel(rowMap, orderSizeExpression, defaultCartonCount
									, defaultFreezerCount, defaultCaseCount);
	}
	
	private Map loadPackingInfo(List dataList) throws SapException {
		//System.out.println("loadPackingInfo ORDERNOS >>"+ (dataList != null ? dataList.size() : 0));		
		SapCartonInfo cartonInfos = new SapCartonInfo(dataList);
		cartonInfos.execute();
		return cartonInfos.getCartonInfos();
	}
	
	public OrderEstimationResult getPackageModel(Map rowMap, String orderSizeExpression, int defaultCartonCount
												, int defaultFreezerCount, int defaultCaseCount) {
		
		int cartonCount = 0; 
		int freezerCount = 0;
		int caseCount = 0;
		boolean isDefault = true;
		
		if(rowMap != null && rowMap.keySet() != null && rowMap.keySet().size() > 0) {
			cartonCount = (getIntegerValue((Integer)rowMap.get("DRYGOODS")))+getIntegerValue(((Integer)rowMap.get("MEZZ1")))
								+getIntegerValue(((Integer)rowMap.get("MEZZ2"))); 
			freezerCount = getIntegerValue(((Integer)rowMap.get("FREEZER")));
			caseCount = getIntegerValue(((Integer)rowMap.get("CASESALE")))+getIntegerValue(((Integer)rowMap.get("PLATTER")));
			
			isDefault = (cartonCount == 0 && freezerCount == 0 && caseCount == 0);								
		}
		
		if(isDefault) {
			cartonCount = defaultCartonCount; 
			freezerCount = defaultFreezerCount;
			caseCount = defaultCaseCount;
		}
		IPackagingModel tmpPackageModel = new PackagingModel();
		tmpPackageModel.setNoOfCartons(cartonCount);
		tmpPackageModel.setNoOfCases(caseCount);
		tmpPackageModel.setNoOfFreezers(freezerCount);
		
		tmpPackageModel.setSource(isDefault ? EnumOrderMetricsSource.DEFAULT : EnumOrderMetricsSource.ACTUAL);
		
		OrderEstimationResult result = new OrderEstimationResult();
		result.setPackagingModel(tmpPackageModel);
		result.setCalculatedOrderSize(ServiceTimeUtil.evaluateExpression(orderSizeExpression
				, ServiceTimeUtil.getServiceTimeFactorParams(tmpPackageModel)));
		return result;
	}
	
	private int getIntegerValue(Integer val) {
		int retVal = 0;
		if(val != null) {
			retVal = val.intValue();
		}			
		return retVal;
	}
	
	public Map getPackagingInfoList(List orderIdLst) throws RoutingServiceException {
		
		try {			
			Map dataMap = loadPackingInfo(orderIdLst);			
			return dataMap;			
		} catch(Exception exp) {
			exp.printStackTrace();
			// Do Nothing returned the configured default values if ERP is down
		}
		return new HashMap();
	}	
}
