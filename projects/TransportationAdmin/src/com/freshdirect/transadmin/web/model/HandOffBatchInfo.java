package com.freshdirect.transadmin.web.model;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.freshdirect.routing.model.IHandOffBatch;
import com.freshdirect.routing.model.IHandOffBatchAction;
import com.freshdirect.routing.model.IHandOffBatchDepotSchedule;
import com.freshdirect.routing.model.IHandOffBatchSession;
import com.freshdirect.transadmin.util.TransStringUtil;

public class HandOffBatchInfo implements java.io.Serializable {
	
	private final IHandOffBatch batch;
	
	private List<HandOffBatchDepotScheduleInfo> depotSchedule;
	
	public HandOffBatchInfo(IHandOffBatch batch) {
		this.batch = batch;
		depotSchedule = new ArrayList<HandOffBatchDepotScheduleInfo>();
		if(this.batch.getDepotSchedule() != null) {
			for(IHandOffBatchDepotSchedule _schInfo : this.batch.getDepotSchedule()) {
				depotSchedule.add(new HandOffBatchDepotScheduleInfo(_schInfo));
			}
		}
	}
	
	public String getBatchId() {
		return batch.getBatchId();
	}
	
	public String getDeliveryDate() {
		try {
			return TransStringUtil.getDate(batch.getDeliveryDate());
		} catch (ParseException e) {
			return null;
		}
	}
	
	public String getCreationInfo() {
		
		StringBuffer strBuf = new StringBuffer();
		try {
			IHandOffBatchAction action = batch.getFirstAction();
			if(action != null) {
				strBuf.append(TransStringUtil.getDatewithTime(action.getActionDateTime()));
				strBuf.append("\n");
				strBuf.append(action.getActionBy());			
			}
		} catch (ParseException e) {
			return null;
		}
		return strBuf.toString();
	}
	
	public String getLastUpdateInfo() {
		
		StringBuffer strBuf = new StringBuffer();
		try {
			IHandOffBatchAction action = batch.getLastAction();
			if(action != null) {
				strBuf.append(TransStringUtil.getDatewithTime(action.getActionDateTime()));
				strBuf.append("\n");
				strBuf.append(action.getActionBy());			
			}
		} catch (ParseException e) {
			return null;
		}
		return strBuf.toString();
	}
	
	public String getSessionInfo() {
		
		StringBuffer strBuf = new StringBuffer();
		
		for(IHandOffBatchSession session : batch.getSession()) {
			if(strBuf.toString().length() > 0) {
				strBuf.append("\n");				
			} 
			strBuf.append(session.getRegion()).append("[").append(session.getSessionName()).append("]");
		}
		return strBuf.toString();
	}
	
	public String getStatus() {
		return batch.getStatus().value();
	}
	
	public String getSystemMessage() {
		return batch.getSystemMessage();
	}
	
	public Date getLastActionDateTime() {
		IHandOffBatchAction action = batch.getLastAction();
		if(action != null) {
			return action.getActionDateTime();
		} else {
			return null;
		}
	}
	
	public List getDepotSchedule() {
		return depotSchedule;
	}
	
}

