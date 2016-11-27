package com.freshdirect.crm;

import java.util.Date;

import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.core.PrimaryKey;

public class CrmClick2CallModel extends ModelSupport {
	
	
	
	
	private PrimaryKey id;
	private boolean status;	
	private String eligibleCustomers;
	private boolean nextDayTimeSlot;
	private Date croModDate;
	private String userId;
	private CrmClick2CallTimeModel[] days;
	private String[] deliveryZones;
	
	public CrmClick2CallModel(boolean status, String eligibleCustomers,
			boolean nextDayTimeSlot, Date croModDate, String userId,
			CrmClick2CallTimeModel[] days, String[] deliveryZones) {
		super();
		this.status = status;
		this.eligibleCustomers = eligibleCustomers;
		this.nextDayTimeSlot = nextDayTimeSlot;
		this.croModDate = croModDate;
		this.userId = userId;
		this.days = days;
		this.deliveryZones = deliveryZones;
	}
	
	public CrmClick2CallModel() {
		super();		
	}
	
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}	
	public String getEligibleCustomers() {
		return eligibleCustomers;
	}
	public void setEligibleCustomers(String eligibleCustomers) {
		this.eligibleCustomers = eligibleCustomers;
	}
	public boolean isNextDayTimeSlot() {
		return nextDayTimeSlot;
	}
	public void setNextDayTimeSlot(boolean nextDayTimeSlot) {
		this.nextDayTimeSlot = nextDayTimeSlot;
	}
	public Date getCroModDate() {
		return croModDate;
	}
	public void setCroModDate(Date croModDate) {
		this.croModDate = croModDate;
	}
	public CrmClick2CallTimeModel[] getDays() {
		return days;
	}
	public void setDays(CrmClick2CallTimeModel[] days) {
		this.days = days;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String[] getDeliveryZones() {
		return deliveryZones;
	}

	public void setDeliveryZones(String[] deliveryZones) {
		this.deliveryZones = deliveryZones;
	}

	/*public String getDeliveryZones() {
		return deliveryZones;
	}

	public void setDeliveryZones(String deliveryZones) {
		this.deliveryZones = deliveryZones;
	}*/
	
	
	
	

}
