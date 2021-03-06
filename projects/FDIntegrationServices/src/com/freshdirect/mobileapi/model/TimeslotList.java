package com.freshdirect.mobileapi.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.freshdirect.delivery.restriction.DlvRestrictionsList;
import com.freshdirect.fdlogistics.model.FDTimeslot;
import com.freshdirect.fdstore.util.FDTimeslotUtil;

public class TimeslotList {

    private FDTimeslotUtil target;

    /**
     * @param timeslotList
     * @return
     */
    public static TimeslotList wrap(FDTimeslotUtil timeslotList) {
        TimeslotList newInstance = new TimeslotList();
        newInstance.target = timeslotList;
        return newInstance;
    }

    /**
     * @param timeslotLists
     * @return
     */
    public static List<TimeslotList> wrap(List<FDTimeslotUtil> timeslotLists) {
        List<TimeslotList> lists = new ArrayList<TimeslotList>();
        for (FDTimeslotUtil timeslotList : timeslotLists) {
            lists.add(wrap(timeslotList));
        }
        return lists;
    }

    /**
     * @return
     */
    public List<Timeslot> getTimeslots(boolean isChefTableUser, SessionUser user ) {
        List<Timeslot> timeSlots = new ArrayList<Timeslot>();
        for (List<FDTimeslot> slots : (Collection<List<FDTimeslot>>) target.getTimeslots()) {
            for (FDTimeslot slot : slots) {
                Timeslot timeslotModel = Timeslot.wrap(slot, isChefTableUser, user);
                boolean isChefTableTimeslot = timeslotModel.isChefsTable();
                if (!isChefTableTimeslot || (isChefTableTimeslot && isChefTableUser)) {
                    timeSlots.add(timeslotModel);
                }
            }
        }
        Collections.sort(timeSlots,TIMESLOT_COMPARATOR);
        return timeSlots;
    }
    
    private final static Comparator<Timeslot> TIMESLOT_COMPARATOR = new Comparator<Timeslot>() {
		public int compare( Timeslot t1, Timeslot t2 ) {
			return t1.getBegDateTime().compareTo(t2.getBegDateTime());
		}
	};

    public boolean isKosherSlotAvailable(DlvRestrictionsList restrictions) {
        return target.isKosherSlotAvailable(restrictions);
    }

    public boolean hasCapacity() {
        return target.hasCapacity();
    }

    public Timeslot findTimeslotById(String timeslotId, boolean includeChefTable, SessionUser user) {
        Timeslot foundTimeslot = null;
        List<Timeslot> timeslots = getTimeslots(includeChefTable, user);
        for (Timeslot timeslot : timeslots) {
            if (timeslot.getTimeslotId().equals(timeslotId)) {
                foundTimeslot = timeslot;
                break;
            }
        }
        return foundTimeslot;
    }
}
