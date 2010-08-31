package com.freshdirect.transadmin.model;

import java.util.Date;

public class ScribLabel {
	private String scribLabelId;
	private Date date;
	private String scribLabel;
	
	public String getScribLabelId() {
		return scribLabelId;
	}
	public ScribLabel(Date date, String scribLabel) {
		super();
		this.date = date;
		this.scribLabel = scribLabel;
	}
	public void setScribLabelId(String scribLabelId) {
		this.scribLabelId = scribLabelId;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getScribLabel() {
		return scribLabel;
	}
	public void setScribLabel(String scribLabel) {
		this.scribLabel = scribLabel;
	}
	public ScribLabel() {
		
	}

}
