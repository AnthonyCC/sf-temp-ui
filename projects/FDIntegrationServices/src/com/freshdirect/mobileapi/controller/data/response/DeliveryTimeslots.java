package com.freshdirect.mobileapi.controller.data.response;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.TimeslotList;
import com.freshdirect.mobileapi.model.DeliveryTimeslots.TimeSlotCalculationResult;

public class DeliveryTimeslots extends CheckoutResponse {

    private Map<String, String> reservationTypes;

    public Map<String, String> getReservationTypes() {
        return reservationTypes;
    }

    public void setReservationTypes(Map<String, String> reservationTypes) {
        this.reservationTypes = reservationTypes;
    }

    public String getSelectedTimeslotId() {
        return selectedTimeslotId;
    }

    public void setSelectedTimeslotId(String selectedTimeslotId) {
        this.selectedTimeslotId = selectedTimeslotId;
    }

    private String selectedTimeslotId;

    private String reservedTimeslotId;
    
    private boolean showPremiumSlots;
    
    private boolean showDPTermsAndConditions;

    private Date sameDayCutoff;
    
    private boolean minOrderReqd;
    
    private ShipToAddress address;
    

    public String getReservedTimeslotId() {
        return reservedTimeslotId;
    }

    public void setReservedTimeslotId(String reservedTimeslotId) {
        this.reservedTimeslotId = reservedTimeslotId;
    }

    private List<Timeslot> timeSlots = new ArrayList<Timeslot>();

    private List<Restriction> restrictions = new ArrayList<Restriction>();

	private boolean showMinNotMetMessage;

    public List<Timeslot> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(List<Timeslot> timeSlots) {
        this.timeSlots = timeSlots;
    }

    public List<Restriction> getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(List<Restriction> restrictions) {
        this.restrictions = restrictions;
    }

    /**
     * @param result
     */
    public DeliveryTimeslots(TimeSlotCalculationResult result, SessionUser user) {
        List<TimeslotList> slotLists = result!=null?result.getTimeslotList():null;
        if(slotLists!=null){
	        for (TimeslotList slotList : slotLists) {
	            timeSlots.addAll(Timeslot.initWithList(slotList.getTimeslots(result.isUserChefTable(), user)));
	        }
        }
        if(user.getUserContext()!=null&&user.getUserContext().getStoreContext()!=null&&
        		user.getUserContext().getStoreContext().getEStoreId()!=null&&
        		user.getUserContext().getStoreContext().getEStoreId().equals(EnumEStoreId.FDX)){
        	timeSlots = combineUnavailableTS(timeSlots);
        }
        List<String> restrictionMessages = result!=null?result.getMessages():null;
        if(restrictionMessages != null) {
	        for (String restrictionMessage : restrictionMessages) {
	            this.restrictions.add(new Restriction(restrictionMessage));
	        }
        }
        this.selectedTimeslotId = result!=null?result.getPreselectedTimeslotId():null;
        this.reservedTimeslotId = result!=null?result.getReservationTimeslotId():null;
        this.showPremiumSlots = result!=null?result.isShowPremiumSlots():false;
        this.showDPTermsAndConditions = result!=null?result.isShowDPTermsAndConditions():false;
        this.sameDayCutoff = result!=null?result.getSameDayCutoff():null;
        this.minOrderReqd = result!=null?result.isMinOrderReqd():false;
        this.showMinNotMetMessage = result!=null?result.isShowMinNotMetMessage():false;
        
    }
    
    private List<Timeslot> combineUnavailableTS(List<Timeslot> ts) {
		List<Timeslot> ts2 = new ArrayList<Timeslot>();
        if(!ts.isEmpty()){
        	int loopnumber = 0;
            boolean foundunavailablets = false;
        	Timeslot lastunavailabletsfound = ts.get(loopnumber);
	        for(;loopnumber<ts.size();loopnumber++){
	        	if(ts.get(loopnumber).isFull()||ts.get(loopnumber).isUnavailable()){
	        		if(!foundunavailablets){
	        			foundunavailablets = true;
	        			lastunavailabletsfound = ts.get(loopnumber);
	        		}else if(lastunavailabletsfound.getStartDate().getDate()==ts.get(loopnumber).getStartDate().getDate() &&
	        					lastunavailabletsfound.getEndDate().getDate()==ts.get(loopnumber).getEndDate().getDate()) {
	        			lastunavailabletsfound.setEnd(ts.get(loopnumber).getEndDate());
	        			try {
							lastunavailabletsfound.setEnd(ts.get(loopnumber).getEnd());
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	        		}else{
	        			ts2.add(lastunavailabletsfound);
	        			lastunavailabletsfound = ts.get(loopnumber);
	        		}
	        	}else{
	        		if(foundunavailablets){
	        			ts2.add(lastunavailabletsfound);
	        			foundunavailablets = false;
	        			ts2.add(ts.get(loopnumber));
	        		}else{
	        			ts2.add(ts.get(loopnumber));
	        		}
        		}
	        }
	        if(foundunavailablets){
	        	ts2.add(lastunavailabletsfound);
	        }
        }
        return ts2;
	}

	public boolean isShowPremiumSlots() {
		return showPremiumSlots;
	}

	public void setShowPremiumSlots(boolean showPremiumSlots) {
		this.showPremiumSlots = showPremiumSlots;
	}

	public Date getSameDayCutoff() {
		return sameDayCutoff;
	}

	public void setSameDayCutoff(Date sameDayCutoff) {
		this.sameDayCutoff = sameDayCutoff;
	}

	public boolean isShowDPTermsAndConditions() {
		return showDPTermsAndConditions;
	}

	public void setShowDPTermsAndConditions(boolean showDPTermsAndConditions) {
		this.showDPTermsAndConditions = showDPTermsAndConditions;
	}

	public boolean isMinOrderReqd() {
		return minOrderReqd;
	}
	
	public boolean isShowMinNotMetMessage() {
		return showMinNotMetMessage;
	}

	
    public ShipToAddress getAddress() {
        return address;
    }
    
    public void setAddress(ShipToAddress address) {
        this.address = address;
    }
	
}
