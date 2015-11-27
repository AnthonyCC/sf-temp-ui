package com.freshdirect.mobileapi.model.tagwrapper;

import javax.servlet.jsp.JspException;

import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.logistics.delivery.model.TimeslotContext;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.util.MobileApiProperties;
import com.freshdirect.webapp.taglib.fdstore.DeliveryTimeSlotTag;
import com.freshdirect.webapp.taglib.fdstore.Result;

public class DeliveryTimeSlotTagWrapper extends GetterTagWrapper {

	public static final String FORCE_ORDER = "forceorder";
    public static final String ADDRESS_CHANGE = "addressChange";

    public DeliveryTimeSlotTagWrapper(SessionUser user) {
        super(new DeliveryTimeSlotTag(), user);
    }

    public Result getDeliveryTimeSlotResult(ErpAddressModel address) throws FDException {
        ((DeliveryTimeSlotTag)wrapTarget).setAddress(address);
        ((DeliveryTimeSlotTag)wrapTarget).setDeliveryInfo(true);
        ((DeliveryTimeSlotTag)wrapTarget).setReturnSameDaySlots(showSameDaySlots());
        if(null !=getUser() && getUser().isEligibleForPreReservation()){
    		((DeliveryTimeSlotTag)wrapTarget).setTimeSlotContext(TimeslotContext.RESERVE_TIMESLOTS);
    	}else{
    		((DeliveryTimeSlotTag)wrapTarget).setTimeSlotContext(TimeslotContext.CHECK_AVAILABLE_TIMESLOTS);
    	}
        return (Result) getResult();
    }
    
    public Result getDeliveryTimeSlotResult(ErpAddressModel address, boolean isAuthenticated) throws FDException {
        ((DeliveryTimeSlotTag)wrapTarget).setAddress(address);
        ((DeliveryTimeSlotTag)wrapTarget).setDeliveryInfo(true);
        ((DeliveryTimeSlotTag)wrapTarget).setReturnSameDaySlots(showSameDaySlots());
        if(isAuthenticated){
        	((DeliveryTimeSlotTag)wrapTarget).setTimeSlotContext(TimeslotContext.CHECKOUT_TIMESLOTS);
        }else{
        	if(null !=getUser() && getUser().isEligibleForPreReservation()){
        		((DeliveryTimeSlotTag)wrapTarget).setTimeSlotContext(TimeslotContext.RESERVE_TIMESLOTS);
        	}else{
        		((DeliveryTimeSlotTag)wrapTarget).setTimeSlotContext(TimeslotContext.CHECK_AVAILABLE_TIMESLOTS);
        	}
        }        
        return (Result) getResult();
    }
    

    @Override
    protected Object getResult() throws FDException {
        try {
        	addExpectedRequestValues(new String[] { FORCE_ORDER, ADDRESS_CHANGE }, new String[] { FORCE_ORDER,
        			ADDRESS_CHANGE });//gets,sets
            
        	   
            wrapTarget.doStartTag();
        } catch (JspException e) {
            throw new FDException(e);
        }
        return pageContext.getAttribute(GET_RESULT);
    }
    
    private boolean showSameDaySlots() {
    	return !(this.getUser() != null 
    				&& this.getUser().isDpNewTcBlocking() 
    						&& !MobileApiProperties.isSameDayDpCompatible());
    }
}
