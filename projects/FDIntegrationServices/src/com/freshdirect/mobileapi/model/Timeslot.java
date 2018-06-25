package com.freshdirect.mobileapi.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.fdlogistics.model.FDTimeslot;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.logistics.delivery.model.EnumReservationType;
import com.freshdirect.storeapi.application.CmsManager;

public class Timeslot {

    private static Category LOGGER = LoggerFactory.getInstance(Timeslot.class);

    private boolean chefTableUser = false;

    private boolean chefsTable = false;
        
    public static List<Timeslot> wrap(List<FDTimeslot> slots, boolean chefTableUser, SessionUser user) {
        List<Timeslot> result = new ArrayList<Timeslot>();
        for (FDTimeslot slot : slots) {
            result.add(wrap(slot, chefTableUser, user));
        }
        return result;
    }

    /**
     * @param slot
     * @param chefTableUser
     * @return
     */
    public static Timeslot wrap(FDTimeslot slot, boolean chefTableUser, SessionUser user) {
        Timeslot newInstance = new Timeslot();
        newInstance.slot = slot;
        if(CmsManager.getInstance().getEStoreEnum().equals(EnumEStoreId.FDX) && user.getFDSessionUser().isDlvPassActive()){
        	newInstance.setPromoDeliveryFee(0);
        }
        newInstance.chefTableUser = chefTableUser;
        newInstance.chefsTable = (!slot.hasNormalAvailCapacity() && slot.hasAvailCTCapacity());   
        return newInstance;
    }

    private FDTimeslot slot;

    public String getTimeslotId() {
        return slot.getId();
    }

    public Date getBegDateTime() {
        return slot.getStartDateTime();
    }

    public Date getEndDateTime() {
        return slot.getEndDateTime();
    }

    public Date getCutoffDateTime() {
        return slot.getCutoffDateTime();
    }

    public boolean isFull() {
        boolean full = false;
        if (chefTableUser) {
            full = !(slot.hasAvailCTCapacity());
        } else {
            full = !(slot.hasNormalAvailCapacity());
        }
        return full;
    }

    public boolean isChefsTable() {
    	return !slot.hasNormalAvailCapacity() & slot.hasAvailCTCapacity();
    }

    public static String getTimeslotReservationTypeCode() {
        return EnumReservationType.ONETIME_RESERVATION.getName();
    }

	public double getSteeringDiscount() {
		return slot.getSteeringDiscount();
	}

	public double getPremiumAmount() {
		return slot.getPremiumAmount();
	}
	
	public boolean isPremiumSlot() {
		return slot.isPremiumSlot();
	}
	
	public boolean isUnavailable() {
		return slot.isUnavailable();
	}
	
	/* Eco Friendly timeslot*/
	public boolean isEcoFriendly() {
		return slot.isEcoFriendly();
	}
	
	/* Early AM TimeSlot */
	public boolean isEarlyAM(){
		return slot.isEarlyAM();
	}

	/* Building Favs timeslot*/
	public boolean isDepot() {
		return slot.isDepot();
	}
	
	/* Alcohol Delivery Restricted timeslot*/
	public boolean isAlcoholRestricted() {
		return slot.isAlcoholRestricted();
	}
	
	public String getMinOrderMsg() {
		return slot.getMinOrderMsg();
	}
	public double getMinOrderAmt() {
		return slot.getMinOrderAmt();
	}

	public boolean getMinOrderSlot() {
		return (slot.getMinOrderAmt()>0)?true:false;
	}

	public boolean isMinOrderMet() {
		return slot.isMinOrderMet();
	}
	
	public double getDeliveryFee() {
		return slot.getDeliveryFee();
	}

	public double getPromoDeliveryFee() {
		return slot.getPromoDeliveryFee();
	}
	
	public void setDeliveryFee(double dlvfee) {
		slot.setDeliveryFee(dlvfee);
	}

	public void setPromoDeliveryFee(double promoDlvFee) {
		slot.setPromoDeliveryFee(promoDlvFee);
	}
	
}
