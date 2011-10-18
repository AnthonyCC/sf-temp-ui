package com.freshdirect.transadmin.crisis.manager.action;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.routing.constants.EnumCrisisMngBatchStatus;
import com.freshdirect.routing.model.ICrisisManagerBatch;
import com.freshdirect.routing.model.ICrisisManagerBatchDeliverySlot;
import com.freshdirect.routing.service.exception.IIssue;
import com.freshdirect.routing.service.exception.RoutingProcessException;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.CrisisManagerServiceProxy;

public abstract class AbstractCrisisManagerAction {
	private final static Logger LOGGER = LoggerFactory.getInstance(AbstractCrisisManagerAction.class);
	
	private ICrisisManagerBatch batch;
	
	private String userId;
	
	private NumberFormat formatter = new DecimalFormat("00");
	
	public AbstractCrisisManagerAction(ICrisisManagerBatch batch, String userId) {
		super();
		this.batch = batch;
		this.userId = userId;
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

	public Object execute() {
		long startTime = System.currentTimeMillis();
		try {
			Object result = doExecute();
			long endTime = System.currentTimeMillis();
			LOGGER.info("CrisisManagerAction "+this.getClass().getName()+" completed in"+ ((endTime - startTime)/60) +" secs");
			return result;
		} catch (Exception exp) {
			CrisisManagerServiceProxy proxy = new CrisisManagerServiceProxy();
			
			try {
				if(getFailureStatus() != null) {
					
					proxy.updateCrisisMngBatchStatus(this.getBatch().getBatchId(), getFailureStatus());
					proxy.updateCrisisMngBatchMessage(this.getBatch().getBatchId(), decodeErrorMessage(exp));
				}
			} catch (RoutingServiceException e) {
				LOGGER.error("Failure to update OrderScenario batch status", e);
			}

			throw new RoutingServiceException(exp, IIssue.PROCESS_CRISISMNGBATCH_ERROR);
		} catch (Throwable e) {
			LOGGER.error("something really weird occured", e);
			throw new RoutingServiceException(new Exception(e), IIssue.PROCESS_CRISISMNGBATCH_ERROR);
		}
	}
	
	protected static String decodeErrorMessage(Exception exp) {
		String strErrMessage = exp.getMessage() != null ? exp.getMessage() : "";
		if(strErrMessage == null || strErrMessage.trim().length() == 0) {
			if(exp instanceof RoutingProcessException) {
				strErrMessage = ((RoutingProcessException)exp).getIssueMessage();
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

	
	public abstract Object doExecute() throws Exception;
	
	public abstract EnumCrisisMngBatchStatus getFailureStatus();
}
