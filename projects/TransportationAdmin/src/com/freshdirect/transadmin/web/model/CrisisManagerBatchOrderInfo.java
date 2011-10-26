package com.freshdirect.transadmin.web.model;

import java.text.ParseException;
import java.util.Date;

import com.freshdirect.transadmin.model.ICrisisManagerBatchOrder;
import com.freshdirect.transadmin.util.TransStringUtil;

public class CrisisManagerBatchOrderInfo implements java.io.Serializable {
	
	private static final long serialVersionUID = 1430425791125990598L;
	
	private ICrisisManagerBatchOrder batchOrder;
		
	public CrisisManagerBatchOrderInfo(ICrisisManagerBatchOrder batchOrder) {
		super();
		this.batchOrder = batchOrder;
	}
	
	public String getArea(){
		return this.batchOrder.getArea();
	}	

	public String getFirstName(){
		return this.batchOrder.getFirstName();
	}	

	public String getLastName(){
		return this.batchOrder.getLastName();
	}
	
	public String getErpCustomerId(){
		return this.batchOrder.getErpCustomerPK();
	}
	
	public String getFDCustomerId(){
		return this.batchOrder.getFdCustomerPK();
	}

	public Date getDeliveryDate(){
		return this.batchOrder.getDeliveryDate();
	}	

	public String getCutOffTime(){
		try {
			return TransStringUtil.getServerTime(batchOrder.getCutOffTime());
		} catch (ParseException e) {
			return null;
		}
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

	public String getOrderNumber(){
		return this.batchOrder.getOrderNumber();
	}

	public String getErpOrderNumber(){
		return this.batchOrder.getErpOrderNumber();
	}

	public String getReservationInfo(){
		return this.batchOrder.getReservationId() != null ? "Y" : "N";
	}
	
}
