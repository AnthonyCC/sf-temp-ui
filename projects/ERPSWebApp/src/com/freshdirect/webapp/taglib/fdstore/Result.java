package com.freshdirect.webapp.taglib.fdstore;

import java.util.List;
import java.util.Map;

import com.freshdirect.fdstore.customer.FDDeliveryTimeslotModel;
import com.freshdirect.framework.webapp.ActionResult;

public class Result extends ActionResult {
	
	List timeslots;
	Map zones;
	boolean zoneCtActive = false;
	List messages;
	List comments;
	private FDDeliveryTimeslotModel deliveryTimeslotModel;
	
	Result(){}
	
	Result(FDDeliveryTimeslotModel timeslotModel){
		this.deliveryTimeslotModel = timeslotModel;
	}
	
	public List getComments() {
		if(this.deliveryTimeslotModel != null)
			comments = deliveryTimeslotModel.getComments();
		return comments;
	}
	
	public List getTimeslots() {
		if(this.deliveryTimeslotModel != null)
			timeslots = deliveryTimeslotModel.getTimeslotList();
		return timeslots;
	}
	public Map getZones() {
		if(this.deliveryTimeslotModel != null)
			zones = deliveryTimeslotModel.getZones();
		return zones;
	}
	public List getMessages() {
		if(this.deliveryTimeslotModel != null)
			messages = deliveryTimeslotModel.getMessages();
		return messages;
	}
	
	public boolean isZoneCtActive() {
		if(this.deliveryTimeslotModel != null)
			zoneCtActive = deliveryTimeslotModel.isZoneCtActive();
		return zoneCtActive;
	}
	
	public FDDeliveryTimeslotModel getDeliveryTimeslotModel() {
		return deliveryTimeslotModel;
	}
}