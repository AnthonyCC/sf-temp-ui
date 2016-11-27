package com.freshdirect.routing.service;

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
import com.freshdirect.routing.model.IHandOffBatchPlan;
import com.freshdirect.routing.model.IHandOffBatchPlanResource;
import com.freshdirect.routing.model.IHandOffBatchRoute;
import com.freshdirect.routing.model.IHandOffBatchRouteBreak;
import com.freshdirect.routing.model.IHandOffBatchStop;
import com.freshdirect.routing.model.IHandOffBatchTrailer;
import com.freshdirect.routing.model.IHandOffDispatch;
import com.freshdirect.routing.model.TriggerHandOffResult;
import com.freshdirect.routing.model.TruckPreferenceStat;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.truckassignment.Truck;
import com.freshdirect.routing.util.RoutingTimeOfDay;
import com.freshdirect.sap.bapi.BapiSendHandOff.HandOffDispatchIn;


public interface IHandOffService {
	
	TriggerHandOffResult createNewHandOffBatch(Date deliveryDate, String userId, String scenario, Date cutOffDateTime, boolean isStandByMode) throws RoutingServiceException;
	List<IHandOffBatchStop> getOrderByCutoff(final Date deliveryDate, final Date cutOff) throws RoutingServiceException;
	
	void updateHandOffBatchMessage(String handOffBatchId, String message) throws RoutingServiceException;
	void updateHandOffBatchStatus(String handOffBatchId, EnumHandOffBatchStatus status) throws RoutingServiceException;
	
	void addNewHandOffBatchAction(String handOffBatchId, Date actionDateTime
										, EnumHandOffBatchActionType actionType
											, String userId) throws RoutingServiceException;
	void addNewHandOffBatchSession(String handOffBatchId, String sessionName, String region) throws RoutingServiceException;
	void addNewHandOffBatchStops(List<IHandOffBatchStop> dataList) throws RoutingServiceException;
	
	Set<IHandOffBatch> getHandOffBatch(Date deliveryDate) throws RoutingServiceException;
	
	IHandOffBatch getHandOffBatchById(String batchId) throws RoutingServiceException;
	
	Map<String, Integer> getHandOffBatchRouteCnt(Date deliveryDate) throws RoutingServiceException;
	
	void clearHandOffBatch(String handOffBatchId) throws RoutingServiceException;
	
	void clearHandOffBatchStopRoute(Date deliveryDate, String handOffBatchId) throws RoutingServiceException;
	
	void updateHandOffBatchDetails(String handOffBatchId, List<IHandOffBatchTrailer> trailers, List<IHandOffBatchRoute> routes
			, List<IHandOffBatchStop> stops, Map<RoutingTimeOfDay, EnumHandOffDispatchStatus> dispatchStatus, List<IHandOffBatchRouteBreak> breaks) throws RoutingServiceException;
	
	List<IHandOffBatchStop> getHandOffBatchStops(String batchId, boolean filterException) throws RoutingServiceException;
	List<IHandOffBatchRoute> getHandOffBatchRoutes(String batchId) throws RoutingServiceException;
	
	void addNewHandOffBatchDepotSchedules(String handOffBatchId, Set<IHandOffBatchDepotSchedule> dataList) throws RoutingServiceException;
	
	Map<EnumSaleStatus, Integer> getOrderStatsByCutoff(Date deliveryDate, Date cutOff) throws RoutingServiceException;
	
	IHandOffBatchRoute getHandOffBatchStopsByRoute(Date deliveryDate, String routeId, boolean filterException) throws RoutingServiceException;
	
	void updateHandOffBatchCommitEligibility(String handOffBatchId, boolean isEligibleForCommit) throws RoutingServiceException;
	
	void updateHandOffStopException(String handOffBatchId, List<String> exceptionOrderIds) throws RoutingServiceException;
	
	void updateHandOffBatchStopErpNo(List<IHandOffBatchStop> dataList) throws RoutingServiceException;
	
	void addNewHandOffBatchDepotSchedulesEx(String dayOfWeek, Date cutOffTime, Set<IHandOffBatchDepotScheduleEx> dataList) throws RoutingServiceException;
	Set<IHandOffBatchDepotScheduleEx> getHandOffBatchDepotSchedulesEx(String dayOfWeek, Date cutOffTime) throws RoutingServiceException;
	
	Map<String, Map<RoutingTimeOfDay, Set<IHandOffBatchDepotScheduleEx>>> getHandOffBatchDepotSchedulesEx(String dayOfWeek) throws RoutingServiceException;
	
	List<HandOffDispatchIn> getHandOffBatchDispatches(String batchId) throws RoutingServiceException;
	Map<RoutingTimeOfDay, EnumHandOffDispatchStatus> getHandOffBatchDispatchStatus(String batchId) throws RoutingServiceException;
	Map<RoutingTimeOfDay, Integer> getHandOffBatchDispatchCnt(Date deliveryDate) throws RoutingServiceException;
	
	List<IHandOffBatchPlan> getHandOffBatchPlans(String handoffBatchId, Date deliveryDate, Date cutOffDate) throws RoutingServiceException;

	List<IHandOffBatchPlanResource> getHandOffBatchPlanResources(String handoffBatchId, Date deliveryDate, Date cutOffDate) throws RoutingServiceException;
	
	List<IHandOffBatchRoute> getHandOffBatchDispatchRoutes(String handoffBatchId, Date deliveryDate, Date cutOffDate) throws RoutingServiceException;
	
	List<Truck> getAvailableTrucksInService(Date deliveryDate) throws RoutingServiceException;
	
	List<TruckPreferenceStat> getEmployeeTruckPreferences() throws RoutingServiceException;
	
	void clearHandOffBatchAutoDispatches(String handoffBatchId, Date deliveryDate, Date cutOffTime) throws RoutingServiceException;
	
	void addNewHandOffBatchAutoDispatches(Collection dataList) throws RoutingServiceException;
			
	String getLastCommittedHandOffBatch(Date deliveryDate) throws RoutingServiceException;

	Map<String, Integer> getHandOffBatchTrailerCnt(final Date deliveryDate) throws RoutingServiceException;
	
	List<IHandOffBatchTrailer> getHandOffBatchTrailers(String batchId) throws RoutingServiceException;
	
	Map<String, IHandOffDispatch> getHandOffBatchDispatchs(Date deliveryDate, Date cutOffDate) throws RoutingServiceException;
	
	void updateHandOffDispatchTruckInfo(List<IHandOffDispatch> dispatchEntry) throws RoutingServiceException;
	
	int getStopCount(String batchId) throws RoutingServiceException;
	
	List<IHandOffBatchRouteBreak> getHandOffBatchRouteBreaks(String batchId) throws RoutingServiceException;
	
	void updateOrderUnassignedInfo(List<IHandOffBatchStop> unassignedOrders) throws RoutingServiceException;
		
	
}
