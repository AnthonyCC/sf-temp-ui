package com.freshdirect.fdstore.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.freshdirect.delivery.model.DlvZoneModel;
import com.freshdirect.fdstore.customer.FDDeliveryTimeslotModel;
import com.freshdirect.fdstore.customer.FDUserI;

public class DlvTimeslotStats {
	List<String> messages = new ArrayList<String>();
	List<String> comments = new ArrayList<String>();

	boolean hasCapacity = true;
	boolean isKosherSlotAvailable = false;
	boolean ctActive = false;
	double maxDiscount = 0.0;
	int ctSlots = 0;
	int alcoholSlots = 0;
	int ecoFriendlySlots = 0;
	int neighbourhoodSlots = 0;
	double soldOut = 0.0;
	double totalSlots = 0.0;

	HashMap<String, DlvZoneModel> zonesMap = new HashMap<String, DlvZoneModel>();
	boolean isAlcoholDelivery = false;
	
	public boolean isAlcoholDelivery() {
		return isAlcoholDelivery;
	}
	
	public void setAlcoholDelivery(boolean isAlcoholDelivery) {
		this.isAlcoholDelivery = isAlcoholDelivery;
	}
	
	public List<String> getMessages() {
		return messages;
	}
	
	public List<String> getComments() {
		return comments;
	}
	
	public HashMap<String, DlvZoneModel> getZonesMap() {
		return zonesMap;
	}
	
	public void setZonesMap(HashMap<String, DlvZoneModel> zonesMap) {
		this.zonesMap = zonesMap;
	}

	public void setCtActive(boolean ctActive) {
		this.ctActive = ctActive;
	}
	
	public void incrementCtSlots() {
		ctSlots++;
	}

	public void incrementAlcoholSlots() {
		alcoholSlots++;
	}

	public void incrementEcoFriendlySlots() {
		ecoFriendlySlots++;
	}
	
	public void incrementNeighbourhoodSlots() {
		neighbourhoodSlots++;
	}
	
	public void incrementSoldOutSlots() {
		soldOut++;
	}
	
	public void incrementTotalSlots() {
		totalSlots++;
	}
	
	public void setMaximumDiscount(double discount) {
		if (discount > maxDiscount)
			maxDiscount = discount;
	}
	
	public void updateKosherSlotAvailable(boolean flag) {
		isKosherSlotAvailable = isKosherSlotAvailable || flag;
	}
	
	public void updateHasCapacity(boolean flag) {
		hasCapacity = hasCapacity || flag;
	}
	
	
	/**
	 * Apply stat results to delivery model
	 * 
	 * @param deliveryModel
	 */
	public void apply(FDDeliveryTimeslotModel deliveryModel) {
		deliveryModel.setAlcoholDelivery(isAlcoholDelivery);
		// deliveryModel.setTimeslotList(timeslotList);
		deliveryModel.setZones(zonesMap);
		deliveryModel.setZoneCtActive(ctActive);
		deliveryModel.setHasCapacity(hasCapacity);
		deliveryModel.setKosherSlotAvailable(isKosherSlotAvailable);
		deliveryModel.setGeoRestrictionmessages(messages);
		deliveryModel.setMaxDiscount(maxDiscount);
		deliveryModel.setAlcoholRestrictedCount(alcoholSlots);
		deliveryModel.setEcoFriendlyCount(ecoFriendlySlots);
		deliveryModel.setNeighbourhoodCount(neighbourhoodSlots);
		deliveryModel.setPercSlotsSold(totalSlots > 0 ? Math
				.round((soldOut / totalSlots) * 100) : 0.0);
	}

	/**
	 * Update chefs table stats in user
	 * @param user
	 */
	public void apply(FDUserI user) {
		if (user != null) {
			user.setTotalCTSlots(ctSlots);
			user.setPercSlotsSold(totalSlots > 0 ? Math
					.round((soldOut / totalSlots) * 100) : 0.0);
		}
	}
}
