package com.freshdirect.routing.model;

public class RoutingSchedulerIdentity extends BaseModel implements IRoutingSchedulerIdentity {
	
	private java.lang.String regionId;

    private IAreaModel area;

    private java.util.Date deliveryDate;

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
		return regionId+"-"+area+"-"+deliveryDate;
	}
}
