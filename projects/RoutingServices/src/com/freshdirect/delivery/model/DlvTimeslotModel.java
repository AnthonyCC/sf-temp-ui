package com.freshdirect.delivery.model;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import com.freshdirect.delivery.EnumRegionServiceType;
import com.freshdirect.delivery.EnumTimeslotStatus;
import com.freshdirect.delivery.planning.DlvShiftTimeslotModel;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.routing.model.IAreaModel;
import com.freshdirect.routing.model.IDeliverySlot;

public class DlvTimeslotModel extends DlvShiftTimeslotModel {

	private static final long	serialVersionUID	= 795111720212622032L;

	private Date baseDate;

	private String zoneId;

	/** Total capacity */
	private int capacity;

	/** Total order */
	private int order;

	/** Chef's Table Capacity (ctCapacity <= capacity) */
	private int chefsTableCapacity;
	
	private int premiumCapacity;

	private int premiumCtCapacity;
	
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

	private int premiumCtReleaseTime;
	
	/** CT capacity activated indicator */
	private boolean ctActive;
	
	private boolean premiumCtActive;
	
	private int premiumAllocation;
	
	private int premiumCtAllocation;
	
	private String zoneCode;
	
	private boolean premiumSlot;
	
	private IDeliverySlot routingSlot;
	
	/* Dummy Property for Hibernate*/
	private String isDynamic;
	private String isClosed;
	
	/* Window Steering Discount Value*/
	private double steeringDiscount;

	private double premiumAmount;
		
	private int totalConfirmed;
	
	private EnumRegionServiceType regionSvcType;
	
	public boolean hasSteeringRadius() {
		if(this.getRoutingSlot() != null && this.getRoutingSlot().getDeliveryCost() != null && this.getRoutingSlot().getSteeringRadius() != null) {	
			double additionalDistance = (double) this.getRoutingSlot().getDeliveryCost().getAdditionalDistance();
			if(this.getRoutingSlot().getSteeringRadius().doubleValue() != BigDecimal.ZERO.doubleValue())	{
				if(additionalDistance / 100 <= this.getRoutingSlot().getSteeringRadius().doubleValue())
						return true;
			} else {
				if(additionalDistance / 100 == 0) 
					return true;
			}
		}
		return false;
	}
	
	public boolean isEcoFriendly() {		
		
		if(this.getRoutingSlot() != null && this.getRoutingSlot().getDeliveryCost() != null && this.getRoutingSlot().getEcoFriendly() != null) {
			double additionalDistance = (double) this.getRoutingSlot().getDeliveryCost().getAdditionalDistance();
			if(this.getRoutingSlot().getEcoFriendly().doubleValue() != BigDecimal.ZERO.doubleValue())	{		
				if(additionalDistance / 100 <= this.getRoutingSlot().getEcoFriendly().doubleValue())
						return true;
			}else{
				if(additionalDistance / 100 == 0) 
					return true;
			}
		}
		return false;
	}
	
	public boolean isDepot(){
		if(this.getRoutingSlot()!=null && this.getRoutingSlot().getSchedulerId()!=null 
					&& this.getRoutingSlot().getSchedulerId().getArea()!= null){
			IAreaModel areaModel = this.getRoutingSlot().getSchedulerId().getArea();			
			return areaModel.isDepot();
		}
		return false;
	}

	public double getSteeringDiscount() {
		return steeringDiscount;
	}

	public void setSteeringDiscount(double steeringDiscount) {
		this.steeringDiscount = steeringDiscount;
	}

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
		this.setRoutingStartTime(shiftTimeslot.getRoutingStartTime());
		this.setRoutingEndTime(shiftTimeslot.getRoutingEndTime());
		this.setCutoffTime(shiftTimeslot.getCutoffTime());
		if(shiftTimeslot.getPremiumCutoffTime()!=null)
		this.setPremiumCutoffTime(shiftTimeslot.getPremiumCutoffTime());
		this.setStatus(shiftTimeslot.getStatus());
	}

	public DlvTimeslotModel(PrimaryKey pk, String zoneId, Date baseDate,
			TimeOfDay startTime, TimeOfDay endTime, TimeOfDay routingStartTime, TimeOfDay routingEndTime, TimeOfDay cutoffTime,
			EnumTimeslotStatus status, int capacity, int ctCapacity,
			int baseAllocation, int ctAllocation, int ctReleaseTime, boolean ctActive,String zoneCode, int premiumCapacity,
			 int premiumCtCapacity, TimeOfDay premiumCutoffTime, int premiumCtReleaseTime, 
			 boolean premiumCtActive,int premiumAllocation, int premiumCtAllocation, boolean premiumSlot, int totalConfirmed) {
		super();
		this.setPK(pk);
		this.setBaseDate(baseDate);
		this.setZoneId(zoneId);
		this.setStartTime(startTime);
		this.setEndTime(endTime);
		this.setRoutingStartTime(routingStartTime);
		this.setRoutingEndTime(routingEndTime);
		this.setCutoffTime(cutoffTime);
		this.setPremiumCutoffTime(premiumCutoffTime);
		this.setStatus(status);
		this.setCapacity(capacity);
		this.setChefsTableCapacity(ctCapacity);
		this.setPremiumCapacity(premiumCapacity);
		this.setPremiumCtCapacity(premiumCtCapacity);
		
		this.setChefsTableCapacity(ctCapacity);
		this.setBaseAllocation(baseAllocation);
		this.setChefsTableAllocation(ctAllocation);
		this.setCtReleaseTime(ctReleaseTime);
		this.setPremiumCtReleaseTime(premiumCtReleaseTime);
		this.setPremiumCtActive(premiumCtActive);
		this.setCtActive(ctActive);
		this.setZoneCode(zoneCode);	
		this.setPremiumAllocation(premiumAllocation);
		this.setPremiumCtAllocation(premiumCtAllocation);
		this.setPremiumSlot(premiumSlot);
		this.setTotalConfirmed(totalConfirmed);
	}
	
	
	//
	// basic properties
	//


	public int getTotalConfirmed() {
		return totalConfirmed;
	}

	public void setTotalConfirmed(int totalConfirmed) {
		this.totalConfirmed = totalConfirmed;
	}
	
	public void setPremiumCtAllocation(int premiumCtAllocation) {
		this.premiumCtAllocation = premiumCtAllocation;
		
	}

	public void setPremiumAllocation(int premiumAllocation) {
		this.premiumAllocation = premiumAllocation;
		
	}
	public int getPremiumCtAllocation() {
		return premiumCtAllocation;
		
	}
	public int getPremiumAllocation() {
		return premiumAllocation;
		
	}

	public int getPremiumBaseAllocation() {
		return premiumAllocation;
		
	}
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
		if(!premiumSlot)
			return getBaseAllocation() + getChefsTableAllocation() + 
					getPremiumCapacity();
		else
			return getBaseAllocation() + getChefsTableAllocation();
	}
	
	public int getBaseCapacity() {
		if(!premiumSlot)
			return getCapacity() - getChefsTableCapacity() - getPremiumCapacity();
		else
			return getCapacity() - getChefsTableCapacity();
	}

	public int getTotalAvailable() {
			return this.getCapacity() - this.getTotalAllocation();
	}

	public int getBaseAvailable() {
		if(!premiumSlot)
			return this.getBaseCapacity() - this.getBaseAllocation();
		else
			return this.getPremiumBaseCapacity() - this.getBaseAllocation();
		
	}
	
	public int getChefsTableAvailable() {
			return this.getChefsTableCapacity() - this.getChefsTableAllocation();
	}
	
	//premium calculated properties
	public int getPremiumBaseAvailable() {
		return this.getPremiumBaseCapacity() - this.getPremiumBaseAllocation();
	}
	public int getPremiumCtAvailable() {
		return this.getPremiumCtCapacity() - this.getPremiumCtAllocation();
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
		if(!premiumSlot)
		{
		int allocation = 0;
		
		allocation = getBaseAllocation();
		
		if (isCTCapacityReleased(currentDate))
			allocation += getChefsTableAllocation();
		else
			allocation +=  getChefsTableCapacity();
		
		allocation += getPremiumCapacity();
			
		return allocation;
		}
		else
		return calculatePremiumAllocation(currentDate);
	}
	
	public int calculatePremiumAllocation(Date currentDate) {
		
		int allocation = 0;
		
		allocation = getPremiumBaseAllocation();
		
		if (isPremiumCtCapacityReleased(currentDate))
			allocation += getPremiumCtAllocation();
		else
			allocation +=  getPremiumCtCapacity();
		
		return allocation;
			
	}
	public boolean isCTCapacityReleased(Date currentDate) {
		if(!premiumSlot)
		{
		if (isCtActive())  {
			Calendar releaseCal = Calendar.getInstance();
			releaseCal.setTime(getCutoffTimeAsDate());
			releaseCal.add(Calendar.MINUTE, -getCtReleaseTime());
			Calendar now = DateUtil.toCalendar(currentDate);
			return now.after(releaseCal);
		}else
			return false;
		}
		else
			return isPremiumCtCapacityReleased(currentDate);
		 
	}

	public boolean isPremiumCtCapacityReleased(Date currentDate) {
		if (isPremiumCtActive() && getPremiumCutoffTime()!=null)  {
			Calendar releaseCal = Calendar.getInstance();
			releaseCal.setTime(getPremiumCutoffAsDate());
			releaseCal.add(Calendar.MINUTE, -getPremiumCtReleaseTime());
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
	
	public Date getRoutingStartTimeAsDate() {
		return new TimeOfDay(getRoutingSlot().getStartTime()).getAsDate(getBaseDate());
	}

	public Date getRoutingEndTimeAsDate() {
		return new TimeOfDay(getRoutingSlot().getStopTime()).getAsDate(getBaseDate());
	}
	
	public Date getPremiumCutoffAsDate() {
		return getPremiumCutoffTime().getAsDate(getBaseDate());
	}

	public Date getCutoffTimeAsDate() {
		if(!premiumSlot)
		{
		Calendar cutoffCal = getCutoffTime().getAsCalendar(getBaseDate());
		// as cutoff time is always the day before
		cutoffCal.add(Calendar.DATE, -1);
		return cutoffCal.getTime();
		}
		else
			return getPremiumCutoffAsDate();
	}

	public Date getCutoffTimeAsNormalDate() {
		if(!premiumSlot)
		{
		return getCutoffTime().getNormalDate();	
		}
		else
		{
		return getPremiumCutoffTime().getNormalDate();			
		}
	}

	public boolean isMatching(Date baseDate, Date startTime, Date endTime) {
		if (this.baseDate.equals(DateUtil.truncate(baseDate))
				&& this.getStartTime().equals(new TimeOfDay(startTime))
				&& this.getEndTime().equals(new TimeOfDay(endTime))) {
			return true;
		}
		return false;
	}
	
	public boolean isRoutingSlotMatching(Date baseDate, Date startTime, Date endTime) {
		if (this.baseDate.equals(DateUtil.truncate(baseDate))
				&& new TimeOfDay(this.getRoutingSlot().getStartTime()).equals(new TimeOfDay(startTime))
				&& new TimeOfDay(this.getRoutingSlot().getStopTime()).equals(new TimeOfDay(endTime))) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * Checks whether t falls within boundaries of timeslot 
	 * 
	 * @param baseDate The date of timeslot (yyyy:mm:dd part)
	 * @param t Date containing the HH:MM portion
	 * @return
	 */
	public boolean isWithinRange(Date baseDate, Date t) {
		final TimeOfDay __t = new TimeOfDay(t);
		if (this.baseDate.equals(DateUtil.truncate(baseDate))
				&& this.getStartTime().compareTo(__t) <= 0
				&& this.getEndTime().compareTo(__t) > 0) {
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
	
	public double getPremiumCapacityPercentage() {
		//System.out.println(getCapacity()+"-COST->" +getChefsTableCapacity());
		double result = 0.0;
		if(getCapacity() > 0) {
			return getPremiumCapacity()/(double)getCapacity();
		}
		return result;
	}

	public int getPremiumCapacity() {
		return premiumCapacity;
	}
	public int getPremiumBaseCapacity() {
		return getPremiumCapacity() - getPremiumCtCapacity();
	}
	

	public void setPremiumCapacity(int premiumCapacity) {
		this.premiumCapacity = premiumCapacity;
	}

	public int getPremiumCtCapacity() {
		return premiumCtCapacity;
	}

	public void setPremiumCtCapacity(int premiumCtCapacity) {
		this.premiumCtCapacity = premiumCtCapacity;
	}

	public int getPremiumCtReleaseTime() {
		return premiumCtReleaseTime;
	}

	public void setPremiumCtReleaseTime(int premiumCtReleaseTime) {
		this.premiumCtReleaseTime = premiumCtReleaseTime;
	}

	public boolean isPremiumCtActive() {
		return premiumCtActive;
	}

	public void setPremiumCtActive(boolean premiumCtActive) {
		this.premiumCtActive = premiumCtActive;
	}

	public boolean isPremiumSlot() {
		return premiumSlot;
	}

	public void setPremiumSlot(boolean premiumSlot) {
		this.premiumSlot = premiumSlot;
	}

	public double getPremiumAmount() {
		return premiumAmount;
	}
	
	public void setPremiumAmount(double premiumAmount) {
		this.premiumAmount = premiumAmount;
	}
	
	public String toString() {
		return "DlvTimesotModel[BaseDate:" + this.getBaseDate() + " startTime:"
				+ super.getStartTime() + " endTime:" + super.getEndTime()
				+ " capacity:" + this.getCapacity() + " ctCapacity:"
				+ this.getChefsTableCapacity() + " baseAlloc:"
				+ this.getBaseAllocation() + " ctAlloc:"
				+ this.getChefsTableAllocation() + ": " + this.getPK()
				+ " Eco-Friendly:" + this.isEcoFriendly() 
				+ " RadiusSteering:" + this.hasSteeringRadius() + "]";
	}

	public EnumRegionServiceType getRegionSvcType() {
		return regionSvcType;
	}

	public void setRegionSvcType(EnumRegionServiceType regionSvcType) {
		this.regionSvcType = regionSvcType;
	}

	

}