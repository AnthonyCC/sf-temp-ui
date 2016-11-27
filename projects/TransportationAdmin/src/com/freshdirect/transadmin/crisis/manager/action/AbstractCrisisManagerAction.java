package com.freshdirect.transadmin.crisis.manager.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.routing.model.GenericSearchModel;
import com.freshdirect.routing.model.IGenericSearchModel;
import com.freshdirect.routing.util.RoutingDateUtil;
import com.freshdirect.transadmin.constants.EnumCrisisMngBatchStatus;
import com.freshdirect.transadmin.model.ICrisisManagerBatch;
import com.freshdirect.transadmin.model.ICrisisManagerBatchDeliverySlot;
import com.freshdirect.transadmin.service.ICrisisManagerService;
import com.freshdirect.transadmin.service.exception.IIssue;
import com.freshdirect.transadmin.service.exception.TransAdminServiceException;
import com.freshdirect.transadmin.util.CrisisManagerUtil;
import com.freshdirect.transadmin.util.TransStringUtil;

public abstract class AbstractCrisisManagerAction {
	
	private final static Logger LOGGER = LoggerFactory.getInstance(AbstractCrisisManagerAction.class);
	
	private ICrisisManagerBatch batch;
	
	private String userId;
	
	private ICrisisManagerService  crisisMngService;
		
	public AbstractCrisisManagerAction(ICrisisManagerBatch batch, String userId, ICrisisManagerService  crisisManagerService) {
		super();
		this.batch = batch;
		this.userId = userId;
		this.crisisMngService = crisisManagerService;
	}

	public ICrisisManagerBatch getBatch() {
		return batch;
	}

	public void setBatch(ICrisisManagerBatch batch) {
		this.batch = batch;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public ICrisisManagerService getCrisisMngService() {
		return crisisMngService;
	}
	
	public void setCrisisManagerService(ICrisisManagerService crisisMngService) {
		this.crisisMngService = crisisMngService;
	}

	public Object execute() {
		long startTime = System.currentTimeMillis();
		try {
			Object result = doExecute();
			long endTime = System.currentTimeMillis();
			LOGGER.info("CrisisManagerAction "+this.getClass().getName()+" completed in"+ ((endTime - startTime)/60) +" secs");
			return result;
		} catch (Exception exp) {
					
			try {
				if(getFailureStatus() != null) {
					
					this.crisisMngService.updateCrisisMngBatchStatus(this.getBatch().getBatchId(), getFailureStatus());
					this.crisisMngService.updateCrisisMngBatchMessage(this.getBatch().getBatchId(), decodeErrorMessage(exp));
				}
			} catch (TransAdminServiceException e) {
				LOGGER.error("Failure to update CrisisMng batch status", e);
			}

			throw new TransAdminServiceException(exp, IIssue.PROCESS_CRISISMNGBATCH_ERROR);
		} catch (Throwable e) {
			LOGGER.error("something really weird occured", e);
			throw new TransAdminServiceException(new Exception(e), IIssue.PROCESS_CRISISMNGBATCH_ERROR);
		}
	}
	
	protected static String decodeErrorMessage(Exception exp) {
		String strErrMessage = exp.getMessage() != null ? exp.getMessage() : "";
		if(strErrMessage == null || strErrMessage.trim().length() == 0) {
			if(exp instanceof TransAdminServiceException) {
				strErrMessage = ((TransAdminServiceException)exp).getIssueMessage();
			}
		}
		if(strErrMessage == null || strErrMessage.trim().length() == 0) {
			strErrMessage = exp.toString();
		}
		return strErrMessage;
	}
	
	protected static String[] getArray(String values){
		StringTokenizer tokenizer = new StringTokenizer(values, ",");
		String[] valueArray = new String[tokenizer.countTokens()];
		int index = 0;
		while(tokenizer.hasMoreTokens()){
			String token = tokenizer.nextToken();
			valueArray[index] = token.trim();
			index++;
		}
		return valueArray;
	}
	
	protected void checkDeliverySlotExceptions() throws TransAdminServiceException {
		
		Map<String, List<ICrisisManagerBatchDeliverySlot>> batchTimeSlots 
											= this.crisisMngService.getCrisisMngBatchTimeslotByZone(this.getBatch().getBatchId(), this.getBatch().getBatchType());
		
		Map<String, List<ICrisisManagerBatchDeliverySlot>> destinationTimeSlots 
											= this.crisisMngService.getTimeslotByDate(this.getBatch().getDestinationDate());
		if(batchTimeSlots != null && destinationTimeSlots != null){
			List<ICrisisManagerBatchDeliverySlot> destAreaSlots = null;
			for(Map.Entry<String, List<ICrisisManagerBatchDeliverySlot>> slotEntry : batchTimeSlots.entrySet()){
				destAreaSlots = destinationTimeSlots.get(slotEntry.getKey());
				for(ICrisisManagerBatchDeliverySlot _slot : slotEntry.getValue()){
					if(destAreaSlots != null)
						matchTimeslot(_slot, destAreaSlots);
					else
						break;
				}
			}
			this.crisisMngService.addCrisisMngBatchDeliveryslot(batchTimeSlots);
		}		
	}	
	
	protected boolean matchTimeslot(ICrisisManagerBatchDeliverySlot _slot
												, List<ICrisisManagerBatchDeliverySlot> destAreaSlots) {

		Iterator<ICrisisManagerBatchDeliverySlot> itr = destAreaSlots.iterator();
		while (itr.hasNext()) {
			ICrisisManagerBatchDeliverySlot _tempSlot = itr.next();

			if (_slot.getStartTime().equals(_tempSlot.getStartTime())
					&& _slot.getEndTime().equals(_tempSlot.getEndTime())) {
				_slot.setDestStartTime(_tempSlot.getStartTime());
				_slot.setDestEndTime(_tempSlot.getEndTime());
				_slot.setTimeSlotId(_tempSlot.getTimeSlotId());
				return true;
			}
		}
		return false;
	}
	
	protected String formatMessages(List<String> messages) {
		
		StringBuffer strBuf = new StringBuffer();
		if(messages != null) {
			for(String message : messages) {
				strBuf.append(message).append("<br/> ");
			}
		}
		return strBuf.toString();
	}
	
	protected String formatMessages1(List<String> messages) {
		
		StringBuffer strBuf = new StringBuffer();
		if(messages != null) {
			for(String message : messages) {
				strBuf.append(message).append(", ");
			}
		}
		return strBuf.toString();
	}
	
	protected void blockDeliveryCapacity() throws Exception {
		/*Block Capacity for source & destination dates*/
     	CrisisManagerUtil.doBlockCapacity(getDeliveryDates(false), this.getUserId());
	}
	
	protected void unBlockDeliveryCapacity(boolean filterException) throws Exception {
		/*Un-Block Capacity for destination date*/
    	CrisisManagerUtil.doUnBlockCapacity(getDeliveryDates(filterException), this.getUserId());
	}
	
	private List<IGenericSearchModel> getDeliveryDates(boolean filterException) throws ParseException {
		List<IGenericSearchModel> models = new ArrayList<IGenericSearchModel>();    	
		Date startTime = null; Date endTime = null;
		if(this.getBatch().getStartTime()!= null && this.getBatch().getEndTime() != null){
		 startTime = TransStringUtil.getDatewithTime(TransStringUtil.getDate(TransStringUtil.getNormalDate())
				 +" "+ RoutingDateUtil.getServerTime(this.getBatch().getStartTime()));
		 endTime = TransStringUtil.getDatewithTime(TransStringUtil.getDate(TransStringUtil.getNormalDate())
				 +" "+ RoutingDateUtil.getServerTime(this.getBatch().getEndTime()));
		}
		if(!filterException){
			IGenericSearchModel sourceModel = new GenericSearchModel();
	    	sourceModel.setSourceDate(this.getBatch().getDeliveryDate());
	    	sourceModel.setCutOffDate(this.getBatch().getCutOffDateTime());
	    	sourceModel.setArea(this.getBatch().getArea());
	    	sourceModel.setStartTime(startTime);
	    	sourceModel.setEndTime(endTime);
	    	models.add(sourceModel);
		}
	    IGenericSearchModel destModel = new GenericSearchModel();
	    destModel.setSourceDate(this.getBatch().getDestinationDate());
	    destModel.setCutOffDate(this.getBatch().getCutOffDateTime());
	    destModel.setArea(this.getBatch().getArea());
	    destModel.setStartTime(startTime);
	    destModel.setEndTime(endTime);
	    models.add(destModel);
    	
		return models;
	}
	
	public abstract Object doExecute() throws Exception;
	
	public abstract EnumCrisisMngBatchStatus getFailureStatus();
}
