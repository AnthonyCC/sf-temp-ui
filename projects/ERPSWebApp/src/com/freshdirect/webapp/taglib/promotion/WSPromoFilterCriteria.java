package com.freshdirect.webapp.taglib.promotion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.freshdirect.framework.util.DateUtil;

public class WSPromoFilterCriteria {

	private Date fromDate;
	private String fromDateStr;
	private Date toDate;
	private String toDateStr;
	private String status;
	
	
	public WSPromoFilterCriteria() {
	
	}

	public Date getFromDate() {
		return getFormattedDate(fromDateStr, "00:00 AM");
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public String getFromDateStr() {
		return fromDateStr;
	}

	public void setFromDateStr(String dateStr) {
		this.fromDateStr = dateStr;
	}

	private Date getFormattedDate(String dateStr, String time){
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
			Date date = sdf.parse(dateStr+" "+time);
			Calendar cal = DateUtil.toCalendar(date);				
			return cal.getTime();
		} catch (ParseException pe) { }
	
	
		return null;
	}

	public boolean isEmpty(){
		boolean isEmtpy = true;
		if((null != fromDate) 
				|| (null != toDate))
				{
			isEmtpy = false;
		}
		return isEmtpy;
	}
	public Date getToDate() {
		return getFormattedDate(toDateStr, "11:59 PM");
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getToDateStr() {
		return toDateStr;
	}

	public void setToDateStr(String toDateStr) {
		this.toDateStr = toDateStr;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
