package com.freshdirect.routing.model;

import java.util.Date;

import com.freshdirect.routing.util.RoutingStringUtil;

public class HandOffBatchTrailer extends TrailerModel implements IHandOffBatchTrailer  {
	
	private String batchId;	
	
	
	public HandOffBatchTrailer(ITrailerModel refModel) {
		super();
		this.setTrailerId(refModel.getTrailerId());
		this.setStartTime(refModel.getStartTime());
		this.setDispatchTime(refModel.getDispatchTime());
		this.setRoutes(refModel.getRoutes());
		this.setDrivingDirection(refModel.getDrivingDirection());
		this.setCompletionTime(refModel.getCompletionTime());
		this.setRoutingTrailerId(refModel.getRoutingTrailerId());		
		this.setDistance(refModel.getDistance());
		this.setTravelTime(refModel.getTravelTime());
		this.setServiceTime(refModel.getServiceTime());		
		this.setMaxRunTime(refModel.getMaxRunTime());
		this.setPreferredRunTime(refModel.getPreferredRunTime());		
	}
	
	public HandOffBatchTrailer() {
		super();
		
	}
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}	
	
	@Override
	public Date getTrailerDispatchTime(){
		if(this.getDispatchTime() != null) {					
			return this.getDispatchTime().getAsDate();
		} else {
			return null;
		}
	}
	
	@Override
	public String getCrossDockId(){
		return this.getOriginId();
	}
}
