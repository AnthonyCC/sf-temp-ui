package com.freshdirect.fdlogistics.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.freshdirect.logistics.delivery.model.DlvZoneModel;

public class FDDeliveryTimeslots implements Serializable {
		
	
		private List<FDTimeslotList> timeslotList = new ArrayList<FDTimeslotList>();
		private Map<String, DlvZoneModel> zones;
		private List<String> geoRestrictionmessages;
		private Date sameDayCutoff;
		private boolean ctActive = false;
		private int ctSlots = 0;
		private int ecoFriendlySlots = 0;
		private int neighbourhoodSlots = 0;
		private double soldoutSlots = 0.0D;
		private double totalSlots = 0.0D;
		private boolean hasCapacity = true;
		String preselectedTimeslotId = null;
		private List<String> comments;

		public Date getSameDayCutoff() {
			return this.sameDayCutoff;
		}

		public void setSameDayCutoff(Date sameDayCutoff) {
			this.sameDayCutoff = sameDayCutoff;
		}

		public Map<String, DlvZoneModel> getZones() {
			return this.zones;
		}

		public void setZones(Map<String, DlvZoneModel> zones) {
			this.zones = zones;
		}

		public List<String> getGeoRestrictionmessages() {
			return this.geoRestrictionmessages;
		}

		public void setGeoRestrictionmessages(List<String> geoRestrictionmessages) {
			this.geoRestrictionmessages = geoRestrictionmessages;
		}

		public List<String> getComments() {
			return this.comments;
		}

		public void setComments(List<String> comments) {
			this.comments = comments;
		}

		public boolean isCtActive() {
			return this.ctActive;
		}

		public void setCtActive(boolean ctActive) {
			this.ctActive = ctActive;
		}

		public int getCtSlots() {
			return this.ctSlots;
		}

		public void setCtSlots(int ctSlots) {
			this.ctSlots = ctSlots;
		}

		public int getEcoFriendlySlots() {
			return this.ecoFriendlySlots;
		}

		public void setEcoFriendlySlots(int ecoFriendlySlots) {
			this.ecoFriendlySlots = ecoFriendlySlots;
		}

		public int getNeighbourhoodSlots() {
			return this.neighbourhoodSlots;
		}

		public void setNeighbourhoodSlots(int neighbourhoodSlots) {
			this.neighbourhoodSlots = neighbourhoodSlots;
		}

		public double getTotalSlots() {
			return this.totalSlots;
		}

		public void setTotalSlots(double totalSlots) {
			this.totalSlots = totalSlots;
		}

		public double getSoldoutSlots() {
			return this.soldoutSlots;
		}

		public void setSoldoutSlots(double soldoutSlots) {
			this.soldoutSlots = soldoutSlots;
		}

		public boolean isHasCapacity() {
			return this.hasCapacity;
		}

		public void setHasCapacity(boolean hasCapacity) {
			this.hasCapacity = hasCapacity;
		}

		public String getPreselectedTimeslotId() {
			return this.preselectedTimeslotId;
		}

		public void setPreselectedTimeslotId(String preselectedTimeslotId) {
			this.preselectedTimeslotId = preselectedTimeslotId;
		}

		public List<FDTimeslotList> getTimeslotList() {
			return this.timeslotList;
		}

		public void setTimeslotList(List<FDTimeslotList> timeslotList) {
			this.timeslotList = timeslotList;
		}
}

