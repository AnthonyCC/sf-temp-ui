package com.freshdirect.fdstore.promotion;

import java.util.Date;

import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.core.PrimaryKey;

public class PromotionDlvDay extends ModelSupport {

	private Integer dayId;
	private Integer redeemCnt;
	private double capacityUtilization;
	
	public PromotionDlvDay(Integer dayId, Integer redeemCnt, double capacityUtilization) {
		super();
		this.dayId = dayId;
		this.redeemCnt = redeemCnt;
		this.capacityUtilization = capacityUtilization;
	}
	
	public PromotionDlvDay() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getDayId() {
		return dayId;
	}
	public void setDayId(Integer dayId) {
		this.dayId = dayId;
	}

	public Integer getRedeemCnt() {
		return redeemCnt;
	}

	public void setRedeemCnt(Integer redeemCnt) {
		this.redeemCnt = redeemCnt;
	}

	public double getCapacityUtilization() {
		return capacityUtilization;
	}

	public void setCapacityUtilization(double capacityUtilization) {
		this.capacityUtilization = capacityUtilization;
	}
	
}
