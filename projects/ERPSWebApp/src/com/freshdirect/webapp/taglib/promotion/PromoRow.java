package com.freshdirect.webapp.taglib.promotion;

import java.util.Date;

import com.freshdirect.fdstore.promotion.EnumPromotionType;

public class PromoRow {
	String id;
	String code;
	String redemptionCode = "";
	String name;
	String description;
	double amount;
	Date start;
	Date expire;
	String zone;
	EnumPromotionType type;

	public double getAmount() {
		return amount;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public Date getExpire() {
		return expire;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getRedemptionCode() {
		return redemptionCode;
	}

	public Date getStart() {
		return start;
	}

	public EnumPromotionType getType() {
		return type;
	}

	public String getZone() {
		return zone;
	}
}