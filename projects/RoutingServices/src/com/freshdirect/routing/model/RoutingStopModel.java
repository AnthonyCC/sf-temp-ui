package com.freshdirect.routing.model;

import java.util.Date;

public class RoutingStopModel extends OrderModel implements IRoutingStopModel, Comparable  {
	
	private int stopNo;
		
	private boolean isDepot;
	
	private Date stopArrivalTime;
	private Date stopDepartureTime;
	
	private String routingRouteId;
	
	public RoutingStopModel() {
		super();	
	}
	
	public RoutingStopModel(int stopNo) {
		super();
		this.stopNo = stopNo;
	}

	public boolean isDepot() {
		return isDepot;
	}

	public void setDepot(boolean isDepot) {
		this.isDepot = isDepot;
	}
	
	public Date getStopArrivalTime() {
		return stopArrivalTime;
	}

	public void setStopArrivalTime(Date stopArrivalTime) {
		this.stopArrivalTime = stopArrivalTime;
	}

	public int getStopNo() {
		return stopNo;
	}

	public void setStopNo(int stopNo) {
		this.stopNo = stopNo;
	}

	public Date getStopDepartureTime() {
		return stopDepartureTime;
	}

	public void setStopDepartureTime(Date stopDepartureTime) {
		this.stopDepartureTime = stopDepartureTime;
	}

	public String getRoutingRouteId() {
		return routingRouteId;
	}

	public void setRoutingRouteId(String routingRouteId) {
		this.routingRouteId = routingRouteId;
	}

	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		return
		(this.getOrderNumber() == null) ?
				PRIME * result +  stopNo :
					this.getOrderNumber().hashCode() ^ stopNo;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final IRoutingStopModel other = (IRoutingStopModel) obj;
		if (stopNo == other.getStopNo() && (this.getOrderNumber() != null && other.getOrderNumber() != null 
				&& this.getOrderNumber().equalsIgnoreCase(other.getOrderNumber()))) {
			return true;
		}
		return false;
	}

	
	
	public int compareTo(Object objCompare) {
		final int BEFORE = -1;
	    final int EQUAL = 0;
	    final int AFTER = 1;
	    
	    if ( this.equals(objCompare)) return EQUAL;
	    
	    if(objCompare instanceof RoutingStopModel) {
	    	IRoutingStopModel tmpKey = (IRoutingStopModel)objCompare;
	    	
	    	if(this.getStopNo() < tmpKey.getStopNo()) {
	    		return BEFORE; 
	    	} else {
	    		return AFTER; 
	    	}
	    }

		return 0;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("Order No: ").append(this.getOrderNumber()).append(" Sequence No: ").append(this.getStopNo())
									.append(" Address: ").append(this.getDeliveryInfo().getDeliveryLocation().getBuilding().getSrubbedStreet())
															.append(",").append(this.getDeliveryInfo().getDeliveryLocation().getBuilding().getZipCode())
									.append(" Stop A-Time: ").append(this.getStopArrivalTime())
									.append(" Stop D-Time: ").append(this.getStopDepartureTime());
		
		return buf.toString();
	}
	
}
