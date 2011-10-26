package com.freshdirect.transadmin.web.model;

import java.text.ParseException;

import com.freshdirect.transadmin.model.ICancelOrderModel;
import com.freshdirect.transadmin.util.TransStringUtil;

public class CrisisManagerBatchCancelOrderInfo implements java.io.Serializable {
	
	private static final long serialVersionUID = -1018415078278693692L;
	
	private ICancelOrderModel batchOrder;
		
	public CrisisManagerBatchCancelOrderInfo(ICancelOrderModel batchOrder) {
		super();
		this.batchOrder = batchOrder;
	}
	
	public String getArea(){
		return this.batchOrder.getArea();
	}
	
	public String getStartTime(){
		try {
			return TransStringUtil.getServerTime(batchOrder.getStartTime());
		} catch (ParseException e) {
			return null;
		}	
	}

	public String getEndTime(){
		try {
			return TransStringUtil.getServerTime(batchOrder.getEndTime());
		} catch (ParseException e) {
			return null;
		}		
	}

	public int getOrderCount(){
		return this.batchOrder.getOrderCount();
	}

	public int getReservationCount(){
		return this.batchOrder.getReservationCount();
	}


	
}
