package com.freshdirect.transadmin.web.model;

import java.text.ParseException;
import com.freshdirect.transadmin.model.ZoneSupervisor;
import com.freshdirect.transadmin.util.TransStringUtil;

public class ZoneSupervisorCommand implements java.io.Serializable{	
		
	private String dayPart;
	private String supervisorId;
	private String effectiveDate;
	private String supervisorName;
	
	public ZoneSupervisorCommand(ZoneSupervisor src) {
		
		this.dayPart  = src.getDayPart()!=null ? src.getDayPart(): "";		
		this.supervisorId = src.getSupervisorId()!=null
								    ? src.getSupervisorId():"";
		try {
			this.effectiveDate = src.getEffectiveDate() != null 
										? TransStringUtil.getDate(src.getEffectiveDate()) : "";
		} catch (ParseException e) {			
			e.printStackTrace();
		}
		this.supervisorName = src.getSupervisorName()!=null ? src.getSupervisorName():"";
	}
	
	public String getSupervisorName() {
		return supervisorName;
	}

	public void setSupervisorName(String supervisorName) {
		this.supervisorName = supervisorName;
	}
	
	public String getDayPart() {
		return dayPart;
	}
	public void setDayPart(String dayPart) {
		this.dayPart = dayPart;
	}
	public String getSupervisorId() {
		return supervisorId;
	}
	public void setSupervisorId(String supervrisorId) {
		this.supervisorId = supervisorId;
	}
	public String getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	
}
