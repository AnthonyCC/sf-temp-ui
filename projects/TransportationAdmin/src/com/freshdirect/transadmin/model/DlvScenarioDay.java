package com.freshdirect.transadmin.model;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import com.freshdirect.transadmin.util.EnumDayOfWeek;

public class DlvScenarioDay implements java.io.Serializable{
	
	private DlvScenarioDaysId scenariodaysId = new DlvScenarioDaysId();
	
	private Date normalDate;
	private String normalDateStr;
	private DlvServiceTimeScenario scenario;
	
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
	public DlvScenarioDaysId getScenariodaysId() {
		return scenariodaysId;
	}
	public void setScenariodaysId(DlvScenarioDaysId scenariodaysId) {
		this.scenariodaysId = scenariodaysId;
	}
	
	public DlvScenarioDay(DlvScenarioDaysId scenariodaysId, Date normalDate,
			DlvServiceTimeScenario scenario) {
		super();
		this.scenariodaysId = scenariodaysId;
		this.normalDate = normalDate;
		this.scenario = scenario;
	}
	public DlvScenarioDay() {
		
	}
	public DlvScenarioDay(DlvScenarioDaysId Id) {
		this.scenariodaysId = Id;
	}
	
	public String getDayOfWeekInText(){
		if(this.scenariodaysId==null) return "";

		EnumDayOfWeek dow=EnumDayOfWeek.getEnum(Integer.toString(scenariodaysId.getDayOfWeek().intValue()));
		if(dow!=null)
			return dow.getDesc();
		else
			return "";
	}
	
	
	
}
