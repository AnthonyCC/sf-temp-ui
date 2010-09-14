package com.freshdirect.routing.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.routing.constants.EnumHandOffBatchActionType;
import com.freshdirect.routing.constants.EnumHandOffBatchStatus;
import com.freshdirect.routing.model.IHandOffBatch;
import com.freshdirect.routing.model.IHandOffBatchDepotSchedule;
import com.freshdirect.routing.model.IHandOffBatchRoute;
import com.freshdirect.routing.model.IHandOffBatchStop;


public interface IHandOffDAO {
		
	String getNewHandOffBatchId() throws SQLException;
	List<IHandOffBatchStop> getOrderByCutoff(final Date deliveryDate, final Date cutOff) throws SQLException;
	
	String addNewHandOffBatch(Date deliveryDate, String scenario, Date cutOffDateTime, boolean isStandByMode) throws SQLException;
	void addNewHandOffBatchSession(String handOffBatchId, String sessionName, String region) throws SQLException;
	void addNewHandOffBatchDepotSchedules(Set<IHandOffBatchDepotSchedule> dataList) throws SQLException;
	void addNewHandOffBatchAction(String handOffBatchId, Date actionDateTime
									, EnumHandOffBatchActionType actionType, String userId) throws SQLException;
	void addNewHandOffBatchStops(List<IHandOffBatchStop> dataList) throws SQLException;
		
	void updateHandOffBatchMessage(String handOffBatchId, String message) throws SQLException;
	void updateHandOffBatchStatus(String handOffBatchId, EnumHandOffBatchStatus status) throws SQLException;
	
	Set<IHandOffBatch> getHandOffBatch(final Date deliveryDate) throws SQLException;
	
	IHandOffBatch getHandOffBatchById(final String batchId) throws SQLException;
	
	Map<String, Integer> getHandOffBatchRouteCnt(final Date deliveryDate) throws SQLException;
	
	void clearHandOffBatchSession(String handOffBatchId) throws SQLException;
	void clearHandOffBatchDepotSchedule(String handOffBatchId) throws SQLException;
	void clearHandOffBatchStops(String handOffBatchId) throws SQLException;
	void clearHandOffBatchRoutes(String handOffBatchId) throws SQLException;
	
	void addNewHandOffBatchRoutes(List<IHandOffBatchRoute> dataList) throws SQLException;
	
	void updateHandOffBatchStopRoute(List<IHandOffBatchStop> dataList) throws SQLException;
	
	void updateHandOffBatchStopErpNo(List<IHandOffBatchStop> dataList) throws SQLException;
	
	void clearHandOffBatchStopsRoute(String handOffBatchId) throws SQLException;
	
	List<IHandOffBatchStop> getHandOffBatchStops(final String batchId, final boolean filterException) throws SQLException;
	List<IHandOffBatchRoute> getHandOffBatchRoutes(final String batchId) throws SQLException;
	
	Map<EnumSaleStatus, Integer> getOrderStatsByCutoff(final Date deliveryDate, final Date cutOff) throws SQLException;
	
	IHandOffBatchRoute getHandOffBatchStopsByRoute(final Date deliveryDate, final String routeId, final boolean filterException) throws SQLException;
	
	void updateHandOffBatchCommitEligibility(String handOffBatchId, boolean isEligibleForCommit) throws SQLException;
	
	void updateHandOffStopException(String handOffBatchId, List<String> exceptionOrderIds) throws SQLException;
	
	void clearHandOffStopException(String handOffBatchId) throws SQLException;
}
