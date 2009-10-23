package com.freshdirect.transadmin.datamanager.report.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class CutOffReportData implements  Serializable {
	private String cutOff;
	private TreeMap reportData;
	private TreeMap summaryData;
	private TreeMap tripReportData;
	
	private TreeMap detailData;
	
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
	
	public String toString() {
		return cutOff.toString()+"->"+reportData+"->"+detailData;
	}
	
	
}
