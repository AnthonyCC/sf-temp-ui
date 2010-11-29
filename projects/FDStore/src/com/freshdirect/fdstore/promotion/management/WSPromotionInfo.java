package com.freshdirect.fdstore.promotion.management;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.fdstore.promotion.AssignedCustomerParam;
import com.freshdirect.fdstore.promotion.EnumDCPDContentType;
import com.freshdirect.fdstore.promotion.EnumPromotionStatus;
import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.core.PrimaryKey;

public class WSPromotionInfo extends ModelSupport {
	private String promotionCode; 
	private String name;
	private Date effectiveDate;
	private String zoneCode;
	private String startTime;
	private String endTime;
	private double discount;
	private EnumPromotionStatus status;
	public String getPromotionCode() {
		return promotionCode;
	}
	public void setPromotionCode(String promotionCode) {
		this.promotionCode = promotionCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public String getZoneCode() {
		return zoneCode;
	}
	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public EnumPromotionStatus getStatus() {
		return status;
	}
	public void setStatus(EnumPromotionStatus status) {
		this.status = status;
	}
	
}
