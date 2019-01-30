package com.freshdirect.mobileapi.controller.data.response;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.freshdirect.mobileapi.controller.data.DateFormat;

public class Timeslot implements DateFormat {

    private boolean full = false;

    private String id;

    private Date start;

    private Date end;

    private Date cutoffDate;

    private Boolean isChefsTable;
    
    private double steeringDiscount;

    private Boolean ecoFriendly;

    private Boolean isDepot;
    
    private Boolean alcoholRestricted;
    
    private double premiumAmount;
    private boolean premiumSlot;
    private boolean sameDaySlot;
    
    private String minOrderMsg;
    private double minOrderAmt;
    private boolean minOrderMet;
    private double deliveryFee;
    private double promoDeliveryFee;

    private final SimpleDateFormat formatter = new SimpleDateFormat(STANDARDIZED_DATE_FORMAT);

	private boolean unavailable;
	
	private Boolean isEarlyAM;
	
	private boolean isMidWeekDlvPassApplicable; // Added for Midweek DeliveryPass

	
	public static List<Timeslot> initWithList(List<com.freshdirect.mobileapi.model.Timeslot> slots) {
        List<Timeslot> newInstances = new ArrayList<Timeslot>();
        for (com.freshdirect.mobileapi.model.Timeslot slot : slots) {
            newInstances.add(new Timeslot(slot));
        }
        return newInstances;
    }

    public Timeslot(com.freshdirect.mobileapi.model.Timeslot slot) {
        this.id = slot.getTimeslotId();
        this.start = slot.getBegDateTime();
        this.end = slot.getEndDateTime();
        this.cutoffDate = slot.getCutoffDateTime();
        this.isChefsTable = slot.isChefsTable();
        this.full = slot.isFull();
        
        this.steeringDiscount = slot.getSteeringDiscount();
        this.ecoFriendly = slot.isEcoFriendly();
        this.isDepot = slot.isDepot();
        this.alcoholRestricted = slot.isAlcoholRestricted();
        this.premiumAmount = slot.getPremiumAmount();
        this.premiumSlot = slot.isPremiumSlot();
        this.sameDaySlot = slot.isSameDaySlot();
        this.unavailable = slot.isUnavailable();
        
        this.minOrderMsg = slot.getMinOrderMsg();
        this.minOrderAmt = slot.getMinOrderAmt();
        this.minOrderMet = slot.isMinOrderMet();
        
        this.isEarlyAM = slot.isEarlyAM();
        
        this.deliveryFee = slot.getDeliveryFee();
        this.promoDeliveryFee = slot.getPromoDeliveryFee();
    
    }

    public Timeslot(Date rangeStart, Date rangeEnd, Date cutoff) {
        this.start = rangeStart;
        this.end = rangeEnd;
        this.cutoffDate = cutoff;
    }

    public String getStart() {
        return formatter.format(this.start);
    }

    public Date getStartDate() {
        return this.start;
    }
    
    public void setStart(Date start) {
        this.start = start;
    }

    @JsonSetter("start")
    public void setStart(String start) throws ParseException {
        this.start = formatter.parse(start);
    }

    public String getEnd() {
        return formatter.format(this.end);
    }
    
    public Date getEndDate() {
        return this.end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
    
    @JsonSetter("end")
    public void setEnd(String end) throws ParseException {
        this.end = formatter.parse(end);
    }

    public String getCutoffDate() {
        return formatter.format(this.cutoffDate);
    }
    
    public Date getCutoffDateDate() {
        return this.cutoffDate;
    }

    public void setCutoffDate(Date cutoffDate) {
        this.cutoffDate = cutoffDate;
    }

    public Boolean isChefsTable() {
        return isChefsTable;
    }

    public void setChefsTable(Boolean isChefsTable) {
        this.isChefsTable = isChefsTable;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isFull() {
        return full;
    }

	public double getSteeringDiscount() {
		return steeringDiscount;
	}

	public void setSteeringDiscount(double steeringDiscount) {
		this.steeringDiscount = steeringDiscount;
	}

	public Boolean getEcoFriendly() {
		return ecoFriendly;
}

	public void setEcoFriendly(Boolean ecoFriendly) {
		this.ecoFriendly = ecoFriendly;
	}

	public Boolean getIsDepot() {
		return isDepot;
	}

	public void setIsDepot(Boolean isDepot) {
		this.isDepot = isDepot;
	}

	public Boolean getAlcoholRestricted() {
		return alcoholRestricted;
	}

	public void setAlcoholRestricted(Boolean alcoholRestricted) {
		this.alcoholRestricted = alcoholRestricted;
	}

	public double getPremiumAmount() {
		return premiumAmount;
	}
	
	public boolean isPremiumSlot() {
		return premiumSlot;
	}
	public boolean isSameDaySlot() {
		return sameDaySlot;
	}
	
	public boolean isUnavailable() {
		return unavailable;
	}
	
	public String getMinOrderMsg() {
		return minOrderMsg;
	}
	
	public double getMinOrderAmt() {
		return minOrderAmt;
	}
	
	public boolean getMinOrderSlot() {
		return (minOrderAmt>0)?true:false;
	}
	
	public boolean isMinOrderMet() {
		return minOrderMet;
	}

	public Boolean getIsEarlyAM() {
		return isEarlyAM;
	}

	public void setIsEarlyAM(Boolean isEarlyAM) {
		this.isEarlyAM = isEarlyAM;
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

	public boolean isMidWeekDlvPassApplicable() {
		return isMidWeekDlvPassApplicable;
	}

	public void setMidWeekDlvPassApplicable(boolean isMidWeekDlvPassApplicable) {
		this.isMidWeekDlvPassApplicable = isMidWeekDlvPassApplicable;
	}

	
}
