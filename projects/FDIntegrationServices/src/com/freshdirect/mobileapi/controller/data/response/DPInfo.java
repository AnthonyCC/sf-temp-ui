package com.freshdirect.mobileapi.controller.data.response;

import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.freshdirect.customer.EnumPaymentMethodDefaultType;
import com.freshdirect.deliverypass.DeliveryPassType;
import com.freshdirect.deliverypass.EnumDPAutoRenewalType;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.mobileapi.controller.data.Message;

public class DPInfo extends Message {

	private String name;
	private String deliveryPassStatus;
	private String description;
	private String purchaseDate;
	private String expDate;
	private String hasAutoRenewDP;
	private String autoRenewalDate;
	private double pricePaid;
	private String originalOrderId;
//	private int usedCount;
//	private int remainingCount;
//	private int usablePassCount;
//	private int daysSinceDPExpiry=0;
//	private int daysToDPExpiry=0;
	private double dPSavings;
//	private boolean isFreeTrialRestricted;
//	private DeliveryPassType autoRenewDPType;
//	private int autoRenewUsablePassCount;
//	private double autoRenewPrice;
	private String defaultPaymentMethodPK;
	
	public String getDeliveryPassStatus() {
		return deliveryPassStatus;
	}
	public void setDeliveryPassStatus(EnumDlvPassStatus status) {
		this.deliveryPassStatus = status.getDisplayName();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getExpDate() {
		return expDate;
	}
	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}
	public double getPricePaid() {
		return pricePaid;
	}
	public void setPricePaid(double pricePaid) {
		this.pricePaid = pricePaid;
	}
	public String getOriginalOrderId() {
		return originalOrderId;
	}
	public void setOriginalOrderId(String originalOrderId) {
		this.originalOrderId = originalOrderId;
	}
//	public int getRemainingCount() {
//		return remainingCount;
//	}
//	public void setRemainingCount(int remainingCount) {
//		this.remainingCount = remainingCount;
//	}
//	public int getUsedCount() {
//		return usedCount;
//	}
//	public void setUsedCount(int usedCount) {
//		this.usedCount = usedCount;
//	}
//	public int getUsablePassCount() {
//		return usablePassCount;
//	}
//	public void setUsablePassCount(int usablePassCount) {
//		this.usablePassCount = usablePassCount;
//	}
//	public boolean isFreeTrialRestricted() {
//		return isFreeTrialRestricted;
//	}
//	public void setFreeTrialRestricted(boolean isFreeTrialRestricted) {
//		this.isFreeTrialRestricted = isFreeTrialRestricted;
//	}
//	public int getAutoRenewUsablePassCount() {
//		return autoRenewUsablePassCount;
//	}
//	public void setAutoRenewUsablePassCount(int autoRenewUsablePassCount) {
//		this.autoRenewUsablePassCount = autoRenewUsablePassCount;
//	}
//	public DeliveryPassType getAutoRenewDPType() {
//		return autoRenewDPType;
//	}
//	public void setAutoRenewDPType(DeliveryPassType autoRenewDPType) {
//		this.autoRenewDPType = autoRenewDPType;
//	}
//	public double getAutoRenewPrice() {
//		return autoRenewPrice;
//	}
//	public void setAutoRenewPrice(double autoRenewPrice) {
//		this.autoRenewPrice = autoRenewPrice;
//	}
//	public int getDaysSinceDPExpiry() {
//		return daysSinceDPExpiry;
//	}
//	public void setDaysSinceDPExpiry(int daysSinceDPExpiry) {
//		this.daysSinceDPExpiry = daysSinceDPExpiry;
//	}
//	public int getDaysToDPExpiry() {
//		return daysToDPExpiry;
//	}
//	public void setDaysToDPExpiry(int daysToDPExpiry) {
//		this.daysToDPExpiry = daysToDPExpiry;
//	}
	public double getdPSavings() {
		return dPSavings;
	}
	public void setdPSavings(double dPSavings) {
		this.dPSavings = dPSavings;
	}
	public String getPurchaseDate() {
		return purchaseDate;
	}
	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	
	public String getDefaultPaymentMethodPK() {
        return this.defaultPaymentMethodPK;
    }

    public void setDefaultPaymentMethodPK(String pmPK) {
        this.defaultPaymentMethodPK = pmPK;
    }
	public String getHasAutoRenewDP() {
		return hasAutoRenewDP;
	}
	public void setHasAutoRenewDP(String hasAutoRenewDP) {
		this.hasAutoRenewDP = hasAutoRenewDP;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAutoRenewalDate() {
		return autoRenewalDate;
	}
	public void setAutoRenewalDate(String autoRenewalDate) {
		this.autoRenewalDate = autoRenewalDate;
	}
}
