package com.freshdirect.transadmin.datamanager.report.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.freshdirect.transadmin.model.ICrisisManagerBatch;
import com.freshdirect.transadmin.model.ICrisisManagerBatchDeliverySlot;
import com.freshdirect.transadmin.datamanager.model.ICancelOrderInfo;

@SuppressWarnings("serial")
public class CrisisManagerReportData implements  Serializable {
	
	private ICrisisManagerBatch batch;
	private Map<String, List<ICancelOrderInfo>> orderMapping;
	private List<ICancelOrderInfo> orders;
	private List<ICrisisManagerBatchDeliverySlot> timeslots;
	
	public ICrisisManagerBatch getBatch() {
		return batch;
	}
	public void setBatch(ICrisisManagerBatch batch) {
		this.batch = batch;
	}
	
	public Map<String, List<ICancelOrderInfo>> getOrderMapping() {
		return orderMapping;
	}
	public void setOrderMapping(Map<String, List<ICancelOrderInfo>> orderMapping) {
		this.orderMapping = orderMapping;
	}
	
	public List<ICancelOrderInfo> getOrders() {
		return orders;
	}
	public void setOrders(List<ICancelOrderInfo> orders) {
		this.orders = orders;
	}
	public List<ICrisisManagerBatchDeliverySlot> getTimeslots() {
		Collections.sort(timeslots, new DeliverySlotComparator());
		return timeslots;
	}
	public void setTimeslots(List<ICrisisManagerBatchDeliverySlot> timeslots) {
		this.timeslots = timeslots;
	}
	
	private class DeliverySlotComparator implements Comparator<ICrisisManagerBatchDeliverySlot> {

		public int compare(ICrisisManagerBatchDeliverySlot slot1, ICrisisManagerBatchDeliverySlot slot2) {
			if(slot1.getArea()!= null &&  slot2.getArea() != null) {
				int areaCmp =  (slot1.getArea().compareTo(slot2.getArea()));
				if(areaCmp != 0) 
					return areaCmp;
				return (slot1.getStartTime().compareTo(slot2.getStartTime()));
			}
			return 0;
		}
	}
	
	public String toString() {
		return orderMapping.toString();
	}
}
