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

import com.freshdirect.routing.model.IHandOffBatch;
import com.freshdirect.routing.model.IHandOffBatchRoute;
import com.freshdirect.routing.model.IWaveInstance;
import com.freshdirect.routing.util.RoutingTimeOfDay;
import com.freshdirect.transadmin.datamanager.model.OrderRouteInfoModel;

public class CutOffReportData implements  Serializable {
	private String cutOff;
	private TreeMap reportData;
	private TreeMap summaryData;
	private TreeMap tripReportData;
	
	private TreeMap detailData;
	
	private Map<String, IHandOffBatchRoute> routeMapping;
	
	private IHandOffBatch batch;
	
	private Map<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>> plannedDispatchTree = null;
		
	public IHandOffBatch getBatch() {
		return batch;
	}
	public void setBatch(IHandOffBatch batch) {
		this.batch = batch;
	}
	public Map<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>> getPlannedDispatchTree() {
		return plannedDispatchTree;
	}
	
	public void setPlannedDispatchTree(
			Map<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>> plannedDispatchTree) {
		this.plannedDispatchTree = plannedDispatchTree;
	}
	public Map<String, IHandOffBatchRoute> getRouteMapping() {
		return routeMapping;
	}
	public void setRouteMapping(Map<String, IHandOffBatchRoute> routeMapping) {
		this.routeMapping = routeMapping;
	}
	
	public TreeMap getDetailData() {
		return detailData;
	}
	public void setDetailData(TreeMap detailData) {
		this.detailData = detailData;
	}
	public String getCutOff() {
		return cutOff;
	}
	public void setCutOff(String cutOff) {
		this.cutOff = cutOff;
	}
	public TreeMap getReportData() {
		return reportData;
	}
	public void setReportData(TreeMap reportData) {
		this.reportData = reportData;
	}
	
	public TreeMap getTripReportData() {
		return tripReportData;
	}
	public void setTripReportData(TreeMap tripReportData) {
		this.tripReportData = tripReportData;
	}
	public TreeMap getSummaryData() {
		return summaryData;
	}
	public void setSummaryData(TreeMap summaryData) {
		this.summaryData = summaryData;
	}
	
	public void putReportData(Object key , Object value) {
		if(!reportData.containsKey(key)) {
			reportData.put(key, new ArrayList());
		}
		((List)reportData.get(key)).add(value);
	}
	
	public void putSummaryData(Object key , Object value) {
		if(!summaryData.containsKey(key)) {
			summaryData.put(key, new ArrayList());
		}
		((List)summaryData.get(key)).add(value);
	}
	
	public void putDetailData(Object key , Object value) {
		if(!detailData.containsKey(key)) {
			detailData.put(key, new ArrayList());
		}
		((List)detailData.get(key)).add(value);
	}
	
	public void putTripReportData(Object key , Object value) {
		if(!tripReportData.containsKey(key)) {
			tripReportData.put(key, new ArrayList());
		}
		((List)tripReportData.get(key)).add(value);
	}
	
	public List<String> getSummaryKeys() {
		Set summaryKeys = getSummaryData().keySet();
		List<String> sortedKeys = new ArrayList<String>();
		sortedKeys.addAll(summaryKeys);
		Collections.sort(sortedKeys, new DispatchComparator(getSummaryData()));
		return sortedKeys;
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
		return cutOff.toString()+"->"+reportData+"->"+detailData;
	}
	
	
}
