package com.freshdirect.transadmin.model;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import com.freshdirect.transadmin.util.EnumDayOfWeek;
import com.freshdirect.transadmin.util.TransStringUtil;

public class DlvScenarioDay implements java.io.Serializable{
	
	private String scenariodayId;
	private BigDecimal dayOfWeek;
	
	private Date normalDate;
	private Date cutOff;
	private Date startTime;
	private Date endTime;
	
	
	private DlvServiceTimeScenario scenario;	
	
	public DlvScenarioDay() {
	}
	
	public DlvScenarioDay(BigDecimal dayOfWeek,
			Date normalDate, DlvServiceTimeScenario scenario, Date cutOff, Date startTime, Date endTime) {
		super();
		this.dayOfWeek = dayOfWeek;
		this.normalDate = normalDate;
		this.scenario = scenario;
		this.cutOff = cutOff;
		this.startTime = startTime;
		this.endTime = endTime;
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
		if (this == null)
			return "";

		if (this.getDayOfWeek() != null) {
			EnumDayOfWeek dow = EnumDayOfWeek.getEnum(Integer.toString(this
					.getDayOfWeek().intValue()));
			if (dow != null)
				return dow.getDesc();
		}
		return "";
	}

	public String getTimeRange(){
		if(this.startTime == null || this.endTime == null) return "";
		
		try{
			return TransStringUtil.getServerTime(startTime)+"-"+TransStringUtil.getServerTime(endTime);
		}catch(ParseException pe){
			return "";
		}
	}
	public String getCutoffEx(){
		if(this.cutOff == null ) return "";
		
		try{
			return TransStringUtil.getServerTime(cutOff);
		}catch(ParseException pe){
			return "";
		}
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cutOff == null) ? 0 : cutOff.hashCode());
		result = prime * result
				+ ((dayOfWeek == null) ? 0 : dayOfWeek.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result
				+ ((normalDate == null) ? 0 : normalDate.hashCode());
		result = prime * result
				+ ((scenariodayId == null) ? 0 : scenariodayId.hashCode());
		result = prime * result
				+ ((startTime == null) ? 0 : startTime.hashCode());
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
		if (cutOff == null) {
			if (other.cutOff != null)
				return false;
		} else if (!cutOff.equals(other.cutOff))
			return false;
		if (dayOfWeek == null) {
			if (other.dayOfWeek != null)
				return false;
		} else if (!dayOfWeek.equals(other.dayOfWeek))
			return false;
		if (endTime == null) {
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
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
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		return true;
	}

	public Date getCutOff() {
		return cutOff;
	}

	public void setCutOff(Date cutOff) {
		this.cutOff = cutOff;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}	
}
