package com.freshdirect.delivery.planning;

import com.freshdirect.delivery.EnumTruckType;
import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.core.PrimaryKey;

public class DlvTruckAllocation extends ModelSupport{
    
    private EnumTruckType truckType;
    private int count;
    private int capacity;
 
    public DlvTruckAllocation (){
    	super();
    	truckType = null;
    }
    
    public DlvTruckAllocation (EnumTruckType truckType){
    	this.truckType = truckType;
    }
    
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

	public EnumTruckType getType() {
		return this.truckType;
	}

	public EnumTruckType getTruckType() {
		return truckType;
	}

	public void setTruckType(EnumTruckType truckType) {
		this.truckType = truckType;
	}

}
