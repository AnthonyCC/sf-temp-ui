package com.freshdirect.routing.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IPackagingModel;
import com.freshdirect.routing.model.PackagingModel;
import com.freshdirect.routing.service.IPlantService;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.util.ServiceTimeUtil;
import com.freshdirect.sap.command.SapCartonInfo;
import com.freshdirect.sap.ejb.SapException;

public class PlantService implements IPlantService {
	
	public IPackagingModel getPackagingInfo(IOrderModel model, String orderSizeExpression, int defaultCartonCount
			, int defaultFreezerCount, int defaultCaseCount) throws RoutingServiceException {
		
		List orderIdLst = new ArrayList();
		orderIdLst.add(model.getOrderNumber());
		Map rowMap = null;
		try {
			
			Map resultMap = loadPackingInfo(orderIdLst);
			if(resultMap != null) {
				rowMap = (Map)resultMap.get(model.getOrderNumber());				
			}
		} catch(Exception exp) {
			exp.printStackTrace();
			// Do Nothing returned the configured default values if ERP is down
		}
		
		return getPackageModel(rowMap, orderSizeExpression, defaultCartonCount
									, defaultFreezerCount, defaultCaseCount);
	}
	
	private Map loadPackingInfo(List dataList) throws SapException {
				
		SapCartonInfo cartonInfos = new SapCartonInfo(dataList);
		cartonInfos.execute();
		return cartonInfos.getCartonInfos();
	}
	
	public IPackagingModel getPackageModel(Map rowMap, String orderSizeExpression, int defaultCartonCount
												, int defaultFreezerCount, int defaultCaseCount) {
		
		int cartonCount = 0; 
		int freezerCount = 0;
		int caseCount = 0;
		boolean isDefault = true;
		
		if(rowMap != null) {
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
		tmpPackageModel.setTotalSize1(ServiceTimeUtil.evaluateExpression(orderSizeExpression
										, ServiceTimeUtil.getServiceTimeFactorParams(tmpPackageModel)));
		//tmpPackageModel.setTotalSize2(ServiceTimeUtil.evaluateExpression(RoutingServicesProperties.getTotalSize2Expression()
										//, ServiceTimeUtil.getServiceTimeFactorParams(tmpPackageModel)));
		tmpPackageModel.setDefault(isDefault);
		return tmpPackageModel;
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
