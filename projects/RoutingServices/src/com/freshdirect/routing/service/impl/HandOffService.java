package com.freshdirect.routing.service.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.routing.constants.EnumHandOffBatchActionType;
import com.freshdirect.routing.constants.EnumHandOffBatchStatus;
import com.freshdirect.routing.dao.IHandOffDAO;
import com.freshdirect.routing.manager.IProcessMessage;
import com.freshdirect.routing.model.IHandOffBatch;
import com.freshdirect.routing.model.IHandOffBatchDepotSchedule;
import com.freshdirect.routing.model.IHandOffBatchRoute;
import com.freshdirect.routing.model.IHandOffBatchStop;
import com.freshdirect.routing.model.TriggerHandOffResult;
import com.freshdirect.routing.service.IHandOffService;
import com.freshdirect.routing.service.exception.IIssue;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.util.RoutingDateUtil;
import com.freshdirect.routing.util.RoutingServicesProperties;

public class HandOffService extends BaseService implements IHandOffService {
	
	private IHandOffDAO handOffDAOImpl;

	public IHandOffDAO getHandOffDAOImpl() {
		return handOffDAOImpl;
	}

	public void setHandOffDAOImpl(IHandOffDAO handOffDAOImpl) {
		this.handOffDAOImpl = handOffDAOImpl;
	}
	
	public TriggerHandOffResult createNewHandOffBatch(Date deliveryDate, String userId, String scenario
															, Date cutOffDateTime) throws RoutingServiceException {
		TriggerHandOffResult result = new TriggerHandOffResult();
		
		String handOffBatchId = null;
		try {
			StringBuffer strMessageBuf = new StringBuffer();
			boolean isStandByMode = false;
			Map<EnumSaleStatus, Integer> orderStats = getHandOffDAOImpl().getOrderStatsByCutoff(deliveryDate, cutOffDateTime);
			for(Map.Entry<EnumSaleStatus, Integer> statusEntry : orderStats.entrySet()) {
				
				if(statusEntry.getKey().equals(EnumSaleStatus.NOT_SUBMITTED)
						|| statusEntry.getKey().equals(EnumSaleStatus.NEW)
						|| statusEntry.getKey().equals(EnumSaleStatus.MODIFIED)
							|| statusEntry.getKey().equals(EnumSaleStatus.MODIFIED_CANCELED)) {
					
					strMessageBuf.append(IProcessMessage.ERROR_MESSAGE_PENDINGORDER).append("\n");
				}
			}
			if(RoutingServicesProperties.getRoutingCutOffStandByEnabled()) {
				strMessageBuf.append(IProcessMessage.INFO_MESSAGE_STANDBYMODE).append("\n");
				isStandByMode = true;
			}
			
			handOffBatchId = getHandOffDAOImpl().addNewHandOffBatch(deliveryDate, scenario, cutOffDateTime, isStandByMode);
			getHandOffDAOImpl().addNewHandOffBatchAction(handOffBatchId, RoutingDateUtil.getCurrentDateTime()
															, EnumHandOffBatchActionType.CREATE
															, userId);
			strMessageBuf.append(IProcessMessage.INFO_MESSAGE_HANDOFFBATCHTRIGERRED);
			result.setHandOffBatchId(handOffBatchId);
			result.setMessage(strMessageBuf.toString());
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
	
	public void addNewHandOffBatchStops(List<IHandOffBatchStop> dataList) throws RoutingServiceException {
		try {
			getHandOffDAOImpl().addNewHandOffBatchStops(dataList);
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
			getHandOffDAOImpl().clearHandOffBatchStops(handOffBatchId);
			getHandOffDAOImpl().clearHandOffBatchRoutes(handOffBatchId);
			getHandOffDAOImpl().clearHandOffBatchSession(handOffBatchId);
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
	}
	
	public void clearHandOffBatchStopRoute(String handOffBatchId) throws RoutingServiceException {
		try {
			getHandOffDAOImpl().clearHandOffBatchStopsRoute(handOffBatchId);
			getHandOffDAOImpl().clearHandOffBatchRoutes(handOffBatchId);			
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
	}
	
	public void updateHandOffBatchDetails(List<IHandOffBatchRoute> routes, List<IHandOffBatchStop> stops) throws RoutingServiceException {
		try {
			getHandOffDAOImpl().addNewHandOffBatchRoutes(routes);
			getHandOffDAOImpl().updateHandOffBatchStopRoute(stops);
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
			getHandOffDAOImpl().updateHandOffStopException(handOffBatchId, exceptionOrderIds);
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
	}
	
}
