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
	private String dayOfWeekStr;
	private Date normalDate;
	private String normalDateStr;
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
	
	public DlvServiceTimeScenario getScenario() {
		return scenario;
	}
	public void setScenario(DlvServiceTimeScenario scenario) {
		this.scenario = scenario;
	}
	public String getNormalDateStr() {
		return normalDateStr;
	}
	public Date getNormalDate() {
		return normalDate;
	}
	public void setNormalDate(Date normalDate) {
		this.normalDate = normalDate;
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		String date = formatter.format(normalDate);
		this.normalDateStr= date;
	}
	
	
	public String getDayOfWeekInText(){
		if(this==null) return "";

		EnumDayOfWeek dow=EnumDayOfWeek.getEnum(Integer.toString(this.getDayOfWeek().intValue()));
		if(dow!=null)
			return dow.getDesc();
		else
			return "";
	}
	
}
