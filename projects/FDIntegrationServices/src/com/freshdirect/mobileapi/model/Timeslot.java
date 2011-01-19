package com.freshdirect.mobileapi.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.delivery.EnumReservationType;
import com.freshdirect.delivery.model.DlvZoneModel;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.FDZoneNotFoundException;
import com.freshdirect.fdstore.util.TimeslotLogic;
import com.freshdirect.framework.util.log.LoggerFactory;

public class Timeslot {

    private static Category LOGGER = LoggerFactory.getInstance(Timeslot.class);

    private DlvZoneModel zoneModel = null;

    private boolean chefTableUser = false;

    private int availCapacity = 0;

    private int normalAvailCapacity = 0;

    private boolean chefsTable = false;
        
    public static List<Timeslot> wrap(List<FDTimeslot> slots, boolean chefTableUser) {
        List<Timeslot> result = new ArrayList<Timeslot>();
        for (FDTimeslot slot : slots) {
            result.add(wrap(slot, chefTableUser));
        }
        return result;
    }

    /**
     * @param slot
     * @param chefTableUser
     * @return
     */
    public static Timeslot wrap(FDTimeslot slot, boolean chefTableUser) {
        FDDeliveryManager deliveryManager = FDDeliveryManager.getInstance();
        Timeslot newInstance = new Timeslot();
        newInstance.slot = slot;
        newInstance.chefTableUser = chefTableUser;

        try {
            newInstance.zoneModel = deliveryManager.findZoneById(slot.getZoneId());
        } catch (FDResourceException e) {
            LOGGER.warn("FDResourceException while trying to fetch zoneModel.", e);
        } catch (FDZoneNotFoundException e) {
            LOGGER.warn("FDZoneNotFoundException while trying to fetch zoneModel.", e);
        }

        //Since we're doing availability calculation on instantiation, instead of "gets". there are chance of
        //steal data

        Date now = new Date();
        if (newInstance.zoneModel != null) {
            if (chefTableUser) {
                newInstance.availCapacity = TimeslotLogic.getAvailableCapacity(slot.getDlvTimeslot(), now, TimeslotLogic.PAGE_CHEFSTABLE,
                        newInstance.zoneModel.getCtReleaseTime()) ? 1 : 0;
            } else {
                newInstance.availCapacity = TimeslotLogic.getAvailableCapacity(slot.getDlvTimeslot(), now, TimeslotLogic.PAGE_NORMAL,
                        newInstance.zoneModel.getCtReleaseTime()) ? 1 : 0;
            }
            newInstance.normalAvailCapacity = TimeslotLogic.getAvailableCapacity(slot.getDlvTimeslot(), now, TimeslotLogic.PAGE_NORMAL,
                    newInstance.zoneModel.getCtReleaseTime()) ? 1 : 0;
            newInstance.chefsTable = (newInstance.normalAvailCapacity < 1 && newInstance.availCapacity > 0);
        }
                
        return newInstance;
    }

    private FDTimeslot slot;

    public String getTimeslotId() {
        return slot.getTimeslotId();
    }

    public Date getBegDateTime() {
        return slot.getBegDateTime();
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
            full = (availCapacity < 1);
        } else {
            full = (normalAvailCapacity < 1);
        }
        return full;
    }

    public boolean isChefsTable() {
        /*
        DUP: Chef table determination appears to be in multiple places:
        1) in JSP i_delivery_slots.jsp 
        
        int availCapacity = TimeslotLogic.getAvailableCapacity(slot.getDlvTimeslot(), now, timeslot_page_type, zoneModel.getCtReleaseTime());
        int normalAvailCapacity = availCapacity;

        if (TimeslotLogic.PAGE_CHEFSTABLE == timeslot_page_type) {
             normalAvailCapacity = TimeslotLogic.getAvailableCapacity(slot.getDlvTimeslot(), now, TimeslotLogic.PAGE_NORMAL, zoneModel.getCtReleaseTime());
        }

        if (normalAvailCapacity < 1 && availCapacity > 0) {
             request.setAttribute("shownCTSlot", "TRUE");
        }
        
        2) in com.freshdirect.fdstore.FDTimeslot
         public int getChefsTableAvailable() {
                 return dlvTimeslot.getChefsTableAvailable();
         }
         
         This class duplicates to #1           
        */

        boolean isChefsTable = false;
        if (zoneModel != null) {
            //This below commented out portion is executed in constructor.
            //            int availCapacity = TimeslotLogic.getAvailableCapacity(slot.getDlvTimeslot(), now, TimeslotLogic.PAGE_CHEFSTABLE, zoneModel
            //                    .getCtReleaseTime());
            //            int normalAvailCapacity = TimeslotLogic.getAvailableCapacity(slot.getDlvTimeslot(), now, TimeslotLogic.PAGE_NORMAL, zoneModel
            //                    .getCtReleaseTime());
            isChefsTable = (normalAvailCapacity < 1 && availCapacity > 0);
        }

        return isChefsTable;
    }

    public static String getTimeslotReservationTypeCode() {
        return EnumReservationType.ONETIME_RESERVATION.getName();
    }

	public double getSteeringDiscount() {
		return slot.getSteeringDiscount();
	}

}
