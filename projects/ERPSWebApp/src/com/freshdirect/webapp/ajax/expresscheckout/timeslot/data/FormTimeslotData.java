package com.freshdirect.webapp.ajax.expresscheckout.timeslot.data;

import java.util.Date;
import java.util.List;

public class FormTimeslotData {

    private String id;
    private String dayOfWeek;
    private String dayOfMonth;
    private String month;
    private String year;
    private String timePeriod;
    private List<String> onOpenCoremetrics;
    private boolean showForceOrder;
    private boolean forceOrderEnabled;
    private boolean full;
	private Date startDate;
	private Date endDate;
	private String soFreq;
    private boolean isNewSO3;
	private String soCutOffFormattedDeliveryDate;
	private String soCutOffDeliveryTime;
    private boolean soActivated;
	private String shortDayOfWeek;
	private String soDeliveryDate;
    private Date cutoffDate;
    private String cutoffTime;
    private boolean isChefsTable;
    private double steeringDiscount;
    private boolean ecoFriendly;
    private boolean isDepot;
    private boolean alcoholRestricted;
    private Boolean isEarlyAM;
    private boolean unavailable;
    private double premiumAmount;
    private boolean premiumSlot;
    private String minOrderMsg;
    private double minOrderAmt;
    private boolean minOrderMet;
    private double deliveryFee;
    private double promoDeliveryFee;
	
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(String dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }

    public List<String> getOnOpenCoremetrics() {
        return onOpenCoremetrics;
    }

    public void setOnOpenCoremetrics(List<String> onOpenCoremetrics) {
        this.onOpenCoremetrics = onOpenCoremetrics;
    }

    public boolean isShowForceOrder() {
        return showForceOrder;
    }

    public void setShowForceOrder(boolean showForceOrder) {
        this.showForceOrder = showForceOrder;
    }

    
    public boolean isForceOrderEnabled() {
        return forceOrderEnabled;
    }

    
    public void setForceOrderEnabled(boolean forceOrderEnabled) {
        this.forceOrderEnabled = forceOrderEnabled;
    }
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getSoFreq() {
		return soFreq;
	}

	public void setSoFreq(String soFreq) {
		this.soFreq = soFreq;
	}

	public boolean isNewSO3() {
		return isNewSO3;
	}

	public void setNewSO3(boolean isNewSO3) {
		this.isNewSO3 = isNewSO3;
	}

	/**
	 * @return the soCutOffFormattedDeliveryDate
	 */
	public String getSoCutOffFormattedDeliveryDate() {
		return soCutOffFormattedDeliveryDate;
	}

	/**
	 * @param soCutOffFormattedDeliveryDate the soCutOffFormattedDeliveryDate to set
	 */
	public void setSoCutOffFormattedDeliveryDate(
			String soCutOffFormattedDeliveryDate) {
		this.soCutOffFormattedDeliveryDate = soCutOffFormattedDeliveryDate;
	}

	/**
	 * @return the soCutOffDeliveryTime
	 */
	public String getSoCutOffDeliveryTime() {
		return soCutOffDeliveryTime;
	}

	/**
	 * @param soCutOffDeliveryTime the soCutOffDeliveryTime to set
	 */
	public void setSoCutOffDeliveryTime(String soCutOffDeliveryTime) {
		this.soCutOffDeliveryTime = soCutOffDeliveryTime;
	}

	/**
	 * @return the soActivated
	 */
	public boolean isSoActivated() {
		return soActivated;
	}

	/**
	 * @param soActivated the soActivated to set
	 */
	public void setSoActivated(boolean soActivated) {
		this.soActivated = soActivated;
	}

	public String getShortDayOfWeek() {
		return shortDayOfWeek;
	}

	public void setShortDayOfWeek(String shortDayOfWeek) {
		this.shortDayOfWeek = shortDayOfWeek;
	}

	public String getSoDeliveryDate() {
		return soDeliveryDate;
	}

	public void setSoDeliveryDate(String soDeliveryDate) {
		this.soDeliveryDate = soDeliveryDate;
	}

    public boolean isFull() {
        return full;
    }

    public void setFull(boolean full) {
        this.full = full;
    }

    public Date getCutoffDate() {
        return cutoffDate;
    }

    public void setCutoffDate(Date cutoffDate) {
        this.cutoffDate = cutoffDate;
    }
    
    public String getCutoffTime() {
        return cutoffTime;
    }

    public void setCutoffTime(String cutoffTime) {
        this.cutoffTime = cutoffTime;
    }

    public boolean isChefsTable() {
        return isChefsTable;
    }

    public void setChefsTable(boolean isChefsTable) {
        this.isChefsTable = isChefsTable;
    }

    public double getSteeringDiscount() {
        return steeringDiscount;
    }

    public void setSteeringDiscount(double steeringDiscount) {
        this.steeringDiscount = steeringDiscount;
    }

    public boolean isEcoFriendly() {
        return ecoFriendly;
    }

    public void setEcoFriendly(boolean ecoFriendly) {
        this.ecoFriendly = ecoFriendly;
    }

    public boolean isDepot() {
        return isDepot;
    }

    public void setDepot(boolean isDepot) {
        this.isDepot = isDepot;
    }

    public boolean isAlcoholRestricted() {
        return alcoholRestricted;
    }

    public void setAlcoholRestricted(boolean alcoholRestricted) {
        this.alcoholRestricted = alcoholRestricted;
    }

    public Boolean getIsEarlyAM() {
        return isEarlyAM;
    }

    public void setIsEarlyAM(Boolean isEarlyAM) {
        this.isEarlyAM = isEarlyAM;
    }

    public boolean isUnavailable() {
        return unavailable;
    }

    public void setUnavailable(boolean unavailable) {
        this.unavailable = unavailable;
    }

    public double getPremiumAmount() {
        return premiumAmount;
    }

    public void setPremiumAmount(double premiumAmount) {
        this.premiumAmount = premiumAmount;
    }

    public boolean isPremiumSlot() {
        return premiumSlot;
    }

    public void setPremiumSlot(boolean premiumSlot) {
        this.premiumSlot = premiumSlot;
    }

    public String getMinOrderMsg() {
        return minOrderMsg;
    }

    public void setMinOrderMsg(String minOrderMsg) {
        this.minOrderMsg = minOrderMsg;
    }

    public double getMinOrderAmt() {
        return minOrderAmt;
    }

    public void setMinOrderAmt(double minOrderAmt) {
        this.minOrderAmt = minOrderAmt;
    }

    public boolean isMinOrderMet() {
        return minOrderMet;
    }

    public void setMinOrderMet(boolean minOrderMet) {
        this.minOrderMet = minOrderMet;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public double getPromoDeliveryFee() {
        return promoDeliveryFee;
    }

    public void setPromoDeliveryFee(double promoDeliveryFee) {
        this.promoDeliveryFee = promoDeliveryFee;
    }

}
