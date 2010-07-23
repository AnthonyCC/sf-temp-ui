package com.freshdirect.fdstore.promotion;

import java.util.Date;

import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.core.PrimaryKey;

public class PromotionDlvTimeSlot extends ModelSupport {

	private Integer dayId;
	private String dlvTimeStart;
	private String dlvTimeEnd;
	
	public PromotionDlvTimeSlot(Integer dayId, String dlvTimeStart,
			String dlvTimeEnd) {
		super();
		this.dayId = dayId;
		this.dlvTimeStart = dlvTimeStart;
		this.dlvTimeEnd = dlvTimeEnd;
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
	
	
	
	
}
