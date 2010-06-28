package com.freshdirect.transadmin.model;

import java.math.BigDecimal;

public class DlvScenarioDaysId implements java.io.Serializable{
	
	private String scenarioId;
	private BigDecimal dayOfWeek;
	private String dayOfWeekStr;
	
	public String getDayOfWeekStr() {
		return dayOfWeekStr;
	}


	public BigDecimal getDayOfWeek() {
		return dayOfWeek;
	}
	
	
	public void setDayOfWeek(BigDecimal dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
		this.dayOfWeekStr=dayOfWeek.toString();
	}
	public String getScenarioId() {
		return scenarioId;
	}
	public void setScenarioId(String scenarioId) {
		this.scenarioId = scenarioId;
	}
	
	public DlvScenarioDaysId(String scenarioId, BigDecimal dayOfWeek) {
		super();
		this.scenarioId = scenarioId;
		this.dayOfWeek = dayOfWeek;
	}
	
	public DlvScenarioDaysId() {
	
	}
	
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof DlvScenarioDaysId))
			return false;
		DlvScenarioDaysId castOther = (DlvScenarioDaysId) other;

		return ((this.getScenarioId() == castOther.getScenarioId()) || (this
				.getScenarioId() != null
				&& castOther.getScenarioId() != null && this
				.getScenarioId().equals(castOther.getScenarioId())))
				&& ((this.getDayOfWeek() == castOther.getDayOfWeek()) || (this
						.getDayOfWeek() != null
						&& castOther.getDayOfWeek() != null && this
						.getDayOfWeek().equals(castOther.getDayOfWeek())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getScenarioId() == null ? 0 : this.getScenarioId()
						.hashCode());
		result = 37 * result
				+ (getDayOfWeek() == null ? 0 : this.getDayOfWeek().hashCode());
		
		return result;
	}

	
	
}
