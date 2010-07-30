package com.freshdirect.fdstore.promotion.management;

import com.freshdirect.framework.core.ModelSupport;

public class FDPromoDlvTimeSlotModel extends ModelSupport {

	private String promoDlvZoneId;
	private Integer dayId;
	private String dlvTimeStart;
	private String dlvTimeEnd;
	
	public String getPromoDlvZoneId() {
		return promoDlvZoneId;
	}
	public void setPromoDlvZoneId(String promoDlvZoneId) {
		this.promoDlvZoneId = promoDlvZoneId;
	}
	public Integer getDayId() {
		return dayId;
	}
	public void setDayId(Integer dayId) {
		this.dayId = dayId;
	}
	public String getDlvTimeStart() {
		return dlvTimeStart;
	}
	public void setDlvTimeStart(String dlvTimeStart) {
		this.dlvTimeStart = dlvTimeStart;
	}
	public String getDlvTimeEnd() {
		return dlvTimeEnd;
	}
	public void setDlvTimeEnd(String dlvTimeEnd) {
		this.dlvTimeEnd = dlvTimeEnd;
	}
	@Override
	public String toString() {
		if(null!=dlvTimeStart && null != dlvTimeEnd){
			return "Start Time:"+dlvTimeStart+" End Time:"+dlvTimeEnd;
		}
		return super.toString();
		
	}
	
	
	
}
