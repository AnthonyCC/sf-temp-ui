package com.freshdirect.webapp.taglib.fdstore;

import java.util.Date;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.TimeOfDay;

public class CutoffInfo {
	
	private final String zoneCode;
	private final Date deliveryDate;
	private final TimeOfDay now;
	
	private TimeOfDay cutoffTime;
	
	private boolean hasMultipleCutoff;
	private TimeOfDay nextCutoff;
	private TimeOfDay nextEarliestTimeslot;
	private TimeOfDay nextLatestTimeslot;
	private TimeOfDay lastCutoff;
	
	private String zipCode;
	
	public CutoffInfo (String zoneCode, Date deliveryDate) {
		this.zoneCode = zoneCode;
		this.deliveryDate = deliveryDate;
		this.now = new TimeOfDay(new Date());
	}

	public TimeOfDay getCutoffTime() {
		return cutoffTime;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public boolean displayWarning() {
		if(now.after(cutoffTime)){
			return false;
		}
		return DateUtil.getDiffInMinutes(cutoffTime, now) <= FDStoreProperties.getCutoffWarnStart() * 60;
	}
	
	public boolean isHourPastCutoff() {
		return (Math.abs(TimeOfDay.getDurationAsHours(lastCutoff, now)) <= 1 && lastCutoff.before(now));
	}

	public int getMinsBeforeCutoff() {
		if(now.after(cutoffTime)){
			return 0;
		}
		
		//Subtracting 1 because the difference should be from 59 not 60
		return DateUtil.getDiffInMinutes(cutoffTime, now) - 1;
	}
	
	public boolean hasMultipleCutoff() {
		return hasMultipleCutoff;
	}
	
	public TimeOfDay getNextCutoff() {
		return nextCutoff;
	}

	public TimeOfDay getNextEarliestTimeslot() {
		return nextEarliestTimeslot;
	}
	
	public TimeOfDay getNextLatestTimeslot() {
		return nextLatestTimeslot;
	}
	
	public TimeOfDay getLastCutoff() {
		return lastCutoff;
	}
	
	public String getZipCode() {
		return zipCode;
	}
	
	public String getZoneCode() {
		return zoneCode;
	}
	
	public void setCutoffTime(TimeOfDay cutoffTime) {
		this.cutoffTime = cutoffTime;
	}
	
	public void setHasMultipleCutoff(boolean hasMultipleCutoff) {
		this.hasMultipleCutoff = hasMultipleCutoff;
	}

	public void setNextCutoff(TimeOfDay nextCutoff) {
		this.nextCutoff = nextCutoff;
	}
	
	public void setNextEarliestTimeslot(TimeOfDay nextEarliestTimeslot) {
		this.nextEarliestTimeslot = nextEarliestTimeslot;
	}
	
	public void setNextLatestTimeslot(TimeOfDay nextLatestTimeslot) {
		this.nextLatestTimeslot = nextLatestTimeslot;
	}
	
	public void setLastCutoff(TimeOfDay lastCutoff) {
		this.lastCutoff = lastCutoff;
	}
	
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	public String toString() {
		
		return "CutoffInfo["
			+ "displayWarning="
			+ this.displayWarning()
			+ " minsBeforeCutoff="
			+ this.getMinsBeforeCutoff()
			+ " cutoffTime="
			+ this.cutoffTime
			+ " deliveryDate="
			+ this.deliveryDate
			+ " hasMultipleCutoff="
			+ this.hasMultipleCutoff
			+ " nextCutoff="
			+ this.nextCutoff
			+ " nextEarliestTimeslot="
			+ this.nextEarliestTimeslot
			+ " nextLatestTimeslot="
			+ this.nextLatestTimeslot
			+ " lastCutoff="
			+ this.lastCutoff
			+ " zipCode="
			+ this.zipCode
			+ " zoneCode="
			+ this.zoneCode
			+ "]";
	}
}
