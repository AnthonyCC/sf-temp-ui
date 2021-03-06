package com.freshdirect.routing.service.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.routing.constants.EnumHandOffBatchActionType;
import com.freshdirect.routing.constants.EnumHandOffBatchStatus;
import com.freshdirect.routing.constants.EnumHandOffDispatchStatus;
import com.freshdirect.routing.dao.IHandOffDAO;
import com.freshdirect.routing.manager.IProcessMessage;
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
import com.freshdirect.routing.service.IHandOffService;
import com.freshdirect.routing.service.exception.IIssue;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.truckassignment.Truck;
import com.freshdirect.routing.util.RoutingDateUtil;
import com.freshdirect.routing.util.RoutingServicesProperties;
import com.freshdirect.routing.util.RoutingTimeOfDay;
import com.freshdirect.routing.util.RoutingUtil;
import com.freshdirect.sap.bapi.BapiSendHandOff.HandOffDispatchIn;

public class HandOffService extends BaseService implements IHandOffService {
	
	private IHandOffDAO handOffDAOImpl;

	public IHandOffDAO getHandOffDAOImpl() {
		return handOffDAOImpl;
	}

	public void setHandOffDAOImpl(IHandOffDAO handOffDAOImpl) {
		this.handOffDAOImpl = handOffDAOImpl;
	}
	
	public TriggerHandOffResult createNewHandOffBatch(Date deliveryDate, String userId, String scenario
															, Date cutOffDateTime
															, boolean isStandByMode) throws RoutingServiceException {
		TriggerHandOffResult result = new TriggerHandOffResult();
		
		String handOffBatchId = null;
		try {
			
			Map<EnumSaleStatus, Integer> orderStats = getHandOffDAOImpl().getOrderStatsByCutoff(deliveryDate, cutOffDateTime);
			List<String> messages = new ArrayList<String>();
						
			List<String> exceptions = new ArrayList<String>();
			
			for(Map.Entry<EnumSaleStatus, Integer> statusEntry : orderStats.entrySet()) {
				
				if(statusEntry.getKey().equals(EnumSaleStatus.NOT_SUBMITTED)
						|| statusEntry.getKey().equals(EnumSaleStatus.NEW)
						|| statusEntry.getKey().equals(EnumSaleStatus.MODIFIED)
							|| statusEntry.getKey().equals(EnumSaleStatus.MODIFIED_CANCELED)) {
					
					exceptions.add(statusEntry.getKey() +"="+ statusEntry.getValue());
				}
			}
			if(exceptions.size() > 0) {
				messages.add(IProcessMessage.ERROR_MESSAGE_PENDINGORDER);
				messages.addAll(exceptions);
			}
			
			if(RoutingServicesProperties.getRoutingCutOffStandByEnabled()) {
				messages.add(IProcessMessage.INFO_MESSAGE_STANDBYMODE);
				isStandByMode = true;
			}
			
			handOffBatchId = getHandOffDAOImpl().addNewHandOffBatch(deliveryDate, scenario, cutOffDateTime, isStandByMode);
			getHandOffDAOImpl().addNewHandOffBatchAction(handOffBatchId, RoutingDateUtil.getCurrentDateTime()
															, EnumHandOffBatchActionType.CREATE
															, userId);
			messages.add(IProcessMessage.INFO_MESSAGE_HANDOFFBATCHTRIGERRED);
			result.setHandOffBatchId(handOffBatchId);
			result.setMessages(messages);
		}  catch (SQLException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		} catch (ParseException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
		return result;
	}
	
	public List<IHandOffBatchStop> getOrderByCutoff(final Date deliveryDate, final Date cutOff) throws RoutingServiceException {
		try {
			return getHandOffDAOImpl().getOrderByCutoff(deliveryDate, cutOff);
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
	}
	
	public void updateHandOffBatchMessage(String handOffBatchId, String message) throws RoutingServiceException {
		try {
			getHandOffDAOImpl().updateHandOffBatchMessage(handOffBatchId, message);
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
	}
	
	public void updateHandOffBatchStatus(String handOffBatchId, EnumHandOffBatchStatus status) throws RoutingServiceException {
		try {
			getHandOffDAOImpl().updateHandOffBatchStatus(handOffBatchId, status);
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
	}
	
	public void addNewHandOffBatchAction(String handOffBatchId, Date actionDateTime
												, EnumHandOffBatchActionType actionType
												, String userId) throws RoutingServiceException {
		try {
			getHandOffDAOImpl().addNewHandOffBatchAction(handOffBatchId, actionDateTime, actionType, userId);
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
		
	}
	
	public void addNewHandOffBatchSession(String handOffBatchId, String sessionName, String region) throws RoutingServiceException {
		try {
			getHandOffDAOImpl().addNewHandOffBatchSession(handOffBatchId, sessionName, region);
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
	}
	
	public void addNewHandOffBatchDepotSchedules(String handOffBatchId, Set<IHandOffBatchDepotSchedule> dataList) throws RoutingServiceException {
		try {
			getHandOffDAOImpl().clearHandOffBatchDepotSchedule(handOffBatchId);
			getHandOffDAOImpl().addNewHandOffBatchDepotSchedules(dataList);
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
	}
	
	public void addNewHandOffBatchDepotSchedulesEx(String dayOfWeek, Date cutOffTime,
													Set<IHandOffBatchDepotScheduleEx> dataList) throws RoutingServiceException {
		try {
			getHandOffDAOImpl().clearHandOffBatchDepotScheduleEx(dayOfWeek, cutOffTime);
			getHandOffDAOImpl().addNewHandOffBatchDepotSchedulesEx(dataList);
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
	}
	
	public void addNewHandOffBatchStops(List<IHandOffBatchStop> dataList) throws RoutingServiceException {
		try {
			if(dataList != null) {
				List<List<?>> buckets = RoutingUtil.splitList(dataList, RoutingServicesProperties.getJDBCBatchUpdateThreshold());
				for(List bucket: buckets) {
					getHandOffDAOImpl().addNewHandOffBatchStops(bucket);
				}
			}
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
	}
	
	public Set<IHandOffBatch> getHandOffBatch(Date deliveryDate) throws RoutingServiceException {
		try {
			return getHandOffDAOImpl().getHandOffBatch(deliveryDate);
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
	}
	
	public IHandOffBatch getHandOffBatchById(String batchId) throws RoutingServiceException {
		try {
			return getHandOffDAOImpl().getHandOffBatchById(batchId);
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
	}
	
	public Map<String, Integer> getHandOffBatchRouteCnt(Date deliveryDate) throws RoutingServiceException {
		try {
			return getHandOffDAOImpl().getHandOffBatchRouteCnt(deliveryDate);
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
	}
	
	public void clearHandOffBatch(String handOffBatchId) throws RoutingServiceException {
		try {
			getHandOffDAOImpl().clearHandOffBatchRouteBreaks(handOffBatchId);
			getHandOffDAOImpl().clearHandOffBatchStops(handOffBatchId);
			getHandOffDAOImpl().clearHandOffBatchRoutes(handOffBatchId);
			getHandOffDAOImpl().clearHandOffBatchTrailers(handOffBatchId);
			getHandOffDAOImpl().clearHandOffBatchSession(handOffBatchId);
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
	}
	
	public void clearHandOffBatchStopRoute(Date deliveryDate, String handOffBatchId) throws RoutingServiceException {
		try {
			getHandOffDAOImpl().clearHandOffBatchRouteBreaks(handOffBatchId);
			getHandOffDAOImpl().clearHandOffBatchStopsRoute(handOffBatchId);
			getHandOffDAOImpl().clearHandOffBatchRoutes(handOffBatchId);	
			getHandOffDAOImpl().clearHandOffBatchTrailers(handOffBatchId);
			getHandOffDAOImpl().clearHandOffBatchDispatches(handOffBatchId);
			
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
	}
	
	public void updateHandOffBatchDetails(String handOffBatchId, List<IHandOffBatchTrailer> trailers, List<IHandOffBatchRoute> routes
											, List<IHandOffBatchStop> stops, Map<RoutingTimeOfDay, EnumHandOffDispatchStatus> dispatchStatus, List<IHandOffBatchRouteBreak> breaks) throws RoutingServiceException {
		try {
			getHandOffDAOImpl().addNewHandOffBatchDispatches(handOffBatchId, dispatchStatus);
			getHandOffDAOImpl().addNewHandOffBatchRoutes(routes);			
			getHandOffDAOImpl().addNewHandOffBatchTrailers(trailers);
			if(stops != null) {
				List<List<?>> buckets = RoutingUtil.splitList(stops, RoutingServicesProperties.getJDBCBatchUpdateThreshold());
				for(List bucket: buckets) {
					getHandOffDAOImpl().updateHandOffBatchStopRoute(bucket);
				}
			}
			getHandOffDAOImpl().updateHandOffBatchTrailerRoute(trailers);
			getHandOffDAOImpl().addNewHandOffRouteBreaks(breaks);
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
	}
	
	public List<IHandOffBatchStop> getHandOffBatchStops(String batchId, boolean filterException) throws RoutingServiceException {
		try {
			return getHandOffDAOImpl().getHandOffBatchStops(batchId, filterException);
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
	}
	
	public List<IHandOffBatchRoute> getHandOffBatchRoutes(String batchId) throws RoutingServiceException {
		try {
			return getHandOffDAOImpl().getHandOffBatchRoutes(batchId);
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
	}
	
	public List<HandOffDispatchIn> getHandOffBatchDispatches(String batchId) throws RoutingServiceException {
		try {
			return getHandOffDAOImpl().getHandOffBatchDispatches(batchId);
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
	}
	
	public Map<RoutingTimeOfDay, EnumHandOffDispatchStatus> getHandOffBatchDispatchStatus(String batchId) throws RoutingServiceException {
		try {
			return getHandOffDAOImpl().getHandOffBatchDispatchStatus(batchId);
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
	}
	
	public Map<RoutingTimeOfDay, Integer> getHandOffBatchDispatchCnt(Date deliveryDate) throws RoutingServiceException {
		try {
			return getHandOffDAOImpl().getHandOffBatchDispatchCnt(deliveryDate);
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
	}
	
	public Map<EnumSaleStatus, Integer> getOrderStatsByCutoff(Date deliveryDate, Date cutOff) throws RoutingServiceException {
		try {
			return getHandOffDAOImpl().getOrderStatsByCutoff(deliveryDate, cutOff);
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
	}
	
	public IHandOffBatchRoute getHandOffBatchStopsByRoute(Date deliveryDate, String routeId, boolean filterException) throws RoutingServiceException {
		try {
			return getHandOffDAOImpl().getHandOffBatchStopsByRoute(deliveryDate, routeId, filterException);
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
	}
	
	public void updateHandOffBatchCommitEligibility(String handOffBatchId, boolean isEligibleForCommit) throws RoutingServiceException {
		try {
			getHandOffDAOImpl().updateHandOffBatchCommitEligibility(handOffBatchId, isEligibleForCommit);
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
	}
	
	public void updateHandOffStopException(String handOffBatchId, List<String> exceptionOrderIds) throws RoutingServiceException {
		try {
			getHandOffDAOImpl().clearHandOffStopException(handOffBatchId);
			if(exceptionOrderIds != null && exceptionOrderIds.size() > 0) {
				getHandOffDAOImpl().updateHandOffStopException(handOffBatchId, exceptionOrderIds);
			}
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
	}
	
	public void updateHandOffBatchStopErpNo(List<IHandOffBatchStop> dataList) throws RoutingServiceException {
		try {
			getHandOffDAOImpl().updateHandOffBatchStopErpNo(dataList);
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
	}
	
	public Set<IHandOffBatchDepotScheduleEx> getHandOffBatchDepotSchedulesEx(String dayOfWeek, Date cutOffTime) throws RoutingServiceException {
		try {
			return getHandOffDAOImpl().getHandOffBatchDepotSchedulesEx(dayOfWeek, cutOffTime);
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
	}
	
	public Map<String, Map<RoutingTimeOfDay, Set<IHandOffBatchDepotScheduleEx>>> getHandOffBatchDepotSchedulesEx(String dayOfWeek) throws RoutingServiceException {
		try {
			return getHandOffDAOImpl().getHandOffBatchDepotSchedulesEx(dayOfWeek);
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
	}
	
	public List<IHandOffBatchPlan> getHandOffBatchPlans(String handoffBatchId, Date deliveryDate, Date cutOffDate) throws RoutingServiceException {
		List<IHandOffBatchPlan> plans = new ArrayList<IHandOffBatchPlan>();
		try {			
			plans.addAll(getHandOffDAOImpl().getHandOffBatchPlans(handoffBatchId, deliveryDate, cutOffDate));
			
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
		return plans;
	}
	
	public List<IHandOffBatchPlanResource> getHandOffBatchPlanResources(String handoffBatchId, Date deliveryDate, final Date cutOffDate) throws RoutingServiceException {
		List<IHandOffBatchPlanResource> planResources = new ArrayList<IHandOffBatchPlanResource>();
		try {
			planResources.addAll(getHandOffDAOImpl().getHandOffBatchPlanResources(handoffBatchId, deliveryDate, cutOffDate));
			
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
		return planResources;
	}
	
	public List<IHandOffBatchRoute> getHandOffBatchDispatchRoutes(String handoffBatchId, Date deliveryDate, Date cutOffDate) throws RoutingServiceException {
		List<IHandOffBatchRoute> routes = new ArrayList<IHandOffBatchRoute>();
		try {
			routes.addAll(getHandOffDAOImpl().getHandOffBatchDispatchRoutes(handoffBatchId, deliveryDate, cutOffDate));			
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
		return routes;
	}
	
	public List<Truck> getAvailableTrucksInService(Date deliveryDate) throws RoutingServiceException {
		try {
			return getHandOffDAOImpl().getAvailableTrucksInService(deliveryDate);
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
	}
	
	
	public List<TruckPreferenceStat> getEmployeeTruckPreferences() throws RoutingServiceException {
		try {
			return getHandOffDAOImpl().getEmployeeTruckPreferences();
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
	}
	
	public void clearHandOffBatchAutoDispatches(String handoffBatchId, Date deliveryDate, Date cutOffTime) throws RoutingServiceException {
		try {
			getHandOffDAOImpl().clearHandOffBatchAutoDispatchResources(handoffBatchId, deliveryDate, cutOffTime); 
			getHandOffDAOImpl().clearHandOffBatchAutoDispatches(handoffBatchId, deliveryDate, cutOffTime);
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
	}
	
	public void addNewHandOffBatchAutoDispatches(Collection dataList) throws RoutingServiceException {
		try {			
			getHandOffDAOImpl().addNewHandOffBatchAutoDispatches(dataList);			
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
	}
		
	public String getLastCommittedHandOffBatch(Date deliveryDate) throws RoutingServiceException {
		try{
			return getHandOffDAOImpl().getLastCommittedHandOffBatch(deliveryDate);
		}catch(SQLException e){
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
	}

	public Map<String, Integer> getHandOffBatchTrailerCnt(Date deliveryDate) throws RoutingServiceException {
		try {
			return getHandOffDAOImpl().getHandOffBatchTrailerCnt(deliveryDate);
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
}
	}

	public List<IHandOffBatchTrailer> getHandOffBatchTrailers(String batchId) throws RoutingServiceException {
		try {
			return getHandOffDAOImpl().getHandOffBatchTrailers(batchId);
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
	}
	
	public Map<String, IHandOffDispatch> getHandOffBatchDispatchs(Date deliveryDate, Date cutOffDate) throws RoutingServiceException {
		try {
			return getHandOffDAOImpl().getHandOffBatchDispatchs(deliveryDate, cutOffDate);
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
	}
	
	public void updateHandOffDispatchTruckInfo(List<IHandOffDispatch> dispatchEntry) throws RoutingServiceException {
		try {
			getHandOffDAOImpl().updateHandOffDispatchTruckInfo(dispatchEntry);
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
	}

	@Override
	public int getStopCount(String batchId) throws RoutingServiceException {
		try {
			return getHandOffDAOImpl().getStopCount(batchId);
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
	}

	@Override
	public List<IHandOffBatchRouteBreak> getHandOffBatchRouteBreaks(
			String batchId) throws RoutingServiceException {
				try {
					return getHandOffDAOImpl().getHandOffBatchRouteBreaks(batchId);
				} catch (SQLException e) {
					throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
				}
			}

	@Override
	public void updateOrderUnassignedInfo(
			List<IHandOffBatchStop> unassignedOrders) throws RoutingServiceException {
		try {
			getHandOffDAOImpl().updateOrderUnassignedInfo(unassignedOrders);
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
	}
}
