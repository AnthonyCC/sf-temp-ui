package com.freshdirect.transadmin.datamanager.report.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class CutOffReportData implements  Serializable {
	private String cutOff;
	private TreeMap reportData;
	
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
	
	public void putReportData(Object key , Object value) {
		if(!reportData.containsKey(key)) {
			reportData.put(key, new ArrayList());
		}
		((List)reportData.get(key)).add(value);
	}
	public String toString() {
		return cutOff.toString()+"->"+reportData;
	}
	
}
