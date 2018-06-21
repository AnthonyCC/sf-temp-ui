package com.freshdirect.fdstore.deliverypass;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.freshdirect.deliverypass.DeliveryPassType;
import com.freshdirect.deliverypass.DlvPassConstants;
import com.freshdirect.deliverypass.EnumDlvPassStatus;

public class FDUserDlvPassInfo implements Serializable{

	private static final long serialVersionUID = 6459935633407980835L;
	
	private EnumDlvPassStatus status;
	private String description;
	private DeliveryPassType type;
	private Date expDate;
	private Double amount;
	private String originalOrderId;
	private int remainingCount;
	private int usedCount;
	private int usablePassCount;
	private boolean isFreeTrialRestricted;
	private int autoRenewUsablePassCount;
	private DeliveryPassType autoRenewDPType;
	private double autoRenewPrice;
	private NumberFormat CURRENCY_FORMATTER=NumberFormat.getCurrencyInstance(Locale.US);
	private int daysSinceDPExpiry=0;
	private int daysToDPExpiry=0;
	private double dPSavings;
	private Date purchaseDate;
	
	public FDUserDlvPassInfo(@JsonProperty("status") EnumDlvPassStatus status,
			@JsonProperty("typePurchased") DeliveryPassType type, 
			@JsonProperty("description") String description,
			@JsonProperty("expDate") Date expDate,
			@JsonProperty("amount") Double amount,
			@JsonProperty("originalOrderId") String originalOrderId, @JsonProperty("remainingCount") int remCnt,
			@JsonProperty("usedCount") int usedCount, @JsonProperty("usablePassCount") int usablePassCount,
			@JsonProperty("freeTrialRestricted") boolean isFreeTrialRestricted,
			@JsonProperty("autoRenewUsablePassCount") int autoRenewUsablePassCount,
			@JsonProperty("autoRenewDPType") DeliveryPassType autoRenewDPType,
			@JsonProperty("autoRenewPrice") double autoRenewPrice,
			@JsonProperty("purchaseDate") Date purchaseDate) {
		super();
		this.status = status;
		this.type = type;
		this.description = description;
		this.expDate = expDate;
		this.amount = amount;
		this.originalOrderId = originalOrderId;
		this.remainingCount = remCnt;
		this.usedCount = usedCount;
		this.usablePassCount=usablePassCount;
		this.isFreeTrialRestricted=isFreeTrialRestricted;
		this.autoRenewUsablePassCount=autoRenewUsablePassCount;
		this.autoRenewDPType=autoRenewDPType;
		this.autoRenewPrice=autoRenewPrice;
		this.purchaseDate = purchaseDate;
	}

	public int getUsablePassCount() {
		return usablePassCount;
	}
	
	public void setUsablePassCount(int _usablePassCount) {
		usablePassCount=_usablePassCount;
	}

	public Date getExpDate() {
		return expDate;
	}


	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public boolean isUnlimited() {
		if(type != null) {
			return type.isUnlimited();	
		}
		return false;
	}


	public EnumDlvPassStatus getStatus() {
		return status;
	}
	
	public void setStatus(EnumDlvPassStatus status) {
		this.status = status;
	}
	
	public String getOriginalOrderId() {
		return originalOrderId;
	}

	public int getRemainingCount() {
		return remainingCount;
	}

	public int getUsedCount() {
		return usedCount;
	}
	
	public DeliveryPassType getTypePurchased(){
		return type;
	}
	
	public boolean isFreeTrialRestricted() {
		return isFreeTrialRestricted;
	}
	
	public void setIsFreeTrialRestricted(boolean isFreeTrialRestricted) {
		this.isFreeTrialRestricted=isFreeTrialRestricted;
	}
	
	public int getAutoRenewUsablePassCount() {
		return autoRenewUsablePassCount;
	}
	
	public void setAutoRenewUsablePassCount(int _autoRenewUsablePassCount) {
		autoRenewUsablePassCount=_autoRenewUsablePassCount;
	}
	
	public double getAutoRenewPrice() {
		return autoRenewPrice;
	}
	
	public String getAutoRenewPriceAsText() {
		return CURRENCY_FORMATTER.format(autoRenewPrice);
	}
	
	public DeliveryPassType getAutoRenewDPType() {
		return autoRenewDPType;
	}
	
	public String getAutoRenewDPTerm() {
		if (autoRenewDPType == null) return null;

		String name=autoRenewDPType.getName();
		
		if(name.indexOf(DlvPassConstants.UNLIMITED)!=-1) {
			int months=autoRenewDPType.getDuration()/30;
			
			return DeliveryPassUtil.getAsText(months)+" months";
			//return name.substring(0,name.indexOf(DlvPassConstants.UNLIMITED)).trim().toLowerCase();
			
		} else {
			return name;
		}
	}

	public void setDaysSinceDPExpiry(int _daysSinceDPExpiry) {
		this.daysSinceDPExpiry=_daysSinceDPExpiry;
	}
	
	public int getDaysSinceDPExpiry() {
		return daysSinceDPExpiry;
	}
	
	public void setDaysToDPExpiry(int _daysToDPExpiry) {
		this.daysToDPExpiry=_daysToDPExpiry;
	}
	
	public int getDaysToDPExpiry() {
		return daysToDPExpiry;
	}

	public double getDPSavings() {
		return dPSavings;
	}
	
	public void setDPSavings(double savings) {
		dPSavings = savings;
}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	
	//For DeliveryPass Settings Page for Mid Week
	public boolean isMidweekPass() {
		return (null != getTypePurchased() && null != getTypePurchased().getEligibleDlvDays() && !getTypePurchased().getEligibleDlvDays().isEmpty()
				&& getTypePurchased().getEligibleDlvDays().size() < 7);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
}
