package com.freshdirect.crm;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.StringUtil;

public class CrmAuthSearchCriteria implements java.io.Serializable {

	private Date fromDate;
	private String fromDateStr;
	private Date toDate;
	private String toDateStr;
	private String customerName="";
	private BigDecimal amount=new BigDecimal(0);
	
	
	public CrmAuthSearchCriteria() {
	
	}

	public boolean setAmount(String amount) {
		if(!StringUtil.isEmpty(amount) && StringUtil.isDecimal(amount)) {
			this.amount=new BigDecimal(amount);
			return true;
		}
		
		return false;
	}

	public String getAmount() {
		return amount.toPlainString();
	}
	
	public void setCustomerName(String name) {
		if(!StringUtil.isEmpty(name)) {
			customerName=name;
		}
	}

	public String getCustomerName() {
		return customerName;
	}

	
	
	public boolean isEmpty(){
		boolean isEmtpy = true;
		if( null != fromDateStr || null !=toDateStr)
				{
			isEmtpy = false;
		}
		return isEmtpy;
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
}
