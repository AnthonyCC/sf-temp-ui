package com.freshdirect.mobileapi.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.freshdirect.delivery.restriction.DlvRestrictionsList;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.FDTimeslotList;

public class TimeslotList {

    private FDTimeslotList target;

    /**
     * @param timeslotList
     * @return
     */
    public static TimeslotList wrap(FDTimeslotList timeslotList) {
        TimeslotList newInstance = new TimeslotList();
        newInstance.target = timeslotList;
        return newInstance;
    }

    /**
     * @param timeslotLists
     * @return
     */
    public static List<TimeslotList> wrap(List<FDTimeslotList> timeslotLists) {
        List<TimeslotList> lists = new ArrayList<TimeslotList>();
        for (FDTimeslotList timeslotList : timeslotLists) {
            lists.add(wrap(timeslotList));
        }
        return lists;
    }

    /**
     * @return
     */
    public List<Timeslot> getTimeslots(boolean isChefTableUser) {
        List<Timeslot> timeSlots = new ArrayList<Timeslot>();
        for (List<FDTimeslot> slots : (Collection<List<FDTimeslot>>) target.getTimeslots()) {
            for (FDTimeslot slot : slots) {
                Timeslot timeslotModel = Timeslot.wrap(slot, isChefTableUser);
                boolean isChefTableTimeslot = timeslotModel.isChefsTable();
                if (!isChefTableTimeslot || (isChefTableTimeslot && isChefTableUser)) {
                    timeSlots.add(timeslotModel);
                }
            }
        }
        return timeSlots;
    }

    public boolean isKosherSlotAvailable(DlvRestrictionsList restrictions) {
        return target.isKosherSlotAvailable(restrictions);
    }

    public boolean hasCapacity() {
        return target.hasCapacity();
    }

    public Timeslot findTimeslotById(String timeslotId, boolean includeChefTable) {
        Timeslot foundTimeslot = null;
        List<Timeslot> timeslots = getTimeslots(includeChefTable);
        for (Timeslot timeslot : timeslots) {
            if (timeslot.getTimeslotId().equals(timeslotId)) {
                foundTimeslot = timeslot;
                break;
            }
        }
        return foundTimeslot;
    }
}
