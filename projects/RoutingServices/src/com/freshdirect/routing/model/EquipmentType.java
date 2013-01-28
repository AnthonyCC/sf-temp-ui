package com.freshdirect.routing.model;

import java.io.Serializable;

public class EquipmentType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9045947825949243590L;
	private String id;
	private String description;
	private Double fuelConsumption;
	private Double height;
	private Double weight;
	private boolean travelRestrictions;
	private boolean rushHourRestrictions;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getFuelConsumption() {
		return fuelConsumption;
	}
	public void setFuelConsumption(Double fuelConsumption) {
		this.fuelConsumption = fuelConsumption;
	}
	public Double getHeight() {
		return height;
	}
	public void setHeight(Double height) {
		this.height = height;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public boolean isTravelRestrictions() {
		return travelRestrictions;
	}
	public void setTravelRestrictions(boolean travelRestrictions) {
		this.travelRestrictions = travelRestrictions;
	}
	public boolean isRushHourRestrictions() {
		return rushHourRestrictions;
	}
	public void setRushHourRestrictions(boolean rushHourRestrictions) {
		this.rushHourRestrictions = rushHourRestrictions;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
