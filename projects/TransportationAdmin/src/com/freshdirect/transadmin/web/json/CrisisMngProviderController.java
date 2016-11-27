package com.freshdirect.transadmin.web.json;

import static com.freshdirect.transadmin.manager.ICrisisManagerProcessMessage.ERROR_MESSAGE_TIMESLOTEXCEPTION;
import static com.freshdirect.transadmin.manager.ICrisisManagerProcessMessage.INFO_MESSAGE_STANDINGORDEREXPCLEARMSG;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.transadmin.model.CrisisManagerBatchDeliverySlot;
import com.freshdirect.transadmin.model.ICrisisManagerBatch;
import com.freshdirect.transadmin.model.ICrisisManagerBatchDeliverySlot;
import com.freshdirect.routing.model.ICrisisMngBatchOrder;
import com.freshdirect.routing.model.StandingOrderModel;
import com.freshdirect.transadmin.constants.EnumCrisisMngBatchStatus;
import com.freshdirect.transadmin.constants.EnumCrisisMngBatchType;
import com.freshdirect.transadmin.crisis.manager.action.CrisisManagerCancelAction;
import com.freshdirect.transadmin.crisis.manager.action.CrisisManagerCompleteAction;
import com.freshdirect.transadmin.crisis.manager.action.CrisisManagerCreateReservationAction;
import com.freshdirect.transadmin.crisis.manager.action.CrisisManagerOrderCancelAction;
import com.freshdirect.transadmin.crisis.manager.action.CrisisManagerOrderInAction;
import com.freshdirect.transadmin.crisis.manager.action.CrisisManagerPlaceOrderAction;
import com.freshdirect.transadmin.service.ICrisisManagerService;
import com.freshdirect.transadmin.service.exception.TransAdminServiceException;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.model.CrisisManagerBatchInfo;
import com.freshdirect.transadmin.web.model.CrisisManagerBatchStandingOrderInfo;
import com.freshdirect.transadmin.web.model.CrisisManagerBatchTimeslotInfo;


public class CrisisMngProviderController extends BaseJsonRpcController  implements ICrisisMngProvider {
	
	private ICrisisManagerService  crisisManagerService;
		
	public ICrisisManagerService getCrisisManagerService() {
		return crisisManagerService;
	}

	public void setCrisisManagerService(ICrisisManagerService crisisManagerService) {
		this.crisisManagerService = crisisManagerService;
	}

	private static Logger LOGGER = LoggerFactory.getInstance(CrisisMngProviderController.class);
	
	private class OrderCrisisBatchInfoComparator implements Comparator<CrisisManagerBatchInfo> {

		public int compare(CrisisManagerBatchInfo batch1, CrisisManagerBatchInfo batch2) {
			if(batch1.getLastActionDateTime() != null &&  batch2.getLastActionDateTime() != null) {
				return -(batch1.getLastActionDateTime().compareTo(batch2.getLastActionDateTime()));
			}
			return 0;
		}
	}
	
	private class CrisisManagerBatchTimeslotInfoComparator implements Comparator<CrisisManagerBatchTimeslotInfo> {

		public int compare(CrisisManagerBatchTimeslotInfo t1, CrisisManagerBatchTimeslotInfo t2) {
			if(t1.getArea() != null &&  t2.getArea() != null) {
				return -(t2.getArea().compareTo(t1.getArea()));
			}
			return 0;
		}
	}
	
	public CrisisManagerBatchInfo getOrderCrisisBatchById(String batchId) {
				
		try {
			ICrisisManagerBatch batch = this.crisisManagerService.getCrisisMngBatchById(batchId);			
			batch.setOrder(EnumCrisisMngBatchType.REGULARORDER.equals(batch.getBatchType()) ? 
					this.crisisManagerService.getCrisisMngBatchRegularOrder(batchId, false, false)
						: this.crisisManagerService.getCrisisMngBatchStandingOrder(batchId, false, false));			
			batch.setCancelOrder(this.crisisManagerService.getCancelOrderByArea(batchId, batch.getBatchType()));
			batch.setActiveOrder(this.crisisManagerService.getActiveOrderByArea(batch.getDeliveryDate(), batch.getBatchType()));
			return new CrisisManagerBatchInfo(batch);
			
		} catch (TransAdminServiceException e) {
			LOGGER.error("TransAdmin ServiceException ", e);
		} catch (Exception ex) {
			LOGGER.error("TransAdmin ServiceException ", ex);
		} 
		return null;
	}
	
	public List<CrisisManagerBatchInfo> getOrderCrisisBatch(String deliveryDate) {
		
		List<CrisisManagerBatchInfo> result = new ArrayList<CrisisManagerBatchInfo>();
		try {
		
			Set<ICrisisManagerBatch> lstBatch = null;
			if(TransStringUtil.isEmpty(deliveryDate)) {
				lstBatch = this.crisisManagerService.getCrisisMngBatch(null);
			} else {
				lstBatch = this.crisisManagerService.getCrisisMngBatch(TransStringUtil.getDate(deliveryDate));
			}			
			
			if(lstBatch != null) {
				for(ICrisisManagerBatch batch : lstBatch) {					
					CrisisManagerBatchInfo _info = new CrisisManagerBatchInfo(batch);
					result.add(_info);
				}
			}
		
		} catch (TransAdminServiceException e) {
			e.printStackTrace();
			LOGGER.error("Routing service exception", e);
		} catch (ParseException ex) {
			ex.printStackTrace();
			LOGGER.error("parse exception", ex);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Exception wile retriving batches for source date ", e);
		}
		Collections.sort(result, new OrderCrisisBatchInfoComparator());	
		return result;
		
	}
	
	public boolean doOrderCollectionIn(String orderCrisisBatchId) {
		
		String userId = com.freshdirect.transadmin.security.SecurityManager.getUserName(getHttpServletRequest());		
		ICrisisManagerBatch batch;
		try {
			batch = this.crisisManagerService.getCrisisMngBatchById(orderCrisisBatchId);
			CrisisManagerOrderInAction process = new CrisisManagerOrderInAction(batch, userId, this.crisisManagerService);
			process.execute();
		} catch (TransAdminServiceException e) {
			LOGGER.error("<doOrderCollectionIn:> service exception", e);
		}
		return true;
	}
	
	public boolean doCrisisMngBatchCancel(String orderCrisisBatchId) {
		
		String userId = com.freshdirect.transadmin.security.SecurityManager.getUserName(getHttpServletRequest());		
		ICrisisManagerBatch batch;
		try {
			batch = this.crisisManagerService.getCrisisMngBatchById(orderCrisisBatchId);
			CrisisManagerCancelAction process = new CrisisManagerCancelAction(batch, userId, this.crisisManagerService);
			process.execute();
		} catch (TransAdminServiceException e) {
			LOGGER.error("<doOrderScenarioCancel:> Routing service exception", e);
		}
		
		return true;
	}
	
	public boolean doCrisisMngBatchComplete(String batchId) {
		
		String userId = com.freshdirect.transadmin.security.SecurityManager.getUserName(getHttpServletRequest());		
		ICrisisManagerBatch batch;
		try {
			batch = this.crisisManagerService.getCrisisMngBatchById(batchId);
			CrisisManagerCompleteAction process = new CrisisManagerCompleteAction(batch, userId, this.crisisManagerService);
			process.execute();
		} catch (TransAdminServiceException e) {
			LOGGER.error("<doOrderScenarioComplete:> TransAdmin service exception", e);
		}
		
		return true;
	}
	
	
	public boolean doCancelOrder(String orderCrisisBatchId, String[][] orders){
		
		String userId = com.freshdirect.transadmin.security.SecurityManager.getUserName(getHttpServletRequest());		
		List<String> orderIds = new ArrayList<String>();
		ICrisisManagerBatch batch;
		try {
			batch = this.crisisManagerService.getCrisisMngBatchById(orderCrisisBatchId);
			if(EnumCrisisMngBatchType.STANDINGORDER.equals(batch.getBatchType()) && orders != null){
				for (int i = 0; i < orders.length; i++) {		
					orderIds.add(orders[i][0]);
				}
			} else if(EnumCrisisMngBatchType.REGULARORDER.equals(batch.getBatchType())){
				List<ICrisisMngBatchOrder> cancelOrders = this.crisisManagerService.getCrisisMngBatchRegularOrder(orderCrisisBatchId, false, true);			
				if(cancelOrders != null && cancelOrders.size() > 0){
					Iterator<ICrisisMngBatchOrder> orderItr = cancelOrders.iterator();
					while(orderItr.hasNext()){
						ICrisisMngBatchOrder _order = orderItr.next();
						orderIds.add(_order.getOrderNumber());
					}
				}
			}
			CrisisManagerOrderCancelAction process = new CrisisManagerOrderCancelAction(batch, userId, orderIds, this.crisisManagerService);
			process.execute();
		} catch (TransAdminServiceException e) {
			LOGGER.error("<doOrderCollectionIn:> service exception", e);
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public String doCrisisMngCreateReservation(String batchId, String[][] timeslot){
		
		String userId = com.freshdirect.transadmin.security.SecurityManager.getUserName(getHttpServletRequest());		
		ICrisisManagerBatch batch;
		try {
			batch = this.crisisManagerService.getCrisisMngBatchById(batchId);
			String normalDate = TransStringUtil.getDate(TransStringUtil.getNormalDate());
			List<ICrisisManagerBatchDeliverySlot> exceptionSlots = new ArrayList<ICrisisManagerBatchDeliverySlot>();
			
			if(timeslot != null){
				Map<String, List<ICrisisManagerBatchDeliverySlot>> deliverySlots 
												= this.crisisManagerService.getTimeslotByDate(batch.getDestinationDate());

				for(int i= 0;i < timeslot.length;i++){					
					ICrisisManagerBatchDeliverySlot _slot = new CrisisManagerBatchDeliverySlot();
					exceptionSlots.add(_slot);
					_slot.setBatchId(batchId);
					_slot.setArea(timeslot[i][0]);
					_slot.setStartTime(TransStringUtil.getDatewithTime(normalDate +" "+ timeslot[i][1].substring(0, 8)));
					_slot.setEndTime(TransStringUtil.getDatewithTime(normalDate +" "+ timeslot[i][1].substring(11, 19)));
					_slot.setDestStartTime(TransStringUtil.getDatewithTime(normalDate +" "+ timeslot[i][2].substring(0, 8)));
					_slot.setDestEndTime(TransStringUtil.getDatewithTime(normalDate +" "+ timeslot[i][2].substring(11, 19)));
				
					if(deliverySlots != null && deliverySlots.get(timeslot[i][0]) != null){				
						for(ICrisisManagerBatchDeliverySlot _deliverySlot : deliverySlots.get(timeslot[i][0])){
							if(_slot.getDestStartTime().equals(_deliverySlot.getStartTime())
									&& _slot.getDestEndTime().equals(_deliverySlot.getEndTime())){
								_slot.setTimeSlotId(_deliverySlot.getTimeSlotId());
								break;
							}
						}
					}
				}		
				
				this.crisisManagerService.updateCrisisMngBatchDeliveryslot(exceptionSlots);
			}
			
			CrisisManagerCreateReservationAction process = new CrisisManagerCreateReservationAction(batch, userId, this.crisisManagerService);
			
			Map<String, Integer> exceptions = (Map<String, Integer>) process.execute();
			boolean hasError = (exceptions != null && exceptions.keySet().size() > 0);
			if(hasError) {
				StringBuffer exceptionMessage = new StringBuffer();				
				exceptionMessage.append("\n\n"+"Please handle timeslot exceptions as reservation(s) can't be created. Do you want to continue?");				 
				return exceptionMessage.toString();
			}
		} catch (TransAdminServiceException e) {
			e.printStackTrace();
			LOGGER.error("<doCreateReservation:> Service exception", e);
		} catch (ParseException e) {
			LOGGER.error("parse exception", e);
		}
		return null;
	}
	
	public List<CrisisManagerBatchTimeslotInfo> getTimeslotExceptions(String batchId) {
		
		List<CrisisManagerBatchTimeslotInfo> result = new ArrayList<CrisisManagerBatchTimeslotInfo>();
		
		Map<String, List<ICrisisManagerBatchDeliverySlot>> exceptionSlots 
							= this.crisisManagerService.getCrisisMngBatchTimeslot(batchId, false);
		if(exceptionSlots != null){
			for(Map.Entry<String, List<ICrisisManagerBatchDeliverySlot>> exceptionEntry : exceptionSlots.entrySet()){
				
				for(ICrisisManagerBatchDeliverySlot _slot : exceptionEntry.getValue()){
					result.add(new CrisisManagerBatchTimeslotInfo(_slot));
				}
			}			
		}	
		Collections.sort(result, new CrisisManagerBatchTimeslotInfoComparator());	
		return result;
	}
	
	public List<String> getTimeslotByZone(String batchId, String zone) {
		List<String> r = new ArrayList<String>();
		List<CrisisManagerBatchTimeslotInfo> result = new ArrayList<CrisisManagerBatchTimeslotInfo>();
		
		ICrisisManagerBatch batch;
		batch = this.crisisManagerService.getCrisisMngBatchById(batchId);
		
		Map<String, List<ICrisisManagerBatchDeliverySlot>> timeSlots 
							= this.crisisManagerService.getTimeslotByDate(batch.getDestinationDate());
		if(timeSlots != null && timeSlots.get(zone) != null){				
				for(ICrisisManagerBatchDeliverySlot _slot : timeSlots.get(zone)){
					result.add(new CrisisManagerBatchTimeslotInfo(_slot));
				}
		}	
		Collections.sort(result, new CrisisManagerBatchTimeslotInfoComparator());
		
		if(result != null && result.size() > 0){
			Iterator<CrisisManagerBatchTimeslotInfo> itr = result.iterator();
			while(itr.hasNext()){
				CrisisManagerBatchTimeslotInfo _slot = itr.next();
				r.add(_slot.getTimeslot());
			}
		}
		return r;
	}
	
	public List<CrisisManagerBatchStandingOrderInfo> getStandingOrderByBatchId(String batchId){
				
		List<CrisisManagerBatchStandingOrderInfo> standingOrders = new ArrayList<CrisisManagerBatchStandingOrderInfo>();
		List<ICrisisMngBatchOrder> batchSOList = this.crisisManagerService.getCrisisMngBatchStandingOrder(batchId, false, false);
		Iterator<ICrisisMngBatchOrder> itr =  batchSOList.iterator();
		while(itr.hasNext()){
			ICrisisMngBatchOrder model = itr.next();			
			standingOrders.add(new CrisisManagerBatchStandingOrderInfo(model));
		}
		return standingOrders;
	}
	
	public String placeStandingOrder(String batchId, String[][] _standingOrderData, String[][] timeslot){
		
		String userId = com.freshdirect.transadmin.security.SecurityManager.getUserName(getHttpServletRequest());		
		List<StandingOrderModel> standingOrders = new ArrayList<StandingOrderModel>();
		ICrisisManagerBatch batch;
		
		try {
			batch = this.crisisManagerService.getCrisisMngBatchById(batchId);
			
			String normalDate = TransStringUtil.getDate(TransStringUtil.getNormalDate());
			List<ICrisisManagerBatchDeliverySlot> exceptionSlots = new ArrayList<ICrisisManagerBatchDeliverySlot>();
			
			if(timeslot != null){
				Map<String, List<ICrisisManagerBatchDeliverySlot>> deliverySlots 
												= this.crisisManagerService.getTimeslotByDate(batch.getDestinationDate());

				for(int i= 0;i < timeslot.length;i++){					
					ICrisisManagerBatchDeliverySlot _slot = new CrisisManagerBatchDeliverySlot();
					exceptionSlots.add(_slot);
					_slot.setBatchId(batchId);
					_slot.setArea(timeslot[i][0]);
					_slot.setStartTime(TransStringUtil.getDatewithTime(normalDate +" "+ timeslot[i][1].substring(0, 8)));
					_slot.setEndTime(TransStringUtil.getDatewithTime(normalDate +" "+ timeslot[i][1].substring(11, 19)));
					_slot.setDestStartTime(TransStringUtil.getDatewithTime(normalDate +" "+ timeslot[i][2].substring(0, 8)));
					_slot.setDestEndTime(TransStringUtil.getDatewithTime(normalDate +" "+ timeslot[i][2].substring(11, 19)));
				
					if(deliverySlots != null && deliverySlots.get(timeslot[i][0]) != null){				
						for(ICrisisManagerBatchDeliverySlot _deliverySlot : deliverySlots.get(timeslot[i][0])){
							if(_slot.getDestStartTime().equals(_deliverySlot.getStartTime())
									&& _slot.getDestEndTime().equals(_deliverySlot.getEndTime())){
								_slot.setTimeSlotId(_deliverySlot.getTimeSlotId());
								break;
							}
						}
					}
				}		
				if(exceptionSlots.size() > 0)
					this.crisisManagerService.updateCrisisMngBatchDeliveryslot(exceptionSlots);
			}
			
			String foundException = checkExceptions(batch);
			if(foundException == null && _standingOrderData != null){
				this.crisisManagerService.updateCrisisMngBatchMessage(batch.getBatchId(), INFO_MESSAGE_STANDINGORDEREXPCLEARMSG);
				standingOrders = getStandingOrderModels(batch, _standingOrderData);
				CrisisManagerPlaceOrderAction process = new CrisisManagerPlaceOrderAction(batch, userId
																				, standingOrders, this.crisisManagerService);
				process.execute();
			} else {
				return foundException;
			}
		} catch (TransAdminServiceException e) {
			LOGGER.error("<placeStandingOrder:> service exception", e);
			return null;
		} catch (ParseException e) {
			LOGGER.error("<placeStandingOrder:> Date parse exception", e);			
		} 	
		
		return null;
	}

	private String checkExceptions(ICrisisManagerBatch batch) {
		Map<String, Integer> foundExceptions = new HashMap<String, Integer>();
		
		Map<String, List<ICrisisManagerBatchDeliverySlot>> batchExpSlots 
											= this.crisisManagerService.getCrisisMngBatchTimeslot(batch.getBatchId(), false);
		if (batchExpSlots.size() > 0) {
			for (Map.Entry<String, List<ICrisisManagerBatchDeliverySlot>> exceptionEntry : batchExpSlots.entrySet()) {
				for (ICrisisManagerBatchDeliverySlot _slot : exceptionEntry.getValue()) {
					if (!foundExceptions.containsKey(_slot.getArea())) {
						foundExceptions.put(_slot.getArea(), 0);
					}
					foundExceptions.put(_slot.getArea(), foundExceptions.get(_slot.getArea()).intValue() + 1);
				}
			}
		}

		if (foundExceptions != null && foundExceptions.size() > 0) {
			this.crisisManagerService.updateCrisisMngBatchStatus(batch.getBatchId(), EnumCrisisMngBatchStatus.PLACESOFAILED);
			this.crisisManagerService.updateCrisisMngBatchMessage(batch.getBatchId(), ERROR_MESSAGE_TIMESLOTEXCEPTION);
			boolean hasError = (foundExceptions != null && foundExceptions.keySet().size() > 0);
			if(hasError) {
				StringBuffer exceptionMessage = new StringBuffer();			
				exceptionMessage.append("Please handle timeslot exceptions before you place order(s). Do you want to continue?");				 
				return exceptionMessage.toString();
			}
		}
		return null;
	}
	
	private List<StandingOrderModel> getStandingOrderModels(ICrisisManagerBatch batch, String[][] _standingOrderData){
		
		List<StandingOrderModel> standingOrders = new ArrayList<StandingOrderModel>();
		
		Map<String, ICrisisMngBatchOrder> orderMapping = getBatchOrder(batch.getBatchId());

		Map<String, List<ICrisisManagerBatchDeliverySlot>> batchTimeSlots 
												= this.crisisManagerService.getCrisisMngBatchTimeslot(batch.getBatchId(), true);
		
		if(_standingOrderData != null && _standingOrderData.length > 0){
			List<ICrisisManagerBatchDeliverySlot> areaSlots = null;
			StandingOrderModel _standingOrder = null;
			ICrisisMngBatchOrder _batchOrder = null;
			for(int i= 0;i < _standingOrderData.length;i++){							
				_standingOrder = new StandingOrderModel();
				_standingOrder.setId(_standingOrderData[i][0]);
				_standingOrder.setOrderId(_standingOrderData[i][1]);
				_standingOrder.setAltDate(batch.getDestinationDate());
				_batchOrder = orderMapping.get(_standingOrderData[i][1]);
				
				areaSlots = batchTimeSlots.get(_batchOrder.getArea());
				Iterator<ICrisisManagerBatchDeliverySlot> itr = areaSlots.iterator();
				while (itr.hasNext()) {
					ICrisisManagerBatchDeliverySlot _tempSlot = itr.next();
					if (_batchOrder.getStartTime().equals(_tempSlot.getStartTime())
							&& _batchOrder.getEndTime().equals(_tempSlot.getEndTime()) 
							&& _tempSlot.getDestStartTime() != null && _tempSlot.getDestEndTime() != null) {
						_standingOrder.setStartTime(_tempSlot.getDestStartTime());
						_standingOrder.setEndTime(_tempSlot.getDestEndTime());							
						break;
					}
				}				
				standingOrders.add(_standingOrder);
			}
	    }
		return standingOrders;
	}
	
	private Map<String, ICrisisMngBatchOrder> getBatchOrder(String batchId){
		Map<String, ICrisisMngBatchOrder> orderMapping = new HashMap<String, ICrisisMngBatchOrder>();
		List<ICrisisMngBatchOrder> batchOrders = this.crisisManagerService.getCrisisMngBatchStandingOrder(batchId, false, false);
		if(batchOrders != null && batchOrders.size() > 0){
			Iterator<ICrisisMngBatchOrder> orderItr = batchOrders.iterator();
			while(orderItr.hasNext()){
				ICrisisMngBatchOrder _order = orderItr.next();
				if(!orderMapping.containsKey(_order.getOrderNumber())){
					orderMapping.put(_order.getOrderNumber(), _order);
				}
			}
		}
		
		return orderMapping;
	}
}
