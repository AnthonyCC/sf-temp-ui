package com.freshdirect.fdstore.promotion;

import java.util.Date;

import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.core.PrimaryKey;

public class PromotionDlvTimeSlot extends ModelSupport {

	private Integer dayId;
	private String dlvTimeStart;
	private String dlvTimeEnd;
	private String[] windowTypes;
	private String forWindowTime;
	private Integer cutOffExpTime;
	
	public PromotionDlvTimeSlot(Integer dayId, String dlvTimeStart,
			String dlvTimeEnd, String[] windowTypes, String forWindowTime, int cutOffExpTime) {
		super();
		this.dayId = dayId;
		this.dlvTimeStart = dlvTimeStart;
		this.dlvTimeEnd = dlvTimeEnd;
		this.windowTypes = windowTypes;
		this.forWindowTime = forWindowTime;
		this.cutOffExpTime = cutOffExpTime;
	}
	
	public PromotionDlvTimeSlot() {
		super();
		// TODO Auto-generated constructor stub
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
	public String[] getWindowTypes() {
		return windowTypes;
	}
	public void setWindowTypes(String[] windowTypes) {
		this.windowTypes = windowTypes;
	}

	public Integer getCutOffExpTime() {
		return cutOffExpTime;
	}

	public void setCutOffExpTime(Integer cutOffExpTime) {
		this.cutOffExpTime = cutOffExpTime;
	}

	public String getForWindowTime() {
		return forWindowTime;
	}

	public void setForWindowTime(String forWindowTime) {
		this.forWindowTime = forWindowTime;
	}	
}
