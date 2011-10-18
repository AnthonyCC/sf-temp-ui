package com.freshdirect.transadmin.datamanager.report.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.freshdirect.routing.model.ICrisisManagerBatch;
import com.freshdirect.routing.model.IHandOffBatch;
import com.freshdirect.routing.model.IHandOffBatchRoute;
import com.freshdirect.routing.model.IHandOffBatchTrailer;
import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.model.IWaveInstance;
import com.freshdirect.routing.util.RoutingTimeOfDay;
import com.freshdirect.transadmin.datamanager.model.ICancelOrderInfo;
import com.freshdirect.transadmin.datamanager.model.OrderRouteInfoModel;

public class CrisisManagerReportData implements  Serializable {
	
	private ICrisisManagerBatch batch;
	private Map<String, List<ICancelOrderInfo>> orderMapping;
	private List<ICancelOrderInfo> regularOrders;
	private List<ICancelOrderInfo> standingOrders;
			
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
	
	public List<ICancelOrderInfo> getRegularOrders() {
		return regularOrders;
	}
	public void setRegularOrders(List<ICancelOrderInfo> regularOrders) {
		this.regularOrders = regularOrders;
	}
	public List<ICancelOrderInfo> getStandingOrders() {
		return standingOrders;
	}
	public void setStandingOrders(List<ICancelOrderInfo> standingOrders) {
		this.standingOrders = standingOrders;
	}



	private class DispatchComparator implements Comparator<String> {		
		TreeMap summaryData;
		
		public DispatchComparator(TreeMap summaryData) {
			super();
			this.summaryData = summaryData;
		}

		public int compare(String route1, String route2) {
						
			if(summaryData != null && route1 != null && route2 != null) {
				List _orders1 = 	(List)summaryData.get(route1); 
				List _orders2 = 	(List)summaryData.get(route2);
				Date dispatchTime1 = null;
				int dispatchSequence1 = 0;
				Date dispatchTime2 = null;
				int dispatchSequence2 = 0;
				if(_orders1 != null && _orders1.size() > 0) {
					dispatchTime1 = ((OrderRouteInfoModel)_orders1.get(0)).getDispatchTime();
	        		dispatchSequence1 = ((OrderRouteInfoModel)_orders1.get(0)).getDispatchSequence();
		        }
				if(_orders2 != null && _orders2.size() > 0) {
					dispatchTime2 = ((OrderRouteInfoModel)_orders2.get(0)).getDispatchTime();
	        		dispatchSequence2 = ((OrderRouteInfoModel)_orders2.get(0)).getDispatchSequence();
		        }
				if(dispatchTime1 != null && dispatchTime2 != null) {
					int dateCmp = dispatchTime1.compareTo(dispatchTime2);
			        if (dateCmp != 0) {
			            return dateCmp;
			        }
			        return (dispatchSequence1 < dispatchSequence2 ? -1 :
			                (dispatchSequence1 == dispatchSequence2 ? 0 : 1));
				}

			}						
			return -1;
		}		
	}
	
	public String toString() {
		return orderMapping.toString();
	}
	
	
}
