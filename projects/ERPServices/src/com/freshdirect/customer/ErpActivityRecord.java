package com.freshdirect.customer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Comparator;

import com.freshdirect.framework.util.DateUtil;

public class ErpActivityRecord implements java.io.Serializable {
	
	private static final long serialVersionUID = 8592047251020131505L;

	private String customerId;
	private String custFirstName;
	private String custLastName;
	private EnumTransactionSource source;
	private String initiator;

	private EnumAccountActivityType type;
	private String note;
	private Date date;
	
	//Specific to Delivery Pass Activity.
	private String deliveryPassId;
	private String changeOrderId;
	private String reason;
	private Date fromDate;
	private String fromDateStr;
	private Date toDate;
	private String toDateStr;

	public Date getFromDate() {
		return getFormattedDate(fromDateStr, "00:00 AM");
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return getFormattedDate(toDateStr, "11:59 PM");
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}


	private String standingOrderId;
	
	private String masqueradeAgent;
	
	
	public String getMasqueradeAgent() {
		return masqueradeAgent;
	}

	public void setMasqueradeAgent( String agentId ) {
		masqueradeAgent = agentId;
	}
	
	public String getChangeOrderId() {
		return changeOrderId;
	}

	public void setChangeOrderId(String changeOrderId) {
		this.changeOrderId = changeOrderId;
	}

	
	public String getDeliveryPassId() {
		return deliveryPassId;
	}

	public void setDeliveryPassId(String deliveryPassId) {
		this.deliveryPassId = deliveryPassId;
	}

	
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	
	public void setActivityType(EnumAccountActivityType t) {
		this.type = t;
	}

	public EnumAccountActivityType getActivityType() {
		return this.type;
	}

	
	public void setNote(String s) {
		this.note = s;
	}

	public String getNote() {
		return this.note != null && this.note.length() != 0 ? this.note : this.type.getName();
	}

	
	public void setDate(Date d) {
		this.date = d;
	}

	public Date getDate() {
		return this.date;
	}

	
	public String getCustomerId() {
		return customerId;
	}
	
	public void setCustomerId(String string) {
		customerId = string;
	}

	
	public String getInitiator() {
		return initiator;
	}
	
	public void setInitiator(String string) {
		initiator = string;
	}

	
	public EnumTransactionSource getSource() {
		return source;
	}

	public void setSource(EnumTransactionSource source) {
		this.source = source;
	}
	
	
	public String getStandingOrderId() {
		return standingOrderId;
	}

	public void setStandingOrderId(String standingOrderId) {
		this.standingOrderId = standingOrderId;
	}


	public final static Comparator<ErpActivityRecord> COMP_DATE = new Comparator<ErpActivityRecord>() {
		public int compare(ErpActivityRecord c1, ErpActivityRecord c2) {
			return c1.getDate().compareTo(c2.getDate()); 	
		}
	};
		
	public final static Comparator<ErpActivityRecord> COMP_ACTIVITY = new Comparator<ErpActivityRecord>() {
		public int compare(ErpActivityRecord c1, ErpActivityRecord c2) {
			if(null !=c1.getActivityType() && null != c2.getActivityType()){
				return c1.getActivityType().getName().toLowerCase().compareTo(c2.getActivityType().getName().toLowerCase());
			}else{
				return 0;
			}
		}
	};
	
	public final static Comparator<ErpActivityRecord> COMP_INITIATOR = new Comparator<ErpActivityRecord>() {
		public int compare(ErpActivityRecord c1, ErpActivityRecord c2) {
			if(null !=c1.getInitiator() && null != c2.getInitiator()){
				return c1.getInitiator().toLowerCase().compareTo(c2.getInitiator().toLowerCase());
			}else{
				return 0;
			}
		}
	};
	
	public final static Comparator<ErpActivityRecord> COMP_SOURCE = new Comparator<ErpActivityRecord>() {
		public int compare(ErpActivityRecord c1, ErpActivityRecord c2){
			if(null !=c1.getSource() && null != c2.getSource()){
				return c1.getSource().getName().toLowerCase().compareTo(c2.getSource().getName().toLowerCase());
			}else{
				return 0;
			}			
		}
	};
	
	public boolean isEmptyForFilter(){
		boolean isEmtpy = true;
		if((null != initiator && !initiator.trim().equalsIgnoreCase("")) 
				|| (null != fromDate)
				|| (null != toDate) 
				|| (null != type) 
				|| (null != source)){
			isEmtpy = false;
		}
		return isEmtpy;
	}

	public String getFromDateStr() {
		return fromDateStr;
	}

	public void setFromDateStr(String fromDateStr) {
		this.fromDateStr = fromDateStr;
	}

	public String getToDateStr() {
		return toDateStr;
	}

	public void setToDateStr(String toDateStr) {
		this.toDateStr = toDateStr;
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

	public String getCustFirstName() {
		return custFirstName;
	}

	public void setCustFirstName(String custFirstName) {
		this.custFirstName = custFirstName;
	}

	public String getCustLastName() {
		return custLastName;
	}

	public void setCustLastName(String custLastName) {
		this.custLastName = custLastName;
	}
}
