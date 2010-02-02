/*
 * DlvTimeslotModel.java
 *
 * Created on August 27, 2001, 5:11 PM
 */

package com.freshdirect.delivery.model;

/**
 * 
 * @author knadeem
 * @version
 */
import java.util.Calendar;
import java.util.Date;

import com.freshdirect.delivery.EnumTimeslotStatus;
import com.freshdirect.delivery.planning.DlvShiftTimeslotModel;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.routing.model.IDeliverySlot;

public class DlvTimeslotModel extends DlvShiftTimeslotModel {

	private Date baseDate;

	private String zoneId;

	/** Total capacity */
	private int capacity;

	/** Total order */
	private int order;

	/** Chef's Table Capacity (ctCapacity <= capacity) */
	private int chefsTableCapacity;

	/** Planned total capacity (informational) */
	private int plannedCapacity;

	/** Number of reservations in base pool */
	private int baseAllocation;

	/** Number of reservations in CT pool */
	private int chefsTableAllocation;

	/** Number of pre-reservations */
	private int preAllocation;

	/** minutes of CT capacity auto release time */
	private int ctReleaseTime;
	
	/** CT capacity activated indicator */
	private boolean ctActive;
	
	private String zoneCode;
	
	
	private IDeliverySlot routingSlot;
	
	/* Dummy Property for Hibernate*/
	private String isDynamic;
	private String isClosed;
	
		
	public String getIsDynamic() {
		return isDynamic;
	}

	public void setIsDynamic(String isDynamic) {
		this.isDynamic = isDynamic;
	}

	public String getIsClosed() {
		return isClosed;
	}

	public void setIsClosed(String isClosed) {
		this.isClosed = isClosed;
	}

	public DlvTimeslotModel() {
		super();
	}

	public DlvTimeslotModel(DlvShiftTimeslotModel shiftTimeslot) {
		super();
		this.setStartTime(shiftTimeslot.getStartTime());
		this.setEndTime(shiftTimeslot.getEndTime());
		this.setCutoffTime(shiftTimeslot.getCutoffTime());
		this.setStatus(shiftTimeslot.getStatus());
	}

	public DlvTimeslotModel(PrimaryKey pk, String zoneId, Date baseDate,
			TimeOfDay startTime, TimeOfDay endTime, TimeOfDay cutoffTime,
			EnumTimeslotStatus status, int capacity, int ctCapacity,
			int baseAllocation, int ctAllocation, int ctReleaseTime, boolean ctActive,String zoneCode) {
		super();
		this.setPK(pk);
		this.setBaseDate(baseDate);
		this.setZoneId(zoneId);
		this.setStartTime(startTime);
		this.setEndTime(endTime);
		this.setCutoffTime(cutoffTime);
		this.setStatus(status);
		this.setCapacity(capacity);
		this.setChefsTableCapacity(ctCapacity);
		this.setBaseAllocation(baseAllocation);
		this.setChefsTableAllocation(ctAllocation);
		this.setCtReleaseTime(ctReleaseTime);
		this.setCtActive(ctActive);
		this.setZoneCode(zoneCode);		
	}
	
	
	//
	// basic properties
	//

	
	public String getZoneCode() {
		return this.zoneCode;
	}

	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}
	
	public String getZoneId() {
		return this.zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	public int getCapacity() {
		return capacity;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getChefsTableCapacity() {
		return chefsTableCapacity;
	}

	public void setChefsTableCapacity(int chefsTableCapacity) {
		this.chefsTableCapacity = chefsTableCapacity;
	}

	public int getPlannedCapacity() {
		return this.plannedCapacity;
	}

	public void setPlannedCapacity(int plannedCapacity) {
		this.plannedCapacity = plannedCapacity;
	}

	public int getBaseAllocation() {
		return baseAllocation;
	}

	public void setBaseAllocation(int baseReservationCount) {
		this.baseAllocation = baseReservationCount;
	}

	public int getChefsTableAllocation() {
		return chefsTableAllocation;
	}

	public void setChefsTableAllocation(int chefstableReservationCount) {
		this.chefsTableAllocation = chefstableReservationCount;
	}

	public int getPreAllocation() {
		return this.preAllocation;
	}

	public void setPreAllocation(int preAllocation) {
		this.preAllocation = preAllocation;
	}

	public Date getBaseDate() {
		return baseDate;
	}

	public void setBaseDate(Date timeslotDate) {
		this.baseDate = timeslotDate;
	}

	public int getCtReleaseTime() {
		return ctReleaseTime;
	}

	public void setCtReleaseTime(int ctReleaseTime) {
		this.ctReleaseTime = ctReleaseTime;
	}
	
	public boolean isCtActive() {
		return this.ctActive;
	}

	public void setCtActive(boolean ctActive) {
		this.ctActive = ctActive;
	}
	
	//
	// artifical properties
	//
	
	/**
	 * @return total allocation = base allocation + chef's table allocation
	 */
	public int getTotalAllocation() {
		return getBaseAllocation() + getChefsTableAllocation();
	}
	
	public int getBaseCapacity() {
		return getCapacity() - getChefsTableCapacity();
	}

	public int getTotalAvailable() {
		return this.getCapacity() - this.getTotalAllocation();
	}

	public int getBaseAvailable() {
		return this.getBaseCapacity() - this.getBaseAllocation();
	}
	
	public int getChefsTableAvailable() {
		return this.getChefsTableCapacity() - this.getChefsTableAllocation();
	}
	
	//
	// helper methods
	//

	/**
	 * Current allocation has a time-dependent definition:
	 * <ul>
	 *  <li>before CT Release Time = Base Allocation + CT Capacity,</li>
	 *  <li>after CT Release Time = Base Allocation + CT Allocation.</li>
	 * </ul>
	 * 
	 * @param currentDate date to use for calculation (never null)
	 * @return current allocation based on current date.
	 */
	public int calculateCurrentAllocation(Date currentDate) {
		if (isCTCapacityReleased(currentDate))
			return getTotalAllocation();
		else
			return getChefsTableCapacity() + getBaseAllocation();
	}

	public boolean isCTCapacityReleased(Date currentDate) {
		if (isCtActive())  {
			Calendar releaseCal = Calendar.getInstance();
			releaseCal.setTime(getCutoffTimeAsDate());
			releaseCal.add(Calendar.MINUTE, -getCtReleaseTime());
			Calendar now = DateUtil.toCalendar(currentDate);
			return now.after(releaseCal);
		}else
			return false;
	}

	public boolean isCapacityOverriden() {
		return this.plannedCapacity != this.getCapacity();
	}

	public boolean isOvercommited() {
		return this.getCapacity() < this.calculateCurrentAllocation(new Date())
				|| this.getChefsTableCapacity() > this.getCapacity();
	}

	public Date getStartTimeAsDate() {
		return getStartTime().getAsDate(getBaseDate());
	}

	public Date getEndTimeAsDate() {
		return getEndTime().getAsDate(getBaseDate());
	}

	public Date getCutoffTimeAsDate() {
		Calendar cutoffCal = getCutoffTime().getAsCalendar(getBaseDate());
		// as cutoff time is always the day before
		cutoffCal.add(Calendar.DATE, -1);
		return cutoffCal.getTime();
	}

	public boolean isMatching(Date baseDate, Date startTime, Date endTime) {
		if (this.baseDate.equals(DateUtil.truncate(baseDate))
				&& this.getStartTime().equals(new TimeOfDay(startTime))
				&& this.getEndTime().equals(new TimeOfDay(endTime))) {
			return true;
		}
		return false;
	}
	
	public IDeliverySlot getRoutingSlot() {
		return routingSlot;
	}

	public void setRoutingSlot(IDeliverySlot routingSlot) {
		this.routingSlot = routingSlot;
	}
	
		
	public double getChefsTablePercentage() {
		//System.out.println(getCapacity()+"-COST->" +getChefsTableCapacity());
		double result = 0.0;
		if(getCapacity() > 0) {
			return getChefsTableCapacity()/(double)getCapacity();
		}
		return result;
	}
	
	
		

}