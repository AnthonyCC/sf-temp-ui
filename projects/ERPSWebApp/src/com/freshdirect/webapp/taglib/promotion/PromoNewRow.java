package com.freshdirect.webapp.taglib.promotion;

import java.util.Date;

import com.freshdirect.fdstore.promotion.EnumPromotionStatus;
import com.freshdirect.fdstore.promotion.EnumPromotionType;

public class PromoNewRow {

	String id;
	String code;
	String redemptionCode = "";
	String name;
	String description;
	double amount;
	Date start;
	Date expire;
	EnumPromotionType type;
	EnumPromotionStatus status;
	String createdBy;
	String modifiedBy;
	String createdModifiedBy;
	
	boolean chef;
	boolean cos;
	boolean newCust;
	boolean cosNew;
	boolean dp;
	boolean mktg;

	public double getAmount() {
		return amount;
	}

	public EnumPromotionStatus getStatus() {
		return status;
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

	public void setId(String id) {
		this.id = id;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setRedemptionCode(String redemptionCode) {
		this.redemptionCode = redemptionCode;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public void setExpire(Date expire) {
		this.expire = expire;
	}

	public void setType(EnumPromotionType type) {
		this.type = type;
	}

	public void setStatus(EnumPromotionStatus status) {
		this.status = status;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getCreatedModifiedBy() {
		return createdModifiedBy;
	}

	public void setCreatedModifiedBy(String createdModifiedBy) {
		this.createdModifiedBy = createdModifiedBy;
	}

	public boolean isChef() {
		return chef;
	}

	public void setChef(boolean chef) {
		this.chef = chef;
	}

	public boolean isCos() {
		return cos;
	}

	public void setCos(boolean cos) {
		this.cos = cos;
	}

	public boolean isNewCust() {
		return newCust;
	}

	public void setNewCust(boolean newCust) {
		this.newCust = newCust;
	}

	public boolean isCosNew() {
		return cosNew;
	}

	public void setCosNew(boolean cosNew) {
		this.cosNew = cosNew;
	}

	public boolean isDp() {
		return dp;
	}

	public void setDp(boolean dp) {
		this.dp = dp;
	}

	public boolean isMktg() {
		return mktg;
	}

	public void setMktg(boolean mktg) {
		this.mktg = mktg;
	}

	
}
