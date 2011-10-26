package com.freshdirect.transadmin.web.model;

import java.text.ParseException;

import com.freshdirect.transadmin.model.IActiveOrderModel;
import com.freshdirect.transadmin.util.TransStringUtil;

public class CrisisManagerBatchActiveOrderInfo implements java.io.Serializable {
	
	private static final long serialVersionUID = 1430425791125990598L;
	
	private IActiveOrderModel batchOrder;
		
	public CrisisManagerBatchActiveOrderInfo(IActiveOrderModel batchOrder) {
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
	
}
