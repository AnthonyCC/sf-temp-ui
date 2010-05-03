package com.freshdirect.transadmin.model;

public class BuildingOperationHoursId implements java.io.Serializable, TrnBaseEntityI {

	private String deliveryBuildingId="";
	private String dayOfWeek="";
	
	
	public BuildingOperationHoursId() {
	}

	public BuildingOperationHoursId(String deliveryBuildingId, String dayOfWeek) {
		this.deliveryBuildingId = deliveryBuildingId;
		this.dayOfWeek = dayOfWeek;
	}
	
	public String getDeliveryBuildingId() {
		return deliveryBuildingId;
	}
	public void setDeliveryBuildingId(String deliveryBuildingId) {
		this.deliveryBuildingId = deliveryBuildingId;
	}
	public String getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	@Override
	public boolean isObsoleteEntity() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof BuildingOperationHoursId))
			return false;
		BuildingOperationHoursId castOther = (BuildingOperationHoursId) other;

		return this.getDayOfWeek().equals(castOther.getDayOfWeek()) &&
		       this.getDeliveryBuildingId().equals(castOther.getDeliveryBuildingId());
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getDayOfWeek() == null ? 0 : this.getDayOfWeek()
						.hashCode());
		result = 37 * result
				+ (getDeliveryBuildingId() == null ? 0 : this.getDeliveryBuildingId().hashCode());
		return result;
	}
}
