package com.freshdirect.routing.service.proxy;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.routing.constants.EnumHandOffBatchActionType;
import com.freshdirect.routing.constants.EnumHandOffBatchStatus;
import com.freshdirect.routing.constants.EnumHandOffDispatchStatus;
import com.freshdirect.routing.model.IHandOffBatch;
import com.freshdirect.routing.model.IHandOffBatchDepotSchedule;
import com.freshdirect.routing.model.IHandOffBatchDepotScheduleEx;
import com.freshdirect.routing.model.IHandOffBatchDispatchResource;
import com.freshdirect.routing.model.IHandOffBatchPlan;
import com.freshdirect.routing.model.IHandOffBatchRoute;
import com.freshdirect.routing.model.IHandOffBatchStop;
import com.freshdirect.routing.model.IHandOffDispatch;
import com.freshdirect.routing.model.TriggerHandOffResult;
import com.freshdirect.routing.model.TruckPreferenceStat;
import com.freshdirect.routing.service.IHandOffService;
import com.freshdirect.routing.service.RoutingServiceLocator;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.truckassignment.Truck;
import com.freshdirect.routing.util.RoutingTimeOfDay;
import com.freshdirect.sap.bapi.BapiSendHandOff.HandOffDispatchIn;

public class HandOffServiceProxy  extends BaseServiceProxy  {	
		
	public IHandOffService getService() {
		return RoutingServiceLocator.getInstance().getHandOffService();
	}
	
	public TriggerHandOffResult createNewHandOffBatch(Date deliveryDate, String userId, String scenario, Date cutOffDateTime, boolean isStandByMode) throws RoutingServiceException {
		return getService().createNewHandOffBatch(deliveryDate, userId, scenario, cutOffDateTime, isStandByMode);
	}
	
	public List<IHandOffBatchStop> getOrderByCutoff(final Date deliveryDate, final Date cutOff) throws RoutingServiceException {
		return getService().getOrderByCutoff(deliveryDate, cutOff);
	}
	
	public void updateHandOffBatchMessage(String handOffBatchId, String message) throws RoutingServiceException {
		getService().updateHandOffBatchMessage(handOffBatchId, message);
	}
	
	public void updateHandOffBatchStatus(String handOffBatchId, EnumHandOffBatchStatus status) throws RoutingServiceException {
		getService().updateHandOffBatchStatus(handOffBatchId, status);
	}
	
	public void addNewHandOffBatchAction(String handOffBatchId, Date actionDateTime
											, EnumHandOffBatchActionType actionType
												, String userId) throws RoutingServiceException {
		getService().addNewHandOffBatchAction(handOffBatchId, actionDateTime, actionType, userId);
	}
	
	public void addNewHandOffBatchSession(String handOffBatchId, String sessionName, String region) throws RoutingServiceException {
		getService().addNewHandOffBatchSession(handOffBatchId, sessionName, region);
	}
	
	public void addNewHandOffBatchStops(List<IHandOffBatchStop> dataList) throws RoutingServiceException {
		getService().addNewHandOffBatchStops(dataList);
	}
	
	public Set<IHandOffBatch> getHandOffBatch(Date deliveryDate) throws RoutingServiceException {
		return getService().getHandOffBatch(deliveryDate);
	}
	
	public IHandOffBatch getHandOffBatchById(String batchId) throws RoutingServiceException {
		return getService().getHandOffBatchById(batchId);
	}
	
	public Map<String, Integer> getHandOffBatchRouteCnt(Date deliveryDate) throws RoutingServiceException {
		return getService().getHandOffBatchRouteCnt(deliveryDate);
	}
	
	public void clearHandOffBatch(String handOffBatchId) throws RoutingServiceException {
		getService().clearHandOffBatch(handOffBatchId);
	}
	
	public void clearHandOffBatchStopRoute(Date deliveryDate, String handOffBatchId) throws RoutingServiceException {
		getService().clearHandOffBatchStopRoute(deliveryDate, handOffBatchId);
	}
	
	public void updateHandOffBatchDetails(String handOffBatchId, List<IHandOffBatchRoute> routes
											, List<IHandOffBatchStop> stops, Map<RoutingTimeOfDay, EnumHandOffDispatchStatus> dispatchStatus) throws RoutingServiceException {
		getService().updateHandOffBatchDetails(handOffBatchId, routes, stops, dispatchStatus);
	}
	
	public List<IHandOffBatchStop> getHandOffBatchStops(String batchId, boolean filterException) throws RoutingServiceException {
		return getService().getHandOffBatchStops(batchId, filterException);
	}
	
	public List<IHandOffBatchRoute> getHandOffBatchRoutes(String batchId) throws RoutingServiceException {
		return getService().getHandOffBatchRoutes(batchId);
	}
	
	public void addNewHandOffBatchDepotSchedules(String handOffBatchId, Set<IHandOffBatchDepotSchedule> dataList) throws RoutingServiceException {
		getService().addNewHandOffBatchDepotSchedules(handOffBatchId, dataList);
	}
	
	public Map<EnumSaleStatus, Integer> getOrderStatsByCutoff(Date deliveryDate, Date cutOff) throws RoutingServiceException {
		return getService().getOrderStatsByCutoff(deliveryDate, cutOff);
	}
	
	public IHandOffBatchRoute getHandOffBatchStopsByRoute(Date deliveryDate, String routeId, boolean filterException) throws RoutingServiceException {
		return getService().getHandOffBatchStopsByRoute(deliveryDate, routeId, filterException);
	}
	
	public void updateHandOffBatchCommitEligibility(String handOffBatchId, boolean isEligibleForCommit) throws RoutingServiceException {
		getService().updateHandOffBatchCommitEligibility(handOffBatchId, isEligibleForCommit);
	}
	
	public void updateHandOffStopException(String handOffBatchId, List<String> exceptionOrderIds) throws RoutingServiceException {
		getService().updateHandOffStopException(handOffBatchId, exceptionOrderIds);
	}
	
	public void updateHandOffBatchStopErpNo(List<IHandOffBatchStop> dataList) throws RoutingServiceException {
		getService().updateHandOffBatchStopErpNo(dataList);
	}
	
	public void addNewHandOffBatchDepotSchedulesEx(String dayOfWeek, Date cutOffTime, Set<IHandOffBatchDepotScheduleEx> dataList) throws RoutingServiceException {
		getService().addNewHandOffBatchDepotSchedulesEx(dayOfWeek, cutOffTime, dataList);
	}
	
	public Set<IHandOffBatchDepotScheduleEx> getHandOffBatchDepotSchedulesEx(String dayOfWeek, Date cutOffTime) throws RoutingServiceException {
		return getService().getHandOffBatchDepotSchedulesEx(dayOfWeek, cutOffTime);
	}
	
	public List<HandOffDispatchIn> getHandOffBatchDispatches(String batchId) throws RoutingServiceException {
		return getService().getHandOffBatchDispatches(batchId);
	}
	
	public Map<RoutingTimeOfDay, EnumHandOffDispatchStatus> getHandOffBatchDispatchStatus(String batchId) throws RoutingServiceException {
		return getService().getHandOffBatchDispatchStatus(batchId);
	}
	
	public Map<RoutingTimeOfDay, Integer> getHandOffBatchDispatchCnt(Date deliveryDate) throws RoutingServiceException {
		return getService().getHandOffBatchDispatchCnt(deliveryDate);
	}
	
	public List<IHandOffBatchPlan> getHandOffBatchPlansByDispatchStatus(String handoffBatchId, Date deliveryDate) throws RoutingServiceException {
		return getService().getHandOffBatchPlansByDispatchStatus(handoffBatchId, deliveryDate);
	}
	
	public List<IHandOffBatchDispatchResource> getHandOffBatchPlanResourcesByDispatchStatus(String handoffBatchId, Date deliveryDate) throws RoutingServiceException {
		return getService().getHandOffBatchPlanResourcesByDispatchStatus(handoffBatchId, deliveryDate);
	}
	
	public List<IHandOffBatchRoute> getHandOffBatchDispatchRoutes(String handoffBatchId, Date deliveryDate) throws RoutingServiceException {
		return getService().getHandOffBatchDispatchRoutes(handoffBatchId, deliveryDate);
	}
	
	public List<Truck> getAvailableTrucksInService(String assetType, Date deliveryDate, String assetStatus) throws RoutingServiceException {
		return getService().getAvailableTrucksInService(assetType, deliveryDate, assetStatus);
	}
	
	public List<TruckPreferenceStat> getEmployeeTruckPreferences() throws RoutingServiceException {
		return getService().getEmployeeTruckPreferences();
	}
	
	public void clearHandOffBatchAutoDispatches(String handoffBatchId, Date deliveryDate) throws RoutingServiceException{
		getService().clearHandOffBatchAutoDispatches(handoffBatchId, deliveryDate);
	}
	
	public void addNewHandOffBatchAutoDispatches(Collection dataList) throws RoutingServiceException {
		getService().addNewHandOffBatchAutoDispatches(dataList);
	}
	
	public void addNewHandOffBatchAutoDispatchResources(Collection dataList) throws RoutingServiceException {
		getService().addNewHandOffBatchAutoDispatchResources(dataList);
	}
	
	public Set<IHandOffDispatch> getHandOffDispatch(String handoffBatchId, Date deliveryDate) throws RoutingServiceException {
		return getService().getHandOffDispatch(handoffBatchId, deliveryDate);
	}
		
	public String getLastCommittedHandOffBatch(Date deliveryDate) throws RoutingServiceException {
		return getService().getLastCommittedHandOffBatch(deliveryDate);
	}
}
