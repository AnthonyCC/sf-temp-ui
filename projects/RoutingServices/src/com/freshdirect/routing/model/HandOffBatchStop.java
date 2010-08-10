package com.freshdirect.routing.model;

import java.util.Date;


public class HandOffBatchStop extends RoutingStopModel implements IHandOffBatchStop  {
	
	private String batchId;
	
	private String sessionName;
	
	private String routeId;
	
	
	public HandOffBatchStop(IRoutingStopModel refModel) {
		super();
		this.setDepot(refModel.isDepot());		
		this.setStopNo(refModel.getStopNo());
		this.setStopArrivalTime(refModel.getStopArrivalTime());
		this.setStopDepartureTime(refModel.getStopDepartureTime());
		
		this.setOrderNumber(refModel.getOrderNumber());
		this.setErpOrderNumber(refModel.getErpOrderNumber());
		this.setCustomerName(refModel.getCustomerName());
		this.setCustomerNumber(refModel.getCustomerNumber());
		this.setDeliveryInfo(refModel.getDeliveryInfo());
		this.setRoutingRouteId(refModel.getRoutingRouteId());
	}
	
	public HandOffBatchStop() {
		super();
		
	}
	
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	public String getSessionName() {
		return sessionName;
	}
	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}
	public String getRouteId() {
		return routeId;
	}
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	@Override
	public Date getBuildingCloseTime() {
		if(this.getDeliveryInfo() != null
				&& this.getDeliveryInfo().getDeliveryLocation() != null
				 	&& this.getDeliveryInfo().getDeliveryLocation().getBuilding() != null 
				 	&& this.getDeliveryInfo().getDeliveryLocation().getBuilding().getOperationDetails() != null) {
			if(this.getDeliveryInfo().getDeliveryLocation().getBuilding().getOperationDetails().size() > 0) {
				IBuildingOperationDetails detail = this.getDeliveryInfo().getDeliveryLocation()
														.getBuilding().getOperationDetails().iterator().next();
				return detail.getBldgEndHour();
			}
		}
		return null;
	}

	@Override
	public Date getBuildingOpenTime() {
		if(this.getDeliveryInfo() != null
				&& this.getDeliveryInfo().getDeliveryLocation() != null
				 	&& this.getDeliveryInfo().getDeliveryLocation().getBuilding() != null 
				 	&& this.getDeliveryInfo().getDeliveryLocation().getBuilding().getOperationDetails() != null) {
			if(this.getDeliveryInfo().getDeliveryLocation().getBuilding().getOperationDetails().size() > 0) {
				IBuildingOperationDetails detail = this.getDeliveryInfo().getDeliveryLocation()
														.getBuilding().getOperationDetails().iterator().next();
				return detail.getBldgStartHour();
			}
		}
		return null;
	}

	@Override
	public String getBuildingType() {
		
		if(this.getDeliveryInfo() != null
				&& this.getDeliveryInfo().getDeliveryLocation() != null
				 	&& this.getDeliveryInfo().getDeliveryLocation().getBuilding() != null ) {
						
			if(this.getDeliveryInfo().getDeliveryLocation().getBuilding().isHouse()) {
				return "House";
			}
			
			if(this.getDeliveryInfo().getDeliveryLocation().getBuilding().isSvcEnt()) {
				return "Service Entrance";
			}
			
			if(this.getDeliveryInfo().getDeliveryLocation().getBuilding().isWalkup()) {
				return  "Walkup";
			}
			
			if(this.getDeliveryInfo().getDeliveryLocation().getBuilding().isDoorman()) {
				return  "Doorman";
			}
			
			if(this.getDeliveryInfo().getDeliveryLocation().getBuilding().isElevator()) {
				return  "Elevator";
			}
			
			if(this.getDeliveryInfo().getDeliveryLocation().getBuilding().isFreightElevator()) {
				return  "Freight Elevator";
			}
		}
		return null;
	}
	

	@Override
	public String getCrossStreet() {
		if(this.getDeliveryInfo() != null
				&& this.getDeliveryInfo().getDeliveryLocation() != null
				 	&& this.getDeliveryInfo().getDeliveryLocation().getBuilding() != null ) {
			return this.getDeliveryInfo().getDeliveryLocation().getBuilding().getCrossStreet();
		}
		return null;
	}

	@Override
	public String getCrossStreet2() {
		if(this.getDeliveryInfo() != null
				&& this.getDeliveryInfo().getDeliveryLocation() != null
				 	&& this.getDeliveryInfo().getDeliveryLocation().getBuilding() != null ) {
			return this.getDeliveryInfo().getDeliveryLocation().getBuilding().getSvcCrossStreet();
		}
		return null;
	}
	
	
	@Override
	public String getDifficultReason() {
		if(this.getDeliveryInfo() != null
				&& this.getDeliveryInfo().getDeliveryLocation() != null
				 	&& this.getDeliveryInfo().getDeliveryLocation().getBuilding() != null ) {
			return this.getDeliveryInfo().getDeliveryLocation().getBuilding().getDifficultReason();
		}
		return null;
	}

	@Override
	public String getServiceEntrance() {
		if(this.getDeliveryInfo() != null
				&& this.getDeliveryInfo().getDeliveryLocation() != null
				 	&& this.getDeliveryInfo().getDeliveryLocation().getBuilding() != null ) {
			return this.getDeliveryInfo().getDeliveryLocation().getBuilding().getSvcScrubbedStreet();
		}
		return null;
	}
	
	@Override
	public String getDeliveryInstructionA() {
		if(this.getDeliveryInfo() != null
				&& this.getDeliveryInfo().getDeliveryLocation() != null
				 	&& this.getDeliveryInfo().getDeliveryLocation().getBuilding() != null ) {
			if(this.getDeliveryInfo().getDeliveryLocation().getBuilding().isHandTruckAllowed()) {
				return "Hand Trucks Allowed via Main Entrance";
			}
		}
		return null;
	}
	
	@Override
	public String getDeliveryInstructionB() {
		if(this.getDeliveryInfo() != null
				&& this.getDeliveryInfo().getDeliveryLocation() != null
				 	&& this.getDeliveryInfo().getDeliveryLocation().getBuilding() != null ) {
			return this.getDeliveryInfo().getDeliveryLocation().getBuilding().getOther();
		}
		return null;
	}

	@Override
	public Date getServiceEntranceCloseTime() {
		if(this.getDeliveryInfo() != null
				&& this.getDeliveryInfo().getDeliveryLocation() != null
				 	&& this.getDeliveryInfo().getDeliveryLocation().getBuilding() != null 
				 	&& this.getDeliveryInfo().getDeliveryLocation().getBuilding().getOperationDetails() != null) {
			if(this.getDeliveryInfo().getDeliveryLocation().getBuilding().getOperationDetails().size() > 0) {
				IBuildingOperationDetails detail = this.getDeliveryInfo().getDeliveryLocation()
														.getBuilding().getOperationDetails().iterator().next();
				return detail.getServiceEndHour();
			}
		}
		return null;
	}

	@Override
	public Date getServiceEntranceOpenTime() {
		if(this.getDeliveryInfo() != null
				&& this.getDeliveryInfo().getDeliveryLocation() != null
				 	&& this.getDeliveryInfo().getDeliveryLocation().getBuilding() != null 
				 	&& this.getDeliveryInfo().getDeliveryLocation().getBuilding().getOperationDetails() != null) {
			if(this.getDeliveryInfo().getDeliveryLocation().getBuilding().getOperationDetails().size() > 0) {
				IBuildingOperationDetails detail = this.getDeliveryInfo().getDeliveryLocation()
														.getBuilding().getOperationDetails().iterator().next();
				return detail.getServiceStartHour();
			}
		}
		return null;
	}

	@Override
	public String getServiceType() {
		if(this.getDeliveryInfo() != null) {
			return this.getDeliveryInfo().getServiceType();
		}
		return null;
	}
	
}
