package com.freshdirect.delivery.planning;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.freshdirect.delivery.EnumTruckType;
import com.freshdirect.delivery.model.DlvTimeslotModel;
import com.freshdirect.framework.core.PrimaryKey;

/**
 * User: knadeem
 * Date: Dec 26, 2002
 * Time: 4:32:43 PM
 * File: DlvResource
 */
public class DlvResourceModel extends DlvAbstractShiftModel{

	private String name;
    private Date day;
    private String zoneCode;
    private int people;
    private double deliveryRate;
    //private List timeslots = new ArrayList();
    //private List truckList = new ArrayList();
    private Map truckAllocations = new HashMap();

    public DlvResourceModel(){
        super();
    }
    
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }
    
    public String getZoneCode(){
    	return this.zoneCode;
    }
    
    public void setZoneCode(String zoneCode){
    	this.zoneCode = zoneCode;
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    public double getDeliveryRate() {
        return deliveryRate;
    }

    public void setDeliveryRate(double deliveryRate) {
        this.deliveryRate = deliveryRate;
    }
    
	
	public List getTruckList() {
		return new ArrayList(this.truckAllocations.values());
	}

	public void setTruckList(List truckList) {
		for(Iterator i = truckList.iterator(); i.hasNext(); ){
			DlvTruckAllocation truck = (DlvTruckAllocation)i.next();
			this.truckAllocations.put(truck.getType(), truck);
		}		
	}

	public DlvTruckAllocation getTruckAllocation(EnumTruckType type) {
		DlvTruckAllocation t = (DlvTruckAllocation)this.truckAllocations.get(type);
		if (t==null) {
			t = new DlvTruckAllocation(type);
			this.setTruckAllocation(t);
		}
		return t;
    }
    

	public void setTruckAllocation(DlvTruckAllocation truck) {
		this.truckAllocations.put(truck.getType(), truck);
	}
	
	public List getTruckAllocations() {
		return new ArrayList(this.truckAllocations.values());
	}
	
	public void setTruckAllocations(List truckList) {
		for(Iterator i = truckList.iterator(); i.hasNext(); ){
			DlvTruckAllocation truck = (DlvTruckAllocation)i.next();
			this.truckAllocations.put(truck.getType(), truck);
		}	
	}
	
	public int getNumberOfTrucks(){
		int numTrucks = 0;
		for(Iterator i = this.getTruckAllocations().iterator(); i.hasNext(); ){
			DlvTruckAllocation truck = (DlvTruckAllocation)i.next();
			numTrucks += truck.getCount();
		}
		
		return numTrucks;
	}
	
	public int getTrucksCapacity() {
		double trucksCapacity = 0;
		for(Iterator i = this.truckAllocations.values().iterator(); i.hasNext(); ){
			DlvTruckAllocation truck = (DlvTruckAllocation)i.next();
			trucksCapacity += truck.getCapacity() * truck.getCount();
		}
		return (int)trucksCapacity;
	}
	
	public void calculateCapacities() {
		double totalDuration = 0.0;
		for(Iterator i = this.getTimeslots().iterator(); i.hasNext(); ){
			totalDuration += ((DlvTimeslotModel)i.next()).getDuration();
		}
		
		double peoplesCapacity = this.people * this.deliveryRate * totalDuration;
	
		double totalCapacity = Math.min( this.getTrucksCapacity(), peoplesCapacity);
		
		DlvTimeslotModel timeslot = null;
		for(Iterator i = this.getTimeslots().iterator(); i.hasNext(); ){
			timeslot = (DlvTimeslotModel)i.next();
			double slotCapacity = (timeslot.getDuration() / totalDuration) * totalCapacity;
			int capacity = (int)(slotCapacity * (timeslot.getTrafficFactor()/100));
			timeslot.setCapacity(capacity);
			timeslot.setPlannedCapacity(capacity);		
		}
	}
	
	public void shutdown() {
		DlvTimeslotModel timeslot = null;
		Date now = new Date();
		for(Iterator i = this.getTimeslots().iterator(); i.hasNext(); ){
			timeslot = (DlvTimeslotModel) i.next();
			timeslot.setCapacity(timeslot.calculateCurrentAllocation(now));
		}
	}

	public boolean isOvercommited() {
		for (Iterator i=this.getTimeslots().iterator(); i.hasNext(); ) {
			if ( ((DlvTimeslotModel)i.next()).isOvercommited() ) {
				return true;
			}
		}
		return false;
	}

	public int getCapacityTotal(){
		int capacity = 0;
		for(Iterator i = this.getTimeslots().iterator(); i.hasNext(); ){
			DlvTimeslotModel timeslot = (DlvTimeslotModel)i.next();
			capacity += timeslot.getCapacity();
		}
		return capacity;
	}

	/**
	 * NOTE: time dependent.
	 * 
	 * @return
	 */
	public int calculateAllocationTotal(){
		Date now = new Date();
		int allocation = 0;
		for(Iterator i = this.getTimeslots().iterator(); i.hasNext(); ){
			DlvTimeslotModel timeslot = (DlvTimeslotModel)i.next();
			allocation += timeslot.calculateCurrentAllocation(now);
		}
		return allocation;
	}

	public int getOrderTotal(){
		int orderCount = 0;
		for(Iterator i = this.getTimeslots().iterator(); i.hasNext(); ){
			DlvTimeslotModel timeslot = (DlvTimeslotModel)i.next();
			orderCount += timeslot.getOrder();
		}
		return orderCount;
	}

	public int getBaseCapacityTotal(){
		int capacity = 0;
		for(Iterator i = this.getTimeslots().iterator(); i.hasNext(); ){
			DlvTimeslotModel timeslot = (DlvTimeslotModel)i.next();
			capacity += timeslot.getBaseCapacity();
		}
		return capacity;
	}

	public int getBaseAllocationTotal(){
		int allocation = 0;
		for(Iterator i = this.getTimeslots().iterator(); i.hasNext(); ){
			DlvTimeslotModel timeslot = (DlvTimeslotModel)i.next();
			allocation += timeslot.getBaseAllocation();
		}
		return allocation;
	}

	public int getPreReservationTotal(){
		int c = 0;
		for(Iterator i = this.getTimeslots().iterator(); i.hasNext(); ){
			DlvTimeslotModel t = (DlvTimeslotModel) i.next();
			c += t.getPreAllocation();
		}
		return c;
	}
	
	
	public int getCTCapacityTotal(){
		int capacity = 0;
		for(Iterator i = this.getTimeslots().iterator(); i.hasNext(); ){
			DlvTimeslotModel timeslot = (DlvTimeslotModel)i.next();
			capacity += timeslot.getChefsTableCapacity();
		}
		return capacity;
	}
	
	public int getCTAllocationTotal(){
		int allocation = 0;
		for(Iterator i = this.getTimeslots().iterator(); i.hasNext(); ){
			DlvTimeslotModel timeslot = (DlvTimeslotModel)i.next();
			allocation += timeslot.getChefsTableAllocation();
		}
		return allocation;
	}
	

	
	public void setPK(PrimaryKey pk) {
		super.setPK(pk);
	}

}
