package com.freshdirect.routing.model;

import com.freshdirect.routing.constants.RoutingActivityType;

public class RoutingSchedulerIdentity extends BaseModel implements IRoutingSchedulerIdentity {
	
	private java.lang.String regionId;

    private IAreaModel area;

    private java.util.Date deliveryDate;
    
    private boolean isDepot;
    private boolean dynamic;
    private RoutingActivityType type;

	public boolean isDepot() {
		return isDepot;
	}

	public void setDepot(boolean isDepot) {
		this.isDepot = isDepot;
	}

	public IAreaModel getArea() {
		return area;
	}

	public void setArea(IAreaModel area) {
		this.area = area;
	}

	public java.util.Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(java.util.Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public java.lang.String getRegionId() {
		return regionId;
	}

	public void setRegionId(java.lang.String regionId) {	
		this.regionId = regionId;
	}
	
	public int hashCode() {
		return
			(regionId==null || area==null || deliveryDate==null) ?
			super.hashCode() :
				regionId.hashCode() ^ area.hashCode() ^ deliveryDate.hashCode();
	}
	
	public boolean equals(Object obj) {		
		if(obj != null && obj instanceof RoutingSchedulerIdentity) {
			RoutingSchedulerIdentity tmpObj = (RoutingSchedulerIdentity)obj;
			if(regionId != null && regionId.equals(tmpObj.getRegionId())  
					&& area != null && area.equals(tmpObj.getArea()) 
						&& deliveryDate != null && deliveryDate.equals(tmpObj.getDeliveryDate())) {
				return true;
			}
		}		
		return false;
	}
	
	public String toString() {
		return regionId+"-"+area.getAreaCode()+"-["+deliveryDate+"]";
	}

	@Override
	public boolean isDynamic() {
		// TODO Auto-generated method stub
		return dynamic;
	}

	@Override
	public void setDynamic(boolean dynamic) {
		// TODO Auto-generated method stub
		this.dynamic = dynamic;
		
	}

	public RoutingActivityType getType() {
		return type;
	}

	public void setType(RoutingActivityType type) {
		this.type = type;
	}
}
