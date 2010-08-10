package com.freshdirect.routing.model;

import java.util.Date;

import com.freshdirect.routing.util.RoutingDateUtil;
import com.freshdirect.routing.util.RoutingStringUtil;

public class HandOffBatchRoute extends RouteModel implements IHandOffBatchRoute  {
	
	private String batchId;
	private String area;
	private String sessionName;
		
	
	public HandOffBatchRoute(IRouteModel refModel) {
		super();
		this.setRouteId(refModel.getRouteId());
		this.setStartTime(refModel.getStartTime());
		this.setStops(refModel.getStops());
		this.setDrivingDirection(refModel.getDrivingDirection());
		this.setCompletionTime(refModel.getCompletionTime());
		this.setRoutingRouteId(refModel.getRoutingRouteId());
		
		this.setDistance(refModel.getDistance());
		this.setTravelTime(refModel.getTravelTime());
		this.setServiceTime(refModel.getServiceTime());
		this.setRoutingRouteId(refModel.getRoutingRouteId());
	}
	
	public HandOffBatchRoute() {
		super();
		
	}
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getSessionName() {
		return sessionName;
	}
	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}
	

	@Override
	public Date getCheckInTime() {
		// TODO Auto-generated method stub
		if(this.getCompletionTime() != null) {
			return RoutingDateUtil.addMinutes(this.getCompletionTime(), 25);
		} else {
			return null;
		}
	}

	@Override
	public String getDeliveryModel() {
		if(this.getStops() != null && this.getStops().size() > 0) {
			IRoutingStopModel stop = (IRoutingStopModel)this.getStops().first();
			return stop.getDeliveryInfo() != null ? stop.getDeliveryInfo().getDeliveryModel() : null;
		}
		return null;
	}

	@Override
	public Date getDepartTime() {		
		return this.getStartTime(); // Same as Route Start Time
	}

	@Override
	public Date getDispatchTime() {
		
		if(this.getStartTime() != null) {
			return RoutingDateUtil.addMinutes(this.getStartTime(), -25);
		} else {
			return null;
		}
	}
	
	@Override
	public Date getLastStopCompletionTime() {
		
		if(this.getStops() != null && this.getStops().size() > 0) {
			IRoutingStopModel stop = (IRoutingStopModel)this.getStops().last();
			return stop.getStopDepartureTime();
		}
		return null;
	}
	
	@Override
	public Date getFirstStopTime() {
		
		if(this.getStops() != null && this.getStops().size() > 0) {
			IRoutingStopModel stop = (IRoutingStopModel)this.getStops().first();
			return stop.getStopDepartureTime();
		}
		return null;
	}

	@Override
	public Date getReturnToBuildingTime() {
		
		return this.getCompletionTime();
	}

	@Override
	public String getServiceTimeInfo() {
		
		return RoutingStringUtil.calcHM((int)getServiceTime());
	}

	@Override
	public String getTravelTimeInfo() {
		return RoutingStringUtil.calcHM((int)getTravelTime());
	}
		
}
