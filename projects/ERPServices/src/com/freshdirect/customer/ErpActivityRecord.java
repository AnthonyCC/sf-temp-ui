package com.freshdirect.customer;

import java.util.Date;
import java.util.Comparator;
import java.util.List;
import com.freshdirect.enum.EnumModel;

public class ErpActivityRecord implements java.io.Serializable {

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
	
	private abstract static class ActivityRecordComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			return this.compare((ErpActivityRecord) o1, (ErpActivityRecord) o2);
		}
		
		protected abstract int compare(ErpActivityRecord c1, ErpActivityRecord c2);
		
		protected int compare(List enumList, EnumModel e1, EnumModel e2) {
			return compare(enumList.indexOf(e1), enumList.indexOf(e2));
		}
		
		protected int compare(int p1, int p2) {
			return p1 == p2 ? 0 : (p1 < p2 ? -1 : 1);
		}
	}
	
	public final static Comparator COMP_DATE = new ActivityRecordComparator() {
		protected int compare(ErpActivityRecord c1, ErpActivityRecord c2) {
			return c1.getDate().compareTo(c2.getDate()); 	
		}
	};
		
	public final static Comparator COMP_ACTIVITY = new ActivityRecordComparator () {
		protected int compare(ErpActivityRecord c1, ErpActivityRecord c2) {
			return c1.getActivityType().getName().toLowerCase().compareTo(c2.getActivityType().getName().toLowerCase());
		}
	};
	
	public final static Comparator COMP_INITIATOR = new ActivityRecordComparator() {
		protected int compare(ErpActivityRecord c1, ErpActivityRecord c2) {
			return c1.getInitiator().toLowerCase().compareTo(c2.getInitiator().toLowerCase());
		}
	};
	
	public final static Comparator COMP_SOURCE = new ActivityRecordComparator() {
		protected int compare(ErpActivityRecord c1, ErpActivityRecord c2){
			return c1.getSource().getName().toLowerCase().compareTo(c2.getSource().getName().toLowerCase());
		}
	};
}
