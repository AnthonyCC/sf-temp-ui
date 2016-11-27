package com.freshdirect.webapp.taglib.callcenter;

public class ComplaintDeptInfo {
	Double total = null;
	String label = null;

	public ComplaintDeptInfo() {
		this.total = new Double(0.0);
		this.label = "";
	}

	public Double getTotal() { return this.total; }
	public void setTotal(Double d) { this.total = d; }
	public String getLabel() { return this.label; }
	public void setLabel(String s) { this.label = s; }
}