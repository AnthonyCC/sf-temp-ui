package com.freshdirect.transadmin.web.model;

import java.text.ParseException;

import com.freshdirect.transadmin.model.ICrisisManagerBatchDeliverySlot;
import com.freshdirect.transadmin.util.TransStringUtil;

public class CrisisManagerBatchTimeslotInfo implements java.io.Serializable {
	
	private final ICrisisManagerBatchDeliverySlot batchTimeslot;
	
	public CrisisManagerBatchTimeslotInfo(ICrisisManagerBatchDeliverySlot batchOrder) {
		super();
		this.batchTimeslot = batchOrder;
	}

	public String getArea(){
		return this.batchTimeslot.getArea();
	}	

	public String getTimeslot(){
		try {
			return TransStringUtil.getServerTime(batchTimeslot.getStartTime())+" - "+ TransStringUtil.getServerTime(batchTimeslot.getEndTime());
		} catch (ParseException e) {
			return null;
		}	
	}

	public String getNewTimeslot(){
		try {
			return TransStringUtil.getServerTime(batchTimeslot.getDestStartTime())+" - "+ TransStringUtil.getServerTime(batchTimeslot.getDestEndTime());
		} catch (ParseException e) {
			return null;
		}		
	}	
}
