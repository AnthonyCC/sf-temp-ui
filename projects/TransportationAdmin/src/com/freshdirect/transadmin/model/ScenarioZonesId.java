package com.freshdirect.transadmin.model;

public class ScenarioZonesId implements java.io.Serializable{
	
	private String scenarioId;	
	private String zoneCode;
	
	public String getScenarioId() {
		return scenarioId;
	}
	public void setScenarioId(String scenarioId) {
		this.scenarioId = scenarioId;
	}
	public String getZoneCode() {
		return zoneCode;
	}
	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}
	
	public ScenarioZonesId(String scenarioId, String zoneCode) {
		super();
		this.scenarioId = scenarioId;
		this.zoneCode = zoneCode;
	}
	public ScenarioZonesId() {
		// TODO Auto-generated constructor stub
	}
	
	
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ScenarioZonesId))
			return false;
		ScenarioZonesId castOther = (ScenarioZonesId) other;

		return ((this.getScenarioId() == castOther.getScenarioId()) || (this
				.getScenarioId() != null
				&& castOther.getScenarioId() != null && this
				.getScenarioId().equals(castOther.getScenarioId())))
				&& ((this.getZoneCode() == castOther.getZoneCode()) || (this
						.getZoneCode() != null
						&& castOther.getZoneCode() != null && this
						.getZoneCode().equals(castOther.getZoneCode())));
	}
			

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getScenarioId() == null ? 0 : this.getScenarioId()
						.hashCode());
		result = 37 * result
				+ (getZoneCode() == null ? 0 : this.getZoneCode().hashCode());
		return result;
	}

	
	

}
