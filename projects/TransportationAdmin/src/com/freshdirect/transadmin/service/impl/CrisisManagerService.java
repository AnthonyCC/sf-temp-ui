package com.freshdirect.transadmin.service.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.transadmin.constants.EnumCrisisMngBatchActionType;
import com.freshdirect.transadmin.constants.EnumCrisisMngBatchStatus;
import com.freshdirect.transadmin.constants.EnumCrisisMngBatchType;
import com.freshdirect.transadmin.dao.ICrisisManagerDAO;
import com.freshdirect.routing.manager.IProcessMessage;
import com.freshdirect.transadmin.manager.ICrisisManagerProcessMessage;
import com.freshdirect.transadmin.model.IActiveOrderModel;
import com.freshdirect.transadmin.model.ICancelOrderModel;
import com.freshdirect.transadmin.model.ICrisisManagerBatch;
import com.freshdirect.transadmin.model.ICrisisManagerBatchDeliverySlot;
import com.freshdirect.transadmin.model.ICrisisManagerBatchReservation;
import com.freshdirect.routing.model.ICrisisMngBatchOrder;
import com.freshdirect.routing.model.ICustomerModel;
import com.freshdirect.routing.model.StandingOrderModel;
import com.freshdirect.transadmin.util.TransportationAdminProperties;
import com.freshdirect.transadmin.web.model.TriggerCrisisManagerResult;
import com.freshdirect.transadmin.service.ICrisisManagerService;
import com.freshdirect.routing.util.RoutingDateUtil;
import com.freshdirect.routing.util.RoutingServicesProperties;
import com.freshdirect.routing.util.RoutingUtil;
import com.freshdirect.transadmin.service.exception.IIssue;
import com.freshdirect.transadmin.service.exception.TransAdminServiceException;

public class CrisisManagerService implements ICrisisManagerService {
	
	private ICrisisManagerDAO crisisManagerDAOImpl;

	public ICrisisManagerDAO getCrisisManagerDAOImpl() {
		return crisisManagerDAOImpl;
	}

	public void setCrisisManagerDAOImpl(ICrisisManagerDAO crisisManagerDAOImpl) {
		this.crisisManagerDAOImpl = crisisManagerDAOImpl;
	}

	public TriggerCrisisManagerResult createNewCrisisMngBatch(Date deliveryDate, Date destinationDate, String userId, String[] zone, 
			Date cutOffDateTime, Date startTime, Date endTime, String[] deliveryType, EnumCrisisMngBatchType batchType, String profileName, boolean isStandByMode) throws TransAdminServiceException{
		
		TriggerCrisisManagerResult result = new TriggerCrisisManagerResult();
		String batchId = null;
		try {
			List<String> messages = new ArrayList<String>();			
			
			if(RoutingServicesProperties.getRoutingCutOffStandByEnabled()) {
				messages.add(IProcessMessage.INFO_MESSAGE_STANDBYMODE);
				isStandByMode = true;
			}
			
			batchId = getCrisisManagerDAOImpl().addNewCrisisMngBatch(deliveryDate, destinationDate, zone
													,cutOffDateTime, startTime, endTime, deliveryType, batchType, profileName, isStandByMode);
			getCrisisManagerDAOImpl().addNewCrisisMngBatchAction(batchId, RoutingDateUtil.getCurrentDateTime()
													, EnumCrisisMngBatchActionType.CREATE, userId);
			if(batchId != null) {
				ICrisisManagerBatch batch = getCrisisManagerDAOImpl().getCrisisMngBatchById(batchId);
				if(EnumCrisisMngBatchType.REGULARORDER.equals(batch.getBatchType())){
					messages.add(ICrisisManagerProcessMessage.INFO_MESSAGE_CRISISMNGREGORDERBATCHTRIGERRED);
				}else{
					messages.add(ICrisisManagerProcessMessage.INFO_MESSAGE_CRISISMNGSOORDERBATCHTRIGERRED);
				}
			}			
			result.setCrisisMngBatchId(batchId);
			result.setMessages(messages);
			
		}catch (SQLException exp) {
			throw new TransAdminServiceException(exp, IIssue.PROCESS_CRISISMNGBATCH_ERROR);
		}catch (ParseException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);
		}
		
		return result;
	}
	
	public Set<ICrisisManagerBatch> getCrisisMngBatch(final Date deliveryDate) throws TransAdminServiceException {
		try {
			return getCrisisManagerDAOImpl().getCrisisMngBatch(deliveryDate);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new TransAdminServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);
		}
	}
	
	public void updateCrisisMngBatchMessage(String orderScenarioBatchId, String message) throws TransAdminServiceException {
		try {
			getCrisisManagerDAOImpl().updateCrisisMngBatchMessage(orderScenarioBatchId, message);
		} catch (SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);
		}
	}
	
	public void updateCrisisMngBatchStatus(String crisisMngBatchId, EnumCrisisMngBatchStatus status) throws TransAdminServiceException {
		try {
			getCrisisManagerDAOImpl().updateCrisisMngBatchStatus(crisisMngBatchId, status);
		} catch (SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);
		}
	}
	
	public ICrisisManagerBatch getCrisisMngBatchById(String batchId) throws TransAdminServiceException {
		try{
			return getCrisisManagerDAOImpl().getCrisisMngBatchById(batchId);
		}catch(SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public void addNewCrisisMngBatchAction(String crisisMngBatchId, Date actionDateTime, EnumCrisisMngBatchActionType actionType
			, String userId) throws TransAdminServiceException{
		try{
			getCrisisManagerDAOImpl().addNewCrisisMngBatchAction(crisisMngBatchId, actionDateTime, actionType, userId);
		}catch(SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public void clearCrisisMngBatch(String batchId, EnumCrisisMngBatchType batchType) throws TransAdminServiceException{
		try{
			getCrisisManagerDAOImpl().clearCrisisMngBatchCustomer(batchId);
			if(EnumCrisisMngBatchType.REGULARORDER.equals(batchType)){
				getCrisisManagerDAOImpl().clearCrisisMngBatchReservation(batchId);
				getCrisisManagerDAOImpl().clearCrisisMngBatchOrder(batchId);
			} else {				
				getCrisisManagerDAOImpl().clearCrisisMngBatchStandingOrder(batchId);
			}
			getCrisisManagerDAOImpl().clearCrisisMngBatchDeliverySlot(batchId);
		}catch(SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public List<ICrisisMngBatchOrder> getOrderByCriteria(Date deliveryDate,Date cutOffDateTime, String[] area, String startTime, String endTime, String[] deliveryType, String profileName) throws TransAdminServiceException{
		try{
			return getCrisisManagerDAOImpl().getOrderByCriteria(deliveryDate, cutOffDateTime, area, startTime, endTime, deliveryType, profileName);
		}catch(SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public Map<EnumSaleStatus, Integer> getOrderStatsByDate(Date deliveryDate, String batchId, EnumCrisisMngBatchType batchType) throws TransAdminServiceException {
		try{
			return getCrisisManagerDAOImpl().getOrderStatsByDate(deliveryDate, batchId, batchType);
		}catch(SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public void addNewCrisisMngBatchCustomer(Set<ICustomerModel> batchCustomers, String batchId) throws TransAdminServiceException {
		try{
			List<ICustomerModel> customers = new ArrayList<ICustomerModel>();
			for(ICustomerModel cust : batchCustomers){
				customers.add(cust);
			}
			if(customers != null) {
				List<List<?>> buckets = RoutingUtil.splitList(customers, RoutingServicesProperties.getJDBCBatchUpdateThreshold());
				for(List bucket: buckets) {
					getCrisisManagerDAOImpl().addNewCrisisMngBatchCustomer(bucket, batchId);
				}
			}			
		}catch(SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public void addNewCrisisMngBatchRegularOrder(List<ICrisisMngBatchOrder> batchOrders, String batchId) throws TransAdminServiceException {
		try{
			if(batchOrders != null) {
				List<List<?>> buckets = RoutingUtil.splitList(batchOrders, RoutingServicesProperties.getJDBCBatchUpdateThreshold());
				for(List bucket: buckets) {
					getCrisisManagerDAOImpl().addNewCrisisMngBatchRegularOrder(bucket, batchId);
				}
			}
		}catch(SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public List<ICrisisMngBatchOrder> getCrisisMngBatchRegularOrder(String batchId, boolean filterException, boolean filterOrder) throws TransAdminServiceException {
		try{
			return getCrisisManagerDAOImpl().getCrisisMngBatchRegularOrder(batchId, filterException, filterOrder);
		}catch(SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public void updateCrisisMngOrderException(String orderCrisisBatchId, String batchType, List<String> exceptionOrderIds) throws TransAdminServiceException {
		try{
			if(exceptionOrderIds != null) {
				List<List<?>> buckets = RoutingUtil.splitList(exceptionOrderIds, TransportationAdminProperties.getJDBCBatchUpdateThreshold());
				for(List bucket: buckets) {
					getCrisisManagerDAOImpl().updateCrisisMngOrderException(orderCrisisBatchId, batchType, bucket);
				}
			}
		}catch(SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public List<ICrisisManagerBatchReservation> getReservationByCriteria(Date deliveryDate, Date cutOffDateTime, String[] area, String startTime, String endTime, String profileName) throws TransAdminServiceException {
		try{
			return getCrisisManagerDAOImpl().getReservationByCriteria(deliveryDate, cutOffDateTime, area, startTime, endTime, profileName);
		}catch(SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public void addNewCrisisMngBatchReservation(List<ICrisisManagerBatchReservation> reservations, String batchId) throws TransAdminServiceException {
		try{
			if(reservations != null) {
				List<List<?>> buckets = RoutingUtil.splitList(reservations, RoutingServicesProperties.getJDBCBatchUpdateThreshold());
				for(List bucket: buckets) {
					getCrisisManagerDAOImpl().addNewCrisisMngBatchReservation(bucket, batchId);
				}
			}
		}catch(SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public Map<String, ICrisisManagerBatchReservation> getCrisisMngBatchReservation(String batchId, boolean filterException) throws TransAdminServiceException {
		try{
			return getCrisisManagerDAOImpl().getCrisisMngBatchReservation(batchId, filterException);
		}catch(SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public void updateCrisisMngReservationException(String crisisMngBatchId, List<String> exceptionRsvIds) throws TransAdminServiceException {
		try{
			if(exceptionRsvIds != null) {
				List<List<?>> buckets = RoutingUtil.splitList(exceptionRsvIds, TransportationAdminProperties.getJDBCBatchUpdateThreshold());
				for(List bucket: buckets) {
					getCrisisManagerDAOImpl().updateCrisisMngReservationException(crisisMngBatchId, bucket);
				}
			}
		}catch(SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public void updateCrisisMngOrderStatus(String crisisMngBatchId, String batchType, List<String> exceptionOrderIds) throws TransAdminServiceException {
		try{
			if(exceptionOrderIds != null) {
				List<List<?>> buckets = RoutingUtil.splitList(exceptionOrderIds, TransportationAdminProperties.getJDBCBatchUpdateThreshold());
				for(List bucket: buckets) {
					getCrisisManagerDAOImpl().updateCrisisMngOrderStatus(crisisMngBatchId, batchType, bucket);
				}
			}
		} catch(SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public void updateCrisisMngReservationStatus(String crisisMngBatchId, List<String> exceptionRsvIds) throws TransAdminServiceException {
		try{
			if(exceptionRsvIds != null) {
				List<List<?>> buckets = RoutingUtil.splitList(exceptionRsvIds, TransportationAdminProperties.getJDBCBatchUpdateThreshold());
				for(List bucket: buckets) {
					getCrisisManagerDAOImpl().updateCrisisMngReservationStatus(crisisMngBatchId, bucket);
				}
			}
			getCrisisManagerDAOImpl().updateCrisisMngReservationStatus(crisisMngBatchId, exceptionRsvIds);
		}catch(SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public Map<String, List<ICrisisManagerBatchDeliverySlot>> getCrisisMngBatchTimeslotByZone(String crisisMngBatchId, EnumCrisisMngBatchType batchType) throws TransAdminServiceException {
		try{
			return getCrisisManagerDAOImpl().getCrisisMngBatchTimeslotByZone(crisisMngBatchId, batchType);
		}catch(SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public Map<String, List<ICrisisManagerBatchDeliverySlot>> getTimeslotByDate(Date deliveryDate) throws TransAdminServiceException {
		try{
			return getCrisisManagerDAOImpl().getTimeslotByDate(deliveryDate);
		}catch(SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public void addCrisisMngBatchDeliveryslot(Map<String, List<ICrisisManagerBatchDeliverySlot>> groupedSlots) throws TransAdminServiceException {
		try{
			getCrisisManagerDAOImpl().addCrisisMngBatchDeliveryslot(groupedSlots);
		}catch(SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public void updateCrisisMngBatchDeliveryslot(List<ICrisisManagerBatchDeliverySlot> groupedSlots) throws TransAdminServiceException {
		try{
			getCrisisManagerDAOImpl().updateCrisisMngBatchDeliveryslot(groupedSlots);
		}catch(SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public Map<String, List<ICrisisManagerBatchDeliverySlot>> getCrisisMngBatchTimeslot(String batchId, boolean filterException) throws TransAdminServiceException {
		try{
			return getCrisisManagerDAOImpl().getCrisisMngBatchTimeslot(batchId, filterException);
		}catch(SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public void clearCrisisMngBatchDeliverySlot(String batchId) throws TransAdminServiceException {
		try{
			getCrisisManagerDAOImpl().clearCrisisMngBatchDeliverySlot(batchId);
		}catch(SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}	
	
	public List<ICrisisMngBatchOrder> getStandingOrderByCriteria(Date deliveryDate,Date cutOffDateTime, String[] area, String startTime, String endTime, String[] deliveryType, String profileName) throws TransAdminServiceException{
		try{
			return getCrisisManagerDAOImpl().getStandingOrderByCriteria(deliveryDate, cutOffDateTime, area, startTime, endTime, deliveryType, profileName);
		}catch(SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public void addNewCrisisMngBatchStandingOrder(List<ICrisisMngBatchOrder> soOrders, String batchId) throws TransAdminServiceException{
		try{
			if(soOrders != null) {
				List<List<?>> buckets = RoutingUtil.splitList(soOrders, RoutingServicesProperties.getJDBCBatchUpdateThreshold());
				for(List bucket: buckets) {
					getCrisisManagerDAOImpl().addNewCrisisMngBatchStandingOrder(bucket, batchId);
				}
			}
		}catch(SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public List<ICrisisMngBatchOrder> getCrisisMngBatchStandingOrder(String batchId, boolean filterException,boolean filterOrder) throws TransAdminServiceException{
		try{
			return getCrisisManagerDAOImpl().getCrisisMngBatchStandingOrder(batchId, filterException, filterOrder);
		}catch(SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public void updateCrisisMngBatchStandingOrder(String batchId, List<StandingOrderModel> batchStandingOrders) throws TransAdminServiceException {
		try{
			if(batchStandingOrders != null) {
				List<List<?>> buckets = RoutingUtil.splitList(batchStandingOrders, TransportationAdminProperties.getJDBCBatchUpdateThreshold());
				for(List bucket: buckets) {
					getCrisisManagerDAOImpl().updateCrisisMngBatchStandingOrder(batchId, bucket);
				}
			}
		}catch(SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public List<IActiveOrderModel> getActiveOrderByArea(Date deliveryDate, EnumCrisisMngBatchType batchType) throws TransAdminServiceException {
		try{
			return getCrisisManagerDAOImpl().getActiveOrderByArea(deliveryDate, batchType);
		}catch(SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public void updateCrisisMngBatchOrderReservation(String batchId, Map<String, String> batchOrderReservations) throws TransAdminServiceException {
		try{
			getCrisisManagerDAOImpl().updateCrisisMngBatchOrderReservation(batchId, batchOrderReservations);
		}catch(SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
	public List<ICancelOrderModel> getCancelOrderByArea(String batchId, EnumCrisisMngBatchType batchType) throws TransAdminServiceException {
		try{
			return getCrisisManagerDAOImpl().getCancelOrderByArea(batchId, batchType);
		}catch(SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_CRISISMNGBATCH_ERROR);			
		}
	}
	
}
