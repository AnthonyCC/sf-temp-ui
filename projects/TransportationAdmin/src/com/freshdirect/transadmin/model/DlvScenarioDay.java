package com.freshdirect.transadmin.model;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import com.freshdirect.transadmin.util.EnumDayOfWeek;

public class DlvScenarioDay implements java.io.Serializable{
	
	private String scenariodayId;
	private BigDecimal dayOfWeek;
	
	private Date normalDate;
	
	private DlvServiceTimeScenario scenario;	
	
	public DlvScenarioDay() {
	}
	
	public DlvScenarioDay(BigDecimal dayOfWeek,
			Date normalDate, DlvServiceTimeScenario scenario) {
		super();
		this.dayOfWeek = dayOfWeek;
		this.normalDate = normalDate;
		this.scenario = scenario;
	}
	
	
	public String getScenariodayId() {
		return scenariodayId;
	}
	public void setScenariodayId(String scenariodayId) {
		this.scenariodayId = scenariodayId;
	}
	
	public BigDecimal getDayOfWeek() {
		return dayOfWeek;
	}	
	
	public void setDayOfWeek(BigDecimal dayOfWeek) {
		this.dayOfWeek = dayOfWeek;	
	}	
	
	public DlvServiceTimeScenario getScenario() {
		return scenario;
	}
	public void setScenario(DlvServiceTimeScenario scenario) {
		this.scenario = scenario;
	}
	
	public Date getNormalDate() {
		return normalDate;
	}
	public void setNormalDate(Date normalDate) {
		this.normalDate = normalDate;		
	}
	
	
	public String getDayOfWeekInText(){
		if(this==null) return "";

		EnumDayOfWeek dow=EnumDayOfWeek.getEnum(Integer.toString(this.getDayOfWeek().intValue()));
		if(dow!=null)
			return dow.getDesc();
		else
			return "";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dayOfWeek == null) ? 0 : dayOfWeek.hashCode());
		result = prime * result
				+ ((normalDate == null) ? 0 : normalDate.hashCode());
		result = prime * result
				+ ((scenariodayId == null) ? 0 : scenariodayId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DlvScenarioDay other = (DlvScenarioDay) obj;
		if (dayOfWeek == null) {
			if (other.dayOfWeek != null)
				return false;
		} else if (!dayOfWeek.equals(other.dayOfWeek))
			return false;
		if (normalDate == null) {
			if (other.normalDate != null)
				return false;
		} else if (!normalDate.equals(other.normalDate))
			return false;
		if (scenariodayId == null) {
			if (other.scenariodayId != null)
				return false;
		} else if (!scenariodayId.equals(other.scenariodayId))
			return false;
		return true;
	}	
}
