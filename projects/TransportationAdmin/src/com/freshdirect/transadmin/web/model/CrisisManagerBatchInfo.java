package com.freshdirect.transadmin.web.model;

import java.text.ParseException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.freshdirect.routing.model.ICrisisMngBatchOrder;
import com.freshdirect.transadmin.model.IActiveOrderModel;
import com.freshdirect.transadmin.model.ICancelOrderModel;
import com.freshdirect.transadmin.model.ICrisisManagerBatch;
import com.freshdirect.transadmin.model.ICrisisManagerBatchAction;
import com.freshdirect.transadmin.util.TransStringUtil;

public class CrisisManagerBatchInfo implements java.io.Serializable {
	
	private static final long serialVersionUID = 8143971804213734098L;

	private final ICrisisManagerBatch batch;	
	
	private List<CrisisManagerBatchOrderInfo> order;
	private List<CrisisManagerBatchCancelOrderInfo> cancelOrder;
	private List<CrisisManagerBatchActiveOrderInfo> activeOrder;
	
	public CrisisManagerBatchInfo(ICrisisManagerBatch batch) {
		this.batch = batch;		
			
		order = new ArrayList<CrisisManagerBatchOrderInfo>();
		cancelOrder = new ArrayList<CrisisManagerBatchCancelOrderInfo>();
		activeOrder = new ArrayList<CrisisManagerBatchActiveOrderInfo>();
		if(this.batch.getOrder() != null) {
			for(ICrisisMngBatchOrder _orderInfo : this.batch.getOrder()) {
				order.add(new CrisisManagerBatchOrderInfo(_orderInfo));
			}
		}
		if(this.batch.getCancelOrder() != null) {
			for(ICancelOrderModel _orderInfo : this.batch.getCancelOrder()) {
				cancelOrder.add(new CrisisManagerBatchCancelOrderInfo(_orderInfo));
			}
		}
		if(this.batch.getActiveOrder() != null) {
			for(IActiveOrderModel _orderInfo : this.batch.getActiveOrder()) {
				activeOrder.add(new CrisisManagerBatchActiveOrderInfo(_orderInfo));
			}
		}
	}
	
	public String getProcessId() {
		return batch.getBatchId();
	}
	
	public String getDateInfo() {
		StringBuffer strBuf = new StringBuffer();
		try {			
			strBuf.append("<b>Source Date: </b>"+ TransStringUtil.getDate(batch.getDeliveryDate())).append("<br/>");
			strBuf.append("<b>Destination Date: </b>"+ TransStringUtil.getDate(batch.getDestinationDate())).append("<br/>");				
		} catch (ParseException e) {
			return null;
		}
		return strBuf.toString();
	}
	public String getBatchType() {
		return batch.getBatchType().value();
	}
	public String getCriteriaInfo() {
		StringBuffer strBuf = new StringBuffer();
		try {
			if(batch.getCutOffDateTime() != null) {
				strBuf.append("Cutoff Time: <b>"+TransStringUtil.getServerTime(batch.getCutOffDateTime())).append("</b><br/>");
			}else{
				strBuf.append("Cutoff Time: <b>ALL</b><br/>");
			}
			if(batch.getArea() != null && batch.getArea().length > 0){
				strBuf.append("Zone: (");
				for (int i = 0; i < batch.getArea().length; i++) {
					if (i > 0) {
						strBuf.append(',');
					}
					strBuf.append(batch.getArea()[i]);
				}
				strBuf.append(")<br/>");
			}else {
				strBuf.append("Zone: <b>ALL</b><br/>");
			}
			if(batch.getStartTime() != null) {
				strBuf.append("Start Time: "+TransStringUtil.getServerTime(batch.getStartTime())).append("<br/>");
			}
			if(batch.getEndTime() != null) {
				strBuf.append("End Time: "+TransStringUtil.getServerTime(batch.getEndTime())).append("<br/>");
			}			
			if(batch.getDeliveryType() != null && batch.getDeliveryType().length > 0){
				strBuf.append("Delivery Type: (");
				for (int i = 0; i < batch.getDeliveryType().length; i++) {
					if (i > 0) {
						strBuf.append(',');
					}
					strBuf.append(batch.getDeliveryType()[i]);
				}
				strBuf.append(')');
			}
		} catch (ParseException e) {
			return null;
		}
		return strBuf.toString();
	}
	
	public String getCreationInfo() {
		
		StringBuffer strBuf = new StringBuffer();
		try {
			ICrisisManagerBatchAction action = batch.getFirstAction();
			if(action != null) {
				strBuf.append(TransStringUtil.getDatewithTime(action.getActionDateTime()));
				strBuf.append("<br/>");
				
				strBuf.append(batch.isEligibleForCancel() ? "<img src=\"./images/greendot.gif\" border=\"0\" />" 
						: "<img src=\"./images/reddot.gif\" border=\"0\" />");
				
				strBuf.append(action.getActionBy());
				strBuf.append("<br/>");
				if(batch.getActiveOrder() != null){
					strBuf.append("Active Orders: <b>").append(batch.getActiveOrder().size()).append("</b><br/>");
				}
				strBuf.append("Total Orders: <b>").append(batch.getNoOfOrders()).append("</b><br/>");
				strBuf.append("<a style=\"color:#8B2252;font-size: 10px;\" href='javascript:showOrderInfoTable("+batch.getBatchId()+");'>More Details</a>").append("<br/>");
				strBuf.append("");
				//strBuf.append("Order Cancelled: <b>").append(batch.getNoOfOrdersCancelled()).append("</b><br/>");
				//strBuf.append("Rsv Count: <b>").append(batch.getNoOfReservations()).append("</b><br/>");
				//strBuf.append("Rsv Cancelled: <b>").append(batch.getNoOfReservationsCancelled()).append("</b>");
				strBuf.append("<br/>");												
			}
		} catch (ParseException e) {
			return null;
		}
		return strBuf.toString();
	}
	
	public String getLastUpdateInfo() {
		
		StringBuffer strBuf = new StringBuffer();
		try {
			ICrisisManagerBatchAction action = batch.getLastAction();
			if(action != null) {
				strBuf.append(TransStringUtil.getDatewithTime(action.getActionDateTime()));
				strBuf.append("<br/>");
				strBuf.append(action.getActionBy());	
				strBuf.append("<br/>");
				strBuf.append("<b>No Of Orders: ").append(batch.getNoOfOrders()).append("</b>");
			}
		} catch (ParseException e) {
			return null;
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
		ICrisisManagerBatchAction action = batch.getLastAction();
		if(action != null) {
			return action.getActionDateTime();
		} else {
			return null;
		}
	}
	
	public List getOrder() {
		return order;
	}

	public List<CrisisManagerBatchCancelOrderInfo> getCancelOrder() {
		return cancelOrder;
	}

	public List<CrisisManagerBatchActiveOrderInfo> getActiveOrder() {
		return activeOrder;
	}
	
}

