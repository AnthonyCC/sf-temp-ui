package com.freshdirect.fdstore.standingorders;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

public class FDStandingOrderAltDeliveryDate implements Serializable{

	private static final long serialVersionUID = -3087295029694549320L;
	private Date origDate;
	private Date altDate;
	private String description;
	private DateFormat dateFormat;

	public DateFormat getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	public Date getOrigDate() {
		return origDate;
	}

	public String getOrigDateFormatted() {
		return dateFormat.format(origDate);
	}

	public void setOrigDate(Date origDate) {
		this.origDate = origDate;
	}
	
	public Date getAltDate() {
		return altDate;
	}
	
	public String getAltDateFormatted() {
		return dateFormat.format(altDate);
	}
	
	public void setAltDate(Date altDate) {
		this.altDate = altDate;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String toString(){
		return String.format("'" + description + "' (original: " + getOrigDateFormatted() +", alternative: " + getAltDateFormatted() + ")");
	}
}
