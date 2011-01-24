package com.freshdirect.routing.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.routing.model.IDeliveryModel;
import com.freshdirect.routing.model.IHandOffBatch;
import com.freshdirect.routing.model.ILocationModel;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IServiceTimeScenarioModel;

public class ProcessContext implements Serializable {
	
	private Object dataModel;
	
	private Object erpOrderIdLst;
	
	private Object orderPackageCache;
	
	private Object deliveryTypeCache;
			
	private Object locationList;
	
	private Object buildingList;
	
	private Object processInfo;
	
	private Object processParam;
	
	private Object processScenario;
	
	private Object orderList;
	
	private Object lateDeliveryOrderList;

	private IHandOffBatch handOffBatch;
	
	private Object userId;
	
	private Object serviceTimeTypeCache;
	
	private int maxSessionSize;
	
	private boolean isHandOffProcess;
			
	public boolean isHandOffProcess() {
		return isHandOffProcess;
	}
	public void setHandOffProcess(boolean isHandOffProcess) {
		this.isHandOffProcess = isHandOffProcess;
	}
	public Object getUserId() {
		return userId;
	}
	public void setUserId(Object userId) {
		this.userId = userId;
	}

	public IHandOffBatch getHandOffBatch() {
		return handOffBatch;
	}
	public void setHandOffBatch(IHandOffBatch handOffBatch) {
		this.handOffBatch = handOffBatch;
	}
	
	public Object getLocationList() {
		return locationList;
	}

	public void setLocationList(Object locationList) {
		this.locationList = locationList;
	}
	
	public Object getBuildingList() {
		return buildingList;
	}

	public void setBuildingList(Object buildingList) {
		this.buildingList = buildingList;
	} 


	public Object getDataModel() {
		return dataModel;
	}

	public void setDataModel(Object dataModel) {
		this.dataModel = dataModel;
	}
		
	/* THis is for calculating bulk order size */
	public Object getErpOrderIdLst() {
		return erpOrderIdLst;
	}
	public void setErpOrderIdLst(Object erpOrderIdLst) {
		this.erpOrderIdLst = erpOrderIdLst;
	}
	
	public Object getDeliveryTypeCache() {
		return deliveryTypeCache;
	}

	public void setDeliveryTypeCache(Object deliveryTypeCache) {
		this.deliveryTypeCache = deliveryTypeCache;
	}

	public Object getOrderPackageCache() {
		return orderPackageCache;
	}

	public void setOrderPackageCache(Object orderPackageCache) {
		this.orderPackageCache = orderPackageCache;
	}
	
	public Object getProcessInfo() {
		return processInfo;
	}

	public void setProcessInfo(Object processInfo) {
		this.processInfo = processInfo;
	}
	
	public void addProcessInfo(ProcessInfo info) {
		if(processInfo == null) {
			processInfo = new ArrayList();
		}
		((List)processInfo).add(info);
	}

	public Object getProcessParam() {
		return processParam;
	}

	public void setProcessParam(Object processParam) {
		this.processParam = processParam;
	}
	
	public void addProcessParam(Object key, Object value) {
		if(processParam == null) {
			processParam = new HashMap();
		}
		((HashMap)processParam).put(key, value);
	}
	
	public void addProcessParam(Map paramMap) {
		if(processParam == null) {
			processParam = new HashMap();
		}
		((HashMap)processParam).putAll(paramMap);
	}

	public Object getProcessScenario() {
		return processScenario;
	}

	public void setProcessScenario(Object processScenario) {
		this.processScenario = processScenario;
	}
	
	public IOrderModel getOrderInfo() {
		return (IOrderModel)dataModel;
	}
	
	public IDeliveryModel getDeliveryInfo() {
		return (IDeliveryModel)((IOrderModel)dataModel).getDeliveryInfo();
	}
	
	public ILocationModel getLocationInfo() {
		return (ILocationModel)((IDeliveryModel)
					((IOrderModel)dataModel).getDeliveryInfo()).getDeliveryLocation();
	}
	
	public IServiceTimeScenarioModel getRoutingScenario() {
		return (IServiceTimeScenarioModel)processScenario;
	}

	public Object getOrderList() {
		return orderList;
	}

	public void setOrderList(Object orderList) {
		this.orderList = orderList;
	}
	
	public void addOrder(Object dataObject) {
		if(orderList == null) {
			orderList = new ArrayList();
		}
		((List)orderList).add(dataObject);
	}

	public Object getLateDeliveryOrderList() {
		return lateDeliveryOrderList;
	}

	public void setLateDeliveryOrderList(Object lateDeliveryOrderList) {
		this.lateDeliveryOrderList = lateDeliveryOrderList;
	}
	
	public Object getServiceTimeTypeCache() {
		return serviceTimeTypeCache;
	}
	
	public void setServiceTimeTypeCache(Object serviceTimeTypeCache) {
		this.serviceTimeTypeCache = serviceTimeTypeCache;
	}
	
	public int getMaxSessionSize() {
		return maxSessionSize;
	}
	public void setMaxSessionSize(int maxSessionSize) {
		this.maxSessionSize = maxSessionSize;
	}
	
}
