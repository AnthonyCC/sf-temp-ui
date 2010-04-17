package com.freshdirect.customer;

import java.util.Date;
import java.util.Comparator;

public class ErpActivityRecord implements java.io.Serializable {
	private static final long serialVersionUID = 8592047251020131505L;

	private String customerId;
	private EnumTransactionSource source;
	private String initiator;

	private EnumAccountActivityType type;
	private String note;
	private Date date;
	
	//Specific to Delivery Pass Activity.
	private String deliveryPassId;
	private String changeOrderId;
	private String reason;

	private String standingOrderId;
	
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
		return this.note;
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

	public String getInitiator() {
		return initiator;
	}

	public EnumTransactionSource getSource() {
		return source;
	}

	public void setCustomerId(String string) {
		customerId = string;
	}

	public void setInitiator(String string) {
		initiator = string;
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
			return c1.getActivityType().getName().toLowerCase().compareTo(c2.getActivityType().getName().toLowerCase());
		}
	};
	
	public final static Comparator<ErpActivityRecord> COMP_INITIATOR = new Comparator<ErpActivityRecord>() {
		public int compare(ErpActivityRecord c1, ErpActivityRecord c2) {
			return c1.getInitiator().toLowerCase().compareTo(c2.getInitiator().toLowerCase());
		}
	};
	
	public final static Comparator<ErpActivityRecord> COMP_SOURCE = new Comparator<ErpActivityRecord>() {
		public int compare(ErpActivityRecord c1, ErpActivityRecord c2){
			return c1.getSource().getName().toLowerCase().compareTo(c2.getSource().getName().toLowerCase());
		}
	};
}
