package com.freshdirect.cms.ui.model;

import java.io.Serializable;

public class SummaryData implements Serializable, Comparable<SummaryData> {
	
	private static final long serialVersionUID = -1593927596289183883L;

	private String key;		

	private int value;
	
	public SummaryData() {
		key = "";
		value = 0;
	}
	
	public SummaryData(String k, int v) {
		key = k;
		value = v;			
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public int compareTo(SummaryData o) {
		if (o.getValue() < getValue()) return -1;
		if (o.getValue() > getValue()) return 1;
		return 0;
	}
	
}